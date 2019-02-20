package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.machines.ttm.behavior.ProcessBehavior;
import org.monet.space.kernel.model.Task;

public class ActionGotoPlaceInTask extends Action {

	public ActionGotoPlaceInTask() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String place = (String) this.parameters.get(Parameter.PLACE);
		String history = (String) this.parameters.get(Parameter.HISTORY);
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		task = taskLayer.loadTask(id);

		ProcessBehavior process = task.getProcess();
		process.doGoto(place, LibraryEncoding.decode(history));
		process.resume();

		return MessageCode.TASK_RUN;
	}

}
