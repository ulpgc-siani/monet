package client.services.http.builders;

import client.core.model.List;
import client.core.system.Dashboard;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class DashboardBuilder extends EntityBuilder<Dashboard, client.core.model.Dashboard, List<client.core.model.Dashboard>> {
	@Override
	public client.core.model.Dashboard build(HttpInstance instance) {
		if (instance == null)
			return null;

		client.core.system.Dashboard dashboard = new client.core.system.Dashboard();
		initialize(dashboard, instance);
		return dashboard;
	}

	@Override
	public void initialize(client.core.model.Dashboard object, HttpInstance instance) {
	}

	@Override
	public List<client.core.model.Dashboard> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
