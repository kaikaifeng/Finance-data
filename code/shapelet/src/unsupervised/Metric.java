package unsupervised;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Metric {
	public static double FStatistic(){
		double F = 0.0;
		return F;
	}
	
	public static double KruskalWallis(HashMap<Integer, ArrayList<Integer>> map){
		Set<Integer> set = map.keySet();
		double K = 0.0;
		long sum = 0;
		int number = 0;
		for (Integer integer : set) {
			ArrayList<Integer> list = map.get(integer);
			long squareSum = 0;
			for (Integer rank : list) {
				squareSum += rank.intValue() * rank.intValue();
			}
			squareSum /= list.size();
			sum += squareSum;
			number += list.size();
		}
		K = sum * 12;
		K /= number * (++number);
		K -= 3 * number;
		return K;
	}
	
	public static double MoodsMedian(){
		
		return 0.0;
	}
}
