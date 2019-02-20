package org.monet.space.analytics.views.handlers;

import org.monet.space.analytics.views.serializers.ViewSerializer;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.FileInputStream;

public class ViewInputStream {

	private final FileInputStream stream;

	public ViewInputStream(FileInputStream stream) {
		this.stream = stream;
	}

	public ViewSerializer read() throws Exception {
		Serializer serializer = new Persister();
		ViewSerializer view = serializer.read(ViewSerializer.class, stream);
		stream.close();
		return view;
	}
}
