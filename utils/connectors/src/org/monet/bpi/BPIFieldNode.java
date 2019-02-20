package org.monet.bpi;

import org.monet.bpi.types.Link;
import org.monet.bpi.types.Term;

public interface BPIFieldNode<Node extends BPIBaseNode<?>> extends BPIField<Link> {

  public Term getAsTerm();
  public Node getNode();
  
  
}