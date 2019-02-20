package org.monet.bpi;

import java.util.Map;

public interface FieldText extends Field<String> {

	String getGroup(String name);
	String getGroup(int index);
	Map<String, String> getGroups();

}