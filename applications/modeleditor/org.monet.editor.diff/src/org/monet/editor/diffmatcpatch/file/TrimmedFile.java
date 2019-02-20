package org.monet.editor.diffmatcpatch.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.monet.editor.diffmatcpatch.constants.Constants;

public class TrimmedFile {

	private File file;
	
	private FileInputStream fileInputStream = null;
	private BufferedReader reader = null;
	
	public TrimmedFile(File file) {
		this.file = file;
	}
	
	public String fullTrimmedContent(String lineComment) throws IOException {
		String result = "";
		String line;
		FileInputStream fileInputStream = null;
		BufferedReader reader = null;
		try {
			fileInputStream = new FileInputStream(file);
	        reader = new BufferedReader(new InputStreamReader(fileInputStream, Constants.CHARSET));

	        while (reader.ready()) {
	        	line = reader.readLine();
	        	if ((!lineComment.isEmpty()) && line.startsWith(lineComment))
	        		line = line.substring(lineComment.length());
	        	result += line.trim() + Constants.CRLF;
	        }
	        
		} finally {
	        if (reader != null) reader.close();
	        if (fileInputStream != null) fileInputStream.close();
		}
	    
		return result;
	}
	
	public void reset() throws IOException {
		this.close();
		
		this.fileInputStream = new FileInputStream(file);
        this.reader = new BufferedReader(new InputStreamReader(fileInputStream, Constants.CHARSET));
	}
	
	public void close() throws IOException {
        if (this.reader != null) this.reader.close();
        if (this.fileInputStream != null) this.fileInputStream.close();
	}

	public String getLines(int linesCount) throws IOException {
		if (this.reader == null)
			return null;
		
		String result = "";
		String line;
        while (reader.ready() && (linesCount > 0)) {
        	line = reader.readLine();
        	result += line + Constants.CRLF;
        	linesCount--;
        }

        return result;
	}
	
}
