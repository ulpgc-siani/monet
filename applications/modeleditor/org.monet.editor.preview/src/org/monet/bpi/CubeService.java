package org.monet.bpi;

public abstract class CubeService {
  
  protected static CubeService instance;
  
  public static Cube<?> get(String name) {
    return instance.getImpl(name);
  }
  
  public static String generateKey(String dimensionOrMeasure, String levelOrOperator) {
    return instance.generateKeyImpl(dimensionOrMeasure, levelOrOperator);
  }
  
  public static String getDimension(String key) {
    return instance.getDimensionImpl(key);
  }

  public static String getLevel(String key) {
    return instance.getLevelImpl(key);
  }

  public static String getMeasure(String key) {
    return instance.getMeasureImpl(key);
  }

  public static String getOperator(String key) {
    return instance.getOperatorImpl(key);
  }
  
  protected abstract Cube<?> getImpl(String name);
  
  protected abstract String generateKeyImpl(String dimensionOrMeasure, String levelOrOperator);
  
  protected abstract String getDimensionImpl(String key);
  
  protected abstract String getLevelImpl(String key);

  protected abstract String getMeasureImpl(String key);
  
  protected abstract String getOperatorImpl(String key);
  
}