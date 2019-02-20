package client.core.model;

import client.core.model.definition.entity.FieldDefinition;
import client.presenters.displays.IsMultiple;

public interface MultipleField<FieldType extends Field<Definition, Value>, Definition extends FieldDefinition, Value> extends Field<Definition, Value>, IsMultiple<FieldType, Value> {

	FieldType get(int index);
	ClassName getClassNameOfValue();
	Value getValue(int index);
	int getPosition(FieldType field);
	List<FieldType> getAll();
	Value[] getAllValues();
	void add(FieldType field);
	void add(int index, FieldType field);
	FieldType createField(Value newValue);
	void replace(int index, Value newValue);
	void delete(int index);
	void clear();
	int size();
	boolean isNullOrEmpty();
    boolean isMultiple();
}
