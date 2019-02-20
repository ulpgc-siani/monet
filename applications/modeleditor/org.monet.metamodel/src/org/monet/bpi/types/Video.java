package org.monet.bpi.types;

import java.io.InputStream;


public class Video extends File {

  public Video() {
  }

  public Video(String filename) {
    super(filename);
  }

  public Video(String filename, boolean isModelResource) {
    super(filename, isModelResource);
  }

  public boolean equals(Video obj) {
    return this.equals((File) obj);
  }
  
  public static File fromInputStream(String contentType, InputStream stream) {
    return null;
  }

}
