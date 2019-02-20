package org.monet.bpi.types.location;

public abstract class Geometry {

  public boolean contains(Geometry g) {
    return false;
  }
  
  public boolean within(Geometry g) {
    return false;
  }
  
  public boolean covers(Geometry g) {
    return false;
  }
  
  public boolean crosses(Geometry g) {
    return false;
  }
  
  public double distance(Geometry g) {
    return -1;
  }
  
  public boolean equals(Geometry g) {
    return false;
  }
  
  public boolean equals(Geometry g, double tolerance) {
    return false;
  }
  
  public double getArea() {
    return -1;
  }
  
  public Point getCentroid() {
    return null;
  }
  
  public Geometry getBoundingBox() {
    return null;
  }
  
  public boolean isWithinDistance(Geometry geom, double distance) {
    return false;
  }
  
  public boolean touches(Geometry g) {
    return false;
  }
  
  public Geometry union() {
    return null;
  }
  
  public String toString() {
    return null;
  }
  
}
