package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ContainerViewDeclarationSemanticChecks extends NodeViewDeclarationSemanticChecks {

  public void check(ContainerViewDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
                    for(ContainerViewDeclaration.Show item : metaitem.getShowList()) {
                        this.checkName(item.getNode(), NodeDefinition.class);
                                  }
              }
}


















