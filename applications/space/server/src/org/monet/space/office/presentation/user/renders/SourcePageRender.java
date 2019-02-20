package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.model.Source;

import java.util.HashMap;


public class SourcePageRender extends OfficeRender {
	private Source<SourceDefinition> source;

	public SourcePageRender() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setTarget(Object target) {
		this.source = (Source<SourceDefinition>) target;
	}

	@Override
	protected void init() {
		loadCanvas("page.source");

		OfficeRender render = this.rendersFactory.get(this.source, "preview.html?mode=view", this.renderLink, account);
		render.setParameters(this.getParameters());

		String blockName = (this.source.isGlossary()) ? "label.glossary" : "label";
		String businessUnitLabel = this.source.getPartnerLabel();
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("label", this.source.getLabel());
		map.put("id", this.source.getId());

		addMark("label", block(blockName, map));
		addMark("labelBreadcrumbs", this.source.getLabel());
		addMark("businessUnitLabel", businessUnitLabel != null ? businessUnitLabel : "");
		addMark("render(view.source)", render.getOutput());
	}

}