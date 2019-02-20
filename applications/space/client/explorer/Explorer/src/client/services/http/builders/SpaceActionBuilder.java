package client.services.http.builders;

import client.core.system.MonetList;
import client.core.system.Space;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

import static client.core.model.Space.Action;

public class SpaceActionBuilder implements Builder<client.core.model.Space.Action, client.core.model.List<client.core.model.Space.Action>> {
	@Override
	public client.core.model.Space.Action build(HttpInstance instance) {
		if (instance == null)
			return null;

		Space.Action action = Space.Action.parse(instance.asString());
		initialize(action, instance);
		return action;
	}

	@Override
	public void initialize(Action object, HttpInstance instance) {
	}

	@Override
	public client.core.model.List<client.core.model.Space.Action> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
