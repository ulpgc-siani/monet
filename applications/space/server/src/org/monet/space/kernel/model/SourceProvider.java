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

import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;

public class SourceProvider implements SourceLink {
	private SourceLayer sourceLayer;

	private static SourceProvider instance;

	private SourceProvider() {
		this.sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
	}

	public synchronized static SourceProvider getInstance() {
		if (instance == null) instance = new SourceProvider();
		return instance;
	}

	@Override
	public TermList loadTerms(Source<SourceDefinition> source, DataRequest dataRequest, boolean onlyEnabled) {
		return this.sourceLayer.loadSourceTerms(source, dataRequest, onlyEnabled);
	}

	@Override
	public TermList loadTerms(Source<SourceDefinition> source, boolean onlyEnabled) {
		return this.sourceLayer.loadSourceTerms(source, onlyEnabled);
	}

	@Override
	public TermList searchTerms(Source<SourceDefinition> source, DataRequest dataRequest) {
		return this.sourceLayer.searchSourceTerms(source, dataRequest);
	}

	@Override
	public String locateTermCode(Source<SourceDefinition> source, String value) {
		return this.sourceLayer.locateSourceTermCode(source, value);
	}

	@Override
	public String locateTermValue(Source<SourceDefinition> source, String code) {
		return this.sourceLayer.locateSourceTermValue(source, code);
	}

	@Override
	public void populate(Source<SourceDefinition> source) {
		this.sourceLayer.populateSource(source);
	}

}