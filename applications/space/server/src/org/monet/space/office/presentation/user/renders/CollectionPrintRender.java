package org.monet.space.office.presentation.user.renders;

public class CollectionPrintRender extends SetPrintRender {

	public CollectionPrintRender() {
		super();
	}

	@Override
	protected void init() {
		loadCanvas("print.node.collection");
		super.init();
	}

}
