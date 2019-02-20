package client.core.system;

import client.core.model.List;
import client.core.model.definition.Ref;
import client.core.model.definition.entity.ContainerDefinition;
import client.core.model.definition.views.ContainerViewDefinition;
import client.core.model.definition.views.NodeViewDefinition;

import java.util.HashMap;
import java.util.Map;

public class Container<Definition extends ContainerDefinition> extends Node<Definition> implements client.core.model.Container<Definition> {
	private Map<String, client.core.model.Node> childMap = new HashMap<>();
	private List<client.core.model.Node> childList = new MonetList<>();

	public Container() {
	}

	@Override
	protected NodeView loadView(NodeViewDefinition viewDefinition) {
		ContainerViewDefinition containerViewDefinition = (ContainerViewDefinition)viewDefinition;
		Ref component = containerViewDefinition.getShow().getComponent().get(0);
		Node scope = getChild(component.getDefinition());
		NodeView hostView = null;

		if (scope.isForm())
			hostView = new client.core.system.ProxyFormView(new Key(null, component.getValue()), viewDefinition.getLabel(), viewDefinition.isDefault(), scope);
		else if (scope.isCatalog())
			hostView = new client.core.system.ProxyCatalogView(new Key(null, component.getValue()), viewDefinition.getLabel(), viewDefinition.isDefault(), scope);
		else if (scope.isCollection())
			hostView = new client.core.system.ProxyCollectionView(new Key(null, component.getValue()), viewDefinition.getLabel(), viewDefinition.isDefault(), scope);
		else if (scope.isDocument())
			hostView = new client.core.system.ProxyDocumentView(new Key(null, component.getValue()), viewDefinition.getLabel(), viewDefinition.isDefault(), scope);

		ContainerView view = new ContainerView(new Key(viewDefinition.getCode(), viewDefinition.getName()), viewDefinition.getLabel(), viewDefinition.isDefault(), this, hostView);
		view.setDefinition(containerViewDefinition);

		return view;
	}

	public Container(String id, String label) {
		super(id, label, false, Type.CONTAINER);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.Container.CLASS_NAME;
	}

	@Override
	public int getChildrenCount() {
		return childMap.size()/3;
	}

	@Override
	public List<client.core.model.Node> getChildren() {
		return childList;
	}

	@Override
	public <T extends client.core.model.Node> T getChild(String key) {
		return (T) childMap.get(key);
	}

	@Override
	public void addChild(client.core.model.Node node) {
		client.core.model.Key key = node.getKey();

		childMap.put(node.getId(), node);
		childMap.put(key.getName(), node);
		childMap.put(key.getCode(), node);

		childList.add(node);
	}

	public void setChildren(List<client.core.model.Node> children) {
		childMap.clear();
		childList.clear();
		for (client.core.model.Node child : children)
			addChild(child);
	}

	@Override
	public boolean isEnvironment() {
		return getDefinition().isEnvironment();
	}
}
