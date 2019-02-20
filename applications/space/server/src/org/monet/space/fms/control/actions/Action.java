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

package org.monet.space.fms.control.actions;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.monet.space.fms.ApplicationFms;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Action {
	protected String codeLanguage;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ComponentFederation oComponentAccountsManager;
	protected AgentSession oAgentSession;
	protected AgentLogger oAgentException;
	protected ActionsFactory oActionsFactory;
	protected String idSession;

	public Action() {
		this.codeLanguage = null;
		this.request = null;
		this.response = null;
		this.oComponentAccountsManager = ComponentFederation.getInstance();
		this.oAgentSession = AgentSession.getInstance();
		this.oAgentException = AgentLogger.getInstance();
		this.oActionsFactory = ActionsFactory.getInstance();
	}

	protected Boolean initLanguage() {
		Session oSession = this.oAgentSession.get(idSession);

		this.codeLanguage = null;
		if (oSession != null) this.codeLanguage = this.getFederationLayer().getUserLanguage();
		if (this.codeLanguage == null) this.codeLanguage = this.request.getHeader("Accept-Language").substring(0, 2);

		return true;
	}

	protected FederationLayer getFederationLayer() {
		return ComponentFederation.getInstance().getLayer(new FederationLayer.Configuration() {
			@Override
			public String getSessionId() {
				return request.getSession().getId();
			}

			@Override
			public String getCallbackUrl() {
				return ApplicationFms.getCallbackUrl();
			}

			@Override
			public String getLogoUrl() {
				return null;
			}

			@Override
			public HttpServletRequest getRequest() {
				return request;
			}
		});
	}

	public Account getAccount() {
		return this.getFederationLayer().loadAccount();
	}

	public Boolean setRequest(HttpServletRequest oRequest) {
		this.request = oRequest;
		this.idSession = oRequest.getSession().getId();
		return true;
	}

	public Boolean setResponse(HttpServletResponse oResponse) {
		this.response = oResponse;
		return true;
	}

	public Boolean initialize() {

		this.response.setContentType("text/html;charset=UTF-8");
		this.initLanguage();

		return true;
	}

	public abstract String execute();

	protected HashMap<String, List<FileItem>> getPostParameterMap(HttpServletRequest request) throws FileUploadException {
		HashMap<String, List<FileItem>> result = new HashMap<>();
		ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
		List<FileItem> fileList = uploadHandler.parseRequest(request);

		for (FileItem fileItem : fileList) {
			String fileName = fileItem.getFieldName();

			if (! result.containsKey(fileItem.getFieldName()))
				result.put(fileName, new ArrayList<FileItem>());

			result.get(fileName).add(fileItem);
		}
		return result;
	}

}