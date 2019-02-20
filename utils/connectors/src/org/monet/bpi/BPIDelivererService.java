package org.monet.bpi;

import java.net.URI;

public abstract class BPIDelivererService {
  
  protected static BPIDelivererService instance;
  
  public static BPIDelivererService getInstance() {
    return instance;
  }
  
  public abstract void deliver(URI url, BPIBaseNodeDocument<?> document) throws Exception;

  public abstract void deliverToMail(URI from, URI to, String subject, String body, BPIBaseNodeDocument<?> document);

}