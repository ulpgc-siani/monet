package org.jsmtpd.plugins.deliveryServices;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.delivery.FatalDeliveryException;
import org.jsmtpd.core.common.delivery.IDeliveryService;
import org.jsmtpd.core.common.delivery.TemporaryDeliveryException;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.core.mail.EmailAddress;
import org.jsmtpd.core.mail.Rcpt;
import org.jsmtpd.tools.ByteArrayTool;
import org.monet.api.space.mailservice.impl.MailServiceApiImpl;
import org.monet.api.space.mailservice.impl.model.Files;

public class MonetMailbox implements IDeliveryService {
	private Log log = LogFactory.getLog(MonetMailbox.class);
	private String url = "";
	private String certificateFile = "";
	private String certificatePassword = "";

	public void setTryChown(boolean tryChown) {
	}

	public void doDelivery(Email in, List<Rcpt> rcpts) {
		log.debug("Begin  ");
		for (Rcpt rcpt : rcpts) {
			try {
				doSingleDelivery(in, rcpt.getEmailAddress());
				rcpt.setDelivered(Rcpt.STATUS_DELIVERED);
			} catch (FatalDeliveryException e) {
				rcpt.setDelivered(Rcpt.STATUS_ERROR_FATAL);
			} catch (TemporaryDeliveryException e) {
				rcpt.setDelivered(Rcpt.STATUS_ERROR_NOT_FATAL);
			}
		}
		log.debug("end");
	}

	/**
	 * Delivers to one box
	 * 
	 * @param in
	 *          the message
	 * @param rcpt
	 *          one recipient
	 * @throws FatalDeliveryException
	 * @throws TemporaryDeliveryException
	 */
	public void doSingleDelivery(Email in, EmailAddress rcpt) throws FatalDeliveryException, TemporaryDeliveryException {

		byte[] pattern = { '\n', 'F', 'r', 'o', 'm', ' ' };
		byte[] replace = { '\n', '>', 'F', 'r', 'o', 'm', ' ' };
		// convert crlf to lf (unix text files)
		byte[] tempo = ByteArrayTool.replaceBytes(in.getDataAsByte(), ByteArrayTool.CRLF, ByteArrayTool.LF);
		// preserve from :
		tempo = ByteArrayTool.replaceBytes(tempo, pattern, replace);

		String message = new String(tempo);
		String from = in.getFrom().getUser() + "@" + in.getFrom().getHost();
		String body = getBody(message);
		Files files = getFiles(message);

		log.info("From: " + from);
		log.info("Body: " + body);

		org.monet.api.space.mailservice.impl.model.Email email = new org.monet.api.space.mailservice.impl.model.Email();
		email.setFrom(from);
		email.setBody(getBody(message));
		email.setFiles(files);

		MailServiceApiImpl mailServiceApi = new MailServiceApiImpl(url, certificateFile, certificatePassword);
		mailServiceApi.sendMail(email);

	}

