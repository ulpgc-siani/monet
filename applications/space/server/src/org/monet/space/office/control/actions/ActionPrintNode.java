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
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.library.LibraryPDF;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeDataRequest;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.presentation.user.renders.PrintRender;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ActionPrintNode extends PrintAction {
	private NodeLayer nodeLayer;

	private static DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

	public ActionPrintNode() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		final String idNode = LibraryRequest.getParameter(Parameter.ID, this.request);
		final String template = LibraryRequest.getParameter(Parameter.TEMPLATE, this.request);
		String codeView = LibraryRequest.getParameter(Parameter.VIEW, this.request);
		String from = LibraryRequest.getParameter(Parameter.FROM_DATE, this.request);
		String to = LibraryRequest.getParameter(Parameter.TO_DATE, this.request);
		final Node node;
		final NodeDataRequest dataRequest = new NodeDataRequest();
		HashMap<String, String> parameters;
		Date fromDate = null, toDate = null;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (idNode == null || template == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.EXPORT_NODE);

		node = this.nodeLayer.loadNode(idNode);

		if (!this.componentSecurity.canRead(node, this.getAccount()))
			throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, idNode);

		parameters = this.getRequestParameters();

		try {
			if (from != null) fromDate = dateFormatter.parse(from);
			if (to != null) toDate = dateFormatter.parse(to);

			dataRequest.setCondition(new String(parameters.get(Parameter.CONDITION)));
		} catch (NullPointerException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		} catch (ClassCastException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		} catch (ParseException e) {
		}

		dataRequest.setCodeDomainNode(node.getDefinition().getCode());
		dataRequest.setCodeView(codeView);
		dataRequest.setSortsBy(getSortsBy(parameters.get(Parameter.SORTS_BY)));
		dataRequest.setGroupsBy(getGroupsBy(parameters.get(Parameter.GROUPS_BY)));
		dataRequest.setParameters(parameters);

		final Date finalFromDate = fromDate;
		final Date finalToDate = toDate;
		final Account account = getAccount();
		final String dateAttribute = LibraryRequest.getParameter(Parameter.DATE, this.request);
		final String serverName = request.getServerName();
		final String contextPath = request.getContextPath();
		final int serverPort = request.getServerPort();

		Timer timer = new Timer("Monet-Print-Node-" + idNode);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				AgentLogger.getInstance().info("Print node %s of user %s", idNode, account.getUser().getName());
				long threadId = Thread.currentThread().getId();
				Context.getInstance().setUserServerConfig(threadId, serverName, contextPath, serverPort);
				generateDocument(node, dataRequest, finalFromDate, finalToDate, dateAttribute, template, account);
				AgentPushService.getInstance().push(userId(account), Actions.DOWNLOAD_PRINTED_NODE, jsonData());
				Context.getInstance().clear(threadId);
			}

			private JSONObject jsonData() {
				JSONObject result = new JSONObject();
				result.put("Id", idNode);
				result.put("Template", template);
				return result;
			}
		}, 1);

		return null; // Avoid controller getting response writer
	}

	private void generateDocument(Node node, NodeDataRequest dataRequest, Date fromDate, Date toDate, String dateAttribute, String template, Account account) {
		byte[] output;

		PrintRender render = this.rendersFactory.get(node, template + ".html?mode=print", this.getRenderLink(), account);
		render.setParameter("dataRequest", dataRequest);
		render.setRange(rangeOf(dateAttribute, fromDate, toDate));

		try {
			if (template.equals("pdf"))
				output = LibraryPDF.create(new ByteArrayInputStream(render.getOutput().getBytes("utf8"))).toByteArray();
			else if (template.equals("csv"))
				output = render.getOutput().replaceAll("\\\\n", "\r\n").getBytes("UTF-16LE");
			else
				output = render.getOutput().replaceAll("\\\\n", "\r\n").getBytes("UTF-8");

			saveDocument(account, node.getId(), output);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

	}

}