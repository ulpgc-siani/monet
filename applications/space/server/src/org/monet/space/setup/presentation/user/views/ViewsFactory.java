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

package org.monet.space.setup.presentation.user.views;

import org.monet.space.setup.core.constants.ErrorCode;
import org.monet.space.setup.presentation.user.constants.Views;
import org.monet.space.setup.presentation.user.util.Context;
import org.monet.space.kernel.exceptions.SystemException;

import java.lang.reflect.Constructor;
import java.util.HashMap;


public class ViewsFactory {

	private static ViewsFactory oInstance;
	private HashMap<String, Object> hmViews;
	private Context oContext;

	private ViewsFactory() {
		this.hmViews = new HashMap<String, Object>();
		this.oContext = new Context();
		this.registerViews();
	}

	private Boolean registerViews() {

		this.register(Views.LOADING, ViewLoading.class);
		this.register(Views.APPLICATION, ViewApplication.class);
		this.register(Views.LOGIN, ViewLogin.class);
		this.register(Views.ERROR, ViewError.class);

		return true;
	}

	public synchronized static ViewsFactory getInstance() {
		if (oInstance == null) oInstance = new ViewsFactory();
		return oInstance;
	}

	public View get(String sType, String codeLanguage) {
		Class<?> cView;
		View oView = null;

		try {
			cView = (Class<?>) this.hmViews.get(sType);
			Constructor<?> oConstructor = cView.getConstructor(Context.class, String.class);
			oView = (View) oConstructor.newInstance(this.oContext, codeLanguage);
		} catch (NullPointerException oException) {
			throw new SystemException(ErrorCode.VIEWS_FACTORY, sType, oException);
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.VIEWS_FACTORY, sType, oException);
		}

		return oView;
	}

	public Boolean register(String sType, Class<?> cView)
		throws IllegalArgumentException {

		if ((cView == null) || (sType == null)) {
			return false;
		}
		this.hmViews.put(sType, cView);

		return true;
	}

}
