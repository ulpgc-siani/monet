package org.monet.bpi.java;

import org.monet.bpi.Node;
import org.monet.bpi.TransferRequest;
import org.monet.bpi.types.Link;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;

public class TransferRequestImpl implements TransferRequest {

	private Message message;

	public TransferRequestImpl(Message message) {
		this.message = message;
	}

	@Override
	public void setContent(String content) {
		this.message.setContent(content);
	}

	@Override
	public void addNode(String name, Node node) {
		this.message.addAttachment(new MessageAttach(name, ((NodeImpl) node).node));
	}

	@Override
	public void addNode(String name, Link nodeLink) {
		org.monet.space.kernel.model.Node node = ComponentPersistence.getInstance().getNodeLayer().loadNode(nodeLink.getId());
		this.message.addAttachment(new MessageAttach(name, node));
	}

}
