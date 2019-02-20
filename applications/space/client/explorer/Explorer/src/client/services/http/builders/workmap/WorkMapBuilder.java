package client.services.http.builders.workmap;

import client.core.model.List;
import client.core.model.workmap.WorkMap;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

public class WorkMapBuilder implements Builder<WorkMap, List<WorkMap>> {
	@Override
	public WorkMap build(HttpInstance instance) {
		if (instance == null)
			return null;

		client.core.system.workmap.WorkMap workMap = new client.core.system.workmap.WorkMap();
		initialize(workMap, instance);
		return workMap;
	}

	@Override
	public void initialize(WorkMap object, HttpInstance instance) {
		client.core.system.workmap.WorkMap workMap = (client.core.system.workmap.WorkMap)object;
		workMap.setPlace(new PlaceBuilder().build(instance.getObject("place")));
	}

	@Override
	public List<WorkMap> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
