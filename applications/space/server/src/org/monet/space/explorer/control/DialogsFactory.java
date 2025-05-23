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

package org.monet.space.explorer.control;

import com.google.inject.Inject;
import org.monet.space.explorer.control.actions.Action;
import org.monet.space.explorer.control.dialogs.Dialog;
import org.monet.space.explorer.control.dialogs.HttpDialog;
import org.monet.space.explorer.injection.InjectorFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class DialogsFactory {

	@Inject
	protected Map<Class<? extends Action>, Class<? extends Dialog>> dialogsMap;

	public Dialog create(Class<? extends Action> action, HttpServletRequest request) {
        HttpDialog dialog = (HttpDialog)InjectorFactory.get().getInstance(this.dialogsMap.get(action));
        dialog.inject(request);
		return dialog;
	}

}
