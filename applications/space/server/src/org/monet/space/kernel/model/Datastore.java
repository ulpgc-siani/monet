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

import org.monet.metamodel.DatastoreDefinition;
import org.monet.metamodel.DatastoreDefinitionBase.CubeProperty;
import org.monet.metamodel.DatastoreDefinitionBase.DimensionProperty;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Datastore extends BaseObject {
	private HashMap<String, Dimension> dimensionMap = null;
	private HashMap<String, Cube> cubeMap = null;

	public Datastore() {
		super();
	}

	public DatastoreDefinition getDefinition() {
		return Dictionary.getInstance().getDatastoreDefinition(this.getCode());
	}

	public Dimension getDimension(String key) {
		if (this.dimensionMap == null)
			this.createDimensionMap();

		return this.dimensionMap.get(key);
	}

	public Map<String, Dimension> getDimensions() {
		if (this.dimensionMap == null)
			this.createDimensionMap();

		return this.dimensionMap;
	}

	public Cube getCube(String key) {
		if (this.cubeMap == null)
			this.createCubeMap();

		return this.cubeMap.get(key);
	}

	public Map<String, Cube> getCubes() {
		if (this.cubeMap == null)
			this.createCubeMap();

		return this.cubeMap;
	}

	private void createDimensionMap() {
		this.dimensionMap = new HashMap<String, Dimension>();
		Collection<DimensionProperty> dimensionDefinitions = this.getDefinition().getDimensionList();
		for (DimensionProperty definition : dimensionDefinitions) {
			Dimension dimension = new Dimension(definition.getCode());
			this.dimensionMap.put(definition.getCode(), dimension);
			this.dimensionMap.put(definition.getName(), dimension);
		}
	}

	private void createCubeMap() {
		this.cubeMap = new HashMap<String, Cube>();
		Collection<CubeProperty> cubeDefinitions = this.getDefinition().getCubeList();
		for (CubeProperty definition : cubeDefinitions) {
			Cube cube = new Cube(definition.getCode());
			this.cubeMap.put(definition.getCode(), cube);
			this.cubeMap.put(definition.getName(), cube);
		}
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "datastore");
		serializer.attribute("", "code", this.code);
		serializer.attribute("", "name", this.name);

		for (Cube cube : this.getCubes().values())
			cube.serializeToXML(serializer, depth);

		for (Dimension dimension : this.getDimensions().values())
			dimension.serializeToXML(serializer, depth);

		serializer.endTag("", "datastore");
	}

}
