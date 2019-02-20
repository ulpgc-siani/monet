package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeCollection;
import org.monet.bpi.Expression;
import org.monet.bpi.IndexEntry;
import org.monet.bpi.Node;
import org.monet.bpi.NodeCollection;
import org.monet.bpi.OrderExpression;

public abstract class NodeCollectionImpl 
  extends NodeImpl 
  implements NodeCollection, BehaviorNodeCollection {

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
  

  @Override
  public long getCount(Expression where) {
    return -1;
  }

  @Override
  public void remove(Node node) {
    
  };
  
  public Node addNode(Class<? extends Node> definition) {
    return null;
  }

  @Override
  public void onItemAdded(Node newItem) {
  }

  @Override
  public void onItemRemoved(Node removedItem) {
  }
    
}
