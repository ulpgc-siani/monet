package org.monet.bpi;

public interface BPINode<Owner extends BPIBaseNode<?>, 
                         Schema extends BPISchema> 
  extends BPIBaseNode<Schema> {

  public String getOwnerId();
  
  public Owner getOwner();
  
  public void setOwner(Owner owner);
   
  public boolean isPrototype();

}