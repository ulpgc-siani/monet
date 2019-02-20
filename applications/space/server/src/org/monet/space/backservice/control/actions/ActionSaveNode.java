package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.NodeDefinitionChangesResolver;

public class ActionSaveNode extends Action {

	public ActionSaveNode() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String data = (String) this.parameters.get(Parameter.DATA);
		org.monet.space.kernel.model.Node monetNode;

		if (id == null || data == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

        data = LibraryString.cleanNonAsciiCharacters(LibraryEncoding.decode(data));

		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		monetNode = nodeLayer.loadNode(id);
		monetNode.deserializeFromXML(data, new NodeDefinitionChangesResolver(monetNode.getDefinition()));

		nodeLayer.saveNode(monetNode);

		return MessageCode.NODE_SAVED;
	}

}
