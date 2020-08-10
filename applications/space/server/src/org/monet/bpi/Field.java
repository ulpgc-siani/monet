package org.monet.bpi;

import org.monet.metamodel.FieldProperty;

public interface Field<V> {
	String getCode();
	FieldProperty getDefinition();
	String getName();
	String getLabel();
	void clear();
	V get();
	void set(V value);
	boolean isValid();
}