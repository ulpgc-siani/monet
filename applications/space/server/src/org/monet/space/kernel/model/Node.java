/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.monet.metamodel.*;
import org.monet.metamodel.ProjectBase.TypeEnumeration;
import org.monet.metamodel.RuleProperty.ListenProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.FilterProperty;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.metamodel.internal.SchemaDefinition;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryArray;
import org.monet.space.kernel.library.LibraryPath;
import org.monet.space.kernel.library.LibraryXML;
import org.monet.space.kernel.model.map.Location;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.monet.space.kernel.model.Dictionary;

public class Node<T extends NodeDefinition> extends Entity<T> implements ISecurable, Serializable {

	private static final long serialVersionUID = -1095996627571572741L;

	private String idParent;
	private String partnerContext;
	private String idPrototype;
	private DefinitionType type;
	private Node parentNode;
	private NodeList ancestors;
	private NodeList ownedPrototypes;
	private NodeList sharedPrototypes;
	private ArrayList<String> ancestorsIds;
	private String idOwner;
	private User owner;
	private int order;
	private String content;
	private String data;
	private Map<String, String> flags;
	private Map<String, String> notes;
	private boolean isValid;
	private HashMap<String, Reference> references;
	private ReferenceLink referenceLink;
	private NodeList nodeList;
	private NodeList clientNodeList;
	private TaskList clientTaskList;
	private MonetHashMap<String> nodeLabels;
	private AttributeList attributeList;
	private PermissionList permissionList;
	private HashSet<String> grantedUsers;
	private int linksCounter;
	private String schema;
	private HashMap<String, SuperDataItem> superData;
	private NodeLink nodeLink;
	private int linksFromTasksCounter;
	private int attachmentsCounter;
	private TaskList linkedTasks;
	private Location location;
	private NodeState state;

	public static final String PROPERTIES = "Properties";
	public static final String PARENT = "Parent";
	public static final String OWNER = "Owner";
	public static final String ANCESTORS = "Ancestors";
	public static final String PROTOTYPES = "Prototypes";
	public static final String PARTNER_CONTEXT = "PartnerContext";
	public static final String NODELIST = "NodeList";
	public static final String CLIENTNODELIST = "ClientNodeList";
	public static final String CLIENTTASKLIST = "ClientTaskList";
	public static final String ATTRIBUTELIST = "AttributeList";
	public static final String PERMISSIONLIST = "PermissionList";
	public static final String DATA = "Data";
	public static final String FLAGS = "Flags";
	public static final String NOTES = "Notes";
	public static final String LINKS_COUNTER = "Links";
	public static final String LINKS_FROM_TASKS_COUNTER = "LinksFromTasksCounter";
	public static final String ATTACHMENTS_COUNTER = "AttachmentsCounter";
	public static final String LINKED_TASKS = "LinkedTasks";
	public static final String LOCATION = "Location";
	public static final String SCHEMA = "Schema";
	public static final String NODE_LINK = "NodeLink";
	public static final String REFERENCE_LINK = "ReferenceLink";
	public static final String SUPERDATA = "SuperData";
	public static final String STATE = "State";

	public Node() {
		super();
		this.idParent = null;
		this.idPrototype = null;
		this.type = null;
		this.parentNode = null;
		this.ancestors = null;
		this.ancestorsIds = new ArrayList<String>();
		this.ownedPrototypes = new NodeList();
		this.sharedPrototypes = new NodeList();
		this.idOwner = null;
		this.order = Strings.UNDEFINED_INT;
		this.content = Strings.EMPTY;
		this.data = null;
		this.flags = new LinkedHashMap<String, String>();
		this.notes = new LinkedHashMap<String, String>();
		this.isValid = false;
		this.nodeLink = null;
		this.references = new HashMap<String, Reference>();
		this.nodeList = new NodeList();
		this.clientNodeList = new NodeList();
		this.attributeList = new AttributeList();
		this.permissionList = new PermissionList();
		this.grantedUsers = new HashSet<String>();
		this.nodeLabels = new MonetHashMap<String>();
		this.linksCounter = 0;
		this.referenceLink = null;
		this.schema = "";
		this.superData = new HashMap<String, SuperDataItem>();
		this.state = null;
	}

	private AttributeList getAttributeList(String path) {
		AttributeList attributeList = null;
		Attribute attribute;
		String[] pathArray;
		int iPos = 0;

		pathArray = LibraryPath.splitAttributePath(path);
		attributeList = this.getAttributeList();
		while (iPos < pathArray.length - 1) {
			attribute = (Attribute) attributeList.get(pathArray[iPos]);
			if (attribute == null)
				return null;
			attributeList = attribute.getAttributeList();
			iPos++;
		}

		return attributeList;
	}

	public void setId(String id) {
		this.id = id;
		this.nodeList.setIdNode(id);
		this.clientNodeList.setIdNode(id);
	}

	public String getCode() {
		if (this.code == null)
			this.onLoad(this, Node.PROPERTIES);
		return this.code;
	}

	public DefinitionType getType() {
		if (this.type == null)
			return this.getDefinition().getType();
		return this.type;
	}

	public void setType(DefinitionType type) {
		this.type = type;
	}

