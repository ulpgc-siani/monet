package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.MonetLink;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskFact;

import java.util.Date;

public class ActionAddTaskFact extends Action {

	public ActionAddTaskFact() {
	}

	@Override
	public String execute() {
		String taskId = (String) this.parameters.get(Parameter.ID);
		String title = (String) this.parameters.get(Parameter.TITLE);
		String subtitle = (String) this.parameters.get(Parameter.SUBTITLE);
		String userId = (String) this.parameters.get(Parameter.USER);
		String linksValues = (String) this.parameters.get(Parameter.LINKS);
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task;

		if (taskId == null)
			return ErrorCode.WRONG_PARAMETERS;

		task = taskLayer.loadTask(taskId);

		TaskFact fact = new TaskFact();
		fact.setCreateDate(new Date());
		fact.setUserId(userId);
		fact.setTaskId(task.getId());
		fact.setTitle(LibraryEncoding.decode(title));
		fact.setSubTitle(LibraryEncoding.decode(subtitle));

		String[] linksArray = LibraryEncoding.decode(linksValues).split(PARAMETER_SEPARATOR);
		for (String linkValue : linksArray) {
			MonetLink link = MonetLink.from(linkValue);
			fact.addLink(link);
		}
		task.getProcess().addFact(fact);

		return MessageCode.TASK_FACT_ADDED;
	}

}
