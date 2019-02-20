package org.monet.federation.accountoffice.control.servlets.actions.impl;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.control.servlets.actions.Action;
import org.monet.federation.accountoffice.core.components.accountcomponent.SessionComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionChangePassword implements Action {
    private Logger logger;
    private TemplateComponent templateComponent;
    private SessionComponent sessionComponent;
    private AccountLayer accountLayer;
    private Configuration configuration;

    @Inject
    public ActionChangePassword(Logger logger, AccountLayer accountLayer, SessionComponent sessionComponent, TemplateComponent templateComponent, Configuration configuration) {
        this.logger = logger;
        this.templateComponent = templateComponent;
        this.sessionComponent = sessionComponent;
        this.accountLayer = accountLayer;
        this.configuration = configuration;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String path) {
        this.logger.debug("execute()");

        String baseUrl = Utils.getBaseUrl(request);
        String token = (String) request.getParameter("token");

        String username = (String) request.getParameter("username");
        String lang = (String) request.getSession().getAttribute("lang");
        if (lang == null)
            lang = "en";

        try {
            String oldPassword = (String) request.getParameter("oldPassword");
            String newPassword = (String) request.getParameter("newPassword");

            if (username == null || username.isEmpty()) {
                username = (String) request.getSession().getAttribute("nvUser");
                int tries = this.sessionComponent.getAuthTriesOfUser(username);
                this.templateComponent.createChangePasswordTemplate(response, path, true, token, lang, baseUrl, null);
            } else {
                request.getSession().setAttribute("lang", lang);

                String additionalMessage = "";
                if (!this.accountLayer.changePassword(username, oldPassword, newPassword))
                    additionalMessage = null;

                this.templateComponent.createChangePasswordTemplate(response, path, true, token, lang, baseUrl, additionalMessage);
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);

            this.templateComponent.createChangePasswordTemplate(response, path, true, token, lang, baseUrl, "errorUnknow");
        }
    }
}
