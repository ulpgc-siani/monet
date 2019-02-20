package org.monet.space.mobile.model;

public class SourceFilter {

  public long   id;
  public String label;

  public SourceFilter(long id, String label) {
    this.id = id;
    this.label = label;
  }

  @Override
  public String toString() {
    return this.label;
  }

}