	public String getPluginName() {
		return "MonetMailbox plugin";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see smtpd.common.IGenericPlugin#initPlugin()
	 */
	public void initPlugin() throws PluginInitException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsmtpd.common.IGenericPlugin#shutdownPlugin()
	 */
	public void shutdownPlugin() {

	}

	// Autoconf
	public void setUrl(String url) {
		this.url = url;
	}

	public void setCertificateFile(String certificateFile) {
		this.certificateFile = certificateFile;
	}

	public void setCertificatePassword(String certificatePassword) {
		this.certificatePassword = certificatePassword;
	}

	private String getBody(String message) {
		String header = getHeader(message);
		String content = getContent(message);
		String body = "";
		String contentTypeCompare = "";

		String contentType = getContentType(header);

		contentTypeCompare = "text/plain";
		if (contentType.substring(0, contentTypeCompare.length()).equals(contentTypeCompare)) {
			body = content;
		} else {

			contentTypeCompare = "multipart/mixed";
			if (contentType.substring(0, contentTypeCompare.length()).equals(contentTypeCompare)) {
				String boundary = getBoundary(header);

				StringReader reader = new StringReader(content);
				LineNumberReader lineReader = new LineNumberReader(reader);
				String line = null;
				try {
					Boolean isbody = false;
					Boolean isboundary = false;

					while (((line = lineReader.readLine()) != null) && (!isboundary)) {
						if (line.contains(boundary))
							isboundary = true;
					}

					isboundary = false;
					while (((line = lineReader.readLine()) != null) && (!isboundary)) {

						if (!isboundary && line.contains(boundary))
							isboundary = true;

						if (isbody && !isboundary)
							body = body + line + "\n";

						if (!isbody && line.equals(""))
							isbody = true;

					}
					body = body.substring(0, body.length() - 2);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return body;
	}

	private String getHeader(String message) {
		String header = "";

		StringReader reader = new StringReader(new String(message));
		LineNumberReader lineReader = new LineNumberReader(reader);
		String line = null;
		try {
			Boolean isbody = false;
			while (((line = lineReader.readLine()) != null) && (!isbody)) {
				if (!isbody && line.equals(""))
					isbody = true;
				header = header + line;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return header;
	}

	private String getContent(String message) {
		String content = "";

		StringReader reader = new StringReader(new String(message));
		LineNumberReader lineReader = new LineNumberReader(reader);
		String line = null;
		try {
			Boolean isbody = false;
			while (((line = lineReader.readLine()) != null)) {
				if (isbody)
					content = content + line + "\n";

				if (!isbody && line.equals(""))
					isbody = true;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	private String getBoundary(String header) {
		String boundary = "";

		Pattern pattern = Pattern.compile("boundary=\"(.*)\"");
		Matcher matcher = pattern.matcher(header);
		if (matcher.find()) {
			boundary = matcher.group(1);
		}

		return boundary;
	}

	private String getContentType(String header) {
		String contentType = "";

		Pattern pattern = Pattern.compile("Content-Type: (.*);");
		Matcher matcher = pattern.matcher(header);
		if (matcher.find()) {
			contentType = matcher.group(1);
		}

		return contentType;
	}

	private String getFileName(String fileHeader) {
		String data = "";

		Pattern pattern = Pattern.compile("filename=\"(.*)\"");
		Matcher matcher = pattern.matcher(fileHeader);
		if (matcher.find()) {
			data = matcher.group(1);
		}

		return data;
	}

	private Files getFiles(String message) {
		Files files = new Files();
		String content = getContent(message);
		String header = getHeader(message);
		String contentType = getContentType(header);
		String boundary = getBoundary(header);
		String line = null;

		String contentTypeCompare = "multipart/mixed";
		if (contentType.substring(0, contentTypeCompare.length()).equals(contentTypeCompare)) {

			StringReader reader = new StringReader(content);
			LineNumberReader lineReader = new LineNumberReader(reader);

			try {
				Boolean isboundary = false;

				// Ignore body
				while (((line = lineReader.readLine()) != null) && (!isboundary)) {
					if (line.contains(boundary))
						isboundary = true;
				}
				isboundary = false;
				while ((!isboundary) && ((line = lineReader.readLine()) != null)) {
					if (line.contains(boundary))
						isboundary = true;
				}

				while (line != null) {
					String fileHeader = "";
					Boolean isheader = true;
					while ((isheader) && ((line = lineReader.readLine()) != null)) {
						fileHeader = fileHeader + line;
						if (line.equals(""))
							isheader = false;
					}

					String fileName = getFileName(fileHeader);
					String fileContent = "";
					isboundary = false;
					while (((line = lineReader.readLine()) != null) && (!isboundary)) {
						if (line.contains(boundary))
							isboundary = true;
						else
							fileContent = fileContent + line + "\n";
					}
					files.setFile(fileName, fileContent);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return files;
	}

}
