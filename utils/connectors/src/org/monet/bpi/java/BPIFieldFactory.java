package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.api.backservice.impl.model.Attribute;
import org.monet.api.backservice.impl.model.Node;
import org.monet.bpi.BPIField;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.metamodel.*;
import org.monet.v2.model.Dictionary;

import java.util.HashMap;

public class BPIFieldFactory {
	private static BPIFieldFactory instance;
	private HashMap<Class<? extends FieldDeclaration>, Class<? extends BPIFieldImpl<?>>> fields;
	private BackserviceApi api;
	private BPIClassLocator bpiClassLocator;
	private Dictionary dictionary;

	private BPIFieldFactory() {
		this.fields = new HashMap<Class<? extends FieldDeclaration>, Class<? extends BPIFieldImpl<?>>>();
		this.registerFields();
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private Boolean registerFields() {

		this.fields.put(BooleanFieldDeclaration.class, BPIFieldBooleanImpl.class);
		this.fields.put(CheckFieldDeclaration.class, BPIFieldCheckImpl.class);
		this.fields.put(DateFieldDeclaration.class, BPIFieldDateImpl.class);
		this.fields.put(FileFieldDeclaration.class, BPIFieldFileImpl.class);
		this.fields.put(NodeFieldDeclaration.class, (Class) BPIFieldNodeImpl.class);
		this.fields.put(LinkFieldDeclaration.class, (Class) BPIFieldLinkImpl.class);
		this.fields.put(NumberFieldDeclaration.class, BPIFieldNumberImpl.class);
		this.fields.put(PatternFieldDeclaration.class, BPIFieldPatternImpl.class);
		this.fields.put(PictureFieldDeclaration.class, BPIFieldPictureImpl.class);
		this.fields.put(SectionFieldDeclaration.class, BPIFieldSectionImpl.class);
		this.fields.put(SelectFieldDeclaration.class, BPIFieldSelectImpl.class);
		this.fields.put(TextFieldDeclaration.class, BPIFieldTextImpl.class);
		this.fields.put(MemoFieldDeclaration.class, BPIFieldMemoImpl.class);
		this.fields.put(SerialFieldDeclaration.class, BPIFieldSerialImpl.class);

		return true;
	}

	public synchronized static BPIFieldFactory getInstance() {
		if (instance == null) instance = new BPIFieldFactory();
		return instance;
	}

	public BPIField<?> get(String definitionName, FieldDeclaration fieldDeclaration, Attribute attribute, Node node) {
		BPIFieldImpl<?> bpiField;

		try {
			if (fieldDeclaration instanceof SectionFieldDeclaration) {
				bpiField = this.bpiClassLocator.getSectionInstance(definitionName, fieldDeclaration.getName());
				((BPIFieldSectionImpl) bpiField).injectNode(node);
			} else {
				bpiField = this.fields.get(fieldDeclaration.getClass()).newInstance();
			}
			bpiField.injectApi(this.api);
			bpiField.injectBPIClassLocator(this.bpiClassLocator);
			bpiField.injectDictionary(this.dictionary);
			bpiField.setNodeId(node.getId());
			bpiField.setAttribute(attribute);
			bpiField.setFieldDeclaration(fieldDeclaration);
		} catch (NullPointerException oException) {
			return null;
		} catch (Exception oException) {
			return null;
		}

		return bpiField;
	}

}