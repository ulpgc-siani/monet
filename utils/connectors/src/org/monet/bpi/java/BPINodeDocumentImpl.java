package org.monet.bpi.java;


import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPIBehaviorNodeDocument;
import org.monet.bpi.BPINodeDocument;
import org.monet.bpi.BPISchema;
import org.monet.bpi.types.File;

public abstract class BPINodeDocumentImpl<Schema extends BPISchema,
	OperationEnum extends Enum<?>,
	Parent extends BPIBaseNode<?>>
	extends BPINodeImpl<Parent, Schema>
	implements BPINodeDocument<Parent, Schema>, BPIBehaviorNodeDocument<OperationEnum> {

	public void consolidate() {
		this.api.saveNode(this.node);
		this.api.consolidateNode(this.node.getId());
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

