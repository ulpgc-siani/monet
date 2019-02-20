package org.monet.bpi.java;

import org.monet.bpi.Node;
import org.monet.bpi.NodeService;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.SetDefinition;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryMapLayer;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.map.LocationList;
import org.monet.space.kernel.model.map.MapLayerWriter;

public class NodeServiceImpl extends NodeService {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	@Override
	public Node locateImpl(String name) {
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		NodeDefinition definition = BusinessUnit.getInstance().getBusinessModel().getDictionary().getNodeDefinition(name);
		String nodeId = nodeLayer.locateNodeId(definition.getCode());

		if (nodeId.equals(Strings.EMPTY)) return null;

		org.monet.space.kernel.model.Node node = nodeLayer.loadNode(nodeId);
		NodeImpl bpiNode = this.bpiClassLocator.instantiateBehaviour(definition);
		bpiNode.injectNode(node);
		return bpiNode;
	}

	@Override
	public Node getImpl(String nodeId) {
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		org.monet.space.kernel.model.Node node = nodeLayer.loadNode(nodeId);
		NodeImpl bpiNode = this.bpiClassLocator.instantiateBehaviour(node.getDefinition());
		bpiNode.injectNode(node);
		return bpiNode;
	}

	@Override
	public String getKmlLayerImpl(String nodeId) {
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		org.monet.space.kernel.model.Node node = nodeLayer.loadNode(nodeId);

		if (!node.isSet()) return null;

		SetDefinition definition = (SetDefinition) node.getDefinition();
		IndexDefinition indexDefinition = Dictionary.getInstance().getIndexDefinition(definition.getIndex().getValue());
		LocationList locationList = nodeLayer.loadLocationsInNode(node, null, null, indexDefinition.getCode());

		MapLayerWriter writer = LibraryMapLayer.getInstance().getWriter(LibraryMapLayer.MapLayerFormat.KML);
		writer.addLocationList(locationList);
		writer.close();

		return writer.getResultLayer();
	}

	@Override
	public Node createImpl(Class<? extends Node> nodeClass, Node parent) {
		return this.createImpl(nodeClass.getName(), parent);
	}

	@Override
	public Node createImpl(String name, Node parent) {
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		NodeDefinition definition = Dictionary.getInstance().getNodeDefinition(name);
		org.monet.space.kernel.model.Node node = nodeLayer.addNode(definition.getCode(), ((NodeImpl) parent).node);
		NodeImpl bpiNode = this.bpiClassLocator.instantiateBehaviour(definition);
		bpiNode.injectNode(node);
		return bpiNode;
	}

	public static void init() {
		instance = new NodeServiceImpl();
	}

}
