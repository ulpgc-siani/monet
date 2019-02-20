package client.services.http.builders;

import client.core.model.List;
import client.core.model.Space;
import client.core.system.Federation;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class FederationBuilder implements Builder<Space.Federation, List<Space.Federation>> {
	@Override
	public client.core.model.Space.Federation build(HttpInstance instance) {
		if (instance == null)
			return null;

		Federation federation = new Federation();
		initialize(federation, instance);
		return federation;
	}

	@Override
	public void initialize(Space.Federation object, HttpInstance instance) {
		Federation federation = (Federation)object;
		federation.setLabel(instance.getString("label"));
		federation.setLogoUrl(instance.getString("logoUrl"));
		federation.setUrl(instance.getString("url"));
	}

	@Override
	public List<client.core.model.Space.Federation> buildList(HttpList instance) {
		return new MonetList<>();
	}

}
