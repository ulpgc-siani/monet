package org.monet.bpi;

import org.monet.bpi.types.Event;

public interface BehaviorProject {

	/**
	 * Event called when receives a previous registered event.
	 * Events registration are made by using EventService singleton
	 */
	void onReceiveEvent(Event event);

}