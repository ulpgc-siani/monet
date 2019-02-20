package org.monet.bpi;

import org.monet.bpi.types.File;

public interface Importer {

	public ImporterScope prepareImportOf(NodeDocument node);

	public ImporterScope prepareImportOf(Schema schema);

	public ImporterScope prepareImportOf(File file);

	public ImporterScope prepareImportOf(String url);

	public ImporterScope prepareImportOf(String key, CustomerRequest msg);

	public ImporterScope prepareImportOf(String key, ContestantRequest msg);

	public ImporterScope prepareImportOf(String key, ProviderResponse msg);

}
