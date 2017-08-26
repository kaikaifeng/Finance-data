package DataProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
	public static void main(String[] args){
		List<String> list = getInputList("xxxxx", "xx");
		String[] dirs = new String[2];
		dirs[0] = "xxxxxxx";
		dirs[1] = "xxxxxxx";
		for (String string : list) {
			ArrayList<String> arrayList = getData(dirs, string + ".csv");
			toFile("xxxxxxx", arrayList, string);
		}
	}
	
	public static List<String> getInputList(String fileName, String title){
		List<String> list = new ArrayList<String>();
		try(FileInputStream fileInputStream = new FileInputStream(fileName);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);){
			String tmp = null;
			while((tmp = bufferedReader.readLine()) != null){
				if(!tmp.equals("")){
					try{
						String[] sp = tmp.split(",");
						list.add(title + sp[0]);
					}
					catch(Exception exception){
						System.out.println(tmp);
						exception.printStackTrace();
					}
				}
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		return list;
	}
	
	public static Set<String>[] getDataSet(String[] dirs){
		@SuppressWarnings("unchecked")
		Set<String>[] sets = new HashSet[dirs.length];
		for (int i = 0; i < dirs.length; i++) {
			Set<String> set = new HashSet<String>();
			File[] files = new File(dirs[i]).listFiles();
			for (File file : files) {
				//String[] sp = file.getName().split("\\.");
				set.add(file.getName());
			}
			sets[i] = set;
		}
		return sets;
	}
	
	public static ArrayList<String> getData(String[] dirs, String fileName){
		ArrayList<String> arrayList = new ArrayList<String>();
		//arrayList.add(fileName.split("\\.")[0]);
		Set<String>[] sets = getDataSet(dirs);
		for (int i = 0; i < sets.length; i++) {
			Set<String> set = sets[i];
			String dir = dirs[i];
			if (set.contains(fileName)) {
				try(FileInputStream fileInputStream = new FileInputStream(dir + "\\" + fileName);
						InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
						BufferedReader bufferedReader = new BufferedReader(inputStreamReader);){
					String tmp = null;
					while((tmp = bufferedReader.readLine()) != null){
						if(!tmp.equals("")){
							try{
								arrayList.add(tmp.split(",")[5]);
							}
							catch(Exception exception){
								System.out.println(tmp);
								exception.printStackTrace();
							}
						}
					}
				}
				catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
		return arrayList;
	}
	
	public static void toFile(String dir, ArrayList<String> arrayList, String title){
		int length = arrayList.size();
		int n = length / 2400;
		for(int i = 0; i < n; i++){
			try(FileOutputStream fileOutputStream = new FileOutputStream(dir + "\\" + title + "_" + i + ".csv");
					OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
					BufferedWriter bufferedWriter = new BufferedWriter(writer);){
				bufferedWriter.write(title + "_" + i);
				bufferedWriter.newLine();
				for(int j = 0; j < 2400; j++){
					bufferedWriter.write(arrayList.get(i * 2400 + j));
					bufferedWriter.newLine();
				}
			}
			catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}
}
