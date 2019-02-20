package org.monet.bpi.java;

import org.monet.bpi.*;

public abstract class BPINodeCatalogImpl<Schema extends BPISchema,
	OperationEnum extends Enum<?>,
	Parent extends BPIBaseNode<?>,
	Reference extends BPIReference>
	extends BPINodeImpl<Parent, Schema>
	implements BPINodeCatalog<Parent, Schema>, BPIBehaviorNodeCatalog<OperationEnum> {

	public Iterable<Reference> get(BPIExpression where) {
		return get(where, null);
	}

	public Iterable<Reference> get(BPIExpression where, BPIOrderExpression orderBy) {
		BPICollectionIterableImpl<Reference> bpiReference = new BPICollectionIterableImpl<Reference>(this.node, where, orderBy);
		bpiReference.injectDictionary(this.dictionary);
		bpiReference.injectApi(this.api);
		bpiReference.injectBPIClassLocator(this.bpiClassLocator);
		return bpiReference;
	}

}
