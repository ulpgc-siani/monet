package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class NodeFieldDeclarationSemanticChecks extends MultipleableFieldDeclarationSemanticChecks {

  public void check(NodeFieldDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getContain() != null) {
      NodeFieldDeclaration.Contain item = metaitem.getContain();
                    this.checkName(item.getNode(), NodeDefinition.class);
            }
          
                    for(NodeFieldDeclaration.Add item : metaitem.getAddList()) {
                        this.checkName(item.getNode(), NodeDefinition.class);
                }
                        NodeFieldViewDeclarationSemanticChecks nodeFieldViewDeclaration = new NodeFieldViewDeclarationSemanticChecks();
      nodeFieldViewDeclaration.setProblems(this.getProblems());
      nodeFieldViewDeclaration.setModule(this.getModule());
      nodeFieldViewDeclaration.check(metaitem.getNodeFieldViewDeclaration());
        }
}


















