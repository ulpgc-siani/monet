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

import org.monet.space.office.core.model.Language;
import org.monet.space.office.presentation.user.renders.OfficeRender;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TeamLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.TeamAccessException;
import org.monet.space.kernel.model.Team;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionLoadTeam extends Action {
	private TeamLayer teamLayer;

	public ActionLoadTeam() {
		super();
		this.teamLayer = ComponentPersistence.getInstance().getTeamLayer();
	}

	public String execute() {
		Team team;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		try {
			team = this.teamLayer.loadTeam();
		} catch (TeamAccessException exception) {
			this.agentException.error(exception);
			return ErrorCode.READ_TEAM_PERMISSIONS + Strings.COLON + Strings.SPACE + Language.getInstance().getErrorMessage(ErrorCode.READ_TEAM_PERMISSIONS);
		}

		OfficeRender render = this.rendersFactory.get(team, "preview.html?mode=page", this.getRenderLink(), getAccount());
		team.setContent(render.getOutput());

		return team.toJson().toJSONString();
	}

}