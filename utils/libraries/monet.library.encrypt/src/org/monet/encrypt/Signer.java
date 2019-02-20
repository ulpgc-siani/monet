package org.monet.encrypt;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Signer {
  private static final String HMAC_SHA1 = "HmacSHA1";
  private static final String UTF8 = "UTF-8";
  
  private static final String EMPTY_STRING = "";
  private static final String CARRIAGE_RETURN = "\r\n";
  
  public static String signerHMAC(String toSign, String keyString) throws Exception{
    SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8), HMAC_SHA1);
    Mac mac = Mac.getInstance(HMAC_SHA1);
    mac.init(key);
    byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
    return new String(Base64.encodeBase64(bytes)).replace(CARRIAGE_RETURN, EMPTY_STRING);
  }
}
