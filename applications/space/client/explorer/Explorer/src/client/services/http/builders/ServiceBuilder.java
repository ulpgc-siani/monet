package client.services.http.builders;

import client.core.system.Service;

import client.services.http.HttpInstance;

public class ServiceBuilder extends ProcessBuilder<client.core.model.Service> {

	@Override
	public client.core.model.Service build(HttpInstance instance) {
		if (instance == null)
			return null;

		Service service = new Service();
		super.initialize(service, instance);
		return service;
	}

}
