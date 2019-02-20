package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class DesktopDefinitionSemanticChecks extends NodeDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof DesktopDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      DesktopDefinition metaitem = (DesktopDefinition) definition;

  

            DesktopViewDeclarationSemanticChecks desktopViewDeclaration = new DesktopViewDeclarationSemanticChecks();
      desktopViewDeclaration.setProblems(this.getProblems());
      desktopViewDeclaration.setModule(this.getModule());
      for(DesktopViewDeclaration include : metaitem.getDesktopViewDeclarationList()) {
        desktopViewDeclaration.check(include);
      }
        }
}


















