import java.io.*;
import java.net.*;
import java.util.Scanner;

public class UDPServer {

	DatagramSocket serverSocket; // Server socket
	

	DatagramPacket receivePacket;
	DatagramPacket sendPacket;

	byte[] receiveData = new byte[10];
	byte[] sendData = new byte[3];
	
	String in;

	public UDPServer(int port, String targetIP, int targetPort) throws Exception { // Constructor. Sets up socket and port number

		serverSocket = new DatagramSocket(port);

		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(targetIP), targetPort);
	}

	// ------ Functions -----

	public boolean receive() throws Exception {
		try {
			serverSocket.receive(receivePacket);
			in = new String(receivePacket.getData(), "UTF-8");
			System.out.println("received " + in);
			return true;

		} catch (SocketTimeoutException e) {
			return false;
		}
	}

	public void send(byte[] toSend) throws IOException{
		sendPacket.setData(toSend);
		serverSocket.send(sendPacket);
	}
	
	public boolean testConnection() throws Exception {					// Test by trying to send and receive packet of 2 bytes of 99 for 100 times
		sendData[0] = 46;
		sendData[1] = 99;
		sendData[2] = 99;
		sendPacket.setData(sendData);
		for(int i = 0; i < 100; i++) {		
			System.out.println(i);
			serverSocket.send(sendPacket);								// Send a packet with 2 bytes of 99
			if(receive()) {												// If a packet is received
				System.out.println("RECEIVED");
				receiveData = receivePacket.getData();					// Read the packet
				System.out.println(receiveData[0] + receiveData[1]);
				if(receiveData[0] == 99 && receiveData[1] == 99) {		// If the packet have 2 bytes of 99 then test is success
					System.out.println("YAY");
					return true;
				}
			}
		}
		return false;
	}
}