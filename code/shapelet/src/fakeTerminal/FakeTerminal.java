package fakeTerminal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sun.tools.jar.resources.jar;

public class FakeTerminal {
	public static void main(String[] args){
		for(int length = 6; length <= 20; length++){
			for(int i = 0; i < 52; i++){
				//List<String> command = new ArrayList<String>();
				//command.add("java -jar C:\\Users\\kaika\\Desktop\\shapelet\\output\\controller.jar 1 5 6");
				try{
					//ProcessBuilder builder = new ProcessBuilder();  
					//builder.command(command);  
					//final Process process = builder.start(); 
					Process process = Runtime.getRuntime().exec("java -jar C:\\Users\\kaika\\Desktop\\shapelet\\output\\controller.jar 1 " + length + " " + (length + 1));
					new ConsolePrinter(process.getInputStream()).start();
				}
				catch(Exception exception){
					exception.printStackTrace();
				}
			
				//command = new ArrayList<String>();
				//command.add("java -jar C:\\Users\\kaika\\Desktop\\shapelet\\output\\worker.jar 127.0.0.1 D:\\eclipse_workspace\\Shapelet\\sampling\\train_" + i + ".csv\\ D:\\eclipse_workspace\\Shapelet\\samplingLabels.csv 0 " + i);
				try{
					//ProcessBuilder builder = new ProcessBuilder();  
					//builder.command(command);  
					//final Process process = builder.start(); 
					Process process = Runtime.getRuntime().exec(
							"java -jar C:\\Users\\kaika\\Desktop\\shapelet\\output\\worker.jar 127.0.0.1 D:\\eclipse_workspace\\Shapelet\\sampling\\train_" 
									+ i + ".csv\\ D:\\eclipse_workspace\\Shapelet\\samplingLabels.csv 0 " + i);
					new ConsolePrinter(process.getInputStream()).start();
					process.waitFor();
				}
				catch(Exception exception){
					exception.printStackTrace();
				}
			}
		}
	}
}
