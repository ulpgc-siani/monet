package org.monet.docservice.core.library.impl;

import java.io.IOException;

import org.monet.docservice.core.library.LibraryBase64;
import org.monet.docservice.core.log.Logger;

import com.google.inject.Inject;

public class LibraryBase64Impl implements LibraryBase64 {

  private Logger logger;
  
  @Inject
  public void injectLogger(Logger logger) {
    this.logger = logger;
  }
  
  public byte[] decode(String encodedSource) {
    logger.debug("decode(%s)", encodedSource);
    
    byte[] result = null;
    try {
      result =  Base64.decode(encodedSource);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    return result;
  }

  public String encode(byte[] source) {
    logger.debug("encode(%s)", source);
    
    return Base64.encodeBytes(source);
  }

}
