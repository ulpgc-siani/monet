package org.monet.bpi.java;

import org.monet.bpi.*;
import org.monet.metamodel.DocumentDefinition;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.utils.PersisterHelper;

public abstract class ExporterImpl implements Exporter, BehaviorExporter {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();
	NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

	private Node scope;
	public NodeDocumentImpl destination;

	public void injectScope(Node scope) {
		this.scope = scope;
	}

	protected Node getScope() {
		return this.scope;
	}

	public void execute() {
		Schema schema = (Schema) bpiClassLocator.instantiateSchema(this.destination.node.getDefinition());
		try {
			this.onExportItem(schema);
			this.destination.node.setSchema(PersisterHelper.save(schema));
			this.destination.save();
		} catch (Throwable ex) {
			throw new RuntimeException(String.format("Error exporting node %s: %s", this.destination.node.getId(), ex.toString()), ex);
		}
	}

	@Override
	public void onInitialize() {

	}

	public void onExportItem(Schema schema) {
	}

	public abstract ExporterScope prepareExportOf(Node node);

	public class ExporterScopeImpl implements ExporterScope {

		public ExporterScopeImpl(Node scope) {
			ExporterImpl.this.scope = scope;
		}

		protected void internalToDocument(NodeDocument document) {
			ExporterImpl.this.destination = (NodeDocumentImpl) document;
			ExporterImpl.this.execute();
		}

		protected NodeDocument internalToNewDocument(String code) {
			DocumentDefinition definition = Dictionary.getInstance().getDocumentDefinition(code);
			org.monet.space.kernel.model.Node node = nodeLayer.addNode(definition.getCode());
			NodeDocumentImpl bpiNode = bpiClassLocator.instantiateBehaviour(definition);
			bpiNode.injectNode(node);

			this.internalToDocument(bpiNode);

			return bpiNode;
		}

	}

}
