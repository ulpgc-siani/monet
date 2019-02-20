package org.monet.bpi.types;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;


public class Picture extends File {

  public Picture() {
  }

  public Picture(String filename) {
    super(filename);
  }

  public Picture(String filename, boolean isModelResource) {
    super(filename, isModelResource);
  }

  public boolean equals(Picture obj) {
    return this.equals((File) obj);
  }
  
  public static Picture fromFile(File file) {
    return null;
  }

  public static Picture fromInputStream(String contentType, InputStream stream) {
    return null;
  }
  
  public static Picture fromUrl(String url) {
    return null;
  }
  
  public static Picture fromUrl(String url, HashMap<String, List<String>> parameters) {
    return null;
  }
  
}
