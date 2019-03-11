package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.metamodel.internal.Ref;

import java.util.ArrayList;
import java.util.HashMap;

public class CatalogViewRender extends SetViewRender {

	public CatalogViewRender() {
		super();
	}

	@Override
	protected ArrayList<String> getViewSelects(SetViewProperty viewDefinition) {
		ArrayList<Ref> selectList = viewDefinition.getSelect().getNode();
		ArrayList<String> result = new ArrayList<String>();

		for (Ref select : selectList) {
			result.add(this.dictionary.getDefinitionCode(select.getValue()));
		}

		return result;
	}

	@Override
	protected String initAddList(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
		return "";
	}

	@Override
	protected String initMagnets(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
		return "";
	}

	@Override
	protected String initView(String codeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SetViewProperty view = (SetViewProperty) this.definition.getNodeView(codeView);

		if (this.node.requirePartnerContext() && this.node.getPartnerContext() == null) {
			String result = this.initPartnerContext();
			if (result != null) return result;
		}

		boolean isLocationView = codeView.equals("location");
		if (isLocationView) {
			this.initMapWithoutView(map, "location");
			return this.initLocationSystemView(map);
		} else if (view == null) {
			map.put("codeView", codeView);
			map.put("labelDefinition", this.definition.getLabelString());
			return block("view.undefined", map);
		}

		this.initMap(map, view);
		map.put("clec", "clec");

		if (this.isSystemView(view)) {
			return this.initSystemView(map, view);
		}

		this.initContent(map, view);

		return block("view", map);
	}

	@Override
	protected void init() {
		loadCanvas("view.node.catalog");
		super.init();
	}

}
