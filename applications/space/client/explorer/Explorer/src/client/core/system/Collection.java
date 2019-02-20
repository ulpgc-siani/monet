package client.core.system;

import client.core.model.*;
import client.core.model.definition.entity.CollectionDefinition;
import client.core.model.definition.views.NodeViewDefinition;
import client.core.model.definition.views.SetViewDefinition;

import java.util.HashMap;
import java.util.Map;

public class Collection<Definition extends CollectionDefinition> extends Set<Definition> implements client.core.model.Collection<Definition> {
	private Map<String, List<client.core.model.Filter>> filters = new HashMap<>();
	private Map<String, List<client.core.model.Order>> orders = new HashMap<>();

	public Collection() {
		super();
	}

	public Collection(String id, String label, boolean isComponent) {
		super(id, label, isComponent, Type.COLLECTION);
	}

	@Override
	public ClassName getClassName() {
		return client.core.model.Collection.CLASS_NAME;
	}

	@Override
	public List<client.core.model.Filter> getFilters(client.core.model.Key view) {
		String viewCode = view.getCode();

		if (filters.containsKey(viewCode))
			return filters.get(viewCode);

		filters.put(viewCode, loadFilters(viewCode));

		return filters.get(viewCode);
	}

	public void setViewFilters(String view, List<client.core.model.Filter> filterList) {
		filters.put(view, filterList);
	}

	@Override
	public List<client.core.model.Order> getOrders(client.core.model.Key view) {
		String viewCode = view.getCode();

		if (orders.containsKey(viewCode))
			return orders.get(viewCode);

		orders.put(viewCode, loadOrders(viewCode));

		return orders.get(viewCode);
	}

	public void setViewOrders(String view, List<client.core.model.Order> orderList) {
		orders.put(view, orderList);
	}

	@Override
	protected NodeView loadView(NodeViewDefinition viewDefinition) {
		CollectionView view = new CollectionView(new Key(viewDefinition.getCode(), viewDefinition.getName()), viewDefinition.getLabel(), viewDefinition.isDefault(), this);
		view.setDefinition((SetViewDefinition) viewDefinition);
		return view;
	}

}
