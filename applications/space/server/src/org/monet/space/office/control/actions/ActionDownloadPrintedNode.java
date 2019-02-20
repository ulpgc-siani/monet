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

package org.monet.space.office.control.actions;

import org.apache.commons.io.IOUtils;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.office.control.constants.Parameter;

import java.io.IOException;
import java.io.InputStream;

public class ActionDownloadPrintedNode extends PrintAction {
	private NodeLayer nodeLayer;

	public ActionDownloadPrintedNode() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String idNode = LibraryRequest.getParameter(Parameter.ID, this.request);
		String template = LibraryRequest.getParameter(Parameter.TEMPLATE, this.request);

		try {
			Node node = this.nodeLayer.loadNode(idNode);
			InputStream document = loadDocument(idNode);

			this.response.setContentType(MimeTypes.getInstance().get(template));
			this.response.setHeader("Content-Disposition", "attachment; filename=" + LibraryString.replaceAll(node.getLabel(), Strings.SPACE, Strings.UNDERLINED) + Strings.DOT + template);
			IOUtils.copy(document, response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null; // Avoid controller getting response writer
	}

}