package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ServiceProviderDefinitionSemanticChecks extends ProviderDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof ServiceProviderDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      ServiceProviderDefinition metaitem = (ServiceProviderDefinition) definition;

  

    
            if(metaitem.getRequest() != null) {
      ServiceProviderDefinition.Request item = metaitem.getRequest();
                    this.checkName(item.getDocument(), DocumentDefinition.class);
            }
          
            if(metaitem.getResponse() != null) {
      ServiceProviderDefinition.Response item = metaitem.getResponse();
                    this.checkName(item.getDocument(), DocumentDefinition.class);
            }
          
                    for(ServiceProviderDefinition.Implements item : metaitem.getImplementsList()) {
                        this.checkName(item.getCube(), CubeDefinition.class);
                }
              }
}


















