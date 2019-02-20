package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class TaskDefinitionSyncDDBB extends EntityDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      TaskDefinition metaitem = (TaskDefinition) definition;

  

for(TaskDefinition.Implements item : metaitem.getImplementsList()) {
}
            WorkmapDeclarationSyncDDBB workmapDeclaration = new WorkmapDeclarationSyncDDBB();
      workmapDeclaration.setModule(this.getModule());
      workmapDeclaration.sync(metaitem.getWorkmapDeclaration());
                  TaskViewDeclarationSyncDDBB taskViewDeclaration = new TaskViewDeclarationSyncDDBB();
      taskViewDeclaration.setModule(this.getModule());
      taskViewDeclaration.sync(metaitem.getTaskViewDeclaration());
        }
}


















