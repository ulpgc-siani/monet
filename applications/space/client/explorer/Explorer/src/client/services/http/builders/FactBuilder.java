package client.services.http.builders;

import client.core.model.List;
import client.core.model.MonetLink;
import client.core.system.Fact;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import com.google.gwt.core.client.JsArray;

public class FactBuilder implements Builder<client.core.model.Fact, List<client.core.model.Fact>> {
	@Override
	public client.core.model.Fact build(HttpInstance instance) {
		if (instance == null)
			return null;

		Fact fact = new Fact();
		initialize(fact, instance);
		return fact;
	}

	@Override
	public void initialize(client.core.model.Fact object, HttpInstance instance) {
		Fact fact = (Fact)object;

		fact.setTitle(instance.getString("title"));
		fact.setSubTitle(instance.getString("subTitle"));
		fact.setUser(instance.getString("user"));
		fact.setLinks(getLinks(instance.getArray("links")));
		fact.setCreateDate(instance.getDate("createDate"));
	}

	private List<MonetLink> getLinks(JsArray<HttpInstance> links) {
		List<MonetLink> result = new MonetList<>();

		for (int i = 0; i < links.length(); i++) {
			HttpInstance link = links.get(i);
			result.add(new MonetLinkBuilder().build(link));
		}

		return result;
	}

	@Override
	public List<client.core.model.Fact> buildList(HttpList instances) {
		List<client.core.model.Fact> result = new MonetList<>();
		JsArray<HttpInstance> items = instances.getItems();

		for (int i = 0; i < items.length(); i++)
			result.add(build(items.get(i)));

		return result;
	}
}
