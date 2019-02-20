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

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class ThesaurusLocation extends BaseObject {
	private String type;
	private String data;

	public static abstract class Type {
		public static final String DATABASE = "db";
		public static final String FILESYSTEM = "fs";
		public static final String WEBSERVICE = "ws";
	}

	public ThesaurusLocation() {
		super();
		this.type = "";
		this.data = "";
	}

	public String getChain() {
		if (this.type.equals("")) return "";
		return this.type + ":" + this.data;
	}

	public void setChain(String locationChain) {
		if (locationChain == null || locationChain.length() < 3) return;
		this.type = locationChain.substring(0, 2).toLowerCase();
		this.data = locationChain.substring(3);
	}

	public static String createFileSystemChain(String data) {
		return ThesaurusLocation.Type.FILESYSTEM + ":" + data;
	}

	public Boolean isDataBaseType() {
		return this.type.equals(ThesaurusLocation.Type.DATABASE);
	}

	public Boolean isFileSystemType() {
		return this.type.equals(ThesaurusLocation.Type.FILESYSTEM);
	}

	public Boolean isWebServiceType() {
		return this.type.equals(ThesaurusLocation.Type.WEBSERVICE);
	}

	public String getData() {
		return this.data;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}

}