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

import edu.emory.mathcs.backport.java.util.Collections;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.*;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ActionLoadRoleList extends Action {
	private RoleLayer roleLayer;

	public ActionLoadRoleList() {
		super();
		this.roleLayer = ComponentFederation.getInstance().getRoleLayer();
	}

	public String execute() {
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String view = LibraryRequest.getParameter(Parameter.VIEW, this.request);
		DataRequest dataRequest;
		HashMap<String, String> parameters;
		JSONObject result = new JSONObject();
		JSONArray rows = new JSONArray();

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (code == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_ROLE_LIST);
		}

		dataRequest = new DataRequest();
		parameters = this.getRequestParameters();

		dataRequest.setParameters(parameters);

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

		try {
			dataRequest.setCondition(new String(parameters.get(Parameter.CONDITION)));
		} catch (NullPointerException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		} catch (ClassCastException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		}

		RoleList roleList = this.roleLayer.loadRoleList(code, dataRequest);
		if (view == null || view.isEmpty()) {
			HashMap<String, String> groupedRolesIds = new HashMap<String, String>();
			ArrayList<Role> groupedRoles = this.getGroupedRoles(roleList, groupedRolesIds);

			groupedRoles = this.getSortedRoles(groupedRoles);

			for (Role role : groupedRoles) {
				String groupedId = groupedRolesIds.get(role.getId());

				JSONObject jsonRole = role.toJson();
				jsonRole.put("view", "grouped");
				jsonRole.put("groupedId", groupedId);

				String label = role.getLabel();
				String subtitle = "";
				if (role instanceof ServiceRole) {
					ServiceRole serviceRole = (ServiceRole) role;
					FederationUnit partner = serviceRole.getPartner();
					FederationUnitService partnerService = serviceRole.getPartnerService();
					label = partner != null ? partner.getLabel() : "";
					subtitle = partnerService != null ? partnerService.getLabel() : "";
				}

				jsonRole.put("label", label);
				jsonRole.put("subtitle", subtitle);

				rows.add(jsonRole);
			}
		}

		result.put("nrows", roleList.getTotalCount());
		result.put("rows", rows);

		return result.toString();
	}

	private ArrayList<Role> getSortedRoles(ArrayList<Role> roles) {
		ArrayList<Role> sortedRoles = new ArrayList<Role>();

		sortedRoles.addAll(roles);
		Collections.sort(sortedRoles, new Comparator<Object>() {
			public int compare(Object object1, Object object2) {
				Role role1 = (Role) object1;
				Role role2 = (Role) object2;
				String role1Label = LibraryString.cleanString(role1.getLabel()).toLowerCase();
				String role2Label = LibraryString.cleanString(role2.getLabel()).toLowerCase();

				return role1Label.compareTo(role2Label);
			}
		});

		return sortedRoles;
	}

	private ArrayList<Role> getGroupedRoles(RoleList roleList, HashMap<String, String> groupedRolesIds) {
		ArrayList<Role> groupedRoles = new ArrayList<Role>();
		HashMap<String, Integer> groupedRolesPos = new HashMap<String, Integer>();

		int pos = 0;
		for (Role role : roleList.get().values()) {
			String groupedId = this.getGroupedRoleId(role);

			if (!groupedRolesPos.containsKey(groupedId)) {
				groupedRolesIds.put(role.getId(), groupedId);
				groupedRolesPos.put(groupedId, pos);
				groupedRoles.add(role);
				pos++;
			} else {
				Role groupedRole = groupedRoles.get(groupedRolesPos.get(groupedId));

				int groupedRoleId = Integer.valueOf(groupedRole.getId());
				int roleId = Integer.valueOf(role.getId());

				if (roleId > groupedRoleId) {
					groupedRolesIds.put(role.getId(), groupedId);
					groupedRolesPos.put(groupedId, pos);
					groupedRoles.add(role);
					pos++;
				}
			}
		}

		return groupedRoles;
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