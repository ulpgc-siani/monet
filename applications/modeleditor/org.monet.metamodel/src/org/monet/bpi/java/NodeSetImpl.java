package org.monet.bpi.java;

import java.util.HashMap;
import java.util.Map;

import org.monet.bpi.Expression;
import org.monet.bpi.NodeSet;

public abstract class NodeSetImpl 
  extends NodeImpl 
  implements NodeSet {

  @Override
  public long getCount(Expression where) {
    return -1;
  }

  @Override
  public Map<String, String> getParameters() {
    return new HashMap<String, String>();
  }

  @Override
  public void addParameter(String name, String value) {    
  }

  @Override
  public void deleteParameter(String name) {    
  }
  
}
