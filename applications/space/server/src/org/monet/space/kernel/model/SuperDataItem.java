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

public class SuperDataItem extends BaseObject {
	private String codeAttribute;
	private String value;
	private String data;
	private IndicatorList indicatorList;

	public SuperDataItem(SuperDataItem superDataItem) {
		this(superDataItem.getId(), superDataItem.getCodeAttribute(), superDataItem.getCode(), superDataItem.getValue(), superDataItem.getData());
	}

	public SuperDataItem(String id, String codeAttribute, String code, String value, String data) {
		super();
		this.id = id;
		this.codeAttribute = codeAttribute;
		this.code = code;
		this.value = value;
		this.data = data;
		this.indicatorList = null;
	}

	public SuperDataItem() {
		this("", "", "", "", "");
	}

	public String getCodeAttribute() {
		return this.codeAttribute;
	}

	public void setCodeAttribute(String codeAttribute) {
		this.codeAttribute = codeAttribute;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public IndicatorList getIndicatorList() {
		if (this.indicatorList != null) return this.indicatorList;
		this.indicatorList = new IndicatorList();
		this.indicatorList.deserializeFromXML(this.data);
		return this.indicatorList;
	}

	public Boolean equals(SuperDataItem data) {
		if (!this.codeAttribute.equals(data.getCodeAttribute())) return false;
		if (!this.code.equals(data.getCode())) return false;
		if (!this.value.equals(data.getValue())) return false;
		if (!this.data.equals(data.getData())) return false;
		return true;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}
}
