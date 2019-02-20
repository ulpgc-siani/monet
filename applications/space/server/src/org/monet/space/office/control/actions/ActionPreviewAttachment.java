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
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.office.control.constants.Actions;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

public class ActionPreviewAttachment extends Action {
	private ComponentDocuments componentDocuments;

	public ActionPreviewAttachment() {
		super();
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
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		HttpClient oHttpClient = new HttpClient();
		GetMethod oMethod;
		Integer iStatus;
		Boolean bPreview;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (id == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.PREVIEW_ATTACHMENT);
		}

		bPreview = this.componentDocuments.getSupportedFeatures().get(ComponentDocuments.Feature.PREVIEW);
		if ((bPreview == null) || (!bPreview)) {
			throw new SystemException(ErrorCode.PREVIEW_NOT_SUPPORTED, id);
		}

		try {
			oMethod = new GetMethod(this.componentDocuments.getPreviewUrl(this.getParameters()));
			iStatus = oHttpClient.executeMethod(oMethod);
			if (iStatus == HttpStatus.SC_NOT_FOUND) throw new SystemException(ErrorCode.PREVIEW_ATTACHMENT, id, null);
			this.response.getOutputStream().write(oMethod.getResponseBody());
		} catch (HttpException oException) {
			throw new SystemException(ErrorCode.PREVIEW_ATTACHMENT, id, oException);
		} catch (IOException oException) {
			throw new SystemException(ErrorCode.PREVIEW_ATTACHMENT, id, oException);
		}

		return null; // Avoid controller getting response writer
	}

}