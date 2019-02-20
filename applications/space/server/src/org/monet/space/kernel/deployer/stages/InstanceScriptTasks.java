package org.monet.space.kernel.deployer.stages;

import org.monet.metamodel.Distribution;
import org.monet.metamodel.TaskDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.constants.TaskState;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.deployer.errors.ServerError;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Task;

public class InstanceScriptTasks extends Stage {

	@Override
	public void execute() {
		ComponentPersistence componentPersistence = this.globalData.getData(ComponentPersistence.class, GlobalData.COMPONENT_PERSISTENCE);
		TaskLayer taskLayer = componentPersistence.getTaskLayer();

		try {
			Dictionary dictionary = Dictionary.getInstance();

			Distribution distribution = BusinessUnit.getInstance().getDistribution();
			boolean isFirst = true;

			for (Ref script : distribution.getScript()) {
				TaskDefinition definition = dictionary.getTaskDefinition(script.getValue());
				String taskCode = definition.getCode();

				String taskId = taskLayer.getTaskIdIfUnique(taskCode, null);
				if (taskId == null) {
					Task task = taskLayer.createInitializerTask(taskCode);
					if (isFirst) {
						task.getProcess().resume();
						task = taskLayer.loadTask(task.getId()); // Fix obsolete memory task
						isFirst = task.getState().equals(TaskState.FINISHED);
					}
				}
			}

		} catch (Exception exception) {
			problems.add(new ServerError("", exception.getMessage()));
			this.deployLogger.error(exception);
		}
	}

	@Override
	public String getStepInfo() {
		return "Instancing and executing script tasks";
	}
}
