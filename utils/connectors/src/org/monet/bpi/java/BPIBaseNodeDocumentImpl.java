package org.monet.bpi.java;


import org.monet.bpi.BPIBaseNodeDocument;
import org.monet.bpi.BPIBehaviorNodeDocument;
import org.monet.bpi.BPISchema;

public abstract class BPIBaseNodeDocumentImpl<Schema extends BPISchema,
	OperationEnum extends Enum<?>>
	extends BPIBaseNodeImpl<Schema>
	implements BPIBaseNodeDocument<Schema>, BPIBehaviorNodeDocument<OperationEnum> {

	public void consolidate() {
		this.api.saveNode(this.node);
		this.api.consolidateNode(this.node.getId());
	}

}
