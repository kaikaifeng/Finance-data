package multiThread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import output.LocalOutput;
import shapelet.Input;
import shapelet.Serie;
import shapelet.SeriesSet;
import shapelet.Shapelet;
import shapelet.ShapeletScore;
import unsupervised.Metric;

public class Templars extends Knight{
	public static final int FSTATISTIC = 0;
	public static final int KRUSKALWALLIS = 1;
	
	private int code;
	private String describe;
	
	public Templars(SeriesSet seriesSet, int startNumber, int endNumber, int code, String describe) {
		super(seriesSet, startNumber, endNumber);
		this.code = code;
		this.describe = describe;
	}
	
	@Override
	public void run(){
		long start = System.currentTimeMillis();
		long sum = 0;
		for(int i = startNumber; i <= endNumber; i++){
			ArrayList<Shapelet> shapelets = seriesSet.generateShapelet(i);
			ArrayList<ShapeletScore> shapeletScores = new ArrayList<ShapeletScore>();
			int j = 0;
			for (Shapelet shapelet : shapelets) {
				shapelet.sortIndex();
				double[] subsequenceDists = new double[seriesSet.size()];
				int k = 0;
				for (Serie serie : seriesSet.getSeries()) {
					subsequenceDists[k] = serie.subsequenceDist(shapelet);
					sum++;
					k++;
				}
				int[] labels = getLabels(seriesSet);
				double score = 0.0;
				if(code == FSTATISTIC){
					score = Metric.FStatistic(Metric.dealLabelValue(labels, subsequenceDists));
				}
				if(code == KRUSKALWALLIS){
					score = Metric.KruskalWallis(Metric.dealLabelRank(labels, subsequenceDists));
				}
				shapeletScores.add(new ShapeletScore(shapelet, score));
				//System.out.println(shapeletScores.get(shapeletScores.size() - 1).toString());
				j++;
			}
			Collections.sort(shapeletScores);
			ArrayList<ShapeletScore> tops = new ArrayList<ShapeletScore>();
			for(int k = shapeletScores.size() - 1; k >= shapeletScores.size() - 100; k--){
				tops.add(shapeletScores.get(k));
			}
			LocalOutput.toFile("xxxxx\\TimeSeries/scores_" + describe + "_" + i + ".csv", tops);
		}
		
		System.out.println("sum: " + sum);
		System.out.println("time: " + (System.currentTimeMillis() - start));
	}
	
	private int[] getLabels(SeriesSet seriesSet){
		int[] labels = new int[seriesSet.size()];
		ArrayList<Serie> series = seriesSet.getSeries();
		for(int i = 0; i < series.size(); i++){
			labels[i] = series.get(i).getLabel();
		}
		return labels;
	}
	
	public static void main(String[] args) throws IOException{
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(args[0], 49000));
		try(InputStream inputStream = socket.getInputStream();
				InputStreamReader reader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(reader);
				OutputStream outputStream = socket.getOutputStream();
				OutputStreamWriter writer = new OutputStreamWriter(outputStream);
				BufferedWriter bufferedWriter = new BufferedWriter(writer);){
			SeriesSet set = Input.inputFromFile(args[1]);
			Input.addOrderedLabel(set, args[2]);
			bufferedWriter.write("ready");
			bufferedWriter.newLine();
			bufferedWriter.flush();
			String string = bufferedReader.readLine();
			String[] sp = string.split(",");
			int startNumber = Integer.parseInt(sp[0]);
			int endNumber = startNumber + Integer.parseInt(sp[1]);
			System.out.println(startNumber);
			System.out.println(endNumber);
			for(int i = 0; i < endNumber - startNumber; i++){
				Templars templars = new Templars(set, startNumber + i, startNumber + i, Integer.parseInt(args[3]), args[4]);
				Thread thread = new Thread(templars);
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
