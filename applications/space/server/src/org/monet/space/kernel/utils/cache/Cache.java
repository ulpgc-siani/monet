package org.monet.space.kernel.utils.cache;

public interface Cache<K, V> {

	public V get(K key);

	public void put(K key, V value) throws Exception;

	public void remove(K key) throws Exception;

	public void addListener(CacheEventListener<K, V> listener);

	public void reset() throws Exception;

	public enum CacheEventType {ADDED, REMOVED}

	public interface CacheEventListener<K, V> {
		public void onCacheEvent(Cache<K, V> sender, V value, CacheEventType type);
	}

}
