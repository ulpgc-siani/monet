package org.monet.mobile.service.results;

import java.io.File;

import org.monet.mobile.service.Result;

public class FileResult extends Result {
  
  public FileResult() { }
  
  public FileResult(File resultFile, String contentType, String filename, long size) { 
    this.filename = filename;
    this.resultFile = resultFile;
    this.contentType = contentType;
    this.size = size;
  }
  
  public String filename;
  public File resultFile;
  public String contentType;
  public long size;
  
}
