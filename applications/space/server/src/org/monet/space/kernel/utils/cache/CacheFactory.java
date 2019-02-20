package org.monet.space.kernel.utils.cache;

import org.monet.space.kernel.utils.cache.impl.JCSCache;

import java.util.HashMap;

public class CacheFactory {

	private static CacheFactory instance;
	private HashMap<String, Cache<?, ?>> caches = new HashMap<String, Cache<?, ?>>();

	private CacheFactory() {

	}

	public static CacheFactory getInstance() {
		if (instance == null)
			instance = new CacheFactory();
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <K, V> Cache<K, V> get(String name) throws Exception {
		Cache<K, V> cache = (Cache<K, V>) caches.get(name);
		if (cache == null) {
			cache = new JCSCache<K, V>(name);
			caches.put(name, cache);
		}

		return cache;
	}

	public void resetAll() throws Exception {
		for (Cache<?, ?> cache : this.caches.values())
			cache.reset();
	}

}
