package org.monet.docservice.guice.modules;

import org.monet.docservice.docprocessor.Download;
import org.monet.docservice.docprocessor.Preview;
import org.monet.docservice.servlet.Controller;

import javax.servlet.ServletContext;

public class DocServiceServletModule extends com.google.inject.servlet.ServletModule {

	ServletContext servletContext;

	public DocServiceServletModule(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}

	@Override
	protected void configureServlets() {
		serve("/preview/*").with(Preview.class);
		serve("/download/*").with(Download.class);
		serve("/document/*").with(Controller.class);

		install(new ConfigurationModule());
		install(new MainModule());
	}

}
