package servertest;

import java.awt.Color;
import java.awt.SystemTray;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;

import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.plaf.BorderUIResource;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class servergui implements ActionListener {
	private JFrame frame = new JFrame();
	private JLabel templabel = new JLabel("temperatura"),
			pompa1label = new JLabel("Pompa 1"),
			pompa2label = new JLabel("pompa2"),
			conexiune = new JLabel("Conexiune");
	private JLabel imagelabel = new JLabel();
	private ImageIcon icon1Green, on, off;
	private ImageIcon icon2Red;
	private JButton buttonpump1, buttonpump2;
	private JTextField temtf = new JTextField();
	private JTextArea tpa = new JTextArea("0");
	private Timer t, t1;
	private boolean red = true, pompa2 = false,pompa1=false;
	SystemTray tray;
	TrayIcon icon;
	JPanel panelButoane, panelGrafic = new JPanel();
	Dimension d = new Dimension();

	public servergui() {
		// TODO Auto-generated constructor stub
		JLabel templabel = new JLabel("temperatura");
		JTextField temtf = new JTextField("0");
		buttonpump1 = new JButton("read temperature ");
		buttonpump1.addActionListener(this);
		buttonpump2 = new JButton();
		buttonpump2.addActionListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panelButoane = new JPanel();
		GroupLayout gl = new GroupLayout(panelButoane);

		// panel.setLayout(new GroupLayout());
		// panel.add(templabel);
		// panel.add(temtf);
		Font font = new Font("Monospace", Font.BOLD, 28);
		tpa.setFont(font);

		tpa.setForeground(Color.RED);
		tpa.setBackground(Color.BLACK);
		// panel.add(tpa);
		icon1Green = new ImageIcon(this.getClass().getClassLoader()
				.getResource("servertest/led-green.png"));
		icon2Red = new ImageIcon(this.getClass().getClassLoader()
				.getResource("servertest/led-red.png"));
		on = new ImageIcon(this.getClass().getClassLoader()
				.getResource("servertest/on.png"));
		off = new ImageIcon(this.getClass().getClassLoader()
				.getResource("servertest/off.png"));
		// panel.add(buttonTemp);

		buttonpump1.setIcon(off);
		buttonpump1.setActionCommand("pompa1");
		buttonpump1.setText("");
		buttonpump1.setBorderPainted(false);
		buttonpump1.setContentAreaFilled(false);
		buttonpump1.setMultiClickThreshhold(500);
		buttonpump1.setRolloverEnabled(true);
		buttonpump1.setFocusPainted(false);

		buttonpump2.setIcon(off);
		buttonpump2.setActionCommand("pompa2");
		buttonpump2.setContentAreaFilled(false);
		buttonpump2.setFocusPainted(false);
		buttonpump2.setBorderPainted(false);

		// panel.add(buttonpump2);
		imagelabel.setIcon(icon2Red);
		// panel.add(imagelabel);

		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(templabel).addGap(5, 9, 10)
						.addComponent(tpa, 70, 70, 70).addGap(5, 9, 10)
						.addComponent(pompa1label).addComponent(buttonpump1))
				.addGap(5, 9, 10)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(conexiune).addComponent(imagelabel)
						.addGap(5, 9, 10).addComponent(pompa2label)
						.addGap(5, 9, 10).addComponent(buttonpump2)));
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup().addComponent(templabel)
						.addComponent(conexiune))
				.addGap(3, 5, 8)
				.addGroup(gl.createParallelGroup().addComponent(tpa, 35, 36, 36)
						.addComponent(imagelabel))
				.addGap(3, 5, 8)
				.addGroup(gl.createParallelGroup().addComponent(pompa1label)
						.addComponent(pompa2label))
				.addGap(3, 5, 8).addGroup(gl.createParallelGroup()
						.addComponent(buttonpump1).addComponent(buttonpump2)));
		// gl.linkSize(templabel,tpa,pompa1label,buttonpump1);
		// panel.setSize(800, 800);
		panelButoane.setLayout(gl);
		panelButoane.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
		panelGrafic.setBorder(BorderFactory.createLineBorder(Color.blue, 5));
		panelGrafic.setSize(300, 300);
		drawPanel dp= new drawPanel(panelGrafic.getSize());
		panelGrafic= dp;
		
		frame.add(panelButoane, BorderLayout.WEST);
		frame.add(panelGrafic, BorderLayout.CENTER);
		// frame.setSize(new Dimension(500,500));
		frame.setSize(new Dimension(600, 300));
		frame.setResizable(true);
		// frame.set
		frame.setVisible(true);
		if (!SystemTray.isSupported()) {
			System.out.println("not suported");
		} else {
			tray = SystemTray.getSystemTray();
			d = tray.getTrayIconSize();
		}

		// tpa.sett
		ActionListener updatetemp = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				updateTemp();
			}
		};
		t = new Timer(1000, updatetemp);
		(new Thread() {
			public void run() {
				t.start();
			}
		}).start();
		ActionListener updatetray = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				updatesystemTray();
			}
		};
		icon = new TrayIcon(getTrayImage());
		try {
			tray.add(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
		t1 = new Timer(15000, updatetray);
		(new Thread() {
			public void run() {
				t1.start();
			}
		}).start();
		ActionListener updateConnect= new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				checkConnected();
			}
		};
		Timer t2=new Timer(500, updateConnect);
		(new Thread() {
			public void run() {
				t2.start();
			}
		}).start();
		ActionListener updateGraphic= new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				updatePanelDrawing();
			}
		};
		Timer t3= new Timer(1000, updateGraphic);
		(new Thread() {public void run() {t3.start();}}).start();
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new servergui();
				(new Thread() {
					public void run() {
						servertest server = new servertest();
					}
				}).start();

			}
		});
	}
	void updatePanelDrawing() {
		panelGrafic.removeAll();
		drawPanel dp= new drawPanel(panelGrafic.getSize());
		panelGrafic= dp;
	}
	void updatesystemTray() {
		// this.
		icon.setImage(getTrayImage());

	}
	private void checkConnected() {
		ImageIcon icon = new ImageIcon();
		icon = servertest.connected == false ? icon2Red : icon1Green;
		imagelabel.setIcon(icon);
	}
	private BufferedImage getTrayImage() {
		BufferedImage image = new BufferedImage(d.width, d.height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		Float te = servertest.temperature;
		String text = Float.toString(servertest.temperature);
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, d.width, d.height);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (te <= 60) {
			g2.setColor(Color.green);
		} else if (te > 60) {
			g2.setColor(Color.red);
		}
		g2.setFont(new Font(Font.DIALOG, 0, 12));

		g2.drawString(text, 0, d.height - 2);
		return image;
	}
	void updateTemp() {
		// Font font=new Font("Monospace",Font.BOLD,28) ;
		// tpa.setFont(font);
		// tpa.setForeground(Color.RED);
		// tpa.setBackground(Color.BLACK);
		tpa.setText(Float.toString(servertest.temperature));
		// String text=Float.toHexString(servertest.temperature);
		// tray.add();
		// System.out.println("updating temp");
		// Graphics g= frame.getGraphics();
		Component c = panelButoane.getComponent(0);

		Image i = c.createImage(20, 20);
		i.getGraphics().setColor(Color.BLUE);
		i.getGraphics().drawString("0 0 ", 0, 0);

		// panel.add(c)
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if (ae.getActionCommand().equalsIgnoreCase("pompa1")) {
			pompa1=  !pompa1;
			ImageIcon  buttonicon = new ImageIcon();

			
			
			buttonicon = pompa1 == false ? on : off;
			String command = "";
			command = pompa1 == false ? "pomap1on" : "pompa1off";

			buttonpump1.setIcon(buttonicon);
			

			servertest.sendCommand(command);

			System.out.println("pump1 pressed");
		} else if (ae.getActionCommand().equalsIgnoreCase("pompa2")) {
			pompa2 = !pompa2;
			ImageIcon buttonicon = new ImageIcon();
			buttonicon = pompa2 == true ? off : on;
			buttonpump2.setIcon(buttonicon);
			String command = "";
			command = pompa2 == true ? "pompa2off" : "pompa2on";
			servertest.sendCommand(command);
		}
	}
}
