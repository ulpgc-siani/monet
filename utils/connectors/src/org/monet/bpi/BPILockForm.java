package org.monet.bpi;

public interface BPILockForm extends BPILock {

  public String getFormId();
  public <T extends BPIBaseNode<?>> T getForm();
  
}