package org.monet.bpi.java;

import org.monet.bpi.Cube;
import org.monet.bpi.Node;
import org.monet.bpi.FactTransformer;

public class FactTransformerImpl implements FactTransformer {

  protected <T extends Cube<?>> T getCube(String cubeName) {
    return null;
  }

  @Override
  public void onTransform(Node node) {
    // TODO Auto-generated method stub
  }
  
}
