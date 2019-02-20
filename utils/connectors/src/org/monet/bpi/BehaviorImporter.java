package org.monet.bpi;

public interface BehaviorImporter {

	public void onInitialize();

	public void onImportItem(Schema item) throws Exception;

}