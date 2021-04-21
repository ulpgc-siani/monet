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
import org.monet.space.office.core.model.Language;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.constants.LabelCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.User;
import org.monet.space.kernel.model.UserList;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

import java.util.HashMap;

public class ActionLoadUsers extends Action {

	public ActionLoadUsers() {
		super();
	}

	public String execute() {
		UserList userList;
		DataRequest dataRequest = new DataRequest();
		FederationLayer layer = this.getFederationLayer();
		JSONObject result = new JSONObject();
		JSONArray rows = new JSONArray();

		if (!layer.isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		HashMap<String, String> parameters = this.getRequestParameters();

		try {
			dataRequest.setCondition(new String(parameters.get(Parameter.CONDITION)));
		} catch (NullPointerException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		} catch (ClassCastException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		}

		try {
			dataRequest.setStartPos(Integer.valueOf(parameters.get(Parameter.START)));
		} catch (NumberFormatException oException) {
			dataRequest.setStartPos(0);
		} catch (ClassCastException oException) {
			dataRequest.setStartPos(0);
		}

		try {
			dataRequest.setLimit(Integer.valueOf(parameters.get(Parameter.LIMIT)));
		} catch (NumberFormatException oException) {
			dataRequest.setLimit(Strings.UNDEFINED_INT);
		} catch (ClassCastException oException) {
			dataRequest.setLimit(Strings.UNDEFINED_INT);
		}

		userList = layer.searchUsersWithRoles(dataRequest);
		for (User user : userList.get().values()) {
			JSONObject jsonUser = user.toJson();
			String label = user.getInfo().getFullname();

			jsonUser.put("id", user.getId());

			if (label == null || label.trim().isEmpty())
				label = Language.getInstance().getLabel(LabelCode.NO_LABEL);

			jsonUser.put("label", label);
			jsonUser.put("email", user.getInfo().getEmail());
			rows.add(jsonUser);
		}

		result.put("nrows", userList.getTotalCount());
		result.put("rows", rows);

		return result.toString();
	}

}