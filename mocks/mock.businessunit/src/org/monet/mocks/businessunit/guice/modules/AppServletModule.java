package org.monet.mocks.businessunit.guice.modules;

import org.monet.mocks.businessunit.control.Controller;

import javax.servlet.ServletContext;

public class AppServletModule extends com.google.inject.servlet.ServletModule {

	ServletContext servletContext;

	public AppServletModule(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}

	@Override
	protected void configureServlets() {
		serve("/*").with(Controller.class);

		install(new ApplicationModule());
	}

}
