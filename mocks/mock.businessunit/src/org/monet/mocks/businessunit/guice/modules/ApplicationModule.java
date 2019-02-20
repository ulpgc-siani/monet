package org.monet.mocks.businessunit.guice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import org.monet.mocks.businessunit.BusinessUnitService;
import org.monet.mocks.businessunit.Service;
import org.monet.mocks.businessunit.agents.AgentFilesystem;
import org.monet.mocks.businessunit.control.Action;
import org.monet.mocks.businessunit.control.ActionsFactory;
import org.monet.mocks.businessunit.control.actions.ActionCallback;
import org.monet.mocks.businessunit.control.actions.ActionRequestService;
import org.monet.mocks.businessunit.core.Configuration;
import org.monet.mocks.businessunit.core.FileConfiguration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.io.File;

public class ApplicationModule extends AbstractModule {

	private Context context;

	@Override
	protected void configure() {

		try {
			Context context = new InitialContext();
			this.context = (Context) context.lookup("java:comp/env");
		} catch (NamingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		bind(Context.class).toInstance(this.context);
		bind(FileConfiguration.Context.class).toInstance(new FileConfiguration.Context() {
			private ServletContext servletContext;

			@Inject
			public void injectServletContext(ServletContext servletContext) {
				this.servletContext = servletContext;
			}

			@Override
			public String getUserPath() {
				return System.getProperty("user.home") + File.separator + "." + servletContext.getServletContextName();
			}

			@Override
			public String getAppPath() {
				return servletContext.getRealPath("/WEB-INF/classes");
			}
		});

		bind(Configuration.class).to(FileConfiguration.class).asEagerSingleton();
		bind(AgentFilesystem.class).asEagerSingleton();
		bindActionsFactoryMap();
		bind(Service.class).to(BusinessUnitService.class).asEagerSingleton();
	}

	private void bindActionsFactoryMap() {
		MapBinder<String, Class<? extends Action>> actionsMap = MapBinder.newMapBinder(binder(), new TypeLiteral<String>() {
		}, new TypeLiteral<Class<? extends Action>>() {
		});
		actionsMap.addBinding(ActionsFactory.ACTION_REQUEST_SERVICE).toInstance(ActionRequestService.class);
		actionsMap.addBinding(ActionsFactory.ACTION_CALLBACK).toInstance(ActionCallback.class);
		bind(ActionsFactory.class);
	}
}