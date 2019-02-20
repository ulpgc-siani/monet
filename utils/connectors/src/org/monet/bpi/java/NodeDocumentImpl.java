package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeDocument;
import org.monet.bpi.NodeDocument;
import org.monet.bpi.Schema;
import org.monet.bpi.types.File;
import org.monet.metamodel.Definition;
import org.simpleframework.xml.core.Persister;

public abstract class NodeDocumentImpl extends NodeImpl implements NodeDocument, BehaviorNodeDocument {

	@Override
	public Schema genericGetSchema() {
		Definition definition = this.dictionary.getDefinition(this.node.getCode());
		Class<Schema> schemaClass = this.bpiClassLocator.getSchemaClass(definition);
		Persister persister = new Persister();
		try {
			String schema = this.api.getNodeSchema(this.node.getId());
			if (!schema.isEmpty())
				return persister.read(schemaClass, schema);
		} catch (Exception e) {
			return null;
		}
		try {
			return schemaClass.newInstance();
		} catch (Exception ex) {
			return null;
		}
	}

	public void consolidate() {
		this.api.saveNode(this.node);
		this.api.consolidateNode(this.node.getId());
		this.api.makeNodeUnEditable(this.node.getId());
	}

	public void overwriteContent(File file) {
		this.api.saveNodeDocument(this.node.getId(), file.getContent(), file.getContentType());
	}

	public File getContent() {
		String nodeId = this.node.getId();

		File file = new File(nodeId);
		file.setContent(this.api.getNodeDocument(nodeId));
		file.setContentType(this.api.getNodeDocumentContentType(nodeId));

		return file;
	}

}

