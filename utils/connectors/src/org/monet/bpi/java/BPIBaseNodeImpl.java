package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.api.backservice.impl.model.Attribute;
import org.monet.api.backservice.impl.model.AttributeList;
import org.monet.api.backservice.impl.model.Indicator;
import org.monet.api.backservice.impl.model.Node;
import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPIReference;
import org.monet.bpi.BPISchema;
import org.monet.bpi.exceptions.CantCallSaveOnSavingException;
import org.monet.bpi.types.Link;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.metamodel.Definition;
import org.monet.v2.model.Dictionary;
import org.monet.v2.model.LibraryPath;
import org.monet.v2.model.constants.Strings;
import org.simpleframework.xml.core.Persister;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BPIBaseNodeImpl<Schema extends BPISchema> implements BPIBaseNode<Schema> {

	HashMap<String, String> dynNames = new HashMap<String, String>();
	Node node;
	BPIClassLocator bpiClassLocator;
	protected BackserviceApi api;
	boolean isSaving;
	Dictionary dictionary;

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectNode(Node node) {
		this.node = node;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	protected void setDynamicName(String key, String value) {
		this.dynNames.put(key, value);
	}

	protected String resolve(String key) {
		return this.dynNames.get(key);
	}

	@Override
	public String getLabel() {
		return this.node.getLabel();
	}

	@Override
	public void setLabel(String label) {
		this.node.setLabel(label);
	}

	protected BPIReference getReference(String name) {
		return null;
	}

	@Override
	public Map<String, String> getNotes() {
		return this.api.getNodeNotes(this.node.getId());
	}

	@Override
	public String getNote(String name) {
		return this.api.getNodeNotes(this.node.getId()).get(name);
	}

	@Override
	public void addNote(String name, String value) {
		this.api.addNodeNote(this.node.getId(), name, value);
	}

	@Override
	public void deleteNote(String name) {
		this.api.deleteNodeNote(this.node.getId(), name);
	}

	@Override
	public Schema getSchema() {
		Definition definition = this.dictionary.getDefinition(this.node.getCode());
		Class<Schema> schemaClass = this.bpiClassLocator.getSchemaClass(definition.getName());
		Persister persister = new Persister();
		try {
			return persister.read(schemaClass, this.api.getNodeSchema(this.node.getId()));
		} catch (Exception e) {
			try {
				return schemaClass.newInstance();
			} catch (Exception ex) {
				return null;
			}
		}
	}

	protected AttributeList getAttributeList(String path) {
		AttributeList attributeList = null;
		Attribute attribute;
		String[] pathArray;
		int iPos = 0;

		pathArray = LibraryPath.splitAttributePath(path);
		attributeList = this.node.getAttributeList();
		while (iPos < pathArray.length - 1) {
			attribute = (Attribute) attributeList.get(pathArray[iPos]);
			if (attribute == null)
				return null;
			attributeList = attribute.getAttributeList();
			iPos++;
		}

		return attributeList;
	}

	protected Attribute getAttribute(String path) {
		AttributeList attributeList = this.getAttributeList(path);
		HashMap<String, Attribute> attributes;
		Iterator<String> iter;
		Attribute result = null;
		String codeAttribute;

		if (attributeList == null)
			return result;

		codeAttribute = LibraryPath.getAttributeCode(path);
		attributes = attributeList.getByCode(codeAttribute);
		iter = attributes.keySet().iterator();

		if (iter.hasNext()) {
			result = attributes.get(iter.next());
			// if (oResult.isMultiple() && definition is simple) oResult =
			// oResult.getFirstAttribute(codeAttribute);
		}

		return result;
	}

	protected String getIndicatorValue(String path) {
		Attribute attribute;
		Indicator indicator;
		String attributePath, codeAttribute, codeIndicator;

		if (path.indexOf(LibraryPath.SEPARATOR) == -1)
			return Strings.EMPTY;

		attributePath = LibraryPath.getAttributePath(path);
		codeAttribute = LibraryPath.getAttributeCode(attributePath);
		codeIndicator = LibraryPath.getIndicatorCode(path);

		if ((attribute = this.getAttribute(attributePath)) == null)
			return Strings.EMPTY;

		indicator = (Indicator) attribute.getIndicatorList().get(codeIndicator);
		if ((indicator == null) && (attribute = (Attribute) attribute.getFirstAttribute(codeAttribute)) != null) {
			if ((indicator = (Indicator) attribute.getIndicatorList().get(codeIndicator)) == null)
				return Strings.EMPTY;
		}

		if (indicator == null)
			return Strings.EMPTY;

		return indicator.getData();
	}

	@Override
	public void save() {
		if (this.isSaving)
			throw new CantCallSaveOnSavingException();
		this.api.saveNode(this.node);
	}

	@Override
	public void saveNotes() {
		if (this.isSaving)
			throw new CantCallSaveOnSavingException();
		//this.api.saveNodeNotes(this.node);
	}

	@Override
	public void lock() {
		//this.api.lockNode(this.node);
	}

	@Override
	public void unLock() {
		//this.api.unLockNode(this.node);
	}

	public Link toLink() {
		return new Link(Link.LinkType.NODE, this.node.getId(), this.getLabel());
	}

}
