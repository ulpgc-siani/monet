package client.core.system.fields;

import client.core.model.Entity;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.system.MonetList;

public abstract class MultipleField<FieldType extends client.core.model.Field<Definition, Value>, Definition extends FieldDefinition, Value> extends Field<Definition, Value> implements client.core.model.MultipleField<FieldType, Definition, Value> {
	protected client.core.model.factory.EntityFactory entityFactory;
	protected List<FieldType> fieldList = new MonetList<>();

	public MultipleField(Type type) {
		this(null, null, type, new MonetList<FieldType>());
	}

	public MultipleField(String code, String label, Entity.Type type, List<FieldType> fieldList) {
		super(code, label, type);
		this.fieldList = fieldList;
		propagateOwner();
	}

	public void setEntityFactory(client.core.model.factory.EntityFactory entityFactory) {
		this.entityFactory = entityFactory;
	}

	public void setFields(List<FieldType> fields) {
		this.fieldList = fields;
		propagateOwner();
	}

	@Override
	public final FieldType get(int index) {
		return fieldList.size() < index ? null : fieldList.get(index);
	}

	@Override
	public final Value getValue(int index) {
		FieldType field = get(index);
		return field == null ? null : field.getValue();
	}

	@Override
	public final int getPosition(FieldType field) {
		int pos = 0;

		for (FieldType childField : fieldList) {
			if (childField == field)
				return pos;

			pos++;
		}

		return pos;
	}

	@Override
	public final List<FieldType> getAll() {
		return fieldList;
	}

	@Override
	public final Value[] getAllValues() {
		Object[] values = new Object[fieldList.size()];
		int pos = 0;

		for (FieldType field : fieldList)
			values[pos++] = field.getValue();

		return (Value[])values;
	}

	@Override
	public final void add(FieldType field) {
		field.setOwner(this);
		fieldList.add(field);
	}

	@Override
	public final void add(int index, FieldType field) {
		field.setOwner(this);
		fieldList.add(index, field);
	}

	@Override
	public final FieldType createField(Value newValue) {
		return setupField(newValue, entityFactory.<FieldType>createFieldByClassName(getCode(), getLabel(), getClassNameOfValue()));
	}

	@Override
	public final void replace(int index, Value newValue) {
		get(index).setValue(newValue);
	}

	@Override
	public final void delete(int index) {
		fieldList.remove(index);
	}

	@Override
	public final void delete(FieldType field) {
		fieldList.remove(field);
	}

	@Override
	public final void clear() {
		fieldList.clear();
	}

	@Override
	public int size() {
		return fieldList.size();
	}

	@Override
	public final boolean isNullOrEmpty() {
		return getAllValues().length == 0;
	}

    @Override
    public final boolean isMultiple() {
        return true;
    }

	private void propagateOwner() {
		for (FieldType field : fieldList)
			field.setOwner(this);
	}

	private FieldType setupField(Value newValue, FieldType field) {
		field.setOwner(this);
		field.setDefinition(getDefinition());
		if (newValue != null)
			field.setValue(newValue);
		return field;
	}
}
