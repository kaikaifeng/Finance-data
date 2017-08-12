package multiThread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Emperor implements Runnable{
	private int position;
	private int startPosition;
	private int length;
	private Socket socket;
	private boolean[] finished;
	
	public Emperor(Socket socket, int position, boolean[] finished, int startPosition, int length) {
		this.position = position;
		this.startPosition = startPosition;
		this.length = length;
		this.socket = socket;
		this.finished = finished;
	}
	
	@Override
	public void run() {
		try(InputStream inputStream = socket.getInputStream();
				InputStreamReader reader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(reader);
				OutputStream outputStream = socket.getOutputStream();
				OutputStreamWriter writer = new OutputStreamWriter(outputStream);
				BufferedWriter bufferedWriter = new BufferedWriter(writer);) {
			String info = null;
			info = bufferedReader.readLine();
			if(!info.equals("ready")){
				System.out.println(socket.getInetAddress() + ": can't get ready");
				return;
			}
			bufferedWriter.write(startPosition + "," + length);
			bufferedWriter.newLine();
			//bufferedWriter.flush();
			//bufferedWriter.write(length);
			//bufferedWriter.newLine();
			bufferedWriter.flush();
			info = bufferedReader.readLine();
			System.out.println(info);
			if(!info.equals("finished")){
				System.out.println(socket.getInetAddress() + ": can't finish tasks");
			}
			finished[position] = true;
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		} 
	}
	
	public static void main(String[] args) throws IOException{
		int number = 0;
		int start = 0;
		int end = 0;
		try{
			number = Integer.parseInt(args[0]);
			start = Integer.parseInt(args[1]);
			end = Integer.parseInt(args[2]);
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
		if(end < start || start < 1 || number < 1 || (end - start) % number != 0){
			return;
		}
		boolean[] finished = new boolean[number];
		int position = 0;
		int startPosition = start;
		int length = (end - start) / number;
		for(int i = 0; i < number; i++){
			finished[i] = false;
		}
		ServerSocket serverSocket = new ServerSocket(49000);
		while(!allTrue(finished)){
			if(position < number){
				try{
					Socket socket = serverSocket.accept();
					Thread thread = new Thread(new Emperor(socket, position, finished, startPosition, length));
					thread.start();
				}
				catch(Exception exception){
					exception.printStackTrace();
					finished[position] = true;
				}
				position++;
				startPosition += length;
			}
			else{
				try {
					Thread.sleep(100);
				} catch (InterruptedException interruptedException) {
					interruptedException.printStackTrace();
				}
			}
		}
	}
	
	public static boolean allTrue(boolean[] bs){
		boolean bool = true;
		for(int i = 0; i < bs.length; i++){
			bool = bool & bs[i];
			if(!bool){
				return false;
			}
		}
		return true;
	}
}
