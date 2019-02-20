package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class NumberFieldDeclarationSyncDDBB extends MultipleableFieldDeclarationSyncDDBB {

  public void sync(NumberFieldDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

            MetricDeclarationSyncDDBB metricDeclaration = new MetricDeclarationSyncDDBB();
      metricDeclaration.setModule(this.getModule());
      for(MetricDeclaration include : metaitem.getMetricDeclarationList()) {
        metricDeclaration.sync(include);
      }
        }
}


















