package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.*;
import org.monet.bpi.types.File;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.AgentFilesystem;
import org.monet.v3.model.Dictionary;

import java.io.InputStream;

public abstract class ImporterImpl implements Importer, BehaviorImporter {

	private Schema schema;
	private File file;
	private String url;
	private InputStream inputStream;
	private Node scope;
	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

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

		if (this.file == null)
			throw new NotImplementedException();

		String scopeId = null;
		Node scope = this.getScope();
		if (scope != null)
			scopeId = scope.toLink().getId();

		this.api.importNode(this.getClass().getName(), scopeId, AgentFilesystem.readFile(this.file.getFilename()));
	}

	public ImporterScope prepareImportOf(String key, CustomerRequest msg) {
		throw new NotImplementedException();
	}

	public ImporterScope prepareImportOf(String key, ContestantRequest msg) {
		throw new NotImplementedException();
	}

	public ImporterScope prepareImportOf(String key, ProviderResponse msg) {
		throw new NotImplementedException();
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
			this.file = new File(this.file.getFilename(), true);
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
