package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class CollectionDefinitionSemanticChecks extends SetDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof CollectionDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      CollectionDefinition metaitem = (CollectionDefinition) definition;

  

    
                    for(CollectionDefinition.Add item : metaitem.getAddList()) {
                        this.checkName(item.getNode(), NodeDefinition.class);
                }
                
            if(metaitem.getImport() != null) {
      CollectionDefinition.Import item = metaitem.getImport();
                    if(item.getName() != null && !item.getName().isEmpty())
      this.checkName(item.getName(), ImporterDefinition.class);
            }
        }
}


















