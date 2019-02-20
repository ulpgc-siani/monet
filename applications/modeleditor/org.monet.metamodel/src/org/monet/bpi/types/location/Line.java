package org.monet.bpi.types.location;

public class Line extends Geometry {

  public Line(Point[] points) {
    
  }
  
  public Line(String wkt) {
    
  }
  
  public Point getPoint(int index) {
    return null;
  }
  
  public int getPointsCount() {
    return -1;
  }
  
  public double length() {
    return -1;
  }
  
  public boolean isClosed() {
    return false;
  }
  
  public boolean isRing() {
    return false;
  }
  
  public Line reverse() {
    return null;
  }
  
}
