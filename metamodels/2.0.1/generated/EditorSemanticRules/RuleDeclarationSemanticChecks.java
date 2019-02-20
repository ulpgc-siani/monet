package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class RuleDeclarationSemanticChecks extends DeclarationRule {

  public void check(RuleDeclaration metaitem) {
  if(metaitem == null) return;
  
  

    
                    for(RuleDeclaration.Condition item : metaitem.getConditionList()) {
                        this.checkName(item.getField(), FieldDeclaration.class);
                                                    }
                
                    for(RuleDeclaration.Action item : metaitem.getActionList()) {
                                          if(item.getField() != null && !item.getField().isEmpty())
      this.checkName(item.getField(), FieldDeclaration.class);
                        if(item.getView() != null && !item.getView().isEmpty())
      this.checkName(item.getView(), NodeViewDeclaration.class);
                        if(item.getOperation() != null && !item.getOperation().isEmpty())
      this.checkName(item.getOperation(), OperationDeclaration.class);
                }
              }
}


















