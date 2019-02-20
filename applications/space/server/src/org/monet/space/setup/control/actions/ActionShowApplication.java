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

package org.monet.space.setup.control.actions;

import org.monet.space.kernel.Kernel;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.MasterLayer;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Master;
import org.monet.space.setup.control.constants.Parameter;
import org.monet.space.setup.presentation.user.constants.Views;
import org.monet.space.setup.presentation.user.views.ViewApplication;
import org.monet.space.setup.presentation.user.views.ViewsFactory;

import java.io.IOException;
import java.util.Map;

public class ActionShowApplication extends Action {

	public ActionShowApplication() {
		super();
	}

	public String execute() {

		if (!this.kernel.isLogged()) {
			this.launchAuthenticateAction();
			return null;
		}

		MasterLayer masterLayer = ComponentFederation.getMasterLayer();
		Kernel kernel = Kernel.getInstance();
		Map<String, Master> mastersMap = masterLayer.requestMasterListItems();

		try {
			ViewApplication viewApplication = (ViewApplication) ViewsFactory.getInstance().get(Views.APPLICATION, this.codeLanguage);
			viewApplication.setBusinessUnit(BusinessUnit.getInstance());
			viewApplication.setComponentsInfo(kernel.getAvailableComponents());
			viewApplication.setComponentTypes(kernel.getComponentTypes());
			viewApplication.setIsBusinessUnitStarted(BusinessUnit.isRunning());
			viewApplication.setMasters(mastersMap);
			viewApplication.setSelectedTab(request.getParameter(Parameter.TAB));
			viewApplication.execute(response.getWriter());
		} catch (IOException exception) {
			this.agentException.error(exception);
		}

		return null;
	}

}