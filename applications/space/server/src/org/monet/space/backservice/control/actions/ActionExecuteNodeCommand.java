package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.MonetEvent;

public class ActionExecuteNodeCommand extends Action {

	public ActionExecuteNodeCommand() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID_NODE);
		String command = (String) this.parameters.get(Parameter.COMMAND);
		org.monet.space.kernel.model.Node node;

		if (id == null || command == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		node = ComponentPersistence.getInstance().getNodeLayer().loadNode(id);

		MonetEvent event = new MonetEvent(MonetEvent.NODE_EXECUTE_COMMAND, null, node);
		event.addParameter(MonetEvent.PARAMETER_COMMAND, LibraryEncoding.decode(command));
		AgentNotifier.getInstance().notify(event);

		return MessageCode.NODE_COMMAND_EXECUTED;
	}

}
