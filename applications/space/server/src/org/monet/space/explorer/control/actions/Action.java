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
import org.monet.space.explorer.ApplicationExplorer;
import org.monet.space.explorer.configuration.Configuration;
import org.monet.space.explorer.control.dialogs.Dialog;
import org.monet.space.explorer.control.dialogs.HttpDialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.explorer.model.ComponentProvider;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Session;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public abstract class Action<Dlg extends Dialog, Dsp extends Display> {
	private AgentSession agentSession;
	protected ComponentProvider componentProvider;
	protected String idSession;
	protected String language;
	protected Configuration configuration;
	protected Dlg dialog;
	protected Dsp display;

	public Action() {
		this.language = null;
		this.dialog = null;
		this.display = null;
	}

	@Inject
	public void inject(Configuration configuration) {
		this.configuration = configuration;
	}

	@Inject
	public void inject(AgentSession agentSession) {
		this.agentSession = agentSession;
	}

	@Inject
	public void inject(ComponentProvider componentProvider) {
		this.componentProvider = componentProvider;
	}

	public void inject(Dlg dialog) {
		this.dialog = dialog;
		this.idSession = dialog.getSessionId();
	}

	public void inject(Dsp display) {
		this.display = display;
	}

	public Account getAccount() {
		return this.getFederationLayer().loadAccount();
	}

	public void init() {
		display.setContentType("text/html;charset=UTF-8");

		this.initLanguage();
	}

	public abstract void execute() throws IOException;

	public boolean checkUserLogged() {
		return true;
	}

	protected Boolean initLanguage() {
		Session session = agentSession.get(idSession);

		this.language = null;
		if (session != null) this.language = this.getFederationLayer().getUserLanguage();
		if (this.language == null) this.language = dialog.getLanguage();

		return true;
	}

	protected FederationLayer getFederationLayer() {
		return componentProvider.getComponentFederation().getLayer(new FederationLayer.Configuration() {
			@Override
			public String getSessionId() {
				return dialog.getSessionId();
			}

			@Override
			public String getCallbackUrl() {
				return ApplicationExplorer.getCallbackUrl();
			}

			@Override
			public String getLogoUrl() {
				return Configuration.getInstance().getFederationLogoUrl();
			}

			@Override
			public HttpServletRequest getRequest() {
				return ((HttpDialog)dialog).getRequest();
			}
		});
	}

}