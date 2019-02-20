package org.monet.federation.accountoffice.utils;

import org.apache.commons.codec.binary.Base64;
import org.monet.federation.accountoffice.core.model.OAuth;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SignatureService {
  private static final String AND = "&";
  private static final String EQUALS = "=";
  private static final String EMPTY_STRING = "";
  private static final String CARRIAGE_RETURN = "\r\n";
  private static final String UTF8 = "UTF-8";

  private static final String HMAC_SHA1 = "HmacSHA1";
 // private static final String RSA_SHA1 = "SHA1withRSA";

  private static final String OAUTH_SIGN_METHOD_HMAC = "HMAC-SHA1";
  private static final String OAUTH_SIGN_METHOD_RSA = "RSA-SHA1";
  private static final String OAUTH_SIGN_METHOD_PLAINTTEXT = "PLAINTEXT";
  

  public static boolean checkSignature(String url ,Map<String,String> map, String clientSecret, String tokenSecret){
    if(map.get(OAuth.OAUTH_SIGNATURE_METHOD).equals(OAUTH_SIGN_METHOD_PLAINTTEXT)) return true;
    
    String serverSignature = null;
    String clientSignature = "";
    String baseString = "";

    try {
      baseString = extractStringToSign(url,map);
      clientSignature = map.get(OAuth.OAUTH_SIGNATURE);

      if(map.get(OAuth.OAUTH_SIGNATURE_METHOD).equals(OAUTH_SIGN_METHOD_RSA)) serverSignature  = encode(rsaGetSignature(baseString, encode(clientSecret) + AND + encode(tokenSecret)));
      if(map.get(OAuth.OAUTH_SIGNATURE_METHOD).equals(OAUTH_SIGN_METHOD_HMAC)) serverSignature = encode(hmacGetSignature(baseString, encode(clientSecret) + AND + encode(tokenSecret)));

    } catch(Exception e) {
      e.printStackTrace();
    }
    
    return clientSignature.equals(serverSignature) ? true : false;
  }

  private static String hmacGetSignature(String toSign, String keyString ) throws Exception {
    SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8), HMAC_SHA1);
    Mac mac = Mac.getInstance(HMAC_SHA1);
    mac.init(key);
    byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
    return new String(Base64.encodeBase64(bytes)).replace(CARRIAGE_RETURN, EMPTY_STRING);
  }

  private static String rsaGetSignature(String keyString, String toSign) throws Exception{
    return null;
  }

  private static String extractStringToSign(String url, Map<String, String> map) throws Exception {
    
    String mCallback  = (map.get(OAuth.OAUTH_CALLBACK) == null)     ? "" : map.get(OAuth.OAUTH_CALLBACK);
    String mConsumer  = (map.get(OAuth.OAUTH_CONSUMER_KEY) == null) ? "" : map.get(OAuth.OAUTH_CONSUMER_KEY);
    String mNonce     = (map.get(OAuth.OAUTH_NONCE) == null) ? "" : map.get(OAuth.OAUTH_NONCE);
    String mSMethod   = (map.get(OAuth.OAUTH_SIGNATURE_METHOD) == null) ? "" : map.get(OAuth.OAUTH_SIGNATURE_METHOD);
    String mTimestamp = (map.get(OAuth.OAUTH_TIMESTAMP) == null) ? "" : map.get(OAuth.OAUTH_TIMESTAMP);
    String mVersion   = (map.get(OAuth.OAUTH_VERSION) == null)   ? "" : map.get(OAuth.OAUTH_VERSION);
    String mToken     = (map.get(OAuth.OAUTH_TOKEN) == null)     ? "" : map.get(OAuth.OAUTH_TOKEN);
    String mVerifier  = (map.get(OAuth.OAUTH_VERIFIER) == null)  ? "" : map.get(OAuth.OAUTH_VERIFIER);
   
    
    List<String> elements = new ArrayList<String>();
    String callback = OAuth.OAUTH_CALLBACK + EQUALS + mCallback;
    if(!mCallback.equals("")) elements.add(callback);
    
    String consumer  = OAuth.OAUTH_CONSUMER_KEY + EQUALS + mConsumer;
    if(!mConsumer.equals("")) elements.add(consumer);
    
    String nonce     = OAuth.OAUTH_NONCE + EQUALS + mNonce;
    if(!mNonce.equals("")) elements.add(nonce);
    
    String smethod   = OAuth.OAUTH_SIGNATURE_METHOD + EQUALS + mSMethod;
    if(!mSMethod.equals("")) elements.add(smethod);
    
    String timestamp = OAuth.OAUTH_TIMESTAMP + EQUALS + mTimestamp;
    if(!mTimestamp.equals("")) elements.add(timestamp);
    
    String token   =   OAuth.OAUTH_TOKEN + EQUALS + mToken;
    if(!mToken.equals("")) elements.add(token);
    
    String verifier   = OAuth.OAUTH_VERIFIER + EQUALS + mVerifier;
    if(!mVerifier.equals("")) elements.add(verifier);
    
    String version   = OAuth.OAUTH_VERSION + EQUALS + mVersion;
    if(!mVersion.equals("")) elements.add(version);
    
    
    String ret = "POST&"+ encode(url) + AND;
    String ret2 = "";
    for (int i = 0; i < elements.size(); i++) {
      ret2 += elements.get(i);
      if(i != (elements.size() - 1))
        ret2 += AND;
    }

    ret += encode(ret2);
    return ret;
  }
  

  public static String encode(String string) throws Exception
  {
    String encoded = URLEncoder.encode(string, UTF8).replace("*","%2A").replace("+","%2B").replace("%7E", "~");
    return encoded;
  }
}
