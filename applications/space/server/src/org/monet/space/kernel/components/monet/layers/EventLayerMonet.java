package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.EventLayer;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.model.Event;
import org.monet.space.kernel.model.EventList;
import org.monet.space.kernel.producers.ProducerEvent;
import org.monet.space.kernel.producers.ProducerEventList;

public class EventLayerMonet extends PersistenceLayerMonet implements EventLayer {

	public EventLayerMonet(ComponentPersistence componentPersistenceMonet) {
		super(componentPersistenceMonet);
	}

	@Override
	public EventList loadDueEvents() {
		ProducerEventList producer = this.producersFactory.get(Producers.EVENTLIST);
		return producer.loadDueEvents();
	}

	@Override
	public void register(Event event) {
		ProducerEvent producer = this.producersFactory.get(Producers.EVENT);
		producer.create(event);
	}

	@Override
	public Event load(String name) {
		ProducerEvent producer = this.producersFactory.get(Producers.EVENT);
		return producer.load(name);
	}

	@Override
	public void saveFired(Event event, boolean fired) {
		ProducerEvent producer = this.producersFactory.get(Producers.EVENT);
		producer.saveFired(event, fired);
	}

	@Override
	public boolean cancel(String name) {
		ProducerEvent producer = this.producersFactory.get(Producers.EVENT);
		return producer.cancel(name);
	}

	@Override
	public void notifyEvent(Event event) {
		ProducerEvent producer = this.producersFactory.get(Producers.EVENT);
		producer.notify(event);
	}
}
