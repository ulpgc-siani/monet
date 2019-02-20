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
import java.util.HashMap;
import java.util.Map;

public class DatabaseRepositoryQuery extends BaseObject {
	private String name;
	private Map<String, Object> parameters;
	private Map<String, String> subQueries;

	public DatabaseRepositoryQuery(String name, Map<String, Object> parameters) {
		super();
		this.name = name;
		this.parameters = parameters;
		this.subQueries = new HashMap<>();
	}

	public DatabaseRepositoryQuery(String name, Map<String, Object> parameters, Map<String, String> subQueries) {
		super();
		this.name = name;
		this.parameters = parameters != null ? parameters : new HashMap<String, Object>();
		this.subQueries = subQueries;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getParameters() {
		return this.parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public void setSubQueries(Map<String, String> subQueries) {
		this.subQueries = subQueries;
	}

	public Map<String, String> getSubQueries() {
		return subQueries;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}

}
