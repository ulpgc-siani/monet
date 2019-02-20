package org.monet.space.backservice.control.actions;

import org.monet.metamodel.ImporterDefinition;
import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.DummyProgressCallback;
import org.monet.space.kernel.model.Node;

import java.io.StringReader;

public class ActionImportNode extends Action {

	public ActionImportNode() {
	}

	@Override
	public String execute() {
		String importer = (String) this.parameters.get(Parameter.IMPORTER);
		String scopeNodeId = (String) this.parameters.get(Parameter.ID_SCOPE);
		String data = (String) this.parameters.get(Parameter.DATA);
		Dictionary dictionary = Dictionary.getInstance();
		Node scope = null;

		if (importer == null || data == null)
			return ErrorCode.WRONG_PARAMETERS;

		if (!dictionary.existsDefinition(importer))
			return ErrorCode.WRONG_PARAMETERS;

		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		ImporterDefinition importerDefinition = dictionary.getImporterDefinition(importer);

		if (scopeNodeId != null && !scopeNodeId.isEmpty())
			scope = nodeLayer.loadNode(scopeNodeId);

		nodeLayer.importNode(importerDefinition.getCode(), scope, new StringReader(LibraryEncoding.decode(data)), 0, new DummyProgressCallback());

		return MessageCode.NODE_IMPORTED;
	}

}
