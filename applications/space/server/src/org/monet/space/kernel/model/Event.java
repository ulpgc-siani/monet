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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Event extends BaseObject {
	private String name;
	private Map<String, String> properties;
	private Date dueDate;

    public Event() {
        this(null, null, new HashMap<String, String>());
    }

    public Event(String name, Date dueDate, Map<String, String> properties) {
		this.name = name;
        this.dueDate = dueDate;
		this.properties = properties;
	}

	public String getName() {
		return name;
	}

    public void setName(String name) {
        this.name = name;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getProperty(String name) {
		return properties.get(name);
	}

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
    }

}
