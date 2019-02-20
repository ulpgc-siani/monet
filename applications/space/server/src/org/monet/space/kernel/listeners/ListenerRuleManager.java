package org.monet.space.kernel.listeners;

import org.monet.space.kernel.agents.AgentRuleManager;
import org.monet.space.kernel.constants.ModelProperty;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.Node;

public class ListenerRuleManager extends Listener {

	@Override
	public void nodeModified(MonetEvent event) {
		Node node = (Node) event.getSender();
		String property = event.getProperty();
		if (property.equals(ModelProperty.PARENT) ||
			property.equals(ModelProperty.NODELIST) ||
			property.equals(ModelProperty.ATTRIBUTELIST)) {
			AgentRuleManager.getInstance().onNodeChanged(node);
		}
	}

}
