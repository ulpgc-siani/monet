package client.services.http.builders;

import client.core.system.Job;

import client.services.http.HttpInstance;

public class JobBuilder extends TaskBuilder<client.core.model.Job> {

	@Override
	public client.core.model.Job build(HttpInstance instance) {
		if (instance == null)
			return null;

		Job job = new Job();
		super.initialize(job, instance);
		return job;
	}

}
