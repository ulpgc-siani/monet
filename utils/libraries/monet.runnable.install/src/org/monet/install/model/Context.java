package org.monet.install.model;

import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict=false)
public class Context {
  public static final String GLOBAL                             = "global";
  public static final String NAME                               = "name";
  public static final String USERNAME                           = "username";
  public static final String PASSWORD                           = "password";
  public static final String URL                                = "url";
  public static final String DRIVER_CLASS_NAME                  = "driverClassName";
  public static final String MAX_ACTIVE                         = "maxActive";
  public static final String LOG_ABANDONED                      = "logAbandoned";
  public static final String REMOVE_ABANDONED_TIMEOOUT          = "removeAbandonedTimeout";
  public static final String REMOVE_ABANDONED                   = "removeAbandoned";
  public static final String MAX_IDLE                           = "maxIdle";
  public static final String AUTH                               = "auth";
  
  @Element(name="ResourceLink", required=false) ResourceLink resourceLink;
  @Element(name="Resource", required=false) Resource resource;
  
  public void setResourceLink(ResourceLink resourceLink) { this.resourceLink = resourceLink; }
  public void setResource(Resource resource) { this.resource = resource; }

  public static class ResourceLink{
    protected @Attribute(name="global") String global;
    protected @Attribute(name="name") String name;
    protected @Attribute(name="type") String type = "javax.sql.DataSource";
    public ResourceLink() {}
    public ResourceLink(String global, String name) {this.global = global; this.name = name;}
    public void setGlobal(String global) { this.global = global; }
    public void setName(String name) { this.name = name; }
  }

  public static class Resource extends ResourceLink{
    @Attribute(name="username") String username;
    @Attribute(name="password") String password;
    @Attribute(name="url") String url;
    @Attribute(name="driverClassName") String driverClassName;
    @Attribute(name="maxActive") String maxActive;
    @Attribute(name="logAbandoned") String logAbandoned;
    @Attribute(name="removeAbandonedTimeout") String removeAbandonedTimeout;
    @Attribute(name="removeAbandoned") String removeAbandoned;
    @Attribute(name="maxIdle") String maxIdle;
    @Attribute(name="auth") String auth;
    public void fill(Map<String,String> options) {
      this.global = options.get(GLOBAL);
      this.name = options.get(NAME);
      this.username = options.get(USERNAME);
      this.password = options.get(PASSWORD);
      this.url = options.get(URL);
      this.driverClassName = options.get(DRIVER_CLASS_NAME);
      this.maxActive =options.get(MAX_ACTIVE) ;
      this.logAbandoned = options.get(LOG_ABANDONED);
      this.removeAbandonedTimeout = options.get(REMOVE_ABANDONED_TIMEOOUT);
      this.removeAbandoned = options.get(REMOVE_ABANDONED);
      this.maxIdle = options.get(MAX_IDLE);
      this.auth = options.get(AUTH); 
    }
  }
}
