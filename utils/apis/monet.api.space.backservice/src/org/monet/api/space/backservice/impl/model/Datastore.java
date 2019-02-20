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

package org.monet.api.space.backservice.impl.model;

import org.jdom.Element;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public class Datastore extends BaseObject {
	private HashMap<String, Dimension> dimensionMap = new HashMap<String, Dimension>();
	private HashMap<String, Cube> cubeMap = new HashMap<String, Cube>();

	public Datastore() {
		super();
	}

	public Dimension getDimension(String key) {
		return this.dimensionMap.get(key);
	}

	public Cube getCube(String key) {
		return this.cubeMap.get(key);
	}

	public void deserializeFromXML(Element node) throws ParseException {

		if (node.getAttribute("code") != null) this.code = node.getAttributeValue("code");
		if (node.getAttribute("name") != null) this.name = node.getAttributeValue("name");

		List<Element> cubeList = node.getChildren("cube");
		for (Element cubeElement : cubeList) {
			Cube child = new Cube(cubeElement.getAttributeValue("code"));
			child.deserializeFromXML(cubeElement);
			this.cubeMap.put(child.getCode(), child);
		}

		List<Element> dimensionList = node.getChildren("dimension");
		for (Element dimensionElement : dimensionList) {
			Dimension child = new Dimension(dimensionElement.getAttributeValue("code"));
			child.deserializeFromXML(dimensionElement);
			this.dimensionMap.put(child.getCode(), child);
		}
	}

	@Override
	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
	}

}
