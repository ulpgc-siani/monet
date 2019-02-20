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

import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.simpleframework.xml.ElementList;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskFact extends BaseObject implements Fact {

	private String title;
	private String subtitle;
	private String userId;
	private String taskId;
	private Date createDate;

	@ElementList(required = false)
	private ArrayList<MonetLink> items = new ArrayList<MonetLink>();

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getInternalCreateDate() {
		return createDate;
	}

	public String getCreateDate() {
		return LibraryDate.getDateAndTimeString(this.createDate, Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.DEFAULT, true, Strings.BAR45);
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskId() {
		return taskId;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public void setSubTitle(String subtitle) {
		this.subtitle = subtitle;
	}

	@Override
	public String getSubTitle() {
		return this.subtitle;
	}

	@Override
	public void addLink(MonetLink monetLink) {
		this.items.add(monetLink);
	}

	@Override
	public List<MonetLink> getLinks() {
		return this.items;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

		serializer.startTag("", "fact");
		serializer.attribute("", "id", this.id);
		serializer.attribute("", "title", this.title);
		serializer.attribute("", "subtitle", this.subtitle);
		serializer.attribute("", "idUser", this.userId);
		serializer.attribute("", "idTask", this.taskId);
		serializer.attribute("", "createDate", dateFormat.format(this.createDate));

		serializer.startTag("", "links");
		for (MonetLink link : this.items) {
			serializer.startTag("", "link");
			serializer.text(link.toString());
			serializer.endTag("", "link");
		}
		serializer.endTag("", "links");

		serializer.endTag("", "fact");
	}

}
