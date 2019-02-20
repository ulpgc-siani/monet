package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Files {
	
	public void saveTextFile(String fileName, String Text) {
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.write(Text);
			writer.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public List<String> loadTextFile(String fileName) {
		File file = new File(fileName);
		List<String> result = new ArrayList<String>();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				result.add(text);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
}
