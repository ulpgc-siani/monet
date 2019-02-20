package org.monet.bpi;

import java.util.List;

public interface NodeForm extends Node, Georeferenced {

	public void reset();

	public List<Field<?>> getFields();

}
