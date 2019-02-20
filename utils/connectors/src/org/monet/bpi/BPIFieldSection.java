package org.monet.bpi;

public interface BPIFieldSection extends BPIField<Void> {

  public <T, F extends BPIField<V>, V> T getField(String definition, String name);
  
}