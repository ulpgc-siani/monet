package client.services.http.builders;

import client.core.model.List;
import client.core.system.Node;
import client.services.http.HttpInstance;
import com.google.gwt.query.client.js.JsMap;

import static client.core.model.Node.Type;

public class NodeBuilder<T extends client.core.model.Node> extends EntityBuilder<Node, T, List<T>> implements Builder<T, List<T>> {

	@Override
	public void initialize(T object, HttpInstance instance) {
		super.initialize(object, instance);

		Node node = (Node)object;
		node.setIsComponent(instance.getBoolean("isComponent"));
		node.setLocked(instance.getBoolean("locked"));
		node.setType(Type.fromString(instance.getString("type")));
		node.setEntityFactory(this);

		initializeNotes(node, instance.<String>getMap("notes"));
	}

	private void initializeNotes(Node node, JsMap<String, String> map) {
		for (String note : map.keys())
			node.addNote(note, map.get(note));
	}

}
