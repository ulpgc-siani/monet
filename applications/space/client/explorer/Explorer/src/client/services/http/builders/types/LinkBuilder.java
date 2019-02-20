package client.services.http.builders.types;

import client.core.system.types.Link;
import client.core.system.types.LinkList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

public class LinkBuilder implements Builder<client.core.model.types.Link, client.core.model.types.LinkList> {
	@Override
	public client.core.model.types.Link build(HttpInstance instance) {
		if (instance == null || instance.getString("id").isEmpty())
			return null;

		Link link = new Link();
		initialize(link, instance);
		return link;
	}

	@Override
	public void initialize(client.core.model.types.Link object, HttpInstance instance) {
		Link link = (Link)object;
		link.setId(instance.getString("id"));
		link.setLabel(instance.getString("label"));
	}

	@Override
	public client.core.model.types.LinkList buildList(HttpList instance) {
		LinkList result = new LinkList();
		result.setTotalCount(instance.getTotalCount());

		for (int i = 0; i < instance.getItems().length(); i++) {
			result.add(build(instance.getItems().get(i)));
		}

		return result;
	}
}
