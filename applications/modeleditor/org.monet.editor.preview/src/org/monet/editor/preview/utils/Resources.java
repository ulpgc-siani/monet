package org.monet.editor.preview.utils;

import java.io.InputStream;
import java.net.URL;

public class Resources {

  public static InputStream getAsStream(String name) {
    return Resources.class.getResourceAsStream(name);
  }
  
  public static URL get(String name) {
    return Resources.class.getResource(name);
  }

  public static String getAsPath(String name) {
    return Resources.class.getResource(name).getPath();
  }

}
