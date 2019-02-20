package org.monet.space.office.presentation.user.renders;

public class CatalogPrintRender extends SetPrintRender {

	public CatalogPrintRender() {
		super();
	}

	@Override
	protected void init() {
		loadCanvas("print.node.collection");
		super.init();
	}

}
