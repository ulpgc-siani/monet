package client.services.http;

import client.core.model.Space;
import client.services.Services;

public class HttpService {

	protected final Stub stub;
	protected final Services services;

	public HttpService(Stub stub, Services services) {
		this.stub = stub;
		this.services = services;
	}

	protected Space.Configuration getConfiguration() {
		return services.getSpaceService().load().getConfiguration();
	}

}
