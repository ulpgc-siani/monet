package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.library.LibraryEncoding;

public class ActionSaveTask extends Action {

	public ActionSaveTask() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String data = (String) this.parameters.get(Parameter.DATA);
		org.monet.space.kernel.model.Task clientTask, spaceTask;

		if (id == null || data == null)
			return ErrorCode.WRONG_PARAMETERS;

		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		NodeLayer nodeLayer = componentPersistence.getNodeLayer();
		TaskLayer taskLayer = componentPersistence.getTaskLayer();


		clientTask = new org.monet.space.kernel.model.Task();
		clientTask.deserializeFromXML(LibraryEncoding.decode(data));

		spaceTask = taskLayer.loadTask(id);
		spaceTask.getCode(); // Force loading properties
		spaceTask.setTargetId(clientTask.getTargetId());
		spaceTask.setLabel(clientTask.getLabel());
		spaceTask.setDescription(clientTask.getDescription());

		taskLayer.saveTask(spaceTask);

		return MessageCode.TASK_SAVED;
	}

}
