package org.monet.api.space.setupservice;

import java.io.InputStream;

import org.monet.api.space.setupservice.impl.model.Status;

public interface SpaceSetupApi {

  public String getVersion();

  public Status getStatus();  

  public void run();

  public void stop();

  public void putLabel(String label);

  public void putLogo(InputStream logo);
  
  public void updateModel(InputStream model);
  
  public void executeConstructor(InputStream constructor);
  
}
