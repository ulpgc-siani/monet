package org.monet.api.deploy.setupservice;

import org.monet.api.deploy.setupservice.impl.model.FederationManifest;
import org.monet.api.deploy.setupservice.impl.model.ServerState;
import org.monet.api.deploy.setupservice.impl.model.SpaceManifest;
import org.monet.api.deploy.setupservice.impl.model.Status;
import org.monet.api.deploy.setupservice.impl.model.Ticket;


public interface DeploySetupApi {

  public String getVersion();
  
  public boolean isTicketCompleted(Ticket ticket);
  
  public ServerState getServerState(String url);
    
  public void reset();
  
  public void restart();
  
  public void upgrade(String appRepositoryUrl);
  
  public Status getFederationStatus(String name);
  
  public Ticket createFederation(String name, String label, FederationManifest manifest);

  public void removeFederation(String name);

  public void resetFederation(String name);
  
  public void restartFederation(String name);

  public Status getSpaceStatus(String name);

  public Ticket createSpace(String name, String label, SpaceManifest manifest);
  
  public void removeSpace(String name, SpaceManifest manifest);
  
  public void resetSpace(String name);
  
  public void restartSpace(String name);
  
}
