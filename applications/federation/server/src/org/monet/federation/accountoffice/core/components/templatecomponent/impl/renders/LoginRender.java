package org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders;

import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.FederationRender;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.NoAuthenticatedUser;

import java.util.HashMap;
import java.util.Properties;

public class LoginRender extends FederationRender {

    public LoginRender(Logger logger, Configuration configuration, String template) {
        super(logger, configuration, template);
    }

    @Override
    public void init(String lang) {
        this.lang = lang;
    }

    @Override
    protected void init() {
        Properties props = this.configuration.getLanguage(lang);
        String requestToken = this.getParameterAsString(Parameter.REQUEST_TOKEN);
        String baseUrl = this.getParameterAsString(Parameter.BASE_URL);
        boolean showError = (Boolean) this.getParameter(Parameter.SHOW_ERROR);
        boolean showCaptcha = (Boolean) this.getParameter(Parameter.SHOW_CAPTCHA);
        Integer retries = (Integer) this.getParameter(Parameter.RETRIES);

        loadCanvas(TemplateParams.CANVAS_LOGIN);

        addMark(TemplateParams.ORGANITATION, props.getProperty(TemplateComponent.ORGANIZATION));
        addMark(TemplateParams.REMEMBERME, props.getProperty(TemplateComponent.REMEMBERME));
        addMark(TemplateParams.SIGNIN, props.getProperty(TemplateComponent.SIGNIN));
        addMark(TemplateParams.ABOUT, props.getProperty(TemplateComponent.ABOUT));
        addMark(TemplateParams.COPYRIGHT, this.getCopyright(props));
        addMark(TemplateParams.PRIVACY, props.getProperty(TemplateComponent.PRIVACY));
        addMark(TemplateParams.PASSWORD, props.getProperty(TemplateComponent.PASSWORD));
        addMark(TemplateParams.USER, props.getProperty(TemplateComponent.USER));
        addMark(TemplateParams.CLOSE, props.getProperty(TemplateComponent.CLOSE));
        addMark(TemplateParams.POWERED_BY, props.getProperty(TemplateComponent.POWERED_BY));
        addMark(TemplateParams.LOGIN_TITLE, props.getProperty(TemplateComponent.LOGIN_TITLE));
        addMark(TemplateParams.TOKEN, requestToken);
        addMark(TemplateParams.LOGO, baseUrl + "/accounts/authorization/resources/?id=logo");
        addMark(TemplateParams.URL_FEDERATION, baseUrl);            //requestURL
        addMark(TemplateParams.ACTION, baseUrl + "/accounts/authorization/?action=login&oauth_token=" + requestToken);
        addMark(TemplateParams.ACTION_RESET_PASSWORD, baseUrl + "/accounts/authorization/?view=resetPassword&oauth_token=" + requestToken);
        addMark(TemplateParams.ACTION_CHANGE_PASSWORD, baseUrl + "/accounts/authorization/?view=changePassword&oauth_token=" + requestToken);
        addMark(TemplateParams.RESET_PASSWORD, props.getProperty(TemplateComponent.RESET_PASSWORD));
        addMark(TemplateParams.CHANGE_PASSWORD, props.getProperty(TemplateComponent.CHANGE_PASSWORD));
        addMark(TemplateParams.HOME, props.getProperty(TemplateComponent.HOME));
        addMark(TemplateParams.ACTION_HOME, baseUrl + "/accounts/authorization/home");

        String register = "";
        if (this.configuration.isRegisterEnable()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(TemplateParams.REGISTER_MESSAGE, props.getProperty(TemplateComponent.REGISTER_MESSAGE));
            map.put(TemplateParams.ORGANITATION, props.getProperty(TemplateComponent.ORGANIZATION));
            map.put(TemplateParams.REGISTER, props.getProperty(TemplateComponent.REGISTER));
            map.put(TemplateParams.ACTION_REGISTER, baseUrl + "/accounts/authorization/?view=register&oauth_token=" + requestToken);
            register = block(TemplateParams.BLOCK_REGISTER, map);
        }

        addMark(TemplateParams.REGISTER, register);

        if (showError)
            addMark(TemplateParams.USER_NOT_FOUND, props.getProperty(TemplateComponent.USER_NOT_FOUND));
        else
            addMark(TemplateParams.USER_NOT_FOUND, "");

        this.addLanguagesMark(lang);

        String captcha = "";
        if (showCaptcha) {
            HashMap<String, Object> mapCaptcha = new HashMap<String, Object>();
            mapCaptcha.put(TemplateParams.CAPTCHA, baseUrl + "/accounts/authorization/resources/?id=captcha");
            captcha = block(TemplateParams.BLOCK_CAPTCHA, mapCaptcha);
        }
        addMark(TemplateParams.SHOW_CAPTCHA, captcha);

        String retriesBlock = "";
        if (retries > 0) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(TemplateParams.COUNT, retries);
            map.put(TemplateParams.TIME, NoAuthenticatedUser.SUSPEND_TIME / 1000);
            map.put(TemplateParams.RETRIES, retries > 1 ? props.getProperty(TemplateComponent.RETRIES_MULTIPLE) : props.getProperty(TemplateComponent.RETRIES));
            map.put(TemplateParams.RETRIES_SUSPENDED, props.getProperty(TemplateComponent.RETRIES_SUSPENDED));
            map.put(TemplateParams.SECONDS, props.getProperty(TemplateComponent.SECONDS));
            map.put(TemplateParams.SUSPENDED, retries > NoAuthenticatedUser.MAX_RETRIES?block(TemplateParams.BLOCK_RETRIES_SUSPENDED, map):"");
            retriesBlock = block(TemplateParams.BLOCK_RETRIES, map);
        }
        addMark(TemplateParams.RETRIES, retriesBlock);

