package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Node;

public class ActionAddNodeNote extends Action {

	public ActionAddNodeNote() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String name = (String) this.parameters.get(Parameter.NAME);
		String value = (String) this.parameters.get(Parameter.VALUE);
		Node node;

		if (id == null || name == null)
			return ErrorCode.WRONG_PARAMETERS;

		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		node = nodeLayer.loadNode(id);
		node.addNote(LibraryEncoding.decode(name), LibraryEncoding.decode(value));
		nodeLayer.saveNodeNotes(node);

		return MessageCode.NODE_NOTE_ADDED;
	}

}
