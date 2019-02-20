package client.services.http.builders;

import client.core.system.Activity;

import client.services.http.HttpInstance;

public class ActivityBuilder extends ProcessBuilder<client.core.model.Activity> {

	@Override
	public client.core.model.Activity build(HttpInstance instance) {
		if (instance == null)
			return null;

		Activity activity = new Activity();
		super.initialize(activity, instance);
		return activity;
	}

}
