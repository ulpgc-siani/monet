package org.monet.bpi.java;

import org.monet.bpi.Cube;
import org.monet.bpi.Datastore;
import org.monet.bpi.Dimension;

public class DatastoreImpl implements Datastore {
  
  protected Dimension getDimension(Class<? extends Dimension> dimensionClass, String name) {
    return null;
  }

  protected Cube getCube(Class<? extends Cube> cubeClass, String name) {
    return null;
  }
  
}
