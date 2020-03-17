package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.internal.*;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Language;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.presentation.user.constants.RenderParameter;

import java.util.ArrayList;
import java.util.HashMap;

public class LayoutViewRender extends ViewRender {
	private LayoutDefinition definition;

	public LayoutViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.definition = (LayoutDefinition) target;
	}

	@Override
	protected String initView(String codeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Attribute attribute = (Attribute)this.getParameter(RenderParameter.ATTRIBUTE);

		map.put("elements", this.initElements(this.definition.getElements(), attribute));

		return block("view", map);
	}

	private String initElements(ArrayList<LayoutElementDefinition> elements, Attribute container) {
		StringBuilder rows = new StringBuilder();
		StringBuilder columns = new StringBuilder();
		boolean lastElementIsBreak = false;

		for (LayoutElementDefinition elementDefinition : elements) {
			columns.append(this.initElement(elementDefinition, container));

			if (elementDefinition.isBreak() || elementDefinition.isSection()) {
				rows.append(this.renderRow(columns));
				columns = new StringBuilder();
				lastElementIsBreak = true;

				if (elementDefinition.isBreak())
					continue;
			}

			lastElementIsBreak = false;
		}

		if (!lastElementIsBreak)
			rows.append(this.renderRow(columns));

		return rows.toString();
	}

	private String initElement(LayoutElementDefinition elementDefinition, Attribute container) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String element = "";

		if (elementDefinition.isSection())
			element = this.initSection((LayoutElementSectionDefinition) elementDefinition);
		else if (elementDefinition.isBox())
			element = this.initBox((LayoutElementBoxDefinition) elementDefinition, container);
		else if (elementDefinition.isSpace())
			element = this.initSpace((LayoutElementSpaceDefinition) elementDefinition);
		else
			element = block("element", map);

		map.put("width", elementDefinition.getWidth() + elementDefinition.getWidthUnit());
		map.put("height", elementDefinition.getHeight() + elementDefinition.getHeightUnit());
		map.put("element", element);

		return block("column", map);
	}

	private String initSection(LayoutElementSectionDefinition sectionDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String label = Language.getInstance().getModelResource(sectionDefinition.getLabel());

		map.put("label", label != null ? label : sectionDefinition.getLabel());
		map.put("opened", sectionDefinition.isOpened() ? "opened" : "");
		map.put("elements", this.initElements(sectionDefinition.getElements(), null));

		return block("element.section", map);
	}

	private String initBox(LayoutElementBoxDefinition boxDefinition, Attribute container) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Node form = (Node) this.getParameter("form");
		FormDefinition formDefinition = (FormDefinition) form.getDefinition();
		FieldProperty fieldDefinition = formDefinition.getField(boxDefinition.getLink());
		Attribute attribute = null;

		if (container != null)
			attribute = container.getAttribute(fieldDefinition.getCode());
		else
		    attribute = form.getAttribute(fieldDefinition.getCode());

		if (fieldDefinition.isComposite() && boxDefinition.getElements() != null)
			return this.initBoxComposite(boxDefinition, attribute);
		else {
			Field field = new Field(form, attribute, fieldDefinition);
			OfficeRender render = this.rendersFactory.get(field, this.template, this.renderLink, account);

			render.setParameters(this.getParameters());
			render.setParameter(RenderParameter.READONLY, !allowEdition(form) ? "true" : "false");
			render.setParameter(RenderParameter.EDITION, boxDefinition.getEdition());
			render.setParameter(RenderParameter.FORMAT, boxDefinition.getFormat());

			map.put("width", boxDefinition.getWidth() + boxDefinition.getWidthUnit());
			map.put("height", boxDefinition.getHeight() + boxDefinition.getHeightUnit());
			map.put("footer", boxDefinition.getFooter());
			map.put("render(field)", render.getOutput());
		}

		return block("element.box", map);
	}

	private String initBoxComposite(LayoutElementBoxDefinition boxDefinition, Attribute attribute) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("elements", this.initElements(boxDefinition.getElements(), attribute));
		return block("element.box.composite", map);
	}

	private String initSpace(LayoutElementSpaceDefinition spaceDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("width", spaceDefinition.getWidth() + spaceDefinition.getWidthUnit());
		map.put("height", spaceDefinition.getHeight() + spaceDefinition.getHeightUnit());

		return block("element.space", map);
	}

	private String renderRow(StringBuilder columns) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("columns", columns.toString());
		return block("row", map);
	}

	@Override
	protected void init() {
		loadCanvas("view.layout");
		super.init();
	}

}
