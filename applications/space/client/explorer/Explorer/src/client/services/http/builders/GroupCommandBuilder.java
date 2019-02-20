package client.services.http.builders;

import client.core.system.GroupCommand;

import client.services.http.HttpInstance;

public class GroupCommandBuilder extends CommandBuilder {
	@Override
	public client.core.model.Command build(HttpInstance instance) {
		if (instance == null)
			return null;

		GroupCommand command = new GroupCommand();
		initialize(command, instance);
		return command;
	}

	@Override
	public void initialize(client.core.model.Command object, HttpInstance instance) {
		super.initialize(object, instance);

		GroupCommand command = (GroupCommand)object;
		command.setCommandList(buildList(instance.getList("children")));
	}

}
