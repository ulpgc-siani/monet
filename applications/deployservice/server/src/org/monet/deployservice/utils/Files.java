package org.monet.deployservice.utils;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class Files {

	private Logger logger;
  private Shell shell = new Shell();


  public Files() {
		logger = Logger.getLogger(this.getClass());
	}

	private String getPath() {
		String path = "";
		try {
			path = new java.io.File(".").getCanonicalPath();
		} catch (Exception exception) {
			logger.error("Unable to read current path.");
		}
		return path;
	}

	public void copy(String src, String dst) throws IOException {
		try (InputStream in = new FileInputStream(src)) {
			try (OutputStream out = new FileOutputStream(dst)) {

				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			}
		}
	}

	public void remove(String fileName) {
		logger.debug("Delete file: " + fileName);
		if (! fileExists(fileName))
			logger.warn("Delete file: " + fileName +". File not exists.");
		else {
			File file = new File(fileName);
			try {
				boolean success = file.delete();
				if (!success)
					throw new IllegalArgumentException("Delete: deletion failed.");
			} catch (Exception e) {
				ExceptionPrinter exeptionPrinter = new ExceptionPrinter();
				logger.error(e.getMessage() + " File: " + fileName + "\nStackTrace: " + exeptionPrinter.printToString(e));
			}
		}
	}

	public void removeDir(String dir) {
		if (fileExists(dir)) {
			File directory = new File(dir);
			File[] files = directory.listFiles();

			for (int x = 0; x < files.length; x++) {

				if (files[x].isDirectory()) {
					this.removeDir(dir + "/" + files[x].getName());
				}

				File file = new File(dir + "/" + files[x].getName());
				if ((file.exists()) && (!file.isDirectory()) && (!file.delete())) {
					logger.error("I can't delete file '" + dir + "/" + files[x].getName() + "'.");
				}
			}
			if (!directory.delete()) {
				logger.error("I can't delete directory '" + dir + "'.");
			}
		} else {
			logger.info("Removing dir: Directory '" + dir + "' not exists.");
		}
	}

	public void makeDir(String dir) {
		logger.info("Making dir: " + dir);
		File directory = new File(dir);

		if (!directory.exists())
			if (!directory.mkdir()) {
				logger.error("I can't create directory '" + dir + "'.");
			}
	}

	public void makeDir(String dir, String user, String group) throws IOException, InterruptedException {
		logger.info("Making dir: " + dir + ", with user: " + user);
		File directory = new File(dir);

		if (!directory.exists()) {
			makeDir(dir);
			chown(dir, user);			
			chgrp(dir, group);			
		}
	}

	public Boolean fileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	public Boolean isDirectory(String fileName) {
		File file = new File(fileName);
		return file.isDirectory();
	}

	public String[] fileList(String dir) {
		File directory = new File(dir);
		File[] files = directory.listFiles();
		ArrayList<String> fileList = new ArrayList<String>();

		for (int x = 0; x < files.length; x++) {

			if (files[x].isDirectory()) {
				String[] list = this.fileList(files[x].getAbsolutePath());

				for (int y = 0; y < list.length; y++) {
					fileList.add(list[y]);
				}
			} else {
				fileList.add(files[x].getAbsoluteFile().toString());
			}
		}

		String[] result = new String[fileList.size()];
		for (int x = 0; x < result.length; x++) {
			result[x] = fileList.get(x);
		}

		return result;
	}

	public String[] dirList(String dir) {
		File directory = new File(dir);
		File[] files = directory.listFiles();
		ArrayList<String> dirList = new ArrayList<String>();

		for (int x = 0; x < files.length; x++) {
			if (files[x].isDirectory()) {
				dirList.add(files[x].getAbsolutePath());
			}
		}

		String[] result = new String[dirList.size()];
		for (int x = 0; x < result.length; x++) {
			result[x] = dirList.get(x);
		}

		return result;
	}

	public String baseName(String fileAbs) {
		File file = new File(fileAbs);
		return file.getName();
	}

	public void renameFile(String fileFrom, String fileTo) {
    logger.info("Rename file from '"+fileFrom+"' to '"+fileTo+"'");
		try {
			copy(fileFrom, fileTo);
			remove(fileFrom);
		} catch (IOException e) {
			logger.error("I can't rename '" + fileFrom + "' to '" + fileTo + "'. Message: " + e.getMessage());
		}
	}

	public void chown(String dir, String user, String group) {
		chown(dir, user);
		chgrp(dir, group);
	}

	private void chown(String dir, String user) {
		if (SystemOS.isWindows()) dir = dir.replace("/", "\\");

		String command = "chown " + user + " " + dir;
    if (SystemOS.isWindows()) command = "subinacl /file "+dir+" /setowner="+user;
    logger.info(command);
    shell.executeCommand(command, new File(getPath()));

    if (! SystemOS.isWindows() && (isDirectory(dir)) && (new File(dir).listFiles().length > 0)) {
      command = "chown " + user + " " +dir+ "/* -R -H";
      logger.info(command);
      shell.executeCommand(command, new File(getPath()));
    }
  }
	
	private void chgrp(String dir, String group) {
		if (SystemOS.isWindows()) dir = dir.replace("/", "\\");

		String command = "chgrp " + group + " " + dir;
    if (SystemOS.isWindows()) command = "subinacl /file "+dir+" /setprimarygroup="+ group;
    logger.info(command);
    shell.executeCommand(command, new File(getPath()));

    if (! SystemOS.isWindows() && (isDirectory(dir)) && (new File(dir).listFiles().length > 0)) {
      command = "chgrp " + group + " " + dir + "/* -R";
      logger.info(command);
      shell.executeCommand(command, new File(getPath()));
    }
	}
	
	public void ln(String fileSource, String fileLink) throws IOException, InterruptedException {
		if (! SystemOS.isWindows()) {
			String command = "";
			String[] commands;
			Process process;

			command = " sudo ln -s \"" + fileSource + "\" \"" + fileLink + "\"";
			commands = new String[]{"sh", "-c", command};
			logger.info(command);

			process = Runtime.getRuntime().exec(commands, null, new File(fileSource).getParentFile());
			process.waitFor();
		}
		else {
      if (SystemOS.isWindows()) {
//        Shell shell = new Shell();
        String command = "mklink /D \"" + fileLink + "\" \"" + fileSource + "\"";
        logger.info(command);
        shell.executeCommand(command, new File(getPath()));
      }
    }
	}

	public void chmod(String fileName, String parameters) throws IOException, InterruptedException {
		if (! SystemOS.isWindows()) {
		  String command = "";
		  String[] commands;
		  Process process;

		  command = " sudo chmod " + parameters + " \"" + fileName + "\"";
		  commands = new String[] { "sh", "-c", command };
		  logger.info(command);

		  process = Runtime.getRuntime().exec(commands, null, new File(fileName).getParentFile());
		  process.waitFor();
		}
	}

	public void replaceTextInFile(String fileName, String fromText, String toText) {

	  String oldtext = "";
		try {
			File file = new File(fileName);
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line = "";

        StringBuilder buf = new StringBuilder();
        while ((line = reader.readLine()) != null) {
          buf.append(line + "\n");
        }
        oldtext = buf.toString();
			}

			String newtext = oldtext.replaceAll(fromText, toText);

			try (FileWriter writer = new FileWriter(fileName)) {
        writer.write(newtext);
      }
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void saveTextFile(String fileName, String Text) {
		try {
			try (FileWriter writer = new FileWriter(fileName)) {
        writer.write(Text);
      }
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public String loadTextFile(String fileName) {
		File file = new File(fileName);
		StringBuffer contents = new StringBuffer();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				contents.append(text).append(System.getProperty("line.separator"));
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

		// show file contents here
		return contents.toString();
	}

	/**
	 * This function will copy files or directories from one location to another.
	 * note that the source and the destination must be mutually exclusive. This
	 * function can not be used to copy a directory to a sub directory of itself.
	 * The function will also have problems if the destination files already
	 * exist.
	 * 
	 * @param src
	 *          -- A File object that represents the source for the copy
	 * @param dest
	 *          -- A File object that represnts the destination for the copy.
	 * @throws IOException
	 *           if unable to copy.
	 */
	public void copyFiles(File src, File dest) throws IOException {
		// Check to ensure that the source is valid...
		if (!src.exists()) {
			throw new IOException("copyFiles: Can not find source: " + src.getAbsolutePath() + ".");
		} else if (!src.canRead()) { // check to ensure we have rights to the
			                           // source...
			throw new IOException("copyFiles: No right to source: " + src.getAbsolutePath() + ".");
		}
		// is this a directory copy?
		if (src.isDirectory()) {
			if (!dest.exists()) { // does the destination already exist?
				// if not we need to make it exist if possible (note this is mkdirs not
				// mkdir)
				if (!dest.mkdirs()) {
					throw new IOException("copyFiles: Could not create direcotry: " + dest.getAbsolutePath() + ".");
				}
			}
			// get a listing of files...
			String list[] = src.list();
			// copy all the files in the list.
			for (int i = 0; i < list.length; i++) {
				File dest1 = new File(dest, list[i]);
				File src1 = new File(src, list[i]);
				copyFiles(src1, dest1);
			}
		} else {
			// This was not a directory, so lets just copy the file
			FileInputStream fin = null;
//			FileOutputStream fout = null;
			byte[] buffer = new byte[4096]; // Buffer 4K at a time (you can change
			                                // this).
			int bytesRead;
			try {
				// open the files for input and output
				fin = new FileInputStream(src);
        try (FileOutputStream fout = new FileOutputStream(dest)) {
          // while bytesRead indicates a successful read, lets write...
          while ((bytesRead = fin.read(buffer)) >= 0) {
            fout.write(buffer, 0, bytesRead);
          }
        }

			} catch (IOException e) { // Error copying file...
				IOException wrapper = new IOException("copyFiles: Unable to copy file: " + src.getAbsolutePath() + "to" + dest.getAbsolutePath() + ".");
				wrapper.initCause(e);
				wrapper.setStackTrace(e.getStackTrace());
				throw wrapper;
			} finally { // Ensure that the files are closed (if they were open).
				if (fin != null) {
					fin.close();
				}
/*				if (fout != null) {
					fout.close();
				}*/
			}
		}
	}

	public String getDirTemp() {
		return System.getProperty("java.io.tmpdir");
	}

	public String getFileTemp() {
		long l = System.currentTimeMillis();
		return getDirTemp() + "/" + l;
	}
	
  private byte[] createChecksum(String filename) throws Exception {
    MessageDigest complete = MessageDigest.getInstance("MD5");
    try (InputStream fis =  new FileInputStream(filename)) {

      byte[] buffer = new byte[1024];
      int numRead;

      do {
        numRead = fis.read(buffer);
        if (numRead > 0) {
          complete.update(buffer, 0, numRead);
        }
      } while (numRead != -1);

    }
    return complete.digest();
  }

  public String getMd5(String filename) throws Exception {
    byte[] b = createChecksum(filename);
    String result = "";

    for (int i=0; i < b.length; i++) {
        result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
    }
    return result;
  }	
	
	
}
