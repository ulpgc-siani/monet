package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.MailService;
import org.monet.bpi.types.File;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.Dictionary;

import java.util.List;

public class MailServiceImpl extends MailService {

	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(MailServiceImpl service) {
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
	public void send(String to, String subject, String content, File... attachments) {
		throw new NotImplementedException();
	}

	@Override
	public void send(List<String> toList, String subject, String content, File... attachments) {
		throw new NotImplementedException();
	}

	@Override
	public void send(String to, String subject, String htmlContent, String textContent, File... attachments) {
		throw new NotImplementedException();
	}

	@Override
	public void send(List<String> toList, String subject, String htmlContent, String textContent, File... attachments) {
		throw new NotImplementedException();
	}

	public static void init() {
	}

}
