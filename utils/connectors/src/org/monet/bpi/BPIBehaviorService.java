package org.monet.bpi;


public interface BPIBehaviorService<Schema, Request extends BPIBaseNode<?>, Response extends BPIBaseNode<?>> {

  public <T extends Request> T getRequest();
  public <T extends Response> T getResponse();
  public void onCalculateFactsAtStart();
  public void onCalculateFactsAtFinish();
  
}