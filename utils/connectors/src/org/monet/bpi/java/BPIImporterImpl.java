package org.monet.bpi.java;

import org.monet.bpi.BPIBehaviorImporter;

public abstract class BPIImporterImpl<Schema> implements BPIBehaviorImporter<Schema> {
  
  @Override
  public abstract void onInitialize();
  
  @Override
  public abstract void onImportItem(Schema item) throws Exception;

  @SuppressWarnings("unchecked")
  void importItem(Object item) throws Exception {
    this.onImportItem((Schema)item);
  }
  
}
