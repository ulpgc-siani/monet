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

import net.minidev.json.JSONObject;
import org.monet.metamodel.SourceDefinition;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Source;
import org.monet.space.kernel.model.TermList;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionLoadSourceNewTerms extends Action {
	private SourceLayer sourceLayer;

	public ActionLoadSourceNewTerms() {
		super();
		this.sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		JSONObject result = new JSONObject();
		TermList termList;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (id == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_SOURCE_NEW_TERMS);
		}

		Source<SourceDefinition> source = this.sourceLayer.loadSource(id);
		termList = this.sourceLayer.loadSourceNewTerms(source);

		result.put("source", source.toJson());
		result.put("termList", termList.toJson());

		return result.toString();
	}

}