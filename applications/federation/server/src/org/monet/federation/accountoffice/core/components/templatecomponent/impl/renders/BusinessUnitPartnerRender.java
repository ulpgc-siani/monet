package org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders;

import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.FederationRender;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.BusinessUnit;

import java.net.MalformedURLException;
import java.util.Properties;

public class BusinessUnitPartnerRender extends FederationRender {
  
  public BusinessUnitPartnerRender(Logger logger, Configuration configuration, String template){
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
    String code = this.getParameterAsString(Parameter.CODE);
    String message = props.getProperty(TemplateComponent.BUSINESS_UNIT_PARTNER_REQUEST_MESSAGE);
    String label = businessUnit.getLabel();
    String responseUrl = baseUrl + "/businessunit/requests/?business_unit=" + businessUnit.getId();

    loadCanvas(TemplateParams.CANVAS_BUSINESS_UNIT_PARTNER);
    
    try {
      message = message.replace("::label::", label);
      message = message.replace("::code::", code);
      message = message.replace("::responseUrl::", responseUrl);
      
      addMark(TemplateParams.BUSINESS_UNIT_PARTNER_REQUEST, props.getProperty(TemplateComponent.BUSINESS_UNIT_PARTNER_REQUEST));
      addMark(TemplateParams.BUSINESS_UNIT_PARTNER_REQUEST_MESSAGE, message);
      addMark(TemplateParams.BUSINESS_UNIT_PARTNER_INFO, props.getProperty(TemplateComponent.BUSINESS_UNIT_PARTNER_INFO));
      addMark(TemplateParams.BUSINESS_UNIT_PARTNER_NAME, props.getProperty(TemplateComponent.BUSINESS_UNIT_PARTNER_NAME));
      addMark(TemplateParams.BUSINESS_UNIT_PARTNER_LABEL, props.getProperty(TemplateComponent.BUSINESS_UNIT_PARTNER_LABEL));
      addMark(TemplateParams.BUSINESS_UNIT_PARTNER_URL, props.getProperty(TemplateComponent.BUSINESS_UNIT_PARTNER_URL));
      
      addMark(TemplateParams.CODE, code);
      addMark(TemplateParams.NAME, businessUnit.getName());
      addMark(TemplateParams.LABEL, label);
      addMark(TemplateParams.RESPONSE_URL, responseUrl);
      addMark(TemplateParams.URL, businessUnit.getUri().toURL().toString());
    } catch (MalformedURLException e) {
      frLogger.error("business unit render", e);
    }

    this.addLanguagesMark(lang);
    
  }
}
