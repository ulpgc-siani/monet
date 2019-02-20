package org.monet.bpi;

public interface BPIBehaviorImporter<Schema> {
	
  public void onInitialize();

  public void onImportItem(Schema item) throws Exception;
	
}