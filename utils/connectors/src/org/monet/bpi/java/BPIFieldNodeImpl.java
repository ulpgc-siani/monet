package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Indicator;
import org.monet.api.backservice.impl.model.Node;
import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPIFieldNode;
import org.monet.bpi.types.Link;
import org.monet.bpi.types.Term;
import org.monet.v2.metamodel.Definition;

public class BPIFieldNodeImpl<N extends BPIBaseNode<?>> extends BPIFieldImpl<Link> implements BPIFieldNode<N> {

	@Override
	public Link get() {
		String code = this.getIndicatorValue(Indicator.CODE);
		String value = this.getIndicatorValue(Indicator.VALUE);
		return new Link(Link.LinkType.NODE, code, value);
	}

	@Override
	public Term getAsTerm() {
		Link link = this.get();
		if (link == null)
			return null;
		return new Term(link.getId(), link.getLabel());
	}

	@SuppressWarnings("unchecked")
	@Override
	public N getNode() {
		Node node = this.api.openNode(this.get().getId());
		Definition definition = this.dictionary.getDefinition(node.getCode());
		BPIBaseNodeImpl<?> containedNode = bpiClassLocator.getDefinitionInstance(definition.getName());
		containedNode.injectNode(node);
		containedNode.injectApi(this.api);
		containedNode.injectDictionary(this.dictionary);
		containedNode.injectBPIClassLocator(this.bpiClassLocator);
		return (N) containedNode;
	}

	@Override
	public void set(Link value) {
		this.setIndicatorValue(Indicator.CODE, value.getId().toString());
		this.setIndicatorValue(Indicator.NODE, value.getId().toString());
		this.setIndicatorValue(Indicator.VALUE, value.getLabel().toString());
	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof Link)
			return this.get().equals(value);
		else
			return false;
	}

	@Override
	public void clear() {
		this.set(new Link(Link.LinkType.NODE, "", ""));
	}

}