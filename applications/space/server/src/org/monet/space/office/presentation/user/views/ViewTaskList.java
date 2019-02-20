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
import org.monet.space.office.presentation.user.constants.ViewMode;
import org.monet.space.office.presentation.user.constants.ViewType;
import org.monet.space.office.presentation.user.util.Context;
import org.monet.space.kernel.constants.Common;
import org.monet.space.kernel.constants.ObjectTypes;
import org.monet.space.kernel.model.TaskType;

import java.io.Writer;
import java.util.List;

public class ViewTaskList extends ViewSystem {
	public List<TaskType> types;
	public List<String> states;

	public ViewTaskList(Context oContext, AgentRender oAgentRender, String codeLanguage) {
		super(oContext, oAgentRender, codeLanguage);
		this.type = ViewType.BROWSE;
		this.mode = ViewMode.DEFAULT;
	}

	public void setTypes(List<TaskType> types) {
		this.types = types;
	}

	public void setStates(List<String> states) {
		this.states = states;
	}

	public void execute(Writer writer) {
		String sTemplateFilename;

		super.execute(writer);

		this.mode = ObjectTypes.TASKLIST.toLowerCase() + Common.FileExtensions.HTML;
		sTemplateFilename = this.getTemplateFilename(this.codeType);
		if (sTemplateFilename == null) return;

		this.context.put(Tags.TASKLIST_TYPES, this.types);
		this.context.put(Tags.TASKLIST_STATES, this.states);

		this.agentTemplates.mergeModelTemplate(sTemplateFilename, this.context, writer);
	}

}