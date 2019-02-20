package org.monet.grided.library;

import java.io.File;

import org.apache.tomcat.util.http.fileupload.FileItem;

public class LibraryFileUploader {
  
  public File uploadFile(FileItem item, String directoryName, String fileName) throws Exception {
    File file;   
    File directory = new File(directoryName);
    if (! directory.exists()) { directory.mkdirs(); }

    file = new File(directory.getAbsolutePath(), fileName);    
    item.write(file); 
    return file;
  }
}
