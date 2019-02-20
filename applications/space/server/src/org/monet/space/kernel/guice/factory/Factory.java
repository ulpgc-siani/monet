package org.monet.space.kernel.guice.factory;

public interface Factory<T, V> {

	public V create(T key);

}
