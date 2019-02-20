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

package org.monet.space.analytics.providers.renders;

import org.monet.space.kernel.agents.AgentLogger;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class RenderProvidersFactory {
	private static RenderProvidersFactory instance;
	private HashMap<String, Object> renders;

	public static final String SUMUS_CHART = "sumuschart";
	public static final String SUMUS_DOCUMENT = "sumusdocument";
	public static final String GOOGLE_CHART = "googlechart";
	public static final String GOOGLE_MAP_CHART = "googlemapchart";
	public static final String GOOGLE_DOCUMENT = "googledocument";

	private RenderProvidersFactory() {
		this.renders = new HashMap<String, Object>();
		this.register(RenderProvidersFactory.SUMUS_CHART, SumusChartProviderRender.class);
		this.register(RenderProvidersFactory.SUMUS_DOCUMENT, SumusDocumentProviderRender.class);
		this.register(RenderProvidersFactory.GOOGLE_CHART, GoogleChartProviderRender.class);
		this.register(RenderProvidersFactory.GOOGLE_MAP_CHART, GoogleMapChartProviderRender.class);
		this.register(RenderProvidersFactory.GOOGLE_DOCUMENT, GoogleDocumentProviderRender.class);
	}

	public synchronized static RenderProvidersFactory Instance() {
		if (instance == null)
			instance = new RenderProvidersFactory();
		return instance;
	}

	public DatawareHouseProviderRender get(String key) {
		Class<?> renderClass;
		DatawareHouseProviderRender render = null;

		try {
			renderClass = (Class<?>) this.renders.get(key);
			Constructor<?> constructor = renderClass.getConstructor();
			render = (DatawareHouseProviderRender) constructor.newInstance();
		} catch (NullPointerException exception) {
			AgentLogger.getInstance().error("No render found", exception);
			return null;
		} catch (Exception exception) {
			AgentLogger.getInstance().error("No render found", exception);
			return null;
		}

		return render;
	}

	public Boolean register(String code, Class<?> renderClass) throws IllegalArgumentException {

		if ((renderClass == null) || (code == null))
			return false;

		this.renders.put(code, renderClass);

		return true;
	}

}
