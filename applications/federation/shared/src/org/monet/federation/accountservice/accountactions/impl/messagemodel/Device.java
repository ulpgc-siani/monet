package org.monet.federation.accountservice.accountactions.impl.messagemodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;

@Root(name = "device")
public class Device {
	private static Serializer persister = new Persister();

	@Attribute
	private String userId;
	@Attribute
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String serialize() throws Exception {
		StringWriter writer = new StringWriter();
		persister.write(this, writer);
		return writer.toString();
	}

}
