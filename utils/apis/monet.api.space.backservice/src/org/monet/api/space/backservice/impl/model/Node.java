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

public class Node extends BaseObject {
	private String idParent;
	private String idOwner;
	private String owner;
	private String label;
	private String description;
	private Date createDate;
	private Date updateDate;
	private Date deleteDate;
	private boolean isHighlighted;
	private boolean isLocked;
	private boolean isEditable;
	private boolean isDeleteable;
	private String partnerContext;
	private boolean isPrototype;
	private String prototypeId;
	private int order;
	private NodeList nodeList;
	private AttributeList attributeList;

	public Node() {
		this.idParent = null;
		this.idOwner = null;
		this.owner = null;
		this.label = "";
		this.description = "";
		this.createDate = new Date();
		this.updateDate = new Date();
		this.deleteDate = null;
		this.isHighlighted = false;
		this.isLocked = false;
		this.isEditable = true;
		this.isDeleteable = true;
		this.partnerContext = null;
		this.isPrototype = false;
		this.prototypeId = "-1";
		this.order = -1;
		this.nodeList = new NodeList();
		this.attributeList = new AttributeList();
	}

	public String getParentId() {
		return this.idParent;
	}

	public void setParentId(String idParent) {
		this.idParent = idParent;
	}

	public String getOwnerId() {
		return this.idOwner;
	}

	public void setOwnerId(String id) {
		this.idOwner = id;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String code) {
		this.owner = code;
	}

	public int getOrder() {
		return this.order;
	}

	public void setOrder(Integer order) {
		this.order = order;
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

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public Date getDeleteDate() {
		return this.deleteDate;
	}

	public boolean isHighlighted() {
		return this.isHighlighted;
	}

	public boolean isLocked() {
		return this.isLocked;
	}

	public boolean isEditable() {
		return this.isEditable;
	}

	public boolean isDeleteable() {
		return this.isDeleteable;
	}

	public String getPartnerContext() {
		return this.partnerContext;
	}

	public boolean isPrototype() {
		return this.isPrototype;
	}

	public String getPrototypeId() {
		return this.prototypeId;
	}

	public NodeList getNodeList() {
		return this.nodeList;
	}

	public void setNodeList(NodeList nodeList) {
		this.nodeList = nodeList;
	}

	public AttributeList getAttributeList() {
		return this.attributeList;
	}

	public void setAttributeList(AttributeList attributeList) {
		this.attributeList = attributeList;
	}

	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

		serializer.startTag("", "node");

		serializer.attribute("", "id", this.id);
		serializer.attribute("", "code", this.getCode());

		if (this.idParent != null) serializer.attribute("", "idParent", this.idParent);

		if (this.idOwner != null) {
			serializer.attribute("", "idOwner", this.idOwner);
			serializer.attribute("", "owner", this.owner);
		}

		serializer.attribute("", "createDate", dateFormat.format(this.createDate));
		serializer.attribute("", "updateDate", dateFormat.format(this.updateDate));

		if (deleteDate != null)
			serializer.attribute("", "deleteDate", (this.deleteDate != null) ? dateFormat.format(this.deleteDate) : "");

		serializer.attribute("", "isHighlighted", String.valueOf(this.isHighlighted));
		serializer.attribute("", "isLocked", String.valueOf(this.isLocked));
		serializer.attribute("", "isEditable", String.valueOf(this.isEditable));
		serializer.attribute("", "isDeleteable", String.valueOf(this.isDeleteable));

		if (this.partnerContext != null)
			serializer.attribute("", "partnerContext", this.partnerContext);

		serializer.attribute("", "isPrototype", String.valueOf(this.isPrototype));
		if (this.getPrototypeId() != null)
			serializer.attribute("", "idPrototype", this.getPrototypeId());

		serializer.attribute("", "order", String.valueOf(this.order));

		serializer.startTag("", "label");
		serializer.text(this.label);
		serializer.endTag("", "label");

		serializer.startTag("", "description");
		serializer.text(this.description);
		serializer.endTag("", "description");

		this.getNodeList().serializeToXML(serializer);
		this.getAttributeList().serializeToXML(serializer);

		serializer.endTag("", "node");
	}

	public void deserializeFromXML(Element node) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

		if (node.getAttribute("id") != null) this.id = node.getAttributeValue("id");
		if (node.getAttribute("code") != null) this.code = node.getAttributeValue("code");
		if (node.getAttribute("idParent") != null) this.idParent = node.getAttributeValue("idParent");
		if (node.getAttribute("idOwner") != null) this.idOwner = node.getAttributeValue("idOwner");
		if (node.getAttribute("owner") != null) this.owner = node.getAttributeValue("owner");
		if (node.getAttribute("order") != null) this.order = Integer.valueOf(node.getAttributeValue("order"));
		if (node.getAttribute("createDate") != null) this.createDate = dateFormat.parse(node.getAttributeValue("createDate"));
		if (node.getAttribute("updateDate") != null) this.updateDate = dateFormat.parse(node.getAttributeValue("updateDate"));
		if (node.getAttribute("deleteDate") != null && !node.getAttributeValue("deleteDate").isEmpty()) this.deleteDate = dateFormat.parse(node.getAttributeValue("deleteDate"));
		if (node.getAttribute("isHighlighted") != null) this.isHighlighted = Boolean.valueOf(node.getAttributeValue("isHighlighted"));
		if (node.getAttribute("isLocked") != null) this.isLocked = Boolean.valueOf(node.getAttributeValue("isLocked"));
		if (node.getAttribute("isEditable") != null) this.isEditable = Boolean.valueOf(node.getAttributeValue("isEditable"));
		if (node.getAttribute("isDeleteable") != null) this.isDeleteable = Boolean.valueOf(node.getAttributeValue("isDeleteable"));
		if (node.getAttribute("partnerContext") != null) this.partnerContext = node.getAttributeValue("partnerContext");
		if (node.getAttribute("isPrototype") != null) this.isPrototype = Boolean.valueOf(node.getAttributeValue("isPrototype"));
		if (node.getAttribute("idPrototype") != null && !node.getAttributeValue("idPrototype").isEmpty()) this.prototypeId = node.getAttributeValue("idPrototype");
		if (node.getAttribute("order") != null) this.order = Integer.valueOf(node.getAttributeValue("order"));

		if (node.getChild("label") != null) this.label = node.getChild("label").getText();
		if (node.getChild("description") != null) this.description = node.getChild("description").getText();

		this.nodeList.deserializeFromXML(node.getChild("nodelist"));
		this.attributeList.deserializeFromXML(node.getChild("attributelist"));
	}

}
