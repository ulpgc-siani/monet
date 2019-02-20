package org.monet.api.space.setupservice.impl;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.monet.api.space.setupservice.SpaceSetupApi;
import org.monet.api.space.setupservice.impl.library.LibraryRestfull;
import org.monet.api.space.setupservice.impl.model.Status;

public class SpaceSetupApiImpl implements SpaceSetupApi {
  private String location;  
  private String certificateFilename;  
  private String certificatePassword;  
  
  public SpaceSetupApiImpl(String location, String certificateFilename, String certificatePassword) {
    this.location = location + "/servlet/setupservice";
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
      throw new RuntimeException(this.location + ": " + e.getMessage(), e);
    }
  }

  @Override
  public Status getStatus() {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    Status status = new Status();
    String statusValue = "";
    
    try {
      parameters.put("op", new StringBody("getstatus"));
      statusValue = (String)LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
      status.deserializeFromXML(statusValue);
    } catch (Exception e) {
      throw new RuntimeException(this.location + ": " + e.getMessage(), e);
    }
    
    return status;
  }

  @Override
  public void run() {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("run"));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      throw new RuntimeException(this.location + ": " + e.getMessage(), e);
    }
  }

  @Override
  public void stop() {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("stop"));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      throw new RuntimeException(this.location + ": " + e.getMessage(), e);
    }
  }

  @Override
  public void putLabel(String label) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("putlabel"));
      parameters.put("label", new StringBody(label));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      throw new RuntimeException(this.location + ": " + e.getMessage(), e);
    }
  }

  @Override
  public void putLogo(InputStream logo) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("putlogo"));
      parameters.put("logo", new InputStreamBody(logo, "logo.png"));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      throw new RuntimeException(this.location + ": " + e.getMessage(), e);
    }
  }

  @Override
  public void updateModel(InputStream model) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("updatemodel"));
      parameters.put("model", new InputStreamBody(model, "model.zip"));
      
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      throw new RuntimeException(this.location + ": " + e.getMessage(), e);
    }
  }
  
  @Override
  public void executeConstructor(InputStream constructor) {
    HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
    
    try {
      parameters.put("op", new StringBody("executeconstructor"));
      parameters.put("constructor", new InputStreamBody(constructor, "constructor.zip"));
      LibraryRestfull.execute(this.location, parameters, this.certificateFilename, this.certificatePassword);
    } catch (Exception e) {
      throw new RuntimeException(this.location + ": " + e.getMessage(), e);
    }
  }

}
