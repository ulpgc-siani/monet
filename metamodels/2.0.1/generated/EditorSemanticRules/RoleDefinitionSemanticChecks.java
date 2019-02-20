package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class RoleDefinitionSemanticChecks extends EntityDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof RoleDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      RoleDefinition metaitem = (RoleDefinition) definition;

  

    
            if(metaitem.getUse() != null) {
      RoleDefinition.Use item = metaitem.getUse();
                    this.checkName(item.getEnvironment(), NodeDefinition.class);
            }
        }
}


















