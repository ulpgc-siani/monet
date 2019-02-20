package org.monet.space.kernel.model.map;

import org.monet.space.kernel.agents.AgentLogger;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;

public class ExtendedDataWriter {

	private AgentLogger logger = AgentLogger.getInstance();
	private XmlSerializer serializer;
	private StringWriter writer;

	public ExtendedDataWriter() {
		try {
			this.serializer = XmlPullParserFactory.newInstance().newSerializer();
			this.writer = new StringWriter();
			this.serializer.setOutput(this.writer);
		} catch (Exception e) {
			this.logger.error(e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public String getResult() {
		if (this.writer != null)
			return this.writer.toString();
		return null;
	}

	public void write(String key, String label, String value) {
		String tag = "m:" + key;
		try {
			this.serializer.startTag("", tag);
			this.serializer.attribute("", "label", label);
			if (value != null) this.serializer.text(value);
			this.serializer.endTag("", tag);
		} catch (Exception e) {
			this.logger.error(e);
		}
	}

}