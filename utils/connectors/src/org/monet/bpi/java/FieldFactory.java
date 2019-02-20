package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.model.Attribute;
import org.monet.api.space.backservice.impl.model.Node;
import org.monet.bpi.Field;
import org.monet.metamodel.*;
import org.monet.v3.BPIClassLocator;

import java.util.HashMap;

public class FieldFactory {
	private static FieldFactory instance;
	private HashMap<String, Class<? extends FieldImpl<?>>> fields;
	private BPIClassLocator bpiClassLocator;
	private BackserviceApi api;
	private Dictionary dictionary;

	private FieldFactory() {
		this.fields = new HashMap<String, Class<? extends FieldImpl<?>>>();
		this.registerFields();
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private Boolean registerFields() {

		this.fields.put(BooleanFieldProperty.class.getName(), FieldBooleanImpl.class);
		this.fields.put(CheckFieldProperty.class.getName(), FieldCheckImpl.class);
		this.fields.put(DateFieldProperty.class.getName(), FieldDateImpl.class);
		this.fields.put(FileFieldProperty.class.getName(), FieldFileImpl.class);
		this.fields.put(NodeFieldProperty.class.getName(), (Class) FieldNodeImpl.class);
		this.fields.put(LinkFieldProperty.class.getName(), (Class) FieldLinkImpl.class);
		this.fields.put(NumberFieldProperty.class.getName(), FieldNumberImpl.class);
		this.fields.put(PictureFieldProperty.class.getName(), FieldPictureImpl.class);
		this.fields.put(CompositeFieldProperty.class.getName(), FieldCompositeImpl.class);
		this.fields.put(SelectFieldProperty.class.getName(), FieldSelectImpl.class);
		this.fields.put(TextFieldProperty.class.getName(), FieldTextImpl.class);
		this.fields.put(MemoFieldProperty.class.getName(), FieldMemoImpl.class);
		this.fields.put(SerialFieldProperty.class.getName(), FieldSerialImpl.class);
		this.fields.put(SummationFieldProperty.class.getName(), FieldSummationImpl.class);

		return true;
	}

	public synchronized static FieldFactory getInstance() {
		if (instance == null) instance = new FieldFactory();
		return instance;
	}

	public Field<?> get(String definitionName, FieldProperty fieldProperty, Attribute attribute, Node node) {
		FieldImpl<?> bpiField;

		try {
			if (fieldProperty instanceof CompositeFieldProperty) {
				bpiField = this.bpiClassLocator.instantiateBehaviour(fieldProperty);
				((FieldCompositeImpl) bpiField).injectNode(node);
			} else {
				Class<?> declarationClass = fieldProperty.getClass();
				Class<? extends FieldImpl<?>> fieldClass = null;
				while (fieldClass == null && !declarationClass.equals(Object.class)) {
					fieldClass = this.fields.get(declarationClass.getName());
					declarationClass = declarationClass.getSuperclass();
				}
				bpiField = fieldClass.newInstance();
			}
			bpiField.injectBPIClassLocator(this.bpiClassLocator);
			bpiField.injectApi(this.api);
			bpiField.injectDictionary(this.dictionary);
			bpiField.setNodeId(node.getId());
			bpiField.setAttribute(attribute);
			bpiField.setFieldDefinition(fieldProperty);
		} catch (NullPointerException oException) {
			return null;
		} catch (Exception oException) {
			return null;
		}

		return bpiField;
	}

}