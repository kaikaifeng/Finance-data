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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import output.LocalOutput;
import output.Log;
import shapelet.Input;
import shapelet.Serie;
import shapelet.SeriesSet;
import shapelet.Shapelet;
import shapelet.ShapeletScore;
import unsupervised.Metric;
import unsupervised.Support;

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
			//double[] scores = new double[shapelets.size()];
			ArrayList<ShapeletScore> shapeletScores = new ArrayList<ShapeletScore>(shapelets.size());
			int j = 0;
			for (Shapelet shapelet : shapelets) {
				shapelet.sortIndex();
				double[] subsequenceDists = new double[seriesSet.size()];
				//int[] labels = new int[seriesSet.size()];
				int k = 0;
				//System.out.println(seriesSet.size());
				for (Serie serie : seriesSet.getSeries()) {
					subsequenceDists[k] = serie.subsequenceDist(shapelet);
					//labels[j] = serie.getLabel();
					sum++;
					k++;
				}
				int[] labels = Support.KMeans(subsequenceDists, 2);
				//System.out.println(labels.length);
				double score = Metric.KruskalWallis(Metric.dealLabelRank(labels, subsequenceDists));
				//System.out.println(score);
				//scores[j] = score;
				j++;
				shapeletScores.add(new ShapeletScore(shapelet, score));
			}
			Collections.sort(shapeletScores);
			ArrayList<ShapeletScore> tops = new ArrayList<ShapeletScore>();
			for(int k = shapeletScores.size() - 1; k >= shapeletScores.size() - 1000; k--){
				tops.add(shapeletScores.get(k));
			}
			LocalOutput.toFile("KruskalWallis/scores_" + startNumber + ".csv", tops);
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
			//Thread.sleep(10000);
			bufferedWriter.write("finished");
			bufferedWriter.newLine();
			bufferedWriter.flush();
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
			Log log = Log.getLog(args[0]);
			log.record("Done");
			System.out.println("Done");
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
