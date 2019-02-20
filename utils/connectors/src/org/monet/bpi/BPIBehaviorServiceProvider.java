package org.monet.bpi;


public interface BPIBehaviorServiceProvider<Schema, Request extends BPIBaseNode<?>, Response extends BPIBaseNode<?>> {

  public String getProviderLabel();
  public <T extends Request> T getRequest();
  public <T extends Response> T getResponse();
  public void onCalculateFactsAtStart();
  public void onCalculateFactsAtFinish();
  
}