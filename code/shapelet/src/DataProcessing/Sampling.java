package DataProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Sampling {
	public static void fromFiles(String dirName, String newDirName, int number){
		File dir = new File(dirName);
		File newDir = new File(newDirName);
		if(!newDir.exists()){
			newDir.mkdirs();
		}
		File[] files = dir.listFiles();
		for(int i = 0; i < number; i++){
			copyFile(files[i].getAbsolutePath(), newDirName + File.separator + files[i].getName());
		}
	}
	
	public static void copyFile(String oldFileName, String newFileName){
		try { 
			int byteRead = 0; 
			File oldFile = new File(oldFileName); 
			if (oldFile.exists()) { 
				InputStream inStream = new FileInputStream(oldFile); //读入原文件 
				FileOutputStream fs = new FileOutputStream(newFileName); 
				byte[] buffer = new byte[1444]; 
				int length; 
				while ( (byteRead = inStream.read(buffer)) != -1) {   
					fs.write(buffer, 0, byteRead); 
				} 
				inStream.close(); 
			} 
		} 
		catch (Exception exception) { 
			exception.printStackTrace(); 
		} 
	}
	
	public static void samplingLabels(String dirName, String originalFileName, String fileName){
		File dir = new File(dirName);
		ArrayList<String> strings = new ArrayList<String>();
		String tmp = null;
		try(FileInputStream fileInputStream = new FileInputStream(originalFileName);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				FileOutputStream fileOutputStream = new FileOutputStream(fileName);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
				BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);){
			while((tmp = bufferedReader.readLine()) != null){
				strings.add(tmp);
			}
			File[] files = dir.listFiles();
			int[] numbers = new int[files.length];
			int i = 0;
			for (File file : files) {
				try{
					numbers[i++] = Integer.parseInt(file.getName().split("\\.")[0]);
				}
				catch(Exception exception){
					exception.printStackTrace();
				}
			}
			for (int num : numbers) {
				bufferedWriter.write(num + "\r\n");
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
