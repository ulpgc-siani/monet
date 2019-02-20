package org.monet.space.office.presentation.user.renders;

import java.util.HashMap;

public class SourceListViewRender extends OfficeRender {

	public SourceListViewRender() {
		super();
	}

	private String initSourceTemplate() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		return block("view$sourceTemplate:client-side", map);
	}

	private String initTermTemplate() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		return block("view$termTemplate:client-side", map);
	}

	@Override
	protected void init() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		loadCanvas("view.sourcelist");

		map.put("id", this.getParameterAsString("id"));
		map.put("sourceTemplate", this.initSourceTemplate());
		map.put("termTemplate", this.initTermTemplate());

		addMark("view", block("view", map));
	}

}