package org.monet.bpi.java;

import java.util.List;

import org.monet.bpi.Cube;
import org.monet.bpi.CubeFact;
import org.monet.bpi.types.Date;

public class CubeImpl implements Cube {

  protected CubeFact insertFactImpl(Class<? extends CubeFact> cubeFactClass, Date date) {
    return null;
  }
  
  public void save() {
  }

  public void save(List<CubeFact> facts) {
  }

}
