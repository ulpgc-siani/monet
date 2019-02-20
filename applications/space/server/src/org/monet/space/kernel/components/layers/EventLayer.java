package org.monet.space.kernel.components.layers;

import org.monet.space.kernel.model.Event;
import org.monet.space.kernel.model.EventList;

public interface EventLayer extends Layer {

	EventList loadDueEvents();
	void register(Event event);
	Event load(String name);
	void saveFired(Event event, boolean fired);
	boolean cancel(String name);
	void notifyEvent(Event event);

}
