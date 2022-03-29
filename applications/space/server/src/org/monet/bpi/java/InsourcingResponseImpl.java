package org.monet.bpi.java;

import org.monet.bpi.InsourcingResponse;
import org.monet.bpi.Node;
import org.monet.bpi.NodeDocument;
import org.monet.bpi.Schema;
import org.monet.bpi.types.File;
import org.monet.metamodel.NodeDefinition;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Message.MessageAttach;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.PersisterHelper;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.InputStream;
import java.util.Iterator;

public class InsourcingResponseImpl implements InsourcingResponse {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	private Message message;

	public InsourcingResponseImpl(Message message) {
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
	public File getFile(String name) {
		MessageAttach attach = this.message.getAttachment(name);
		if (attach != null)
			return new File(attach);
		else
			return null;
	}

	@Override
	public NodeDocument getDocument(String name, Class<? extends NodeDocument> documentType) {
		MessageAttach attach = this.message.getAttachment(name);
		NodeDocumentImpl bpiNode = null;
		if (attach != null) {
			ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
			ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
			NodeLayer nodeLayer = componentPersistence.getNodeLayer();
			NodeDefinition definition = Dictionary.getInstance().getNodeDefinition(documentType.getName());
			org.monet.space.kernel.model.Node node = nodeLayer.addNode(definition.getCode());
			bpiNode = BPIClassLocator.getInstance().instantiateBehaviour(definition);
			bpiNode.injectNode(node);

			InputStream attachStream = null;
			try {
				attachStream = attach.getInputStream();
				String contentType = attach.getContentType();
				componentDocuments.uploadDocument(node.getId(), attachStream, contentType, MimeTypes.getInstance().isPreviewable(contentType));
				node.setSchema(componentDocuments.getDocumentSchema(node.getId()));
				nodeLayer.saveNodeSchema(node);
				if (MimeTypes.PDF.equals(contentType))
					nodeLayer.makeUneditable(node);
			} catch (Exception e) {
				//nodeLayer.deleteAndRemoveNodeFromTrash(node.getId());
				AgentLogger.getInstance().error(e);
				throw new RuntimeException("Error extracting document from message. See log for details.");
			} finally {
				StreamHelper.close(attachStream);
			}
		}
		return bpiNode;
	}

	@Override
	public Schema getSchema(String name, Class<? extends Schema> schemaType) {
		Schema schema = null;
		InputStream attachStream = null;
		try {
			MessageAttach attach = this.message.getAttachment(name);
			if (attach != null) {
				attachStream = attach.getInputStream();
				schema = PersisterHelper.load(StreamHelper.toString(attachStream), schemaType);
			}
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			throw new RuntimeException("Error extracting document from message. See log for details.");
		} finally {
			StreamHelper.close(attachStream);
		}

		return schema;
	}

	@Override
	public String getString(String name) {
		InputStream attachStream = null;
		try {
			MessageAttach attach = this.message.getAttachment(name);
			if (attach != null) {
				attachStream = attach.getInputStream();
				return StreamHelper.toString(attachStream);
			} else
				return null;
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			throw new RuntimeException("Error extracting document from message. See log for details.");
		} finally {
			StreamHelper.close(attachStream);
		}
	}

	@Override
	public Iterator<String> getAttachmentsKeys() {
		return this.message.getAttachmentKeys();
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
