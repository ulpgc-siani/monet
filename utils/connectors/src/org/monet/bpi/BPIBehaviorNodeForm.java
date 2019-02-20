package org.monet.bpi;


public interface BPIBehaviorNodeForm<OperationEnum extends Enum<?>> extends BPIBehaviorNode<OperationEnum> {
  
  public void onFieldChanged(BPIField<?> field);
  
}