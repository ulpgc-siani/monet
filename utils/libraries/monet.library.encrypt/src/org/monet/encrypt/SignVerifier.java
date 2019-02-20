package org.monet.encrypt;

public class SignVerifier {
  /**
   * 
   * @param signature Signature hmac
   * @param textToSign Original text
   * @param key Sign key
   */
  public static boolean verifyHMAC(String signature, String textToSign, String key){
    try {
      return (Signer.signerHMAC(textToSign, key).equals(signature)) ? true : false;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
