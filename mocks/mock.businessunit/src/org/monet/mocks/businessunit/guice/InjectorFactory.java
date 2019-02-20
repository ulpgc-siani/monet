package org.monet.mocks.businessunit.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.monet.mocks.businessunit.guice.modules.AppServletModule;

import javax.servlet.ServletContext;

public class InjectorFactory {

  private static Injector injector;
  
  public synchronized static Injector get(ServletContext servletContext) {
    if(injector == null)
      injector = Guice.createInjector(new AppServletModule(servletContext));
    return injector;
  }
  
  public synchronized static Injector get() {
    return injector;
  }
}
