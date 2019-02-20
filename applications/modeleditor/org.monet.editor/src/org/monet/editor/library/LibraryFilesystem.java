package org.monet.editor.library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class LibraryFilesystem {

  private LibraryFilesystem() {

  }

  public static String getReaderContent(Reader reader) {
    StringBuffer sbContent = new StringBuffer();
    BufferedReader oBufferedReader;
    String sLine;

    try {
      oBufferedReader = new BufferedReader(reader);
      while ((sLine = oBufferedReader.readLine()) != null) {
        sbContent.append(sLine + "\n\r");
      }
    } catch (IOException oException) {
      throw new FileSystemException("Could not get content from reader", oException);
    }

    return sbContent.toString();
  }

  private static String readContents(InputStream stream) throws IOException {
    StringWriter writer = new StringWriter();
    try {
      InputStreamReader input = new InputStreamReader(stream, "UTF-8");
      int len;
      char[] buff = new char[16384];
      while ((len = input.read(buff)) > 0)
        writer.write(buff, 0, len);
    } finally {
      StreamHelper.close(stream);
    }

    return writer.toString();
  }

  public static String readContents(File source) {
    try {
      return readContents(new FileInputStream(source));
    } catch (Exception exception) {
      throw new FileSystemException("Could not read file " + source.getAbsolutePath(), exception);
    }
  }

  public static String readContents(IFile source) {
    InputStream inputStream = null;
    try {
      if (!source.isSynchronized(IFile.DEPTH_ZERO))
        source.refreshLocal(IFile.DEPTH_ZERO, null);
      inputStream = source.getContents();
      return readContents(inputStream);
    } catch (Exception exception) {
      throw new FileSystemException("Could not read file " + source.getLocation().toString(), exception);
    }
  }

  public static void writeContents(IFile destination, String content) {
    FileOutputStream outputStream = null;
    try {
      File file = destination.getLocation().toFile();
      if (!destination.exists()) {
        LibraryFilesystem.forceDir(destination);
        file.getParentFile().mkdirs();
        file.createNewFile();
      }

      outputStream = new FileOutputStream(file);
      OutputStreamWriter oWriter = new OutputStreamWriter(outputStream, "UTF-8");
      oWriter.write(content);
      oWriter.close();
      destination.refreshLocal(IFile.DEPTH_ZERO, null);
    } catch (Exception oException) {
      throw new FileSystemException("Could not write file" + destination.getLocation().toString(), oException);
    } finally {
      StreamHelper.close(outputStream);
    }
  }

  public static void writeContents(File destination, InputStream stream) {
    FileOutputStream outputStream = null;
    try {
      if (!destination.exists()) {
        destination.getParentFile().mkdirs();
        destination.createNewFile();
      }

      outputStream = new FileOutputStream(destination);
      StreamHelper.copyData(stream, outputStream);
    } catch (Exception oException) {
      throw new FileSystemException("Could not write file" + destination.getAbsolutePath(), oException);
    } finally {
      StreamHelper.close(outputStream);
    }
  }

  public static Boolean copyFile(String source, String destination) {
    return copyFile(new File(source), new File(destination));
  }

  public static Boolean copyFile(File source, File destination) {

    LibraryFilesystem.forceDir(destination.getParentFile().getAbsolutePath());

    try {
      InputStream in = new FileInputStream(source);
      OutputStream out = new FileOutputStream(destination);

      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      in.close();
      out.close();
    } catch (IOException ex) {
      return false;
    }

    return true;
  }

  public static void listFiles(String dirIn, List<File> outFiles, boolean recursive) {
    File dir = new File(dirIn);
    File[] files = dir.listFiles();
    if (files != null)
      for (int i = 0; i < files.length; i++) {
        if (files[i].isFile())
          outFiles.add(files[i]);
        else if (recursive)
          listFiles(files[i].getAbsolutePath(), outFiles, recursive);
      }
  }

  public static void listDirs(String dirIn, List<File> outdirs, boolean recursive) {
    File dir = new File(dirIn);
    File[] files = dir.listFiles();

    if (files != null)
      for (int i = 0; i < files.length; i++) {
        if (files[i].isDirectory()) {
          outdirs.add(files[i]);
          if (recursive)
            listFiles(files[i].getAbsolutePath(), outdirs, recursive);
        }
      }
  }

  public static String[] listFiles(String directoryName) {
    File[] aFiles = null;
    ArrayList<String> resultList = new ArrayList<String>();
    String[] result;
    FilenameFilter filter;
    Integer iPos;

    filter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return !name.startsWith(".");
      }
    };

    aFiles = new File(directoryName).listFiles(filter);
    for (iPos = 0; iPos < aFiles.length; iPos++) {
      if (aFiles[iPos].isDirectory())
        continue;
      resultList.add(aFiles[iPos].getName());
    }

    result = new String[resultList.size()];
    return (String[]) resultList.toArray(result);
  }

  public static String[] listDir(File directory) {
    FilenameFilter filter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return !name.startsWith(".");
      }
    };

    return directory.list(filter);
  }

  public static Boolean forceDir(String directory) {
    return new File(directory).mkdirs();
  }

  public static Boolean existFile(String filename) {
    return new File(filename).exists();
  }

  public static void forceDir(IFile destination) throws CoreException {
    forceDir(destination.getParent());
  }

  public static void forceDir(IContainer folder) throws CoreException {
    if (folder.exists())
      return;
    else if (!folder.getParent().exists())
      forceDir(folder.getParent());
    ((IFolder) folder).create(false, false, null);
  }

  public static void cleanFolder(IFolder proxiesFolder) throws CoreException {
    for (IResource resource : proxiesFolder.members()) {
      resource.delete(true, null);
    }
  }

}
