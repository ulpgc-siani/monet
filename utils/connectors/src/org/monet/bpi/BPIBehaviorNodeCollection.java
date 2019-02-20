package org.monet.bpi;


public interface BPIBehaviorNodeCollection<OperationEnum extends Enum<?>> extends BPIBehaviorNode<OperationEnum> {

  
  public void onItemAdded(BPINode<?, ?> newItem);
  
  
  public void onItemRemoved(BPINode<?, ?> removedItem);
  
}