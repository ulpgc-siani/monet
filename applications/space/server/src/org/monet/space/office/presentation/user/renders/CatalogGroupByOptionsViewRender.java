package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.space.kernel.model.Node;

public class CatalogGroupByOptionsViewRender extends CatalogViewRender {

	public CatalogGroupByOptionsViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		if (target == null || !(target instanceof Node)) return;
		super.setTarget(target);
	}

	@Override
	protected String initView(String codeView) {
		SetViewProperty view = (SetViewProperty) this.definition.getNodeView(codeView);
		String groupByCode = this.getParameterAsString("groupby");
		return this.initGroupByOptions(groupByCode, view);
	}

}
