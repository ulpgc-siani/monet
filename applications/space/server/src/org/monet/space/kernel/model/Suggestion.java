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
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

public class Suggestion extends BaseObject {
	private String idTask;
	private SuggestionTarget target;
	private Integer weight;
	private Boolean isRead;
	private String notes;
	private Date createDate;

	public Suggestion() {
		super();
		this.target = new SuggestionTarget(null, SuggestionTarget.NONE);
		this.createDate = new Date();
	}

	public String getIdTask() {
		return this.idTask;
	}

	public void setIdTask(String idTask) {
		this.idTask = idTask;
	}

	public SuggestionTarget getTarget() {
		return this.target;
	}

	public void setTarget(SuggestionTarget target) {
		this.target = target;
	}

	public Integer getWeight() {
		return this.weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Boolean getRead() {
		return this.isRead;
	}

	public void setRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getCreateDate(String format, String codeLanguage, TimeZone zone) {
		if (this.createDate == null) return null;
		return LibraryDate.getDateAndTimeString(this.createDate, codeLanguage, zone, format, false, Strings.BAR45);
	}

	public String getCreateDate(String sFormat) {
		return this.getCreateDate(sFormat, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getCreateDate() {
		return this.getCreateDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalCreateDate() {
		return this.createDate;
	}

	public Boolean setCreateDate(Date dtCreated) {
		this.createDate = dtCreated;
		return true;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}

}
