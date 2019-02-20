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

import org.monet.space.office.configuration.Configuration;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Common;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.DummyProgressCallback;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;


public class ActionImportNode extends Action {
	private NodeLayer nodeLayer;

	public ActionImportNode() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String importerKey = LibraryRequest.getParameter(Parameter.ID, this.request);
		String sData = LibraryRequest.getParameter(Parameter.DATA, this.request);
		String sFilename;
		String sFileDir = Configuration.getInstance().getImportsDir();
		Reader dataReader = null;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (sData == null) {
			sFilename = this.getAccount().getId() + Common.FileExtensions.XML;

			try {
				dataReader = new InputStreamReader(new FileInputStream(sFileDir + Strings.BAR45 + sFilename), "UTF-8");
			} catch (Exception e) {
				throw new DataException(ErrorCode.WRONG_FORMAT, Actions.IMPORT_NODE, e);
			}
		} else {
			dataReader = new StringReader(sData);
		}

		this.nodeLayer.importNode(importerKey, null, dataReader, 0, new DummyProgressCallback());

		return "none";
	}

}