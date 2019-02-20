package org.monet.federation.accountoffice.guice;

import javax.servlet.ServletContext;

import org.monet.federation.accountoffice.guice.modules.ServletModule;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class InjectorFactory {
  private static Injector injector;

  public synchronized static Injector get(ServletContext servletContext) {
    if(injector == null)
      injector = Guice.createInjector(new ServletModule(servletContext));
    return injector;
  }

  public synchronized static Injector get() {
    return injector;
  }
}
