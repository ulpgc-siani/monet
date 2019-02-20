package org.monet.bpi;

public abstract class SourceService {

	protected static SourceService instance;

	public static Source get(String name, String url) {
		return instance.getImpl(name, url);
	}

	protected abstract Source getImpl(String name, String url);

}