package shapelet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	public static void main(String[] args){
		/*File f = new File("dir");
		File[] files = f.listFiles();
		for (File file : files) {
			System.out.println(file.getName());
		}*/
		
		SeriesSet set = Input.inputFromFile("C:\\Users\\kaika\\Desktop\\shapelet\\output\\sample");
		for (Serie serie : set.getSeries()) {
			System.out.println(serie.getName());
		}
		
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
					serie.naiveSubsequenceDist(shapelet);
					sum++;
					if(sum % 10000 == 0){
						System.out.println(sum);
					}
				}
			}
		}
		System.out.println("sum: " + sum);
		System.out.println("time: " + (System.currentTimeMillis() - start));
	}
}
