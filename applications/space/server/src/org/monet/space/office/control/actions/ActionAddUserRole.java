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

import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.UserInfo;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.Role;
import org.monet.space.kernel.model.User;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;

import java.util.Date;

public class ActionAddUserRole extends Action {
	private RoleLayer roleLayer;

	public ActionAddUserRole() {
		super();
		this.roleLayer = ComponentFederation.getInstance().getRoleLayer();
	}

	public String execute() {
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String userId = LibraryRequest.getParameter(Parameter.ID_USER, this.request);
		String username = LibraryRequest.getParameter(Parameter.USERNAME, this.request);
		String beginDateValue = LibraryRequest.getParameter(Parameter.BEGIN_DATE, this.request);
		String expireDateValue = LibraryRequest.getParameter(Parameter.EXPIRE_DATE, this.request);
		Date beginDate = null, expireDate = null;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (code == null || userId == null || beginDateValue == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.ADD_USER_ROLE);

		beginDate = LibraryDate.parseDate(beginDateValue);

		if (expireDateValue != null && !expireDateValue.isEmpty())
			expireDate = LibraryDate.parseDate(expireDateValue);

		FederationLayer federationLayer = this.getFederationLayer();
		Account account = federationLayer.locateAccount(username);
		if (account == null) {
			UserInfo userInfo = new UserInfo();
			userInfo.setFullname(username);
			account = federationLayer.createAccount(null, username, userInfo);
		}

		User user = account.getUser();
		if (this.roleLayer.existsNonExpiredUserRole(code, user))
			return ErrorCode.ROLE_ACTIVE;

		Role role = this.roleLayer.addUserRole(code, user, beginDate, expireDate);
		federationLayer.createOrUpdateAccount(account);

		return role.toJson().toJSONString();
	}

}