package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class CubeDefinitionSemanticChecks extends AnalyticalDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof CubeDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      CubeDefinition metaitem = (CubeDefinition) definition;

  

    
            if(metaitem.getSchema() != null) {
      CubeDefinition.Schema item = metaitem.getSchema();
            }
          
            if(metaitem.getExternal() != null) {
      CubeDefinition.External item = metaitem.getExternal();
                    this.checkName(item.getProvider(), CubeProviderDefinition.class);
            }
                  FactDeclarationSemanticChecks factDeclaration = new FactDeclarationSemanticChecks();
      factDeclaration.setProblems(this.getProblems());
      factDeclaration.setModule(this.getModule());
      factDeclaration.check(metaitem.getFactDeclaration());
                  DimensionDeclarationSemanticChecks dimensionDeclaration = new DimensionDeclarationSemanticChecks();
      dimensionDeclaration.setProblems(this.getProblems());
      dimensionDeclaration.setModule(this.getModule());
      for(DimensionDeclaration include : metaitem.getDimensionDeclarationList()) {
        dimensionDeclaration.check(include);
      }
                  IndicatorDeclarationSemanticChecks indicatorDeclaration = new IndicatorDeclarationSemanticChecks();
      indicatorDeclaration.setProblems(this.getProblems());
      indicatorDeclaration.setModule(this.getModule());
      for(IndicatorDeclaration include : metaitem.getIndicatorDeclarationList()) {
        indicatorDeclaration.check(include);
      }
        }
}


















