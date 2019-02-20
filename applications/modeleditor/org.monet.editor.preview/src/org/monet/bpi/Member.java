package org.monet.bpi;

public interface Member {
  
  public String getKey();

  public String getLink();
  
  public String getValue();

  public String getExtra(String key);

}
