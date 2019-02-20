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

package org.monet.space.frontservice.presentation.user.renders;

import org.monet.space.frontservice.core.constants.ErrorCode;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.ServiceList;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class RendersFactory {
	private static RendersFactory instance;
	private HashMap<String, Object> renders;

	private RendersFactory() {
		this.renders = new HashMap<String, Object>();
		this.register("servicelist", ServiceListRender.class);
	}

	public synchronized static RendersFactory getInstance() {
		if (instance == null) instance = new RendersFactory();
		return instance;
	}

	public FrontserviceRender get(Object object, String template) {
		Class<?> renderClass;
		FrontserviceRender render = null;
		String code = "";

		if (object instanceof ServiceList) code = "servicelist";

		if (code.isEmpty()) return null;

		try {
			renderClass = (Class<?>) this.renders.get(code);
			Constructor<?> constructor = renderClass.getConstructor();
			render = (FrontserviceRender) constructor.newInstance();
			render.setTarget(object);
			render.setTemplate(template);
		} catch (NullPointerException oException) {
			throw new SystemException(ErrorCode.RENDERS_FACTORY, code, oException);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.RENDERS_FACTORY, code, oException);
		}

		return render;
	}

	public Boolean register(String code, Class<?> renderClass) throws IllegalArgumentException {

		if ((renderClass == null) || (code == null)) {
			return false;
		}
		this.renders.put(code, renderClass);

		return true;
	}

}
