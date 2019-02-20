package org.monet.grided.agents;

import org.monet.grided.core.model.ImportError;
import org.monet.grided.core.model.Progress;
import org.monet.grided.core.model.ProgressCallback;

public class AgentImportMonitor {

  public static AgentImportMonitor instance;  
  private ImportError error;
  private Progress progress;  
  private boolean isCanceled; 
  private ProgressCallback progressCallback;
  
  public synchronized static AgentImportMonitor getInstance() {
    if (instance == null) instance = new AgentImportMonitor();
    return instance;
  }
  
  private AgentImportMonitor() {
    this.progressCallback = new ProgressCallback() {
            
      public void onProgressUpdate(Progress progress) {
        AgentImportMonitor.this.progress = progress;
      }

      public void onFailure(ImportError error) {
        AgentImportMonitor.this.error = error;
      }

      @Override
      public void onComplete(Progress progress) {
        AgentImportMonitor.this.progress = progress;
      }

      @Override
      public boolean isCanceled() {
        return this.isCanceled();
      }            
    };
    this.doStart();    
  }
      
  public ProgressCallback getCallback() {
    return this.progressCallback;
  }
    
  public synchronized void startListening() {
    this.doStart();        
  }
  
  public synchronized void stopListening() {
    this.isCanceled = true;
    this.doStop();
  }
  
  public synchronized boolean isListening() {    
    return this.isCanceled == false;
  }
  
  public synchronized boolean hasErrors() {
    return this.error != null; 
  }
  
  public synchronized boolean hasProgress() {
    return this.progress != null;
  }
  
  public synchronized Progress getProgress() {    
    return this.progress;
  }
  
  public synchronized ImportError getError() {    
    return this.error;
  }  

  private void doStart() {
    this.error = null;
    this.progress = null;
    this.isCanceled = false;
  }

  private void doStop() {
    this.isCanceled = true;
    this.progress = null;
    this.error = null;
  }
}
