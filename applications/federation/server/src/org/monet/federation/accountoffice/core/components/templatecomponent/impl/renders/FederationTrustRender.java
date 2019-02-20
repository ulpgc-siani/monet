package org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders;

import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.FederationRender;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.accountoffice.core.model.Federation;

import java.net.MalformedURLException;
import java.util.Properties;

public class FederationTrustRender extends FederationRender {
  
  public FederationTrustRender(Logger logger, Configuration configuration, String template){
    super(logger, configuration, template);
  }

  @Override
  public void init(String lang){
    this.lang = lang;
  }

  @Override
  protected void init() {
    Properties props = this.configuration.getLanguage(lang);
    Federation federation = (Federation)this.getParameter(Parameter.FEDERATION);
    String baseUrl = this.getParameterAsString(Parameter.BASE_URL);
    String code = this.getParameterAsString(Parameter.CODE);
    String message = props.getProperty(TemplateComponent.FEDERATION_TRUST_REQUEST_MESSAGE);
    String label = federation.getLabel();
    String responseUrl = baseUrl + "/federation/requests/?federation=" + federation.getId();
    
    loadCanvas(TemplateParams.CANVAS_FEDERATION_TRUST);
    
    try {
      message = message.replace("::label::", label);
      message = message.replace("::code::", code);
      message = message.replace("::responseUrl::", responseUrl);

      addMark(TemplateParams.FEDERATION_TRUST_REQUEST, props.getProperty(TemplateComponent.FEDERATION_TRUST_REQUEST));
      addMark(TemplateParams.FEDERATION_TRUST_REQUEST_MESSAGE, message);
      addMark(TemplateParams.FEDERATION_TRUST_INFO, props.getProperty(TemplateComponent.FEDERATION_TRUST_INFO));
      addMark(TemplateParams.FEDERATION_TRUST_NAME, props.getProperty(TemplateComponent.FEDERATION_TRUST_NAME));
      addMark(TemplateParams.FEDERATION_TRUST_LABEL, props.getProperty(TemplateComponent.FEDERATION_TRUST_LABEL));
      addMark(TemplateParams.FEDERATION_TRUST_URL, props.getProperty(TemplateComponent.FEDERATION_TRUST_URL));
      
      addMark(TemplateParams.CODE, code);
      addMark(TemplateParams.NAME, federation.getName());
      addMark(TemplateParams.LABEL, label);
      addMark(TemplateParams.RESPONSE_URL, responseUrl);
      addMark(TemplateParams.URL, federation.getUri().toURL().toString());
    } catch (MalformedURLException e) {
      frLogger.error("render federation", e);
    }

    this.addLanguagesMark(lang);
  }
}
