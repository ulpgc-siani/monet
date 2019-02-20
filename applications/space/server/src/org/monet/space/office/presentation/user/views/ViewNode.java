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

import org.monet.space.office.core.model.Language;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeList;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.constants.Tags;
import org.monet.space.office.presentation.user.agents.AgentRender;
import org.monet.space.office.presentation.user.util.Context;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ViewNode extends View {
	String codeTemplate;

	public ViewNode(Context oContext, AgentRender oAgentRender, String codeLanguage) {
		super(oContext, oAgentRender, codeLanguage);
		this.codeTemplate = null;
	}

	protected Boolean printNodeList(Node oNode) {
		this.context.put(Tags.NODELIST, oNode.getNodeList());
		return true;
	}

	protected Boolean printSections(Node oNode) {
		HashMap<String, LinkedHashMap<String, Node>> hmSections = new HashMap<String, LinkedHashMap<String, Node>>();
		NodeList oNodeList = oNode.getNodeList();
		Iterator<String> iter = oNodeList.get().keySet().iterator();
		String codeType = Strings.EMPTY;

		while (iter.hasNext()) {
			Node oCurrentNode = (Node) oNodeList.get(iter.next());
			codeType = oCurrentNode.getCode();
			if (!hmSections.containsKey(codeType)) hmSections.put(codeType, new LinkedHashMap<String, Node>());
			hmSections.get(codeType).put(oCurrentNode.getId(), oCurrentNode);
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
		Node oNode;
		String code = null;

		super.execute(writer);

		if (this.target == null) return;

		oNode = (Node) this.target;
		code = (this.codeTemplate != null) ? this.codeTemplate : oNode.getCode();
		sTemplateFilename = this.getTemplateFilename(code);
		if (sTemplateFilename == null) {
			try {
				writer.write(Language.getInstance().getErrorMessage(ErrorCode.TEMPLATE_NOT_FOUND) + " Id: " + oNode.getId() + ". Mode: " + this.type + " - " + code + " - " + this.mode);
			} catch (IOException e) {
				this.agentLogger.error(e);
			}
		}

		this.context.put(sTemplateFilename, false);
		this.context.put(Tags.NODE, oNode);
		this.context.put(Tags.TARGET, oNode);

		this.printNodeList(oNode);
		this.printSections(oNode);

		this.agentTemplates.mergeModelTemplate(sTemplateFilename, this.context, writer);
	}

}