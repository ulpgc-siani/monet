package org.monet.space.backservice.control.actions;

import org.monet.bpi.java.ExporterImpl;
import org.monet.bpi.java.NodeDocumentImpl;
import org.monet.bpi.java.NodeImpl;
import org.monet.metamodel.ExporterDefinition;
import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Node;

import java.io.IOException;

public class ActionExecuteExporter extends Action {

	public ActionExecuteExporter() {
	}

	@Override
	public String execute() {
		String exporterKey = (String) this.parameters.get(Parameter.EXPORTER);
		String scopeId = (String) this.parameters.get(Parameter.SCOPE);
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
		Node resultNode = null;
		Node scopeNode = null;

		if (exporterKey == null)
			return ErrorCode.WRONG_PARAMETERS;

		try {

			if (scopeId != null)
				scopeNode = nodeLayer.loadNode(scopeId);

			resultNode = this.executeExporter(exporterKey, scopeNode);
			String resultId = resultNode.getId();
			String schema = componentDocuments.getDocumentSchema(resultId);
			byte[] schemaBytes = schema.getBytes("UTF-8");

			this.response.setContentLength(schemaBytes.length);
			this.response.setContentType(componentDocuments.getDocumentContentType(resultId));
			this.response.setHeader("Content-Disposition", "attachment; filename=" + resultId);
			this.response.getOutputStream().write(schemaBytes);
			this.response.getOutputStream().flush();
		} catch (IOException exception) {
			this.agentException.error(exception);
		}
		finally {
			if (resultNode != null)
				nodeLayer.deleteAndRemoveNodeFromTrash(resultNode.getId());
		}

		return null;
	}

	private Node executeExporter(String exporterKey, Node scopeNode) {
		Dictionary dictionary = Dictionary.getInstance();
		ExporterDefinition definition = dictionary.getExporterDefinition(exporterKey);
		String destinationCode = dictionary.getDefinitionCode(definition.getTarget().getValue());
		BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();
		ExporterImpl bpiExporter = bpiClassLocator.instantiateBehaviour(definition);
		Node destinationNode = ComponentPersistence.getInstance().getNodeLayer().addNode(destinationCode);
		NodeDocumentImpl destination = bpiClassLocator.instantiateBehaviour(destinationNode.getDefinition());

		destination.injectNode(destinationNode);

		if (scopeNode != null) {
			NodeImpl scope = bpiClassLocator.instantiateBehaviour(scopeNode.getDefinition());
			scope.injectNode(scopeNode);
			bpiExporter.injectScope(scope);
		}

		bpiExporter.destination = destination;
		bpiExporter.execute();

		return destinationNode;
	}

}
