package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ExporterDefinitionSemanticChecks extends DefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof ExporterDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      ExporterDefinition metaitem = (ExporterDefinition) definition;

  

    
            if(metaitem.getSchema() != null) {
      ExporterDefinition.Schema item = metaitem.getSchema();
            }
        }
}


















