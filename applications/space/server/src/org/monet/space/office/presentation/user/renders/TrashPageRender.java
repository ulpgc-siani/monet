package org.monet.space.office.presentation.user.renders;

import org.monet.space.kernel.model.Trash;

public class TrashPageRender extends OfficeRender {

	public TrashPageRender() {
		super();
	}

	@Override
	protected void init() {
		loadCanvas("page.trash");

		OfficeRender trashRender = this.rendersFactory.get(new Trash(), "preview.html?mode=view", this.renderLink, account);
		trashRender.setParameters(this.getParameters());

		addMark("render(view.trash)", trashRender.getOutput());
	}

}