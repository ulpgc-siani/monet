package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeDocument;
import org.monet.bpi.NodeDocument;
import org.monet.bpi.Schema;
import org.monet.bpi.types.File;

public abstract class NodeDocumentImpl  
  extends NodeImpl 
  implements NodeDocument, BehaviorNodeDocument {

  @Override
  public Schema genericGetSchema() {
    return null;
  }
  
  public void consolidate() {
  }
  
  public void overwriteContent(File newContent) {
  }
  
}

