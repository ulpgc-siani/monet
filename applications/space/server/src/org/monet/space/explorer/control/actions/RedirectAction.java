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

package org.monet.space.explorer.control.actions;

import com.google.inject.Inject;
import org.monet.space.explorer.control.dialogs.RedirectDialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.kernel.agents.AgentLogger;

import java.io.File;
import java.io.IOException;

public class RedirectAction extends Action<RedirectDialog, Display> {
	private AgentLogger logger;

	@Inject
	public void inject(AgentLogger logger) {
		this.logger = logger;
	}

	public void execute() throws IOException {
		String page = dialog.getPage();

		try {

			if (page == null) {
				display.writeError(String.format(org.monet.space.explorer.model.Error.PAGE_NOT_FOUND, page));
				return;
			}

			String filename = configuration.getPageDir(page);
			File file = new File(filename);

			if (!file.exists()) {
				display.writeError(String.format(org.monet.space.explorer.model.Error.PAGE_NOT_FOUND, page));
			}

			dialog.dispatch(configuration.getPageUrl(page), display);

		} catch (RuntimeException exception) {
			logger.error(exception);
		}
	}

	@Override
	public boolean checkUserLogged() {
		return false;
	}

}