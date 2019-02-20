package org.monet.bpi;

public abstract class JobService {

	protected static JobService instance;

	public static Job get(String id) {
		return instance.getImpl(id);
	}

	public static Job create(Class<? extends Job> clazz) {
		return instance.createImpl(clazz);
	}

	protected abstract Job getImpl(String id);

	protected abstract Job createImpl(Class<? extends Job> clazz);

}