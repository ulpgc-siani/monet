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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cube extends BaseObject {
	private String label;
	private String description;
	private Date createDate;
	private Date updateDate;

	public Cube(String code) {
		super();
		this.setCode(code);
		this.label = "";
		this.description = "";
		this.createDate = new Date();
		this.updateDate = null;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void deserializeFromXML(Element node) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

		if (node.getAttribute("code") != null) this.code = node.getAttributeValue("code");
		if (node.getAttribute("name") != null) this.name = node.getAttributeValue("name");
		if (node.getAttribute("createDate") != null) this.createDate = dateFormat.parse(node.getAttributeValue("createDate"));
		if (node.getAttribute("updateDate") != null) this.updateDate = dateFormat.parse(node.getAttributeValue("updateDate"));

		if (node.getChild("label") != null) this.label = node.getChild("label").getText();
		if (node.getChild("description") != null) this.description = node.getChild("description").getText();
	}

	@Override
	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
	}
}
