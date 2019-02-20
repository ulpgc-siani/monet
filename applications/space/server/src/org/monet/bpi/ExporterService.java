package org.monet.bpi;

public abstract class ExporterService {

	protected static ExporterService instance;

	public static Exporter get(String name) {
		return instance.getImpl(name);
	}

	protected abstract Exporter getImpl(String name);

}