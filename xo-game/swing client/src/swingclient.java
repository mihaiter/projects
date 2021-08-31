import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.net.URL;
import java.awt.event.*;

public class swingclient implements ActionListener, WindowListener, KeyListener {
	Socket socket;
	Scanner in;
	JButton connectButton, restartButton, stopButton, buttons[] = new JButton[9];
	JTextField tf;
	JPanel panel;
	PrintWriter pw;
	Boolean stop = false, ok = false;
	String message = "";
	int moves = 0;
	myglass glass=new myglass();

	ImageIcon icono, iconx, icon;

	public swingclient() {

		// TODO Auto-generated constructor stub

		initialise();
		JFrame f = new JFrame("O");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(panel);
		f.setGlassPane(glass);
		f.setResizable(false);
		glass.setVisible(true);
		f.pack();
		// f.setLocation(BorderLayout.NORTH);
		f.setVisible(true);
		
	}

	private void initialise() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		connectButton = new JButton("connect");
		restartButton = new JButton("restart");
		stopButton = new JButton("stop");
		icon = new ImageIcon(this.getClass().getClassLoader().getResource("redbuttonicon.png"));
		iconx = new ImageIcon(this.getClass().getClassLoader().getResource("X-icon.png"));
		icono = new ImageIcon(this.getClass().getClassLoader().getResource("O-icon.png"));
		JPanel buttonpanel = new JPanel();
		buttonpanel.setLayout(new GridLayout(3, 3));
		buttonpanel.setBackground(Color.decode("#FFFFCC"));
		buttonpanel.getInsets().set(20, 20, 20, 20);
		buttonpanel.setBorder(BorderFactory.createMatteBorder(20, 20, 20, 20, Color.green));
	
		//glass.setSize(buttonpanel.getSize());
		
		for (int i = 0; i < buttons.length; i++) {

			buttons[i] = new JButton(icon);
			buttons[i].setActionCommand(Integer.toString(i));
			buttons[i].setBorderPainted(false);
			buttons[i].setContentAreaFilled(false);
			buttonpanel.add(buttons[i]);

			buttons[i].addActionListener(this);
		}

		tf = new JTextField(30);
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		p.add(connectButton);
		p.add(restartButton);
		panel.add(p, BorderLayout.NORTH);
		panel.add(buttonpanel, BorderLayout.CENTER);
		//panel.add(tf, BorderLayout.SOUTH);
		connectButton.addActionListener(this);
		restartButton.addActionListener(this);
		tf.addKeyListener(this);
		glass.repaint();
	}
	class myglass extends JComponent{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public myglass(){
			System.out.println("inside glass");
		}
		protected void paintComponent(Graphics g ) {
//			System.out.println("painting");
			g.setColor(Color.blue);
			g.fillRect(19, 109, 247,10);
			g.fillRect(19, 167, 247,10);
			g.fillRect(96, 56, 10,174);
			g.fillRect(180, 56, 10,174);
			g.fillRect(17, 51, 249,10);
			g.fillRect(17, 225, 249,10);
			g.fillRect(17, 56, 10,174);
			g.fillRect(256, 56, 10,174);
			
		}	
	}
	public static void main(String[] args) {
		try {

			MetalLookAndFeel laf = new MetalLookAndFeel();
			laf.setCurrentTheme(new OceanTheme());
			UIManager.setLookAndFeel(laf);

		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new swingclient();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if (ae.getActionCommand().equalsIgnoreCase("connect")) {
			if (socket == null) {
				try {
					socket = new Socket(InetAddress.getLocalHost(), 9000);
					pw = new PrintWriter(socket.getOutputStream(), true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Thread t = new Thread() {
				public void run() {
					readmessages();
				}
			};
			t.start();
			connectButton.setEnabled(false);

		} else if (ae.getActionCommand().equalsIgnoreCase("restart")) {
			// if(socket==null) {connectButton.doClick();}
			pw.println("restart");

			for (int i = 0; i < 9; i++) {
				buttons[i].setIcon(icon);
				buttons[i].addActionListener(this);
			}

			ok = false;
			moves = 0;
			message = "32";
		} else if (Integer.parseInt(ae.getActionCommand()) >= 0 && Integer.parseInt(ae.getActionCommand()) <= 9) {
			if (ok && moves <= 3) {
				ok = false;
				int i = Integer.parseInt(ae.getActionCommand());

				if (i >= 0 && i <= 9) {
					moves++;
					if (socket == null) {
						connectButton.doClick();
					}
					buttons[i].setIcon(icono);
					buttons[i].removeActionListener(this);
					pw.println("button" + ae.getActionCommand());
					pw.println("moves" + moves);
					// System.out.println("client moves= "+moves);
				}
			}
		}
	}

	void readmessages() {
		
		try {
			if(socket==null) {}else {
		Scanner sc= new Scanner(socket.getInputStream());
		while(true) {
			try {Thread.sleep(10);}catch(InterruptedException e) {e.printStackTrace();}
		String msg=sc.nextLine();
		if(msg.contains("server")) {ok=false;}
		else  if(msg.contains("client")) {ok=true;JOptionPane.showMessageDialog(panel, "you start");}
	
		if(msg!=message) {message=msg;
		if(message.contains("button")) {
		int i=Integer.parseInt(message.replace("button", ""));
	//System.out.println("client messsage"+message);
		if(i>=0&&i<=9) {
			buttons[i].setIcon(iconx);
			ok=true;
			buttons[i].removeActionListener(this);
			
		}
		}
			}
		}
		}
		}catch(IOException e) {e.printStackTrace();}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		{
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent ke) {
		// TODO Auto-generated method stub
		if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
			restartButton.doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
