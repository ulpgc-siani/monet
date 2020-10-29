package org.monet.docservice.guice;

import javax.servlet.ServletContext;

import com.google.inject.Module;
import org.monet.docservice.guice.modules.DocServiceServletModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.Arrays;

public class InjectorFactory {

  private static Injector injector;

  public static void register(Module... modules) {
      injector = Guice.createInjector(Arrays.asList(modules));
  }

  public synchronized static Injector get(ServletContext servletContext) {
    if(injector == null)
      injector = Guice.createInjector(new DocServiceServletModule(servletContext));
    return injector;
  }
  
  public synchronized static Injector get() {
    return injector;
  }
}
