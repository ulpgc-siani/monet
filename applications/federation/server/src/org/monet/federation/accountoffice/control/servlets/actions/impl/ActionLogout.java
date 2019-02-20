package org.monet.federation.accountoffice.control.servlets.actions.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.control.servlets.actions.Action;
import org.monet.federation.accountoffice.core.components.accountcomponent.SessionComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ActionLogout implements Action {
	private Logger logger;
	private SessionComponent sessionComponent;

	@Inject
	public void injectLogger(Logger logger) {
		this.logger = logger;
	}

	@Inject
	public void injectAccountComponent(SessionComponent sessionComponent, Configuration configuration) {
		this.sessionComponent = sessionComponent;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String path) {

		try {
			logger.info("ActionLogout.execute()");

			String user = (String) request.getSession().getAttribute("user");
			logger.info("User %s logged out", user);

			this.sessionComponent.deleteUser(user);
			HttpSession session = request.getSession();
            session.invalidate();

			String baseUrl = Utils.getBaseUrl(request);
			Utils.sendRedirect(response, baseUrl + "/accounts/authorization/");
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return;
	}
}
