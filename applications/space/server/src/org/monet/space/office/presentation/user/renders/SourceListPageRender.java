package org.monet.space.office.presentation.user.renders;

import org.monet.space.kernel.model.SourceList;

public class SourceListPageRender extends OfficeRender {

	public SourceListPageRender() {
		super();
	}

	@Override
	protected void init() {
		loadCanvas("page.sourcelist");

		OfficeRender listRender = this.rendersFactory.get(new SourceList(), "preview.html?mode=view", this.renderLink, account);
		listRender.setParameters(this.getParameters());

		addMark("render(view.sourcelist)", listRender.getOutput());
	}

}