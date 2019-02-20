package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class CubeDefinitionSyncDDBB extends AnalyticalDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      CubeDefinition metaitem = (CubeDefinition) definition;

  

            FactDeclarationSyncDDBB factDeclaration = new FactDeclarationSyncDDBB();
      factDeclaration.setModule(this.getModule());
      factDeclaration.sync(metaitem.getFactDeclaration());
                  DimensionDeclarationSyncDDBB dimensionDeclaration = new DimensionDeclarationSyncDDBB();
      dimensionDeclaration.setModule(this.getModule());
      for(DimensionDeclaration include : metaitem.getDimensionDeclarationList()) {
        dimensionDeclaration.sync(include);
      }
                  IndicatorDeclarationSyncDDBB indicatorDeclaration = new IndicatorDeclarationSyncDDBB();
      indicatorDeclaration.setModule(this.getModule());
      for(IndicatorDeclaration include : metaitem.getIndicatorDeclarationList()) {
        indicatorDeclaration.sync(include);
      }
        }
}


















