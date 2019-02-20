package org.monet.bpi.java;

import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPIBehaviorImporterContextual;

public abstract class BPIImporterContextualImpl<Schema> extends BPIImporterImpl<Schema> implements BPIBehaviorImporterContextual<Schema> {

  public void onInitialize() {
    
  }
  
  @Override
  public abstract void onInitialize(BPIBaseNode<?> context);
  
}
