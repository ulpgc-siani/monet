package org.monet.mocks.businessunit;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public interface Service {

	public interface Message {
		public Map<String, File> getAttachments();
		public void addAttachment(String key, File file);
		public String getContent();
		public void setContent(String content);
	}

	public String requestService(String serviceUrl, boolean urgent, Date startDate, Date endDate, String comments);
	public boolean sendMessage(String mailboxServiceUrl, String code, Message message);
	public Message readMessage(InputStream message);
}
