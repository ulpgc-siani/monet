package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ContainerDefinitionSemanticChecks extends NodeDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof ContainerDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      ContainerDefinition metaitem = (ContainerDefinition) definition;

  

    
                    for(ContainerDefinition.Contain item : metaitem.getContainList()) {
                        this.checkName(item.getNode(), NodeDefinition.class);
                }
                        ContainerViewDeclarationSemanticChecks containerViewDeclaration = new ContainerViewDeclarationSemanticChecks();
      containerViewDeclaration.setProblems(this.getProblems());
      containerViewDeclaration.setModule(this.getModule());
      for(ContainerViewDeclaration include : metaitem.getContainerViewDeclarationList()) {
        containerViewDeclaration.check(include);
      }
        }
}


















