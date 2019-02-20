package org.monet.federation.accountoffice.core.components.templatecomponent;

import org.monet.federation.accountoffice.core.components.templatecomponent.impl.FederationRender;

public interface TemplateFactory {
  
  public static enum Renders {
    LOGIN, 
    REGISTER, 
    BOARD, 
    RESET_PASSWORD, 
    CHANGE_PASSWORD, 
    FEDERATION_TRUST, 
    VALIDATE_FEDERATION_TRUST, 
    VALIDATE_BUSINESS_UNIT_PARTNER, 
    BUSINESS_UNIT_PARTNER 
  };
  
  public static final String TEMPLATES = "templates";
  
  FederationRender getRender(Renders render);
}
