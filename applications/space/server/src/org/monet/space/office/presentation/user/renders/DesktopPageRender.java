package org.monet.space.office.presentation.user.renders;


public class DesktopPageRender extends NodePageRender {

	public DesktopPageRender() {
		super();
	}

	@Override
	protected void initControlInfo() {
		super.initControlInfo();
		addMark("children", "");
		addMark("addList", "");
	}

	@Override
	protected void init() {
		loadCanvas("page.node.desktop");
		super.init();
	}

}
