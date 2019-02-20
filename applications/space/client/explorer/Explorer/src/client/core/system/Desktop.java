package client.core.system;

import client.core.model.*;
import client.core.model.definition.entity.DesktopDefinition;
import client.core.model.definition.views.DesktopViewDefinition;
import client.core.model.definition.views.NodeViewDefinition;

import java.util.HashMap;
import java.util.Map;

public class Desktop<Definition extends DesktopDefinition> extends Node<Definition> implements client.core.model.Desktop<Definition> {
	private Map<String, client.core.model.Node> singletonMap = new HashMap<>();

	public Desktop() {
	}

	@Override
	protected NodeView loadView(NodeViewDefinition viewDefinition) {
		DesktopView view = new DesktopView(new Key(viewDefinition.getCode(), viewDefinition.getName()), viewDefinition.getLabel(), viewDefinition.isDefault(), this);
		view.setDefinition((DesktopViewDefinition)viewDefinition);
		return view;
	}

	public Desktop(String id, String label) {
		super(id, label, false, Type.DESKTOP);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.Desktop.CLASS_NAME;
	}

	@Override
	public int getSingletonsCount() {
		return singletonMap.size()/2;
	}

	@Override
	public client.core.model.Node getSingleton(String key) {
		return singletonMap.get(key);
	}

	public void setSingletons(List<client.core.model.Node> singletonList) {
		singletonMap.clear();
		for (client.core.model.Node singleton : singletonList) {
			client.core.model.Key key = singleton.getKey();
			singletonMap.put(key.getCode(), singleton);
			singletonMap.put(key.getName(), singleton);
		}
	}

	@Override
	public boolean isEnvironment() {
		return true;
	}
}
