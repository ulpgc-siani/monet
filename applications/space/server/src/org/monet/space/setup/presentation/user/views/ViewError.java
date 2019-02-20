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
import org.monet.space.setup.core.constants.Tags;

import java.io.Writer;


public class ViewError extends View {
	private String codeError;

	public ViewError(Context oContext, String codeLanguage) {
		super(oContext, codeLanguage);
		this.codeError = null;
	}

	public Boolean setErrorCode(String codeError) {
		this.codeError = codeError;
		return true;
	}

	public void execute(Writer writer) {
		super.execute(writer);

		this.context.put(Tags.ERROR_CODE, this.codeError);

		this.oAgentTemplates.merge("/setup/templates/error.tpl", this.context, writer);
	}

}