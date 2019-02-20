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
import org.monet.space.explorer.control.RendersFactory;
import org.monet.space.explorer.control.dialogs.HomeDialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.explorer.control.renders.renders.HomeRender;
import org.monet.space.explorer.control.renders.renders.LogoutRender;
import org.monet.space.explorer.control.renders.renders.UpdatingSpaceRender;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Task;

import java.io.IOException;

public class HomeAction extends Action<HomeDialog, Display> {
	private LayerProvider layerProvider;
	private RendersFactory rendersFactory;

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	@Inject
	public void inject(RendersFactory rendersFactory) {
		this.rendersFactory = rendersFactory;
	}

	public void execute() throws IOException {
		FederationLayer layer = getFederationLayer();
		Account account;

		if (dialog.isLoginOperation()) {
			layer.login(dialog.getVerifier());
			display.redirectTo(dialog.getUrl());
			return;
		}

		if (!layer.isLogged()) {
			redirectToFederation(layer);
			return;
		}

		account = layer.loadAccount();
		if (!account.hasPermissions()) {
			LogoutRender render = rendersFactory.create(LogoutRender.NAME);
			render.execute(display.getWriter());
			return;
		}

		Task currentInitializerTask = layerProvider.getTaskLayer().getCurrentInitializerTask();
		if (currentInitializerTask != null && !account.canResolveInitializerTask(currentInitializerTask)) {
			UpdatingSpaceRender render = rendersFactory.create(UpdatingSpaceRender.NAME);
			render.execute(display.getWriter());
			return;
		}

		HomeRender homeRender = rendersFactory.create(HomeRender.NAME);
		homeRender.execute(display.getWriter());
	}

	@Override
	public boolean checkUserLogged() {
		return false;
	}

	private void redirectToFederation(FederationLayer layer) {
		String authUrl = layer.getAuthorizationUrl();
		authUrl += (authUrl.indexOf("?") != -1) ? "&" : "?";
		authUrl += dialog.getQueryString();
		display.redirectTo(authUrl);
	}

}