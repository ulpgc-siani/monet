package org.monet.bpi.types;

import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;

import java.io.InputStream;
import java.util.UUID;

public class Video extends File {

	public Video() {
	}

	public Video(String filename) {
		super(filename);
	}

	public Video(String filename, boolean isModelResource) {
		super(filename, isModelResource);
	}

	public Video(MessageAttach source) {
		super(source);
	}

	public boolean equals(Video obj) {
		return this.equals((File) obj);
	}

	public static File fromInputStream(String contentType, InputStream stream) {
		File file = new File(UUID.randomUUID().toString());

		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
		componentDocuments.uploadDocument(file.getFilename(), stream, contentType, false);

		return file;
	}

}
