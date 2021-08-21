package servertest;

public class dataread {
	private Float temp;
	private long miliseconds;
	public Float getTemp() {
		return temp;
	}
	public dataread() {}
	public void setTemp(Float temp) {
		this.temp = temp;
	}
	public long getMiliseconds() {
		return miliseconds;
	}
	public void setMiliseconds(long miliseconds) {
		this.miliseconds = miliseconds;
	}
	public dataread(Float temp, long miliseconds) {
		
		this.temp = temp;
		this.miliseconds = miliseconds;
	}
	

}
