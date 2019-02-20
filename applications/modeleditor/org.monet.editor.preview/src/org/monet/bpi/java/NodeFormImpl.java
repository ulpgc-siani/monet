package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeForm;
import org.monet.bpi.Field;
import org.monet.bpi.NodeForm;
import org.monet.bpi.types.Location;

public abstract class NodeFormImpl 
  extends NodeImpl 
  implements NodeForm, BehaviorNodeForm {

  protected <T, F extends Field<V>, V> T getField(String definition, String name) {
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

}
