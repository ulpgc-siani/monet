package org.monet.grided.control.actions;

import java.util.Map;

import org.monet.grided.control.actions.Factory;
import org.monet.grided.control.guice.InjectorFactory;

import com.google.inject.Inject;

public class ActionsFactoryImpl<X, Y> implements Factory<X, Y> {

  @Inject
  protected Map<X, Class<? extends Y>> factoryMap;

  public Y create(X key) {
    return InjectorFactory.get().getInstance(this.factoryMap.get(key));
  }
}