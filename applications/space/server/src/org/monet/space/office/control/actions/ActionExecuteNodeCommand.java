/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.office.control.actions;

import net.minidev.json.JSONObject;
import org.monet.bpi.types.File;
import org.monet.metamodel.NodeDefinitionBase;
import org.monet.space.analytics.model.Language;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.agents.AgentUserClient;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.model.*;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionExecuteNodeCommand extends Action {
	protected NodeLayer nodeLayer;

	public ActionExecuteNodeCommand() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String id = this.request.getParameter(Parameter.ID);
		String name = this.request.getParameter(Parameter.NAME);

		Node node;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		OperationConfirmation confirmation = new OperationConfirmation();

		node = this.nodeLayer.loadNode(id);
		MonetEvent event = new MonetEvent(MonetEvent.NODE_EXECUTE_COMMAND_CONFIRMATION_WHEN, null, node);
		event.addParameter(MonetEvent.PARAMETER_COMMAND, name);
		event.addParameter(MonetEvent.PARAMETER_COMMAND_CONFIRMATION_REQUIRED, confirmation);
		AgentNotifier.getInstance().notify(event);

		if (confirmation.isRequired())
			return result("message", serializeConfirmation(name, node)).toJSONString();

		try {
			return execute(name, node).toJSONString();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	private JSONObject serializeConfirmation(String operation, Node node) {
		JSONObject result = new JSONObject();
		NodeDefinitionBase.OperationProperty operationDefinition = node.getDefinition().getOperationMap().get(operation);
		NodeDefinitionBase.OperationProperty.ConfirmationProperty confirmationDefinition = operationDefinition.getConfirmation();

		result.put("required", true);

		if (confirmationDefinition != null) {
			Language language = Language.getInstance();
			result.put("title", language.getModelResource(confirmationDefinition.getTitle()));
			result.put("description", language.getModelResource(confirmationDefinition.getDescription()));
		}

		return result;
	}

	protected JSONObject execute(String name, Node node) throws Exception {
		Long threadId = Thread.currentThread().getId();
		AgentUserClient agentUserClient = AgentUserClient.getInstance();
		MonetEvent event = new MonetEvent(MonetEvent.NODE_EXECUTE_COMMAND, null, node);
		event.addParameter(MonetEvent.PARAMETER_COMMAND, name);
		AgentNotifier.getInstance().notify(event);

		ClientOperation operation = agentUserClient.getOperationForUser(threadId);
		File file = agentUserClient.getFileForUser(threadId);
		String message = agentUserClient.getMessageForUser(threadId);

		agentUserClient.clear(threadId);

		if (operation != null)
			return result("operation", operation.toJson());
		else if (file != null) {
			saveFile(file);
			return result("operation", createDownloadFileOperation(file).toJson());
		}
		else {
			if (message != null)
				throw new RuntimeException(ErrorCode.EXECUTE_NODE_COMMAND + Strings.COLON + Strings.SPACE + message);
			else
				return done();
		}
	}

	protected JSONObject done() {
		return result("message", message("success", "done"));
	}

	protected JSONObject message(String code, String content) {
		JSONObject message = new JSONObject();
		message.put("code", code);
		message.put("content", content);
		return message;
	}

	protected JSONObject result(String type, JSONObject data) {
		JSONObject result = new JSONObject();
		result.put("type", type);
		if (data != null) result.put("data", data);
		return result;
	}

	public void saveFile(File file) {
		Session session = getSession();
		session.setVariable(file.getFilename(), file);
	}

	private ClientOperation createDownloadFileOperation(File file) {
		JSONObject parameters = new JSONObject();
		parameters.put("Name", file.getFilename());
		return new ClientOperation(Actions.DOWNLOAD_NODE_COMMAND_FILE, parameters);
	}

	protected boolean operationDone(JSONObject result) {
		String type = (String) result.get("type");

		if (!"message".equals(type))
			return true;

		JSONObject data = (JSONObject) result.get("data");

		return data.get("content").equals("done");
	}

}