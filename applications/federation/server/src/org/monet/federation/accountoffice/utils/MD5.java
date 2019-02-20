package org.monet.federation.accountoffice.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

  private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', };
  
  public static final String calculate(String data) {
    MessageDigest digest;
    try {
      digest = java.security.MessageDigest.getInstance("MD5");
      digest.update(data.getBytes());
      byte[] hash = digest.digest();
      return asHex(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private static final String asHex(byte hash[]) {
    char buf[] = new char[hash.length * 2];
    for (int i = 0, x = 0; i < hash.length; i++) {
      buf[x++] = HEX_CHARS[(hash[i] >>> 4) & 0xf];
      buf[x++] = HEX_CHARS[hash[i] & 0xf];
    }
    return new String(buf);
  }
}
