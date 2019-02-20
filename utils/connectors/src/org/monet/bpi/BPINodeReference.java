package org.monet.bpi;

public interface BPINodeReference {

  public String getLabel();

  public void setLabel(String label);

  public String getDescription();

  public void setDescription(String description);

  public String getAttributeValue(String code);
  
  public void setAttributeValue(String code, String value);
 
}