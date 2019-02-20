package org.monet.space.explorer.injection;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class ServletContextListener extends GuiceServletContextListener {

	private ServletContext servletContext;

	@Override
	protected Injector getInjector() {
		return InjectorFactory.get(this.servletContext);
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		this.servletContext = servletContextEvent.getServletContext();
		super.contextInitialized(servletContextEvent);
	}

	public void contextDestroyed(ServletContextEvent event) {
	}

}
