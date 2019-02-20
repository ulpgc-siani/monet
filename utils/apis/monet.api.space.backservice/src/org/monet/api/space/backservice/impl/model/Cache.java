package org.monet.api.space.backservice.impl.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cache<T> extends LinkedHashMap<String, T> {

	static final int MAX_ENTRIES = 400;

	public Cache() {
		this(MAX_ENTRIES);
	}

	public Cache(int maxEntries) {
		super(maxEntries, .75F, true);
	}

	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(key) && get(key) != null;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<String, T> eldest) {
		return size() > MAX_ENTRIES;
	}

}
