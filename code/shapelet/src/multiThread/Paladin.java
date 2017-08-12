package multiThread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import shapelet.Input;
import shapelet.Serie;
import shapelet.SeriesSet;
import shapelet.Shapelet;

public class Paladin extends Knight{
	private Socket socket;
	
	public Paladin(SeriesSet seriesSet, int startNumber, int endNumber) {
		super(seriesSet, startNumber, endNumber);
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run(){
		long start = System.currentTimeMillis();
		long sum = 0;
		for(int i = startNumber; i <= endNumber; i++){
			ArrayList<Shapelet> shapelets = seriesSet.generateShapelet(i);
			for (Shapelet shapelet : shapelets) {
				shapelet.sortIndex();
				for (Serie serie : seriesSet.getSeries()) {
					serie.subsequenceDist(shapelet);
					sum++;
				}
			}
		}
		System.out.println("sum: " + sum);
		System.out.println("time: " + (System.currentTimeMillis() - start));
		/*try(OutputStream outputStream = socket.getOutputStream();
				OutputStreamWriter writer = new OutputStreamWriter(outputStream);
				BufferedWriter bufferedWriter = new BufferedWriter(writer);){
			bufferedWriter.write("finished");
			bufferedWriter.newLine();
			bufferedWriter.flush();
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		}*/
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException{
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(args[0], 49000));
		try(InputStream inputStream = socket.getInputStream();
				InputStreamReader reader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(reader);
				OutputStream outputStream = socket.getOutputStream();
				OutputStreamWriter writer = new OutputStreamWriter(outputStream);
				BufferedWriter bufferedWriter = new BufferedWriter(writer);){
			SeriesSet set = Input.inputFromFile("sample");
			bufferedWriter.write("ready");
			bufferedWriter.newLine();
			bufferedWriter.flush();
			String string = bufferedReader.readLine();
			String[] sp = string.split(",");
			int startNumber = Integer.parseInt(sp[0]);
			System.out.println(startNumber);
			//bufferedReader.readLine();
			int endNumber = startNumber + Integer.parseInt(sp[1]);
			System.out.println(endNumber);
			for(int i = 0; i < endNumber - startNumber; i++){
				Paladin paladin = new Paladin(set, startNumber + i, startNumber + i);
				paladin.setSocket(socket);
				Thread thread = new Thread(paladin);
				thread.start();
			}
			bufferedWriter.write("finished");
			bufferedWriter.newLine();
			bufferedWriter.flush();
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
