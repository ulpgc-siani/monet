package org.monet.bpi.java;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.monet.bpi.DelivererService;
import org.monet.bpi.NodeDocument;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentRestfullClient;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DelivererServiceImpl extends DelivererService {

	public static void init() {
		instance = new DelivererServiceImpl();
	}

	private InputStream getDocumentContent(NodeDocument document) {
		String idDocument = document.toLink().getId();
		return ComponentDocuments.getInstance().getDocumentContent(idDocument);
	}

	@Override
	public void deliver(URI url, NodeDocument document) throws Exception {
		InputStream documentContent = null;

		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			documentContent = getDocumentContent(document);
			parameters.put("document", new InputStreamBody(documentContent, "document"));
			AgentRestfullClient.getInstance().executePost(url.toString(), parameters);
		} finally {
			StreamHelper.close(documentContent);
		}
	}

	@Override
	public void deliver(URI url, Map<String, String> params) throws Exception {
		InputStream documentContent = null;
		try {
			HashMap<String, ContentBody> parameters = new HashMap<String, ContentBody>();
			for (Map.Entry<String, String> entry : params.entrySet())
				parameters.put(entry.getKey(), new StringBody(entry.getValue(), ContentType.APPLICATION_JSON));
			AgentRestfullClient.getInstance().executePost(url.toString(), parameters);
		} finally {
			StreamHelper.close(documentContent);
		}
	}

	@Override
	public void deliverToMail(URI from, URI to, String subject, String body, NodeDocument document) {
		HtmlEmail email = new HtmlEmail();
		String filename = Configuration.getInstance().getTempDir() + UUID.randomUUID().toString();
		InputStream documentContent = null;
		FileOutputStream output;

		try {
			documentContent = getDocumentContent(document);
			output = new FileOutputStream(filename);
			IOUtils.copy(documentContent, output);
			output.close();

			email.setHostName(from.getHost());
			email.setFrom(from.toString());
			email.addTo(to.toString());
			email.setSubject(subject);
			email.embed(new File(filename), "document");
			email.setHtmlMsg(body);
			email.setTextMsg(body);
			email.setSmtpPort(465);
			email.setSSL(true);

			email.send();
		} catch (FileNotFoundException exception) {
			AgentLogger.getInstance().error(exception);
		} catch (EmailException exception) {
			AgentLogger.getInstance().error(exception);
		} catch (IOException exception) {
			AgentLogger.getInstance().error(exception);
		} finally {
			StreamHelper.close(documentContent);
		}

	}

}