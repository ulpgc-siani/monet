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

import org.monet.space.fms.control.constants.Actions;
import org.monet.space.fms.core.constants.ErrorCode;
import org.monet.space.kernel.exceptions.SystemException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ActionsFactory {
	private static ActionsFactory oInstance;
	private HashMap<String, Class<? extends Action>> hmActions;

	private ActionsFactory() {
		this.hmActions = new HashMap<String, Class<? extends Action>>();
		this.registerActions();
	}

	private Boolean registerActions() {

		this.register(Actions.UPLOAD_DOCUMENTS, ActionUploadDocuments.class);
		this.register(Actions.DOWNLOAD_DOCUMENT, ActionDownloadDocument.class);
		this.register(Actions.UPLOAD_IMAGES, ActionUploadImages.class);
		this.register(Actions.DOWNLOAD_IMAGE, ActionDownloadImage.class);

		return true;
	}

	public synchronized static ActionsFactory getInstance() {
		if (oInstance == null) oInstance = new ActionsFactory();
		return oInstance;
	}

	public Action get(String sType, HttpServletRequest oRequest, HttpServletResponse oResponse) {
		Class<?> cAction;
		Action oAction = null;

		try {
			cAction = (Class<?>) this.hmActions.get(sType);
			oAction = (Action) cAction.newInstance();
			oAction.setRequest(oRequest);
			oAction.setResponse(oResponse);
			oAction.initialize();
		} catch (NullPointerException oException) {
			throw new SystemException(ErrorCode.ACTIONS_FACTORY, sType, oException);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.ACTIONS_FACTORY, sType, oException);
		}

		return oAction;
	}

	public Boolean register(String sType, Class<? extends Action> cAction)
		throws IllegalArgumentException {

		if ((cAction == null) || (sType == null)) {
			return false;
		}
		this.hmActions.put(sType, cAction);

		return true;
	}

}
