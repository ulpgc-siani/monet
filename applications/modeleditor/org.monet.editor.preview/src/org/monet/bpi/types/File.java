package org.monet.bpi.types;


public class File {

  protected boolean isAttachment = true;
  private String filename;
  
  private boolean isModelResource = false;
  
  public File() {}
  
  public File(String filename) {
    this.filename = filename;
  }
  
  public File(String filename, boolean isModelResource) {
    this(filename);
    this.isModelResource = isModelResource;
  }
  
  public String getFilename() {
    return this.filename;
  }
  
  public void setFilename(String filename) {
    this.filename = filename;
  }
  
  public boolean equals(File obj) {
    return this.filename.equals(obj.filename);
  }
  
  public boolean isModelResource() {
    return this.isModelResource;
  }
  
}
