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

import org.monet.space.kernel.model.NodeList;
import org.monet.space.kernel.model.SearchNodeResult;
import org.monet.space.office.core.constants.Tags;
import org.monet.space.office.presentation.user.agents.AgentRender;
import org.monet.space.office.presentation.user.util.Context;

import java.io.Writer;

public class ViewSearch extends View {
	String codeTemplate;

	public ViewSearch(Context oContext, AgentRender oAgentRender, String codeLanguage) {
		super(oContext, oAgentRender, codeLanguage);
		this.codeTemplate = null;
	}

	public Boolean setCodeTemplate(String codeTemplate) {
		this.codeTemplate = codeTemplate;
		return true;
	}

	public void execute(Writer writer) {
		String sTemplateFilename;
		SearchNodeResult searchResult;

		super.execute(writer);

		if (this.target == null) return;
		if (this.codeTemplate == null) return;

		searchResult = (SearchNodeResult) this.target;
		sTemplateFilename = this.getTemplateFilename(this.codeTemplate);
		if (sTemplateFilename == null) return;

		this.context.put(sTemplateFilename, false);
		this.context.put(Tags.SEARCH_RESULT, searchResult);
		this.context.put(Tags.CONDITION, searchResult.getCondition());
		this.context.put(Tags.TOTALCOUNT, searchResult.getTotalCount());
		this.context.put(Tags.NODELIST, (NodeList) searchResult.getList());

		this.agentTemplates.mergeModelTemplate(sTemplateFilename, this.context, writer);
	}

}