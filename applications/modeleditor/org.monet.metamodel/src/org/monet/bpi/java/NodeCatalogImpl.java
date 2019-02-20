package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeCatalog;
import org.monet.bpi.Expression;
import org.monet.bpi.IndexEntry;
import org.monet.bpi.NodeCatalog;
import org.monet.bpi.OrderExpression;

public abstract class NodeCatalogImpl 
  extends NodeSetImpl 
  implements NodeCatalog, BehaviorNodeCatalog {

  protected Iterable<?> genericGetAll() {
    return genericGet(null, null);
  }
  
  protected Iterable<?> genericGetAll(OrderExpression orderBy) {
    return genericGet(null, orderBy);
  }
  
  protected Iterable<?> genericGet(Expression where) {
    return null;
  }
  
  protected Iterable<?> genericGet(Expression where, OrderExpression orderBy) {
    return null;
  }
  
  protected IndexEntry genericGetFirst(Expression where) {
    return null;
  }
  
}
