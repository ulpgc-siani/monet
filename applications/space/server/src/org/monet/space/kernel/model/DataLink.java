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

package org.monet.space.kernel.model;

import org.monet.space.kernel.constants.Strings;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class DataLink extends BaseObject {
	private String indexCode;
	private String codeNode;

	public DataLink() {
		super();
		this.indexCode = Strings.EMPTY;
		this.codeNode = Strings.EMPTY;
	}

	public String getIndexCode() {
		return this.indexCode;
	}

	public Boolean setIndexCode(String indexNode) {
		this.indexCode = indexNode;
		return true;
	}

	public String getCodeNode() {
		return this.codeNode;
	}

	public Boolean setCodeNode(String codeNode) {
		this.codeNode = codeNode;
		return true;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}
}