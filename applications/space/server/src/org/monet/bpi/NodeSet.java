package org.monet.bpi;

import java.util.Map;

public interface NodeSet extends Node {

	public long getCount(Expression where);

	public Map<String, String> getParameters();

	public void addParameter(String name, String value);

	public void deleteParameter(String name);

}
