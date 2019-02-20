package org.monet.bpi.java;

import org.monet.bpi.*;

public abstract class BPIBaseNodeCatalogImpl<Schema extends BPISchema,
	OperationEnum extends Enum<?>,
	Reference extends BPIReference>
	extends BPIBaseNodeImpl<Schema>
	implements BPIBaseNodeCatalog<Schema>, BPIBehaviorNodeCatalog<OperationEnum> {

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
