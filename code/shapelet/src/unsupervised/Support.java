package unsupervised;

import java.util.ArrayList;
import java.util.HashMap;

public class Support {
	public static int[] KMeans(double[] values, int kernelNumber){
		int[] labels = new int[values.length];
		double[] kernels = randKernel(values, kernelNumber);
		//int[] indexs = new int[values.length];
		//double[] assessment = new double[values.length];
		for(int i = 0; i < values.length; i++){
			labels[i] = 0;
			//assessment[i] = 0.0;
		}
		boolean clusterChanged = true;
		while(clusterChanged){
			clusterChanged = false;
			//find the current best center
			for(int i = 0; i < values.length; i++){
				double minDist = Double.MAX_VALUE;
				int minIndex = -1;
				for(int j = 0; j < kernelNumber; j++){
					double dist = dist(values[i], kernels[j]);
					if(dist < minDist){
						minDist = dist;
						minIndex = j;
					}
				}
				if(minIndex != labels[i]){
					clusterChanged = true;
				}
				labels[i] = minIndex;
				//assessment[i] = minDist * minDist;
			}
			//update kernel
			for(int i = 0; i < kernelNumber; i++){
				kernels[i] = mean(values, labels, i);
			}
		}
		return labels;
	}
	
	private static double[] randKernel(double[] values, int kernelNumber){
		double[] kernels = new double[kernelNumber];
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for(int i = 0; i < values.length; i++){
			if(values[i] < min){
				min = values[i];
			}
			if(values[i] > max){
				max = values[i];
			}
		}
		double range = max - min;
		for(int i = 0; i < kernelNumber; i++){
			kernels[i] = min + range * Math.random();
		}
		return kernels;
	}
	
	private static double dist(double v1, double v2){
		return (v1 - v2) * (v1 - v2);
	}
	
	private static double mean(double[] values, int[] labels, int index){
		int number = 0;
		double sum = 0.0;
		for(int i = 0; i < labels.length; i++){
			if(index == labels[i]){
				sum += values[i];
				number++;
			}
		}
		return sum / number;
	}
	
	public static void outHeapSort(double[] values, int[] labels){
		if(values.length != labels.length){
			return;
		}
		//HashMap<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
		for(int i = (values.length - 1) / 2; i > 0; i--){
			int hole = i;
			int child = 0;
			double tmp = values[hole];
			int labelTmp = labels[hole];
			for(; hole * 2 < values.length; hole = child){
				child = hole * 2;
				if(child != (values.length - 1) && values[child + 1] < values[child]){
					child++;
				}
				if(values[child] < tmp){
					values[hole] = values[child];
					labels[hole] = labels[child];
				}
				else{
					break;
				}
			}
			values[hole] = tmp;
			labels[hole] = labelTmp;
		}
		for(int i = 1; i < values.length; i++){
			double tmp = values[values.length - i];
			values[values.length - i] = values[1];
			values[1] = tmp;
			int labelTmp = labels[values.length - i];
			labels[values.length - i] = labels[1];
			labels[1] = labelTmp;
			int hole = 1;
			int child = 0;
			for(; hole * 2 < values.length - i; hole = child){
				child = hole * 2;
				if(child != (values.length - i - 1) && values[child + 1] < values[child]){
					child++;
				}
				if(values[child] < tmp){
					values[hole] = values[child];
					labels[hole] = labels[child];
				}
				else{
					break;
				}
			}
			values[hole] = tmp;
			labels[hole] = labelTmp;
		}
		/*for (int i : labels) {
			System.out.print(i + " ");
		}
		System.out.println();
		for (double d : values) {
			System.out.print(d + " ");
		}*/
		/*for(int i = 0; i < values.length; i++){
			if(!map.containsKey(new Integer(labels[i]))){
				map.put(new Integer(labels[i]), new ArrayList<Integer>());
			}
			map.get(labels[i]).add(new Integer(i + 1));
		}
		return map;*/
	}
}
