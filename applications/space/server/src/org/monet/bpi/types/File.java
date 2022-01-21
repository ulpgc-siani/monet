package org.monet.bpi.types;

import org.apache.commons.io.IOUtils;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentRestfullClient;
import org.monet.space.kernel.agents.AgentRestfullClient.Result;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.library.LibraryFile;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;
import org.monet.space.kernel.model.BusinessModel;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.StreamHelper;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class File {

	@Attribute(name = "is-attachment")
	protected boolean isAttachment = true;
	@Text
	private String filename;

	private InputStream content;
	private MessageAttach source;
	private boolean isModelResource = false;
	private String contentType;

	public File() {
	}

	public File(File file) {
		this.isAttachment = file.isAttachment;
		this.filename = file.filename;
		this.source = file.source;
		this.isModelResource = file.isModelResource;
	}

	public File(String filename) {
		this.filename = filename;
	}

	public File(String filename, boolean isModelResource) {
		this(filename);
		this.isModelResource = isModelResource;
	}

	public File(MessageAttach source) {
		this.filename = source.getFile().getName();
		this.contentType = source.getContentType();
		this.source = source;
	}

	public String getFilename() {
		return this.filename;
	}

	public String getContentType() {
		if (contentType != null)
			return contentType;

		return LibraryFile.getContentType(new ByteArrayInputStream(getContent()));
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean equals(File obj) {
		return this.filename.equals(obj.filename);
	}

	public boolean isStoredAtDocEngine() {
		return source == null && !isModelResource;
	}

	public boolean isModelResource() {
		return this.isModelResource;
	}

	public MessageAttach getSource() {
		return source;
	}

	public String getAbsolutePath() {
		if (this.isModelResource())
			return BusinessModel.getInstance().getAbsoluteFilename(this.filename);

		return this.filename;
	}

	public InputStream getContentAsStream() {
		try {
			if (this.isModelResource()) return new FileInputStream(getAbsolutePath());
			if (this.source != null) return this.source.getInputStream();
			if (this.content != null) return content;

			ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
			return componentDocuments.getDocumentContent(this.filename);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] getContent() {
		InputStream content = null;

		try {
			content = getContentAsStream();
			return IOUtils.toByteArray(content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			StreamHelper.close(content);
		}
	}

	public void setContent(byte[] content) {
		this.content = new ByteArrayInputStream(content);
	}

	public void setContent(InputStream content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return this.filename;
	}

	public static File fromFile(java.io.File file) {
		String filePath = file.getAbsolutePath();
		File result = new File(LibraryFile.getFilename(filePath));

		result.content = AgentFilesystem.getInputStream(filePath);
		result.contentType = MimeTypes.getInstance().getFromFile(file);

		return result;
	}

	public static File fromInputStream(String contentType, InputStream stream) {
		return fromInputStream(generateFilename(contentType), contentType, stream);
	}

	public static File fromInputStream(String filename, String contentType, InputStream stream) {
		File file = new File(filename);

		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
		componentDocuments.uploadDocument(file.filename, stream, contentType, false);

		return file;
	}

	protected static String generateFilename(String contentType) {
		return UUID.randomUUID().toString().replace("-", "") + "." + MimeTypes.getInstance().getExtension(contentType);
	}

	public static File fromUrl(String url) {
		try {
			Result result = AgentRestfullClient.getInstance().executeGet(url);
			return fromInputStream(result.type, result.content);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static File fromUrl(String url, HashMap<String, List<String>> parameters) {
		try {
			Result result = AgentRestfullClient.getInstance().executePostMultiParams(url, AgentRestfullClient.convertParameterMap(parameters));
			return fromInputStream(result.type, result.content);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
