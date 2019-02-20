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

package org.monet.space.kernel.model.wrappers;

import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.kernel.model.Node;

import java.util.HashMap;
import java.util.Map;

public class NodeSetWrapper {
	private Node node;

	public NodeSetWrapper(Node node) {
		this.node = node;
	}

	public Map<String, String> getFilterParameters() {
		Map<String, String> result = new HashMap<>();
		Attribute attribute = node.createAttribute(node.getAttributeList(), Attribute.FILTER_PARAMETERS);

		for (Indicator indicator : attribute.getIndicatorList())
			result.put(indicator.getCode(), indicator.getData());

		return result;
	}

	public void addFilterParameter(String name, String value) {
		Attribute attribute = node.createAttribute(node.getAttributeList(), Attribute.FILTER_PARAMETERS);
		attribute.addOrSetIndicatorValue(name, 0, value);
	}

	public void deleteFilterParameter(String name) {
		Attribute attribute = node.createAttribute(node.getAttributeList(), Attribute.FILTER_PARAMETERS);
		attribute.getIndicatorList().delete(name);
	}


}
