package org.monet.bpi;

import org.monet.bpi.types.File;

import java.util.List;

public abstract class MailService {

	protected static MailService instance;

	public static MailService getInstance() {
		return instance;
	}

	public abstract void send(String to, String subject, String content, File... attachments);

	public abstract void send(List<String> toList, String subject, String content, File... attachments);

	public abstract void send(String to, String subject, String htmlContent, String textContent, File... attachments);

	public abstract void send(List<String> toList, String subject, String htmlContent, String textContent, File... attachments);

}