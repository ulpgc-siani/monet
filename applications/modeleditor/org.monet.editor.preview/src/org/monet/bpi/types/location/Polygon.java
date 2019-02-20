package org.monet.bpi.types.location;

public class Polygon extends Geometry {
  
  public Polygon(Line border, Line[] holes) {
    
  }
  
  public Polygon(String wkt) {
    
  }
  
  public Line getBorder() {
    return null;
  }
  
  public int getHolesCount() {
    return -1;
  }
  
  public Line getHole(int index) {
    return null;
  }
  
}
