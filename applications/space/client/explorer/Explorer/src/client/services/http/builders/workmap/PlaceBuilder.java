package client.services.http.builders.workmap;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.workmap.Place;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

public class PlaceBuilder implements Builder<client.core.model.workmap.Place, List<client.core.model.workmap.Place>> {
	@Override
	public client.core.model.workmap.Place build(HttpInstance instance) {
		if (instance == null)
			return null;

		Place place = new Place();
		initialize(place, instance);
		return place;
	}

	@Override
	public void initialize(client.core.model.workmap.Place object, HttpInstance instance) {
		Place place = (Place)object;
		place.setCode(instance.getString("code"));

		if (instance.getObject("action") != null)
			place.setAction(new ActionBuilder().build(instance.getObject("action")));
	}

	@Override
	public List<client.core.model.workmap.Place> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
