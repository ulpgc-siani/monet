package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.Node;
import org.monet.bpi.NodeService;
import org.monet.v3.BPIClassLocator;
import org.monet.metamodel.Definition;
import org.monet.metamodel.NodeDefinition;
import org.monet.v3.model.Dictionary;

public class NodeServiceImpl extends NodeService {
	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(NodeServiceImpl service) {
		instance = service;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}


	@Override
	public Node locateImpl(String name) {
		NodeDefinition definition = (NodeDefinition)this.dictionary.getDefinition(name);
		org.monet.api.space.backservice.impl.model.Node node = this.api.locateNode(definition.getCode(), 0);

		if (node == null) return null;

		NodeImpl bpiNode = this.bpiClassLocator.instantiateBehaviour(definition);
		bpiNode.injectNode(node);
		bpiNode.injectApi(this.api);
		bpiNode.injectDictionary(this.dictionary);
		bpiNode.injectBPIClassLocator(this.bpiClassLocator);

		return bpiNode;
	}

	@Override
	public Node getImpl(String nodeId) {
		org.monet.api.space.backservice.impl.model.Node node = this.api.openNode(nodeId);
		Definition definition = this.dictionary.getDefinition(node.getCode());

		NodeImpl bpiNode = this.bpiClassLocator.instantiateBehaviour(definition);
		bpiNode.injectNode(node);
		bpiNode.injectApi(this.api);
		bpiNode.injectDictionary(this.dictionary);
		bpiNode.injectBPIClassLocator(this.bpiClassLocator);

		return bpiNode;
	}

	@Override
	public Node createImpl(Class<? extends Node> nodeClass, Node parent) {
		return this.createImpl(nodeClass.getName(), parent);
	}

	@Override
	public Node createImpl(String name, Node parent) {
		NodeDefinition definition = (NodeDefinition) this.dictionary.getDefinition(name);
		org.monet.api.space.backservice.impl.model.Node node = this.api.createNode(definition.getCode(), ((NodeImpl) parent).node.getId());

		NodeImpl bpiNode = this.bpiClassLocator.instantiateBehaviour(definition);
		bpiNode.injectNode(node);
		bpiNode.injectApi(this.api);
		bpiNode.injectDictionary(this.dictionary);
		bpiNode.injectBPIClassLocator(this.bpiClassLocator);

		return bpiNode;
	}

	public static void init() {
	}

}
