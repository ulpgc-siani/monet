package org.monet.bpi.java;

import org.monet.bpi.*;
import org.monet.bpi.types.File;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentRestfullClient;
import org.monet.space.kernel.agents.AgentRestfullClient.Result;
import org.monet.space.kernel.bpi.java.importer.ImportIterable;
import org.monet.space.kernel.bpi.java.importer.ImportIterator;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.library.LibraryPDF;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;
import org.monet.space.kernel.model.BusinessModel;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.PersisterHelper;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.*;

public abstract class ImporterImpl implements Importer, BehaviorImporter {

	private Schema schema;
	private File file;
	private String url;
	private InputStream inputStream;
	private Node scope;

	@Override
	public void onInitialize() {

	}

	public void onImportItem(Schema item) {
	}

	protected abstract Class<?> getTargetSchemaClass();

	void genericSetTarget(NodeDocument node) {
		this.schema = node.genericGetSchema();
	}

	void genericSetScope(Node node) {
		this.scope = node;
	}

	public void injectScope(Node scope) {
		this.scope = scope;
	}

	protected Node getScope() {
		return this.scope;
	}

	private void importItem(Schema schema) {
		try {
			this.onImportItem(schema);
		} catch (Throwable ex) {
			throw new RuntimeException("Error on importing items: " + ex.getMessage(), ex);
		}
	}

	private void execute() throws Exception {
		this.onInitialize();

		if (this.schema != null) {
			this.importItem(schema);
		} else if (this.file != null) {
			if (this.file.isStoredAtDocEngine()) {
				String contentType = this.file.getContentType().toLowerCase();
				String content = null;

				if (MimeTypes.PDF.equals(contentType)) {
					content = ComponentDocuments.getInstance().getDocumentSchema(this.file.getFilename());
					this.importItem((Schema) PersisterHelper.load(content, this.getTargetSchemaClass()));
				}
				else if (contentType.contains("/xml")) {
					content = new String(this.file.getContent());
					ImportIterator<Schema> iterator = new ImportIterator<Schema>(this.getTargetSchemaClass(), new StringReader(content), content.length(), "schema");
					for (Schema schema : new ImportIterable<Schema>(iterator)) {
						this.importItem(schema);
					}
				}
				else
					throw new RuntimeException("Source of import not supported");

			} else {
				String contentType = this.file.getSource() != null ? this.file.getSource().getContentType() : MimeTypes.XML;
				Reader sourceReader = null;
				long size = -1;
				try {
					InputStream stream = streamOf(this.file);
					size = stream != null ? stream.available() : 0;
					byte[] data = StreamHelper.readBytes(stream);
					String content = new String(data);
					if (content.contains(Message.REFERENCED_DOCUMENT_MESSAGE)) {
						String documentReferenced = content.replace(Message.REFERENCED_DOCUMENT_MESSAGE, "").replace("\r\n", "");
						content = ComponentDocuments.getInstance().getDocumentSchema(documentReferenced);
						this.importItem((Schema) PersisterHelper.load(content, this.getTargetSchemaClass()));
					}else {
						stream = new ByteArrayInputStream(data);
						if (MimeTypes.PDF.equals(contentType)) {
							content = LibraryPDF.extractXmlData(stream);
							sourceReader = new StringReader(content);
						} else if (MimeTypes.XML.equals(contentType)) {
							sourceReader = AgentFilesystem.getReader(stream);
						} else {
							throw new RuntimeException("Source of import not supported");
						}

						ImportIterator<Schema> iterator = new ImportIterator<Schema>(this.getTargetSchemaClass(), sourceReader, size, "schema");
						for (Schema schema : new ImportIterable<Schema>(iterator)) {
							this.importItem(schema);
						}
					}

				} finally {
					StreamHelper.close(sourceReader);
				}
			}
		} else if (this.url != null || this.inputStream != null) {
			InputStream contentStream = null;
			Reader sourceReader = null;
			long size = 0;
			try {
				if (this.inputStream == null) {
					Result result = AgentRestfullClient.getInstance().executeGet(this.url.toString());
					contentStream = result.content;
					size = result.size;
				} else {
					contentStream = this.inputStream;
					size = -1;
				}
				sourceReader = new InputStreamReader(contentStream, "UTF-8");
				ImportIterator<Schema> iterator = new ImportIterator<Schema>(this.getTargetSchemaClass(), sourceReader, size, "schema");
				for (Schema schema : new ImportIterable<Schema>(iterator)) {
					this.importItem(schema);
				}
			} finally {
				StreamHelper.close(contentStream);
				StreamHelper.close(sourceReader);
			}
		}
	}

	private InputStream streamOf(File file) {
		try {
			java.io.File javaFile = new java.io.File(file.getFilename());

			if (!javaFile.exists() && this.file.getSource() != null)
				javaFile = file.getSource().getFile();

			return javaFile != null ? new FileInputStream(javaFile) : new ByteArrayInputStream(file.getContent());
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public ImporterScope prepareImportOf(String key, CustomerRequest msg) {
		try {
			MessageAttach attach = ((CustomerRequestImpl) msg).message.getAttachment(key);
			if (attach == null)
				throw new RuntimeException("No attach with key " + key);
			if (attach.getContentType().equals(MimeTypes.XML))
				return this.prepareImportOf(attach.getInputStream());
			else
				return this.prepareImportOf(new File(attach));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ImporterScope prepareImportOf(String key, ContestantRequest msg) {
		try {
			MessageAttach attach = ((ContestantRequestImpl) msg).message.getAttachment(key);
			if (attach == null)
				throw new RuntimeException("No attach with key " + key);
			if (attach.getContentType().equals(MimeTypes.XML))
				return this.prepareImportOf(attach.getInputStream());
			else
				return this.prepareImportOf(new File(attach));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ImporterScope prepareImportOf(String key, ProviderResponse msg) {
		try {
			MessageAttach attach = ((ProviderResponseImpl) msg).message.getAttachment(key);
			if (attach == null)
				throw new RuntimeException("No attach with key " + key);
			if (attach.getContentType().equals(MimeTypes.XML))
				return this.prepareImportOf(attach.getInputStream());
			else
				return this.prepareImportOf(new File(attach));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ImporterScope prepareImportOf(NodeDocument node) {
		this.schema = node.genericGetSchema();
		return new ImporterScopeImpl();
	}

	public ImporterScope prepareImportOf(Schema schema) {
		this.schema = schema;
		return new ImporterScopeImpl();
	}

	public ImporterScope prepareImportOf(File file) {
		this.file = file;
		if (this.file.isModelResource())
			this.file = new File(BusinessModel.getInstance().getAbsoluteFilename(this.file.getFilename()), true);
		return new ImporterScopeImpl();
	}

	public ImporterScope prepareImportOf(String url) {
		this.url = url;
		return new ImporterScopeImpl();
	}

	public ImporterScope prepareImportOf(InputStream stream) {
		this.inputStream = stream;
		return new ImporterScopeImpl();
	}

	private class ImporterScopeImpl implements ImporterScope {

		@Override
		public void atScope(Node scope) {
			ImporterImpl.this.scope = scope;
			try {
				execute();
			} catch (Throwable ex) {
				throw new RuntimeException("Error importing items: " + ex.getMessage(), ex);
			}
		}

		@Override
		public void atGlobalScope() {
			this.atScope(null);
		}

	}

}
