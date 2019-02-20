package org.monet.encrypt.library;

import java.security.Principal;
import java.util.HashMap;

public class X509CertificateHelper {

  public static final HashMap<String, String> toMap(Principal principal) {
    HashMap<String, String> values = new HashMap<String, String>();
    for (String keyValue : principal.getName().split(",")) {
      String[] keyValuePair = keyValue.trim().split("=");
      if (keyValuePair.length == 2) {
        values.put(keyValuePair[0], keyValuePair[1]);
      }
    }
    return values;
  }
  
}
