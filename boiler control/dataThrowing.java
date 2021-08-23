package servertest;

import java.io.*;


public class dataThrowing {
	   /** 
	      * This is the method which I would like to execute
	      * before a selected method execution.
	   */
	   public void beforeAdvice(){
	      System.out.println("Going to setup student profile.");
	   }
	   
	   /** 
	      * This is the method which I would like to execute
	      * after a selected method execution.
	   */
	   public void afterAdvice(){
	      System.out.println("Student profile has been setup.");
	   }

	   /** 
	      * This is the method which I would like to execute
	      * when any method returns.
	   */
	   public void afterReturningAdvice(Object retVal) {
	      System.out.println("Returning:"+ retVal.toString() );
	   }

	   /**
	      * This is the method which I would like to execute
	      * if there is an exception raised.
	   */
	   public void AfterThrowingAdvice(IllegalArgumentException ex){
	      System.out.println("There has been an exception: " + ex.toString());   
	   }
	}

/*
import org.springframework.aop.ThrowsAdvice;
public class dataThrowing implements ThrowsAdvice{
	FileOutputStream fo;
	String file,path;
	PrintWriter out;
	public void  afterThrowing(Exception e) throws IOException {
		try {
			fo=new FileOutputStream("servertest/log.txt",true);
			out= new PrintWriter(fo);
			out.println(e.getMessage());
		}catch(Exception ex) {
			e.printStackTrace();
		}
		finally {
			out.close();
			fo.close();
		}
		// TODO Auto-generated constructor stub
	}

}
*/
