package org.monet.metamodel;

import org.monet.api.space.backservice.impl.model.Attribute;
import org.monet.api.space.backservice.impl.model.AttributeList;
import org.monet.metamodel.interfaces.HasMappings;
import org.monet.metamodel.interfaces.HasSchema;
import org.monet.metamodel.interfaces.IsInitiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class FormDefinition extends FormDefinitionBase implements IsInitiable, HasMappings {
	private ArrayList<FieldProperty> superFieldList = new ArrayList<FieldProperty>();
	private HashMap<String, String> fieldsPath = new HashMap<String, String>();

	@Override
	public AttributeList buildAttributes() {
		return this.buildAttributeList(this._allFieldPropertyList);
	}

	private AttributeList buildAttributeList(List<FieldProperty> fieldDefinitions) {
		AttributeList attributeList = new AttributeList();

		for (FieldProperty fieldDefinition : fieldDefinitions) {
			Attribute attribute = this.buildAttribute(fieldDefinition);
			attributeList.add(attribute);
		}

		return attributeList;
	}

	private Attribute buildAttribute(FieldProperty fieldDeclaration) {
		Attribute attribute = new Attribute();

		attribute.setCode(fieldDeclaration.getCode());

		if (fieldDeclaration.isMultiple())
			attribute.setAttributeList(new AttributeList());
		else {
			if(fieldDeclaration instanceof CompositeFieldProperty) {
				CompositeFieldProperty compositeFieldProperty = (CompositeFieldProperty)fieldDeclaration;
				attribute.setAttributeList(this.buildAttributeList(compositeFieldProperty.getAllFieldPropertyList()));
			} else {
				attribute.setAttributeList(new AttributeList());
			}
		}

		return attribute;
	}

	private void createFieldPropertyList(CompositeFieldProperty field) {

		if (!this.fieldsPath.containsKey(field.getCode()))
			this.fieldsPath.put(field.getCode(), field.getCode());

		for (FieldProperty childField : field.getAllFieldPropertyList()) {

			this._allFieldPropertyMap.put(childField.getCode(), childField);
			this._allFieldPropertyMap.put(childField.getName(), childField);

			if (childField.isSuperfield())
				this.superFieldList.add(childField);

			this.fieldsPath.put(childField.getCode(), this.fieldsPath.get(field.getCode()) + "." + childField.getCode());

			if (childField.isComposite())
				this.createFieldPropertyList((CompositeFieldProperty) childField);
			else if (childField.isBoolean())
				addBooleanFieldProperty((BooleanFieldProperty) childField);
			else if (childField.isCheck())
				addCheckFieldProperty((CheckFieldProperty) childField);
			else if (childField.isDate())
				addDateFieldProperty((DateFieldProperty) childField);
			else if (childField.isFile())
				addFileFieldProperty((FileFieldProperty) childField);
			else if (childField.isLink())
				addLinkFieldProperty((LinkFieldProperty) childField);
			else if (childField.isMemo())
				addMemoFieldProperty((MemoFieldProperty) childField);
			else if (childField.isNode())
				addNodeFieldProperty((NodeFieldProperty) childField);
			else if (childField.isNumber())
				addNumberFieldProperty((NumberFieldProperty) childField);
			else if (childField.isPicture())
				addPictureFieldProperty((PictureFieldProperty) childField);
			else if (childField.isSelect())
				addSelectFieldProperty((SelectFieldProperty) childField);
			else if (childField.isSerial())
				addSerialFieldProperty((SerialFieldProperty) childField);
			else if (childField.isText())
				addTextFieldProperty((TextFieldProperty) childField);
			else if (childField.isSummation())
				addSummationFieldProperty((SummationFieldProperty) childField);
		}
	}

	protected void createFieldPropertyList() {
		super.createAllFieldPropertyList();

		for (FieldProperty field : this._allFieldPropertyList) {
			if (field.isComposite()) {
				if (field.isSuperfield())
					this.superFieldList.add(field);
				this.createFieldPropertyList((CompositeFieldProperty) field);
			} else
				this.fieldsPath.put(field.getCode(), field.getCode());
		}
	}

	public void init() {

		this.createAllFieldPropertyMap();
		this.createFieldPropertyList();

		for (FormViewProperty view : this._formViewPropertyMap.values()) {

			if (view.isDefault() && !view.isVisibleWhenEmbedded())
				this.defaultView = view;

			this.viewsMap.put(view.getCode(), view);
			this.viewsMap.put(view.getName(), view);

			if (!view.isVisibleWhenEmbedded())
				this.tabViewList.add(view);

			this.viewList.add(view);
		}

	}

	public List<FieldProperty> getSuperFieldPropertyList() {
		return this.superFieldList;
	}

	public boolean hasSuperFieldProperties() {
		return this.superFieldList.size() > 0;
	}

	public HashSet<String> getSchemaAttributesUsingSuperFields() {
		HashMap<String, String> fieldsAttributes;
		HashSet<String> result = new HashSet<String>();

		if (!this.hasSuperFieldProperties())
			return result;

		fieldsAttributes = ((HasSchema) this).getSchemaDefinition().getFieldsAttributesMap();

		for (FieldProperty field : this.superFieldList) {
			if (fieldsAttributes.containsKey(field._name))
				result.add(fieldsAttributes.get(field._code));
		}

		return result;
	}

	public String getFieldPath(String key) {
		FieldProperty fieldDefinition = this.getField(key);
		return this.fieldsPath.get(fieldDefinition.getCode());
	}

	protected void initRulesMap() {
		super.initRulesMap();
		for (RuleFormProperty rule : this._ruleFormPropertyList)
			this.rulesMap.put(rule.getCode(), rule);
	}

	@Override
	public Class<?> getMappingClass(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<String> getSources() {
		ArrayList<String> sourceList = new ArrayList<String>();

		for (SummationFieldProperty property : this.getSummationFieldPropertyList())
			if (property.getSource() != null)
				sourceList.add(property.getSource().getValue());

		for (CheckFieldProperty property : this.getCheckFieldPropertyList())
			if (property.getSource() != null)
				sourceList.add(property.getSource().getValue());

		for (SelectFieldProperty property : this.getSelectFieldPropertyList())
			if (property.getSource() != null)
				sourceList.add(property.getSource().getValue());

		return sourceList;
	}

	public HashMap<String, FieldProperty> getFields() {
		return this._allFieldPropertyMap;
	}

}
