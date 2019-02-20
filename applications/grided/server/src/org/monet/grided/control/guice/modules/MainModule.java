package org.monet.grided.control.guice.modules;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.monet.grided.constants.Actions;
import org.monet.grided.control.actions.Action;
import org.monet.grided.control.actions.ActionsFactory;
import org.monet.grided.control.actions.impl.ActionsFactoryImpl;
import org.monet.grided.control.actions.impl.AddFederationAction;
import org.monet.grided.control.actions.impl.AddModelAction;
import org.monet.grided.control.actions.impl.AddServerAction;
import org.monet.grided.control.actions.impl.AddSpaceAction;
import org.monet.grided.control.actions.impl.DownloadImageAction;
import org.monet.grided.control.actions.impl.LoadAccountActionMock;
import org.monet.grided.control.actions.impl.LoadFederationAction;
import org.monet.grided.control.actions.impl.LoadFederationsAction;
import org.monet.grided.control.actions.impl.LoadImportProgressAction;
import org.monet.grided.control.actions.impl.LoadImporterTypesAction;
import org.monet.grided.control.actions.impl.LoadModelAction;
import org.monet.grided.control.actions.impl.LoadModelsAction;
import org.monet.grided.control.actions.impl.LoadServerAction;
import org.monet.grided.control.actions.impl.LoadServerSpaces;
import org.monet.grided.control.actions.impl.LoadServerStateAction;
import org.monet.grided.control.actions.impl.LoadServersAction;
import org.monet.grided.control.actions.impl.LoadServicesAction;
import org.monet.grided.control.actions.impl.LoadSpaceAction;
import org.monet.grided.control.actions.impl.LoadSpacesByModelAction;
import org.monet.grided.control.actions.impl.LoadSpacesWithModelAction;
import org.monet.grided.control.actions.impl.LoginAction;
import org.monet.grided.control.actions.impl.RemoveFederationsAction;
import org.monet.grided.control.actions.impl.RemoveModelsAction;
import org.monet.grided.control.actions.impl.RemoveServersAction;
import org.monet.grided.control.actions.impl.RemoveSpacesAction;
import org.monet.grided.control.actions.impl.SaveFederationAction;
import org.monet.grided.control.actions.impl.SaveLogAction;
import org.monet.grided.control.actions.impl.SaveServerAction;
import org.monet.grided.control.actions.impl.SaveSpaceAction;
import org.monet.grided.control.actions.impl.ShowApplicationAction;
import org.monet.grided.control.actions.impl.StartFederationAction;
import org.monet.grided.control.actions.impl.StartImportAction;
import org.monet.grided.control.actions.impl.StartSpaceAction;
import org.monet.grided.control.actions.impl.StopFederationAction;
import org.monet.grided.control.actions.impl.StopSpaceAction;
import org.monet.grided.control.actions.impl.UpgradeSpacesAction;
import org.monet.grided.control.actions.impl.UploadImageAction;
import org.monet.grided.control.actions.impl.UploadModelVersionAction;
import org.monet.grided.control.guice.providers.DeployServiceProvider;
import org.monet.grided.control.guice.providers.FederationStoreDriverProvider;
import org.monet.grided.control.guice.providers.ModelDataReaderProvider;
import org.monet.grided.control.guice.providers.ModelStoreDriverProvider;
import org.monet.grided.control.guice.providers.SpaceServiceProvider;
import org.monet.grided.control.guice.providers.SpaceStoreDriverProvider;
import org.monet.grided.control.injectors.Log4jTypeListener;
import org.monet.grided.control.log.Logger;
import org.monet.grided.core.configuration.Configuration;
import org.monet.grided.core.configuration.ServerConfiguration;
import org.monet.grided.core.configuration.impl.ConfigurationImpl;
import org.monet.grided.core.configuration.impl.ServerConfigurationImpl;
import org.monet.grided.core.lib.Filesystem;
import org.monet.grided.core.lib.FilesystemImpl;
import org.monet.grided.core.persistence.FederationsRepository;
import org.monet.grided.core.persistence.ModelsRepository;
import org.monet.grided.core.persistence.ServersRepository;
import org.monet.grided.core.persistence.SpacesRepository;
import org.monet.grided.core.persistence.database.QueriesStore;
import org.monet.grided.core.persistence.database.impl.FederationsProducer;
import org.monet.grided.core.persistence.database.impl.ModelsProducer;
import org.monet.grided.core.persistence.database.impl.QueriesStoreImpl;
import org.monet.grided.core.persistence.database.impl.ServersProducer;
import org.monet.grided.core.persistence.database.impl.SpacesProducer;
import org.monet.grided.core.persistence.filesystem.FederationDataStoreDriver;
import org.monet.grided.core.persistence.filesystem.ModelDataStoreDriver;
import org.monet.grided.core.persistence.filesystem.SpaceDataStoreDriver;
import org.monet.grided.core.persistence.filesystem.impl.FederationStoreConfig;
import org.monet.grided.core.persistence.filesystem.impl.ModelStoreConfig;
import org.monet.grided.core.persistence.filesystem.impl.ServerStoreConfig;
import org.monet.grided.core.persistence.filesystem.impl.SpaceStoreConfig;
import org.monet.grided.core.persistence.filesystem.readers.ModelDataReader;
import org.monet.grided.core.persistence.impl.FederationsRepositoryImpl;
import org.monet.grided.core.persistence.impl.ModelsRepositoryImpl;
import org.monet.grided.core.persistence.impl.ServersRepositoryImpl;
import org.monet.grided.core.persistence.impl.SpacesRepositoryImpl;
import org.monet.grided.core.services.adapters.MonetGridedAdapter;
import org.monet.grided.core.services.adapters.impl.MonetGridedAdapterImpl;
import org.monet.grided.core.services.deploy.DeployService;
import org.monet.grided.core.services.grided.GridedService;
import org.monet.grided.core.services.grided.impl.GridedServiceImpl;
import org.monet.grided.core.services.space.SpaceService;
import org.monet.grided.exceptions.SystemException;
import org.monet.grided.ui.templates.TemplatesEngine;
import org.monet.grided.ui.templates.impl.TemplatesEngineImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.MapBinder;

