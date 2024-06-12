package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.*;
import org.monet.v3.BPIClassLocator;
import org.monet.metamodel.Dictionary;
import org.monet.metamodel.DocumentDefinition;
import org.apache.commons.lang.NotImplementedException;

public abstract class ExporterImpl implements Exporter, BehaviorExporter {
	private Node scope;
	public NodeDocumentImpl destination;
	private BPIClassLocator bpiClassLocator;
	private Dictionary dictionary;
	private BackserviceApi api;

	public void injectScope(Node scope) {
		this.scope = scope;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	protected Node getScope() {
		return this.scope;
	}

	public void execute() {
		throw new NotImplementedException();
	}

	@Override
	public void onInitialize() {
	}

	public void onExportItem(Schema schema) {
	}

	public abstract ExporterScope prepareExportOf(Node node);

	public class ExporterScopeImpl implements ExporterScope {
		private BackserviceApi api;
		private Dictionary dictionary;
		private BPIClassLocator bpiClassLocator;

		public ExporterScopeImpl(Node scope) {
			ExporterImpl.this.scope = scope;
		}

		protected void internalToDocument(NodeDocument document) {
			ExporterImpl.this.destination = (NodeDocumentImpl) document;
			ExporterImpl.this.execute();
		}

		protected NodeDocument internalToNewDocument(String code) {
			DocumentDefinition definition = (DocumentDefinition)this.dictionary.getDefinition(code);
			org.monet.api.space.backservice.impl.model.Node node = this.api.createNode(definition.getCode(), null);
			NodeDocumentImpl bpiNode = bpiClassLocator.instantiateBehaviour(definition);
			bpiNode.injectNode(node);

			this.internalToDocument(bpiNode);

			return bpiNode;
		}

	}

}
