package org.monet.space.mobile.model.schema;

import java.util.Date;
import java.util.Locale;

public class Location {

  public String label;
  public String wkt;
  public Date   timestamp;

  public void setLatLon(double latitude, double longitude, Date timestamp) {
    this.wkt = String.format(Locale.US, "POINT (%f %f)", latitude, longitude);
    this.timestamp = timestamp;
  }

  public double getLatitude() {
    if (this.wkt != null && this.wkt.startsWith("POINT")) {
      return Double.parseDouble(this.wkt.substring(this.wkt.lastIndexOf("(") + 1, this.wkt.lastIndexOf(" ")));
    }
    return 0;
  }

  public double getLongitude() {
    if (this.wkt != null && this.wkt.startsWith("POINT")) {
      return Double.parseDouble(this.wkt.substring(this.wkt.lastIndexOf(" ") + 1, this.wkt.lastIndexOf(")")));
    }
    return 0;
  }

}
