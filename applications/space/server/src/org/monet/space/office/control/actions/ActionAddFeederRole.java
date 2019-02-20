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
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.FederationUnit;
import org.monet.space.kernel.model.FederationUnitFeeder;
import org.monet.space.kernel.model.Role;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

import java.util.Date;

public class ActionAddFeederRole extends Action {
	private RoleLayer roleLayer;

	public ActionAddFeederRole() {
		super();
		ComponentFederation componentFederation = ComponentFederation.getInstance();
		this.roleLayer = componentFederation.getRoleLayer();
	}

	public String execute() {
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String beginDateValue = LibraryRequest.getParameter(Parameter.BEGIN_DATE, this.request);
		String expireDateValue = LibraryRequest.getParameter(Parameter.EXPIRE_DATE, this.request);
		String partnerId = LibraryRequest.getParameter(Parameter.PARTNER_ID, this.request);
		String partnerServiceName = LibraryRequest.getParameter(Parameter.PARTNER_SERVICE_NAME, this.request);
		Date beginDate = null, expireDate = null;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (code == null || beginDateValue == null || partnerId == null || partnerServiceName == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.ADD_SERVICE_ROLE);
		}

		beginDate = LibraryDate.parseDate(beginDateValue);

		if (expireDateValue != null && !expireDateValue.isEmpty())
			expireDate = LibraryDate.parseDate(expireDateValue);

		FederationLayer federationLayer = this.getFederationLayer();
		FederationUnit partner = federationLayer.loadPartner(partnerId);
		FederationUnitFeeder partnerFeeder = partner.getFeederList().get(partnerServiceName);

		if (this.roleLayer.existsNonExpiredFeederRole(code, partner, partnerFeeder))
			return ErrorCode.ROLE_ACTIVE;

		Role role = this.roleLayer.addFeederRole(code, partner, partnerFeeder, beginDate, expireDate);

		return role.toJson().toJSONString();
	}

}