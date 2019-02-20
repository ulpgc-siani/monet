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

package org.monet.space.setup.control.actions;

import org.monet.space.setup.control.constants.Actions;
import org.monet.space.setup.core.constants.ErrorCode;
import org.monet.space.kernel.exceptions.SystemException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;


public class ActionsFactory {
	private static ActionsFactory oInstance;
	private HashMap<String, Object> hmActions;

	private ActionsFactory() {
		this.hmActions = new HashMap<String, Object>();
		this.registerActions();
	}

	private Boolean registerActions() {
		this.register(Actions.SHOW_APPLICATION, ActionShowApplication.class);
		this.register(Actions.SHOW_LOADING, ActionShowLoading.class);
		this.register(Actions.SHOW_BUSINESS_UNIT_STOPPED, ActionShowBusinessUnitStopped.class);
		this.register(Actions.SHOW_LOGIN, ActionShowLogin.class);
		this.register(Actions.LOGIN, ActionLogin.class);
		this.register(Actions.LOGOUT, ActionLogout.class);
		this.register(Actions.ADD_MASTER_FROM_SIGNATURE, ActionAddMasterFromSignature.class);
		this.register(Actions.ADD_MASTER_FROM_CERTIFICATE, ActionAddMasterFromCertificate.class);
		this.register(Actions.DELETE_MASTER, ActionDeleteMaster.class);
		this.register(Actions.START, ActionStart.class);
		this.register(Actions.STOP, ActionStop.class);
		this.register(Actions.UPLOAD_DISTRIBUTION, ActionUploadDistribution.class);
		this.register(Actions.DOWNLOAD_DISTRIBUTION, ActionDownloadDistribution.class);
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

	public Boolean register(String sType, Object cAction)
		throws IllegalArgumentException {

		if ((cAction == null) || (sType == null)) {
			return false;
		}
		this.hmActions.put(sType, cAction);

		return true;
	}

}
