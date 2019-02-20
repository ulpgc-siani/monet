package org.monet.bpi;

public interface BPILockService extends BPILock {

  public String getRequestDocumentId();

  public <T extends BPIBaseNode<?>> T getRequestDocument();

  public String getResponseDocumentId();
  
  public <T extends BPIBaseNode<?>> T getResponseDocument();

}