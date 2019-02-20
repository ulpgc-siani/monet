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

import net.minidev.json.JSONObject;
import org.monet.metamodel.DashboardDefinition;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class Dashboard extends Entity {
	private Object asset;
	private DashboardDefinition definition;
	private String content;
	private String model;

	public void injectAsset(Object asset) {
		this.asset = asset;
	}

	public Dashboard(DashboardDefinition definition) {
		super();
		this.definition = definition;
	}

	public String getCode() {
		return this.definition.getCode();
	}

	public DashboardDefinition getDefinition() {
		return this.definition;
	}

	public String getLabel() {
		return this.definition.getLabelString();
	}

	@Override
	public String getInstanceLabel() {
		return this.definition.getLabelString();
	}

	public String getDescription() {
		return this.definition.getDescriptionString();
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Object getAsset() {
		return this.asset;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	@Override
	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		result.put("id", this.getId());
		result.put("code", this.getCode());
		result.put("label", this.getLabel());
		result.put("description", this.getDescription());
		result.put("content", this.getContent());
		result.put("model", this.getModel());
		return result;
	}

}