        String applet = "";
        if (this.configuration.isCertificateActive()) {
            HashMap<String, Object> mapApplet = new HashMap<String, Object>();
            mapApplet.put(TemplateParams.URL_FEDERATION, baseUrl);
            applet = block(TemplateParams.BLOCK_APPLET, mapApplet);
        }
        addMark(TemplateParams.APPLET, applet);

        String googleLogo = "";
        String yahooLogo = "";
        String otherProvider = "";
        if (this.configuration.isOpenIDActive()) {
            googleLogo = baseUrl + "/accountoffice/images/google.gif";
            yahooLogo = baseUrl + "/accountoffice/images/yahoo.gif";
            otherProvider = props.getProperty(TemplateComponent.OTHER_PROVIDER);
        }
        addMark(TemplateParams.LOGO_GOOGLE_PROVIDER, googleLogo);
        addMark(TemplateParams.LOGO_YAHOO_PROVIDER, yahooLogo);
        addMark(TemplateParams.OTHER_PROVIDER, otherProvider);

        HashMap<String, Object> methodsLogin = new HashMap<String, Object>();
        if (this.configuration.isCertificateActive() || this.configuration.isOpenIDActive()) {
            if (this.configuration.isOpenIDActive())
                methodsLogin.put(TemplateParams.OPEN_ID, props.getProperty(TemplateComponent.OPEN_ID));
            else methodsLogin.put(TemplateParams.OPEN_ID, "");
            if (this.configuration.isCertificateActive())
                methodsLogin.put(TemplateParams.CERTIFICATE, props.getProperty(TemplateComponent.CERTIFICATE));
            else methodsLogin.put(TemplateParams.CERTIFICATE, "");
            methodsLogin.put(TemplateParams.USERNAME_PASSWORD, props.getProperty(TemplateComponent.USERNAME_PASSWORD));
        }
        String certificateAuthenticationBlock = this.configuration.isCertificateActive() ? block(TemplateParams.BLOCK_CERTIFICATE_AUTHENTICATION, methodsLogin) : "";
        addMark(TemplateParams.CERTIFICATE_AUTHENTICATION, certificateAuthenticationBlock);


    }

}
