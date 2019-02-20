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

import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.library.LibraryPDF;
import org.monet.space.kernel.model.DataRequest.GroupBy;
import org.monet.space.kernel.model.DataRequest.SortBy;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskList;
import org.monet.space.kernel.model.TaskSearchRequest;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.office.presentation.user.renders.PrintRender;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ActionPrintTaskList extends Action {
	private NodeLayer nodeLayer;

	private static DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

	public ActionPrintTaskList() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String template = LibraryRequest.getParameter(Parameter.TEMPLATE, this.request);
		String inbox = LibraryRequest.getParameter(Parameter.INBOX, this.request);
		String folder = LibraryRequest.getParameter(Parameter.FOLDER, this.request);
		String condition = LibraryRequest.getParameter(Parameter.CONDITION, this.request);
		String sortsBy = LibraryRequest.getParameter(Parameter.SORTS_BY, this.request);
		String groupsBy = LibraryRequest.getParameter(Parameter.GROUPS_BY, this.request);
		String dateAttribute = LibraryRequest.getParameter(Parameter.DATE, this.request);
		String from = LibraryRequest.getParameter(Parameter.FROM_DATE, this.request);
		String to = LibraryRequest.getParameter(Parameter.TO_DATE, this.request);
		Date fromDate = null, toDate = null;
		TaskSearchRequest searchRequest;
		List<SortBy> sortsByList;
		Map<String, GroupBy> groupsByMap;
		String type, state, role, urgent, owner;
		byte[] output;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		sortsByList = getSortsBy(sortsBy);
		groupsByMap = getGroupsByMap(groupsBy);

		type = groupsByMap.containsKey("type") ? (String) groupsByMap.get("type").value(0) : null;
		state = groupsByMap.containsKey("state") ? (String) groupsByMap.get("state").value(0) : null;
		role = groupsByMap.containsKey("role") ? (String) groupsByMap.get("role").value(0) : null;
		urgent = groupsByMap.containsKey("urgent") ? (String) groupsByMap.get("urgent").value(0) : null;
		owner = groupsByMap.containsKey("owner") ? (String) groupsByMap.get("owner").value(0) : null;

		if (type != null && (type.isEmpty() || type.equals("all"))) type = null;
		if (state != null && (state.isEmpty() || state.equals("all"))) state = null;
		if (folder == null || folder.isEmpty()) folder = Task.Situation.ACTIVE;
		if (role != null && (role.isEmpty() || role.equals("all"))) role = null;
		if (urgent != null && (urgent.isEmpty() || urgent.equals("all"))) urgent = null;
		if (owner != null && (owner.isEmpty() || owner.equals("all"))) owner = null;

		searchRequest = new TaskSearchRequest();
		searchRequest.setCondition(condition);
		searchRequest.setParameters(getRequestParameters());
		searchRequest.addParameter(Task.Parameter.TYPE, type);
		searchRequest.addParameter(Task.Parameter.STATE, state);
		searchRequest.addParameter(Task.Parameter.INBOX, inbox);
		searchRequest.addParameter(Task.Parameter.SITUATION, folder);
		searchRequest.addParameter(Task.Parameter.ROLE, role);
		searchRequest.addParameter(Task.Parameter.URGENT, urgent);
		searchRequest.addParameter(Task.Parameter.OWNER, owner);
		searchRequest.setSortsBy(sortsByList);
		searchRequest.setStartPos(0);
		searchRequest.setLimit(-1);

		try {
			if (from != null) fromDate = dateFormatter.parse(from);
			if (to != null) toDate = dateFormatter.parse(to);
		} catch (ParseException e) {
		}

		PrintRender render = this.rendersFactory.get(new TaskList(), template + ".html?mode=print", this.getRenderLink(), getAccount());
		render.setParameter("request", searchRequest);
		render.setRange(rangeOf(dateAttribute, fromDate, toDate));

		try {

			if (template.equals("pdf"))
				output = LibraryPDF.create(new ByteArrayInputStream(render.getOutput().getBytes("utf8"))).toByteArray();
			else if (template.equals("csv"))
				output = render.getOutput().replaceAll("\\\\n", "\r\n").getBytes("UTF-16LE");
			else
				output = render.getOutput().replaceAll("\\\\n", "\r\n").getBytes("UTF-8");

			this.response.setContentLength(output.length);
			this.response.setContentType(MimeTypes.getInstance().get(template));
			this.response.setHeader("Content-Disposition", "attachment; filename=tasks." + template);
			this.response.getOutputStream().write(output);
			this.response.getOutputStream().flush();

		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		return null; // Avoid controller getting response writer
	}

	private PrintRender.Range rangeOf(final String dateAttribute, final Date fromDate, final Date toDate) {

		if (dateAttribute == null)
			return null;

		return new PrintRender.Range() {
			@Override
			public String attribute() {
				return dateAttribute;
			}

			@Override
			public Date from() {
				return fromDate;
			}

			@Override
			public Date to() {
				return toDate;
			}
		};
	}

}