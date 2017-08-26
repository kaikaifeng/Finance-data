package shapelet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Input {
	public static SeriesSet inputFromFile(String dir){
		SeriesSet set = new SeriesSet();
		File inputDir = new File(dir);
		if(!inputDir.isDirectory()){
			return null;
		}
		File[] files = inputDir.listFiles();
		for (File file : files) {
			String tmp = null;
			try(FileInputStream fileInputStream = new FileInputStream(file);
					InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);){
				ArrayList<Double> list = new ArrayList<Double>();
				String name = bufferedReader.readLine();
				while((tmp = bufferedReader.readLine()) != null){
					if(!tmp.equals("")){
						try{
							list.add(new Double(tmp));
						}
						catch(Exception exception){
							exception.printStackTrace();
						}
					}
				}
				double[] ds = new double[list.size()];
				for(int i = 0; i < list.size(); i++){
					ds[i] = list.get(i);
				}
				//default label
				set.addSerie(new Serie(ds, 0, name));
			}
			catch (Exception exception) {
				System.out.println(tmp);
				exception.printStackTrace();
			}
		}
		return set;
	}
	
	public static SeriesSet addLabel(SeriesSet set, String labelFile){
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		String tmp = null;
		try(FileInputStream fileInputStream = new FileInputStream(labelFile);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);){
			while((tmp = bufferedReader.readLine()) != null){
				if(!tmp.equals("")){
					try{
						String[] sp = tmp.split(",");
						hashMap.put(sp[0], Integer.parseInt(sp[1]));
					}
					catch (Exception exception){
						exception.printStackTrace();
					}
				}
			}
		}
		catch (Exception exception) {
			System.out.println(tmp);
			exception.printStackTrace();
		}
		for (Serie serie : set.getSeries()) {
			serie.setLabel(hashMap.get(serie.getName()).intValue());
		}
		return set;
	}
}
