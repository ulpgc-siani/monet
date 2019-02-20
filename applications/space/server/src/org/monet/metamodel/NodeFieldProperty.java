package org.monet.metamodel;

// NodeFieldDeclaration
// Declaración que se utiliza para modelar un campo nodo

public class NodeFieldProperty extends NodeFieldPropertyBase {

	public boolean isAggregateRelation() {
		return this._addProperty != null && this._addProperty._node.size() > 0;
	}

}
