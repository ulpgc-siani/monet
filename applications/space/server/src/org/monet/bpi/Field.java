package org.monet.bpi;

public interface Field<V> {
	String getCode();
	String getName();
	String getLabel();
	void clear();
	V get();
	void set(V value);
	boolean isValid();
}