package org.monet.space.explorer.control.dialogs;

import org.monet.metamodel.IndexDefinition;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.control.dialogs.deserializers.FilterDeserializer;
import org.monet.space.explorer.control.dialogs.deserializers.OrderDeserializer;
import org.monet.space.explorer.model.Filter;
import org.monet.space.explorer.model.Order;

import java.util.List;

public class LoadIndexEntriesDialog extends LoadIndexDialog {

	public IndexDefinition getDefinition() {
		return dictionary.getIndexDefinition(getEntityId());
	}

	public List<Filter> getFilters() {
		return new FilterDeserializer(createDeserializerHelper()).deserializeList(getString(Parameter.FILTERS));
	}

	public List<Order> getOrders() {
		return new OrderDeserializer(createDeserializerHelper()).deserializeList(getString(Parameter.ORDERS));
	}

	public String getCondition() {
		return getString(Parameter.CONDITION);
	}

	public int getStart() {
		return getInt(Parameter.START);
	}

	public int getLimit() {
		return getInt(Parameter.LIMIT);
	}

}
