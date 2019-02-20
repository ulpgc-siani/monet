package org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders;

import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.FederationRender;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;

import java.util.HashMap;
import java.util.Properties;

public class RegisterRender extends FederationRender{
  
  public RegisterRender(Logger logger, Configuration configuration, String template){
    super(logger, configuration, template);
  }

  @Override
  public void init(String lang){
    this.lang = lang;
  }

  @Override
  protected void init() {
    Properties props = this.configuration.getLanguage(lang);
    String requestToken = this.getParameterAsString(Parameter.REQUEST_TOKEN);
    String baseUrl = this.getParameterAsString(Parameter.BASE_URL);
    String additionalMessage = this.getParameterAsString(Parameter.ADDITIONAL_MESSAGE);
    boolean showError = (Boolean)this.getParameter(Parameter.SHOW_ERROR);

    loadCanvas(TemplateParams.CANVAS_REGISTER);
    
    addMark(TemplateParams.ORGANITATION,      props.getProperty(TemplateComponent.ORGANIZATION));   
    addMark(TemplateParams.SIGNIN,            props.getProperty(TemplateComponent.SIGNIN));
    addMark(TemplateParams.ABOUT,             props.getProperty(TemplateComponent.ABOUT));
    addMark(TemplateParams.COPYRIGHT,         this.getCopyright(props));
    addMark(TemplateParams.PRIVACY,           props.getProperty(TemplateComponent.PRIVACY));
    addMark(TemplateParams.FULLNAME,          props.getProperty(TemplateComponent.FULLNAME));
    addMark(TemplateParams.PASSWORD,          props.getProperty(TemplateComponent.PASSWORD));
    addMark(TemplateParams.RPASSWORD,         props.getProperty(TemplateComponent.RPASSWORD));
    addMark(TemplateParams.REGISTER,          props.getProperty(TemplateComponent.REGISTER));
    addMark(TemplateParams.CANCEL,            props.getProperty(TemplateComponent.CANCEL));
    addMark(TemplateParams.USER,              props.getProperty(TemplateComponent.USER));
    addMark(TemplateParams.CLOSE,             props.getProperty(TemplateComponent.CLOSE));
    addMark(TemplateParams.REGISTER_TITLE,    props.getProperty(TemplateComponent.REGISTER_TITLE));
    addMark(TemplateParams.POWERED_BY,        props.getProperty(TemplateComponent.POWERED_BY));
    
    addMark(TemplateParams.ORGANIZATION_NAME,   props.getProperty(TemplateComponent.ORGANIZATION_NAME));
    addMark(TemplateParams.DEPARTMENT_NAME,     props.getProperty(TemplateComponent.DEPARTMENT_NAME));
    addMark(TemplateParams.CITY,                props.getProperty(TemplateComponent.CITY));
    addMark(TemplateParams.PROVINCE,            props.getProperty(TemplateComponent.PROVINCE));
    addMark(TemplateParams.COUNTRY,             props.getProperty(TemplateComponent.COUNTRY));
    
    addMark(TemplateParams.TOKEN,             requestToken);
    addMark(TemplateParams.LOGO,              baseUrl + "/accounts/authorization/resources/?id=logo");
    addMark(TemplateParams.URL_FEDERATION,    baseUrl);
    addMark(TemplateParams.ACTION,            baseUrl + "/accounts/authorization/?action=register");
    addMark(TemplateParams.ACTION_CANCEL,     baseUrl + "/accounts/authorization/home");

    addMark(TemplateParams.HOME, props.getProperty(TemplateComponent.HOME));
    addMark(TemplateParams.ACTION_HOME, baseUrl + "/accounts/authorization/home");

    if(showError)
      if (additionalMessage != null && additionalMessage.equals("errorExists"))
          addMark(TemplateParams.ERROR_EXISTS_USER, props.getProperty(TemplateComponent.ERROR_USER_EXISTS));
      else
        addMark(TemplateParams.ERROR_CREATE_USER, props.getProperty(TemplateComponent.ERROR_CREATE_USER));
    else
      addMark(TemplateParams.ERROR_CREATE_USER, "");

    this.addLanguagesMark(lang);
    
    HashMap<String, Object> methodsLogin= new HashMap<String, Object>();
    if(this.configuration.isCertificateActive() || this.configuration.isOpenIDActive()){
      if(this.configuration.isOpenIDActive())  methodsLogin.put(TemplateParams.OPEN_ID, props.getProperty(TemplateComponent.OPEN_ID));
      else  methodsLogin.put(TemplateParams.OPEN_ID, "");
      if(this.configuration.isCertificateActive()) methodsLogin.put(TemplateParams.CERTIFICATE,  props.getProperty(TemplateComponent.CERTIFICATE));
      else methodsLogin.put(TemplateParams.CERTIFICATE, "");
      methodsLogin.put(TemplateParams.USERNAME_PASSWORD, props.getProperty(TemplateComponent.USERNAME_PASSWORD));
    }
    String certificateAuthenticationBlock = this.configuration.isCertificateActive()?block(TemplateParams.BLOCK_CERTIFICATE_AUTHENTICATION, methodsLogin):"";
    addMark(TemplateParams.CERTIFICATE_AUTHENTICATION, certificateAuthenticationBlock);
    
    if(!showError && !additionalMessage.isEmpty())
      addMark(TemplateParams.REGISTER_SAVE,    props.getProperty(TemplateComponent.REGISTER_SAVE));
    else
      addMark(TemplateParams.REGISTER_SAVE,    "");
  }
}
