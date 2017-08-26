package output;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class LocalOutput {
	public static <E> void toFile(String fileName, List<E> list){
		try(FileOutputStream fileOutputStream = new FileOutputStream(fileName, true);
				OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
				BufferedWriter bufferedWriter = new BufferedWriter(writer);){
			for (E e : list) {
				bufferedWriter.write(e.toString());
				bufferedWriter.newLine();
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
