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

package org.monet.space.frontservice.control.actions;

import org.monet.metamodel.Definition;
import org.monet.metamodel.TaskDefinition;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.frontservice.presentation.user.renders.FrontserviceRender;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.ServiceList;

import java.util.ArrayList;
import java.util.List;

public class ActionShowServices extends Action {

	public ActionShowServices() {
		super();
	}

	public String execute() {
		FrontserviceRender render;
		Dictionary dictionary = Dictionary.getInstance();

		String url = LibraryRequest.getBaseUrl(this.request);
		url = url.substring(0, url.indexOf("servlet/frontservice")) + "service/";

		List<Definition> serviceDefinitionList = new ArrayList<Definition>();

		for (TaskDefinition definition : dictionary.getTaskDefinitionList())
			if (definition.isService()) serviceDefinitionList.add(definition);

		render = this.rendersFactory.get(new ServiceList(), "services");
		render.setParameter("baseUrl", url);
		render.setParameter("businessServices", serviceDefinitionList);
		render.setParameter("sourceServices", dictionary.getSourceDefinitionList());

		return render.getOutput();
	}

}