package org.monet.metamodel.internal;

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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.ArrayList;

public class LayoutElementSectionDefinition extends LayoutElementDefinition {

	private
	@XmlAttribute(required = false, name = "label")
	String label;

	private
	@XmlAttribute(required = false, name = "opened")
	String opened;

	private
	@XmlElements({@XmlElement(name = "section", type = LayoutElementSectionDefinition.class), @XmlElement(name = "box", type = LayoutElementBoxDefinition.class), @XmlElement(name = "break", type = LayoutElementBreakDefinition.class), @XmlElement(name = "space", type = LayoutElementSpaceDefinition.class), @XmlElement(name = "grid", type = LayoutElementGridDefinition.class), @XmlElement(name = "slider", type = LayoutElementGridDefinition.class)})
	ArrayList<LayoutElementDefinition> elements;

	public LayoutElementSectionDefinition() {
		super();
		this.elements = new ArrayList<LayoutElementDefinition>();
	}

	public String getLabel() {
		return label;
	}

	public boolean isOpened() {

		if (opened == null)
			return true;

		return Boolean.valueOf(opened);
	}

	public ArrayList<LayoutElementDefinition> getElements() {
		return elements;
	}

}
