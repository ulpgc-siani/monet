package client.core.model;

import cosmos.gwt.model.HasValue;

import static client.core.model.Instance.ClassName;

public interface Filter {

	ClassName CLASS_NAME = new ClassName("Filter");

	String getName();
	String getLabel();
	List<Option> getOptions();
	void setOptions(List<Option> options);
	ClassName getInstanceClass();
	boolean equals(Object object);

	interface Option extends HasValue {
		ClassName CLASS_NAME = new ClassName("Filter.Option");
	}
}