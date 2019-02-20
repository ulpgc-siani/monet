package org.monet.bpi;

import org.monet.metamodel.FieldProperty;

public interface Field<V> {

	public String getCode();

	public String getName();

	public String getLabel();

	public <T extends FieldProperty> T getDefinition();

	public void clear();

	public V get();

	public void set(V value);

}