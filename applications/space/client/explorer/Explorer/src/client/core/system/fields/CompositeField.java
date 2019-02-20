package client.core.system.fields;

import client.core.model.Entity;
import client.core.model.definition.entity.field.CompositeFieldDefinition;
import client.core.model.types.Composite;

public class CompositeField extends Field<CompositeFieldDefinition, Composite> implements client.core.model.fields.CompositeField {
	private client.core.model.types.Composite value;
	private boolean conditioned;

	public CompositeField() {
		this(null, null);
	}

	public CompositeField(String code, String label) {
		super(code, label, Type.COMPOSITE);
		this.value = new client.core.system.types.Composite();
		this.conditioned = false;
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.CompositeField.CLASS_NAME;
	}

	@Override
	public client.core.model.types.Composite getValue() {
		return this.value;
	}

	@Override
	public String getValueAsString() {
		return "";
	}

	@Override
	public boolean getConditioned() {
		return conditioned;
	}

	@Override
	public void setValue(client.core.model.types.Composite value) {
		this.value = value;
		propagateOwner();
		if (getDefinition() != null) setDefinition(getDefinition());
	}

	public void setConditioned(boolean conditioned) {
		this.conditioned = conditioned;
	}

	@Override
	public final boolean isMultiple() {
		return false;
	}

	@Override
	public final boolean isNullOrEmpty() {
		return getValue() == null || getValue().size() == 0;
	}

	@Override
	public void setOwner(Entity owner) {
		super.setOwner(owner);
		propagateOwner();
	}

	private void propagateOwner() {
		for (client.core.model.Field field : getValue())
			field.setOwner(this);
	}

}
