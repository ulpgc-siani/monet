package org.monet.bpi;

import java.util.Map;

public interface Task {

  public String getId();
  
  public void setLabel(String label);
  
  public String getLabel();

  public void setDescription(String description);
  
  public void setUrgent(boolean value);

  public boolean isUrgent();

  public void setComments(String comments);
  
  public String getComments();

  public Map<String, String> getFlags();

  public String getFlag(String name);
  
  public boolean isFlagActive(String name);

  public void setFlag(String name, String value);
  
  public void setFlag(String name, boolean value);

  public void removeFlag(String name);
  
  public void addLog(String title, String text);
  
  public void addLog(String title, String text, Iterable<MonetLink> links);
  
  public void save();
  
  public void resume();
  
  public void free();
  
  public void abort();
  
  public void doGoto(String place, String historyText);

  public String currentPlace();

  public User getOwner();

  public void assignTo(User user, String reason);
  
  public boolean isFinished();
  
  public boolean isAborted();

  public MonetLink toMonetLink();
  
}