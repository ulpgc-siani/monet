package org.monet.deployservice.utils;

import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


@SuppressWarnings("rawtypes")
public class Zip {

  private Logger logger;

	public Zip() {
    logger = Logger.getLogger(this.getClass());
	}
	
	private void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}

	public void unCompress(String fileName, String dir) {
		Enumeration entries;

		if (!dir.equals(""))
			dir = dir + "/";

		try {
      try (ZipFile zipFile = new ZipFile(fileName)) {
        entries = zipFile.entries();

        String dirName = "";

        while (entries.hasMoreElements()) {
          ZipEntry entry = (ZipEntry) entries.nextElement();

          if (entry.isDirectory()) {
            dirName = dir + entry.getName();
            if ((!(new File(dirName)).exists()) && (!(new File(dirName)).mkdir())) {
              System.out.println("I can't create directory '" + dirName + "'.");
            }
            continue;
          }

          copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(dir + entry.getName())));
        }
      }
		} catch (IOException ioe) {
			System.err.println("Unhandled exception:");
			ioe.printStackTrace();
			return;
		}
	}

	public void unCompressOnlyFile(String fileZip, String fileFrom, String fileTo) {
//    logger.info("UnCompress only file '"+fileZip+"' from '"+fileFrom + "' to '"+fileTo+"'");

		Enumeration entries;
		if (!new File(fileZip).exists()) {
			System.err.println("File not exits: " + fileZip);
			return;
		}
		
		try {
      try (ZipFile zipFile = new ZipFile(fileZip)) {

        entries = zipFile.entries();
        String fileDest;

        while (entries.hasMoreElements()) {
          ZipEntry entry = (ZipEntry) entries.nextElement();

          if (entry.getName().length() >= fileFrom.length()) {
            if (entry.getName().substring(0, fileFrom.length()).equals(fileFrom)) {

              if (entry.getName().length() == fileFrom.length())
                fileDest = fileTo;
              else {
                File fileToReal = new File(fileTo);
                if (!fileToReal.isDirectory())
                  continue;

                fileDest = fileTo + "/" + entry.getName().substring(fileFrom.length() + 1);
              }

              if (entry.isDirectory()) {
                if ((!(new File(fileDest)).exists()) && (!(new File(fileDest)).mkdirs())) {
                  System.out.println("I can't create directory '" + fileDest + "'.");
                }
              } else {
                copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(fileDest)));
              }
            }
          }
        }
      }
		} catch (IOException ioe) {
			System.err.println("Unhandled exception:");
			ioe.printStackTrace();
			return;
		}

	}

	public String unCompressOnlyFile(String fileZip, String fileFrom) {
		String result = "";
		Files files = new Files();
		String fileTemp = files.getFileTemp();

		unCompressOnlyFile(fileZip, fileFrom, fileTemp);
		result = files.loadTextFile(fileTemp);
		files.remove(fileTemp);

		return result;
	}

	public void compress(String fileName, String dir) {
	  logger.info("Compress dir '"+dir+"' to '"+fileName);
	  logger.debug("Details: " + App.getStackTrace());

		Files files = new Files();
		String[] filenames = files.fileList(dir);

		byte[] buf = new byte[4096];

		try {
			String outFilename = fileName;
			try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename))) {

				for (int i = 0; i < filenames.length; i++) {
					try (FileInputStream in = new FileInputStream(filenames[i])) {

						String file = filenames[i].substring(dir.length() + 1);
						if (SystemOS.isWindows()) file = file.replace("\\", "/");

						out.putNextEntry(new ZipEntry(file));

						int len;
						while ((len = in.read(buf)) != -1) {
							out.write(buf, 0, len);
						}

						out.closeEntry();
					}
				}

				out.flush();
			}
		} catch (IOException e) {
		}
		
	}

	public Boolean fileExists(String fileZip, String fileName) {
		Boolean result = false;
		ZipFile zipFile;

		try {
			zipFile = new ZipFile(fileZip);
			Enumeration e = zipFile.entries();

			while (e.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) e.nextElement();

				if (entry.getName().toLowerCase().equals(fileName.toLowerCase())) {
					result = true;
					break;
				}
			}

			zipFile.close();
		} catch (IOException ioe) {
			System.out.println("Error opening zip file: " + fileZip + ", file into zip: " + fileName + "\nException:");
			ioe.printStackTrace();
		}

		return result;
	}

  public Boolean isValid(String fileZip) {
    ZipFile zipfile = null;
    try {
        zipfile = new ZipFile(fileZip);
        return true;
    } catch (ZipException e) {
        return false;
    } catch (IOException e) {
        return false;
    } finally {
        try {
            if (zipfile != null) {
                zipfile.close();
            }
        } catch (IOException e) {
        }
    }  	
  }  
}
