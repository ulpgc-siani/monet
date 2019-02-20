package org.monet.bpi.types;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;

import java.io.IOException;
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

	public boolean equals(Picture obj) {
		return this.equals((File) obj);
	}

	public static Picture fromInputStream(String contentType, InputStream stream) throws IOException {
		Picture picture = new Picture(UUID.randomUUID().toString());
		picture.setContent(IOUtils.toByteArray(stream));
		picture.setContentType(contentType);
		return picture;
	}

	public static Picture fromInputStream(InputStream stream) throws IOException {
		return Picture.fromInputStream(null, stream);
	}

	public static Picture fromUrl(String url) {
		throw new NotImplementedException();
	}

	public static Picture fromUrl(String url, HashMap<String, List<String>> parameters) {
		throw new NotImplementedException();
	}

}
