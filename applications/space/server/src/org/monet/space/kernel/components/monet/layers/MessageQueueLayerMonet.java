package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.MessageQueueLayer;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.MessageQueueItem;
import org.monet.space.kernel.producers.ProducerMessageQueue;

import java.io.InputStream;
import java.util.List;

public class MessageQueueLayerMonet extends PersistenceLayerMonet implements MessageQueueLayer {

	public MessageQueueLayerMonet(ComponentPersistence componentPersistence) {
		super(componentPersistence);
	}

	@Override
	public long create(String orderId, String url, InputStream message, String code, String type, String hash) {
		ProducerMessageQueue producerMessageQueue;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerMessageQueue = (ProducerMessageQueue) this.producersFactory.get(Producers.MESSAGEQUEUE);

		return producerMessageQueue.insert(orderId, url, message, code, type, hash);
	}

	@Override
	public List<MessageQueueItem> loadNotSent() {
		ProducerMessageQueue producerMessageQueue;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerMessageQueue = (ProducerMessageQueue) this.producersFactory.get(Producers.MESSAGEQUEUE);

		return producerMessageQueue.loadPendings();
	}

	@Override
	public void save(MessageQueueItem item) {
		ProducerMessageQueue producerMessageQueue;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerMessageQueue = (ProducerMessageQueue) this.producersFactory.get(Producers.MESSAGEQUEUE);
		producerMessageQueue.save(item);
	}

	@Override
	public void delete(MessageQueueItem item) {
		ProducerMessageQueue producerMessageQueue;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerMessageQueue = (ProducerMessageQueue) this.producersFactory.get(Producers.MESSAGEQUEUE);
		producerMessageQueue.delete(item);
	}

	@Override
	public InputStream getMessageStream(MessageQueueItem item) {
		ProducerMessageQueue producerMessageQueue;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerMessageQueue = (ProducerMessageQueue) this.producersFactory.get(Producers.MESSAGEQUEUE);

		return producerMessageQueue.getMessageStream(item);
	}

}
