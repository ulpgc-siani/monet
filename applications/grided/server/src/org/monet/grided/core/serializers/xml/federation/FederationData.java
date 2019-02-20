package org.monet.grided.core.serializers.xml.federation;

import java.util.ArrayList;
import java.util.List;

import org.monet.grided.core.persistence.filesystem.Data;
import org.monet.grided.core.serializers.xml.Logo;
import org.monet.grided.core.serializers.xml.Organization;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="federation-manifest", strict=false)
public class FederationData implements Data {
        
  @Element(name="organization", required=false) Organization organization;
  @Element(name="connection", required=false) Connection connection;
  @Element(name="setup", required=false) String setup;
  @Element (name="authentication", required=false) Authentication authentication;
  
  public String getLogoFilename() {
      Logo logo = this.organization.getLogo();
      return (logo != null)? logo.getName() : "";
  }
  
  public String getName() {
      return this.organization.getName();
  }
  
  public String getLabelText(String language) {
      return this.getOrganization().getLabel();
  }
  
  public String getUrl() {
      return this.getOrganization().getUrl();
  }
  
  public Organization getOrganization() { return organization; }
  public void setOrganization(Organization organization) { this.organization = organization; }
  
  public Connection getConnection() { return connection; }
  public void setConnection(Connection connection) {this.connection = connection; }
      
  public Authentication getAuthentication() { return authentication; }
  public void setAuthentication(Authentication auth) { this.authentication = auth; }
    
  public String getSetup() { return setup; }
  
  
  public FederationData() {
      this.organization = new Organization();
      this.connection = new DatabaseConnection("", "", "");
      this.authentication = new Authentication();      
  }  
     
  public abstract class ConnectionTypes {
      public static final String DATABASE = "database";
      public static final String LDAP = "ldap";
      public static final String MOCK = "mock";      
  }
  
  public abstract class DatabaseTypes {
      public static final String MYSQL = "mysql";
      public static final String ORACLE = "oracle";
  }
  
  public interface Connection {
      public String getUrl();
      public void setUrl(String url);
      
      public String getUser();
      public  void setUser(String user); 
      
      public String getPassword();
      public void setPassword(String password);
      
      public String getType();
      public void setType(String type);      
  }
  
    
  public static class Authentication {
    public static final String PASSWORD = "Password";
    public static final String OPENID = "OpenId";
    public static final String CERTIFICATE = "Certificate";
    
    @Element(name="use-password", required=false) boolean password;
    @Element(name="use-open-id", required=false) boolean openId;
    @Element(name="use-certificate", required=false) boolean certificate;
    
    public Authentication() {
        this.openId = false;
        this.certificate = false;
        this.password = false;
    }
    
    public boolean getCertificate() { return certificate; }
    public void setCertificate(boolean c) { this.certificate = c; }
    
    public boolean getPassword() { return password; }
    public void setPassword(boolean password) {this.password = password; }
    
    public boolean getOpenId() { return openId; }
    public void setOpenId(boolean openId) { this.openId = openId; } 
    
    
    public List<String> getActiveMethods() {
      ArrayList<String> aMethods = new ArrayList<String>();
      if(password == true) aMethods.add(PASSWORD);
      if(openId == true)   aMethods.add(OPENID);
      if(certificate == true) aMethods.add(CERTIFICATE);
      return aMethods;
    }        
  }
}
