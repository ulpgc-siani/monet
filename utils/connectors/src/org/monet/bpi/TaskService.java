package org.monet.bpi;

public abstract class TaskService {

	protected static TaskService instance;

	public static org.monet.bpi.Task get(String id) {
		return instance.getImpl(id);
	}

	public static org.monet.bpi.Task create(Class<? extends org.monet.bpi.Task> clazz) {
		return instance.createImpl(clazz);
	}

	protected abstract org.monet.bpi.Task getImpl(String id);

	protected abstract org.monet.bpi.Task createImpl(Class<? extends org.monet.bpi.Task> clazz);

}