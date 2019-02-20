package org.monet.metacompiler.util;

import java.io.InputStream;

public class Resources {

  public static InputStream getAsStream(String name) {
    return Resources.class.getResourceAsStream(name);
  }
  
  public static String getFullPath(String name) {
    return Resources.class.getResource(name).getPath();
  }

}
