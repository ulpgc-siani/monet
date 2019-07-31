package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty.ShowProperty;
import org.monet.metamodel.internal.LayoutDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.presentation.user.constants.RenderParameter;

import java.util.HashMap;

public class FormViewRender extends NodeViewRender {
	private FormDefinition definition;

	public FormViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.node = (Node) target;
		this.definition = (FormDefinition) this.node.getDefinition();
	}

	protected void initContent(HashMap<String, Object> viewMap, FormViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String shows = "";
		ShowProperty showDefinition = viewDefinition.getShow();

		if (showDefinition == null) {
			map.put("shows", shows);
			viewMap.put("content", block("content", map));
			return;
		}

		if (showDefinition.getField() != null && showDefinition.getField().size() > 0)
			shows = this.initContentWithFields(showDefinition);
		else if (showDefinition.getLayout() != null)
			shows = this.initContentWithLayout(showDefinition);

		map.put("shows", shows);

		viewMap.put("content", block("content", map));
	}

	private String initContentWithFields(ShowProperty showDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String shows = "";

		for (Ref show : showDefinition.getField()) {
			FieldProperty fieldDefinition = this.definition.getField(show.getValue());
			Attribute attribute = this.node.getAttribute(fieldDefinition.getCode());
			Field field = new Field(this.node, attribute, fieldDefinition);

			OfficeRender render = this.rendersFactory.get(field, this.template, this.renderLink, account);
			render.setParameters(this.getParameters());
			render.setParameter(RenderParameter.READONLY, !allowEdition(node) ? "true" : "false");
			map.put("render(field)", render.getOutput());

			shows += block("show.field", map);
			map.clear();
		}

		return shows;
	}

	private String initContentWithLayout(ShowProperty showDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		LayoutDefinition layoutDefinition = this.dictionary.getLayoutDefinition(showDefinition.getLayout());
		String shows = "";

		OfficeRender render = this.rendersFactory.get(layoutDefinition, this.template, this.renderLink, account);
		render.setParameters(this.getParameters());
		render.setParameter(RenderParameter.READONLY, !allowEdition(node) ? "true" : "false");
		render.setParameter(RenderParameter.FORM, this.node);
		map.put("render(layout)", render.getOutput());

		shows = block("show.layout", map);

		return shows;
	}

	@Override
	protected String initView(String codeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		FormViewProperty view = (FormViewProperty) this.definition.getNodeView(codeView);

		if (this.node.requirePartnerContext() && this.node.getPartnerContext() == null) {
			String result = this.initPartnerContext();
			if (result != null) return result;
		}

		String result = initViewFromCode(codeView, view, map);
		if (result != null) return result;

		this.initMap(map, view);

		if (this.isSystemView(view)) {
			return this.initSystemView(map, view);
		}

		this.initContent(map, view);

		return block("view", map);
	}

	@Override
	protected void init() {
		loadCanvas("view.node.form");
		super.init();
	}

}
