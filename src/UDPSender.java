import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPSender extends Thread{
	
	public DatagramSocket sendingSocket;
	public DatagramPacket sendingPacket;
	
	int localPort;
	InetAddress targetIP;
	int targetPort;
	
	public static byte[] dataOut;
	
	public UDPSender(int localPort, String targetIP, int targetPort) {
		
		dataOut = new byte[4];		// Initialize bytes
		dataOut[0] = 1;
		dataOut[1] = 2;
		dataOut[2] = 0;
		dataOut[3] = 0;
		this.localPort = localPort;
		this.targetPort = targetPort;
		
		try {		// Try to format IP
			this.targetIP = InetAddress.getByName(targetIP);
		} catch (UnknownHostException e) {
			System.out.println("IP Error");
			e.printStackTrace();
		}
		
		try {		// Try creating a new socket
			sendingSocket = new DatagramSocket(localPort);
		} catch (SocketException e) {
			System.out.println("Socket error");
			e.printStackTrace();
		}
		
		sendingPacket = new DatagramPacket(dataOut, dataOut.length, this.targetIP, targetPort);
		System.out.println("Created socket: " + "Target: " + targetIP + ":" + targetPort + ", sending from " + localPort);
	}
	
	public void run() {
		while (true) {		// Send data continuously
			sendingPacket.setData(dataOut);
			
			try {
				sendingSocket.send(sendingPacket);
				
				System.out.println(dataOut[0]);
				System.out.println(dataOut[1]);
				System.out.println(dataOut[2]);
				System.out.println(dataOut[3]);
				
			} catch (IOException e) {
				System.out.println("sending error");
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
