package org.monet.bpi;

public abstract class ImporterService {

	protected static ImporterService instance;

	public static Importer get(String name) {
		return instance.getImpl(name);
	}

	protected abstract Importer getImpl(String name);

}