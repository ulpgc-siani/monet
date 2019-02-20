package org.monet.space.kernel.utils.cache.impl;

import org.apache.jcs.JCS;
import org.apache.jcs.engine.CacheElement;
import org.apache.jcs.engine.behavior.IElementAttributes;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.jcs.engine.control.event.behavior.IElementEvent;
import org.apache.jcs.engine.control.event.behavior.IElementEventHandler;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.utils.StreamHelper;
import org.monet.space.kernel.utils.cache.Cache;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Properties;

public class JCSCache<K, V> implements Cache<K, V> {

	private JCS jcsCache;
	private static CompositeCacheManager ccm;
	private ArrayList<CacheEventListener<K, V>> listeners = new ArrayList<Cache.CacheEventListener<K, V>>();

	public JCSCache(String name) throws Exception {
		if (ccm == null) {
			ccm = CompositeCacheManager.getUnconfiguredInstance();
			Properties props = new Properties();
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(Configuration.getInstance().getConfigurationDir() + "/cache.ccf");
				props.load(inputStream);
			} finally {
				StreamHelper.close(inputStream);
			}
			ccm.configure(props);
		}

		this.jcsCache = JCS.getInstance(name);
		IElementAttributes attributes = this.jcsCache.getDefaultElementAttributes();
		attributes.addElementEventHandler(new IElementEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void handleElementEvent(IElementEvent event) {
				CacheElement element = (CacheElement) ((EventObject) event).getSource();
				for (CacheEventListener<K, V> listener : listeners)
					listener.onCacheEvent(JCSCache.this, (V) element.val, CacheEventType.REMOVED);
			}
		});
		this.jcsCache.setDefaultElementAttributes(attributes);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) {
		return (V) this.jcsCache.get(key);
	}

	@Override
	public void put(K key, V value) throws Exception {
		Object oldValue = jcsCache.get(key);
		if (oldValue != null) {
			this.jcsCache.remove(key);
		}
		this.jcsCache.put(key, value);
		for (CacheEventListener<K, V> listener : listeners)
			listener.onCacheEvent(this, value, CacheEventType.ADDED);
	}

	@Override
	public void remove(K key) throws Exception {
		Object oldValue = jcsCache.get(key);
		if (oldValue != null)
			this.jcsCache.remove(key);
	}

	@Override
	public void addListener(CacheEventListener<K, V> listener) {
		this.listeners.add(listener);
	}

	@Override
	public void reset() throws Exception {
		this.jcsCache.clear();
	}

}
