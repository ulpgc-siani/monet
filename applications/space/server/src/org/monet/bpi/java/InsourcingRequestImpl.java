package org.monet.bpi.java;

import org.monet.bpi.InsourcingRequest;
import org.monet.bpi.Node;
import org.monet.bpi.NodeDocument;
import org.monet.bpi.Schema;
import org.monet.bpi.types.File;
import org.monet.bpi.types.Link;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;
import org.monet.space.kernel.model.BusinessModel;
import org.monet.space.kernel.utils.MimeTypes;

import java.io.UnsupportedEncodingException;

public class InsourcingRequestImpl implements InsourcingRequest {

	private Message message;

	public InsourcingRequestImpl(Message message) {
		this.message = message;
	}

	@Override
	public void setContent(String content) {
		this.message.setContent(content);
	}

	@Override
	public void attachFile(String name, File file) {
		if (file.isModelResource()) {
			String filename = BusinessModel.getInstance().getAbsoluteFilename(file.getFilename());
			this.message.addAttachment(new MessageAttach(name, new java.io.File(filename)));
		} else
			this.message.addAttachment(new MessageAttach(name, file.getFilename(), false));
	}

	@Override
	public void attachDocument(String name, NodeDocument document) {
		document.save();
		this.message.addAttachment(new MessageAttach(name, ((NodeDocumentImpl) document).node.getId(), document.isShared()));
	}

	@Override
	public void attachSchema(String name, Schema schema) {
		this.message.addAttachment(new MessageAttach(name, schema));
	}

	@Override
	public void attachSchema(String name, NodeDocument document) {
		this.message.addAttachment(new MessageAttach(name, document.genericGetSchema()));
	}

	@Override
	public void attachString(String name, String content) {
		try {
			this.message.addAttachment(new MessageAttach(name, content.getBytes("UTF-8"), MimeTypes.TEXT));
		} catch (UnsupportedEncodingException e) {
			AgentLogger.getInstance().error(e);
		}
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
