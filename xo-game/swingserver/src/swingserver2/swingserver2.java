package swingserver2;

import java.net.*;
import java.util.Random;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.awt.event.WindowListener;

import javax.swing.*;

public class swingserver2 implements ActionListener {
	ServerSocket server;
	JButton start, buttons[] = new JButton[9];
	JTextArea area;
	ImageIcon icon, iconx, icono;
	JPanel panel;
	Thread t1, t2, t3;
	Socket socket;
	BufferedReader in;
	Scanner sc;
	PrintWriter pw;
	String winner="";
	Boolean ok = true;
	Boolean locations[]= new Boolean[9];
	Random rand= new Random();
	Boolean istart=false;
	myglasspane glasspane= new myglasspane();
	int results[][]= new int[3][3];
	int clientmoves=0;
	int servermoves=0;
	static String message = "34";

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new swingserver2();
			}
		});
	}

	public swingserver2() {
		// TODO Auto-generated constructor stub
		JFrame frame = new JFrame("X");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialisegui();
		frame.setContentPane(panel);
		runapp();
		frame.setGlassPane(glasspane);
		glasspane.setVisible(true);
		frame.pack();
		
		frame.setVisible(true);

		frame.addWindowListener(new myWindowAdapter(this));

	}

	void runapp() {
		t1 = new Thread() {
			public void run() {
				startserver();
			}
		};
		t2 = new Thread() {
			public void run() {
				handleclients();
			}
		};
		t3 = new Thread() {
			public void run() {
				handleeachclient();
			}
		};
	}

	void initialisegui() {
		icon = new ImageIcon(this.getClass().getClassLoader().getResource("redbuttonicon.png"));
		icono = new ImageIcon(this.getClass().getClassLoader().getResource("O-icon.png"));
		iconx = new ImageIcon(this.getClass().getClassLoader().getResource("X-icon.png"));
		panel = new JPanel();
		istart= rand.nextBoolean();
		
		JPanel butpanel = new JPanel();
		butpanel.getInsets().set(20, 20, 20, 20);
		butpanel.setBackground(Color.decode("#FFFFCC"));
		butpanel.setBorder(BorderFactory.createMatteBorder(20, 20, 20, 20, Color.blue));
		panel.setLayout(new BorderLayout());
		butpanel.setLayout(new GridLayout(3, 3));
		start = makeButton("start server");
		area = new JTextArea(10, 10);
		panel.add(start, BorderLayout.NORTH);
		//panel.add(area, BorderLayout.CENTER);
		for (int i = 0; i < 9; i++) {
			buttons[i] = makeButton(Integer.toString(i), icon);
			butpanel.add(buttons[i]);
		}
		for(int i=0;i<9;i++) {
			locations[i]=false;
		}
		panel.add(butpanel, BorderLayout.CENTER);

	}
	class myglasspane extends JComponent{
		myglasspane(){}
		protected void paintComponent(Graphics g) {
			g.setColor(Color.GREEN);
			g.fillRect(19, 99, 247,10);
			g.fillRect(19, 158, 247,10);
			g.fillRect(96, 47, 10,174);
			g.fillRect(180, 47, 10,174);
			g.fillRect(17, 42, 249,10);
			g.fillRect(17, 214, 249,10);
			g.fillRect(17, 47, 10,174);
			g.fillRect(256, 47, 10,174);
			
		}
	}
	JButton makeButton(String name) {
		JButton b = new JButton(name);
		b.addActionListener(this);
		b.setActionCommand(name);
		return b;
	}

	JButton makeButton(String name, Icon icon) {
		JButton b = new JButton(icon);
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		b.setActionCommand(name);
	//	b.setBorder(null);
		b.addActionListener(this);
		return b;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equalsIgnoreCase("start server")) {t1.start();
		start.setEnabled(false);}
		else if(Integer.parseInt(ae.getActionCommand())>=0&&
				Integer.parseInt(ae.getActionCommand())<9){
			if (ok) {
				servermoves++;
				ok = false;
				message = ae.getActionCommand();

				(new Thread() {
					public void run() {
						printmessages();
					}
				}).start();
				buttons[Integer.parseInt(ae.getActionCommand())].setIcon(iconx);
				locations[Integer.parseInt(ae.getActionCommand())]=true;
				buttons[Integer.parseInt(ae.getActionCommand())].removeActionListener(this);
				prepareresults();
				if(searchcomb()) {JOptionPane.showMessageDialog(panel, winner+ " is wins");}
				if(clientmoves+servermoves==9) {JOptionPane.showMessageDialog(panel, "finished");}
				
			}
		}
		
		// TODO Auto-generated method stub
		
	}
	Boolean searchcomb() {
		Boolean win=false;
		for(int i=0;i<3;i++) {
			if(verticalsearch(i)==3) {win= true; winner="x";}
			if(verticalsearch(i)==-3){win= true;winner="o";}
			if(horisontalsearch(i)==3) {win= true;winner="x";}
			if(horisontalsearch(i)==-3){win= true;winner="o";}
			if(diagonalsearch(true)==3){win= true; winner="x";}
			if(diagonalsearch(true)==-3) {win= true;winner="o";}
			if(diagonalsearch(false)==3){win= true; winner="x";}
			if(diagonalsearch(false)==-3){win= true;winner="o";}
			
		}
		return win;
	}
	int diagonalsearch(Boolean ud) {
		int sum=0;
		if(ud) {
		for(int i=0;i<3;i++) {
			sum+=results[i][i];
		}}else
		{
			sum=results[0][2]+results[1][1]+results[2][0];
		}
		return sum;
	}
	int horisontalsearch(int index) {
		int sum=0;
		for(int i=0;i<3;i++) {
			sum+=results[i][index];
		}
		return sum;
	}
	int verticalsearch(int index) {
		int sum=0;
		for(int i=0;i<3;i++) {
			sum+=results[index][i];
		}
		return sum;
	}
	void prepareresults() {
		
		int index=0;
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				if(locations[index]) {
					
			if(buttons[index].getIcon()==icono) {results[i][j]=-1;}
			if(buttons[index].getIcon()==iconx) {results[i][j]=1;}
			
			}
				index++;
		//	System.out.print("  "+results[i][j]+"  ");
			}
		//	System.out.println(" ");
			
		}
		
	}
	void startserver() {
		area.append("starting server \n");
		try {
			server = new ServerSocket(9000);
			area.append("server started \n");
			t2.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void handleclients() {
		area.append("handling clients \n");
		while (true) {
			try {
				socket = server.accept();

				(new Thread() {
					public void run() {
						handleeachclient();
					}
				}).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void handleeachclient() {
		try {
			pw = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			sc = new Scanner(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		selectstart();
		area.append("handling each client\n");

		(new Thread() {
			public void run() {
				readmessages();

			}
		}).start();
		(new Thread() {
			public void run() {
				printmessages();
			}
		}).start();
	}
	void selectstart() {
		if(istart) {pw.println("server");ok=true;JOptionPane.showMessageDialog(panel, "you start");}
		else {pw.println("client");ok=false;}
	}
	void readmessages() {
		String messagetemp="";
		while (true) {
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {

				if (sc.hasNext()) {
					String s = sc.nextLine();
					
					if(messagetemp.equalsIgnoreCase(s)) {}
					else {
						messagetemp=s;
					//	if(clientmoves+servermoves==9) {JOptionPane.showMessageDialog(panel, "finished");}
				//		if(searchcomb()) {JOptionPane.showMessageDialog(panel, winner+ " is wins");}
					
					
					if (s.contains("button")) {
						String str= s.replace("button", "");
						ok = true;	
						int but=Integer.parseInt(str);
							buttons[but].setIcon(icono);
							locations[but]=true;
						
						buttons[but].removeActionListener(this);
					}
					else if(s.contains("moves")) {
						String str=s.replace("moves", "");
						clientmoves=Integer.parseInt(str);
						area.append(clientmoves+"\n");
						
					}
					
					if(s.contains("restart")) {
						restart();
						
					}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	void restart() {
		System.out.println("restarting server");
		for(int i=0;i<9;i++) {
			buttons[i].setIcon(icon);
			buttons[i].addActionListener(this);
		}
		istart= rand.nextBoolean();
		selectstart();
		for(int i=0;i<9;i++) {
			locations[i]=false;
		}
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				results[i][j]=0;
			}
		}
		clientmoves=0;
		 servermoves=0;
	}
	void printmessages() {
		String msg = "";
		while (true) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!message.equalsIgnoreCase(msg)) {
				msg = message;
				if (Integer.parseInt(message) >= 0 && Integer.parseInt(message) < 9) {
					pw.println("button"+message);
					// System.out.println("sent message ="+message);
				}
			}
		}
	}

	class myWindowAdapter extends WindowAdapter {
		swingserver2 swin;

		public myWindowAdapter(swingserver2 swin) {
			this.swin = swin;
		}

		public void WindowClosing(WindowEvent we) {
			try {
				if (server != null)
					server.close();
				if (socket != null)
					socket.close();
				System.out.println("server closed");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
