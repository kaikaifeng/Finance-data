package unsupervised;

import shapelet.Shapelet;

public class UShapelet extends Shapelet{
	private int position;
	
	public UShapelet(double[] list) {
		super(list);
	}
	
	public UShapelet(double[] list, String from){
		super(list,from);
	}
	
	public UShapelet(double[] list, int position){
		super(list);
		this.position = position;
	}
	
	public UShapelet(double[] list, String from, int position){
		super(list, from);
		this.position = position;
	}
	
	public void setPosition(int position){
		this.position = position;
	}
	
	public int getPosition(){
		return position;
	}
}
