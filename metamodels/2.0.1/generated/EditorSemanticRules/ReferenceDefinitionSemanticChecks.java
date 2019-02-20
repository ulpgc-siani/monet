package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ReferenceDefinitionSemanticChecks extends DefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof ReferenceDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      ReferenceDefinition metaitem = (ReferenceDefinition) definition;

  

    
            if(metaitem.getAdd() != null) {
      ReferenceDefinition.Add item = metaitem.getAdd();
                    this.checkName(item.getReference(), ReferenceDefinition.class);
                    if(item.getOn() != null && !item.getOn().isEmpty())
      this.checkName(item.getOn(), AttributeDeclaration.class);
            }
                  AttributeDeclarationSemanticChecks attributeDeclaration = new AttributeDeclarationSemanticChecks();
      attributeDeclaration.setProblems(this.getProblems());
      attributeDeclaration.setModule(this.getModule());
      for(AttributeDeclaration include : metaitem.getAttributeDeclarationList()) {
        attributeDeclaration.check(include);
      }
                  ReferenceViewDeclarationSemanticChecks referenceViewDeclaration = new ReferenceViewDeclarationSemanticChecks();
      referenceViewDeclaration.setProblems(this.getProblems());
      referenceViewDeclaration.setModule(this.getModule());
      for(ReferenceViewDeclaration include : metaitem.getReferenceViewDeclarationList()) {
        referenceViewDeclaration.check(include);
      }
        }
}


















