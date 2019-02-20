package org.monet.space.office.presentation.user.renders;


public class CatalogPageRender extends NodePageRender {

	public CatalogPageRender() {
		super();
	}

	@Override
	protected void init() {
		loadCanvas("page.node.catalog");
		super.init();
	}

	@Override
	protected void initControlInfo() {
		super.initControlInfo();
		addMark("addList", "");
		addMark("children", "");
	}

}
