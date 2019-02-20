package client.services.http.builders;

import client.core.model.List;
import client.core.system.MonetLink;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class MonetLinkBuilder implements Builder<client.core.model.MonetLink, List<client.core.model.MonetLink>> {

	@Override
	public client.core.model.MonetLink build(HttpInstance instance) {
		if (instance == null)
			return null;

		MonetLink link = new MonetLink();
		initialize(link, instance);
		return link;
	}

	@Override
	public void initialize(client.core.model.MonetLink object, HttpInstance instance) {
		MonetLink link = (MonetLink)object;
		link.fromString(instance.getString("value"), instance.getString("label"));
	}

	@Override
	public List<client.core.model.MonetLink> buildList(HttpList instance) {
		List<client.core.model.MonetLink> result = new MonetList<>();

		for (int i = 0; i < instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return result;
	}
}
