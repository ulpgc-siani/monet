package org.monet.docservice.guice;

import javax.servlet.ServletContext;

import org.monet.docservice.guice.modules.DocServiceServletModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectorFactory {

  private static Injector injector;
  
  public synchronized static Injector get(ServletContext servletContext) {
    if(injector == null)
      injector = Guice.createInjector(new DocServiceServletModule(servletContext));
    return injector;
  }
  
  public synchronized static Injector get() {
    return injector;
  }
}
