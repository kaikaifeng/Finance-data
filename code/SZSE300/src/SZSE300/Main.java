package SZSE300;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
	public static void main(String[] args){
		//��һ�������Ƕ�����ļ���
		System.out.println(args[0]);
		//�ڶ������������ݿ�����
		System.out.println(args[1]);
		//�������������û���
		System.out.println(args[2]);
		//���ĸ������Ƿ���������
		System.out.println(args[3]);
		//����������Ǳ�ǰ׺
		System.out.println(args[4]);
		//�����������Ǳ��׺
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
