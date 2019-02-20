package org.monet.bpi.java;

import org.apache.commons.mail.*;
import org.monet.bpi.MailService;
import org.monet.bpi.types.File;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.utils.MimeTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class MailServiceImpl extends MailService {

	@Override
	public void send(String to, String subject, String content, File... attachments) {
		Email email = null;

		try {
			email = this.createEmail(false, attachments.length > 0);
			email.addBcc(to);
		} catch (Throwable ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}

		this.send(email, subject, null, content, attachments);
	}

	@Override
	public void send(List<String> toList, String subject, String content, File... attachments) {
		Email email = null;

		try {
			email = this.createEmail(false, attachments.length > 0);

			for (String to : toList)
				email.addBcc(to);
		} catch (Throwable ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}

		this.send(email, subject, null, content, attachments);
	}

	@Override
	public void send(String to, String subject, String htmlContent, String textContent, File... attachments) {
		Email email = null;

		try {
			email = this.createEmail(false, attachments.length > 0);
			email.addBcc(to);
		} catch (Throwable ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}

		this.send(email, subject, htmlContent, textContent, attachments);
	}

	@Override
	public void send(List<String> toList, String subject, String htmlContent, String textContent, File... attachments) {
		Email email = null;

		try {
			email = this.createEmail(false, attachments.length > 0);
			for (String to : toList)
				email.addBcc(to);
		} catch (Throwable ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}

		this.send(email, subject, htmlContent, textContent, attachments);
	}

	public static void init() {
		instance = new MailServiceImpl();
	}

	private Email createEmail(boolean containsHtml, boolean containsAttachments) {
		return containsHtml ? new HtmlEmail() : containsAttachments ? new MultiPartEmail() : new SimpleEmail();
	}

	private void send(Email email, String subject, String htmlContent, String textContent, File... attachments) {
		Configuration configuration = Configuration.getInstance();

		try {
			email.setHostName(configuration.getValue(Configuration.MAIL_ADMIN_HOST));
			email.setAuthentication(configuration.getValue(Configuration.MAIL_ADMIN_USERNAME), configuration.getValue(Configuration.MAIL_ADMIN_PASSWORD));
			email.setSmtpPort(Integer.valueOf(configuration.getValue(Configuration.MAIL_ADMIN_PORT)));
			email.setSSL(Boolean.valueOf(configuration.getValue(Configuration.MAIL_ADMIN_SECURE)));

			String tls = configuration.getValue(Configuration.MAIL_ADMIN_TLS);
			if (!tls.isEmpty()) email.setTLS(Boolean.valueOf(tls));

			email.setFrom(configuration.getValue(Configuration.MAIL_ADMIN_FROM));

			email.setSubject(subject);
			if (htmlContent != null) {
				HtmlEmail htmlEmail = (HtmlEmail) email;
				htmlEmail.setHtmlMsg(htmlContent);
				htmlEmail.setTextMsg(textContent);
			} else {
				email.setMsg(textContent);
			}

			if (attachments.length > 0) {
				MultiPartEmail multipartEmail = (MultiPartEmail) email;
				for (File file : attachments)
					multipartEmail.attach(new EmailDataSource(file), file.getFilename(), file.getFilename());
			}

			email.send();
		} catch (EmailException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	private class EmailDataSource implements javax.activation.DataSource {

		private File file;

		public EmailDataSource(File file) {
			this.file = file;
		}

		@Override
		public String getContentType() {
			return MimeTypes.getInstance().getFromFilename(this.file.getFilename());
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return ComponentDocuments.getInstance().getDocumentContent(this.file.getFilename());
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
