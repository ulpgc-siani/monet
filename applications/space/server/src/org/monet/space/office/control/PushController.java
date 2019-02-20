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

package org.monet.space.office.control;

import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Session;
import org.monet.space.kernel.model.User;
import org.monet.space.office.core.constants.ErrorCode;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "office.push", urlPatterns = {"/servlet/office.push"}, asyncSupported = true)
public class PushController extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	private void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Account account;
		User user;

		if (!Configuration.getInstance().isPushEnable()) {
			response.getWriter().println("Push Technology not enabled");
			return;
		}

		String sessionId = request.getSession().getId();

		Session session = AgentSession.getInstance().get(sessionId);
		if (session == null) {
			response.getWriter().println(ErrorCode.USER_NOT_LOGGED);
			return;
		}

		account = session.getAccount();
		user = account.getUser();

		if (user == null || user.getName().equals(Strings.EMPTY)) {
			response.getWriter().println(ErrorCode.USER_NOT_LOGGED);
			return;
		}

		String clientId = request.getParameter("i");
		String compatibilityMode = request.getParameter("c");
		boolean isCompatibilityMode = compatibilityMode != null && !compatibilityMode.isEmpty();

		AsyncContext asyncContext = request.startAsync();
		asyncContext.setTimeout(60000);

		response.setContentType("application/octet-stream");
		response.setHeader("Transfer-Encoding", "chunked");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-type", "text/xml;charset=utf-8");

		AgentPushService.getInstance().addClient(user, sessionId, clientId, asyncContext, isCompatibilityMode);

		if (isCompatibilityMode) {
			PrintWriter writer = asyncContext.getResponse().getWriter();
			writer.println("<html>");
			writer.println("<head>");
			writer.println("<meta HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\"/>");
			writer.println("</head>");
			writer.println("<body>");
			writer.println("<script>parent.openChannel()</script>");
			writer.println("<p>                                                                                                                                                                                                   </p>");
		}

		asyncContext.getResponse().flushBuffer();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		this.doRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		this.doRequest(request, response);
	}

}