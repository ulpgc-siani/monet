package org.monet.docservice.guice;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.monet.docservice.Application;
import org.monet.docservice.core.log.impl.DatabaseAppender;
import org.monet.docservice.docprocessor.worker.WorkQueueManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.lang.ref.PhantomReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

//import java.sql.Driver;
//import java.sql.DriverManager;
//import java.util.Enumeration;

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

		Application application = InjectorFactory.get().getInstance(Application.class);
		application.setName(servletContext.getServletContextName());
		application.run();
	}

	@SuppressWarnings("rawtypes")
	public void contextDestroyed(ServletContextEvent event) {
		InjectorFactory.get().getInstance(Application.class).stop();
	}

}
