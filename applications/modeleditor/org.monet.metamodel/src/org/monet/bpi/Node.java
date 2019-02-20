package org.monet.bpi;

import java.util.List;
import java.util.Map;

import org.monet.bpi.types.Date;
import org.monet.bpi.types.Link;

public interface Node {

  public User getAuthor();
  
  public String getOwnerId();
  
  public Node getOwner();
  
  public void setOwner(Node parent);
  
  public void addPermission(String userId);

  public void addPermission(User user);

  public void addPermission(String userId, Date beginDate);

  public void addPermission(User user, Date beginDate);

  public void addPermission(String userId, Date beginDate, Date endDate);

  public void addPermission(User user, Date beginDate, Date endDate);

  public boolean hasPermission(String userId);

  public boolean hasPermission(User user);

  public void deletePermission(String userId);

  public void deletePermission(User user);

  public boolean isPrototype();
  
  public Node getPrototypeNode();
  
  public boolean isGeoReferenced();
  
  public String getCode();
  
  public String getName();
  
  public String getLabel();
  
  public void setLabel(String label);

  public String getDescription();
  
  public void setDescription(String description);

  public String getColor();
  
  public void setColor(String color);

  public Map<String, String> getFlags();

  public String getFlag(String name);

  public void setFlag(String name, String value);

  public void removeFlag(String name);

  public String getNote(String name);

  public void setNote(String name, String value);

  public void removeNote(String name);
   
  public void save();

  public void lock();

  public void unLock();
  
  public boolean isLocked();
  
  public Link toLink();
  
  public MonetLink toMonetLink();
  
  public MonetLink toMonetLink(boolean editMode);
  
  public void transform();
  
  public void evaluateRules();
  
  public Node clone(Node parent);

  public void merge(Node source);

  public String getPartnerContext();
  
  public void setPartnerContext(String context);

  public void setEditable(boolean value);
  
  public boolean isEditable();

  public void setDeletable(boolean value);

  public boolean isDeletable();
  
  public List<Node> getLinksIn();
  
  public List<Node> getLinksOut();

  public List<Task> getLinkedTasks();
  
}