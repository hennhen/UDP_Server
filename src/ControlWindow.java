import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.*;
import java.util.Scanner;
import java.io.*;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class ControlWindow extends JFrame implements KeyListener {

	static UDPSender sender;
	static UDPListener listener;
	static Manager manager;

	static byte pwm;
	static byte angle;

	// Declare the components;
	JLabel lblMotorStats;
	JLabel lblServoStats;
	JLabel lblPWM;
	JTextField lblTxPort;
	JTextField lblRxPort;
	JTextField lblCarIP;
	JTextField lblCarPort;
	JLabel lblMessage1;
	JLabel lblMessage2;
	JButton btnCreateSocket;
	JSlider PWMSlider;
	JSlider angleSlider = new JSlider();
	JLabel lblAngle = new JLabel("30");
	JLabel DISTANCE_TEXT = new JLabel("Distance: ");
	static JLabel lblDistance = new JLabel("0");
	JLabel CM_TEXT = new JLabel("cm");

	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ControlWindow frame = new ControlWindow();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Constructor, Create the frame.
	public ControlWindow() {
		pwm = 30;
		angle = 30;
		
		initGUI();
		addListeners();
	}

	private void initGUI() {
		// Window
		setBounds(100, 100, 474, 352);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel lblMotorPwm = new JLabel("Motor PWM:");
		lblMotorPwm.setBounds(43, 157, 80, 27);
		getContentPane().add(lblMotorPwm);

		JLabel SERVO_ANGLE_TEXT = new JLabel("Servo Angle:");
		SERVO_ANGLE_TEXT.setHorizontalAlignment(SwingConstants.CENTER);
		SERVO_ANGLE_TEXT.setBounds(159, 157, 94, 27);
		getContentPane().add(SERVO_ANGLE_TEXT);

		// Motor Status Label
		lblMotorStats = new JLabel("0");
		lblMotorStats.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblMotorStats.setBounds(74, 173, 49, 40);
		getContentPane().add(lblMotorStats);

		// Servo Status Label
		lblServoStats = new JLabel("0");
		lblServoStats.setBounds(200, 173, 44, 40);
		lblServoStats.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		getContentPane().add(lblServoStats);

		// User input for socket creation
		lblCarIP = new JTextField();
		lblCarIP.setBounds(76, 55, 130, 26);
		getContentPane().add(lblCarIP);
		lblCarIP.setColumns(10);

		lblTxPort = new JTextField();
		lblTxPort.setBounds(76, 22, 130, 26);
		getContentPane().add(lblTxPort);
		lblTxPort.setColumns(10);

		lblCarPort = new JTextField();
		lblCarPort.setBounds(287, 55, 130, 26);
		getContentPane().add(lblCarPort);
		lblCarPort.setColumns(10);

		// Constant texts
		JLabel CAR_IP_TEXT = new JLabel("Car IP: ");
		CAR_IP_TEXT.setBounds(20, 60, 44, 16);
		getContentPane().add(CAR_IP_TEXT);

		JLabel CAR_PORT_TEXT = new JLabel("Car Port:");
		CAR_PORT_TEXT.setBounds(228, 60, 61, 16);
		getContentPane().add(CAR_PORT_TEXT);

		JLabel TX_PORT_TEXT = new JLabel("Tx Port:");
		TX_PORT_TEXT.setBounds(20, 27, 80, 16);
		getContentPane().add(TX_PORT_TEXT);

		// Other stuff
		lblMessage1 = new JLabel("");
		lblMessage1.setForeground(Color.RED);
		lblMessage1.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessage1.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		lblMessage1.setBounds(43, 129, 160, 16);
		getContentPane().add(lblMessage1);

		lblMessage2 = new JLabel("");
		lblMessage2.setForeground(Color.RED);
		lblMessage2.setBounds(244, 129, 176, 16);
		getContentPane().add(lblMessage2);

		btnCreateSocket = new JButton("Create Socket");
		getContentPane().add(btnCreateSocket);
		btnCreateSocket.setBounds(159, 88, 143, 29);

		lblPWM = new JLabel();
		lblPWM.setHorizontalAlignment(SwingConstants.CENTER);
		lblPWM.setBounds(56, 225, 44, 16);
		getContentPane().add(lblPWM);

		PWMSlider = new JSlider();
		PWMSlider.setMaximum(253);
		PWMSlider.setValue(30);
		lblPWM.setText(Integer.toString(PWMSlider.getValue()));
		PWMSlider.setBounds(13, 199, 124, 29);
		getContentPane().add(PWMSlider);

		JButton btnFocus = new JButton("Focus");
		btnFocus.setBounds(179, 243, 117, 29);
		btnFocus.addKeyListener(this);
		btnFocus.setFocusable(true);
		btnFocus.requestFocus();
		getContentPane().add(btnFocus);

		JLabel RX_PORT_TEXT = new JLabel("Rx Port: ");
		RX_PORT_TEXT.setBounds(228, 27, 61, 16);
		getContentPane().add(RX_PORT_TEXT);

		lblRxPort = new JTextField();
		lblRxPort.setBounds(287, 22, 130, 26);
		getContentPane().add(lblRxPort);
		lblRxPort.setColumns(10);
		
		angleSlider.setMinimum(5);
		angleSlider.setMaximum(45);
		angleSlider.setValue(30);
		angleSlider.setBounds(140, 199, 124, 29);
		getContentPane().add(angleSlider);

		lblAngle.setHorizontalAlignment(SwingConstants.CENTER);
		lblAngle.setBounds(182, 225, 49, 16);
		getContentPane().add(lblAngle);

		DISTANCE_TEXT.setBounds(331, 162, 86, 16);
		getContentPane().add(DISTANCE_TEXT);

		lblDistance.setHorizontalAlignment(SwingConstants.CENTER);
		lblDistance.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblDistance.setBounds(319, 184, 61, 29);
		getContentPane().add(lblDistance);

		CM_TEXT.setHorizontalAlignment(SwingConstants.CENTER);
		CM_TEXT.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		CM_TEXT.setBounds(359, 184, 34, 29);
		getContentPane().add(CM_TEXT);
	}

	private void addListeners() {
		btnCreateSocket.addActionListener(new ActionListener() { // Create button is pressed, try to create sender and receiver
			public void actionPerformed(ActionEvent e) {
				try {
					listener.listeningSocket.close();
					sender.sendingSocket.close();
				} catch (Exception e3) {
					System.out.println("Closing error, its ok");
				}

				try {
					// Create sender and receiver
					sender = new UDPSender(Integer.parseInt(lblTxPort.getText()), lblCarIP.getText(),
							Integer.parseInt(lblCarPort.getText()));
					listener = new UDPListener(Integer.parseInt(lblRxPort.getText()));
					manager = new Manager(sender, listener);
					manager.start(); // Also starts sender and listener

					lblMessage1.setText("Socket Created!");
					lblMessage1.paintImmediately(lblMessage1.getVisibleRect());

				} catch (NumberFormatException e1) {
					lblMessage1.setText("Input error");
					lblMessage2.setText("Try again");

				} catch (Exception e1) {
					lblMessage1.setText("Socket error");
					lblMessage2.setText("Try again");
				}
				btnCreateSocket.setText("Recreate Socket");
			}
		});
		
		angleSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lblAngle.setText(Integer.toString(angleSlider.getValue()));
				angle = (byte) angleSlider.getValue();
			}
		});
		
		PWMSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lblPWM.setText(Integer.toString(PWMSlider.getValue()));
				pwm = (byte) PWMSlider.getValue();
			}
		});
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch ((char) e.getKeyCode()) {
		case 'W':
			lblMotorStats.setText(Integer.toString(Manager.convertByte(pwm)));
			manager.setPwm(pwm);
			manager.setDir((byte) 2);
			break;
		case 'S':
			lblMotorStats.setText(Integer.toString(-Manager.convertByte(pwm)));
			manager.setPwm(pwm);
			manager.setDir((byte) 3);
			break;
		case 'A':
			lblServoStats.setText(Integer.toString(-angle));
			manager.setAngle((byte) -angle);
			break;
		case 'D':
			lblServoStats.setText(Integer.toString(angle));
			manager.setAngle(angle);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch ((char) e.getKeyCode()) {
		case 'W':
		case 'S':
			lblMotorStats.setText("0");
			manager.setPwm((byte) 0);
			break;
		case 'A':
		case 'D':
			lblServoStats.setText("0");
			manager.setAngle((byte) 0);
			break;
		}
		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {}
}
