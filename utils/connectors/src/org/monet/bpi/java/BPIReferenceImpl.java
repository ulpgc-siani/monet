package org.monet.bpi.java;


import org.monet.api.backservice.BackserviceApi;
import org.monet.api.backservice.impl.model.Node;
import org.monet.api.backservice.impl.model.Reference;
import org.monet.api.backservice.impl.model.ReferenceAttribute;
import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPIReference;
import org.monet.bpi.types.Link;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.metamodel.Definition;
import org.monet.v2.metamodel.ReferenceDefinition;
import org.monet.v2.model.Dictionary;

public class BPIReferenceImpl implements BPIReference {

	Node node;
	Reference reference;
	private BPIClassLocator bpiClassLocator;
	private BackserviceApi api;
	private Dictionary dictionary;

	void injectReference(Reference reference) {
		this.reference = reference;
	}

	void injectNode(Node node) {
		this.node = node;
	}

	void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	void injectApi(BackserviceApi api) {
		this.api = api;
	}

	void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getAttribute(String name) {
		ReferenceDefinition definition = (ReferenceDefinition)this.dictionary.getDefinition(this.reference.getCode());
		String code = definition.getAttribute(name).getCode();
		return (T) this.reference.getAttribute(code).getValue();
	}

	protected <T> void setAttribute(String name, T value) {
		ReferenceDefinition definition = (ReferenceDefinition)this.dictionary.getDefinition(this.reference.getCode());
		String code = definition.getAttribute(name).getCode();
		ReferenceAttribute attribute = this.reference.getAttribute(code);
		attribute.setValue((String)value);
	}

	private Node loadNode() {
		if (this.node != null)
			return this.node;

		return this.api.openNode(String.valueOf(this.reference.getIdNode()));
	}

	@SuppressWarnings("unchecked")
	public <T extends BPIBaseNode<?>> T getReferencedNode() {
		Node node = this.loadNode();

		Definition definition = this.dictionary.getDefinition(node.getCode());
		BPIBaseNodeImpl<?> referencedNode = bpiClassLocator.getDefinitionInstance(definition.getName());
		referencedNode.injectNode(node);
		referencedNode.injectApi(this.api);
		referencedNode.injectBPIClassLocator(this.bpiClassLocator);
		referencedNode.injectDictionary(this.dictionary);
		return (T) referencedNode;
	}

	@Override
	public void save() {
		Node node = this.loadNode();
		this.api.saveNodeReference(node.getId(), this.reference);
	}

	@Override
	public Link toLink() {
		return new Link(Link.LinkType.NODE, this.reference.getIdNode(), this.reference.getLabel());
	}


}
