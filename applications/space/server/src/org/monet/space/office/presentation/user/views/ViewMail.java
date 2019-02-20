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

package org.monet.space.office.presentation.user.views;

import org.monet.space.office.core.constants.Tags;
import org.monet.space.office.presentation.user.agents.AgentRender;
import org.monet.space.office.presentation.user.util.Context;
import org.monet.space.kernel.constants.Strings;

import java.io.Writer;

public class ViewMail extends View {
	private String sTemplate;
	private String sSubject;
	private String sBody;

	public ViewMail(Context oContext, AgentRender oAgentRender, String codeLanguage) {
		super(oContext, oAgentRender, codeLanguage);
		this.sTemplate = null;
		this.sSubject = Strings.EMPTY;
		this.sBody = Strings.EMPTY;
	}

	public Boolean setSubject(String sSubject) {
		this.sSubject = sSubject;
		return true;
	}

	public Boolean setBody(String sBody) {
		this.sBody = sBody;
		return true;
	}

	public Boolean setTemplate(String sTemplate) {
		this.sTemplate = sTemplate;
		return true;
	}

	public void execute(Writer writer) {
		super.execute(writer);

		if (this.sTemplate == null) return;

		this.context.put(Tags.SUBJECT, this.sSubject);
		this.context.put(Tags.BODY, this.sBody);

		this.agentTemplates.mergeApplicationTemplate(this.sTemplate, this.context, writer);
	}

}