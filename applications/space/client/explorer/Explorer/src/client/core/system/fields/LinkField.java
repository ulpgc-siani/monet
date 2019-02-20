package client.core.system.fields;

import client.core.model.Filter;
import client.core.model.List;
import client.core.model.Order;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.core.model.Index;
import client.core.model.types.Link;

public class LinkField extends Field<LinkFieldDefinition, Link> implements client.core.model.fields.LinkField {
	private Link value;
	private Index index;

	public LinkField() {
		super(Type.LINK);
	}

	public LinkField(String code, String label) {
		super(code, label, Type.LINK);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.LinkField.CLASS_NAME;
	}

	@Override
	public Link getValue() {
		return this.value;
	}

	@Override
	public String getValueAsString() {
		return getValue().getId();
	}

	@Override
	public void setValue(Link value) {
		this.value = value;
	}

	@Override
	public final boolean isMultiple() {
		return false;
	}

	@Override
	public final boolean isNullOrEmpty() {
		Link value = this.getValue();
		return value == null || value.getId() == null || value.getId().isEmpty();
	}

	@Override
    public Index getIndex() {
        return index;
    }

	@Override
	public void setIndex(Index index) {
		this.index = index;
	}

	@Override
	public List<Filter> getFilters(client.core.model.Key view) {
		return null;
	}

	@Override
	public List<Order> getOrders(client.core.model.Key view) {
		return null;
	}
}
