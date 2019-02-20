package org.monet.grided.core.model;


public interface ProgressCallback {
  public void onProgressUpdate(Progress progress);
  public void onFailure(ImportError error);
  public void onComplete(Progress progress);
  public boolean isCanceled();   
}
