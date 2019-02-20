package org.monet.bpi;

import org.monet.bpi.types.Link;

public interface Node {

  public String getOwnerId();
  
  public Node getOwner();
  
  public void setOwner(Node parent);
  
  public boolean isPrototype();
  
  public String getLabel();
  
  public void setLabel(String label);

  public String getNote(String name);

  public void addNote(String name, String value);

  public void deleteNote(String name);
   
  public void save();

  public void saveNotes();
  
  public void lock();

  public void unLock();
  
  public Link toLink();
  
  public void transform();
  
  public void evaluateRules();
  
  public Node clone(Node parent);

  public void merge(Node source);

}