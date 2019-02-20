package client.services.http.builders;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.Role;
import client.core.system.Trash;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class RolesBuilder extends EntityBuilder<Role, client.core.model.Role, List<client.core.model.Role>> {
	@Override
	public client.core.model.Role build(HttpInstance instance) {
		return null;
	}

	@Override
	public void initialize(client.core.model.Role object, HttpInstance instance) {
	}

	@Override
	public List<client.core.model.Role> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
