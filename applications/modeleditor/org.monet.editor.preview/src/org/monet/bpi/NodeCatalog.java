package org.monet.bpi;


public interface NodeCatalog extends Node {

  public long getCount(Expression where);
}
