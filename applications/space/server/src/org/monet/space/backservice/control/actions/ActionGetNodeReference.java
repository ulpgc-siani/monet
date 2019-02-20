package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Reference;

public class ActionGetNodeReference extends Action {

	public ActionGetNodeReference() {
	}

	@Override
	public String execute() {
		String nodeId = (String) this.parameters.get(Parameter.ID);
		String name = (String) this.parameters.get(Parameter.NAME);
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

		Node node = nodeLayer.loadNode(nodeId);
		Reference reference = node.getReference(LibraryEncoding.decode(name));

		return reference.serializeToXML();
	}

}
