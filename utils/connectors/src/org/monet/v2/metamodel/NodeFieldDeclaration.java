package org.monet.v2.metamodel;


// NodeFieldDeclaration
// Declaración que se utiliza para modelar un campo nodo

public class NodeFieldDeclaration extends NodeFieldDeclarationBase {

  public boolean isAggregateRelation() {
    return this._addList.size() > 0;
  }

}
