package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class MapDefinitionSemanticChecks extends EntityDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof MapDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      MapDefinition metaitem = (MapDefinition) definition;

  

    
                    for(MapDefinition.Contain item : metaitem.getContainList()) {
                                          if(item.getNode() != null && !item.getNode().isEmpty())
      this.checkName(item.getNode(), NodeDefinition.class);
                }
              }
}


















