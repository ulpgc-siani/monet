package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class CatalogDefinitionSemanticChecks extends SetDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof CatalogDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      CatalogDefinition metaitem = (CatalogDefinition) definition;

  

            ParameterDeclarationSemanticChecks parameterDeclaration = new ParameterDeclarationSemanticChecks();
      parameterDeclaration.setProblems(this.getProblems());
      parameterDeclaration.setModule(this.getModule());
      for(ParameterDeclaration include : metaitem.getParameterDeclarationList()) {
        parameterDeclaration.check(include);
      }
        }
}


















