package org.monet.federation.accountoffice.core.library;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.UUID;

public class LibraryCodeGenerator {

  public static String generateCode() throws NoSuchAlgorithmException {
    Security.addProvider(new FNVProvider());
    MessageDigest digest = MessageDigest.getInstance("FNV-1a");
    digest.update((UUID.randomUUID().toString()).getBytes());
    byte[] hash1 = digest.digest();
    
    String sHash1 = "m" + String.valueOf(LibraryBase64.encode(hash1)).replaceAll("=", "").replaceAll("-", "_").replaceAll("/", "_").replaceAll("\\+","_").toLowerCase();
    
    return sHash1;
  }
  
}
