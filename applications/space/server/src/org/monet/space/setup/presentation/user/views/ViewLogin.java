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

package org.monet.space.setup.presentation.user.views;

import org.monet.space.setup.presentation.user.util.Context;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.MasterLayer;
import org.monet.space.kernel.model.Session;
import org.monet.space.setup.control.constants.SessionVariable;
import org.monet.space.setup.core.constants.Tags;

import java.io.Writer;

public class ViewLogin extends View {

	public ViewLogin(Context oContext, String codeLanguage) {
		super(oContext, codeLanguage);
	}

	public void execute(Writer writer) {
		Session oSession = org.monet.space.kernel.model.Context.getInstance().getCurrentSession();
		String codeError = (String) oSession.getVariable(SessionVariable.ERROR);
		MasterLayer masterLayer = ComponentFederation.getMasterLayer();

		super.execute(writer);

		//this.context.put(Tags., );

		if (codeError != null) {
			this.context.put(Tags.ERROR_CODE, codeError);
			oSession.setVariable(SessionVariable.ERROR, null);
		}

		this.context.put(Tags.COLONIZED, masterLayer.colonized());

		this.oAgentTemplates.merge("/setup/templates/login.tpl", this.context, writer);
	}

}