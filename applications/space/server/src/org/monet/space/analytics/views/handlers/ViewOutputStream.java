package org.monet.space.analytics.views.handlers;

import org.monet.space.analytics.views.serializers.ViewSerializer;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.FileOutputStream;

public class ViewOutputStream {

	private final FileOutputStream stream;

	public ViewOutputStream(FileOutputStream stream) {
		this.stream = stream;
	}

	public void write(ViewSerializer view) throws Exception {
		Serializer serializer = new Persister();
		serializer.write(view, stream);
		stream.close();
	}
}
