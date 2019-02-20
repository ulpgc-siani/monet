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

public class ActionGetNodeFlags extends Action {

	public ActionGetNodeFlags() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		Node node;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		node = nodeLayer.loadNode(id);
		Map<String, String> flags = node.getFlags();
		StringBuffer result = new StringBuffer();
		for (String flag : flags.keySet()) {
			if (result.length() != 0)
				result.append(PARAMETER_SEPARATOR);

			try {
				result.append(flag + "=" + URLEncoder.encode(flags.get(flag), "UTF-8"));
			} catch (UnsupportedEncodingException exception) {
				AgentLogger.getInstance().error(exception);
			}
		}

		return result.toString();
	}

}
