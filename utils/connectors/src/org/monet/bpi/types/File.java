package org.monet.bpi.types;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class File {

	@Attribute(name = "is-attachment")
	protected boolean isAttachment = true;
	@Text
	protected String filename;

	private InputStream content;
	private String contentType;
	private boolean isModelResource = false;

	public File() {
	}

	public File(File file) {
		this.isAttachment = file.isAttachment;
		this.filename = file.filename;
		this.isModelResource = file.isModelResource;
	}

	public File(String filename) {
		this.filename = filename;
	}

	public File(String filename, boolean isModelResource) {
		this(filename);
		this.isModelResource = isModelResource;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean equals(File obj) {
		return this.filename.equals(obj.filename);
	}

	public boolean isStoredAtDocEngine() {
		return !isModelResource;
	}

	public boolean isModelResource() {
		return this.isModelResource;
	}

	public InputStream getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = new ByteArrayInputStream(content);
	}

	public void setContent(InputStream content) {
		this.content = content;
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return this.filename;
	}

	public static File fromInputStream(String contentType, InputStream stream) throws IOException {
		File file = new File(UUID.randomUUID().toString());
		file.content = stream;
		file.contentType = contentType;
		return file;
	}

	public static File fromInputStream(InputStream stream) throws IOException {
		return File.fromInputStream(null, stream);
	}

	public static File fromUrl(String url) {
		throw new NotImplementedException();
	}

	public static File fromUrl(String url, HashMap<String, List<String>> parameters) {
		throw new NotImplementedException();
	}

}
