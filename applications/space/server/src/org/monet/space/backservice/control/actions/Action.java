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

package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.ApplicationBackService;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public abstract class Action {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HashMap<String, Object> parameters;
	protected AgentSession agentSession;
	protected AgentLogger agentException;
	protected ActionsFactory actionsFactory;
	protected String sender;
	protected String idSession;
	protected String codeLanguage;

	protected final String PARAMETER_SEPARATOR = "#__#";

	protected final Dictionary dictionary = Dictionary.getInstance();

	public Action() {
		this.agentException = AgentLogger.getInstance();
		this.actionsFactory = ActionsFactory.getInstance();
	}

	protected Boolean initLanguage() {
		this.codeLanguage = null;
		if (this.codeLanguage == null) this.codeLanguage = this.request.getLocale().getLanguage();
		return true;
	}

	public void setSender(User senderUser) {
		this.sender = senderUser.getInfo().getFullname();
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
		this.idSession = request.getSession().getId();
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setParameters(HashMap<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Boolean initialize() {
		this.response.setContentType("text/html;charset=UTF-8");
		this.initLanguage();
		return true;
	}

	protected FederationLayer.Configuration createConfiguration() {
		return new FederationLayer.Configuration() {
			@Override
			public String getSessionId() {
				return request.getSession().getId();
			}

			@Override
			public String getCallbackUrl() {
				return ApplicationBackService.getCallbackUrl();
			}

			@Override
			public String getLogoUrl() {
				return null;
			}

			@Override
			public HttpServletRequest getRequest() {
				return request;
			}
		};
	}

	public abstract String execute();

}