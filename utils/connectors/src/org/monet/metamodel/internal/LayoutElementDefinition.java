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

public abstract class LayoutElementDefinition {
	private
	@XmlAttribute(name = "width", required = false)
	String width;
	private
	@XmlAttribute(name = "height", required = false)
	String height;
	private
	@XmlAttribute(name = "edition", required = false)
	String edition;
	private
	@XmlAttribute(name = "placeholder", required = false)
	String placeHolder;
	private
	@XmlAttribute(name = "hint", required = false)
	String hint;

	public LayoutElementDefinition() {
		super();
	}

	public float getWidth() {
		return this.getFloatValue(width);
	}

	public String getWidthUnit() {
		return this.getUnit(width);
	}

	public float getHeight() {
		return this.getFloatValue(height);
	}

	public String getHeightUnit() {
		return this.getUnit(height);
	}

	public String getEdition() {
		return edition;
	}

	public String getPlaceHolder() {
		return placeHolder;
	}

	public String getHint() {
		return hint;
	}

	public boolean isSection() {
		return this instanceof LayoutElementSectionDefinition;
	}

	public boolean isBox() {
		return this instanceof LayoutElementBoxDefinition;
	}

	public boolean isBreak() {
		return this instanceof LayoutElementBreakDefinition;
	}

	public boolean isSpace() {
		return this instanceof LayoutElementSpaceDefinition;
	}

	private float getFloatValue(String value) {

		if (value == null || value.isEmpty())
			return 0;

		String localValue = value.replace("px", "");
		localValue = localValue.replace("%", "");

		return Float.parseFloat(localValue);
	}

	private String getUnit(String value) {

		if (value == null || value.isEmpty())
			return "";

		if (value.indexOf("%") != -1)
			return "%";
		else if (value.indexOf("px") != -1)
			return "px";

		return "px";
	}

}
