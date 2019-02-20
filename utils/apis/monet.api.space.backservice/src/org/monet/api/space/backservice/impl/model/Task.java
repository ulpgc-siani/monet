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

public class Task extends BaseObject {
	private String type;
	private String label;
	private String description;
	private String idTarget;
	private String state;
	private Date createDate;
	private Date updateDate;
	private Date startDate;
	private Date startSuggestedDate;
	private Date endDate;
	private Date endSuggestedDate;

	public static final class Parameter {
		public static final String TYPE = "type";
		public static final String STATE = "state";
		public static final String SITUATION = "situation";
	}

	public static final String PROPERTIES = "Properties";
	public static final String ENROLMENTS = "Enrolments";
	public static final String PROCESS = "Process";
	public static final String DATA = "Data";
	public static final String TARGET = "Target";
	public static final String INPUT = "Input";
	public static final String OUTPUT = "Output";
	public static final String SERVICE_INSTANCE = "ServiceInstance";

	public Task() {
		super();
		this.type = "";
		this.label = "";
		this.description = "";
		this.idTarget = null;
		this.state = "";
		this.createDate = new Date();
		this.updateDate = new Date();
		this.startDate = null;
		this.startSuggestedDate = null;
		this.endDate = null;
		this.endSuggestedDate = null;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTargetId() {
		return this.idTarget;
	}

	public void setTargetId(String targetId) {
		this.idTarget = targetId;
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

	public String getState() {
		return this.state;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartSuggestedDate() {
		return startSuggestedDate;
	}

	public void setStartSuggestedDate(Date startSuggestedDate) {
		this.startSuggestedDate = startSuggestedDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndSuggestedDate() {
		return endSuggestedDate;
	}

	public void setEndSuggestedDate(Date endSuggestedDate) {
		this.endSuggestedDate = endSuggestedDate;
	}

	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

		serializer.startTag("", "task");

		serializer.attribute("", "id", this.id);
		serializer.attribute("", "type", this.getType());
		if (this.idTarget != null) serializer.attribute("", "idTarget", this.idTarget);
		serializer.attribute("", "state", this.state);
		serializer.attribute("", "code", this.code);
		serializer.attribute("", "name", this.name);

		if (this.createDate != null) serializer.attribute("", "createDate", dateFormat.format(this.createDate));
		if (this.updateDate != null) serializer.attribute("", "updateDate", dateFormat.format(this.updateDate));
		if (this.startDate != null) serializer.attribute("", "startDate", dateFormat.format(this.startDate));
		if (this.startSuggestedDate != null) serializer.attribute("", "startSuggestedDate", dateFormat.format(this.startSuggestedDate));
		if (this.endDate != null) serializer.attribute("", "endDate", dateFormat.format(this.endDate));
		if (this.endSuggestedDate != null) serializer.attribute("", "endSuggestedDate", dateFormat.format(this.endSuggestedDate));

		serializer.startTag("", "label");
		serializer.text(this.label);
		serializer.endTag("", "label");

		serializer.startTag("", "description");
		serializer.text(this.description);
		serializer.endTag("", "description");

		serializer.endTag("", "task");
	}

	public void deserializeFromXML(Element task) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

		if (task.getAttribute("id") != null)
			this.id = task.getAttributeValue("id");

		if (task.getAttribute("code") != null)
			this.code = task.getAttributeValue("code");

		if (task.getAttribute("name") != null)
			this.name = task.getAttributeValue("name");

		if (task.getAttribute("type") != null) {
			this.type = task.getAttributeValue("type");
			if ((!this.type.equals("")) && (!this.type.equals("-1")))
				this.setType(this.type);
		}

		if (task.getAttribute("idTarget") != null)
			this.idTarget = task.getAttributeValue("idTarget");
		if (task.getAttribute("state") != null)
			this.state = task.getAttributeValue("state");
		if (task.getAttribute("createDate") != null && !task.getAttributeValue("createDate").isEmpty())
			this.createDate = dateFormat.parse(task.getAttributeValue("createDate"));
		if (task.getAttribute("updateDate") != null && !task.getAttributeValue("updateDate").isEmpty())
			this.updateDate = dateFormat.parse(task.getAttributeValue("updateDate"));
		if (task.getAttribute("startDate") != null && !task.getAttributeValue("startDate").isEmpty())
			this.setStartDate(dateFormat.parse(task.getAttributeValue("startDate")));
		if (task.getAttribute("startSuggestedDate") != null && !task.getAttributeValue("startSuggestedDate").isEmpty())
			this.setStartSuggestedDate(dateFormat.parse(task.getAttributeValue("startSuggestedDate")));
		if (task.getAttribute("endDate") != null && !task.getAttributeValue("endDate").isEmpty())
			this.setEndDate(dateFormat.parse(task.getAttributeValue("endDate")));
		if (task.getAttribute("endSuggestedDate") != null && !task.getAttributeValue("endSuggestedDate").isEmpty())
			this.setEndSuggestedDate(dateFormat.parse(task.getAttributeValue("endSuggestedDate")));

		if (task.getChild("label") != null)
			this.label = task.getChild("label").getText();
		if (task.getChild("description") != null)
			this.description = task.getChild("description").getText();
	}

}
