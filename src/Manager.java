import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Manager extends Thread{
	
	UDPSender sender;
	UDPListener listener;
	
	static boolean tooClose;
	
	public Manager(UDPSender sender, UDPListener listener){
		tooClose = false;
		
		this.sender = sender;
		this.listener = listener;
	}
	
	/* Converts signed byte in Java */
	public static int convertByteToInt(byte in) {
		if(in < 0) {
			return 256 + in;
		} else {
			return in;
		}
	}
	
	/* Updates a JLabel safely in another thread */
	public static void updateLabel(JLabel lbl, String str) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				lbl.setText(str);
			}
		});
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
		sender.start();
		listener.start();
		
		/* Constantly checking sensor data */
		while(true) {
			try {
				Thread.sleep((long) 0.1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			/* If the car is within 80 cm from a wall for the first time*/
			if(UDPListener.receiveData[1] < 80 && UDPListener.receiveData[1] > 0 && !tooClose) {
				
				/* If the car is going forward */
				if(UDPSender.dataOut[1] == 2){
					/* Reverse thrust until less than 2 pulses per interval is collected from the encoder (until it stops) */
					while(UDPListener.receiveData[2] > 2 && UDPListener.receiveData[1] < 80 && UDPListener.receiveData[1] > 0) {
						setDir((byte) 3);
						setPwm((byte) 200);
					}
				}
				/* Finally stop the car */
				setPwm((byte) 0);
				tooClose = true;
				updateLabel(ControlWindow.lblMessage2, "Too Close!");
				
			} else if (UDPListener.receiveData[1] > 80 || UDPListener.receiveData[1] == 0) {
				tooClose = false;
				updateLabel(ControlWindow.lblMessage2, "");
			}
		}
	}
}
