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

import org.monet.space.kernel.constants.Common;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeList;
import org.monet.space.office.core.constants.Tags;
import org.monet.space.office.presentation.user.agents.AgentRender;
import org.monet.space.office.presentation.user.util.Context;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ViewTree extends View {
	private String codeTemplate;

	public ViewTree(Context oContext, AgentRender oAgentRender, String codeLanguage) {
		super(oContext, oAgentRender, codeLanguage);
		this.codeTemplate = Common.Codes.TEMPLATE_MODEL_ROOT;
	}

	private Boolean printNodeList() {
		NodeList oNodeList = (NodeList) this.target;
		this.context.put(Tags.NODELIST, oNodeList);
		return true;
	}

	private Boolean printSections() {
		HashMap<String, LinkedHashMap<String, Node>> hmSections = new HashMap<String, LinkedHashMap<String, Node>>();
		NodeList oNodeList = (NodeList) this.target;
		Iterator<String> iter = oNodeList.get().keySet().iterator();
		String codeType = Strings.EMPTY;

		while (iter.hasNext()) {
			Node oNode = (Node) oNodeList.get(iter.next());
			codeType = oNode.getCode();
			if (!hmSections.containsKey(codeType)) hmSections.put(codeType, new LinkedHashMap<String, Node>());
			hmSections.get(codeType).put(oNode.getId(), oNode);
		}

		this.context.put(Tags.SECTIONS, hmSections);

		return true;
	}

	public Boolean setCodeTemplate(String codeTemplate) {
		this.codeTemplate = codeTemplate;
		return true;
	}

	public void execute(Writer writer) {
		String sTemplateFilename;

		super.execute(writer);

		if (this.target == null) return;

		sTemplateFilename = this.getTemplateFilename(this.codeTemplate);
		if (sTemplateFilename == null) return;

		this.printNodeList();
		this.printSections();

		this.agentTemplates.mergeModelTemplate(sTemplateFilename, this.context, writer);
	}

}