package org.monet.filesystem;


import java.io.InputStream;

public class Resources {

  public static InputStream getAsStream(String name) {
    return Resources.class.getResourceAsStream(name);
  }
  
}
