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

import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.UserList;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionSearchUsers extends Action {

	public ActionSearchUsers() {
		super();
	}

	public String execute() {
		String sCondition = LibraryRequest.getParameter(Parameter.CONDITION, this.request);
		String sStart = LibraryRequest.getParameter(Parameter.START, this.request);
		String sLimit = LibraryRequest.getParameter(Parameter.LIMIT, this.request);
		UserList oUserList;
		DataRequest oDataRequest = new DataRequest();
		FederationLayer layer = this.getFederationLayer();

		if (!layer.isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (sCondition == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SEARCH_USERS);
		}

		try {
			oDataRequest.setStartPos(Integer.valueOf(sStart));
		} catch (NumberFormatException oException) {
			oDataRequest.setStartPos(0);
		} catch (ClassCastException oException) {
			oDataRequest.setStartPos(0);
		}

		try {
			oDataRequest.setLimit(Integer.valueOf(sLimit));
		} catch (NumberFormatException oException) {
			oDataRequest.setLimit(10);
		} catch (ClassCastException oException) {
			oDataRequest.setLimit(10);
		}

		oUserList = layer.searchUsersWithRoles(oDataRequest);

		return oUserList.toJson().toJSONString();
	}

}