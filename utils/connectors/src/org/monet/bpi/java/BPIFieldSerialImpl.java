package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Indicator;
import org.monet.bpi.BPIFieldSerial;

public class BPIFieldSerialImpl extends BPIFieldImpl<String> implements BPIFieldSerial {
  
  @Override
  public String get() {
    return this.getIndicatorValue(Indicator.VALUE);
  }

  @Override
  public void set(String value) {
    this.setIndicatorValue(Indicator.VALUE, value);
  }

  @Override
  public boolean equals(Object value) {
    if(value instanceof String)
      return this.get().equals(value);
    else
      return false;
  }

  @Override
  public void clear() {
    this.set("");
  }
  
}