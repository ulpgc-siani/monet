package org.monet.bpi;


public interface BPIBehaviorImporterContextual<Schema> extends BPIBehaviorImporter<Schema> {

  public void onInitialize(BPIBaseNode<?> context);

}