package org.monet.bpi;

public abstract class DatastoreService {

	protected static DatastoreService instance;

	public static Datastore get(String name) {
		return instance.getImpl(name);
	}

	protected abstract Datastore getImpl(String name);

}