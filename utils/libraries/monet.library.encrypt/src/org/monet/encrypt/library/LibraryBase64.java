package org.monet.encrypt.library;

import java.io.IOException;

public class LibraryBase64  {
  
  public static byte[] decode(String encodedSource) throws IOException {
    byte[] result = null;
    try {
      result =  Base64.decode(encodedSource);
    } catch (IOException e) {
      throw e;
    }
    return result;
  }

  public static String encode(byte[] source) {
    return Base64.encodeBytes(source);
  }

}
