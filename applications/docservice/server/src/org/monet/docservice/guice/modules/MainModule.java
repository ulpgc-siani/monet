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

public class MainModule extends AbstractModule {

	private Context context;

	@Override
	protected void configure() {
		install(new LoggerModule());

		try {
			Context context = new InitialContext();
			this.context = (Context) context.lookup("java:comp/env");
		} catch (NamingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		bind(Context.class).toInstance(this.context);
		bind(ServerConfigurator.class).to(ServerConfiguratorImpl.class).asEagerSingleton();
		bind(Configuration.class).to(ConfigurationImpl.class).asEagerSingleton();
		bind(AgentFilesystem.class).to(AgentFilesystemImpl.class);
		bind(LibraryFile.class).to(LibraryFileImpl.class);
		bind(LibraryZip.class).to(LibraryZipImpl.class);
		bind(LibraryBase64.class).to(LibraryBase64Impl.class);
		bind(LibraryUtils.class).to(LibraryUtilsImpl.class);
		bind(PreviewGenerator.class).to(JPedalPreviewGenerator.class).asEagerSingleton();
		bind(DocumentProcessor.class).annotatedWith(OpenDoc.class).to(OpenDocument.class);
		bind(DocumentProcessor.class).annotatedWith(OpenXml.class).to(OpenXmlDocument.class);
		bind(DocumentProcessor.class).annotatedWith(PortableDocument.class).to(PdfProcessor.class);
		bind(DocumentProcessor.class).annotatedWith(XmlDocument.class).to(XmlProcessor.class);
		bind(DocumentReplacer.class).to(DocumentReplacerImpl.class);
		bind(Signer.class).to(PdfSigner.class);
		bind(PdfXmlExtractor.class).to(PdfXmlExtractorImpl.class);
		bind(QueryStore.class).to(QueryStoreImpl.class).asEagerSingleton();
		bind(WorkQueue.class).to(WorkQueueImpl.class);
		bind(WorkQueueRepository.class).to(WorkQueueImpl.class);
		bind(Formatter.class).to(FormatterImpl.class);
		bind(PartsExtractor.class).to(PartsExtractorImpl.class);
		bind(Repository.class).to(DatabaseRepository.class).asEagerSingleton();
		bind(Serializer.class).to(Persister.class);
		bind(DiskManager.class).to(DiskManagerImpl.class).asEagerSingleton();
		bind(UpgradeManager.class).to(UpgradeManagerImpl.class);
		bind(UpgradeScriptClassLoader.class);
		bind(MimeTypes.class);

		bindOperationsFactoryMap();
		bindActionsFactoryMap();

		bind(DataSourceProvider.class).to(DataSourceProviderImpl.class);
		bind(WorkQueueManager.class).to(WorkQueueManagerImpl.class).asEagerSingleton();
		bind(DocumentPreviewsCleaner.class).to(DocumentPreviewsCleanerImpl.class).asEagerSingleton();
		bind(Application.class).asEagerSingleton();
	}

	@Provides
	public Model getModel() {
		return new ModelImpl();
	}

	@Provides
	public PdfConverter getPdfConverter() throws ConfigurationException {
		Logger logger = InjectorFactory.get().getInstance(Logger.class);
		ArrayList<Class<? extends PdfConverter>> pdfConverters = new ArrayList<Class<? extends PdfConverter>>();

		pdfConverters.add(AsposePdfConverter.class);
		pdfConverters.add(PdfConverterDummyImpl.class);

		PdfConverter pdfConverter = null;
		Iterator<Class<? extends PdfConverter>> iterator = pdfConverters.iterator();
		do {
			pdfConverter = (PdfConverter) InjectorFactory.get().getInstance(iterator.next());
		} while (!pdfConverter.check());

		if (pdfConverter instanceof PdfConverterDummyImpl)
			logger.warn("Not found any library to convert documents to Pdf. Any convertion to PDF will fail.");

		return pdfConverter;
	}

	@Provides
	public ModelProducer getModelProducer() {
		Configuration configuration = InjectorFactory.get().getInstance(Configuration.class);
		Logger logger = InjectorFactory.get().getInstance(Logger.class);

		Class<?> modelProducerClass;
		try {
			modelProducerClass = Class.forName(configuration.getString(Configuration.MODEL_PRODUCER_CLASS));
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			return null;
		}

		return (ModelProducer) InjectorFactory.get().getInstance(modelProducerClass);
	}

	private void bindOperationsFactoryMap() {
		MapBinder<Integer, Class<? extends Operation>> operationsMap = MapBinder.newMapBinder(binder(), new TypeLiteral<Integer>() {
		}, new TypeLiteral<Class<? extends Operation>>() {
		});
		operationsMap.addBinding(Operation.OPERATION_CONSOLIDATE_DOCUMENT).toInstance(ConsolidateDocumentOperation.class);
		operationsMap.addBinding(Operation.OPERATION_UPDATE_DOCUMENT).toInstance(UpdateDocumentOperation.class);
		operationsMap.addBinding(Operation.OPERATION_GENERATE_DOCUMENT_PREVIEW).toInstance(GenerateDocumentPreviewOperation.class);
		operationsMap.addBinding(Operation.OPERATION_EXTRACT_ATTACHMENT).toInstance(ExtractAttachmentOperation.class);

		bind(OperationsFactory.class).to(OperationsFactoryImpl.class);
	}

	private void bindActionsFactoryMap() {
		MapBinder<String, Class<? extends Action>> actionsMap = MapBinder.newMapBinder(binder(), new TypeLiteral<String>() {
		}, new TypeLiteral<Class<? extends Action>>() {
		});
		actionsMap.addBinding(ActionFactory.ACTION_PING).toInstance(Ping.class);
		actionsMap.addBinding(ActionFactory.ACTION_UPLOAD_TEMPLATE).toInstance(UploadTemplate.class);
		actionsMap.addBinding(ActionFactory.ACTION_UPLOAD_TEMPLATE_WITH_SIGN).toInstance(UploadTemplateWithSigns.class);
		actionsMap.addBinding(ActionFactory.ACTION_UPLOAD_DOCUMENT).toInstance(UploadDocument.class);
		actionsMap.addBinding(ActionFactory.ACTION_UPLOAD_IMAGE).toInstance(UploadImage.class);
		actionsMap.addBinding(ActionFactory.ACTION_CREATE_DOCUMENT).toInstance(CreateDocument.class);
		actionsMap.addBinding(ActionFactory.ACTION_UPDATE_DOCUMENT).toInstance(UpdateDocument.class);
		actionsMap.addBinding(ActionFactory.ACTION_EXISTS_DOCUMENT).toInstance(ExistsDocument.class);
		actionsMap.addBinding(ActionFactory.ACTION_REMOVE_DOCUMENT).toInstance(RemoveDocument.class);
		actionsMap.addBinding(ActionFactory.ACTION_CONSOLIDATE_DOCUMENT).toInstance(ConsolidateDocument.class);
		actionsMap.addBinding(ActionFactory.ACTION_CONSOLIDATE_DOCUMENT_WITH_SIGNS).toInstance(ConsolidateDocumentWithSigns.class);
		actionsMap.addBinding(ActionFactory.ACTION_REMOVE_ALL_NODE_FILES).toInstance(RemoveAllNodeFiles.class);
		actionsMap.addBinding(ActionFactory.ACTION_GET_DOCUMENT).toInstance(GetDocument.class);
		actionsMap.addBinding(ActionFactory.ACTION_GET_DOCUMENT_XML_DATA).toInstance(GetDocumentXmlData.class);
		actionsMap.addBinding(ActionFactory.ACTION_GET_DOCUMENT_HASH).toInstance(GetDocumentHash.class);
		actionsMap.addBinding(ActionFactory.ACTION_GET_DOCUMENT_CONTENT_TYPE).toInstance(GetDocumentContentType.class);
		actionsMap.addBinding(ActionFactory.ACTION_COPY_DOCUMENT).toInstance(CopyDocument.class);
		actionsMap.addBinding(ActionFactory.ACTION_OVERWRITE_CONTENT).toInstance(OverwriteDocument.class);
		actionsMap.addBinding(ActionFactory.ACTION_PREPARE_DOCUMENT_SIGNATURE).toInstance(PrepareDocumentSignature.class);
		actionsMap.addBinding(ActionFactory.ACTION_STAMP_DOCUMENT_SIGNATURE).toInstance(StampDocumentSignature.class);
		bind(ActionFactory.class).to(ActionFactoryImpl.class);
	}
}