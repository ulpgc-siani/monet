package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeDesktop;
import org.monet.bpi.Node;
import org.monet.bpi.NodeDesktop;

public abstract class NodeDesktopImpl 
  extends NodeImpl 
  implements NodeDesktop, BehaviorNodeDesktop {

  protected Node getChildNode(String code) {
    return null;
  }
  
  protected Node getLinkedNode(String name) {
    return null;
  }

}
