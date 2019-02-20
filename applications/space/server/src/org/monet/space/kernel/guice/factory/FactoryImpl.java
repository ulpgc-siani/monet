package org.monet.space.kernel.guice.factory;

import com.google.inject.Inject;
import org.monet.space.kernel.guice.InjectorFactory;

import java.util.Map;

public class FactoryImpl<X, Y> implements Factory<X, Y> {

	@Inject
	protected Map<X, Class<? extends Y>> factoryMap;

	public Y create(X key) {
		return InjectorFactory.getInstance().getInstance(this.factoryMap.get(key));
	}
}