package servertest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import java.io.*;
import java.sql.Timestamp;
@Aspect
public class logging {
	FileOutputStream fo;
	String file,path;
	PrintWriter out;
	@Pointcut("execution(* servertest.datareadDao.*(..)) ")
	private void selectAll() {}
	
	@Before("selectAll()")
	public void beforeAdvice() {
		System.out.println("before advice");
		try {
			
			fo=new FileOutputStream("log.txt",true);
			out= new PrintWriter(fo);
			System.out.println("file open");
			Timestamp tsp= new Timestamp(System.currentTimeMillis());
			out.println("before ADVICE"+tsp.toString() );//+this.getClass().getName());
			out.println("before advice");
			out.flush();
			//out.println(e.getMessage());
		}catch(Exception e) {
			out.append(e.getMessage());
		}
		finally {
			
			try{  out.close();fo.close();}catch(Exception ex) {ex.printStackTrace();}
		}
		
	}
	
	@After("selectAll()")
	public void afterAdvice() {
		System.out.println("after advice");
	}
}
