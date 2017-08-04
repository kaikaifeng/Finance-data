package supervised;

import java.util.ArrayList;
import java.util.HashMap;

import unsupervised.Metric;

public class Tools {
	public static double FStatistic(){
		double F = 0.0;
		return F;
	}
	
	public static double KruskalWallis(HashMap<Integer, ArrayList<Integer>> map){
		return Metric.KruskalWallis(map);
	}
}
