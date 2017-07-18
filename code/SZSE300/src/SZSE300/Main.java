package SZSE300;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
	public static void main(String[] args){
		//第一个参数是读入的文件名
		System.out.println(args[0]);
		//第二个参数是数据库名称
		System.out.println(args[1]);
		//第三个参数是用户名
		System.out.println(args[2]);
		//第四个参数是服务器名称
		System.out.println(args[3]);
		//第五个参数是表前缀
		System.out.println(args[4]);
		//第六个参数是表后缀
		System.out.println(args[5]);
		String fileName = args[0];
		String dataBase = args[1];
		String user = args[2];
		String server = args[3];
		String pre = args[4];
		String suff = args[5];
		
		toCSV(fileName, "SZSE300.csv");
		sqlScript("sql.sql", user, server, dataBase, pre, suff, fileName);
	}
	
	public static void toCSV(String fileName, String newName){
		try(FileReader fileReader = new FileReader(fileName);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				FileWriter fileWriter = new FileWriter(newName);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);){
			String string = null;
			while((string = bufferedReader.readLine()) != null){
				String tmp = string.replaceAll(",", "");
				tmp = tmp.replaceAll("\t", ",");
				bufferedWriter.write(tmp + "\r\n");
			}
		}
		catch (Exception exception) {}
	}
	
	public static void sqlScript(String sqlName, String user, String server, String dataBase, String pre, String suff, String fileName){
		try(FileWriter fileWriter = new FileWriter(sqlName);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				FileReader fileReader = new FileReader(fileName);
				BufferedReader bufferedReader = new BufferedReader(fileReader);){
			String string = null;
			while((string = bufferedReader.readLine()) != null){
				String[] sp = string.split("\t");
				String sql = "EXEC " +
				dataBase + 
				".." +
				"xp_cmdshell " + 
				"\'" +
				"bcp " + 
				dataBase +
				".dbo." + 
				pre + 
				sp[0] + 
				suff +
				" out F:\\test\\" +
				sp[0] + 
				".csv -c -T -S " +
				server + 
				" -U " +
				user +
				" -t " + 
				"\",\"" +
				"\'";
				bufferedWriter.write(sql + "\r\n");
			}
		}
		catch (Exception exception) {}
	}
}
