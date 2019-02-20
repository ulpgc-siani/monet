package org.monet.bpi;

import org.monet.bpi.types.File;

import java.util.List;

public abstract class BPIMailService {
  
  protected static BPIMailService instance;
  
  public static BPIMailService getInstance() {
    return instance;
  }
  
  public abstract void send(List<String> to, String subject, String content, File... attachments);
  
  public abstract void send(List<String> to, String subject, String htmlContent, String textContent, File... attachments);
  
}