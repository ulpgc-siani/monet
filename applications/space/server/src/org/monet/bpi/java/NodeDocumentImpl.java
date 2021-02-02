package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeDocument;
import org.monet.bpi.NodeDocument;
import org.monet.bpi.Schema;
import org.monet.bpi.User;
import org.monet.bpi.exceptions.NodeAccessException;
import org.monet.bpi.exceptions.RoleException;
import org.monet.bpi.types.File;
import org.monet.metamodel.DocumentDefinition;
import org.monet.metamodel.DocumentDefinitionBase;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.StreamHelper;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.UUID;

public abstract class NodeDocumentImpl extends NodeImpl implements NodeDocument, BehaviorNodeDocument {

	@Override
	public Schema genericGetSchema() {
		Class<Schema> schemaClass = this.bpiClassLocator.getSchemaClass(this.node.getDefinition());
		Persister persister = new Persister();
		try {
			String schema = this.node.getSchema();
			if (!schema.isEmpty())
				return persister.read(schemaClass, schema);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}
		try {
			return schemaClass.newInstance();
		} catch (Exception ex) {
			AgentLogger.getInstance().error(ex);
			return null;
		}
	}

	@Override
	public void setSignaturesCount(String signature, int count) throws NodeAccessException {

		if (!node.isEditable())
			throw new NodeAccessException();

		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		nodeLayer.setNodeDocumentSignaturesCount(node, signature, count);
	}

	@Override
	public void setUsersForSignature(String signature, int signaturePos, String[] userIds) throws RoleException {
		DocumentDefinition definition = (DocumentDefinition) this.node.getDefinition();
		DocumentDefinitionBase.SignaturesProperty.SignatureProperty signatureDefinition = definition.getSignature(signature);
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		ComponentFederation componentFederation = ComponentFederation.getInstance();
		RoleLayer roleLayer = componentFederation.getRoleLayer();
		FederationLayer federationLayer = componentFederation.getDefaultLayer();

		if (signatureDefinition == null)
			return;

		java.util.List<String> roleCodes = getRoleCodes(signatureDefinition);
		for (String userId : userIds) {
			boolean userAllowed = false;
			org.monet.space.kernel.model.User user = federationLayer.loadUser(userId);

			for (String roleCode : roleCodes) {
				if (roleLayer.existsNonExpiredUserRole(roleCode, user)) {
					userAllowed = true;
					break;
				}
			}

			if (!userAllowed)
				throw new RoleException();

			nodeLayer.addNodeDocumentSignatureUserRestriction(node, signatureDefinition.getCode(), signaturePos, user);
		}
	}

	@Override
	public void disableUsersForSignature(String signature, int signaturePos) {
		DocumentDefinition definition = (DocumentDefinition) this.node.getDefinition();
		DocumentDefinitionBase.SignaturesProperty.SignatureProperty signatureDefinition = definition.getSignature(signature);
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

		if (signatureDefinition == null)
			return;

		nodeLayer.clearNodeDocumentSignatureUsersRestrictions(node, signatureDefinition.getCode(), signaturePos);
		nodeLayer.addNodeDocumentSignatureUserRestriction(node, signatureDefinition.getCode(), signaturePos, null);
	}

	private java.util.List<String> getRoleCodes(DocumentDefinitionBase.SignaturesProperty.SignatureProperty signatureDefinition) {
		Dictionary dictionary = Dictionary.getInstance();
		java.util.List<String> result = new ArrayList<>();

		for (Ref roleRef : signatureDefinition.getFor())
			result.add(dictionary.getDefinitionCode(roleRef.getValue()));

		return result;
	}

	public void consolidate() {
		this.nodeLayer.saveNode(node);
		ComponentDocuments.getInstance().consolidateDocument(node, false);
		this.nodeLayer.makeUneditable(node);
	}

	public void overwriteContent(File newContent) {
		ComponentDocuments.getInstance().overwriteDocument(this.node.getId(), newContent.getFilename());
	}

	public void overwriteContent(byte[] content, String contentType) {
		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
		String uuid = UUID.randomUUID().toString();
		ByteArrayInputStream contentStream = null;

		try {
			contentStream = new ByteArrayInputStream(content);
			componentDocuments.uploadDocument(uuid, contentStream, contentType, MimeTypes.getInstance().isPreviewable(contentType));
			componentDocuments.overwriteDocument(this.node.getId(), uuid);
		}
		finally {
			StreamHelper.close(contentStream);
			if (componentDocuments.existsDocument(uuid))
				componentDocuments.removeDocument(uuid);
		}
	}

	public File getContent() {
		File file = new File();

		file.setFilename(this.node.getId());
		file.setContent(ComponentDocuments.getInstance().getDocumentContent(this.node.getId()));

		return file;
	}

    @Override
    public Boolean isShared() {
		DocumentDefinition definition = (DocumentDefinition) this.node.getDefinition();
		return definition.isShared();
    }

    @Override
	public void onSign(User user) {
	}

	@Override
	public void onSignsComplete() {
	}
}