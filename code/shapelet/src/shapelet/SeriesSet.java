package shapelet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SeriesSet {
	public static double epsilon = 0.00001;
	private ArrayList<Serie> series;
	private HashMap<Integer, ArrayList<Shapelet>> all;
	
	public SeriesSet(){
		series = new ArrayList<Serie>();
		all = new HashMap<Integer, ArrayList<Shapelet>>();
	}
	
	public SeriesSet(Collection<Serie> collection){
		series = new ArrayList<Serie>(collection);
	}
	
	public void addSerie(double[] list, int label, String name){
		series.add(new Serie(list, label, name));
	}
	
	public void addSerie(Serie serie){
		series.add(serie);
	}
	
	public ArrayList<Shapelet> generateShapelet(int length){
		ArrayList<Shapelet> shapelets = new ArrayList<Shapelet>();
		for (Serie serie: series) {
			/*double[] list = serie.getList();
			for(int i = 0; i <= list.length - length; i++){
				double[] let = new double[length];
				for(int j = 0; j < length; j++){
					let[j] = list[i + j];
				}
				shapelets.add(new Shapelet(ZNormalization(let)));
			}*/
			ZNormalizations(serie, length, shapelets);
		}
		return shapelets;
	}
	
	public ArrayList<Serie> getSeries(){
		return series;
	}
	
	public void generateAll(int min, int max){
		for(int i = min; i <= max; i++){
			if(!all.containsKey(new Integer(i))){
				all.put(new Integer(i), generateShapelet(i));
			}
		}
	}
	
	public ArrayList<Shapelet> getShapelets(int length){
		return all.get(new Integer(length));
	}
	
	private void ZNormalizations(Serie serie, int length, ArrayList<Shapelet> shapelets){
		double[] list = serie.getList();
		double[] let = null;
		double sumMu = 0.0;
		double sumSigma = 0.0;
		double mu = 0.0;
		double sigma = 0.0;
		for(int i = 0; i < length; i++){
			sumMu += list[i];
			sumSigma += list[i] * list[i];
		}
		mu = sumMu / length;
		sigma = sumSigma / length;
		sigma -= mu * mu;
		sigma = Math.sqrt(sigma + epsilon);
		let = new double[length];
		for(int i = 0; i < length; i++){
			let[i] = list[i];
			let[i] -= mu;
			let[i] /= sigma;
		}
		double[] part = new double[length];
		System.arraycopy(list, 0, part, 0, length);
		shapelets.add(new Shapelet(let, serie.getName(), part));
		for(int i = 0; i < list.length - length; i++){
			sumMu += list[i + length];
			sumMu -= list[i];
			sumSigma += list[i + length] * list[i + length];
			sumSigma -= list[i] * list[i];
			mu = sumMu / length;
			sigma = sumSigma / length;
			sigma -= mu * mu;
			sigma = Math.sqrt(sigma + epsilon);
			let = new double[length];
			for(int j = 0; j < length; j++){
				let[j] = list[i + j + 1];
				let[j] -= mu;
				let[j] /= sigma;
			}
			part = new double[length];
			System.arraycopy(list, i + 1, part, 0, length);
			shapelets.add(new Shapelet(let, serie.getName(), part));
		}
	}
	
	public static double[] ZNormalization(double[] list){
		double mu = 0.0;
		double sigma = 0.0;
		for(int i = 0 ;i < list.length; i++){
			mu += list[i];
			sigma += list[i] * list[i];
		}
		mu /= list.length;
		sigma /= list.length;
		sigma -= mu * mu;
		sigma = Math.sqrt(sigma + epsilon);
		for(int i = 0 ;i < list.length; i++){
			list[i] -= mu;
			list[i] /= sigma;
		}
		return list;
	}
	
	public int size(){
		return series.size();
	}
}
