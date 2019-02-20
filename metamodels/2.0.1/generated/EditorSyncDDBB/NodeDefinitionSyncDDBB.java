package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class NodeDefinitionSyncDDBB extends EntityDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      NodeDefinition metaitem = (NodeDefinition) definition;

  

for(NodeDefinition.Implements item : metaitem.getImplementsList()) {
}
              OperationDeclarationSyncDDBB operationDeclaration = new OperationDeclarationSyncDDBB();
      operationDeclaration.setModule(this.getModule());
      for(OperationDeclaration include : metaitem.getOperationDeclarationList()) {
        operationDeclaration.sync(include);
      }
                  RuleDeclarationSyncDDBB ruleDeclaration = new RuleDeclarationSyncDDBB();
      ruleDeclaration.setModule(this.getModule());
      for(RuleDeclaration include : metaitem.getRuleDeclarationList()) {
        ruleDeclaration.sync(include);
      }
        }
}


















