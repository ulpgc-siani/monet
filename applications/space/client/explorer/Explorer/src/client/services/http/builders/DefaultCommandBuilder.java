package client.services.http.builders;

import client.core.system.Command;

import client.services.http.HttpInstance;

public class DefaultCommandBuilder extends CommandBuilder {
	@Override
	public client.core.model.Command build(HttpInstance instance) {
		if (instance == null)
			return null;

		Command command = new Command();
		initialize(command, instance);
		return command;
	}

}