	public String getParentId() {
		return this.idParent;
	}

	public void setParentId(String idParent) {
		this.idParent = idParent;
		this.removeLoadedAttribute(Node.PARENT);
	}

	public String getPartnerContext() {
		this.removeLoadedAttribute(Node.PARTNER_CONTEXT);
		onLoad(this, Node.PARTNER_CONTEXT);
		return this.partnerContext;
	}

	public void setPartnerContext(String businessUnitContext) {
		this.partnerContext = businessUnitContext;
		this.addLoadedAttribute(Node.PARTNER_CONTEXT);
	}

	public Node getMainNode() {
		Node result = this;

		while ((result != null) && (result.isComponent()))
			result = result.getParent();
		if (result == null)
			result = this;

		return result;
	}

	public NodeList getAncestors() {
		this.removeLoadedAttribute(Node.ANCESTORS);
		onLoad(this, Node.ANCESTORS);
		return this.ancestors;
	}

	public String[] getAncestorsIds() {
		this.removeLoadedAttribute(Node.ANCESTORS);
		onLoad(this, Node.ANCESTORS);
		return this.ancestorsIds.toArray(new String[0]);
	}

	public String getAncestorsIds(String separator) {
		String[] ancestorsIds = this.getAncestorsIds();
		return LibraryArray.implode(ancestorsIds, Strings.COMMA);
	}

	public void setAncestors(NodeList ancestors) {
		this.ancestors = ancestors;
		this.ancestorsIds.clear();
		for (Node ancestor : this.ancestors.get().values()) {
			this.ancestorsIds.add(ancestor.getId());
		}
		this.addLoadedAttribute(Node.ANCESTORS);
	}

	public Node getParent() {
		if (this.idParent == null)
			return null;

		onLoad(this, Node.PARENT);

		if (this.parentNode == null)
			return null;

		if (!this.parentNode.getId().equals(this.idParent)) {
			this.removeLoadedAttribute(Node.PARENT);
			onLoad(this, Node.PARENT);
		}

		return this.parentNode;
	}

	public void setParent(Node parent) {
		this.parentNode = parent;
		this.setParentId(parent != null ? parent.getId() : null);
		this.addLoadedAttribute(Node.PARENT);
	}

	public NodeList getOwnedPrototypes() {
		this.removeLoadedAttribute(Node.PROTOTYPES);
		onLoad(this, Node.PROTOTYPES);
		return this.ownedPrototypes;
	}

	public void setOwnedPrototypes(NodeList prototypes) {
		this.ownedPrototypes = prototypes;
	}

	public NodeList getSharedPrototypes() {
		this.removeLoadedAttribute(Node.PROTOTYPES);
		onLoad(this, Node.PROTOTYPES);
		return this.sharedPrototypes;
	}

	public void setSharedPrototypes(NodeList prototypes) {
		this.sharedPrototypes = prototypes;
	}

	public NodeList getPrototypes() {
		this.removeLoadedAttribute(Node.PROTOTYPES);
		onLoad(this, Node.PROTOTYPES);

		NodeList result = new NodeList();
		for (Node node : this.ownedPrototypes)
			result.add(node);

		for (Node node : this.sharedPrototypes)
			result.add(node);

		return result;
	}

	public User getOwner() {
		if (this.idOwner == null || this.idOwner.equals("-1"))
			return null;

		onLoad(this, Node.OWNER);

		if (!this.owner.getId().equals(this.idOwner)) {
			this.removeLoadedAttribute(Node.OWNER);
			onLoad(this, Node.OWNER);
		}

		return this.owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
		this.setOwnerId((owner != null && !owner.getId().equals("-1")) ? owner.getId() : null);
		this.addLoadedAttribute(Node.OWNER);
	}

	public String getOwnerId() {
		if (this.idOwner != null && this.idOwner.equals("-1")) return null;
		return this.idOwner;
	}

	public void setOwnerId(String id) {
		this.idOwner = id;
	}

