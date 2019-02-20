package org.monet.space.kernel.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectorFactory {

	private static Injector injector = null;

	public synchronized static Injector getInstance() {
		if (injector == null) injector = Guice.createInjector(new CoreModule());
		return injector;
	}

}
