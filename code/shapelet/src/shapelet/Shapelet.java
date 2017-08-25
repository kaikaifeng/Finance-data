package shapelet;

import unsupervised.Support;

public class Shapelet {
	private double[] list;
	private String from;
	private int[] index;
	private boolean sorted = false;
	private double[] original;
	
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
	
	/*
	 * can use faster copy
	 */
	public void sortIndex(){
		if(!sorted){
			index = new int[list.length];
			double[] AList = new double[list.length + 1];
			original = new double[list.length];
			int[] AIndex = new int[AList.length];
			AList[0] = 0.0;
			AIndex[0] = 0;
			for(int i = 1; i < AIndex.length; i++){
				AIndex[i] = i;
				AList[i] = Math.abs(list[i - 1]);
				original[i - 1] = list[i - 1];
			}
			Support.outHeapSort(AList, AIndex);
			for(int i = 0; i < list.length; i++){
				index[i] = AIndex[i + 1] - 1;
				list[i] = original[index[i]];
			}
			sorted = true;
		}
	}
	
	public int[] getIndex(){
		return index;
	}
	
	public boolean isSorted(){
		return sorted;
	}
	
	public StringBuffer getStringBuffer(){
		StringBuffer stringBuffer = new StringBuffer();
		for (double d : original) {
			stringBuffer.append(d);
			stringBuffer.append(",");
		}
		return stringBuffer;
	}
}
