package org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders;

import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.FederationRender;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;

import java.util.Properties;

public class ResetPasswordRender extends FederationRender{
  
  public ResetPasswordRender(Logger logger, Configuration configuration, String template){
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
    String additionalMessage = (String)this.getParameter(Parameter.ADDITIONAL_MESSAGE);
    boolean showError = (Boolean)this.getParameter(Parameter.SHOW_ERROR);

    loadCanvas(TemplateParams.CANVAS_RESET_PASSWORD);
    
    addMark(TemplateParams.ORGANITATION,          props.getProperty(TemplateComponent.ORGANIZATION));   
    addMark(TemplateParams.ABOUT,                 props.getProperty(TemplateComponent.ABOUT));
    addMark(TemplateParams.COPYRIGHT,             this.getCopyright(props));
    addMark(TemplateParams.PRIVACY,               props.getProperty(TemplateComponent.PRIVACY));
    addMark(TemplateParams.CANCEL,                props.getProperty(TemplateComponent.CANCEL));
    addMark(TemplateParams.RESET_PASSWORD,        props.getProperty(TemplateComponent.RESET_PASSWORD));
    addMark(TemplateParams.CLOSE,                 props.getProperty(TemplateComponent.CLOSE));
    addMark(TemplateParams.RESET_PASSWORD_TITLE,  props.getProperty(TemplateComponent.RESET_PASSWORD_TITLE));
    addMark(TemplateParams.RESET_PASSWORD_SEND,   props.getProperty(TemplateComponent.RESET_PASSWORD_SEND));
    addMark(TemplateParams.POWERED_BY,            props.getProperty(TemplateComponent.POWERED_BY));
    addMark(TemplateParams.EMAIL,            "Email:");
    
    addMark(TemplateParams.TOKEN,             requestToken);
    addMark(TemplateParams.LOGO,              baseUrl + "/accounts/authorization/resources/?id=logo");
    addMark(TemplateParams.URL_FEDERATION,    baseUrl);
    addMark(TemplateParams.ACTION,            baseUrl + "/accounts/authorization/?action=resetpassword");
    addMark(TemplateParams.ACTION_CANCEL,     baseUrl + "/accounts/authorization/");

    addMark(TemplateParams.HOME, props.getProperty(TemplateComponent.HOME));
    addMark(TemplateParams.ACTION_HOME, baseUrl + "/accounts/authorization/home");

    if(showError){
      String result;
      if(additionalMessage != null){
        if(additionalMessage.equals("errorUnknow"))
          result = props.getProperty(TemplateComponent.ERROR);
        else  
          result = props.getProperty(TemplateComponent.SUCCESSFULLY_RESET_PASSWORD);
      }
      else 
        result = props.getProperty(TemplateComponent.ERROR_RESET_PASSWORD);
      addMark(TemplateParams.RESET_PASSWORD_RESULT,    result);
    }else
      addMark(TemplateParams.RESET_PASSWORD_RESULT,    "");
    
   
    this.addLanguagesMark(lang);
  }
}
