package org.monet.editor.diffmatcpatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.monet.editor.diffmatcpatch.constants.Constants;
import org.monet.editor.diffmatcpatch.file.TrimmedFile;
import org.monet.editor.diffmatcpatch.model.Diff;
import org.monet.editor.diffmatcpatch.model.LinesToCharsResult;
import org.monet.editor.diffmatcpatch.model.Operation;


public class DiffLineMode {
	
	private DiffLibrary diffLibrary;
	
	private String deleteLineMark;
	private String deleteBlockMark;
	
	public DiffLineMode() {
		this.diffLibrary = new DiffLibrary();
	}

	private LinkedList<Diff> diff_lineMode(String text1, String text2) {
		LinesToCharsResult linesToCharResult = diffLibrary.diff_linesToChars(text1, text2);
		
		String lineText1 = linesToCharResult.chars1;
		String lineText2 = linesToCharResult.chars2;

		LinkedList<Diff> diffs = diffLibrary.diff_main(lineText1, lineText2, true);
		
		diffLibrary.diff_charsToLines(diffs, linesToCharResult.lineArray);
		
		return diffs;
	}
	
	private String markDeletedText(String text) throws IOException {
		String out = this.deleteBlockMark + Constants.CRLF;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(text));
			String line = reader.readLine();
			while (line != null) {
				out += (this.deleteLineMark + line + Constants.CRLF);
				line = reader.readLine();
			}
			
			return out;
		} finally {
			if (reader != null) reader.close();
		}
	}
	
	private boolean hasBeenInserted(LinkedList<Diff> diffs, Diff diffToInsert) throws IOException {
		
		String markedText = this.markDeletedText(diffToInsert.text).trim();
		
		int idx = diffs.indexOf(diffToInsert);
		
		if ((idx + 1) < diffs.size()) {
			Diff nextDiff = diffs.get(idx + 1);
			if ((nextDiff.operation == Operation.INSERT) && (nextDiff.text.trim().startsWith(markedText))) {
				return true;
			}
		}
		
		return false;
	}
	
	private int countLines(String text) {
		Matcher m = Pattern.compile("(\n)|(\r)|(\r\n)").matcher(text);
		int lines = 1;
		while (m.find()) {
		    lines ++;
		}
		return lines;
	}

	private void saveFile(LinkedList<Diff> diffs, TrimmedFile theirsFile, TrimmedFile mineFile, File file) throws IOException {
		PrintWriter out = null;
		String theirsText, mineText;
		try {
			theirsFile.reset();
			mineFile.reset();
			
			out = new PrintWriter(file, Constants.CHARSET);
				
			Iterator<Diff> iterator = diffs.iterator();
			while (iterator.hasNext()) {
				Diff diff = iterator.next();
				
				int linesCount = this.countLines(diff.text) - 1;

				switch (diff.operation) {
				case EQUAL:
					theirsText = theirsFile.getLines(linesCount);
					mineText = mineFile.getLines(linesCount);
					
					out.print(mineText);
					break;
					
				case INSERT:
					mineText = mineFile.getLines(linesCount);

					out.print(mineText);
					break;
	
				case DELETE:
					if (this.hasBeenInserted(diffs, diff)) 
						break;
					
					theirsText = theirsFile.getLines(linesCount);					
					out.print(this.markDeletedText(theirsText));
					break;
				}
			}
		} finally {
			theirsFile.close();
			mineFile.close();
			if (out != null) out.close();
		}
	}	
	
	public void execute(File theirs, File mine, File fileout) throws IOException {
	
		TrimmedFile theirsFile = new TrimmedFile(theirs);
		TrimmedFile mineFile = new TrimmedFile(mine);
		
		String theirsText = theirsFile.fullTrimmedContent(this.deleteLineMark);
		String mineText = mineFile.fullTrimmedContent(this.deleteLineMark);
		
		LinkedList<Diff> diffs = this.diff_lineMode(theirsText, mineText);
	
		this.saveFile(diffs, theirsFile, mineFile, fileout);
	}

	public String getDeleteLineMark() {
	  return deleteLineMark;
  }

	public void setDeleteLineMark(String deleteLineMark) {
	  this.deleteLineMark = deleteLineMark;
  }

	public String getDeleteBlockMark() {
	  return deleteBlockMark;
  }

	public void setDeleteBlockMark(String deleteBlockMark) {
	  this.deleteBlockMark = deleteBlockMark;
  }

}
