package client.presenters.displays;

import client.core.model.Field;
import client.core.model.List;

public interface IsMultiple<FieldType extends Field, Value> {
	FieldType get(int index);
	Value getValue(int index);
	List<FieldType> getAll();
	Value[] getAllValues();
	void add(FieldType value);
	void add(int index, FieldType value);
	void delete(int index);
	void delete(FieldType field);
	void clear();
	int size();
}
