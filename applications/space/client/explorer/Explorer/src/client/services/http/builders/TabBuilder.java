package client.services.http.builders;

import client.core.model.List;
import client.core.model.Space;
import client.core.system.MonetList;
import client.core.system.Tab;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class TabBuilder implements Builder<Space.Tab, List<Space.Tab>> {
	@Override
	public Space.Tab build(HttpInstance instance) {
		if (instance == null)
			return null;

		Tab tab = new Tab();
		initialize(tab, instance);
		return tab;
	}

	@Override
	public void initialize(Space.Tab object, HttpInstance instance) {
		Tab tab = (Tab)object;

		if (!instance.getString("type").isEmpty())
			tab.setType(Space.Tab.Type.fromString(instance.getString("type")));

		tab.setLabel(instance.getString("label"));
		tab.setActive(instance.getBoolean("active"));
		tab.setEntity(new EntityBuilder().build(instance.getObject("entity")));
		tab.setView(new ViewBuilder().build(instance.getObject("view")));
	}

	@Override
	public List<Space.Tab> buildList(HttpList instance) {
		List<Space.Tab> result = new MonetList<>();

		for (int i=0; i< instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return result;
	}
}
