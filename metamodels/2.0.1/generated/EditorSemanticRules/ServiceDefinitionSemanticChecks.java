package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ServiceDefinitionSemanticChecks extends InteroperabilityDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof ServiceDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      ServiceDefinition metaitem = (ServiceDefinition) definition;

  

    
            if(metaitem.getRequest() != null) {
      ServiceDefinition.Request item = metaitem.getRequest();
                    if(item.getDocument() != null && !item.getDocument().isEmpty())
      this.checkName(item.getDocument(), DocumentDefinition.class);
            }
          
            if(metaitem.getResponse() != null) {
      ServiceDefinition.Response item = metaitem.getResponse();
                    if(item.getDocument() != null && !item.getDocument().isEmpty())
      this.checkName(item.getDocument(), DocumentDefinition.class);
            }
          
            if(metaitem.getExecute() != null) {
      ServiceDefinition.Execute item = metaitem.getExecute();
                    this.checkName(item.getTask(), TaskDefinition.class);
            }
          
                    for(ServiceDefinition.Implements item : metaitem.getImplementsList()) {
                        this.checkName(item.getCube(), CubeDefinition.class);
                }
              }
}


















