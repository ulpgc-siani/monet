package org.monet.grided.core.lib;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;


public interface Filesystem {

//    public static final String NEW_LINE = System.getProperty("line.separator");
//    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String NEW_LINE = "\n";
    public static final String FILE_SEPARATOR = "/";
//	  public static String NEW_LINE = "";

    public String[] listDir(String sDirname);

    public String[] listFiles(String sDirname);
    
    public String[] listFiles(String sDirname, FilenameFilter filter);
    
    public boolean createDir(String sDirname);
    
    public boolean renameDir(String sSource, String sDestination);
    
    public boolean removeDir(String sDirname);
    
    public boolean copyDir(String sSource, String sDestination);

    public boolean copyDir(File oSource, File oDestination);
    
    public void copyFile(String filename, String dataPath);
    
    public boolean mkdirs(String sDirname);

    public boolean existFile(String sFilename);

    public boolean createFile(String sFilename);

    public boolean renameFile(String sSource, String sDestination);

    public boolean removeFile(String sFilename);

    public Reader getReader(String sFilename);
    
    public InputStream getInputStream(String sFilename);
    
    public InputStream getInputStreamFrom(String content);
    
    public OutputStream getOutputStream(String sFilename);
    
    public byte[] getBytesFromFile(String sFilename);
    
    public byte[] getBytesFromInputStream(InputStream is);
    
    public String readFile(String sFilename, String Mode);
    
    public String readFile(String sFilename);
    
    public String getReaderContent(Reader oReader);
    
    public Writer getWriter(String sFilename);
    
    public void writeFile(String sFilename, String sContent);

    public void appendToFile(String sFilename, String sContent);
    
    public String getFilename(String filename);
    
}
