package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.bpi.BPIMailService;
import org.monet.bpi.types.File;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.model.Dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class BPIMailServiceImpl extends BPIMailService {

	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	@Override
	public void send(List<String> to, String subject, String content, File... attachments) {
		this.send(to, subject, null, content, attachments);
	}

	public static void injectInstance(BPIMailServiceImpl service) {
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
	public void send(List<String> to, String subject, String htmlContent, String textContent, File... attachments) {
	}

	private class EmailDataSource implements javax.activation.DataSource {

		private File file;

		public EmailDataSource(File file) {
			this.file = file;
		}

		@Override
		public String getContentType() {
			return null;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return null;
		}

		@Override
		public String getName() {
			return this.file.getFilename();
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			return null;
		}

	}

}
