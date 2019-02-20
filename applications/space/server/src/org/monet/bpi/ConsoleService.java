package org.monet.bpi;

public abstract class ConsoleService {

	protected static ConsoleService instance;

	public static void println(Object object) {
		instance.printlnImpl(object);
	}

	public static void println(String message, Throwable exception) {
		instance.printlnImpl(message, exception);
	}

	protected abstract void printlnImpl(Object object);

	protected abstract void printlnImpl(String message, Throwable exception);

}
