package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Node;

public class ActionDeleteNodeNote extends Action {

	public ActionDeleteNodeNote() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String name = (String) this.parameters.get(Parameter.NAME);
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		Node node;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		node = nodeLayer.loadNode(id);
		node.deleteNote(LibraryEncoding.decode(name));
		nodeLayer.saveNodeNotes(node);

		return MessageCode.NODE_NOTE_DELETED;
	}

}
