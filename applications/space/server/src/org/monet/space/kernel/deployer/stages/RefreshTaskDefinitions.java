package org.monet.space.kernel.deployer.stages;

import org.monet.metamodel.TaskDefinition;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.deployer.errors.ServerError;
import org.monet.space.kernel.model.BusinessUnit;

import java.util.List;

public class RefreshTaskDefinitions extends Stage {

	@Override
	public void execute() {
		ComponentPersistence componentPersistence = this.globalData.getData(ComponentPersistence.class, GlobalData.COMPONENT_PERSISTENCE);
		TaskLayer taskLayer = componentPersistence.getTaskLayer();
		BusinessUnit businessUnit = this.globalData.getData(BusinessUnit.class, GlobalData.BUSINESS_UNIT);
		List<TaskDefinition> taskDefinitions;

		try {
			taskDefinitions = businessUnit.getBusinessModel().getDictionary().getTaskDefinitionList();

			taskLayer.cleanTaskDefinitions();
			for (TaskDefinition definition : taskDefinitions) {
				try {
					taskLayer.insertTaskDefinition(definition);
				} catch (Exception exception) {
					problems.add(new ServerError(definition.getName(), exception.getMessage()));
					this.deployLogger.error(exception);
				}
			}

		} catch (Exception exception) {
			problems.add(new ServerError("", exception.getMessage()));
			this.deployLogger.error(exception);
		}
	}

	@Override
	public String getStepInfo() {
		return "Refreshing tasks definitions";
	}
}
