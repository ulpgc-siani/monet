package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.EventLogLayer;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.EventLog;
import org.monet.space.kernel.model.EventLogList;
import org.monet.space.kernel.producers.ProducerEventLog;

import java.util.List;

public class EventLogLayerMonet extends PersistenceLayerMonet implements EventLogLayer {

	public EventLogLayerMonet(ComponentPersistence componentPersistence) {
		super(componentPersistence);
	}

	@Override
	public void insertEventLogBlock(List<EventLog> eventLogs) {
		ProducerEventLog producerEventLog;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerEventLog = (ProducerEventLog) this.producersFactory.get(Producers.EVENTLOG);

		producerEventLog.insert(eventLogs);
	}

	@Override
	public EventLogList loadEventLogPage(String logger, int offset, int pageSize) {
		ProducerEventLog producerEventLog;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerEventLog = (ProducerEventLog) this.producersFactory.get(Producers.EVENTLOG);

		return producerEventLog.load(logger, offset, pageSize);
	}

	@Override
	public void clearEventLog(String logger) {
		ProducerEventLog producerEventLog;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerEventLog = (ProducerEventLog) this.producersFactory.get(Producers.EVENTLOG);

		producerEventLog.clear(logger);
	}

}
