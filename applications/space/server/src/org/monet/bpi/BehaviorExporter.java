package org.monet.bpi;

public interface BehaviorExporter {

	public void onInitialize();

	public void onExportItem(Schema item) throws Exception;

}