package org.monet.federation.accountoffice.control.sockets.impl;

import com.google.inject.Inject;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

public class ActionPing implements ActionAccountSocket {

	@Inject
	public ActionPing() {
	}

	@Override
	public SocketResponseMessage execute(SocketMessageModel socketMessage) {
		SocketResponseMessage message = new SocketResponseMessage();

		message.setResponse(SocketResponseMessage.RESPONSE_OK);

		return message;
	}

}
