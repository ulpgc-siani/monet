package org.monet.filesystem.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.monet.filesystem.StreamHelper;



public class LibraryFile {

  public static final String DOT              = ".";
  public static final String BAR45            = "/";
  public static final String BAR135           = "\\";

  public static String getBasename(String sFilename) {
    Integer iPos = sFilename.lastIndexOf(DOT);
    if (iPos == -1) return null;
    return sFilename.substring(0, iPos);
  }

  public static String getDirname(String sFilename) {
    sFilename = sFilename.replaceAll(BAR135+BAR135, BAR45);
    Integer iPos = sFilename.lastIndexOf(BAR45);
    if (iPos == -1) return null;
    return sFilename.substring(0, iPos);
  }

  public static String getFilename(String sFilename) {
    sFilename = sFilename.replaceAll(BAR135+BAR135, BAR45);
    Integer iPos = sFilename.lastIndexOf(BAR45);
    if (iPos == -1) return null;
    return sFilename.substring(iPos+1);
  }

  public static String getFilenameWithoutExtension(String sFilename) {
    sFilename = sFilename.replaceAll(BAR135+BAR135, BAR45);
    Integer iPos = sFilename.lastIndexOf(BAR45);
    int iDotPos = sFilename.lastIndexOf(DOT);
    if (iPos == -1) return null;
    return sFilename.substring(iPos+1, iDotPos);
  }

  public static String getExtension(String sFilename) {
    Integer iPos = sFilename.lastIndexOf(DOT);
    if (iPos == -1) return null;
    return sFilename.substring(iPos+1);
  }

  public static List<File> getAllFilesinDirectory(String dir, boolean recursive){
    List<File> list = new ArrayList<File>();
    listFiles(dir, dir.length(), list, recursive);
    return list;
  }

  private static void listFiles(String dirIn, int lenght, List<File> outFiles, boolean recursive){
    File dir = new File(dirIn);
    File[] files = dir.listFiles();

    files = dir.listFiles();

    for (int i = 0; i < files.length; i++) {
      if(files[i].isFile()) outFiles.add(files[i]);
      else if(recursive) listFiles(files[i].getAbsolutePath(), lenght, outFiles, recursive);
    }
  }

  public static void replaceTextInFile(String fileName, String fromText, String toText) throws Exception {
    File file = new File(fileName);
    replaceTextInFile(file,fromText,toText);
  }

  public static void replaceTextInFile(File file, String fromText, String toText) throws Exception {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line = "";
    StringBuilder buffer = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      buffer.append(line + "\r\n");
    }
    reader.close();
    String newtext = buffer.toString().replaceAll(fromText, toText);

    FileWriter writer = new FileWriter(file);
    writer.write(newtext);
    writer.close();
  }

  public static void replaceTextInFile(String fileName, Map<String,String> replaceTextMap) throws Exception {
    File file = new File(fileName);
    replaceTextInFile(file, replaceTextMap);
  }

  public static void replaceTextInFile(File file, Map<String,String> replaceTextMap) throws Exception {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line = "";
    StringBuilder buffer = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      buffer.append(line + "\r\n");
    }
    reader.close();

    String newtext = buffer.toString();
    for (String from : replaceTextMap.keySet()) {
      newtext = newtext.replaceAll(from, replaceTextMap.get(from));  
    }

    FileWriter writer = new FileWriter(file);
    writer.write(newtext);
    writer.close();
  }

  public static void copy(String fromFileName, String toFileName) throws Exception {
    copy(new File(fromFileName), new File(toFileName));
  }

  public static void copy(File fromFile, File toFile) throws Exception {
    if (!fromFile.exists())  throw new IOException("FileCopy: " + "no such source file: "  + fromFile.getName());
    if (!fromFile.isFile())throw new IOException("FileCopy: " + "can't copy directory: " + fromFile.getName());
    if (!fromFile.canRead())throw new IOException("FileCopy: " + "source file is unreadable: "+ fromFile.getName());

    if (toFile.isDirectory())
      toFile = new File(toFile, fromFile.getName());

    if (toFile.exists()) {
      if (!toFile.canWrite())
        throw new IOException("FileCopy: " + "destination file is unwriteable: " + toFile.getName());
    } else {
      String parent = toFile.getParent();
      if (parent == null) parent = System.getProperty("user.dir");
      File dir = new File(parent);
      if (!dir.exists()) throw new IOException("FileCopy: "  + "destination directory doesn't exist: " + parent);
      if (dir.isFile()) throw new IOException("FileCopy: " + "destination is not a directory: " + parent);
      if (!dir.canWrite())throw new IOException("FileCopy: "+ "destination directory is unwriteable: " + parent);
    }

    FileInputStream from = null;
    FileOutputStream to = null; 
    from = new FileInputStream(fromFile);
    to = new FileOutputStream(toFile);
    StreamHelper.copy(from, to);
  }

}