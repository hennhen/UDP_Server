import java.net.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.SwingUtilities;

public class UDPListener extends Thread{

	public DatagramSocket listeningSocket;
	public DatagramPacket receivePacket;
	
	public static byte[] receiveData;
	
	int localPort;
	
	public UDPListener(int port) throws Exception {		// Create a new socket that constantly listens
		
		receiveData = new byte[3];
		this.localPort = port;
		try {
			listeningSocket = new DatagramSocket(port);
		} catch (BindException e) {		// Port binding error
			port = ThreadLocalRandom.current().nextInt(30000, 40000);
			listeningSocket = new DatagramSocket(port);
			System.out.println("Port error, new port is: " + port);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
	}
	
	public void run() {
		localPort = listeningSocket.getLocalPort();
		System.out.println(localPort);
		while (true) {		// Always be listening
			try {
				listeningSocket.receive(receivePacket);
				receiveData = receivePacket.getData();
				System.out.println("\n" + receiveData[0] + "\n"  + receiveData[1] + "\n" + receiveData[2]);
				
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						ControlWindow.lblDistance.setText(Integer.toString(Manager.convertByte(UDPListener.receiveData[1])));
						ControlWindow.lblDistance.setText(Float.toString((float) ((float)Manager.convertByte(UDPListener.receiveData[2]) * 0.148148)));
					}
				});
				
				/*
				System.out.println("Received packet from port: " + localPort);
				System.out.println(receiveData.length);
				for(int i = 0; i < receiveData.length; i++) {
					System.out.print(receiveData[i] + " ");
				}
				*/
				
				// Process */
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			UDPListener listener = new UDPListener(5432);
			listener.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
