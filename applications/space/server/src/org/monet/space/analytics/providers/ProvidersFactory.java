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

package org.monet.space.analytics.providers;

import org.monet.space.kernel.agents.AgentLogger;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class ProvidersFactory {
	private static ProvidersFactory instance;
	private HashMap<String, Object> providers;

	public static final String SUMUS = "sumus";
	public static final String GOOGLE = "google";

	private ProvidersFactory() {
		this.providers = new HashMap<String, Object>();
		this.register(ProvidersFactory.SUMUS, SumusProvider.class);
		this.register(ProvidersFactory.GOOGLE, GoogleProvider.class);
	}

	public synchronized static ProvidersFactory Instance() {
		if (instance == null)
			instance = new ProvidersFactory();
		return instance;
	}

	public Provider get(String key) {
		Class<?> renderClass;
		Provider provider = null;

		try {
			renderClass = (Class<?>) this.providers.get(key);
			Constructor<?> constructor = renderClass.getConstructor();
			provider = (Provider) constructor.newInstance();
		} catch (NullPointerException exception) {
			AgentLogger.getInstance().error("No provider found", exception);
			return null;
		} catch (Exception exception) {
			AgentLogger.getInstance().error("No provider found", exception);
			return null;
		}

		return provider;
	}

	public Boolean register(String code, Class<?> renderClass) throws IllegalArgumentException {

		if ((renderClass == null) || (code == null))
			return false;

		this.providers.put(code, renderClass);

		return true;
	}

}
