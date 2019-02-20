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

package org.monet.space.analytics.control.actions;

import org.monet.space.kernel.exceptions.SystemException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ActionsFactory {
	private static ActionsFactory instance;
	private HashMap<String, Object> actions;

	private ActionsFactory() {
		this.actions = new HashMap<String, Object>();
		this.registerActions();
	}

	private Boolean registerActions() {
		this.actions.put(Actions.LOGOUT, ActionLogout.class);
		this.actions.put(Actions.REDIRECT, ActionRedirect.class);
		this.actions.put(Actions.PING, ActionPing.class);
		this.actions.put(Actions.SHOW_APPLICATION, ActionShowApplication.class);
		this.actions.put(Actions.SHOW_DASHBOARD, ActionShowDashboard.class);
		this.actions.put(Actions.PRINT_DASHBOARD, ActionPrintDashboard.class);
		this.actions.put(Actions.DOWNLOAD_DASHBOARD, ActionDownloadDashboard.class);
		this.actions.put(Actions.LOAD_BUSINESS_MODEL_FILE, ActionLoadBusinessModelFile.class);
		this.actions.put(Actions.LOAD_THEME_FILE, ActionLoadThemeFile.class);
		this.actions.put(Actions.LOAD_DASHBOARD, ActionLoadDashboard.class);
		this.actions.put(Actions.LOAD_CATEGORIES, ActionLoadCategories.class);
		this.actions.put(Actions.LOAD_CHART, ActionLoadChart.class);
		this.actions.put(Actions.LOAD_CHART_API, ActionLoadChartApi.class);
		this.actions.put(Actions.LOAD_ACCOUNT, ActionLoadAccount.class);
		this.actions.put(Actions.SAVE_ACCOUNT, ActionSaveAccount.class);
		return true;
	}

	public synchronized static ActionsFactory getInstance() {
		if (instance == null) instance = new ActionsFactory();
		return instance;
	}

	public String getOperation(String op) {
		if (this.actions.containsKey(op))
			return op;
		return Actions.SHOW_APPLICATION;
	}

	public Action get(String type, HttpServletRequest request, HttpServletResponse response) {
		Class<?> clazz;
		Action action = null;

		try {
			clazz = (Class<?>) this.actions.get(type);
			action = (Action) clazz.newInstance();
			action.setRequest(request);
			action.setResponse(response);
			action.initialize();
		} catch (NullPointerException exception) {
			throw new SystemException("Could not found action", type, exception);
		} catch (Exception exception) {
			throw new SystemException("Could not found action", type, exception);
		}

		return action;
	}

	public Boolean register(String sType, Object cAction)
		throws IllegalArgumentException {

		if ((cAction == null) || (sType == null)) {
			return false;
		}
		this.actions.put(sType, cAction);

		return true;
	}

}
