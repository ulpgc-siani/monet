package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.NodeDefinition;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.configuration.Configuration;

public abstract class NodePrintRender extends PrintRender {
	protected Node node;
	protected NodeDefinition definition;

	public NodePrintRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.node = (Node) target;
		this.definition = this.node.getDefinition();
	}

	@Override
	protected void init() {
		loadCanvas("print.node");

		addMark("label", this.node.getLabel());
		addMark("description", this.definition.getDescription());
		addMark("type", this.getNodeType(this.node));
		addMark("stylesUrl", Configuration.getInstance().getApiUrl() + "?op=loadthemefile&path=_styles");

		this.initContent();
	}

	protected void initContent() {
		addMark("orientation", "portrait");
	}

}
