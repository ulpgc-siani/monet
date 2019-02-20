package org.monet.bpi;

import org.monet.bpi.types.Event;

public abstract class EventService {

	protected static EventService instance;

	public static void registerEvent(Event event) {
		instance.registerEventImpl(event);
	}

	public static Event loadEvent(String name) {
		return instance.loadEventImpl(name);
	}

	public static boolean cancelEvent(String name) {
		return instance.cancelEventImpl(name);
	}

	protected abstract void registerEventImpl(Event event);
	protected abstract Event loadEventImpl(String name);
	protected abstract boolean cancelEventImpl(String name);
}