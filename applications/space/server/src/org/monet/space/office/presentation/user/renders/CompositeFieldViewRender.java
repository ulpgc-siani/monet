package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.CompositeFieldProperty;
import org.monet.metamodel.CompositeFieldPropertyBase;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.internal.LayoutDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.office.presentation.user.constants.RenderParameter;

import java.util.ArrayList;
import java.util.HashMap;

public class CompositeFieldViewRender extends FieldViewRender {

	public CompositeFieldViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.field = (Field) target;
		this.definition = (CompositeFieldProperty)this.field.getFieldDefinition();
	}

	@Override
	protected void initAttributes(HashMap<String, Object> viewMap, String id) {
		super.initAttributes(viewMap, id);
		CompositeFieldProperty definition = (CompositeFieldProperty)this.definition;
		viewMap.put("conditional", definition.isConditional() ? "conditional" : "");
		viewMap.put("extensible", definition.isExtensible() ? "extensible" : "");
	}

	@Override
	protected void initLabel(HashMap<String, Object> viewMap, String id, String codeView) {
		String conditioned = this.getIndicatorValue(this.field.getAttribute(), "conditioned");
		String conditionedValue = "";
		CompositeFieldProperty definition = (CompositeFieldProperty)this.definition;

		super.initLabel(viewMap, id, codeView);

		if (definition.isConditional())
			conditionedValue = conditioned.equals("conditioned")?block("label$conditioned", viewMap):block("label$conditioned.no", viewMap);

		viewMap.put("conditioned", conditionedValue);
		viewMap.put("label", block("label", viewMap));
	}

	@Override
	protected String getFieldType() {
		return "composite";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		CompositeFieldProperty compositeDefinition = (CompositeFieldProperty) this.definition;
		String index = "";
		CompositeFieldPropertyBase.ViewProperty viewDefinition = compositeDefinition.getView();
		String fields = this.renderFields(id, attribute, viewDefinition, isTemplate);

		map.put("labelField", "");
		if (viewDefinition != null && viewDefinition.getSummary() != null && viewDefinition.getSummary().getField().size() > 0) {
			Ref firstFieldRef = viewDefinition.getSummary().getField().get(0);
			String codeField = compositeDefinition.getField(firstFieldRef.getValue()).getCode();
			map.put("labelField", codeField);
		}
		declarationsMap.put("concreteDeclarations", block("field.composite$concreteDeclarations", map));

		map.put("id", id);
		map.put("value", this.getIndicatorValue(attribute, Indicator.VALUE));
		map.put("index", index);
		map.put("fields", fields);

		return block("field.composite", map);
	}

	private String renderFields(String id, Attribute attribute, CompositeFieldPropertyBase.ViewProperty viewDefinition, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<String> fieldNames = new ArrayList<String>();
		FormDefinition formDefinition = (FormDefinition) this.field.getNode().getDefinition();
		CompositeFieldProperty definition = (CompositeFieldProperty)this.definition;
		CompositeFieldPropertyBase.ViewProperty.ShowProperty showDefinition = viewDefinition!=null?viewDefinition.getShow():null;

		if (showDefinition != null && showDefinition.getLayout() != null) {
			LayoutDefinition layoutDefinition = this.dictionary.getLayoutDefinition(showDefinition.getLayout());

			OfficeRender render = this.rendersFactory.get(layoutDefinition, this.template, this.renderLink, account);
			render.setParameters(this.getParameters());
			render.setParameter(RenderParameter.ATTRIBUTE, attribute);
			render.setParameter(RenderParameter.FORM, this.field.getNode());
			render.setParameter(RenderParameter.ID, id);
			render.setParameter(RenderParameter.IS_ROOT, "false");
			render.setParameter(RenderParameter.IS_TEMPLATE, isTemplate);

			return render.getOutput();
		}

		String result = "";

		if (showDefinition != null && showDefinition.getField().size() > 0) {
			for (Ref show : showDefinition.getField())
				fieldNames.add(show.getValue());
		} else {
			for (FieldProperty fieldDefinition : definition.getAllFieldPropertyList())
				fieldNames.add(fieldDefinition.getName());
		}

		for (String fieldName : fieldNames) {
			FieldProperty fieldDeclaration = formDefinition.getField(fieldName);
			Attribute childAttribute = (attribute != null) ? attribute.getAttribute(fieldDeclaration.getCode()) : null;
			Field field = new Field(this.field.getNode(), childAttribute, fieldDeclaration);

			OfficeRender render = this.rendersFactory.get(field, this.template, this.renderLink, account);
			render.setParameters(this.getParameters());
			render.setParameter(RenderParameter.ID, id);
			render.setParameter(RenderParameter.IS_ROOT, "false");
			render.setParameter(RenderParameter.IS_TEMPLATE, isTemplate);

			if (fieldDeclaration.isComposite() && fieldDeclaration.isMultiple())
				render.setParameter("view", "preview");

			map.put("render(view.field)", render.getOutput());
			result += block("field.composite$field", map);
			map.clear();
		}

		return result;
	}

}
