package org.monet.api.space.backservice.impl.model.workmap;

import org.monet.api.space.backservice.impl.model.MailBoxUri;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "contestants")
public class Contestants {

	@Attribute(name = "local-mail-box", required = false)
	private MailBoxUri localMailBox;
	@Attribute(name = "client-mail-box", required = false)
	private MailBoxUri clientMailBox;

	public MailBoxUri getLocalMailBox() {
		return localMailBox;
	}

	public void setLocalMailBox(MailBoxUri localMailBox) {
		this.localMailBox = localMailBox;
	}

	public MailBoxUri getClientMailBox() {
		return clientMailBox;
	}

	public void setClientMailBox(MailBoxUri clientMailBox) {
		this.clientMailBox = clientMailBox;
	}

}
