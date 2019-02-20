package org.monet.space.kernel.components.monet.documents;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.Date;

@Root(name = "presigned")
public class PresignedDocument {

	@Attribute(name = "sign-id", required = false)
	private String signId;
	@Attribute(name = "hash", required = false)
	private String hash;
	@Attribute(name = "date", required = false)
	private Date date;

	public void setSignId(String signId) {
		this.signId = signId;
	}

	public String getSignId() {
		return signId;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getHash() {
		return hash;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
