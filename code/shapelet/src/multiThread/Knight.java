package multiThread;

import java.util.ArrayList;

import multiThread.Knights.Name;
import shapelet.Serie;
import shapelet.SeriesSet;
import shapelet.Shapelet;

public class Knight implements Runnable{
	private SeriesSet seriesSet;
	private int startNumber;
	private int endNumber;
	private Knights.Name name;
	
	public Knight(SeriesSet seriesSet, int startNumber, int endNumber) {
		this.seriesSet = seriesSet;
		this.startNumber = startNumber;
		this.endNumber = endNumber;
	}
	
	public void setStartNumber(int startNumber){
		this.startNumber = startNumber;
	}
	
	public int getStartNumber() {
		return startNumber;
	}
	
	void setEndNumber(int endNumber) {
		this.endNumber = endNumber;
	}
	
	public int getEndNumber() {
		return endNumber;
	}
	
	public void setSeriesSet(SeriesSet seriesSet) {
		this.seriesSet = seriesSet;
	}
	
	public SeriesSet getSeriesSet() {
		return seriesSet;
	}
	
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		long sum = 0;
		for(int i = startNumber; i <= endNumber; i++){
			ArrayList<Shapelet> shapelets = seriesSet.generateShapelet(i);
			for (Shapelet shapelet : shapelets) {
				shapelet.sortIndex();
				for (Serie serie : seriesSet.getSeries()) {
					serie.subsequenceDist(shapelet);
					sum++;
				}
			}
		}
		System.out.println("sum: " + sum);
		System.out.println("time: " + (System.currentTimeMillis() - start));
	}
	
	public void setName(Name name) {
		this.name = name;
	}
	
	public Name getName() {
		return name;
	}
}
