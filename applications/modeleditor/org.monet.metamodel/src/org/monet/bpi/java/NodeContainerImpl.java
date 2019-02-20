package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeContainer;
import org.monet.bpi.Node;
import org.monet.bpi.NodeContainer;
import org.monet.bpi.types.Location;

public abstract class NodeContainerImpl 
  extends NodeImpl 
  implements NodeContainer, BehaviorNodeContainer {

  protected Node getChildNode(String code) {
    return null;
  }
  
  public Location getLocation() {
    return null;
  }
  public void setLocation(Location bpilocation) {
  }
  
}
