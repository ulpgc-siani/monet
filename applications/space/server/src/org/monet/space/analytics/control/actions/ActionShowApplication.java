package org.monet.space.analytics.control.actions;

import org.monet.space.analytics.constants.Parameter;
import org.monet.space.analytics.control.constants.Commands;
import org.monet.space.analytics.renders.*;
import org.monet.space.analytics.utils.AjaxCommandUtil;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.applications.library.LibraryResponse;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Task;

public class ActionShowApplication extends Action {

	public ActionShowApplication() {
		super();
	}

	@Override
	public String execute() {
		String action = this.getParameterAsString(Parameter.ACTION);
		String verifier = this.getParameterAsString(Parameter.VERIFIER);
		String message = null;
		ApplicationRender applicationRender;
		LogoutRender logoutRender;
		UpdatingSpaceRender updatingSpaceRender;
		Account account;
		FederationLayer layer = this.getFederationLayer();
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		RendersFactory rendersFactory = RendersFactory.getInstance();

		if (action != null && action.equals(Actions.LOGIN)) {
			layer.login(verifier);
			LibraryResponse.sendRedirect(this.getResponse(), LibraryRequest.getRequestURL(this.getRequest()));
			return null;
		}

		if (layer.isLogged()) {
			account = layer.loadAccount();

			if (!account.hasPermissions() || account.getRootDashboard() == null) {
				logoutRender = (LogoutRender) rendersFactory.get(RendersFactory.RENDER_LOGOUT);
				logoutRender.setTarget(message);
				return logoutRender.getOutput();
			}

			Task currentInitializerTask = taskLayer.getCurrentInitializerTask();
			if (currentInitializerTask != null && !account.canResolveInitializerTask(currentInitializerTask)) {
				updatingSpaceRender = (UpdatingSpaceRender) rendersFactory.get(RendersFactory.RENDER_UPDATING_SPACE);
				updatingSpaceRender.setBusinessUnit(BusinessUnit.getInstance());
				updatingSpaceRender.setTarget(message);
				return updatingSpaceRender.getOutput();
			}

			applicationRender = (ApplicationRender) rendersFactory.get(RendersFactory.RENDER_APPLICATION);
			applicationRender.setParameter(DatawareHouseRender.Parameter.COMMAND, AjaxCommandUtil.constructCommand(Commands.SHOW_HOME, new String[0]));
			return applicationRender.getOutput();
		} else {
			String authUrl = layer.getAuthorizationUrl();
			authUrl += (authUrl.indexOf("?") != -1) ? "&" : "?";
			authUrl += this.getRequest().getQueryString();
			LibraryResponse.sendRedirect(this.getResponse(), authUrl);
		}

		return null;
	}

}
