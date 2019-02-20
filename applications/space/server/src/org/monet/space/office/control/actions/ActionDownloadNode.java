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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

public class ActionDownloadNode extends Action {
	private NodeLayer nodeLayer;
	private ComponentDocuments componentDocuments;

	public ActionDownloadNode() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		this.componentDocuments = ComponentDocuments.getInstance();
	}

	private HashMap<String, String> getParameters() {
		Enumeration<?> mParameters = this.request.getParameterNames();
		HashMap<String, String> hmResult = new HashMap<String, String>();

		while (mParameters.hasMoreElements()) {
			String sKey = (String) mParameters.nextElement();
			String sValue = (String) this.request.getParameter(sKey);
			hmResult.put(sKey, sValue);
		}

		return hmResult;
	}

	public String execute() {
		String idNode = LibraryRequest.getParameter(Parameter.ID, this.request);
		HttpClient httpClient = new HttpClient();
		Node node;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (idNode == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.DOWNLOAD_NODE);

		node = this.nodeLayer.loadNode(idNode);

		if (!this.componentSecurity.canRead(node, this.getAccount())) {
			throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, idNode);
		}

		if ((!node.isDocument())) {
			throw new DataException(ErrorCode.NOT_DOCUMENT_NEITHER_REPORT, idNode);
		}

		Boolean download = this.componentDocuments.getSupportedFeatures().get(ComponentDocuments.Feature.DOWNLOAD);
		if (download == null || !download)
			throw new SystemException(ErrorCode.DOWNLOAD_NOT_SUPPORTED, idNode);

		try {
			GetMethod method = new GetMethod(this.componentDocuments.getDownloadUrl(this.getParameters()));
			int status = httpClient.executeMethod(method);

			if (status == HttpStatus.SC_NOT_FOUND)
				throw new SystemException(ErrorCode.DOWNLOAD_NODE, idNode, null);

			String contentType = method.getResponseHeader("Content-Type").getValue();
			String extension = MimeTypes.getInstance().getExtension(contentType);
			String fileName = LibraryString.replaceAll(node.getLabel(), Strings.SPACE, Strings.UNDERLINED).replaceAll(Strings.COMMA, "") + Strings.DOT + extension;

			this.response.setContentType(contentType);
			this.response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			IOUtils.copy(method.getResponseBodyAsStream(), response.getOutputStream());

		} catch (HttpException oException) {
			throw new SystemException(ErrorCode.DOWNLOAD_NODE, idNode, oException);
		} catch (IOException oException) {
			throw new SystemException(ErrorCode.DOWNLOAD_NODE, idNode, oException);
		}

		return null; // Avoid controller getting response writer
	}

}