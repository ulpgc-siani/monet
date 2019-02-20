/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.editor.preview.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;

public class FilesystemHelper {

  protected FilesystemHelper() {
  }

  public static String[] listDir(String sDirname) {
    FilenameFilter oFilter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
          return !name.startsWith(".");
      }
    };
    
    return new File(sDirname).list(oFilter);
  }

  public static String[] listFiles(String sDirname) {
    File[] aFiles = null;
    ArrayList<String> alResult = new ArrayList<String>();
    String[] aResult;
    FilenameFilter oFilter;
    Integer iPos;
    
    oFilter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
          return !name.startsWith(".");
      }
    };
    
    aFiles = new File(sDirname).listFiles(oFilter);
    for (iPos=0; iPos<aFiles.length; iPos++) {
      if (aFiles[iPos].isDirectory()) continue;
      alResult.add(aFiles[iPos].getName());
    }
    
    aResult = new String[alResult.size()];
    return (String[])alResult.toArray(aResult);
  }

  public static Boolean createDir(String sDirname) {
    return new File(sDirname).mkdir();
  }

  public static Boolean renameDir(String sSource, String sDestination) {
    File oDestination = new File(sDestination);
    return new File(sSource).renameTo(oDestination);
  }

  public static Boolean removeDir(String sDirname) {
    File oFile = new File(sDirname);
    return removeDir(oFile);
  }
  
  public static Boolean removeDir(File oFile) {    
    if (oFile.exists()) {
      File[] aFiles = oFile.listFiles();
      for(int iPos=0; iPos<aFiles.length; iPos++) {
         if(aFiles[iPos].isDirectory()) {
           FilesystemHelper.removeDir(aFiles[iPos].getAbsolutePath());
         }
         else {
           aFiles[iPos].delete();
         }
      }
    } else {
      return true;
    }
    
    return (oFile.delete());
  }
  
  public static Boolean copyDir(String sSource, String sDestination) throws IOException {
    File oSource = new File(sSource);
    File oDestination = new File(sDestination);
    return copyDir(oSource, oDestination);
  }
  
  public static Boolean copyDir(File oSource, File oDestination) throws IOException {
    if(oSource.exists())  {
      if (oSource.isDirectory()) {
          if (!oDestination.exists()) {
              oDestination.mkdir();
          }
          
          String[] children = oSource.list();
          for (int i=0; i<children.length; i++) {
            copyDir(new File(oSource, children[i]),
                      new File(oDestination, children[i]));
          }
      } else {
          
          InputStream in = new FileInputStream(oSource);
          OutputStream out = new FileOutputStream(oDestination);
          
          // Copy the bits from instream to outstream
          byte[] buf = new byte[1024];
          int len;
          while ((len = in.read(buf)) > 0) {
              out.write(buf, 0, len);
          }
          in.close();
          out.close();
      }
      return true;
    }
    
    return false;
  }
  
  public static Boolean moveDir(File oSource, File oDestination) {
    if(oDestination.exists()) {
      boolean result = true;
      String[] children = oSource.list();
      for (int i=0; i<children.length; i++) {
        File sourceChild = new File(oSource, children[i]);
        File destinationChild = new File(oDestination, children[i]);
        if(sourceChild.isDirectory()){
          if(!moveDir(sourceChild, destinationChild)) result = false;
        } else {
          if(destinationChild.exists()) destinationChild.delete();
          if(!sourceChild.renameTo(destinationChild)) result = false;
        }
      }
      return result;
    } else {
      return oSource.renameTo(oDestination);
    }
  }
  
  public static Boolean forceDir(String sDirname) {
    return new File(sDirname).mkdirs();
  }

  public static Boolean existFile(String sFilename) {
    return new File(sFilename).exists();
  }

  public static Boolean createFile(String sFilename) throws IOException {
    new File(sFilename).createNewFile();
    return true;
  }
  
  public static Boolean copyFile(String source, String destination) throws IOException {
    return copyFile(new File(source), new File(destination));
  }
    
  public static Boolean copyFile(File source, File destination) throws IOException {

    FilesystemHelper.forceDir(destination.getParentFile().getAbsolutePath());

    InputStream in = new FileInputStream(source);
    OutputStream out = new FileOutputStream(destination);
    
    // Copy the bits from instream to outstream
    byte[] buf = new byte[1024];
    int len;
    while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
    }
    in.close();
    out.close();

    return true;
  }

  
  public static Boolean renameFile(String sSource, String sDestination) {
    File oDestination = new File(sDestination);
    return new File(sSource).renameTo(oDestination);
  }

  public static Boolean removeFile(String sFilename) {
    return new File(sFilename).delete();
  }

  public static Reader getReader(String sFilename) throws UnsupportedEncodingException, FileNotFoundException {
    return new InputStreamReader(new FileInputStream(sFilename), "UTF-8");
  }
    
  public static InputStream getInputStream(String sFilename) throws FileNotFoundException {
    return new FileInputStream(sFilename);
  }

  public static byte[] getBytesFromFile(String sFilename) throws Exception {
    File oFile = new File(sFilename);
    InputStream oStream = null;
    long lLength;
    byte[] aBytes = null;
    int iOffset, iNumRead;
    
    try {
      oStream = new FileInputStream(oFile);
  
      lLength = oFile.length();
      if (lLength > Integer.MAX_VALUE) {
        System.out.println("File is too large " + sFilename);
        throw new Exception();
      }
  
      aBytes = new byte[(int)lLength];
  
      iOffset = 0;
      iNumRead = 0;
      while (iOffset < aBytes.length && (iNumRead=oStream.read(aBytes, iOffset, aBytes.length-iOffset)) >= 0) {
        iOffset += iNumRead;
      }
  
      if (iOffset < aBytes.length) {
        System.out.println("Could not completely read file" + sFilename);
        throw new Exception();
      }
    }
    finally {
      StreamHelper.close(oStream);
    }

    return aBytes;
  }
  
  public static String readFile(String sFilename, String Mode) throws IOException {
    char[] sContent = null;

    File oFile = new File(sFilename);
    InputStreamReader oInput = new InputStreamReader(new FileInputStream(oFile), "UTF-8");
    oInput.read(sContent);
    oInput.close();

    return new String(sContent);
  }
  
  public static String readStream(InputStream is) throws IOException {
    if (is != null) {
      Writer writer = new StringWriter();
    
      char[] buffer = new char[1024];
      try {
        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        int n;
        while ((n = reader.read(buffer)) != -1)
          writer.write(buffer, 0, n);
      } finally {
        is.close();
      }
      return writer.toString();
    } 
    else         
      return "";
  }  

  public static String readFile(String sFilename) throws IOException {
    StringBuffer oContent = new StringBuffer();
    InputStreamReader oInputStreamReader;
    BufferedReader oBufferedReader;
    String sLine;

    oInputStreamReader = new InputStreamReader(new FileInputStream(sFilename), "UTF-8");
    oBufferedReader = new BufferedReader(oInputStreamReader);
    while ((sLine = oBufferedReader.readLine()) != null) {
      oContent.append(sLine + "\n");
    }
    oInputStreamReader.close();

    return oContent.toString();
  }

  public static String getReaderContent(Reader oReader) throws IOException {
    StringBuffer sbContent = new StringBuffer();
    BufferedReader oBufferedReader;
    String sLine;

    oBufferedReader = new BufferedReader(oReader);
    while ((sLine = oBufferedReader.readLine()) != null) {
      sbContent.append(sLine + "\n");
    }

    return sbContent.toString();
  }

  public static Boolean writeFile(String sFilename, String sContent) throws IOException {

    OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(sFilename), "UTF-8");
    oWriter.write(sContent);
    oWriter.close();

    return true;
  }
  
  public static Boolean writeFile(File file, InputStream content) throws IOException {
    FileOutputStream outputStream = null;
    
    try {
      outputStream = new FileOutputStream(file);
      copyStream(content, outputStream);
    } finally {
      StreamHelper.close(outputStream);
    }
    
    return true;
  }

  public static Writer getWriter(String sFilename) throws UnsupportedEncodingException, FileNotFoundException {
    return new OutputStreamWriter(new FileOutputStream(sFilename), "UTF-8");
  }

  public static OutputStream getOutputStream(String sFilename) throws FileNotFoundException {
    return new FileOutputStream(sFilename);
  }

  public static Boolean appendToFile(String sFilename, String sContent) throws IOException {

    FileWriter oFileWriter = new FileWriter(sFilename, true);
    oFileWriter.write(sContent);
    oFileWriter.close();

    return true;
  }

  public static void copyStream(InputStream input, OutputStream output) throws IOException {
    byte[] buffer = new byte[4096];
    int readed = 0;
    while((readed = input.read(buffer)) > 0) {
      output.write(buffer, 0, readed);
    }
  }
  
  private static boolean ensureDirectoryExists(final File f) {
    return f.exists() || f.mkdir();
  }
  
  public static boolean copyJarResourcesRecursively(final File destDir,
      final JarURLConnection jarConnection) throws IOException {

    final JarFile jarFile = jarConnection.getJarFile();

    for (final Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
      final JarEntry entry = e.nextElement();
      if (entry.getName().startsWith(jarConnection.getEntryName())) {
        final String filename = StringUtils.removeStart(entry.getName(), //
            jarConnection.getEntryName());

        final File f = new File(destDir, filename);
        if (!entry.isDirectory()) {
          final InputStream entryInputStream = jarFile.getInputStream(entry);
          StreamHelper.copyData(entryInputStream, f);
          entryInputStream.close();
        } else {
          if (!FilesystemHelper.ensureDirectoryExists(f)) {
            throw new IOException("Could not create directory: "
                + f.getAbsolutePath());
          }
        }
      }
    }
    return true;
  }
  
  public static boolean copyResourcesRecursively(final URL originUrl, final File destination) throws IOException {
    final URLConnection urlConnection = originUrl.openConnection();
    if (urlConnection instanceof JarURLConnection) {
      return FilesystemHelper.copyJarResourcesRecursively(destination,
          (JarURLConnection) urlConnection);
    } else {
      return FilesystemHelper.copyDir(new File(originUrl.getPath()), destination);
    }
  }
  
}
