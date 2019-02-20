package org.monet.space.office.presentation.user.renders;

import java.util.HashMap;

public class TrashViewRender extends OfficeRender {

	public TrashViewRender() {
		super();
	}

	private String initItemTemplate() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		return block("view$itemTemplate:client-side", map);
	}

	@Override
	protected void init() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		loadCanvas("view.trash");

		map.put("id", this.getParameterAsString("id"));
		map.put("itemTemplate", this.initItemTemplate());

		addMark("view", block("view", map));
	}

}