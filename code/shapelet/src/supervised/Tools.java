package supervised;

import java.util.ArrayList;
import java.util.HashMap;

import unsupervised.Metric;

public class Tools {
	public static double FStatistic(HashMap<Integer, ArrayList<Double>> map){
		return Metric.FStatistic(map);
	}
	
	public static double KruskalWallis(HashMap<Integer, ArrayList<Integer>> map){
		return Metric.KruskalWallis(map);
	}
}
