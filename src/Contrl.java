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

public class Contrl extends JFrame implements KeyListener {
	
	// Declare the components;
	JLabel lblMotorStats;
	JLabel lblServoStats;
	
	static UDPServer UDP;
	
	static byte[] packet = new byte[3];			// PWM and servo steer
	
	static byte pwm;
	
	JTextField lblCarIP;
	JTextField lblCarPort;
	JTextField lblMyPort;
	JLabel lblMessage1;
	JLabel lblMessage2;
	JButton btnCreateSocket;
	JSlider PWMSlider;
	JLabel lblPWM;
	
	public static void main(String[] args) throws Exception {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Contrl frame = new Contrl();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
}

	/**
	 * Create the frame.
	 */
	public Contrl() {		// Constructor
		packet[0] = 69;
		addComponents();
		addEventListeners();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch((char) e.getKeyCode()) {
		case 'W':
		case 'S':
			lblMotorStats.setText("0");
			packet[1] = 0;
			break;
		case 'A':
		case 'D':
			lblServoStats.setText("0");
			packet[2] = 0;
			break;
	}
		try {
			System.out.println(packet[0]);
			System.out.println(packet[1]);
			System.out.println(packet[2]);
			UDP.send(packet);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	void addComponents() {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel lblMotorPwm = new JLabel("Motor PWM:");
		lblMotorPwm.setBounds(54, 165, 80, 27);
		getContentPane().add(lblMotorPwm);
		
		JLabel lblServoAngle = new JLabel("Servo Angle:");
		lblServoAngle.setBounds(179, 165, 94, 27);
		getContentPane().add(lblServoAngle);
		
		// Motor Status Label
		lblMotorStats = new JLabel("0");
		lblMotorStats.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblMotorStats.setBounds(93, 185, 100, 40);
		getContentPane().add(lblMotorStats);
		
		// Servo Status Label
		lblServoStats = new JLabel("0");
		lblServoStats.setBounds(209, 185, 80, 40);
		lblServoStats.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		getContentPane().add(lblServoStats);
		
		// User input for socket creation
		lblCarIP = new JTextField();
		lblCarIP.setBounds(63, 55, 130, 26);
		getContentPane().add(lblCarIP);
		lblCarIP.setColumns(10);
		
		lblMyPort = new JTextField();
		lblMyPort.setBounds(205, 22, 130, 26);
		getContentPane().add(lblMyPort);
		lblMyPort.setColumns(10);
		
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
		
		JLabel MY_PORT_TEXT = new JLabel("My Port:");
		MY_PORT_TEXT.setBounds(142, 27, 61, 16);
		getContentPane().add(MY_PORT_TEXT);
		
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
		btnCreateSocket.setBounds(159, 88, 143, 29);
		getContentPane().add(btnCreateSocket);
		
		lblPWM = new JLabel();
		lblPWM.setBounds(73, 256, 61, 16);
		getContentPane().add(lblPWM);
		
		PWMSlider = new JSlider();
		PWMSlider.setMaximum(126);
		PWMSlider.setValue(30);
		lblPWM.setText(Integer.toString(PWMSlider.getValue()));
		PWMSlider.setBounds(6, 221, 190, 29);
		getContentPane().add(PWMSlider);
		
		JButton btnFocus = new JButton("Drive");
		btnFocus.setBounds(159, 245, 117, 29);
		btnFocus.addKeyListener(this);
		btnFocus.setFocusable(true);
		btnFocus.requestFocus();
		getContentPane().add(btnFocus);
	}
	
	void addEventListeners() {
		btnCreateSocket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UDP.serverSocket.close();
				} catch (Exception e3) {
					e3.printStackTrace();
				}
				
				try {
					UDP = new UDPServer(Integer.parseInt(lblMyPort.getText()), lblCarIP.getText(), Integer.parseInt(lblCarPort.getText()));
					lblMessage1.setText("Socket Created!");
					lblMessage2.setText("Testing Connection...");
					lblMessage1.paintImmediately(lblMessage1.getVisibleRect());
					lblMessage2.paintImmediately(lblMessage2.getVisibleRect());
					
					if(UDP.testConnection()) {		// Connection testing is good
						lblMessage2.setText("Connected!");

					} else {
						lblMessage2.setText("Failed to connect, try again");
					}
					
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
		PWMSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lblPWM.setText(Integer.toString(PWMSlider.getValue()));
				pwm = (byte) PWMSlider.getValue();
			}
		});
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch((char) e.getKeyCode()) {
			case 'W':
				lblMotorStats.setText("30");
				packet[1] = pwm;
				break;
			case 'S':
				lblMotorStats.setText("-30");
				packet[1] = (byte) -pwm;
				break;
			case 'A':
				lblServoStats.setText("-30");
				packet[2] = -45;
				break;
			case 'D':
				lblServoStats.setText("30");
				packet[2] = 45;
				break;
		}
		try {
			System.out.println(packet[0]);
			System.out.println(packet[1]);
			System.out.println(packet[2]);
			UDP.send(packet);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
