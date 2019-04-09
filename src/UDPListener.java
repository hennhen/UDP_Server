import java.net.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ThreadLocalRandom;

public class UDPListener extends Thread{

	public DatagramSocket listeningSocket;
	public DatagramPacket receivePacket;
	
	public static byte[] receiveData;
	
	int localPort;
	
	/* Create a new socket to listen to */
	public UDPListener(int port) throws Exception {	
		
		receiveData = new byte[3];
		this.localPort = port;
		
		/* Try to bind to the port */
		try {
			listeningSocket = new DatagramSocket(port);
			
		} catch (BindException e) {	
			/* If binding failed, try again with a new port */
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
		
		/* Start to listen for packets */
		while (true) {
			try {
				listeningSocket.receive(receivePacket);
				receiveData = receivePacket.getData();
				
				/* Update the UI components */
				if(receiveData[1] == 0) {
					Manager.updateLabel(ControlWindow.lblDistance, "100+");
				} else {
					Manager.updateLabel(ControlWindow.lblDistance, Integer.toString(Manager.convertByteToInt(receiveData[1])));
				}
				Manager.updateLabel(ControlWindow.lblSpeed, Float.toString((float) (
						Manager.convertByteToInt(receiveData[2]) * 0.148148)));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
