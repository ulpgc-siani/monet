package org.monet.editor.library;

import java.security.MessageDigest;
import java.security.Security;
import java.util.UUID;

import org.monet.editor.MonetLog;
import org.monet.editor.core.security.FNVProvider;

public class LibraryCodeGenerator {

  public static String generateCode(String seed) {
    try {
      Security.addProvider(new FNVProvider());
      MessageDigest digest = MessageDigest.getInstance("FNV-1a");
      digest.update((seed + UUID.randomUUID().toString()).getBytes());
      byte[] hash1 = digest.digest();
      
      String sHash1 = "m" + String.valueOf(LibraryBase64.encode(hash1)).replaceAll("=", "").replaceAll("-", "_").replaceAll("/", "_").replaceAll("\\+","_").toLowerCase();
      
      return sHash1;
      
    } catch (Exception e){
      MonetLog.print(e);
    }
    return "";
  }
  
  public static String generateHashCode(String seed) {
    try {
      Security.addProvider(new FNVProvider());
      MessageDigest digest = MessageDigest.getInstance("FNV-1a");
      digest.update(seed.getBytes());
      byte[] hash1 = digest.digest();
      
      return String.valueOf(LibraryBase64.encode(hash1));
      
    } catch (Exception e){
      MonetLog.print(e);
    }
    return "";
  }
  
}
