package client.core.system.workmap;

import client.core.model.workmap.Place;

public class WorkMap implements client.core.model.workmap.WorkMap {
	private Place place;

	public WorkMap() {
	}

	public WorkMap(Place place) {
		this.place = place;
	}

	@Override
	public Place getPlace() {
		return this.place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	@Override
	public ClassName getClassName() {
		return client.core.model.workmap.WorkMap.CLASS_NAME;
	}
}
