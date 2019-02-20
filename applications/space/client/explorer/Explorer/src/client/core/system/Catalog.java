package client.core.system;

import client.core.model.*;
import client.core.model.Filter;
import client.core.model.Order;
import client.core.model.definition.entity.CatalogDefinition;
import client.core.model.definition.views.NodeViewDefinition;
import client.core.model.definition.views.SetViewDefinition;

public class Catalog<Definition extends CatalogDefinition> extends Set<Definition> implements client.core.model.Catalog<Definition> {

	public Catalog() {
	}

	public Catalog(String id, String label, boolean isComponent) {
		super(id, label, isComponent, Type.CATALOG);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.Catalog.CLASS_NAME;
	}

	@Override
	public List<Filter> getFilters(client.core.model.Key view) {
		return loadFilters(view.getCode());
	}

	@Override
	public List<Order> getOrders(client.core.model.Key view) {
		return loadOrders(view.getCode());
	}

	@Override
	protected NodeView loadView(NodeViewDefinition viewDefinition) {
		CatalogView view = new CatalogView(new Key(viewDefinition.getCode(), viewDefinition.getName()), viewDefinition.getLabel(), viewDefinition.isDefault(), this);
		view.setDefinition((SetViewDefinition)viewDefinition);
		return view;
	}

}
