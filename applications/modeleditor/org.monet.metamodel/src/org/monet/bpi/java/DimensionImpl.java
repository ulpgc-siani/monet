package org.monet.bpi.java;

import org.monet.bpi.Dimension;
import org.monet.bpi.DimensionComponent;

public class DimensionImpl implements Dimension {

  protected DimensionComponent insertComponentImpl(Class<? extends DimensionComponent> dimensionComponentClass, String id) {
    return null;
  }
  
  @Override
  public void save() {
  }

}
