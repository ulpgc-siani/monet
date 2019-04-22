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
import org.monet.http.*;
import org.monet.space.applications.upload.FileUploadWrapper;
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
	protected Request request;
	protected Response response;
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
				return request.getSessionId();
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
			public Request getRequest() {
				return request;
			}
		});
	}

	public Account getAccount() {
		return this.getFederationLayer().loadAccount();
	}

	public Boolean setRequest(HttpServletRequest oRequest) {
		return setRequest(new HttpRequest(oRequest));
	}

	public Boolean setRequest(Request oRequest) {
		this.request = oRequest;
		this.idSession = oRequest.getSessionId();
		return true;
	}

	public Boolean setResponse(HttpServletResponse oResponse) {
		return setResponse(new HttpResponse(oResponse));
	}

	public Boolean setResponse(Response oResponse) {
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
		return getPostParameterMap(new HttpRequest(request));
	}

	protected HashMap<String, List<FileItem>> getPostParameterMap(Request request) throws FileUploadException {
		HashMap<String, List<FileItem>> result = new HashMap<>();

		//OLD IMPLEMENTATION (without using request/response interfaces)
		//ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
		
		FileUploadWrapper uploadHandler = new FileUploadWrapper(new DiskFileItemFactory());

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