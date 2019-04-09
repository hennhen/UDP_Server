import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPSender extends Thread{
	
	public DatagramSocket sendingSocket;
	public DatagramPacket sendingPacket;
	
	int localPort;					// Port to send from
	InetAddress targetIP;
	int targetPort;
	
	public static byte[] dataOut;
	
	/* Create a new socket to send from */
	public UDPSender(int localPort, String targetIP, int targetPort) {
		
		dataOut = new byte[4];
		dataOut[0] = 1;				// Lead byte to sync data reception
		dataOut[1] = 2;				// Direction (2, forward; 3, backward)
		dataOut[2] = 0;				// PWM
		dataOut[3] = 0;				// Servo angle
		
		this.localPort = localPort;	
		this.targetPort = targetPort;
		
		/* Format IP */
		try { 
			this.targetIP = InetAddress.getByName(targetIP);
			
		} catch (UnknownHostException e) {
			System.out.println("IP Error");
			e.printStackTrace();
		}
		
		/* Create a new socket to send from */
		try {
			sendingSocket = new DatagramSocket(localPort);
			
		} catch (SocketException e) {
			System.out.println("Socket error");
			e.printStackTrace();
		}
		
		/* Create outgoing packet */
		sendingPacket = new DatagramPacket(dataOut, dataOut.length, this.targetIP, targetPort);
		sendingPacket.setData(dataOut);
		
		System.out.println("Created socket: " + "Target: " + targetIP + ":" + targetPort + ", sending from " + localPort);
	}
	
	public void run() {
		/* Send data continuously */
		while (true) {
			try {
				sendingSocket.send(sendingPacket);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			/* Wait for 10 ms between sending the next packet */
			try {
				Thread.sleep(10);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
