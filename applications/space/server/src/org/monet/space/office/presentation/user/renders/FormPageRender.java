package org.monet.space.office.presentation.user.renders;


public class FormPageRender extends NodePageRender {

	public FormPageRender() {
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
		loadCanvas("page.node.form");
		super.init();
	}

}
