package org.monet.bpi.java;

import org.monet.bpi.Node;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;

public class TransferResponseImpl implements org.monet.bpi.TransferResponse {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	private Message message;

	public TransferResponseImpl(Message message) {
		this.message = message;
	}

	@Override
	public String getCode() {
		return this.message.getSubject();
	}

	@Override
	public String getContent() {
		return this.message.getContent();
	}

	@Override
	public Node getNode(String name) {
		NodeImpl bpiNode = null;
		MessageAttach attach = this.message.getAttachment(name);
		if (attach != null) {
			org.monet.space.kernel.model.Node node = attach.getNode();
			bpiNode = bpiClassLocator.instantiateBehaviour(node.getDefinition());
			bpiNode.injectNode(node);
		}
		return bpiNode;
	}

}
