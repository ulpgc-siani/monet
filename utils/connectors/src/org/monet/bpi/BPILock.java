package org.monet.bpi;

public interface BPILock {

  public String getId();

  public String getCode();

  public void setResultCode(Enum<?> workStopCode);
  
  public void cancelSolve(String reason);

}