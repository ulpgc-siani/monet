package org.monet.editor.library;

public class LibraryFile {
	
	private LibraryFile() {
		
	}

	public static String getBasename(String filename) {
		Integer index = filename.lastIndexOf(".");
		if (index == -1)
			return null;
		return filename.substring(0, index);
	}

	public static String getFilename(String filename) {
		filename = filename.replaceAll("\\\\", "/");
		Integer index = filename.lastIndexOf("/");
		if (index == -1)
			return filename;
		return filename.substring(index + 1);
	}

	public static String getFilenameWithoutExtension(String filename) {
		filename = filename.replaceAll("\\\\", "/");
		
		if (filename.charAt(0) == '.') 
		  return filename;
		
		Integer index = filename.lastIndexOf("/");
		int dotIndex = filename.lastIndexOf(".");
		if (index == -1) {
		  if(dotIndex < 0)
		    return filename; 
		  return filename.substring(0, dotIndex); 
	  }
		return filename.substring(index + 1, dotIndex);
	}

	public static String getExtension(String filename) {
		Integer index = filename.lastIndexOf(".");
		if (index == -1)
			return null;
		return filename.substring(index + 1);
	}

	public static String getDirname(String filename) {

		filename = filename.replaceAll("\\\\", "/");
		Integer index = filename.lastIndexOf("/");
		if (index == -1)
			return null;
		return filename.substring(0, index);
	}
}