package org.monet.bpi;

import java.util.List;

public interface NodeForm extends Node, Georeferenced {

	void reset();

	List<Field<?>> getFields();

	<T, F extends Field<V>, V> T getField(String name);

}
