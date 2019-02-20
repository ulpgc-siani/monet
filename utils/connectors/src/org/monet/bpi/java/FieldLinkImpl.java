package org.monet.bpi.java;

import org.monet.api.space.backservice.impl.model.Indicator;
import org.monet.bpi.FieldLink;
import org.monet.bpi.Node;
import org.monet.bpi.types.Link;
import org.monet.bpi.types.Term;
import org.monet.metamodel.Definition;

public class FieldLinkImpl extends FieldImpl<Link> implements FieldLink {

	public Link get() {
		String code = this.getIndicatorValue(Indicator.CODE);
		String value = this.getIndicatorValue(Indicator.VALUE);

		if (code.isEmpty() && value.isEmpty())
			return null;

		return new Link(Link.LinkType.NODE, code, value);
	}

	@Override
	public Term getAsTerm() {
		Link link = this.get();
		if (link == null)
			return null;
		return new Term(link.getId(), link.getLabel());
	}

	@Override
	public Node getNode() {
		if (this.get() != null) {
			org.monet.api.space.backservice.impl.model.Node node = this.api.openNode(this.get().getId());
			Definition definition = this.dictionary.getDefinition(node.getCode());
			NodeImpl linkedNode = bpiClassLocator.instantiateBehaviour(definition);
			linkedNode.injectNode(node);
			linkedNode.injectApi(this.api);
			linkedNode.injectDictionary(this.dictionary);
			linkedNode.injectBPIClassLocator(this.bpiClassLocator);
			return linkedNode;
		}
		return null;
	}

	@Override
	public void set(Link value) {
		if (value == null) return;

		String id = value.getId();
		String label = value.getLabel();

		this.setIndicatorValue(Indicator.CODE, id.toString());
		this.setIndicatorValue(Indicator.NODE_LINK, id.toString());
		this.setIndicatorValue(Indicator.VALUE, label.toString());
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