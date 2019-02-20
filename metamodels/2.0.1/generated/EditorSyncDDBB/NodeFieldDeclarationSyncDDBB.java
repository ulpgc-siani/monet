package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class NodeFieldDeclarationSyncDDBB extends MultipleableFieldDeclarationSyncDDBB {

  public void sync(NodeFieldDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

for(NodeFieldDeclaration.Add item : metaitem.getAddList()) {
}
            NodeFieldViewDeclarationSyncDDBB nodeFieldViewDeclaration = new NodeFieldViewDeclarationSyncDDBB();
      nodeFieldViewDeclaration.setModule(this.getModule());
      nodeFieldViewDeclaration.sync(metaitem.getNodeFieldViewDeclaration());
        }
}


















