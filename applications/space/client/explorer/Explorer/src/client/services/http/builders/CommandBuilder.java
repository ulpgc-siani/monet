package client.services.http.builders;

import client.core.model.List;
import client.core.system.Command;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class CommandBuilder extends EntityBuilder<Command,client.core.model.Command,List<client.core.model.Command>> {

	@Override
	public void initialize(client.core.model.Command object, HttpInstance instance) {
		Command command = (Command)object;
		command.setKey(new client.core.system.Key(instance.getString("code"), instance.getString("name")));
		command.setEnabled(instance.getBoolean("enabled"));
		command.setVisible(instance.getBoolean("visible"));
	}

	@Override
	public client.core.model.List<client.core.model.Command> buildList(HttpList instance) {
		client.core.model.List<client.core.model.Command> result = new MonetList<>();

		for (int i = 0; i < instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return result;
	}
}
