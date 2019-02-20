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
import org.monet.metamodel.RoleDefinition;
import org.monet.metamodel.RoleDefinition.EnableFeedersProperty;
import org.monet.metamodel.RoleDefinition.EnableServicesProperty;
import org.monet.space.kernel.constants.LabelCode;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.model.Language;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActionLoadRoleDefinitionList extends Action {

	public ActionLoadRoleDefinitionList() {
		super();
	}

	public String execute() {
		JSONObject result = new JSONObject();
		JSONArray rows = new JSONArray();
		Dictionary dictionary = Dictionary.getInstance();
		List<RoleDefinition> roleDefinitionList = dictionary.getRoleDefinitionList();

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		String query = this.request.getParameter(Parameter.CONDITION);
		int start = Integer.valueOf(this.request.getParameter(Parameter.START));
		int limit = Integer.valueOf(this.request.getParameter(Parameter.LIMIT));
		int pos = -1;

		if (limit == -1)
			limit = roleDefinitionList.size();

		Collections.sort(roleDefinitionList, new Comparator<Object>() {
			private Language language = Language.getInstance();

			public int compare(Object roleDefinition1, Object roleDefinition2) {
				String roleDefinition1Label = this.language.getModelResource(((RoleDefinition) roleDefinition1).getLabel());
				String roleDefinition2Label = this.language.getModelResource(((RoleDefinition) roleDefinition2).getLabel());
				return roleDefinition1Label.compareTo(roleDefinition2Label);
			}
		});

		for (RoleDefinition definition : roleDefinitionList) {

			if (definition.isDisabled())
				continue;

			JSONObject row = new JSONObject();
			String label = Language.getInstance().getModelResource(definition.getLabel());

			if (label == null || label.trim().isEmpty())
				label = Language.getInstance().getLabel(LabelCode.NO_LABEL);

			pos++;

			if (query != null && label.toLowerCase().indexOf(query) == -1)
				continue;

			if (pos < start)
				continue;

			if (pos > start + limit)
				continue;

			EnableServicesProperty enableServicesDefinition = definition.getEnableServices();
			EnableFeedersProperty enableFeedersDefinition = definition.getEnableFeeders();

			row.put("id", definition.getCode());
			row.put("code", definition.getCode());
			row.put("label", label);
			row.put("enableUsers", definition.getDisableUsers() == null);
			row.put("PartnerServiceOntologies", enableServicesDefinition != null ? enableServicesDefinition.getOntology() : null);
			row.put("PartnerFeederOntologies", enableFeedersDefinition != null ? enableFeedersDefinition.getOntology() : null);

			rows.add(row);
		}

		result.put("nrows", roleDefinitionList.size());
		result.put("rows", rows);

		return result.toString();
	}

}