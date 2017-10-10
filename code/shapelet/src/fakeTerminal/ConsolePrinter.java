package fakeTerminal;

import java.io.InputStream;

public class ConsolePrinter extends Thread{
	private InputStream inputStream;
	
	public ConsolePrinter(InputStream inputStream){
		this.inputStream = inputStream;
	}
	
	public void run(){
		try{
			while(this != null){
				int in = inputStream.read();
				if(in != -1){
					System.out.print((char)in);
				}
				else{
					break;
				}
			}
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
	}
}
