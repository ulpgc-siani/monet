package org.monet.bpi.types;


public class Location {

  private java.util.Date   timestamp;

  private String wkt;
  
  public void commit() {
    //Creates Geometry from WKT
  }

  public java.util.Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(java.util.Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getWkt() {
    return wkt;
  }

  public void setWkt(String wkt) {
    this.wkt = wkt;
  }

}
