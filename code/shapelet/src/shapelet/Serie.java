package shapelet;

import java.util.ArrayList;
import java.util.function.DoubleToLongFunction;

public class Serie {
	private double[] list;
	private int label;
	private String name;
	
	public Serie(double[] list, int label, String name){
		this.list = list;
		this.label = label;
		this.name = name;
	}
	
	public void setList(double[] list){
		this.list = list;
	}
	
	public double[] getList(){
		return list;
	}
	
	public void setLabel(int label){
		this.label = label;
	}
	
	public int getLabel(){
		return label;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public double subsequenceDist(Shapelet shapelet){
		return 0;
	}
	
	private double dist(double[] F, double[] subsequence){
		return 0;
	}
	
	private double naiveDist(double[] F, double[] subsequence){
		//first, Z-normalization is needed
		//just Euclidean distance
		if(F.length != subsequence.length){
			return -1;
		}
		double[] normalizated = SeriesSet.ZNormalization(subsequence);
		double sum = 0.0;
		for(int i = 0; i < F.length; i++){
			sum += (F[i] - normalizated[i]) * (F[i] - normalizated[i]);
		}
		return Math.sqrt(sum);
	}
	
	public double naiveSubsequenceDist(Shapelet shapelet) {
		double[] F = shapelet.getList();
		double min = Double.MAX_VALUE;
		for(int i = 0; i <= list.length - F.length; i++){
			double[] subsequence = new double[F.length];
			for(int j = 0; j < F.length; j++){
				subsequence[j] = list[i + j];
			}
			double current = naiveDist(F, subsequence);
			if(current < min){
				min = current;
			}
		}
		return min;
	}
	
	private double betterDist(double[] F, int startPosition, double currentBest, double mu, double sigma){
		double sum = 0.0;
		for(int i = 0; i < F.length; i++){
			double normalizated = list[i + startPosition] - mu;
			normalizated /= sigma;
			sum += (F[i] - normalizated) * (F[i] - normalizated);
			if(sum > currentBest){
				break;
			}
		}
		return sum;
	}
	
	public double betterSubsequenceDist(Shapelet shapelet){
		double[] F = shapelet.getList();
		double sumMu = 0.0;
		double sumSigma = 0.0;
		double currentBest = Double.MAX_VALUE;
		for(int i = 0; i < F.length; i++){
			sumMu += list[i];
			sumSigma += list[i] * list[i];
		}
		double mu = sumMu / F.length;
		double sigma = sumMu / F.length;
		sigma -= mu * mu;
		double dist = betterDist(F, 0, currentBest, mu, sigma);
		if(dist < currentBest){
			currentBest = dist;
		}
		for(int i = 0; i < list.length - F.length; i++){
			sumMu += list[i + F.length];
			sumMu -= list[i];
			sumSigma += list[i + F.length] * list[i + F.length];
			sumSigma -= list[i] * list[i];
			mu = sumMu / F.length;
			sigma = sumMu / F.length;
			sigma -= mu * mu;
			dist = betterDist(F, i + 1, currentBest, mu, sigma);
			if(dist < currentBest){
				currentBest = dist;
			}
		}
		return Math.sqrt(currentBest);
	}
}
