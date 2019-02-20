package org.monet.space.mobile.model;

public class SourceListItem {

  public long   id;
  public String label;

  public SourceListItem(long id, String label) {
    this.id = id;
    this.label = label;
  }

  @Override
  public String toString() {
    return this.label;
  }

}
