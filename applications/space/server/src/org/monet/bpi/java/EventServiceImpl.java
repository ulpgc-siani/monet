package org.monet.bpi.java;

import org.monet.bpi.EventService;
import org.monet.bpi.types.Date;
import org.monet.bpi.types.Event;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.EventLayer;

public class EventServiceImpl extends EventService {

	@Override
	protected void registerEventImpl(Event event) {
		EventLayer eventLayer = ComponentPersistence.getInstance().getEventLayer();
		eventLayer.register(new org.monet.space.kernel.model.Event(event.getName(), event.getDueDate().getValue(), event.getProperties()));
	}

	@Override
	protected Event loadEventImpl(String name) {
		EventLayer eventLayer = ComponentPersistence.getInstance().getEventLayer();

		org.monet.space.kernel.model.Event monetEvent = eventLayer.load(name);
		if (monetEvent == null)
			return null;

		return new Event(monetEvent.getName(), new Date(monetEvent.getDueDate()), monetEvent.getProperties());
	}

	@Override
	protected boolean cancelEventImpl(String name) {
		EventLayer eventLayer = ComponentPersistence.getInstance().getEventLayer();
		return eventLayer.cancel(name);
	}

	public static void init() {
		instance = new EventServiceImpl();
	}

}
