package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.bpi.BPIMonetLink;
import org.monet.bpi.BPINotificationService;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.model.Dictionary;

public class BPINotificationServiceImpl extends BPINotificationService {

	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(BPINotificationServiceImpl service) {
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

	@Override
	public void createForAll(String label, String icon, BPIMonetLink target) {
	}

	@Override
	public void createForTeam(String taskId, String label, String icon, BPIMonetLink target) {
	}

	@Override
	public void createForUser(String userId, String label, String icon, BPIMonetLink target) {
	}

	public static void init() {
		instance = new BPINotificationServiceImpl();
	}

}
