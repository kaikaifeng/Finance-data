package DataProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FormatChanger {
	public static void changeFile(String fileName, String dir){
		File outputDir = new File(new File(dir).getAbsolutePath() + File.separator + new File(fileName).getName());
		outputDir.mkdirs();
		String tmp = null;
		try(FileInputStream fileInputStream = new FileInputStream(fileName);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);){
			int count = 0;
			while((tmp = bufferedReader.readLine()) != null){
				if(tmp.equals("")){
					break;
				}
				String[] sp = tmp.split(",");
				double[] ds = new double[sp.length];
				for(int i = 0; i < sp.length; i++){
					try {
						ds[i] = Double.parseDouble(sp[i]);
					} catch (Exception exception) {
						System.out.println(sp[i]);
						exception.printStackTrace();
						break;
					}
				}
				try(FileOutputStream fileOutputStream = new FileOutputStream(outputDir.getAbsolutePath() + File.separator + count + ".csv");
						OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
						BufferedWriter bufferedWriter = new BufferedWriter(writer);){
					bufferedWriter.write(fileName + File.pathSeparator + count + "\r\n");
					for(int i = 0; i < sp.length; i++){
						bufferedWriter.write(ds[i] + "\r\n");
					}
				}
				catch (Exception exception) {
					exception.printStackTrace();
					break;
				}
				count++;
			}
		}
		catch (Exception exception) {
			System.out.println(tmp);
			exception.printStackTrace();
		}
	}
	
	public static void changeLabel(String fileName, String dir){
		String tmp = null;
		ArrayList<Integer> integers = new ArrayList<Integer>();
		try(FileInputStream fileInputStream = new FileInputStream(fileName);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);){
			while((tmp = bufferedReader.readLine()) != null) {
				if(tmp.equals("")){
					break;
				}
				String[] sp = tmp.split(",");
				try{
					for(int i = 0; i < sp.length; i++){
						if(Double.parseDouble(sp[i]) > 0.5){
							integers.add(Integer.valueOf(i));
							break;
						}
					}
				}
				catch(Exception exception){
					System.out.println(tmp);
					exception.printStackTrace();
					break;
				}
			}
			try(FileOutputStream fileOutputStream = new FileOutputStream(new File(dir).getAbsolutePath() + File.separator + "labels.csv");
					OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
					BufferedWriter bufferedWriter = new BufferedWriter(writer);){
				for (Integer integer : integers) {
					bufferedWriter.write(integer.intValue() + "\r\n");
				}
			}
			catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
