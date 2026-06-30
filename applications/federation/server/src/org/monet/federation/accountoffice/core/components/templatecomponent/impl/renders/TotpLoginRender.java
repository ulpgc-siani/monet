package org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders;

import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.FederationRender;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;

import java.util.HashMap;
import java.util.Properties;

public class TotpLoginRender extends FederationRender {
  public TotpLoginRender(Logger logger, Configuration configuration, String template) {
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
    boolean showSetupMode = (Boolean) this.getParameter(Parameter.SHOW_SETUP_MODE);
    String additionalMessage = this.getParameterAsString(Parameter.ADDITIONAL_MESSAGE);
    String secret = this.getParameterAsString(Parameter.SECRET);
    String otpAuthUri = this.getParameterAsString(Parameter.OTP_AUTH_URI);

    loadCanvas(TemplateParams.CANVAS_TOTP_LOGIN);

    addMark(TemplateParams.ORGANITATION, props.getProperty(TemplateComponent.ORGANIZATION));
    addMark(TemplateParams.COPYRIGHT, this.getCopyright(props));
    addMark(TemplateParams.POWERED_BY, props.getProperty(TemplateComponent.POWERED_BY));
    addMark(TemplateParams.HOME, props.getProperty(TemplateComponent.HOME));
    addMark(TemplateParams.ACTION_HOME, baseUrl + "/accounts/authorization/home");
    addMark(TemplateParams.SIGNIN, props.getProperty(TemplateComponent.SIGNIN));
    addMark(TemplateParams.TOTP_TITLE, props.getProperty(TemplateComponent.TOTP_TITLE));
    addMark(TemplateParams.TOTP_CODE, props.getProperty(TemplateComponent.TOTP_CODE));
    addMark(TemplateParams.TOTP_SECRET, props.getProperty(TemplateComponent.TOTP_SECRET));
    addMark(TemplateParams.TOTP_URI, props.getProperty(TemplateComponent.TOTP_URI));
    addMark(TemplateParams.TOTP_SETUP_MESSAGE, props.getProperty(TemplateComponent.TOTP_SETUP_MESSAGE));
    addMark(TemplateParams.TOTP_VERIFICATION_MESSAGE, props.getProperty(TemplateComponent.TOTP_VERIFICATION_MESSAGE));
    addMark(TemplateParams.TOKEN, requestToken);
    addMark(TemplateParams.LOGO, baseUrl + "/accounts/authorization/resources/?id=logo");
    addMark(TemplateParams.URL_FEDERATION, baseUrl);
    addMark(TemplateParams.ACTION, baseUrl + "/accounts/authorization/?action=login&oauth_token=" + requestToken);
    addMark(TemplateParams.SECRET, secret != null ? secret : "");
    addMark(TemplateParams.OTP_AUTH_URI, otpAuthUri != null ? otpAuthUri : "");
    addMark(TemplateParams.TOTP_QR_URL, baseUrl + "/accounts/authorization/resources/?id=totpqr");

    if (additionalMessage != null && !additionalMessage.isEmpty())
      addMark(TemplateParams.LOGIN_MESSAGE, "<div class=\"error login\">" + props.getProperty(additionalMessage, additionalMessage) + "</div>");
    else
      addMark(TemplateParams.LOGIN_MESSAGE, "");

    addMark(TemplateParams.TOTP_MODE_MESSAGE, showSetupMode ? props.getProperty(TemplateComponent.TOTP_SETUP_MESSAGE) : props.getProperty(TemplateComponent.TOTP_VERIFICATION_MESSAGE));

    String totpSetupBlock = "";
    if (showSetupMode) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put(TemplateParams.TOTP_SECRET, props.getProperty(TemplateComponent.TOTP_SECRET));
      map.put(TemplateParams.TOTP_URI, props.getProperty(TemplateComponent.TOTP_URI));
      map.put(TemplateParams.SECRET, secret != null ? secret : "");
      map.put(TemplateParams.OTP_AUTH_URI, otpAuthUri != null ? otpAuthUri : "");
      map.put(TemplateParams.TOTP_QR_URL, baseUrl + "/accounts/authorization/resources/?id=totpqr");
      totpSetupBlock = block(TemplateParams.BLOCK_TOTP_SETUP, map);
    }
    addMark(TemplateParams.TOTP_SETUP_BLOCK, totpSetupBlock);

    this.addLanguagesMark(lang);
  }
}
