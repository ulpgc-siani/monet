package org.monet.encrypt;

import java.security.MessageDigest;

import org.monet.encrypt.library.LibraryBase64;


public class Hasher {
  private static final String MD5 = "MD5";
  private static final String SHA1 = "SHA1";
  private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6',
    '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', };


  public static byte[] getMD5asByteArray (String original) throws Exception{
    return generateHash(MD5,original);
  }
  
  public static String getMD5asBase64(String original) throws Exception{
    return LibraryBase64.encode(generateHash(MD5,original));
  }

  public static String getMD5asHexadecimal(String original) throws Exception{
    return asHex(generateHash(MD5,original));
  }
  
  public static byte[] getSHA1asByteArray(String original) throws Exception{
    return generateHash(SHA1,original);
  }

  public static String getSHA1asBase64(String original) throws Exception{
    return LibraryBase64.encode(generateHash(SHA1,original));
  }
  
  public static String getSHA1asHexadecimal(String original) throws Exception{
    return asHex(generateHash(SHA1,original));
  }

  private static byte[] generateHash(String method,String original) throws Exception{
    MessageDigest digest;
    digest = java.security.MessageDigest.getInstance(method);
    digest.update(original.getBytes());
    return digest.digest();
  }

  private static String asHex(byte hash[]) {
    char buf[] = new char[hash.length * 2];
    for (int i = 0, x = 0; i < hash.length; i++) {
      buf[x++] = HEX_CHARS[(hash[i] >>> 4) & 0xf];
      buf[x++] = HEX_CHARS[hash[i] & 0xf];
    }
    return new String(buf);
  }
}
