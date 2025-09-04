package org.monet.federation.accountoffice.control.servlets.actions.impl;

import com.google.inject.Inject;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.monet.federation.accountoffice.control.servlets.actions.Action;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.utils.PasswordGenerator;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
import java.util.Random;

public class ActionResetPassword implements Action {
    private Logger logger;
    private TemplateComponent templateComponent;
    private Configuration configuration;
    private AccountLayer accountLayer;

    @Inject
    public ActionResetPassword(Logger logger, AccountLayer accountLayer, Configuration configuration, TemplateComponent templateComponent) {
        this.logger = logger;
        this.templateComponent = templateComponent;
        this.configuration = configuration;
        this.accountLayer = accountLayer;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String path) {
        this.logger.info("ActionResetPassword.execute()");

        String baseUrl = Utils.getBaseUrl(request);
        String lang = (String) request.getSession().getAttribute("lang");
        String token = (String) request.getParameter("token");
        if (lang == null)
            lang = "en";

        try {
            String email = (String) request.getParameter("email");
            String userId = this.accountLayer.loadFromEmail(email).getId();

            if (userId == null) {
                this.templateComponent.createResetPasswordTemplate(response, path, true, token, lang, baseUrl, null);
                return;
            }

            String newPassword = PasswordGenerator.generatePassword();
            SimpleEmail simpleMail = new SimpleEmail();
            simpleMail.setHostName(this.configuration.getProperty(Configuration.SMTP_HOSTNAME));
            simpleMail.setSmtpPort(Integer.valueOf(this.configuration.getProperty(Configuration.SMTP_PORT)));
            simpleMail.setAuthentication(this.configuration.getProperty(Configuration.SMTP_USER), this.configuration.getProperty(Configuration.SMTP_PASS));
            simpleMail.setTLS(Boolean.valueOf(this.configuration.getProperty(Configuration.SMTP_USE_TLS)));

            simpleMail.setFrom(this.configuration.getProperty(Configuration.SMTP_EMAIL_FROM));
            simpleMail.addTo(email);

            Properties props = this.configuration.getLanguage(lang);
            simpleMail.setSubject(props.getProperty(TemplateComponent.RESET_PASSWORD_TITLE));
            simpleMail.setMsg(props.getProperty(TemplateComponent.RESET_PASSWORD_MESSAGE) + "\n" + new String(newPassword));
            simpleMail.send();

            String additionalMessage = "";
            if (!this.accountLayer.resetPassword(userId, new String(newPassword)))
                additionalMessage = null;

            this.templateComponent.createResetPasswordTemplate(response, path, true, token, lang, baseUrl, additionalMessage);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            this.templateComponent.createResetPasswordTemplate(response, path, true, token, lang, baseUrl, "errorUnknow");
        }
    }
}
