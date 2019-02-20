package org.monet.bpi;

import java.util.Map;

public interface FieldText extends Field<String> {

	public String getGroup(String name);

	public String getGroup(int index);

	public Map<String, String> getGroups();

}