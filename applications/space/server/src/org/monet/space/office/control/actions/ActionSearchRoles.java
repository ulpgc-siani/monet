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

import org.monet.metamodel.RoleDefinition;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.User;
import org.monet.space.kernel.model.UserList;
import org.monet.space.mobile.model.Language;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

import java.util.List;

public class ActionSearchRoles extends Action {

	public ActionSearchRoles() {
		super();
	}

	public String execute() {
		String sCondition = LibraryRequest.getParameter(Parameter.CONDITION, this.request);
		FederationLayer layer = this.getFederationLayer();
		Language language = Language.getInstance();

		if (!layer.isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (sCondition == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SEARCH_USERS);
		}

		List<RoleDefinition> roleDefinitionList = Dictionary.getInstance().getRoleDefinitionList();
		UserList roleList = new UserList();


		for (RoleDefinition roleDefinition : roleDefinitionList) {

			if (roleDefinition.isDisabled())
				continue;

			String roleLabel = language.getModelResource(roleDefinition.getLabel());
			if (roleDefinition.isAbstract() || !roleLabel.toLowerCase().contains(sCondition.toLowerCase()))
				continue;
			User userRole = new User();
			userRole.setId(roleDefinition.getCode());
			userRole.setName(roleLabel);
			userRole.getInfo().setFullname(roleLabel);
			userRole.getInfo().setEmail(roleDefinition.getDescriptionString());
			roleList.add(userRole);
		}
		roleList.setTotalCount(roleDefinitionList.size());

		return roleList.toJson().toJSONString();
	}

}