package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Task;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class ActionGetTaskFlags extends Action {

	public ActionGetTaskFlags() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		task = taskLayer.loadTask(id);

		if (task.getProcess() == null)
			return "";

		HashMap<String, String> flags = task.getProcess().getFlags();
		StringBuffer result = new StringBuffer();
		for (String flag : flags.keySet()) {
			if (result.length() != 0)
				result.append(PARAMETER_SEPARATOR);

			try {
				result.append(flag + "=" + URLEncoder.encode(flags.get(flag), "UTF-8"));
			} catch (UnsupportedEncodingException exception) {
				AgentLogger.getInstance().error(exception);
			}
		}

		return result.toString();
	}

}
