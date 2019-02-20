package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class TaskDefinitionSemanticChecks extends EntityDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof TaskDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      TaskDefinition metaitem = (TaskDefinition) definition;

  

    
            
            if(metaitem.getExecution() != null) {
      TaskDefinition.Execution item = metaitem.getExecution();
                                        }
          
            if(metaitem.getTarget() != null) {
      TaskDefinition.Target item = metaitem.getTarget();
                    this.checkName(item.getNode(), NodeDefinition.class);
            }
          
            if(metaitem.getInput() != null) {
      TaskDefinition.Input item = metaitem.getInput();
                    this.checkName(item.getNode(), NodeDefinition.class);
            }
          
            if(metaitem.getOutput() != null) {
      TaskDefinition.Output item = metaitem.getOutput();
                    this.checkName(item.getNode(), NodeDefinition.class);
            }
          
                    for(TaskDefinition.Implements item : metaitem.getImplementsList()) {
                        this.checkName(item.getCube(), CubeDefinition.class);
                }
                        WorkmapDeclarationSemanticChecks workmapDeclaration = new WorkmapDeclarationSemanticChecks();
      workmapDeclaration.setProblems(this.getProblems());
      workmapDeclaration.setModule(this.getModule());
      workmapDeclaration.check(metaitem.getWorkmapDeclaration());
                  TaskViewDeclarationSemanticChecks taskViewDeclaration = new TaskViewDeclarationSemanticChecks();
      taskViewDeclaration.setProblems(this.getProblems());
      taskViewDeclaration.setModule(this.getModule());
      taskViewDeclaration.check(metaitem.getTaskViewDeclaration());
        }
}


















