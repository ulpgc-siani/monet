package org.monet.bpi.java;

import java.util.List;

import org.monet.bpi.BehaviorNodeForm;
import org.monet.bpi.Field;
import org.monet.bpi.NodeForm;
import org.monet.bpi.types.Location;

public abstract class NodeFormImpl 
  extends NodeImpl 
  implements NodeForm, BehaviorNodeForm {

  public <T, F extends Field<V>, V> T getField(String definition, String name) {
    return null;
  }

  @Override
  public List<Field<?>> getFields() {
    return null;
  }
  
  @Override
  public <T, F extends Field<V>, V> T getField(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void onFieldChanged(Field<?> field) {
  }
  
  public Location getLocation() {
    return null;
  }
  
  public void setLocation(Location bpilocation) {
  }
  
  public void reset() {
  }

}
