package org.monet.space.kernel.machines.ttm.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;

import java.util.HashMap;

public class ProcessBase {

	@Attribute(name = "place")
	protected String place;

	@ElementMap(name = "lock-states")
	protected HashMap<String, Integer> lockStates = new HashMap<String, Integer>();

	@Attribute(name = "is-initialized")
	protected boolean isInitialized = false;
	@Attribute(name = "is-finished")
	protected boolean isFinished = false;

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public HashMap<String, Integer> getLockStates() {
		return lockStates;
	}

}
