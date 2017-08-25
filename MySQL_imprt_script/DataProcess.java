import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class DataProcess {
	
	// the InPath and OutPath should endwith /
	public static String MergePath = "/home/hadoop/BUAAQuant/Stk_1F/";
	public static String FormatPath = "/home/hadoop/BUAAQuant/Stk_1F_Format/";
	public static String Headerline = "datetime,start,max,min,end,amount,money\n";
	
	// The Diretory hierarchy is Folder -> 2016/17Data -> StockData
	public static void main(String args[]){
		long starttime = System.currentTimeMillis();
		
		int count = 0;
		String line = "";
		if(MergePath.endsWith("/") == false){
			MergePath = MergePath + "/";
		}
		if(FormatPath.endsWith("/") == false){
			FormatPath = FormatPath + "/";
		}
		File MergeData = new File(MergePath);
		File FormatData = new File(FormatPath);
		if(FormatData.exists() == false){
			FormatData.mkdirs();
		}
	
		for(File DateData:MergeData.listFiles()){
			if(DateData.isDirectory()){
				
				System.out.println(DateData.getName());
				
				try{
					for(File StockData:DateData.listFiles()){
						System.out.println(StockData.getName());
						File FormatStockData = new File(FormatPath+StockData.getName());
						FileWriter fWriter = new FileWriter(FormatStockData,true);
						FileReader fr = new FileReader(StockData);
						BufferedReader br = new BufferedReader(fr);
						line  = br.readLine();
						while(line != null){
							line = Formatize(line);
							fWriter.write(line+"\n");
							line=br.readLine();
						}
						br.close();
						fWriter.close();
					}
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		long endtime = System.currentTimeMillis();
		System.out.println("\nFinish the formatize process of "+ MergeData.getName());
		System.out.println("Time spent on this : "+(endtime-starttime)/1000/1.0 + "s");
	}
	
	public static String Formatize(String s){
		String formatString = "";
		String words[] = s.split(",",3);
		String date = words[0];
		String time = words[1];
		
		formatString = date + "-" + time + "," + words[2];
		return formatString;
	}
	
}
