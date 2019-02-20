package org.monet.docservice.core.library;


public interface LibraryBase64 {

  String encode(byte[] source);
  byte[] decode(String encodedSource);
  
}
