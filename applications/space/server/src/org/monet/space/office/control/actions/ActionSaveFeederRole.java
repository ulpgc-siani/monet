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

import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.FeederRole;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

import java.util.Date;

public class ActionSaveFeederRole extends Action {
	private RoleLayer roleLayer;

	public ActionSaveFeederRole() {
		super();
		this.roleLayer = ComponentFederation.getInstance().getRoleLayer();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String partnerId = LibraryRequest.getParameter(Parameter.PARTNER_ID, this.request);
		String partnerServiceName = LibraryRequest.getParameter(Parameter.PARTNER_SERVICE_NAME, this.request);
		String beginDateValue = LibraryRequest.getParameter(Parameter.BEGIN_DATE, this.request);
		String expireDateValue = LibraryRequest.getParameter(Parameter.EXPIRE_DATE, this.request);

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (id == null || partnerId == null || partnerServiceName == null || beginDateValue == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SAVE_USER_ROLE);

		FeederRole role = (FeederRole) this.roleLayer.loadRole(id);
		Date beginDate = LibraryDate.parseDate(beginDateValue);

		role.setPartnerId(partnerId);
		role.setPartnerFeederName(partnerServiceName);
		role.setBeginDate(beginDate);

		if (expireDateValue != null && !expireDateValue.isEmpty())
			role.setExpireDate(LibraryDate.parseDate(expireDateValue));
		else
			role.setExpireDate(null);

		this.roleLayer.saveRole(role);

		return Language.getInstance().getMessage(MessageCode.ROLE_SAVED);
	}

}