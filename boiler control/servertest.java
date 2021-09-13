package servertest;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.SecurityManager.*;
import java.net.*;
import java.util.*;
import javax.swing.Timer;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class servertest {
	private static ServerSocket serverSocket;
	private static int PORT = 1235;
	static InetAddress hostname;
	static float temperature = 0;
	static Socket link = null;
	static Timer t;
	static protected ApplicationContext context;
	static protected datareadDao dread;
	static private BeanFactory bfact;
	static private Resource res;
	static protected boolean connected = false;
	servertest() {

		connected = false;
		System.out.println("opening port");
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("unnable ");
			System.exit(1);
		}
		do {
			handleClient();
		} while (true);
	}
	protected static void sendCommand(String command) {
		try {
			Scanner input = new Scanner(link.getInputStream());

			PrintWriter output = new PrintWriter(link.getOutputStream(), true);

			output.println(command);
		
			String message = "";

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private static void handleClient() {
		
		try {
			System.out.println("beans");
			 context= new ClassPathXmlApplicationContext("servertest/applicationContext.xml");
			 System.out.println("beans after");  
			 dread=(datareadDao)context.getBean("ddao");
			
	
			 
		}catch(Exception e) {System.out.println("xml not found");e.printStackTrace();}
		
		
		try {
		
			
			hostname = InetAddress.getLocalHost();
			System.out.println("host name=" + hostname);
			System.out.println("port = " + PORT);
			System.out.println("handling client");

			link = serverSocket.accept();
			connected=true;
			System.out.println("client connected");
			Scanner input = new Scanner(link.getInputStream());

			PrintWriter output = new PrintWriter(link.getOutputStream(), true);
			int nmessages = 0;
			String message = "";
			Scanner sc = new Scanner(System.in);
			boolean ok = true;
		
			output.println("beginning connection");
		
			
			
			ActionListener act = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					sendCommand("temperatura");
				}
			};
			t = new Timer(15000, act);
			(new Thread() {
				public void run() {
					t.start();

				}
			}).start();
			
			while (ok) {
				
				if (input.hasNext()) {
				
					message = input.nextLine();
					
					
					temperature = Float.parseFloat(message);
					 try {
				dread.saveData(new dataread(temperature,System.currentTimeMillis()));
					 }catch(Exception e) {e.printStackTrace();}
					
				}

				

				nmessages++;

				if (message.contains("close")) {
					ok = false;
				}
			}
			input.close();
			sc.close();
			output.close();
			connected=false;
			System.out.println("nr messages =" + nmessages);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				connected=false;
				System.out.println("closing connection");
				link.close();
			
			} catch (IOException e) {
				System.out.println("cannot disconnect" + e);
			}
			System.exit(1);
		}
	}
}
