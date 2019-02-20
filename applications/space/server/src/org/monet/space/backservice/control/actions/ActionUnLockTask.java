package org.monet.space.backservice.control.actions;

import org.monet.metamodel.internal.Lock;
import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;
import org.monet.space.kernel.model.Task;

public class ActionUnLockTask extends Action {

	public ActionUnLockTask() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String place = (String) this.parameters.get(Parameter.PLACE);
		String stop = (String) this.parameters.get(Parameter.STOP);
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		task = taskLayer.loadTask(id);

		Lock lock = new Lock(place, stop);
		ProcessBehavior process = task.getProcess();

		process.unlock(lock);
		process.resume();

		return MessageCode.TASK_RUN;
	}

}
