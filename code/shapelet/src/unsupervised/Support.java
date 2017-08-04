package unsupervised;

import java.util.ArrayList;
import java.util.HashMap;

public class Support {
	public static void KMeans(){
		
	}
	
	public static HashMap<Integer, ArrayList<Integer>> outHeapSort(double[] values, int[] labels){
		if(values.length != labels.length){
			return null;
		}
		HashMap<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
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
		for(int i = 0; i < values.length; i++){
			if(!map.containsKey(new Integer(labels[i]))){
				map.put(new Integer(labels[i]), new ArrayList<Integer>());
			}
			map.get(labels[i]).add(new Integer(i + 1));
		}
		return map;
	}
}
