package org.monet.grided.core.services.deploy;

import org.monet.api.deploy.setupservice.impl.model.ServerState;
import org.monet.api.deploy.setupservice.impl.model.Ticket;
import org.monet.grided.core.model.Space;


public interface DeployService {

    public Ticket createSpace(String serverLocation, Space space);
    public Ticket removeSpace(String serverLocation, Space space); 
            
    public ServerState getServerState(String serverIP);
        
//    public List<Process> loadServerProcesses(String serverIP);
//
//    public void addSpace(Space space, Callback<Boolean> callback);
//    public void deleteSpace(Space space, Callback<Boolean> callback);
//    public void resetSpace(Space space, Callback<Boolean> callback);
//    
//    public boolean startFederation(String serverIP, String federationName);
//    public boolean stopFederation(String serverIP, String federationName);
//    public boolean isFederationRunning(String serverIP, String federationName);
//            
//    public boolean ping(String serverIp);
    
}

