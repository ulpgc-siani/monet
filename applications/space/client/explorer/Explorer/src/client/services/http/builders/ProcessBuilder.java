package client.services.http.builders;

import client.core.system.Node;
import client.core.system.Process;
import client.services.http.HttpInstance;
import com.google.gwt.query.client.js.JsMap;

public class ProcessBuilder<T extends client.core.model.Process> extends TaskBuilder<T> {

	@Override
	public void initialize(T object, HttpInstance instance) {
		super.initialize(object, instance);

		Process process = (Process)object;
		initializeShortcuts(process, instance);
	}

	private void initializeShortcuts(client.core.system.Process process, HttpInstance instance) {
		JsMap<String, HttpInstance> map = instance.getMap("shortcuts");

		for (String shortcut : map.keys())
			process.addShortcut(shortcut, (Node)new NodeBuilder<>().build(instance.getMapInstance("shortcuts", shortcut)));
	}

}
