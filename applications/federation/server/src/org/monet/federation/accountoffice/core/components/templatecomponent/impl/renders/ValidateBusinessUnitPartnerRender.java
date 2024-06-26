package org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders;

import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.FederationRender;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.BusinessUnit;

import java.util.HashMap;
import java.util.Properties;

public class ValidateBusinessUnitPartnerRender extends FederationRender {
  
  public ValidateBusinessUnitPartnerRender(Logger logger, Configuration configuration, String template){
    super(logger, configuration, template);
  }

  @Override
  public void init(String lang){
    this.lang = lang;
  }

  @Override
  protected void init() {
    Properties props = this.configuration.getLanguage(lang);
    BusinessUnit businessUnit = (BusinessUnit)this.getParameter(Parameter.BUSINESS_UNIT);
    String baseUrl = this.getParameterAsString(Parameter.BASE_URL);
    Boolean validationResult = (Boolean)this.getParameter(Parameter.RESULT);
    String subtitle = props.getProperty(TemplateComponent.BUSINESS_UNIT_PARTNER_REQUEST_SUBTITLE);

    loadCanvas(TemplateParams.CANVAS_VALIDATE_BUSINESS_UNIT_PARTNER);
    
    subtitle = subtitle.replace("::label::", businessUnit.getLabel());
    
    addMark(TemplateParams.ORGANITATION, props.getProperty(TemplateComponent.ORGANIZATION));
    addMark(TemplateParams.URL_FEDERATION, baseUrl);
    addMark(TemplateParams.LOGO, baseUrl + "/accounts/authorization/resources/?id=logo");
    addMark(TemplateParams.ACTION, baseUrl + "/businessunit/requests/?business_unit=" + businessUnit.getId() + "&lang=" + this.lang);
    addMark(TemplateParams.BUSINESS_UNIT_PARTNER_REQUEST, props.getProperty(TemplateComponent.BUSINESS_UNIT_PARTNER_REQUEST));
    addMark(TemplateParams.BUSINESS_UNIT_PARTNER_REQUEST_SUBTITLE, subtitle);
    addMark(TemplateParams.POWERED_BY, props.getProperty(TemplateComponent.POWERED_BY));
    addMark(TemplateParams.COPYRIGHT, this.getCopyright(props));
    addMark(TemplateParams.HOME, props.getProperty(TemplateComponent.HOME));
    addMark(TemplateParams.ACTION_HOME, baseUrl + "/accounts/authorization/home");

    String blockName = "content";
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put(TemplateParams.VALIDATION_CODE_TITLE, props.getProperty(TemplateComponent.VALIDATION_CODE_TITLE));
    map.put("send", props.getProperty(TemplateComponent.SEND));
    if (validationResult != null) {
      if (validationResult == true) {
        blockName = "content.success";
        map.put("result", props.getProperty(TemplateComponent.BUSINESS_UNIT_PARTNER_VALIDATION_SUCCESS));
      }
      else {
        blockName = "content.failure";
        map.put("result", props.getProperty(TemplateComponent.BUSINESS_UNIT_PARTNER_VALIDATION_FAILURE));
      }
    }
    else
      map.put("validationCodeTitle", props.getProperty(TemplateComponent.VALIDATION_CODE_TITLE));
    
    addMark("content", block(blockName, map));
    
    this.addLanguagesMark(lang);
  }
}
