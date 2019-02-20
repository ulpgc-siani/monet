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

package org.monet.space.office.presentation.user.agents;

import org.monet.space.office.core.model.Language;
import org.monet.space.office.presentation.user.views.ViewNode;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.exceptions.BaseException;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Task;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.presentation.user.constants.Views;
import org.monet.space.office.presentation.user.views.ViewTask;
import org.monet.space.office.presentation.user.views.ViewsFactory;

public class AgentRender {
	private ViewsFactory oViewsFactory;

	public AgentRender() {
		this.oViewsFactory = ViewsFactory.getInstance();
	}

	public Node loadNode(String id) {
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		Node oNode;

		try {
			oNode = nodeLayer.loadNode(id);
		} catch (Exception oException) {
			AgentLogger.getInstance().error(oException);
			oNode = null;
		}

		return oNode;
	}

	public String showNode(String id, String sMode, String sType, String codeLanguage) {
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		Node oNode;
		ViewNode oViewNode;

		try {
			oNode = nodeLayer.loadNode(id);
		} catch (BaseException exception) {
			AgentLogger.getInstance().error(exception);
			return "<div class='error'>" + Language.getInstance().getErrorMessage(ErrorCode.RENDER_NODE) + " " + id + "</div>";
		}

		oViewNode = (ViewNode) this.oViewsFactory.get(Views.NODE, this, codeLanguage);
		oViewNode.setTarget(oNode);
		oViewNode.setType(sType);
		oViewNode.setMode(sMode);

		return oViewNode.execute();
	}

	public String showNodeReference(String id, String sMode, String sType, String codeLanguage) {
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		Node oNode;
		ViewNode oViewNode;

		try {
			oNode = nodeLayer.loadNode(id);
		} catch (BaseException exception) {
			AgentLogger.getInstance().error(exception);
			return "<div class='error'>" + Language.getInstance().getErrorMessage(ErrorCode.RENDER_NODE) + " " + id + "</div>";
		}

		oViewNode = (ViewNode) this.oViewsFactory.get(Views.NODE, this, codeLanguage);
		oViewNode.setTarget(oNode);
		oViewNode.setType(sType);
		oViewNode.setMode(sMode);

		return oViewNode.execute();
	}

	public String showNode(Node oNode, String sMode, String sType, String codeLanguage) {
		ViewNode oViewNode;

		oViewNode = (ViewNode) this.oViewsFactory.get(Views.NODE, this, codeLanguage);
		oViewNode.setTarget(oNode);
		oViewNode.setType(sType);
		oViewNode.setMode(sMode);

		return oViewNode.execute();
	}

	public String showNodeReference(Node oNode, String sMode, String sType, String codeLanguage) {
		ViewNode oViewNode;

		oViewNode = (ViewNode) this.oViewsFactory.get(Views.NODE, this, codeLanguage);
		oViewNode.setTarget(oNode);
		oViewNode.setType(sType);
		oViewNode.setMode(sMode);

		return oViewNode.execute();
	}

	public String showTask(Task oTask, String sMode, String sType, String codeLanguage) {
		ViewTask oViewTask;

		oViewTask = (ViewTask) this.oViewsFactory.get(Views.TASK, this, codeLanguage);
		oViewTask.setTarget(oTask);
		oViewTask.setType(sType);
		oViewTask.setMode(sMode);

		return oViewTask.execute();
	}

}
