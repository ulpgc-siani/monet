package org.monet.space.kernel.machines.ttm.model;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.monet.bpi.Schema;
import org.monet.mobile.model.TaskMetadata;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.map.Location;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.PersisterHelper;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Message {

	public static final String REFERENCED_DOCUMENT_MESSAGE = "idReferenced:";

	public static class MessageAttach {

		private String key;
		private File file;
		private String documentId;
		private Boolean documentInteroperable;
		private byte[] rawContent;
		private Schema schema;
		private Node node;
		private String contentType;

		public MessageAttach(String key, File file) {
			this.key = key;
			this.file = file;
			this.contentType = MimeTypes.getInstance().getFromFile(file);
		}

		public MessageAttach(String key, String documentId, Boolean documentInteroperable) {
			this.key = key;
			this.documentId = documentId;
			this.contentType = ComponentDocuments.getInstance().getDocumentContentType(documentId);
			this.documentInteroperable = documentInteroperable;
		}

		public MessageAttach(String key, byte[] rawContent, String contentType) {
			this.key = key;
			this.rawContent = rawContent;
			this.contentType = contentType;
		}

		public MessageAttach(String key, Schema schema) {
			this.key = key;
			this.schema = schema;
			this.contentType = MimeTypes.XML;
		}

		public MessageAttach(String key, Node node) {
			this.key = key;
			this.node = node;
		}

		public String getKey() {
			return key;
		}

		public Node getNode() {
			return this.node;
		}

		public File getFile() {
			return this.file;
		}

		public InputStream getInputStream() throws Exception {
			if (rawContent != null) {
				return new ByteArrayInputStream(rawContent);
			} else if (file != null) {
				return new FileInputStream(this.file);
			} else if (documentId != null) {
				return documentInteroperable ? this.getDocumentReferencedStream() : this.getDocumentStream();
			} else if (schema != null) {
				return new ByteArrayInputStream(this.serialize().getBytes("UTF-8"));
			} else if (node != null){
				return this.getNodeStream();
			}
			return null;
		}

		private InputStream getNodeStream() throws HttpException, IOException {
			return new ByteArrayInputStream(node.getId().getBytes("UTF-8"));
		}

		private InputStream getDocumentStream() throws HttpException, IOException {
			HttpClient client = new HttpClient();
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("id", URLEncoder.encode(this.documentId, "UTF-8"));
			GetMethod method = new GetMethod(ComponentDocuments.getInstance().getDownloadUrl(parameters));
			int status = client.executeMethod(method);
			if (status == HttpStatus.SC_NOT_FOUND) {
				throw new SystemException(ErrorCode.DOWNLOAD_DOCUMENT, this.documentId, null);
			}

			return method.getResponseBodyAsStream();
		}

		private InputStream getDocumentReferencedStream() throws HttpException, IOException {
			String idReferenced = Message.REFERENCED_DOCUMENT_MESSAGE + BusinessUnit.getInstance().getName() + "_" + URLEncoder.encode(this.documentId, "UTF-8");
			return new ByteArrayInputStream(idReferenced.getBytes("UTF-8"));
		}

		private String serialize() {
			try {
				StringWriter writer = new StringWriter();
				PersisterHelper.save(writer, this.schema);
				return writer.toString();
			} catch (Exception e) {
				AgentLogger.getInstance().errorInModel(e);
			}
			return "<schema/>";
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

	}

	public class Type {
		public static final String DEFAULT = "default";
		public static final String CHAT = "chat";
		public static final String SIGNALING = "signaling";
		public static final String JOB = "job";
		public static final String REQUEST_SERVICE = "requestservice";
	}

	private String to;

	// Message code
	private String subject;

	private String type = Type.DEFAULT;
	private String content;
	private Location location;
	private Schema defaultValues;
	private TaskMetadata metadata;
	private HashMap<String, MessageAttach> attachmets = new HashMap<String, Message.MessageAttach>();

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isChat() {
		return this.type.equals(Message.Type.CHAT);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MessageAttach getAttachment(String key) {
		return this.attachmets.get(key);
	}

	public void addAttachment(MessageAttach content) {
		this.attachmets.put(content.getKey(), content);
	}

	public Collection<MessageAttach> getAttachments() {
		return this.attachmets.values();
	}

	public Iterator<String> getAttachmentKeys() {
		return this.attachmets.keySet().iterator();
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setMetadata(TaskMetadata metadata) {
		this.metadata = metadata;
	}

	public TaskMetadata getMetadata() {
		return this.metadata;
	}

	public void setDefaultValues(Schema schema) {
		this.defaultValues = schema;
	}

	public Schema getDefaultValues() {
		return this.defaultValues;
	}

}
