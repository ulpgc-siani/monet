package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class NodeDefinitionSemanticChecks extends EntityDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof NodeDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      NodeDefinition metaitem = (NodeDefinition) definition;

  

    
            
            
            
            
            
            
            
            if(metaitem.getSchema() != null) {
      NodeDefinition.Schema item = metaitem.getSchema();
            }
          
                    for(NodeDefinition.Implements item : metaitem.getImplementsList()) {
                        if(item.getReference() != null && !item.getReference().isEmpty())
      this.checkName(item.getReference(), ReferenceDefinition.class);
                }
                          OperationDeclarationSemanticChecks operationDeclaration = new OperationDeclarationSemanticChecks();
      operationDeclaration.setProblems(this.getProblems());
      operationDeclaration.setModule(this.getModule());
      for(OperationDeclaration include : metaitem.getOperationDeclarationList()) {
        operationDeclaration.check(include);
      }
                  RuleDeclarationSemanticChecks ruleDeclaration = new RuleDeclarationSemanticChecks();
      ruleDeclaration.setProblems(this.getProblems());
      ruleDeclaration.setModule(this.getModule());
      for(RuleDeclaration include : metaitem.getRuleDeclarationList()) {
        ruleDeclaration.check(include);
      }
        }
}


















