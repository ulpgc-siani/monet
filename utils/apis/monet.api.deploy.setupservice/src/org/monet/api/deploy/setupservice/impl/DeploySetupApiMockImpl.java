package org.monet.api.deploy.setupservice.impl;
import java.util.HashMap;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.monet.api.deploy.setupservice.DeploySetupApi;
import org.monet.api.deploy.setupservice.impl.library.LibraryRestfull;
import org.monet.api.deploy.setupservice.impl.model.FederationManifest;
import org.monet.api.deploy.setupservice.impl.model.ServerState;
import org.monet.api.deploy.setupservice.impl.model.SpaceManifest;
import org.monet.api.deploy.setupservice.impl.model.Status;
import org.monet.api.deploy.setupservice.impl.model.Ticket;

public class DeploySetupApiMockImpl implements DeploySetupApi {
  private String location;  
  private String certificateFilename;  
  private String certificatePassword;  
  
  public DeploySetupApiMockImpl(String location, String certificateFilename, String certificatePassword) {
    this.location = location;
    this.certificateFilename = certificateFilename;
    this.certificatePassword = certificatePassword;
  }

  @Override
  public String getVersion() {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("getversion"));
      return (String)LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return "0.0.0";
  }
  
  @Override
  public ServerState getServerState(String url) {      
      HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
      ServerState serverState = new ServerState();
      
      try {
        parameters.put("op", new StringBody("getserverstate"));
        String xml = LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
        serverState.deserializeFromXML(xml);
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      return serverState;      
  }


  @Override
  public boolean isTicketCompleted(Ticket ticket) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String value = "false";
    
    try {
      parameters.put("op", new StringBody("isticketcompleted"));
      parameters.put("ticket", new StringBody(ticket.serializeToXML()));
      value = (String)LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return Boolean.valueOf(value);
  }
  
  @Override
  public void reset() {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("reset"));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void restart() {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("restart"));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void upgrade(String appRepositoryUrl) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("upgrade"));
      parameters.put("apprepositoryurl", new StringBody(appRepositoryUrl));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Status getFederationStatus(String name) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String statusValue;
    Status status = new Status();
    
    try {
      parameters.put("op", new StringBody("getfederationstatus"));
      parameters.put("name", new StringBody(name));
      statusValue = (String)LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
      status.deserializeFromXML(statusValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return status;
  }

  @Override
  public Ticket createFederation(String name, String label, FederationManifest manifest) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    Ticket ticket = new Ticket();
    String ticketValue;
    
    try {
      parameters.put("op", new StringBody("createfederation"));
      parameters.put("name", new StringBody(name));
      parameters.put("label", new StringBody(label));
      parameters.put("manifest", new StringBody(manifest.serializeToXML()));
      ticketValue = (String)LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
      ticket.deserializeFromXML(ticketValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return ticket;
  }

  @Override
  public void removeFederation(String name) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("removefederation"));
      parameters.put("name", new StringBody(name));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void resetFederation(String name) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("resetfederation"));
      parameters.put("name", new StringBody(name));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public void restartFederation(String name) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("restartfederation"));
      parameters.put("name", new StringBody(name));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Status getSpaceStatus(String name) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    String statusValue;
    Status status = new Status();
    
    try {
      parameters.put("op", new StringBody("getspacestatus"));
      parameters.put("name", new StringBody(name));
      statusValue = (String)LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
      status.deserializeFromXML(statusValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return status;
  }

  @Override
  public Ticket createSpace(String name, String label, SpaceManifest manifest) {	  
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    Ticket ticket = new Ticket();
//    String ticketValue;
    
    try {
      parameters.put("op", new StringBody("createspace"));
      parameters.put("name", new StringBody(name));
      parameters.put("label", new StringBody(label));
      parameters.put("manifest", new StringBody(manifest.serializeToXML()));
//      ticketValue = (String)LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
//      ticket.deserializeFromXML(ticketValue);
      
//TODO
      
/*      
      String ip   = space.getFederation().getServer().getIp();
      String port = "4323";
      String cmd = "<command id=\"create_space\"><parameter id=\"name\">a</parameter><parameter id=\"label\">a</parameter><parameter id=\"url\">http://localhost:8091/a</parameter><parameter id=\"federation-name\">federationservice_user</parameter></command>";
      
      InetAddress destAddr;
      try {
          destAddr = InetAddress.getByName(ip);
          int destPort = Integer.parseInt(port);

          Socket sock = new Socket(destAddr, destPort);

          String SendMessage = cmd;

          System.out.println("Sending message (" + SendMessage.length() + " bytes): ");
          System.out.println(SendMessage);
          try {
              PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
              BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

              out.println(SendMessage);

              String receiveMessage = in.readLine();

              System.out.println("Received message:");
              System.out.println(receiveMessage);
              
              sock.close();
          } catch (IOException e) {
              e.printStackTrace();
          } catch (Exception e) {
              e.printStackTrace();
          }

      } catch (UnknownHostException e1) {
        System.out.println("Should raise a UnknownHostException");
      } // Destination address
      catch (IOException e) {
        System.out.println("Should raise a IOException");
      }
      
      return new Ticket();
*/      
      
      
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return ticket;
  }

  @Override
  public void removeSpace(String name, SpaceManifest manifest) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("removespace"));
      parameters.put("name", new StringBody(name));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void resetSpace(String name) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("resetspace"));
      parameters.put("name", new StringBody(name));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void restartSpace(String name) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("restartspace"));
      parameters.put("name", new StringBody(name));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
