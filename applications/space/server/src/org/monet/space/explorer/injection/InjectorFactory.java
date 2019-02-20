package org.monet.space.explorer.injection;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.servlet.ServletContext;

public class InjectorFactory {

	private static Injector injector;

	public synchronized static Injector get(ServletContext servletContext) {

		if (injector == null) {
			injector = Guice.createInjector(new ApplicationModule(servletContext));
			start();
		}

		return injector;
	}

	public synchronized static Injector get() {
		return injector;
	}

	private static void start() {
	}

}
