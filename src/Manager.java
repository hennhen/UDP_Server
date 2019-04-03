import javax.swing.SwingUtilities;

/*
 * Processes the info from Listener, make decision
 * 
 * Need to block sending from key presses when too close
 * Check if far away enough, then give lock back to key presses
 * 
 * Sender has its own array of bytes, we control the car by calling set methods in processor, which is synchronized
 */

public class Manager extends Thread{
	
	UDPSender sender;
	UDPListener listener;
	
	public Manager(UDPSender sender, UDPListener listener){
		this.sender = sender;
		this.listener = listener;
	}
	
	public static int convertByte(byte in) {		// Fixes negative byte
		if(in < 0) {
			return 256 + in;
		} else {
			return in;
		}
	}
	
	public synchronized void setDir(byte dir) {
		UDPSender.dataOut[1] = dir;
	}
	
	public synchronized void setPwm(byte pwm) {
		UDPSender.dataOut[2] = pwm;
	}
	
	public synchronized void setAngle(byte angle) {
		UDPSender.dataOut[3] = angle;
	}
	
	public void run() {
		sender.start();		// Starts to send data
		listener.start();	// Starts to listen for packets
		
		// Calculation stuff
		while(true) {	// Constantly checking sensor data
			if(UDPListener.receiveData[1] < 50 && UDPListener.receiveData[1] > 0) {
				
			}
		}
		
		
	}
}
