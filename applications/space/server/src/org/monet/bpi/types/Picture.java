package org.monet.bpi.types;

import org.monet.space.kernel.agents.AgentRestfullClient;
import org.monet.space.kernel.agents.AgentRestfullClient.Result;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;
import org.monet.space.kernel.utils.MimeTypes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Picture extends File {

	public Picture() {
	}

	public Picture(Picture picture) {
		super(picture);
	}

	public Picture(String filename) {
		super(filename);
	}

	public Picture(String filename, boolean isModelResource) {
		super(filename, isModelResource);
	}

	public Picture(MessageAttach source) {
		super(source);
	}

	public boolean equals(Picture obj) {
		return this.equals((File) obj);
	}

	public static Picture fromFile(String filename) {
		return fromFile(new File(filename));
	}

	public static Picture fromFile(File file) {
		Picture picture = new Picture(file.getFilename());
		String contentType = MimeTypes.getInstance().getFromStream(new ByteArrayInputStream(file.getContent()));

		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
		componentDocuments.uploadImage(picture.getFilename(), new ByteArrayInputStream(file.getContent()), contentType, -1, -1);

		return picture;
	}

	public static Picture fromInputStream(String contentType, InputStream stream) {
		return fromInputStream(UUID.randomUUID().toString(), contentType, stream);
	}

	public static Picture fromInputStream(String filename, String contentType, InputStream stream) {
		Picture picture = new Picture(filename);

		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
		componentDocuments.uploadImage(picture.getFilename(), stream, contentType, -1, -1);

		return picture;
	}

	public static Picture fromUrl(String url) {
		try {
			Result result = AgentRestfullClient.getInstance().executeGet(url);
			return fromInputStream(result.type, result.content);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Picture fromUrl(String url, HashMap<String, List<String>> parameters) {
		try {
			Result result = AgentRestfullClient.getInstance().executePostMultiParams(url, AgentRestfullClient.convertParameterMap(parameters));
			return fromInputStream(result.type, result.content);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
