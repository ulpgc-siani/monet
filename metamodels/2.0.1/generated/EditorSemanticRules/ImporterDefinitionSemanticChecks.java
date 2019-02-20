package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ImporterDefinitionSemanticChecks extends DefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof ImporterDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      ImporterDefinition metaitem = (ImporterDefinition) definition;

  

    
            
            if(metaitem.getSource() != null) {
      ImporterDefinition.Source item = metaitem.getSource();
                                        }
          
            if(metaitem.getSchema() != null) {
      ImporterDefinition.Schema item = metaitem.getSchema();
                          }
        }
}


















