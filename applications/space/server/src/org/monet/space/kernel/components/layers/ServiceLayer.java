package org.monet.space.kernel.components.layers;

import org.monet.space.kernel.model.Service;

public interface ServiceLayer extends Layer {

	//Service Instances
	public Service loadService(String serviceInstanceId);

	public Service loadServiceForTask(String taskId);

	public Service loadServiceByRequestId(String requestId);

	public Service createService(String definitionKey);

	public void saveService(Service service);

	public void removeService(String serviceInstanceId);

}
