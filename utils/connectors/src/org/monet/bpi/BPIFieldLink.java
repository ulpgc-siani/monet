package org.monet.bpi;

import org.monet.bpi.types.Link;
import org.monet.bpi.types.Term;

public interface BPIFieldLink<LinkedNode extends BPIBaseNode<?>> extends BPIField<Link> {

  public <T extends LinkedNode> T getNode();
  public Term getAsTerm();
}