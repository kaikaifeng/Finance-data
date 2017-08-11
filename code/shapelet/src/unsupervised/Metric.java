package unsupervised;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Metric {
	public static double FStatistic(HashMap<Integer, ArrayList<Integer>> map){
		ArrayList<Integer> integers = new ArrayList<>(map.keySet());
		double[] averages = new double[integers.size()]; 
		double allAverage = 0.0;
		int allNumber = 0;
		int[] number = new int[integers.size()];
		for(int i = 0; i < integers.size(); i++) {
			ArrayList<Integer> list = map.get(integers.get(i));
			double average = 0.0;
			for (Integer value : list) {
				average += value.intValue();
			}
			allAverage += average;
			averages[i] = average / list.size();
			number[i] = list.size();
			allNumber += number[i];
		}
		allAverage /= allNumber;
		double denominator = 0.0;
		for(int i = 0; i < integers.size(); i++){
			ArrayList<Integer> list = map.get(integers.get(i));
			double sum = 0.0;
			for (Integer value : list) {
				sum += (value.doubleValue() - averages[i]) * (value.doubleValue() - averages[i]);
			}
			denominator += sum;
		}
		denominator /= allNumber - integers.size();
		double numerator = 0.0;
		for(int i = 0; i < integers.size(); i++){
			numerator += (averages[i] - allAverage) * (averages[i] - allAverage);
		}
		numerator /= integers.size() - 1;
		return numerator / denominator;
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
