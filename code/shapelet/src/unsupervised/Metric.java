package unsupervised;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Metric {
	/*
	 * no correct
	 */
	public static double FStatistic(HashMap<Integer, ArrayList<Double>> map){
		ArrayList<Integer> integers = new ArrayList<>(map.keySet());
		double[] averages = new double[integers.size()]; 
		double allAverage = 0.0;
		int allNumber = 0;
		int[] number = new int[integers.size()];
		for(int i = 0; i < integers.size(); i++) {
			ArrayList<Double> list = map.get(integers.get(i));
			double average = 0.0;
			for (Double value : list) {
				average += value.doubleValue();
			}
			allAverage += average;
			averages[i] = average / list.size();
			number[i] = list.size();
			allNumber += number[i];
		}
		allAverage /= allNumber;
		double denominator = 0.0;
		for(int i = 0; i < integers.size(); i++){
			ArrayList<Double> list = map.get(integers.get(i));
			double sum = 0.0;
			for (Double value : list) {
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
				//System.out.println(rank.intValue());
				//squareSum += rank.intValue() * rank.intValue();
				squareSum += rank.intValue();
			}
			//System.out.println(squareSum);
			//System.out.println(list.size());
			squareSum = squareSum * squareSum;
			squareSum /= list.size();
			sum += squareSum;
			number += list.size();
			//System.out.println(list.size());
		}
		//System.out.println(sum);
		K = sum * 12;
		//System.out.println(K);
		K /= number * (++number);
		//System.out.println(K);
		K -= 3 * number;
		//System.out.println(K);
		return K;
	}
	
	public static double MoodsMedian(){
		return 0.0;
	}
	
	public static HashMap<Integer, ArrayList<Integer>> dealLabelRank(int[] labels, double[] values){
		if(labels.length != values.length){
			return null;
		}
		HashMap<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
		//copy at first
		int[] tmpLabels = new int[labels.length + 1];
		double[] tmpValues = new double[labels.length + 1];
		tmpLabels[0] = 0;
		tmpValues[0] = 0.0;
		System.arraycopy(labels, 0, tmpLabels, 1, labels.length);
		System.arraycopy(values, 0, tmpValues, 1, labels.length);
		Support.outHeapSort(tmpValues, tmpLabels);
		/*for (double d : tmpValues) {
			System.out.println(d);
		}
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		for(int i = 0; i < labels.length; i++) {
			if(!map.containsKey(Integer.valueOf(tmpLabels[i]))){
				map.put(Integer.valueOf(tmpLabels[i]), new ArrayList<Integer>());
			}
			map.get(Integer.valueOf(tmpLabels[i])).add(new Integer(i + 1));
		}
		return map;
	}
	
	public static HashMap<Integer, ArrayList<Double>> dealLabelValue(int[] labels, double[] values){
		if(labels.length != values.length){
			return null;
		}
		HashMap<Integer, ArrayList<Double>> map = new HashMap<Integer, ArrayList<Double>>();
		int[] tmpLabels = new int[labels.length + 1];
		double[] tmpValues = new double[labels.length + 1];
		tmpLabels[0] = 0;
		tmpValues[0] = 0.0;
		System.arraycopy(labels, 0, tmpLabels, 1, labels.length);
		System.arraycopy(values, 0, tmpValues, 1, labels.length);
		Support.outHeapSort(tmpValues, tmpLabels);
		for(int i = 0; i < labels.length; i++) {
			if(!map.containsKey(Integer.valueOf(tmpLabels[i]))){
				map.put(Integer.valueOf(tmpLabels[i]), new ArrayList<Double>());
			}
			map.get(Integer.valueOf(tmpLabels[i])).add(new Double(tmpValues[i]));
		}
		return map;
	}
}
