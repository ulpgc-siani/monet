package org.monet.grided.control.guice.modules;

import javax.servlet.ServletContext;

import org.monet.grided.control.AppController;


public class ServletModule extends com.google.inject.servlet.ServletModule {
  ServletContext servletContext;

  public ServletModule(ServletContext servletContext) {
    super();
    this.servletContext = servletContext;
  }

  @Override
  protected void configureServlets() {
	  serve("/servlet*").with(AppController.class);
	  
//    serve("/servlet/accounts/request_token/*").with(RequestTokenServlet.class);
   
    install(new MainModule(this.servletContext));
  }
}
