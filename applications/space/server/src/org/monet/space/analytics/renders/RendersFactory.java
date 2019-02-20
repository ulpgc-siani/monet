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

package org.monet.space.analytics.renders;

import org.monet.space.kernel.agents.AgentLogger;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class RendersFactory {
	private static RendersFactory instance;
	private HashMap<String, Object> renders;

	public static final String RENDER_HOME = "home";
	public static final String RENDER_OPERATION_NOT_FOUND = "operationnotfound";
	public static final String RENDER_APPLICATION = "application";
	public static final String RENDER_DASHBOARD_PAGE = "dashboardpage";
	public static final String RENDER_DASHBOARD_VIEW = "dashboardview";
	public static final String RENDER_DASHBOARD_MODEL = "dashboardmodel";
	public static final String RENDER_DATA_RESULT = "dataresult";
	public static final String RENDER_LOGOUT = "logout";
	public static final String RENDER_UPDATING_SPACE = "updatingspace";

	private RendersFactory() {
		this.renders = new HashMap<String, Object>();
		this.register(RendersFactory.RENDER_HOME, HomeRender.class);
		this.register(RendersFactory.RENDER_APPLICATION, ApplicationRender.class);
		this.register(RendersFactory.RENDER_DASHBOARD_MODEL, DashboardModelRender.class);
		this.register(RendersFactory.RENDER_LOGOUT, LogoutRender.class);
		this.register(RendersFactory.RENDER_UPDATING_SPACE, UpdatingSpaceRender.class);
//    this.register(RendersFactory.RENDER_OPERATION_NOT_FOUND, OperationNotFoundRender.class);
//    this.register(RendersFactory.RENDER_CUBE_VIEW_CHARTS_API, CubeViewChartsApiRender.class);
//    this.register(RendersFactory.RENDER_DATA_RESULT, DataResultRender.class);
	}

	public synchronized static RendersFactory getInstance() {
		if (instance == null)
			instance = new RendersFactory();
		return instance;
	}

	public DatawareHouseRender get(String key) {
		Class<?> renderClass;
		DatawareHouseRender render = null;

		try {
			renderClass = (Class<?>) this.renders.get(key);
			Constructor<?> constructor = renderClass.getConstructor();
			render = (DatawareHouseRender) constructor.newInstance();
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
