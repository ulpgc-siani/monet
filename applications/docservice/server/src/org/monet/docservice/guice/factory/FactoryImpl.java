package org.monet.docservice.guice.factory;

import java.util.Map;

import org.monet.docservice.guice.InjectorFactory;

import com.google.inject.Inject;

public class FactoryImpl<X, Y> implements Factory<X, Y> {

  @Inject
  protected Map<X, Class<? extends Y>> factoryMap;

  public Y create(X key) {
    return InjectorFactory.get().getInstance(this.factoryMap.get(key));
  }
}