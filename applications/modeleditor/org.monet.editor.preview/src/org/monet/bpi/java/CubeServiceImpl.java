package org.monet.bpi.java;

import org.monet.bpi.Cube;
import org.monet.bpi.CubeService;

public class CubeServiceImpl extends CubeService {

  protected Cube<?> getImpl(String name) {
    return null;
  }
  
  @Override
  protected String getDimensionImpl(String key) {
    return null;
  }

  @Override
  protected String getLevelImpl(String key) {
    return null;
  }

  @Override
  protected String getMeasureImpl(String key) {
    return null;
  }

  @Override
  protected String getOperatorImpl(String key) {
    return null;
  }

  protected String generateKeyImpl(String dimensionOrMeasure, String levelOrOperator) {
    return null;
  }

}
