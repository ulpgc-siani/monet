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

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.FederationUnit;
import org.monet.space.kernel.model.FederationUnitList;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionLoadUnits extends Action {

	public ActionLoadUnits() {
		super();
	}

	public String execute() {

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		return this.getMembers().toJSONString();
	}

    private JSONArray getMembers() {
		FederationUnitList federationUnitList = this.getFederationLayer().loadMembers(getAccount());
		JSONArray result = new JSONArray();
		String businessUnitName = BusinessUnit.getInstance().getName();

		for (FederationUnit federationUnit : federationUnitList) {

			if (!federationUnit.isVisible())
				continue;

			String name = federationUnit.getName();
			JSONObject jsonPartner = new JSONObject();
			jsonPartner.put("id", name);
			jsonPartner.put("label", federationUnit.getLabel());
			jsonPartner.put("url", federationUnit.getUrl());
			jsonPartner.put("active", businessUnitName.equals(name));
			jsonPartner.put("disabled", false);
			result.add(jsonPartner);
		}

		return result;
	}

}