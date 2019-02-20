package org.monet.bpi.types;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class File {

	@Attribute(name = "is-attachment")
  protected boolean isAttachment = true;
  @Text
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

  public boolean isStoredAtDocEngine() {
    return false;
  }
  
  public boolean isModelResource() {
    return this.isModelResource;
  }
  
  public String getAbsolutePath() {
    return null;
  }

  public byte[] getContent() {
    return null;
  }

  public void setContent(byte[] content) {
  }

  public String getContentType() {
    return null;
  }

  @Override
  public String toString() {
    return this.filename;
  }
  
  public static File fromFile(java.io.File file) {
    return null;
  }

  public static File fromInputStream(String contentType, InputStream stream) {
    return null;
  }
  
  public static File fromInputStream(String filename, String contentType, InputStream stream) {
    return null;
  }

  public static File fromUrl(String url) {
    return null;
  }  
  
  public static File fromUrl(String url, HashMap<String, List<String>> parameters) {
    return null;
  }

}
