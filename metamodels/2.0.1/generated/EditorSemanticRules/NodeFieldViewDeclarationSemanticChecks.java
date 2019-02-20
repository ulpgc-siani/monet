package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class NodeFieldViewDeclarationSemanticChecks extends FieldViewDeclarationSemanticChecks {

  public void check(NodeFieldViewDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getRows() != null) {
      NodeFieldViewDeclaration.Rows item = metaitem.getRows();
                          }
        }
}


















