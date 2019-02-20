package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Node;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class ActionGetNodeNotes extends Action {

	public ActionGetNodeNotes() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		Node node;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		node = nodeLayer.loadNode(id);
		Map<String, String> notes = node.getNotes();
		StringBuffer result = new StringBuffer();
		for (String note : notes.keySet()) {
			if (result.length() != 0)
				result.append(PARAMETER_SEPARATOR);

			try {
				result.append(URLEncoder.encode(note, "UTF-8") + "=" + URLEncoder.encode(notes.get(note), "UTF-8"));
			} catch (UnsupportedEncodingException exception) {
				AgentLogger.getInstance().error(exception);
			}
		}

		return result.toString();
	}

}
