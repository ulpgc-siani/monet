package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.library.LibraryPath;
import org.monet.api.space.backservice.impl.model.Attribute;
import org.monet.api.space.backservice.impl.model.AttributeList;
import org.monet.api.space.backservice.impl.model.Indicator;
import org.monet.api.space.backservice.impl.model.Reference;
import org.monet.bpi.*;
import org.monet.bpi.exceptions.CantCallSaveOnSaveException;
import org.monet.bpi.types.Link;
import org.monet.bpi.types.Location;
import org.monet.metamodel.Definition;
import org.monet.metamodel.Dictionary;
import org.monet.metamodel.NodeDefinition;
import org.monet.v2.model.constants.Strings;
import org.monet.v3.BPIClassLocator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class NodeImpl implements Node, BehaviorNode {

	org.monet.api.space.backservice.impl.model.Node node;
	boolean isCreating;
	boolean isSaving;
	BPIClassLocator bpiClassLocator;
	BackserviceApi api;
	Dictionary dictionary;

	public void injectNode(org.monet.api.space.backservice.impl.model.Node node) {
		this.node = node;
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

	@Override
	public User getAuthor() {
		String ownerId = this.node.getOwnerId();
		if (ownerId == null)
			return null;

		org.monet.api.space.backservice.impl.model.User owner = this.api.loadUser(ownerId);
		if (owner == null)
			return null;

		UserImpl author = new UserImpl();
		author.injectUser(owner);

		return author;
	}

	@Override
	public String getOwnerId() {
		return this.node.getParentId();
	}

	@Override
	public Node getOwner() {
		String ownerId = this.node.getParentId();
		if (ownerId == null || ownerId.equals("-1"))
			return null;
		org.monet.api.space.backservice.impl.model.Node node = this.api.openNode(ownerId);
		Definition definition = this.dictionary.getDefinition(node.getCode());
		NodeImpl containedNode = bpiClassLocator.instantiateBehaviour(definition);
		containedNode.injectNode(node);
		containedNode.injectApi(this.api);
		containedNode.injectBPIClassLocator(this.bpiClassLocator);
		containedNode.injectDictionary(this.dictionary);
		return containedNode;
	}

	@Override
	public void setOwner(Node owner) {
		NodeImpl ownerImpl = (NodeImpl) owner;
		this.api.saveNodeParent(node.getId(), ownerImpl.node.getId());
	}

	@Override
	public boolean isPrototype() {
		if (this.node.isPrototype())
			return true;
		return this.node.getParentId() == null;
	}

	@Override
	public String getId() {
		return this.node.getId();
	}

	@Override
	public String getCode() {
		Definition definition = this.dictionary.getDefinition(node.getCode());
		return definition.getCode();
	}

	@Override
	public String getName() {
		Definition definition = this.dictionary.getDefinition(node.getCode());
		return definition.getName();
	}

	@Override
	public String getLabel() {
		return this.node.getLabel();
	}

	@Override
	public void setLabel(String label) {
		this.node.setLabel(label);
	}

	protected IndexEntry getIndexEntry(String name) {
		Reference reference = this.api.getNodeReference(this.node.getId(), name);
		Definition definition = this.dictionary.getDefinition(reference.getCode());
		IndexEntryImpl bpiIndexEntry = this.bpiClassLocator.instantiateBehaviour(definition);
		bpiIndexEntry.injectApi(this.api);
		bpiIndexEntry.injectBPIClassLocator(this.bpiClassLocator);
		bpiIndexEntry.injectDictionary(this.dictionary);
		bpiIndexEntry.injectIndexEntry(reference);
		bpiIndexEntry.injectNode(this.node);
		return bpiIndexEntry;
	}

	@Override
	public Map<String, String> getFlags() {
		return this.api.getNodeFlags(this.node.getId());
	}

	@Override
	public String getFlag(String name) {
		return this.api.getNodeFlags(this.node.getId()).get(name);
	}

	@Override
	public void setFlag(String name, String value) {
		this.api.addNodeFlag(this.node.getId(), name, value);
	}

	@Override
	public void removeFlag(String name) {
		this.api.deleteNodeFlag(this.node.getId(), name);
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
	public void setNote(String name, String value) {
		this.api.addNodeNote(this.node.getId(), name, value);
	}

	@Override
	public void removeNote(String name) {
		this.api.deleteNodeNote(this.node.getId(), name);
	}

	@Override
	public void save() {
		if (this.isSaving || this.isCreating)
			throw new CantCallSaveOnSaveException();
		this.api.saveNode(this.node);
	}

	@Override
	public void lock() {
		this.api.makeNodeUnEditable(this.node.getId());
		this.api.makeNodeUnDeleteable(this.node.getId());
	}

	@Override
	public void unLock() {
		this.api.makeNodeEditable(this.node.getId());
		this.api.makeNodeDeleteable(this.node.getId());
	}

	@Override
	public boolean isLocked() {
		return this.node.isLocked();
	}

	@Override
	public String getPartnerContext() {
		return this.node.getPartnerContext();
	}

	@Override
	public void setPartnerContext(String context) {

		if (context == null)
			return;

		String nodeContext = this.node.getPartnerContext();
		if (nodeContext != null && nodeContext.equals(context))
			return;

		this.api.saveNodePartnerContext(this.node.getId(), context);
	}

	@Override
	public void setEditable(boolean value) {
		if (value)
			this.api.makeNodeEditable(this.node.getId());
		else
			this.api.makeNodeUnEditable(this.node.getId());
	}

	@Override
	public boolean isEditable() {
		return this.node.isEditable();
	}

	@Override
	public void setDeletable(boolean value) {
		if (value)
			this.api.makeNodeDeleteable(this.node.getId());
		else
			this.api.makeNodeUnDeleteable(this.node.getId());
	}

	@Override
	public boolean isDeletable() {
		return this.node.isDeleteable();
	}

	@Override
	public void setLocation(Location location) {
		throw new NotImplementedException();
	}

	public Link toLink() {
		return new Link(Link.LinkType.NODE, this.node.getId(), this.getLabel());
	}

	public MonetLink toMonetLink() {
		return MonetLink.forNode(this.node.getId(), this.node.getLabel(), false);
	}

	public MonetLink toMonetLink(boolean editMode) {
		return MonetLink.forNode(this.node.getId(), this.node.getLabel(), editMode);
	}

	public void evaluateRules() {
		// Must be implemented by behaviors that have rules to evaluate
	}

	@Override
	public Node clone(Node bpiParent) {
		throw new NotImplementedException();
	}

	@Override
	public void merge(Node source) {
		throw new NotImplementedException();
	}

	@Override
	public void constructor() {
	}

	@Override
	public void onOpened() {
	}

	@Override
	public void onClosed() {
	}

	@Override
	public void onSave() {
	}

	@Override
	public void onSaved() {
	}

	@Override
	public void onSetContext() {
	}

	@Override
	public void onRemoved() {
	}

	@Override
	public void executeCommand(String operation) {
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

		if (iter.hasNext())
			result = attributes.get(iter.next());

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
	public boolean isComponent() {
		NodeDefinition definition = (NodeDefinition) this.dictionary.getDefinition(node.getCode());

		if (definition.isSingleton())
			return false;

		return definition.isComponent();
	}

}
