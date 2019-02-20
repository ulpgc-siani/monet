package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.ConsoleService;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.Dictionary;

public class ConsoleServiceImpl extends ConsoleService {

	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(ConsoleServiceImpl service) {
		instance = service;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public static void init() {
	}

	@Override
	protected void printlnImpl(Object object) {
		System.out.println(String.valueOf(object));
	}

	@Override
	protected void printlnImpl(String message, Throwable exception) {

		if (exception != null)
			exception.printStackTrace();

		System.out.println(message);
	}

}