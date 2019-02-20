package org.monet.bpi.java;

import org.monet.bpi.Task;
import org.monet.bpi.TaskService;
import org.monet.metamodel.TaskDefinition;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.BusinessUnit;

public class TaskServiceImpl extends TaskService {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	@Override
	public Task getImpl(String taskId) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		org.monet.space.kernel.model.Task task = taskLayer.loadTask(taskId);
		Task bpiTask = (Task) this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		((TaskImpl) bpiTask).injectTask(task);
		return bpiTask;
	}

	@Override
	public Task createImpl(Class<? extends Task> taskClass) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		TaskDefinition definition = BusinessUnit.getInstance().getBusinessModel().getDictionary().getTaskDefinition(taskClass.getName());
		org.monet.space.kernel.model.Task task = taskLayer.createTask(definition.getCode());
		Task bpiTask = (Task) this.bpiClassLocator.instantiateBehaviour(definition);
		((TaskImpl) bpiTask).injectTask(task);
		return bpiTask;
	}

	public static void init() {
		instance = new TaskServiceImpl();
	}

}
