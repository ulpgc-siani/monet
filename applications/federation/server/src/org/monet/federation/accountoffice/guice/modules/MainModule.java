package org.monet.federation.accountoffice.guice.modules;

import com.google.inject.AbstractModule;
import org.monet.encrypt.extractor.CertificateExtractor;
import org.monet.federation.accountoffice.control.servlets.actions.ActionFactory;
import org.monet.federation.accountoffice.control.servlets.actions.impl.ActionFactoryImpl;
import org.monet.federation.accountoffice.control.sockets.SocketListener;
import org.monet.federation.accountoffice.control.sockets.impl.AccountSocketFactory;
import org.monet.federation.accountoffice.core.agents.AgentMobilePushService;
import org.monet.federation.accountoffice.core.components.accountcomponent.SessionComponent;
import org.monet.federation.accountoffice.core.components.accountcomponent.impl.SessionComponentImpl;
import org.monet.federation.accountoffice.core.components.certificatecomponent.CertificateComponent;
import org.monet.federation.accountoffice.core.components.certificatecomponent.impl.CertificateComponentImpl;
import org.monet.federation.accountoffice.core.components.requesttokencomponent.RequestTokenComponent;
import org.monet.federation.accountoffice.core.components.requesttokencomponent.impl.RequestTokenImpl;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateComponent;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateFactory;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.TemplateComponentImpl;
import org.monet.federation.accountoffice.core.components.templatecomponent.impl.TemplateFactoryImpl;
import org.monet.federation.accountoffice.core.components.unitcomponent.BusinessUnitComponent;
import org.monet.federation.accountoffice.core.components.unitcomponent.impl.BusinessUnitComponentImpl;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.configuration.ServerConfigurator;
import org.monet.federation.accountoffice.core.configuration.impl.ConfigurationImpl;
import org.monet.federation.accountoffice.core.configuration.impl.ServerConfiguratorImpl;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.database.DataRepositorySource;
import org.monet.federation.accountoffice.core.database.impl.DataRepositoryImpl;
import org.monet.federation.accountoffice.core.database.impl.DataRepositorySourceImpl;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer;
import org.monet.federation.accountoffice.core.layers.account.impl.AccountLayerImpl;
import org.monet.federation.setupservice.control.actions.ActionsFactorySetupService;
import org.monet.federation.setupservice.core.model.FederationStatus;

import javax.servlet.ServletContext;

public class MainModule extends AbstractModule {

    public MainModule(ServletContext servletContext) {
        super();
    }

    @Override
    protected void configure() {
        install(new LoggerModule());

        bind(ServerConfigurator.class).to(ServerConfiguratorImpl.class);
        bind(Configuration.class).to(ConfigurationImpl.class);
        bind(DataRepositorySource.class).to(DataRepositorySourceImpl.class);
        bind(DataRepository.class).to(DataRepositoryImpl.class);
        bind(AccountLayer.class).to(AccountLayerImpl.class);
        bind(SessionComponent.class).to(SessionComponentImpl.class);
        bind(CertificateExtractor.class);
        bind(CertificateComponent.class).to(CertificateComponentImpl.class);
        bind(TemplateComponent.class).to(TemplateComponentImpl.class);
        bind(ActionFactory.class).to(ActionFactoryImpl.class);
        bind(RequestTokenComponent.class).to(RequestTokenImpl.class);
        bind(BusinessUnitComponent.class).to(BusinessUnitComponentImpl.class);
        bind(TemplateFactory.class).to(TemplateFactoryImpl.class);
        bind(ActionsFactorySetupService.class);
        bind(AccountSocketFactory.class);

        bind(FederationStatus.class).asEagerSingleton();
        bind(SocketListener.class).asEagerSingleton();
        bind(AgentMobilePushService.class).asEagerSingleton();
    }
}
