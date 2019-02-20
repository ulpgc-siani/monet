package org.monet.space.mobile.model;

public class Coordinate {
  public static final double DEFAULT_LATITUDE = 28.0789;
  public static final double DEFAULT_LONGITUDE = -15.4519;
  
  private double lat;
  private double lon;
  
  public Coordinate(double lat, double lon) {
    this.setLat(lat);
    this.setLon(lon);
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }
  
  

}
