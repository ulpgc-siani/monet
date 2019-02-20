package client.services.http.builders;

import client.core.system.BusinessUnit;
import client.core.system.BusinessUnitList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class BusinessUnitBuilder implements Builder<client.core.model.BusinessUnit, client.core.model.BusinessUnitList> {
	@Override
	public client.core.model.BusinessUnit build(HttpInstance instance) {
		if (instance == null)
			return null;

		BusinessUnit businessUnit = new BusinessUnit();
		initialize(businessUnit, instance);
		return businessUnit;
	}

	@Override
	public void initialize(client.core.model.BusinessUnit object, HttpInstance instance) {
		BusinessUnit businessUnit = (BusinessUnit)object;
		businessUnit.setName(instance.getString("name"));
		businessUnit.setUrl(instance.getString("url"));
		businessUnit.setType(BusinessUnit.Type.fromString(instance.getString("type")));
		businessUnit.setActive(instance.getBoolean("active"));
		businessUnit.setDisabled(instance.getBoolean("disabled"));
	}

	@Override
	public client.core.model.BusinessUnitList buildList(HttpList instance) {
		BusinessUnitList result = new BusinessUnitList();

		for (int i=0; i< instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return result;
	}
}
