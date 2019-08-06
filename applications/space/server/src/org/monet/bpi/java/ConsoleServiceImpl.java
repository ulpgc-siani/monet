package org.monet.bpi.java;

import org.monet.bpi.ConsoleService;

public class ConsoleServiceImpl extends ConsoleService {

	public static void init() {
		instance = new ConsoleServiceImpl();
	}

	@Override
	protected void printlnImpl(Object object) {
		System.out.println(object);
	}

	@Override
	protected void printlnImpl(String message, Throwable exception) {

		if (exception != null)
			exception.printStackTrace();

		System.out.println(message);
	}

}