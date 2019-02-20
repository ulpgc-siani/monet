package client.services.http.builders;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.Trash;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class TrashBuilder extends EntityBuilder<Trash, client.core.model.Trash, List<client.core.model.Trash>> {
	@Override
	public client.core.model.Trash build(HttpInstance instance) {
		if (instance == null)
			return null;

		Trash trash = new Trash();
		initialize(trash, instance);
		return trash;
	}

	@Override
	public void initialize(client.core.model.Trash object, HttpInstance instance) {
	}

	@Override
	public List<client.core.model.Trash> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
