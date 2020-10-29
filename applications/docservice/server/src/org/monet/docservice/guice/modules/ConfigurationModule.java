package org.monet.docservice.guice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import org.monet.docservice.Application;
import org.monet.docservice.core.agent.AgentFilesystem;
import org.monet.docservice.core.agent.impl.AgentFilesystemImpl;
import org.monet.docservice.core.exceptions.ConfigurationException;
import org.monet.docservice.core.library.LibraryBase64;
import org.monet.docservice.core.library.LibraryFile;
import org.monet.docservice.core.library.LibraryUtils;
import org.monet.docservice.core.library.LibraryZip;
import org.monet.docservice.core.library.impl.LibraryBase64Impl;
import org.monet.docservice.core.library.impl.LibraryFileImpl;
import org.monet.docservice.core.library.impl.LibraryUtilsImpl;
import org.monet.docservice.core.library.impl.LibraryZipImpl;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.MimeTypes;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.configuration.ServerConfigurator;
import org.monet.docservice.docprocessor.configuration.impl.ConfigurationImpl;
import org.monet.docservice.docprocessor.configuration.impl.ServerConfiguratorImpl;
import org.monet.docservice.docprocessor.data.DataSourceProvider;
import org.monet.docservice.docprocessor.data.DiskManager;
import org.monet.docservice.docprocessor.data.QueryStore;
import org.monet.docservice.docprocessor.data.Repository;
import org.monet.docservice.docprocessor.data.impl.DataSourceProviderImpl;
import org.monet.docservice.docprocessor.data.impl.DatabaseRepository;
import org.monet.docservice.docprocessor.data.impl.DiskManagerImpl;
import org.monet.docservice.docprocessor.data.impl.QueryStoreImpl;
import org.monet.docservice.docprocessor.operations.Operation;
import org.monet.docservice.docprocessor.operations.OperationsFactory;
import org.monet.docservice.docprocessor.operations.impl.*;
import org.monet.docservice.docprocessor.pdf.PdfConverter;
import org.monet.docservice.docprocessor.pdf.PdfXmlExtractor;
import org.monet.docservice.docprocessor.pdf.PreviewGenerator;
import org.monet.docservice.docprocessor.pdf.Signer;
import org.monet.docservice.docprocessor.pdf.impl.*;
import org.monet.docservice.docprocessor.templates.DocumentProcessor;
import org.monet.docservice.docprocessor.templates.DocumentReplacer;
import org.monet.docservice.docprocessor.templates.ModelProducer;
import org.monet.docservice.docprocessor.templates.PartsExtractor;
import org.monet.docservice.docprocessor.templates.common.Model;
import org.monet.docservice.docprocessor.templates.impl.DocumentReplacerImpl;
import org.monet.docservice.docprocessor.templates.impl.ModelImpl;
import org.monet.docservice.docprocessor.templates.impl.PartsExtractorImpl;
import org.monet.docservice.docprocessor.templates.opendocument.OpenDoc;
import org.monet.docservice.docprocessor.templates.opendocument.OpenDocument;
import org.monet.docservice.docprocessor.templates.openxml.OpenXml;
import org.monet.docservice.docprocessor.templates.openxml.OpenXmlDocument;
import org.monet.docservice.docprocessor.templates.portabledocument.Formatter;
import org.monet.docservice.docprocessor.templates.portabledocument.FormatterImpl;
import org.monet.docservice.docprocessor.templates.portabledocument.PdfProcessor;
import org.monet.docservice.docprocessor.templates.portabledocument.PortableDocument;
import org.monet.docservice.docprocessor.templates.xml.XmlDocument;
import org.monet.docservice.docprocessor.templates.xml.XmlProcessor;
import org.monet.docservice.docprocessor.worker.DocumentPreviewsCleaner;
import org.monet.docservice.docprocessor.worker.WorkQueue;
import org.monet.docservice.docprocessor.worker.WorkQueueManager;
import org.monet.docservice.docprocessor.worker.WorkQueueRepository;
import org.monet.docservice.docprocessor.worker.impl.DocumentPreviewsCleanerImpl;
import org.monet.docservice.docprocessor.worker.impl.WorkQueueImpl;
import org.monet.docservice.docprocessor.worker.impl.WorkQueueManagerImpl;
import org.monet.docservice.guice.InjectorFactory;
import org.monet.docservice.servlet.factory.ActionFactory;
import org.monet.docservice.servlet.factory.ActionFactoryImpl;
import org.monet.docservice.servlet.factory.impl.*;
import org.monet.docservice.upgrades.UpgradeManager;
import org.monet.docservice.upgrades.UpgradeScriptClassLoader;
import org.monet.docservice.upgrades.impl.UpgradeManagerImpl;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Iterator;

public class ConfigurationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ServerConfigurator.class).to(ServerConfiguratorImpl.class).asEagerSingleton();
		bind(Configuration.class).to(ConfigurationImpl.class).asEagerSingleton();
	}

}