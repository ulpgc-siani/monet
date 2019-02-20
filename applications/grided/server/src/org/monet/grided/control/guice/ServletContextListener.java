package org.monet.grided.control.guice;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ServletContextListener extends GuiceServletContextListener {
  
  private ServletContext servletContext;
  
  @Override
  protected Injector getInjector() {
    return InjectorFactory.get(this.servletContext);
  }

  @Override
  public void contextInitialized(ServletContextEvent event) {
    this.servletContext = event.getServletContext();
    super.contextInitialized(event);    
  }
      
  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    super.contextDestroyed(servletContextEvent);        
  }  
}
