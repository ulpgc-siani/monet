package org.monet.bpi.types;


public class Picture {

  protected boolean isAttachment = true;
 
  private String filename;
  
  public Picture() {}
  
  public Picture(String filename) {
    this.filename = filename;
  }
  
  public String getFilename() {
    return this.filename;
  }
  
  public void setFilename(String filename) {
    this.filename = filename;
  }
  
  public boolean equals(Picture obj) {
    return this.filename.equals(obj.filename);
  }
}