public class MainModule extends AbstractModule {

	private ServletContext servletContext;
	private Context context;

	public MainModule(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}

	@Override
	protected void configure() {
		install(new LoggerModule());

		try {
			Context context = new InitialContext();
			this.context = (Context) context.lookup("java:comp/env");
		} catch (NamingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		bindListener(Matchers.any(), new Log4jTypeListener());

		initializeActionsFactory();
		bind(ServerConfiguration.class).to(ServerConfigurationImpl.class).asEagerSingleton();
		bind(Configuration.class).to(ConfigurationImpl.class).asEagerSingleton();
		bind(Context.class).toInstance(this.context);
		bind(Filesystem.class).to(FilesystemImpl.class);

		bind(ModelDataReader.class).toProvider(ModelDataReaderProvider.class);

		bind(ServersProducer.class);
		bind(FederationsProducer.class);
		bind(SpacesProducer.class);
		bind(ModelsProducer.class);

		bind(FederationDataStoreDriver.class).toProvider(FederationStoreDriverProvider.class);
		bind(SpaceDataStoreDriver.class).toProvider(SpaceStoreDriverProvider.class);
		bind(ModelDataStoreDriver.class).toProvider(ModelStoreDriverProvider.class);

		bind(ServerStoreConfig.class);
		bind(FederationStoreConfig.class);
		bind(SpaceStoreConfig.class);
		bind(ModelStoreConfig.class);

		bind(ServersRepository.class).to(ServersRepositoryImpl.class);
		bind(FederationsRepository.class).to(FederationsRepositoryImpl.class);
		bind(SpacesRepository.class).to(SpacesRepositoryImpl.class);
		bind(ModelsRepository.class).to(ModelsRepositoryImpl.class);

		bind(GridedService.class).to(GridedServiceImpl.class).asEagerSingleton();
		bind(DeployService.class).toProvider(DeployServiceProvider.class);
		bind(SpaceService.class).toProvider(SpaceServiceProvider.class);

		bind(MonetGridedAdapter.class).to(MonetGridedAdapterImpl.class);
	}

	private void initializeActionsFactory() {
		MapBinder<String, Class<? extends Action>> actionsMap = MapBinder.newMapBinder(binder(), new TypeLiteral<String>() {
		}, new TypeLiteral<Class<? extends Action>>() {
		});
		actionsMap.addBinding(Actions.LOGIN).toInstance(LoginAction.class);
		actionsMap.addBinding(Actions.APPLICATION).toInstance(ShowApplicationAction.class);
		actionsMap.addBinding(Actions.LOAD_ACCOUNT).toInstance(LoadAccountActionMock.class);

		actionsMap.addBinding(Actions.ADD_SERVER).toInstance(AddServerAction.class);
		actionsMap.addBinding(Actions.LOAD_SERVERS).toInstance(LoadServersAction.class);
		actionsMap.addBinding(Actions.LOAD_SERVER).toInstance(LoadServerAction.class);
		actionsMap.addBinding(Actions.LOAD_SERVER_STATE).toInstance(LoadServerStateAction.class);
		actionsMap.addBinding(Actions.LOAD_SERVER_SPACES).toInstance(LoadServerSpaces.class);
		actionsMap.addBinding(Actions.SAVE_SERVER).toInstance(SaveServerAction.class);
		actionsMap.addBinding(Actions.REMOVE_SERVERS).toInstance(RemoveServersAction.class);

		actionsMap.addBinding(Actions.LOAD_FEDERATIONS).toInstance(LoadFederationsAction.class);
		actionsMap.addBinding(Actions.LOAD_FEDERATION).toInstance(LoadFederationAction.class);
		actionsMap.addBinding(Actions.REMOVE_FEDERATIONS).toInstance(RemoveFederationsAction.class);
		actionsMap.addBinding(Actions.ADD_FEDERATION).toInstance(AddFederationAction.class);
		actionsMap.addBinding(Actions.SAVE_FEDERATION).toInstance(SaveFederationAction.class);
		actionsMap.addBinding(Actions.START_FEDERATION).toInstance(StartFederationAction.class);
		actionsMap.addBinding(Actions.STOP_FEDERATION).toInstance(StopFederationAction.class);

		actionsMap.addBinding(Actions.LOAD_SPACE).toInstance(LoadSpaceAction.class);
		actionsMap.addBinding(Actions.LOAD_SPACES_WITH_MODEL).toInstance(LoadSpacesWithModelAction.class);
		actionsMap.addBinding(Actions.LOAD_SPACES_BY_MODEL).toInstance(LoadSpacesByModelAction.class);

		actionsMap.addBinding(Actions.LOAD_SERVICES).toInstance(LoadServicesAction.class);
		actionsMap.addBinding(Actions.LOAD_IMPORTER_TYPES).toInstance(LoadImporterTypesAction.class);
		actionsMap.addBinding(Actions.LOAD_IMPORT_PROGRESS).toInstance(LoadImportProgressAction.class);
		actionsMap.addBinding(Actions.START_IMPORT).toInstance(StartImportAction.class);
		actionsMap.addBinding(Actions.REMOVE_SPACES).toInstance(RemoveSpacesAction.class);
		actionsMap.addBinding(Actions.SAVE_SPACE).toInstance(SaveSpaceAction.class);
		actionsMap.addBinding(Actions.ADD_SPACE).toInstance(AddSpaceAction.class);
		actionsMap.addBinding(Actions.START_SPACE).toInstance(StartSpaceAction.class);
		actionsMap.addBinding(Actions.STOP_SPACE).toInstance(StopSpaceAction.class);
		actionsMap.addBinding(Actions.UPDATE_MODEL_IN_SPACES).toInstance(UpgradeSpacesAction.class);

		actionsMap.addBinding(Actions.LOAD_MODELS).toInstance(LoadModelsAction.class);
		actionsMap.addBinding(Actions.LOAD_MODEL).toInstance(LoadModelAction.class);
		actionsMap.addBinding(Actions.ADD_MODEL).toInstance(AddModelAction.class);

		actionsMap.addBinding(Actions.UPLOAD_MODEL_VERSION).toInstance(UploadModelVersionAction.class);
		actionsMap.addBinding(Actions.UPGRADE_SPACES).toInstance(UpgradeSpacesAction.class);
		actionsMap.addBinding(Actions.REMOVE_MODELS).toInstance(RemoveModelsAction.class);
		actionsMap.addBinding(Actions.UPLOAD_IMAGE).toInstance(UploadImageAction.class);
		actionsMap.addBinding(Actions.DOWNLOAD_IMAGE).toInstance(DownloadImageAction.class);

		actionsMap.addBinding(Actions.SAVE_LOG).toInstance(SaveLogAction.class);

		bind(ActionsFactory.class).to(ActionsFactoryImpl.class);
	}

	@Provides
	public TemplatesEngine providesTemplatesEngine(Logger logger, ServerConfiguration serverConfiguration) {
		TemplatesEngineImpl templatesEngine = null;
		String templatesPath = this.servletContext.getRealPath("") + File.separator + "templates";

		try {
			templatesEngine = new TemplatesEngineImpl(templatesPath);
		} catch (Exception ex) {
			String message = String.format("Error Initializing templates engine. templatesPath = %s");
			logger.error(message, ex);
			throw new SystemException(message, ex);
		}
		return templatesEngine;
	}

	@Provides
	public QueriesStore providesQueriesStore(Logger logger, ServerConfiguration serverConfiguration, Configuration configuration) {
		QueriesStore queriesStore;

		String databaseType = configuration.getProperty(Configuration.DATABASE_TYPE);
		String filename = "/database/" + databaseType + ".queries" + ".sql";

		try {
			queriesStore = new QueriesStoreImpl(logger, filename);
		} catch (Exception ex) {
			String message = String.format("Error reading database queries = %s", filename);
			logger.error(message, ex);
			throw new SystemException(message, ex);
		}
		return queriesStore;
	}

	@Provides
	DataSource providesDataSource(Logger logger, ServletContext context, Configuration configuration) {
		DataSource dataSource = null;
		String dataSourceName = "";
		try {
			dataSourceName = configuration.getProperty(Configuration.DATABASE_DATASOURCE);
			dataSource = (DataSource) this.context.lookup(dataSourceName);
		} catch (NamingException ex) {
			String message = String.format("Error loading dataSource with name: %s", dataSourceName);
			logger.error(message, ex);
			throw new SystemException(message, ex);
		}
		return dataSource;
	}
}