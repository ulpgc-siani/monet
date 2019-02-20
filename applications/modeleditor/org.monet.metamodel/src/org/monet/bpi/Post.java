package org.monet.bpi;


public abstract class Post {

  public static Post createInfo() {
    return null;
  }
  
  public static Post createRequest() {
    return null;
  }
  
  public static Post createResponse() {
    return null;
  }
  
  public abstract String getId();
  public abstract void setTitle(String title);
  public abstract void setBody(String body);
  public abstract void setTitleLink(MonetLink link);
  public abstract void setBodyLink(MonetLink link);
  
}