	public int getOrder() {
		return this.order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public void setState(NodeState newState) {
		this.state = newState;
		//TODO: Update timestamp of the node?
	}

	public NodeState getState() {
		if (this.state == null) {
			this.removeLoadedAttribute(Node.STATE);
			this.onLoad(this, Node.STATE);
		}
		return this.state;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getData() {
		onLoad(this, Node.DATA);
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
		this.addLoadedAttribute(Node.DATA);
	}

	public Map<String, String> getFlags() {
		onLoad(this, Node.FLAGS);
		return this.flags;
	}

	public String getFlag(String name) {
		onLoad(this, Node.FLAGS);
		return this.flags.get(name);
	}

	public void addFlag(String name, String value) {
		onLoad(this, Node.FLAGS);
		this.flags.put(name, value);
	}

	public void deleteFlag(String name) {
		onLoad(this, Node.FLAGS);
		this.flags.remove(name);
	}

	public void setFlags(Map<String, String> flags) {
		this.flags = flags;
		this.addLoadedAttribute(Node.FLAGS);
	}

	public Map<String, String> getNotes() {
		onLoad(this, Node.NOTES);
		return this.notes;
	}

	public String getNote(String name) {
		onLoad(this, Node.NOTES);
		return this.notes.get(name);
	}

	public void addNote(String name, String value) {
		onLoad(this, Node.NOTES);
		this.notes.put(name, value);
	}

	public void deleteNote(String name) {
		onLoad(this, Node.NOTES);
		this.notes.remove(name);
	}

	public void setNotes(Map<String, String> notes) {
		this.notes = notes;
		this.addLoadedAttribute(Node.NOTES);
	}

	public Map<String, Integer> getGroupOptionsCount(String codeAttribute, List<String> filterNodes, List<FilterProperty> filterAttributesDefinition) {
		String ownerId = null;

		if ((filterNodes != null) && (filterNodes.size() == 1) && (filterNodes.get(0).equals("")))
			filterNodes = null;

		if (this.isCollection())
			ownerId = this.id;

		return this.getNodeLink().loadReferenceAttributeValuesCount(ownerId, this.getGroupOptionsReferenceCode(), codeAttribute, filterNodes, filterAttributesDefinition);
	}

	public List<String> getGroupOptions(String codeAttribute, List<String> filterNodes, List<FilterProperty> filterAttributesDefinition) {
		Dictionary dictionary = Dictionary.getInstance();
		NodeDefinition definition = this.getDefinition();
		String ownerId = null;

		if ((filterNodes != null) && (filterNodes.size() == 1) && (filterNodes.get(0).equals("")))
			filterNodes = null;

		if (this.isCollection())
			ownerId = this.id;

		return this.getNodeLink().loadReferenceAttributeValues(ownerId, this.getGroupOptionsReferenceCode(), codeAttribute, filterNodes, filterAttributesDefinition);
	}

	public void setNodeLink(NodeLink nodeLink) {
		this.nodeLink = nodeLink;
		this.addLoadedAttribute(Node.NODE_LINK);
	}

	public void addReference(Reference reference) {
		this.references.put(reference.getCode(), reference);
	}

	public ReferenceLink getReferenceLink() {
		onLoad(this, Node.REFERENCE_LINK);
		return this.referenceLink;
	}

	public void setReferenceLink(ReferenceLink referenceLink) {
		this.referenceLink = referenceLink;
	}

	public String getLabel() {
		return this.getReference().getLabel();
	}

	public String getInstanceLabel() {
		return this.getReference().getLabel();
	}

	public String getShortLabel(Integer length) {
		return this.getReference().getShortLabel(length);
	}

	public String getShortLabel() {
		return this.getReference().getShortLabel();
	}

	public void setLabel(String label) {
		this.getReference().setLabel(label);
	}

	public String getDescription() {
		return this.getReference().getDescription();
	}

	public String getShortDescription(Integer length) {
		return this.getReference().getShortDescription(length);
	}

	public String getShortDescription() {
		return this.getReference().getShortDescription();
	}

	public void setDescription(String description) {
		this.getReference().setDescription(description);
	}

	public String getColor() {
		return this.getReference().getColor();
	}

	public void setColor(String color) {
		this.getReference().setColor(color);
	}

	public String getCreateDate() {
		return this.getReference().getCreateDate().getFormattedValue();
	}

	public String getUpdateDate() {
		return this.getReference().getUpdateDate().getFormattedValue();
	}

	public String getDeleteDate() {
		return this.getReference().getDeleteDate().getFormattedValue();
	}

	public boolean isHighlighted() {
		return this.getReference().isHighlighted();
	}

	public boolean isPrototype() {
		return this.getReference().isPrototype();
	}

	public String getPrototypeId() {
		onLoad(this, Node.PROPERTIES);
		return this.idPrototype;
	}

	public void setPrototypeId(String idPrototype) {
		this.idPrototype = idPrototype;
	}

	public boolean isPrototyped() {
		return this.getPrototypeId() != null && !this.getPrototypeId().equals(Strings.UNDEFINED_ID);
	}

	public boolean isGeoReferenced() {
		return this.getReference().isGeoReferenced();
	}

	public boolean isObsolete(long timestamp) {
		long currentTimestamp = getReference().getUpdateDate().getValue().getTime();

		if (this.isPrototyped()) {
			Node prototype = this.nodeLink.loadNode(this.getPrototypeId());
			currentTimestamp = prototype.getReference().getUpdateDate().getValue().getTime();
		}

		return currentTimestamp != timestamp;
	}

	public Object getReferenceAttributeValue(String code) {
		return this.getReference().getAttributeValue(code);
	}

	public Object getReferenceAttributeValue(String codeReference, String code) {
		return this.getReference(codeReference).getAttributeValue(code);
	}

	public HashMap<String, Reference> getReferences() {
		return this.references;
	}

	public Reference getReference() {
		ReferenceLink link;
		Reference reference;

		if (this.references.containsKey(DescriptorDefinition.CODE))
			return this.references.get(DescriptorDefinition.CODE);

		link = this.getReferenceLink();
		reference = link.loadNodeReference(this);

		this.references.put(DescriptorDefinition.CODE, reference);

		return reference;
	}

	public Reference getReference(String code) {
		ReferenceLink link;
		Reference reference;

		if (code == null)
			return this.getReference();
		if (this.references.containsKey(code))
			return this.references.get(code);

		link = this.getReferenceLink();
		if (link == null) {
			return new Reference(code);
		}

		reference = link.loadNodeReference(this, code);

		this.references.put(code, reference);

		return reference;
	}

	public void setReferences(HashMap<String, Reference> references) {
		Iterator<String> iter = references.keySet().iterator();

		this.references.clear();
		while (iter.hasNext()) {
			Reference reference = new Reference(references.get(iter.next()));
			this.references.put(reference.getCode(), reference);
		}
	}

	public void setReference(Reference reference) {
		this.references.put(reference.getCode(), reference);
	}

	public NodeList getNodeList() {
		onLoad(this, Node.NODELIST);
		return this.nodeList;
	}

	public Map<String, Node> getNodeListItems(String[] codeNodes, int start, int limit) {
		NodeLink nodeLink = this.getNodeLink();
		NodeDataRequest dataRequest = new NodeDataRequest();
		if ((codeNodes != null) && (codeNodes.length == 1) && (codeNodes[0].equals("")))
			codeNodes = null;
		dataRequest.setCodeNodes(codeNodes);
		dataRequest.setStartPos(start);
		dataRequest.setLimit(limit);
		return (nodeLink != null) ? nodeLink.requestNodeListItems(this.getId(), dataRequest) : new LinkedHashMap<String, Node>();
	}

	public Map<String, Node> getNodeListItems(String codeReference, String[] codeNodes, int start, int limit) {
		NodeLink nodeLink = this.getNodeLink();
		NodeDataRequest dataRequest = new NodeDataRequest();
		if ((codeNodes != null) && (codeNodes.length == 1) && (codeNodes[0].equals("")))
			codeNodes = null;
		dataRequest.setCodeReference(codeReference);
		dataRequest.setCodeNodes(codeNodes);
		dataRequest.setStartPos(start);
		dataRequest.setLimit(limit);
		return (nodeLink != null) ? nodeLink.requestNodeListItems(this.getId(), dataRequest) : new LinkedHashMap<String, Node>();
	}

	public int getNodeListItemsCount(String codeReference, String[] codeNodes) {
		NodeLink nodeLink = this.getNodeLink();
		NodeDataRequest dataRequest = new NodeDataRequest();
		if ((codeNodes != null) && (codeNodes.length == 1) && (codeNodes[0].equals("")))
			codeNodes = null;
		dataRequest.setCodeReference(codeReference);
		dataRequest.setCodeNodes(codeNodes);
		return (nodeLink != null) ? nodeLink.requestNodeListItemsCount(this.getId(), dataRequest) : 0;
	}

	public int getNodeListItemsCount(String[] codeNodes) {
		NodeLink nodeLink = this.getNodeLink();
		NodeDataRequest dataRequest = new NodeDataRequest();
		if ((codeNodes != null) && (codeNodes.length == 1) && (codeNodes[0].equals("")))
			codeNodes = null;
		dataRequest.setCodeNodes(codeNodes);
		return (nodeLink != null) ? nodeLink.requestNodeListItemsCount(this.getId(), dataRequest) : 0;
	}

	public void setNodeList(NodeList nodeList) {
		this.nodeList = nodeList;
		this.addLoadedAttribute(Node.NODELIST);
	}

	public NodeList getClientNodeList() {
		onLoad(this, Node.CLIENTNODELIST);
		return this.clientNodeList;
	}

	public Boolean setClientNodeList(NodeList clientNodeList) {
		this.clientNodeList = clientNodeList;
		this.addLoadedAttribute(Node.CLIENTNODELIST);
		return true;
	}

	public TaskList getClientTaskList() {
		onLoad(this, Node.CLIENTTASKLIST);
		return this.clientTaskList;
	}

	public Boolean setClientTaskList(TaskList clientTaskList) {
		this.clientTaskList = clientTaskList;
		this.addLoadedAttribute(Node.CLIENTTASKLIST);
		return true;
	}

	public MonetHashMap<String> getNodeLabels() {
		return this.nodeLabels;
	}

	public AttributeList getAttributeList() {
		onLoad(this, Node.ATTRIBUTELIST);
		return this.attributeList;
	}

	public void setAttributeList(AttributeList attributeList) {
		this.attributeList = attributeList;
		this.addLoadedAttribute(Node.ATTRIBUTELIST);
	}

	public LinkedHashMap<String, Attribute> getAttributes(String path) {
		Attribute attribute = this.getAttribute(path);
		MonetHashMap<Attribute> attributes;
		LinkedHashMap<String, Attribute> result = new LinkedHashMap<String, Attribute>();
		Iterator<String> iter;

		if (attribute == null)
			return result;

		attributes = attribute.getAttributeList().get();
		iter = attributes.keySet().iterator();

		while (iter.hasNext()) {
			String code = iter.next();
			result.put(code, (Attribute) attributes.get(code));
		}

		return result;
	}

	public Attribute getFirstAttribute(String path) {
		LinkedHashMap<String, Attribute> attributes = this.getAttributes(path);
		Attribute result = new Attribute();
		Iterator<Attribute> iter;

		iter = attributes.values().iterator();
		if (!iter.hasNext())
			return result;

		return iter.next();
	}

	public Attribute getAttribute(String path) {
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

	public String getFieldValue(String fieldName) {
		String fromParameter;
		FieldProperty fieldDefinition = ((FormDefinition) this.getDefinition()).getField(fieldName);

		Attribute fieldValue = this.getAttribute(fieldDefinition.getCode());
		if (fieldValue == null)
			return "";

		if (fieldDefinition.isLink()) {
			fromParameter = fieldValue.getIndicatorValue(Indicator.CODE);
		} else if (fieldDefinition.isSelect()) {
			Attribute optionValue = fieldValue.getAttribute(Attribute.OPTION);
			if (optionValue == null)
				optionValue = fieldValue;
			fromParameter = optionValue.getIndicatorValue(Indicator.CODE);
		} else {
			fromParameter = fieldValue.getIndicatorValue(Indicator.VALUE);
		}
		return fromParameter;
	}

	public String getIndicatorValue(String path) {
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

	public void setIndicatorValue(String path, String data) {
		Attribute attribute;
		Indicator indicator;
		String attributePath, codeIndicator;

		if (path.indexOf(LibraryPath.SEPARATOR) == -1)
			return;

		attributePath = LibraryPath.getAttributePath(path);
		codeIndicator = LibraryPath.getIndicatorCode(path);

		attribute = (Attribute) this.getAttribute(attributePath);
		indicator = (Indicator) attribute.getIndicatorList().get(codeIndicator);

		indicator.setData(data);
	}

	public boolean isAttributeEqualsTo(String codeAttribute, String value) {
		Attribute attribute = this.getAttribute(codeAttribute);

		if (attribute == null)
			return false;

		for (Indicator indicator : attribute.getIndicatorList().get().values()) {
			if (indicator.getData().equals(value))
				return true;
		}

		return false;
	}

	public boolean isPublic() {

		boolean isBack = BusinessUnit.getInstance().getBusinessModel().getProject().getType().equals(TypeEnumeration.BACK);
		if (isBack)
			return true;

		if (this.idOwner == null)
			return true;

		return this.idOwner.equals("-1");
	}

	public boolean isLinked() {
		onLoad(this, Node.LINKS_COUNTER);
		this.removeLoadedAttribute(Node.LINKS_COUNTER);

		return (this.linksCounter > 0);
	}

	public void setLinksCounter(int linksCounter) {
		this.linksCounter = linksCounter;
	}

	public boolean hasLinksFromTasks() {
		onLoad(this, Node.LINKS_FROM_TASKS_COUNTER);
		this.removeLoadedAttribute(Node.LINKS_FROM_TASKS_COUNTER);

		return (this.linksFromTasksCounter > 0);
	}

	public void setLinksFromTasksCounter(int linksFromTasksCounter) {
		this.linksFromTasksCounter = linksFromTasksCounter;
	}

	public TaskList getLinkedTasks() {
		onLoad(this, Node.LINKED_TASKS);
		this.removeLoadedAttribute(Node.LINKED_TASKS);

		return this.linkedTasks;
	}

	public void setLinkedTasks(TaskList linkedTasks) {
		this.linkedTasks = linkedTasks;
	}

	public boolean hasAttachments() {
		onLoad(this, Node.ATTACHMENTS_COUNTER);
		this.removeLoadedAttribute(Node.ATTACHMENTS_COUNTER);

		return (this.attachmentsCounter > 0);
	}

	public void setAttachmentsCounter(int attachmentsCounter) {
		this.attachmentsCounter = attachmentsCounter;
	}

	public boolean isLocked() {
		return !(this.isEditable() && this.isDeletable());
	}

	public boolean isEditable() {
		return this.getReference().isEditable();
	}

	public boolean isDeletable() {
		return this.getReference().isDeletable();
	}

	public Location getLocation() {
		this.onLoad(this, Node.LOCATION);
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
		this.addLoadedAttribute(Node.LOCATION);
	}

	public boolean isValid() {
		return this.isValid;
	}

	public void setIsValid(Boolean value) {
		this.isValid = value;
	}

	public PermissionList getPermissionList() {
		onLoad(this, Node.PERMISSIONLIST);
		this.removeLoadedAttribute(Node.PERMISSIONLIST);
		return this.permissionList;
	}

	public void setPermissionList(PermissionList permissionList) {
		this.permissionList = permissionList;
	}

	public HashSet<String> getGrantedUsers() {
		return this.grantedUsers;
	}

	public void setGrantedUsers(HashSet<String> grantedUsers) {
		this.grantedUsers = grantedUsers;
	}

	public HashSet<String> getRoles() {
		HashSet<String> result = new HashSet<String>();
		result.add(UserRole.HOLDER);
		return result;
	}

	public HashSet<String> getRules() {
		return new HashSet<String>();
	}

	public T getDefinition() {
		return (T)Dictionary.getInstance().getNodeDefinition(this.getCode());
	}

	public boolean isSingleton() {
		NodeDefinition nodeDefinition = this.getDefinition();
		return nodeDefinition.isSingleton();
	}

	public boolean isComponent() {
		NodeDefinition nodeDefinition = this.getDefinition();
		return nodeDefinition.isComponent();
	}

	public boolean isContainer() {
		return this.getDefinition() instanceof ContainerDefinition;
	}

	public boolean isDesktop() {
		return this.getDefinition() instanceof DesktopDefinition;
	}

	public boolean isCollection() {
		return this.getDefinition() instanceof CollectionDefinition;
	}

	public boolean isSet() {
		return this.isCollection() || this.isCatalog();
	}

	public boolean isForm() {
		return this.getDefinition() instanceof FormDefinition;
	}

	public boolean isDocument() {
		return this.getDefinition() instanceof DocumentDefinition;
	}

	public boolean isCatalog() {
		return this.getDefinition() instanceof CatalogDefinition;
	}

	public Node clone() {
		Node node = new Node();

		node.code = this.getCode();
		node.type = this.getType();
		node.idOwner = this.getOwnerId();
		node.owner = this.getOwner();
		node.parentNode = this.getParent();
		node.idParent = this.getParentId();
		node.order = this.getOrder();
		node.content = this.getContent();
		node.data = this.getData();
		node.isValid = this.isValid();
		node.schema = this.getSchema();
		node.referenceLink = this.getReferenceLink();
		node.cloneReferences(this);
		node.cloneSuperData(this);
		node.nodeList = new NodeList(this.getNodeList());
		node.attributeList = new AttributeList(this.getAttributeList());
		node.location = this.getLocation();
		node.linkLoadListener(this.getLoadListener());
		node.nodeLink = this.getNodeLink();
		node.setLoadedAttributes(this.getLoadedAttributes());

		return node;
	}

	public void clone(Node node) {
		this.code = node.getCode();
		this.type = node.getType();
		this.idOwner = node.getOwnerId();
		this.owner = node.getOwner();
		this.parentNode = node.getParent();
		this.idParent = node.getParentId();
		this.order = node.getOrder();
		this.content = node.getContent();
		this.data = node.getData();
		this.isValid = node.isValid();
		this.schema = node.getSchema();
		this.referenceLink = node.getReferenceLink();
		this.cloneReferences(node);
		this.cloneSuperData(node);
		this.nodeList = new NodeList(node.getNodeList());
		this.attributeList = new AttributeList(node.getAttributeList());
		this.location = node.getLocation();
		this.linkLoadListener(node.getLoadListener());
		this.nodeLink = node.getNodeLink();
		this.setLoadedAttributes(node.getLoadedAttributes());
	}

	private void cloneReferences(Node<T> node) {
		this.references.clear();

		for (Reference reference : node.getReferences().values()) {
			Reference newReference = reference.clone();
			this.references.put(newReference.getCode(), newReference);
		}
	}

	private void cloneSuperData(Node<T> node) {
		this.superData.clear();

		for (Map.Entry<String, SuperDataItem> entry : node.getSuperData().entrySet()) {
			SuperDataItem superDataItem = entry.getValue();
			SuperDataItem newSuperDataItem = new SuperDataItem(superDataItem);
			this.superData.put(entry.getKey(), newSuperDataItem);
		}
	}

	public void merge(Node node) {
		this.merge(node, false);
	}

	public void merge(Node node, boolean onlyNotEmpty) {
		this.getAttributeList().merge(node.getAttributeList(), onlyNotEmpty);
		this.isValid = node.isValid();
	}

	public String getSchema() {
		onLoad(this, Node.SCHEMA);
		return this.schema;
	}

	public String getCodeSuperFieldWithDependencyOnSchemaAttribute(String codeReference, String codeAttribute) {
		FormDefinition definition;
		FieldProperty fieldDefinition;
		AttributeProperty attributeDeclaration;
		IndexDefinition referenceDefinition;
		SchemaDefinition schemaDefinition;
		String code;

		if (!this.isForm())
			return null;

		definition = (FormDefinition) this.getDefinition();
		if (!definition.hasSuperFieldProperties())
			return null;

		schemaDefinition = ((org.monet.metamodel.interfaces.HasSchema) definition).getSchemaDefinition();
		referenceDefinition = BusinessUnit.getInstance().getBusinessModel().getDictionary().getIndexDefinition(codeReference);
		attributeDeclaration = referenceDefinition.getAttribute(codeAttribute);

		if (attributeDeclaration == null)
			return null;
		code = schemaDefinition.getAttributesFieldsMap().get(attributeDeclaration.getCode());
		if (code == null)
			code = schemaDefinition.getAttributesFieldsMap().get(attributeDeclaration.getName());
		if (code == null)
			return null;

		/* disabled isSuperField
		fieldDefinition = definition.getField(code);
		if (fieldDefinition.isSuperfield())
			return fieldDefinition.getCode();
		*/

		return null;
	}

	public void setSchema(String schema) {
		this.schema = schema;
		this.dirtyMap.put(Node.SCHEMA, true);
		this.addLoadedAttribute(Node.SCHEMA);
	}

	public Map<String, SuperDataItem> getSuperData() {
		onLoad(this, Node.SUPERDATA);
		return this.superData;
	}

	public SuperDataItem getSuperDataItem(String id) {
		onLoad(this, Node.SUPERDATA);
		if (!this.superData.containsKey(id))
			return null;
		return this.superData.get(id);
	}

	public String getSuperDataCode(String id) {
		onLoad(this, Node.SUPERDATA);
		if (!this.superData.containsKey(id))
			return "";
		return this.superData.get(id).getCode();
	}

	public String getSuperDataValue(String id) {
		onLoad(this, Node.SUPERDATA);
		if (!this.superData.containsKey(id))
			return "";
		return this.superData.get(id).getValue();
	}

	public String getSuperDataIndicators(String id) {
		onLoad(this, Node.SUPERDATA);
		if (!this.superData.containsKey(id))
			return "";
		return this.superData.get(id).getData();
	}

	public void setSuperData(HashMap<String, SuperDataItem> superData) {
		this.superData = superData;
	}

	public boolean hasRevisions() {
		if (this.isCollection())
			return false;
		return this.nodeLink.requestNodeRevisionItemsCount(this.id, new DataRequest()) > 0;
	}

	public List<String> getNodesToListenForChanges() {
		ArrayList<String> ids = new ArrayList<String>();
		NodeDefinition definition = this.getDefinition();

		for (RuleProperty rule : definition.getRuleNodeList())
			extractListenersFromRuleProperty(rule, ids);

		for (RuleProperty rule : definition.getRuleOperationList())
			extractListenersFromRuleProperty(rule, ids);

		for (RuleProperty rule : definition.getRuleViewList())
			extractListenersFromRuleProperty(rule, ids);

		if (definition.isForm()) {
			FormDefinition formDefinition = (FormDefinition) definition;
			if (formDefinition.getRuleFormList().size() > 0)
				ids.add(this.id);
		}

		return ids;
	}

	private void extractListenersFromRuleProperty(RuleProperty rule, ArrayList<String> ids) {
		if (!ids.contains(id))
			ids.add(id);

		ListenProperty listen = rule.getListen();
		if (listen != null) {
			if (listen.getParent() != null && this.idParent != null)
				ids.add(this.idParent);

			Dictionary dictionary = Dictionary.getInstance();

			if (this.isForm()) {
				FormDefinition formDefinition = (FormDefinition) this.getDefinition();
				for (Ref linked : listen.getLink()) {
					String linkedCode = formDefinition.getField(linked.getValue()).getCode();
					ids.add(this.getAttribute(linkedCode).getIndicatorValue(Indicator.VALUE));
				}
			}

			Node parent = this.getParent();
			if (parent != null && parent.isContainer()) {
				for (Ref sibling : listen.getSibling()) {
					String childCode = dictionary.getDefinitionCode(sibling.getValue());
					ids.add(parent.getAttribute(childCode).getIndicatorValue(Indicator.VALUE));
				}
			}

			if (this.isContainer()) {
				for (Ref child : listen.getChildren()) {
					String childCode = dictionary.getDefinitionCode(child.getValue());
					ids.add(this.getAttribute(childCode).getIndicatorValue(Indicator.VALUE));
				}
			}
		}
	}

	public boolean requirePartnerContext() {
		NodeDefinition definition = this.getDefinition();

		if (definition.requirePartnerContext())
			return true;

		if (this.getParent() != null)
			return this.getParent().requirePartnerContext();

		return false;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "node");

		serializer.attribute("", "id", this.id);
		serializer.attribute("", "code", this.code);
		serializer.attribute("", "order", String.valueOf(this.order));
		if (this.idParent != null) serializer.attribute("", "idParent", this.idParent);
		if (this.idPrototype != null) serializer.attribute("", "idPrototype", this.idPrototype);
		serializer.attribute("", "isLocked", String.valueOf(this.isLocked()));
		serializer.attribute("", "isEditable", String.valueOf(this.isEditable()));
		serializer.attribute("", "isDeleteable", String.valueOf(this.isEditable()));
		if (this.partnerContext != null) serializer.attribute("", "partnerContext", this.partnerContext);

		User owner = this.getOwner();
		if (owner != null) {
			serializer.attribute("", "idOwner", this.getOwnerId());
			serializer.attribute("", "owner", this.getOwner().getName());
		}

		Reference reference = this.getReference();
		if (reference != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");
			serializer.attribute("", "createDate", dateFormat.format(reference.getCreateDate().getValue()));
			serializer.attribute("", "updateDate", dateFormat.format(reference.getUpdateDate().getValue()));
			if (reference.getDeleteDate() != null) serializer.attribute("", "deleteDate", dateFormat.format(reference.getDeleteDate().getValue()));
			serializer.attribute("", "isHighlighted", String.valueOf(reference.isHighlighted()));
			serializer.attribute("", "isPrototype", String.valueOf(reference.isPrototype()));

			String label = reference.getLabel();
			if (label != null) {
				serializer.startTag("", "label");
				serializer.text(label);
				serializer.endTag("", "label");
			}

			String description = reference.getDescription();
			if (description != null) {
				serializer.startTag("", "description");
				serializer.text(description);
				serializer.endTag("", "description");
			}
		}

		if (depth > 0)
			this.getNodeList().serializeToXML(serializer, depth-1);

		this.getAttributeList().serializeToXML(serializer, depth);

		serializer.endTag("", "node");
	}

	private void setLoadListenerToChildren(Node node) {
		for (Node currentNode : node.getNodeList().get().values()) {
			this.setLoadListenerToChildren(currentNode);
		}
		node.linkLoadListener(this.getLoadListener());
	}

	public void deserializeFromXML(String content, ModelChangesResolver resolver) {
		SAXBuilder builder = new SAXBuilder();
		StringReader reader;
		org.jdom.Document document, dummyDocument;
		Element node;

		this.nodeList.clear();
		this.attributeList.clear();

		if (content.equals(Strings.EMPTY))
			return;
		while (!content.substring(content.length() - 1).equals(">"))
			content = content.substring(0, content.length() - 1);

		reader = new StringReader(content);

		try {
			document = builder.build(reader);
			node = document.getRootElement();
			this.unserializeFromXML(node, resolver);
		} catch (JDOMException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_DEFINITION_FROM_XML, content, exception);
		} catch (IOException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_DEFINITION_FROM_XML, content, exception);
		}
	}

	public void unserializeFromXML(Element node, ModelChangesResolver resolver) {
		if (node.getAttribute("id") != null)
			this.id = node.getAttributeValue("id");
		if (node.getAttribute("idParent") != null)
			this.idParent = node.getAttributeValue("idParent");
		if (node.getAttribute("idOwner") != null)
			this.idOwner = node.getAttributeValue("idOwner");
		if (node.getAttribute("order") != null)
			this.order = new Integer(node.getAttributeValue("order"));
		if (node.getAttribute("idPrototype") != null)
			this.idPrototype = node.getAttributeValue("idPrototype");

		if (node.getAttribute("code") != null && !node.getAttributeValue("code").isEmpty() && !node.getAttributeValue("code").equals("-1")) {
			this.code = node.getAttributeValue("code");
			this.setCode(this.code);
		}

		Reference reference = this.getReference();
		if (reference != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

			try {
				if (node.getAttribute("createDate") != null)
					reference.setCreateDate(new org.monet.bpi.types.Date(dateFormat.parse(node.getAttributeValue("createDate"))));
				if (node.getAttribute("updateDate") != null)
					reference.setUpdateDate(new org.monet.bpi.types.Date(dateFormat.parse(node.getAttributeValue("updateDate"))));
				if (node.getAttribute("deleteDate") != null)
					reference.setDeleteDate(new org.monet.bpi.types.Date(dateFormat.parse(node.getAttributeValue("deleteDate"))));
				if (node.getAttribute("isHighlighted") != null)
					reference.setHighlighted(Boolean.valueOf(node.getAttributeValue("isHighlighted")));
				if (node.getAttribute("isPrototype") != null)
					reference.setPrototype(Boolean.valueOf(node.getAttributeValue("isPrototype")));
			} catch (ParseException e) {
			}

			if (node.getChild("label") != null)
				reference.setLabel(node.getChild("label").getText());
			if (node.getChild("description") != null)
				reference.setDescription(node.getChild("description").getText());
		}

		this.nodeList.clear();
		this.nodeList.unserializeFromXML(node.getChild("nodelist"));

		for (Node child : this.nodeList.get().values()) {
			this.setLoadListenerToChildren(child);
		}

		this.attributeList.clear();
		this.attributeList.deserializeFromXML(node.getChild("attributelist"), resolver);

		if (node.getChild("attributelist") != null) {
			org.jdom.Document dummyDocument = new org.jdom.Document((Element) node.getChild("attributelist").clone());
			content = LibraryXML.clearHeader(LibraryXML.outputString(dummyDocument));
			this.setData(content);
		} else
			this.setData(Strings.EMPTY);

		this.addLoadedAttribute(Node.NODELIST);
		this.addLoadedAttribute(Node.ATTRIBUTELIST);
	}

	public JSONObject toJson() {
		String content = this.getContent();
		Boolean isPartialLoading = this.isPartialLoading();
		JSONObject result = new JSONObject(), nodeList = new JSONObject();

		if (isPartialLoading)
			this.disablePartialLoading();

		nodeList.put("count", this.getNodeList().getCount());

		result.put("id", this.getId());
		result.put("idparent", this.getParentId());
		result.put("code", this.getCode());
		result.put("isPrototype", this.isPrototype());
		result.put("content", content);
		result.put("reference", this.getReference().toJson());
		result.put("nodelist", nodeList);

		if (isPartialLoading)
			this.enablePartialLoading();

		return result;
	}

	public void fromJson(String data) {
		this.addLoadedAttribute(Node.ATTRIBUTELIST);
		this.attributeList.fromJson(data);
	}

	public Attribute createAttribute(AttributeList attributeList, String code) {
		Attribute attribute = attributeList.get(code);

		if (attribute == null) {
			attribute = new Attribute();
			attribute.setCode(code);
			attributeList.add(attribute);
		}

		return attribute;
	}

	private String getGroupOptionsReferenceCode() {
		Dictionary dictionary = Dictionary.getInstance();
		NodeDefinition definition = this.getDefinition();
		String codeReference = null;

		if (this.isCollection())
			codeReference = dictionary.getDefinitionCode(((CollectionDefinition) definition).getIndex().getValue());
		else if (this.isCatalog())
			codeReference = dictionary.getDefinitionCode(((CatalogDefinition) definition).getIndex().getValue());

		return codeReference;
	}

	protected NodeLink getNodeLink() {
		onLoad(this, Node.NODE_LINK);
		return this.nodeLink;
	}

}
