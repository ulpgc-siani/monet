package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.ServiceLayer;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Service;
import org.monet.space.kernel.producers.ProducerService;

public class ServiceLayerMonet extends PersistenceLayerMonet implements ServiceLayer {

	public ServiceLayerMonet(ComponentPersistence componentPersistence) {
		super(componentPersistence);
	}

	@Override
	public Service loadService(String serviceId) {
		ProducerService producerService;
		Service service;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerService = (ProducerService) this.producersFactory.get(Producers.SERVICE);
		service = producerService.load(serviceId);

		return service;
	}

	@Override
	public Service loadServiceForTask(String idTask) {
		ProducerService producerService;
		Service service;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerService = (ProducerService) this.producersFactory.get(Producers.SERVICE);
		service = producerService.loadForTask(idTask);

		return service;
	}

	@Override
	public Service loadServiceByRequestId(String requestId) {
		ProducerService producerService;
		Service service;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerService = (ProducerService) this.producersFactory.get(Producers.SERVICE);
		service = producerService.loadByRequestId(requestId);

		return service;
	}

	@Override
	public Service createService(String code) {
		ProducerService producerService;
		Service service;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerService = (ProducerService) this.producersFactory.get(Producers.SERVICE);
		service = producerService.create(code);

		return service;
	}

	@Override
	public void saveService(Service service) {
		ProducerService producerService;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerService = (ProducerService) this.producersFactory.get(Producers.SERVICE);

		producerService.save(service);
	}

	@Override
	public void removeService(String serviceId) {
		ProducerService producerService;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerService = (ProducerService) this.producersFactory.get(Producers.SERVICE);

		producerService.remove(serviceId);
	}

}
