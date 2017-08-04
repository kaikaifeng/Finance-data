package shapelet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import unsupervised.Support;

public class Main {
	public static void main(String[] args){
		/*File f = new File("dir");
		File[] files = f.listFiles();
		for (File file : files) {
			System.out.println(file.getName());
		}*/
		
		SeriesSet set = Input.inputFromFile("C:\\Users\\kaika\\Desktop\\shapelet\\output\\sample");
		/*for (Serie serie : set.getSeries()) {
			System.out.println(serie.getName());
		}*/
		
		ArrayList<Shapelet> shapelets = set.generateShapelet(100);
		
		/*HashMap<Integer, String> aHashMap = new HashMap<Integer, String>();
		aHashMap.put(new Integer(1), "111");
		System.out.println(aHashMap.get(new Integer(1)));*/
		
		//set.generateAll(50, 100);
		long start = System.currentTimeMillis();
		long sum = 0;
		for(int i = 50; i <= 50; i++){
			ArrayList<Shapelet> sh = set.generateShapelet(i);
			for (Shapelet shapelet : sh) {
				for (Serie serie : set.getSeries()) {
					//serie.naiveSubsequenceDist(shapelet);
					serie.betterSubsequenceDist(shapelet);
					sum++;
					if(sum % 10000 == 0){
						System.out.println(sum);
					}
				}
			}
		}
		System.out.println("sum: " + sum);
		System.out.println("time: " + (System.currentTimeMillis() - start));
		
		/*double[][] a = new double[7][];
		a[1] = new double[9];
		a[2] = new double[4];
		System.out.println(a[1].length);
		System.out.println(a[2].length);*/
		
		double[] b = {0, 1, 4, 5, -1, 10, 12, -8, 0.3, -0.3};
		int[] c = {0, 1, 1, 1, 2, 2, 6, 8, 1, 3};
		Support.outHeapSort(b, c);
	}
}
