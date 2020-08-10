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
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.*;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

import java.util.HashMap;

public class ActionLoadGroupedRoleList extends Action {
	private RoleLayer roleLayer;

	public ActionLoadGroupedRoleList() {
		super();
		this.roleLayer = ComponentFederation.getInstance().getRoleLayer();
	}

	public String execute() {
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String groupedId = LibraryRequest.getParameter(Parameter.GROUPED_ID, this.request);
		DataRequest dataRequest;
		JSONObject result = new JSONObject();
		JSONArray rows = new JSONArray();

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (code == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_ROLE_LIST);

		dataRequest = new DataRequest();
		dataRequest.setCode(code);
		dataRequest.setLimit(-1);

		HashMap<String, String> parameters = this.getRequestParameters();
		dataRequest.setParameters(parameters);

		try {
			dataRequest.setCondition(new String(parameters.get(Parameter.CONDITION)));
		} catch (NullPointerException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		} catch (ClassCastException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		}

		RoleList roleList = this.roleLayer.loadRoleList(dataRequest);
		boolean isFirst = true;
		for (Role role : roleList.get().values()) {
			String roleGroupedId = this.getGroupedRoleId(role);
			if (!groupedId.isEmpty() && !groupedId.equals(roleGroupedId)) continue;

			JSONObject jsonRole = role.toJson();

			if (isFirst) {
				isFirst = false;
				if ((Boolean) jsonRole.get("expired") != true)
					continue;
			}

			rows.add(jsonRole);
			String label = role.getLabel();
			String subtitle = "";
			if (role instanceof ServiceRole) {
				ServiceRole serviceRole = (ServiceRole) role;
				label = serviceRole.getPartner().getLabel();
				subtitle = serviceRole.getPartnerService().getLabel();
			}

			jsonRole.put("label", label);
			jsonRole.put("subtitle", subtitle);
		}

		result.put("nrows", rows.size());
		result.put("rows", rows);

		return result.toString();
	}

	private String getGroupedRoleId(Role role) {
		String id = "";

		if (role.isUserRole()) {
			User user = ((UserRole) role).getUser();
			id = user.getId();
		} else if (role.isServiceRole()) {
			ServiceRole serviceRole = (ServiceRole) role;
			id = LibraryString.generateIdForKey(serviceRole.getPartnerId() + serviceRole.getPartnerServiceName());
		} else if (role.isFeederRole()) {
			FeederRole feederRole = (FeederRole) role;
			id = LibraryString.generateIdForKey(feederRole.getPartnerId() + feederRole.getPartnerFeederName());
		}

		return id;
	}

}