package org.monet.bpi;


public abstract class Post {

  public static Post create() {
    return null;
  }
  
  public static Post create(String title, String body) {
    return null;
  }
  
  public static Post create(String title, MonetLink titleLink) {
    return null;
  }
  
  public static Post create(String title, String body, MonetLink titleLink, MonetLink bodyLink) {
    return null;
  }
  
  public abstract void setTitle(String title);
  public abstract void setBody(String body);
  public abstract void setTitleLink(MonetLink link);
  public abstract void setBodyLink(MonetLink link);
  
}
