package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.model.Source;

import java.util.HashMap;

public class SourceViewRender extends OfficeRender {
	private Source<SourceDefinition> source;

	public SourceViewRender() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setTarget(Object target) {
		this.source = (Source<SourceDefinition>) target;
	}

	private String initTermTemplate() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		return block("view$termTemplate:client-side", map);
	}

	@Override
	protected void init() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		loadCanvas("view.source");

		map.put("id", this.source.getId());
		map.put("code", this.source.getCode());
		map.put("label", this.source.getLabel());
		map.put("termTemplate", this.initTermTemplate());

		addMark("view", block("view", map));
	}

}