package shapelet;

public class Shapelet {
	private double[] list;
	private String from;
	
	public Shapelet(double[] list){
		this.list = list;
	}
	
	public Shapelet(double[] list, String from){
		this.list = list;
		this.from = from;
	}
	
	public void setList(double[] list){
		this.list = list;
	}
	
	public double[] getList(){
		return list;
	}
	
	public void setFrom(String from){
		this.from = from;
	}
	
	public String getFrom(){
		return from;
	}
}
