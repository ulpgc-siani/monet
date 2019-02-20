package org.monet.bpi;

import org.monet.bpi.types.Link;


public interface Task {

  public String getId();
  
  public void setLabel(String label);
  
  public String getLabel();

  public void setDescription(String description);
  
  public String getContextVariable(String name);

  public void setContextVariable(String name, String value);

  public void removeContextVariable(String name);
  
  public void addLog(String title, String text);
  
  public void addLog(String title, String text, Link link);
  
  public void save();
  
  public void resume();
  
}