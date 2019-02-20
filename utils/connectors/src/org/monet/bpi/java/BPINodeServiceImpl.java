package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.api.backservice.impl.model.Node;
import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPINode;
import org.monet.bpi.BPINodeService;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.metamodel.Definition;
import org.monet.v2.model.Dictionary;
import org.monet.v2.model.constants.Strings;

import java.lang.annotation.Annotation;

public class BPINodeServiceImpl extends BPINodeService {
	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(BPINodeServiceImpl service) {
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

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BPIBaseNode<?>> T locate(String name) {
		String code = this.dictionary.getNodeDefinition(name).getCode();
		String nodeId = this.api.locateNode(code).getId();

		if (nodeId.equals(Strings.EMPTY)) return null;

		Node node = this.api.openNode(nodeId);
		BPIBaseNodeImpl<?> bpiNode = this.bpiClassLocator.getDefinitionInstance(name);
		bpiNode.injectNode(node);
		bpiNode.injectApi(this.api);
		bpiNode.injectDictionary(this.dictionary);
		bpiNode.injectBPIClassLocator(this.bpiClassLocator);
		return (T) bpiNode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BPIBaseNode<?>> T get(String nodeId) {
		Node node = this.api.openNode(nodeId);
		Definition definition = this.dictionary.getDefinition(node.getCode());
		BPIBaseNodeImpl<?> bpiNode = this.bpiClassLocator.getDefinitionInstance(definition.getName());
		bpiNode.injectNode(node);
		bpiNode.injectApi(this.api);
		bpiNode.injectDictionary(this.dictionary);
		bpiNode.injectBPIClassLocator(this.bpiClassLocator);
		return (T) bpiNode;
	}

	@Override
	public <T extends BPIBaseNode<?>> T create(Class<T> nodeClass, BPINode<?, ?> parent) {
		String name = getDefinitionName(nodeClass);
		return this.create(name, parent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BPIBaseNode<?>> T create(String name, BPINode<?, ?> parent) {
		String code = this.dictionary.getDefinition(name).getCode();
		Node node = this.api.createNode(code, ((BPINodeImpl<?, ?>) parent).node.getId());
		BPIBaseNodeImpl<?> bpiNode = this.bpiClassLocator.getDefinitionInstance(name);
		bpiNode.injectNode(node);
		bpiNode.injectApi(this.api);
		bpiNode.injectDictionary(this.dictionary);
		bpiNode.injectBPIClassLocator(this.bpiClassLocator);
		return (T) bpiNode;
	}

	private <T> String getDefinitionName(Class<T> clazz) {
		try {
			for (Annotation annotation : clazz.getAnnotations()) {
				if (annotation instanceof Definition) {
					return ((Definition) annotation).getName();
				}
			}
		} catch (Exception e) {
		}

		return null;
	}

}
