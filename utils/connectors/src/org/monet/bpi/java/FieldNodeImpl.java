package org.monet.bpi.java;

import org.monet.api.space.backservice.impl.model.Indicator;
import org.monet.bpi.FieldNode;
import org.monet.bpi.Node;
import org.monet.bpi.types.Link;
import org.monet.bpi.types.Term;
import org.monet.metamodel.Definition;

public class FieldNodeImpl extends FieldImpl<Link> implements FieldNode {

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
	public <T extends Node> T getNode() {
		org.monet.api.space.backservice.impl.model.Node node = this.api.openNode(this.get().getId());
		Definition definition = this.dictionary.getDefinition(node.getCode());
		NodeImpl containedNode = bpiClassLocator.instantiateBehaviour(definition);
		containedNode.injectNode(node);
		containedNode.injectApi(api);
		return (T)containedNode;
	}

	@Override
	public void set(Link value) {
		this.api.saveNodeParent(value.getId(), this.nodeId);
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