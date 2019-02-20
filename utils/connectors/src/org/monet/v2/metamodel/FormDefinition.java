package org.monet.v2.metamodel;


import org.jdom.JDOMException;
import org.monet.api.backservice.impl.model.Attribute;
import org.monet.api.backservice.impl.model.AttributeList;
import org.monet.v2.metamodel.internal.SchemaDefinition;
import org.simpleframework.xml.core.Commit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class FormDefinition extends FormDefinitionBase {

	private ArrayList<FieldDeclaration> superFieldList = new ArrayList<FieldDeclaration>();
	private SchemaDefinition schemaDefinition;

	@Override
	public AttributeList buildAttributes() {
		return this.buildAttributeList(this._fieldDeclarationList);
	}

	private AttributeList buildAttributeList(List<FieldDeclaration> fieldDeclarations) {
		AttributeList attributeList = new AttributeList();

		for (FieldDeclaration fieldDeclaration : this._fieldDeclarationList) {
			Attribute attribute = this.buildAttribute(fieldDeclaration);
			attributeList.add(attribute);
		}

		return attributeList;
	}

	private Attribute buildAttribute(FieldDeclaration fieldDeclaration) {
		Attribute attribute = new Attribute();

		attribute.setCode(fieldDeclaration.getCode());

		if (fieldDeclaration.isMultiple())
			attribute.setAttributeList(new AttributeList());
		else {
			if(fieldDeclaration instanceof SectionFieldDeclaration) {
				SectionFieldDeclaration sectionFieldDeclaration = (SectionFieldDeclaration)fieldDeclaration;
				attribute.setAttributeList(this.buildAttributeList(sectionFieldDeclaration.getFieldDeclarationList()));
			} else {
				attribute.setAttributeList(new AttributeList());
			}
		}

		return attribute;
	}

	private void createFieldDeclarationList(SectionFieldDeclaration field) {
		for (FieldDeclaration childField : field.getFieldDeclarationList()) {

			this._fieldDeclarationMap.put(childField.getCode(), childField);
			this._fieldDeclarationMap.put(childField.getName(), childField);

			if (childField.isSuper())
				this.superFieldList.add(childField);

			if (childField.isSection())
				this.createFieldDeclarationList((SectionFieldDeclaration) childField);
			else if (childField.isBoolean())
				_booleanFieldDeclarationList.add((BooleanFieldDeclaration) childField);
			else if (childField.isCheck())
				_checkFieldDeclarationList.add((CheckFieldDeclaration) childField);
			else if (childField.isDate())
				_dateFieldDeclarationList.add((DateFieldDeclaration) childField);
			else if (childField.isFile())
				_fileFieldDeclarationList.add((FileFieldDeclaration) childField);
			else if (childField.isLink())
				_linkFieldDeclarationList.add((LinkFieldDeclaration) childField);
			else if (childField.isLocation())
				_locationFieldDeclarationList.add((LocationFieldDeclaration) childField);
			else if (childField.isMemo())
				_memoFieldDeclarationList.add((MemoFieldDeclaration) childField);
			else if (childField.isNode())
				_nodeFieldDeclarationList.add((NodeFieldDeclaration) childField);
			else if (childField.isNumber())
				_numberFieldDeclarationList.add((NumberFieldDeclaration) childField);
			else if (childField.isPattern())
				_patternFieldDeclarationList.add((PatternFieldDeclaration) childField);
			else if (childField.isPicture())
				_pictureFieldDeclarationList.add((PictureFieldDeclaration) childField);
			else if (childField.isSelect())
				_selectFieldDeclarationList.add((SelectFieldDeclaration) childField);
			else if (childField.isSerial())
				_serialFieldDeclarationList.add((SerialFieldDeclaration) childField);
			else if (childField.isText())
				_textFieldDeclarationList.add((TextFieldDeclaration) childField);
		}
	}

	protected void createFieldDeclarationList() {
		super.createFieldDeclarationList();
		for (SectionFieldDeclaration field : this._sectionFieldDeclarationList) {
			if (field.isSuper())
				this.superFieldList.add(field);
			this.createFieldDeclarationList(field);
		}
	}

	@Commit
	public void commit() {

		if (_fieldDeclarationMap == null)
			createFieldDeclarationMap();

		for (FormViewDeclaration view : this._formViewDeclarationList) {

			switch (view.getType()) {
				case TAB:
					this.tabViews.add(view);
					if (view.isDefault())
						this.defaultTabView = view;
					break;
				case EMBEDDED:
					this.embeddedViews.add(view);
					if (view.isDefault())
						this.defaultEmbeddedView = view;
					break;
			}

			this.viewsMap.put(view.getCode(), view);
			this.viewsMap.put(view.getName(), view);
		}

	}

	public List<FieldDeclaration> getSuperFieldDeclarationList() {
		return this.superFieldList;
	}

	public boolean hasSuperFieldDeclarations() {
		return this.superFieldList.size() > 0;
	}

	public SchemaDefinition getSchemaDefinition() {
		if (this.schemaDefinition == null) {
			this.schemaDefinition = new SchemaDefinition();
			try {
				if (this.getSchema() != null)
					this.schemaDefinition.unserializeFromXML(this.getSchema().getContent());
			} catch (JDOMException e) {
				return null;
			}
		}

		return this.schemaDefinition;
	}

	public HashSet<String> getSchemaAttributesUsingSuperFields() {
		HashMap<String, String> fieldsAttributes;
		HashSet<String> result = new HashSet<String>();

		if (!this.hasSuperFieldDeclarations())
			return result;

		fieldsAttributes = this.getSchemaDefinition().getFieldsAttributesMap();

		for (FieldDeclaration field : this.superFieldList) {
			if (fieldsAttributes.containsKey(field._name))
				result.add(fieldsAttributes.get(field._code));
		}

		return result;
	}

}
