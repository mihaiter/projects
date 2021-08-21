package servertest;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import java.lang.*;


import javax.swing.JPanel;

public class drawPanel extends JPanel {
	private Dimension d;
	private static ApplicationContext context=new ClassPathXmlApplicationContext("servertest/applicationContext.xml");
	private static datareadDao 	databean= (datareadDao)context.getBean("ddao");
	public drawPanel(Dimension d) {
		this.d=d;
		
	}
	private void drawGraphic(Graphics2D g) {
		List<dataread> data= databean.getAllDataRowMApper();
		dataread first,last;
		first=data.get(1);
		last=data.get(data.size()-1);
		System.out.println(first.getMiliseconds());
		System.out.println(last.getMiliseconds());
		
		long interval=last.getMiliseconds()-first.getMiliseconds();
		for(dataread da:data) {
//			System.out.println("interval :"+interval);
		//	long value=(getminutes( da.getMiliseconds())-getminutes(first.getMiliseconds()))/(getminutes(last.getMiliseconds())
	//			-getminutes(first.getMiliseconds()))*((long)d.width)+5;
			long l=(da.getMiliseconds()-first.getMiliseconds());
			long r=(l*(long)60)/interval+(long )20;
		//	float f= (float)value;
//			System.out.println(l);
			
			g.drawString(".",r,
					(da.getTemp()-20)*(d.height-10)/60 +20);
			
		}
	}
	
	private long getminutes(long milli) {
		long sec=milli/1000;
		long min= (sec/60)%60;
		return min;
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.red);
		g2.drawLine(5, d.height-55, d.width, d.height-55);
		g2.drawLine(5, 5, 5, d.height-55);
		drawGraphic(g2);
	}
	
}
