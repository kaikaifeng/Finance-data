package shapelet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import multiThread.Knight;
import multiThread.Paladin;
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
		
		//main test
		long start = System.currentTimeMillis();
		long sum = 0;	
		/*for (Shapelet shapelet : shapelets) {
			for (Serie serie : set.getSeries()) {
				serie.naiveSubsequenceDist(shapelet);
				//serie.betterSubsequenceDist(shapelet);
				//serie.subsequenceDist(shapelet);
				sum++;
				if(sum % 10000 == 0){
					//System.out.println(sum);
				}
			}
		}
		System.out.println("naive");
		System.out.println("sum: " + sum);
		System.out.println("time: " + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		sum = 0;
		for (Shapelet shapelet : shapelets) {
			for (Serie serie : set.getSeries()) {
				//serie.naiveSubsequenceDist(shapelet);
				serie.betterSubsequenceDist(shapelet);
				//serie.subsequenceDist(shapelet);
				sum++;
				if(sum % 10000 == 0){
					//System.out.println(sum);
				}
			}
		}
		System.out.println("better");
		System.out.println("sum: " + sum);
		System.out.println("time: " + (System.currentTimeMillis() - start));
		
		start = System.currentTimeMillis();
		sum = 0;
		for (Shapelet shapelet : shapelets) {
			shapelet.sortIndex();
			for (Serie serie : set.getSeries()) {
				//serie.naiveSubsequenceDist(shapelet);
				//serie.betterSubsequenceDist(shapelet);
				serie.subsequenceDist(shapelet);
				sum++;
				if(sum % 10000 == 0){
					//System.out.println(sum);
				}
			}
		}
		System.out.println("sorted");
		System.out.println("sum: " + sum);
		System.out.println("time: " + (System.currentTimeMillis() - start));*/
		
		/*double[][] a = new double[7][];
		a[1] = new double[9];
		a[2] = new double[4];
		System.out.println(a[1].length);
		System.out.println(a[2].length);*/
		
		//show me the sort
		/*double[] b = {0, 1, 4, 5, -1, 10, 12, -8, 0.3, -0.3};
		int[] c = {0, 1, 1, 1, 2, 2, 6, 8, 1, 3};
		int[] c = new int[b.length];
		for(int i = 0; i < c.length; i++){
			c[i] = i;
		}
		Support.outHeapSort(b, c);
		for (int i : c) {
			System.out.print(i + " ");
		}
		System.out.println();
		for (double d : b){
			System.out.print(d + " ");
		}*/
		/*for(double d : shapelets.get(1).getList()){
			System.out.print(d + " ");
		}
		System.out.println();
		double[] tmp = new double[100];
		for(int i = 0; i < 100; i++){
			tmp[i] = shapelets.get(1).getList()[i];
		}
		shapelets.get(1).sortIndex();
		for(int i : shapelets.get(1).getIndex()){
			System.out.print(i + " ");
		}
		System.out.println();
		for(double d : shapelets.get(1).getList()){
			System.out.print(d + " ");
		}
		System.out.println();
		for(int i = 0; i < 100; i++){
			if(!(tmp[shapelets.get(1).getIndex()[i]] - shapelets.get(1).getList()[i] < 1E-15)){
				System.out.println(tmp[shapelets.get(1).getIndex()[i]] + " " + shapelets.get(1).getList()[i]);
				System.out.println(i);
			}
		}*/
		
		//show me the number
		/*System.out.println(set.getSeries().get(0).naiveSubsequenceDist(shapelets.get(10000)));
		System.out.println(set.getSeries().get(0).betterSubsequenceDist(shapelets.get(10000)));
		shapelets.get(10000).sortIndex();
		System.out.println(set.getSeries().get(0).subsequenceDist(shapelets.get(10000)));*/
		
		//multiThread test
		for(int i = 0; i < 1; i++){
			Knight knight = new Paladin(set, 50 + i, 50 + i);
			Thread thread = new Thread(knight);
			thread.start();
		}
		
		//copy test
		/*int[] a = {1, 2};
		int[] b = new int[2];
		System.arraycopy(a, 0, b, 0, a.length);
		for (int i : b) {
			System.out.println(i);
		}*/
	}
}
