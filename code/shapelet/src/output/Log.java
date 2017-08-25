package output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Log {
	private static final int port = 49005;
	private Socket socket;
	private BufferedWriter bufferedWriter = null;
	private static HashMap<String, Log> logs = new HashMap<String, Log>();
	
	private Log(){}
	
	private Log(String host){
		try {
			socket = new Socket(host, port);
			logs.put(host, this);
		} catch (UnknownHostException unknownHostException) {
			unknownHostException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	public static Log getLog(String host){
		if(logs.containsKey(host)){
			return logs.get(host); 
		}
		return new Log(host);
	}
	
	@SuppressWarnings("finally")
	public static ServerSocket getServer(){
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			//System.out.println(serverSocket.getInetAddress().getHostAddress());
			//new Log(serverSocket.getInetAddress().getHostAddress());
			return serverSocket;
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		return null;
	}
	
	public void record(String string){
		if(bufferedWriter == null){
			try{
				if(socket == null){
					System.out.println("!!!");
				}
				System.out.println(socket.isConnected());
				OutputStream outputStream = socket.getOutputStream();
				OutputStreamWriter writer = new OutputStreamWriter(outputStream);
				bufferedWriter = new BufferedWriter(writer);
				bufferedWriter.write(string);
				bufferedWriter.newLine();
				bufferedWriter.flush();
				bufferedWriter.close();
				writer.close();
				outputStream.close();
				//socket.close();
				System.out.println("$$$$$$$$");
				System.out.println(socket.isClosed());
			}
			catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}
}
