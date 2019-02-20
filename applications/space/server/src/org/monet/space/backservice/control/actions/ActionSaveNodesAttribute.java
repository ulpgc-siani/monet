package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.library.LibraryString;

public class ActionSaveNodesAttribute extends Action {

	public ActionSaveNodesAttribute() {
	}

	@Override
	public String execute() {
		String nodes = (String) this.parameters.get(Parameter.NODES);
		String data = (String) this.parameters.get(Parameter.DATA);
		org.monet.space.kernel.model.Attribute monetAttribute = new org.monet.space.kernel.model.Attribute();
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

		if (nodes == null || data == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

        data = LibraryString.cleanNonAsciiCharacters(LibraryEncoding.decode(data));
		nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		//data = nodeProcessor.processNode(data);
		monetAttribute.deserializeFromXML(data, null);
		nodeLayer.saveNodesAttribute(nodes.split(","), monetAttribute);

		return MessageCode.NODES_ATTRIBUTE_SAVED;
	}

}
