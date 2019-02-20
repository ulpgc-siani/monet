package org.monet.federation.accountoffice.guice.modules;

import javax.servlet.ServletContext;

import org.monet.federation.accountoffice.control.servlets.AccessTokenServlet;
import org.monet.federation.accountoffice.control.servlets.AuthorizationResourcesServlet;
import org.monet.federation.accountoffice.control.servlets.AuthorizationServlet;
import org.monet.federation.accountoffice.control.servlets.AutoAuthorizationServlet;
import org.monet.federation.accountoffice.control.servlets.HomeServlet;
import org.monet.federation.accountoffice.control.servlets.OpenIdCallbackServlet;
import org.monet.federation.accountoffice.control.servlets.RequestTokenServlet;
import org.monet.federation.accountoffice.control.servlets.ValidateBusinessUnitPartnerServlet;
import org.monet.federation.accountoffice.control.servlets.ValidateFederationTrustServlet;
import org.monet.federation.setupservice.control.ControllerSetupService;

import com.google.inject.Inject;

public class ServletModule extends com.google.inject.servlet.ServletModule{
  ServletContext servletContext;
  
  public ServletModule(ServletContext servletContext) {
    super();
    this.servletContext = servletContext;
  }
  
  @Inject
  

  @Override
  protected void configureServlets() {

    serve("/accounts/authorization/callback/openid/*").with(OpenIdCallbackServlet.class);
    serve("/accounts/authorization/resources/*").with(AuthorizationResourcesServlet.class);
    serve("/accounts/authorization/*").with(AuthorizationServlet.class);
    serve("/accounts/autoauthorization/*").with(AutoAuthorizationServlet.class);

    serve("/accounts/tokens/request/*").with(RequestTokenServlet.class);
    serve("/accounts/tokens/access/*").with(AccessTokenServlet.class);
    
    serve("/federation/requests/*").with(ValidateFederationTrustServlet.class);
    serve("/businessunit/requests/*").with(ValidateBusinessUnitPartnerServlet.class);

    serve("/home/*").with(HomeServlet.class);
    serve("").with(HomeServlet.class);
    serve("/").with(HomeServlet.class);
    
    serve("/setupservice/*").with(ControllerSetupService.class);
    
    install(new MainModule(this.servletContext));
  }
  
}
