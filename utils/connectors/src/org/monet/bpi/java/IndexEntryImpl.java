package org.monet.bpi.java;


import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.model.Reference;
import org.monet.api.space.backservice.impl.model.ReferenceAttribute;
import org.monet.bpi.IndexEntry;
import org.monet.bpi.types.Link;
import org.monet.v3.BPIClassLocator;
import org.monet.metamodel.Definition;
import org.monet.metamodel.Dictionary;
import org.monet.metamodel.IndexDefinition;

public class IndexEntryImpl implements IndexEntry {

	Reference index;
	org.monet.api.space.backservice.impl.model.Node node;
	private BPIClassLocator bpiClassLocator;
	private Dictionary dictionary;
	private BackserviceApi api;

	void injectIndexEntry(Reference index) {
		this.index = index;
	}

	void injectNode(org.monet.api.space.backservice.impl.model.Node node) {
		this.node = node;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	private org.monet.api.space.backservice.impl.model.Node loadNode() {
		if (this.node != null)
			return this.node;

		return this.api.openNode(String.valueOf(this.index.getIdNode()));
	}

	@SuppressWarnings("unchecked")
	protected <T> T getAttribute(String name) {
		IndexDefinition definition = (IndexDefinition)this.dictionary.getDefinition(this.index.getCode());
		String code = definition.getAttribute(name).getCode();
		return (T) this.index.getAttribute(code).getValue();
	}

	protected <T> void setAttribute(String name, T value) {
		IndexDefinition definition = (IndexDefinition)this.dictionary.getDefinition(this.index.getCode());
		String code = definition.getAttribute(name).getCode();
		ReferenceAttribute attribute = this.index.getAttribute(code);
		attribute.setValue((String)value);
	}

	public org.monet.bpi.Node getIndexedNode() {
		org.monet.api.space.backservice.impl.model.Node node = loadNode();

		Definition definition = this.dictionary.getDefinition(node.getCode());
		NodeImpl indexedNode = bpiClassLocator.instantiateBehaviour(definition);
		indexedNode.injectBPIClassLocator(this.bpiClassLocator);
		indexedNode.injectApi(this.api);
		indexedNode.injectDictionary(this.dictionary);
		indexedNode.injectNode(node);
		return indexedNode;
	}

	@Override
	public void save() {
		org.monet.api.space.backservice.impl.model.Node node = this.loadNode();
		this.api.saveNodeReference(node.getId(), this.index);
	}

	@Override
	public Link toLink() {
		org.monet.api.space.backservice.impl.model.Node node = loadNode();
		return new Link(Link.LinkType.NODE, node.getId(), node.getLabel());
	}

}
