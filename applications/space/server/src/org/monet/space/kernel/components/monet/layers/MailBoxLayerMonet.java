package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.MailBoxLayer;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.machines.ttm.model.MailBox;
import org.monet.space.kernel.producers.ProducerMailBox;

public class MailBoxLayerMonet extends PersistenceLayerMonet implements MailBoxLayer {

	public MailBoxLayerMonet(ComponentPersistence componentPersistenceMonet) {
		super(componentPersistenceMonet);
	}

    @Override
    public boolean exists(String mailBoxId) {
        ProducerMailBox producerMailBox;

        if (!this.isStarted()) {
            throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
        }

        producerMailBox = (ProducerMailBox) this.producersFactory.get(Producers.MAILBOX);
        return producerMailBox.exists(mailBoxId);
    }

    @Override
	public MailBox load(String id) {
		ProducerMailBox producerMailBox;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerMailBox = (ProducerMailBox) this.producersFactory.get(Producers.MAILBOX);
		return producerMailBox.load(id);
	}

	@Override
	public void create(MailBox mailBox) {
		ProducerMailBox producerMailBox;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerMailBox = (ProducerMailBox) this.producersFactory.get(Producers.MAILBOX);
		producerMailBox.create(mailBox);
	}

	@Override
	public void delete(String id) {
		ProducerMailBox producerMailBox;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerMailBox = (ProducerMailBox) this.producersFactory.get(Producers.MAILBOX);
		producerMailBox.delete(id);
	}

	@Override
	public void deleteWithTaskId(String taskId) {
		ProducerMailBox producerMailBox;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerMailBox = (ProducerMailBox) this.producersFactory.get(Producers.MAILBOX);
		producerMailBox.deleteWithTaskId(taskId);
	}

	@Override
	public void addPermission(String mailBoxId, String userId) {
		ProducerMailBox producerMailBox;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerMailBox = (ProducerMailBox) this.producersFactory.get(Producers.MAILBOX);
		producerMailBox.addPermission(mailBoxId, userId);
	}

	@Override
	public void removePermission(String mailBoxId, String userId) {
		ProducerMailBox producerMailBox;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerMailBox = (ProducerMailBox) this.producersFactory.get(Producers.MAILBOX);
		producerMailBox.removePermission(mailBoxId, userId);
	}

    @Override
    public boolean hasPermission(String mailBoxId, String userId) {
        ProducerMailBox producerMailBox;

        if (!this.isStarted()) {
            throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
        }

        producerMailBox = (ProducerMailBox) this.producersFactory.get(Producers.MAILBOX);
        return producerMailBox.hasPermission(mailBoxId, userId);
    }

}
