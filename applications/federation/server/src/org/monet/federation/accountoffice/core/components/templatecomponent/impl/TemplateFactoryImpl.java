package org.monet.federation.accountoffice.core.components.templatecomponent.impl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateFactory;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.BoardRender;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.BusinessUnitPartnerRender;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.ChangePasswordRender;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.FederationTrustRender;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.LoginRender;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.RegisterRender;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.ResetPasswordRender;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.ValidateBusinessUnitPartnerRender;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.renders.ValidateFederationTrustRender;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TemplateFactoryImpl implements TemplateFactory{
  private Map<Renders,Object> renders;
  private Logger logger;
  private Configuration configuration;
  
  @Inject
  public TemplateFactoryImpl(Logger logger, Configuration configuration){
    this.logger = logger;
    this.configuration = configuration;

    renders = new HashMap<Renders,Object>();
    renders.put(Renders.LOGIN,        LoginRender.class);
    renders.put(Renders.REGISTER,     RegisterRender.class);
    renders.put(Renders.BOARD,        BoardRender.class);
    renders.put(Renders.RESET_PASSWORD,  ResetPasswordRender.class);
    renders.put(Renders.CHANGE_PASSWORD,  ChangePasswordRender.class);
    renders.put(Renders.FEDERATION_TRUST,  FederationTrustRender.class);
    renders.put(Renders.BUSINESS_UNIT_PARTNER,  BusinessUnitPartnerRender.class);
    renders.put(Renders.VALIDATE_FEDERATION_TRUST,  ValidateFederationTrustRender.class);
    renders.put(Renders.VALIDATE_BUSINESS_UNIT_PARTNER,  ValidateBusinessUnitPartnerRender.class);
  }

  @Override
  public FederationRender getRender(Renders render) {
    Class<?> clazz;
    FederationRender renderInstance = null;
    
    try {
      clazz = (Class<?>)this.renders.get(render);
      
      Constructor<?> constructor = clazz.getConstructor(Logger.class, Configuration.class, String.class);
      renderInstance = (FederationRender)constructor.newInstance(logger, configuration, TemplateFactory.TEMPLATES);
    } catch (Exception e) {
      this.logger.error(e.getMessage(), e);
    }

    return renderInstance;
  }
}
