package org.monet.space.kernel.utils;

import java.util.Date;
import java.util.HashMap;

public class TimeCache<I, T> {
	Long elapsedTime = 5 * 1000 * 60L;

	private HashMap<I, Long> timeCache = new HashMap<I, Long>();
	private HashMap<I, T> cache = new HashMap<I, T>();

	public TimeCache(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public void put(I id, T value) {
		cache.put(id, value);
		timeCache.put(id, new Date().getTime());
	}

	public T get(I id) {
		Long currentTime = new Date().getTime();
		Long valueTime = timeCache.get(id);

		if (valueTime == null) return null;
		if (currentTime - valueTime > this.elapsedTime) {
			this.invalidate(id);
			return null;
		}

		return cache.get(id);
	}

	public void invalidate(I id) {
		timeCache.remove(id);
		cache.remove(id);
	}

}
