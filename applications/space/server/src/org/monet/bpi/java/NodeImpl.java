package org.monet.bpi.java;

import org.monet.bpi.*;
import org.monet.bpi.MonetLink;
import org.monet.bpi.Node;
import org.monet.bpi.Task;
import org.monet.bpi.User;
import org.monet.bpi.exceptions.CantCallSaveOnSaveException;
import org.monet.bpi.types.Date;
import org.monet.bpi.types.Link;
import org.monet.bpi.types.Location;
import org.monet.bpi.types.location.GeometryFactory;
import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.DesktopDefinitionBase.RuleLinkProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.FormDefinitionBase.RuleFormProperty;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.NodeDefinitionBase.RuleOperationProperty;
import org.monet.metamodel.NodeDefinitionBase.RuleViewProperty;
import org.monet.metamodel.ViewProperty;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.PermissionType;
import org.monet.space.kernel.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class NodeImpl implements Node, BehaviorNode {

	org.monet.space.kernel.model.Node node;
	NodeDefinition definition;
	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();
	NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	boolean isCreating;
	boolean isSaving;
	private NodeState state;
	private NodeState mainState;

	public void injectNode(org.monet.space.kernel.model.Node node) {
		this.node = node;
		this.definition = node.getDefinition();
	}

	@Override
	public User getAuthor() {
		org.monet.space.kernel.model.User user = this.node.getOwner();

		if (user == null)
			return null;

		UserImpl author = new UserImpl();
		author.injectUser(user);

		return author;
	}

	@Override
	public String getOwnerId() {
		return this.node.getParentId();
	}

	@Override
	public void addPermission(String userId) {
		addPermission(userId, new Date());
	}

	@Override
	public void addPermission(User user) {
		addPermission(user.getId());
	}

	@Override
	public void addPermission(String userId, Date beginDate) {
		addPermission(userId, beginDate, null);
	}

	@Override
	public void addPermission(User user, Date beginDate) {
		addPermission(user.getId(), beginDate);
	}

	@Override
	public void addPermission(String userId, Date beginDate, Date endDate) {
		PermissionList permissionList = node.getPermissionList();

		Permission permission = new Permission();
		permission.setIdObject(node.getId());
		permission.setIdUser(userId);
		permission.setType(PermissionType.READ_WRITE_CREATE_DELETE);
		permission.setBeginDate(beginDate.getValue());
		permission.setExpireDate(endDate != null ? endDate.getValue() : null);
		permissionList.add(permission);

		nodeLayer.saveNodePermissions(node, permissionList);
	}

	@Override
	public void addPermission(User user, Date beginDate, Date endDate) {
		addPermission(user.getId(), beginDate, endDate);
	}

	@Override
	public boolean hasPermission(String userId) {
		PermissionList permissionList = node.getPermissionList();
		return permissionList.getByUser(userId).size() > 0;
	}

	@Override
	public boolean hasPermission(User user) {
		return hasPermission(user.getId());
	}

	@Override
	public void deletePermission(String userId) {
		PermissionList permissionList = node.getPermissionList();
		permissionList.deleteUserPermissions(userId);
		nodeLayer.saveNodePermissions(node, permissionList);
	}

	@Override
	public void deletePermission(User user) {
		deletePermission(user.getId());
	}

	@Override
	public Node getOwner() {

		String ownerId = this.node.getParentId();
		if (ownerId == null || ownerId.equals("-1"))
			return null;

		return nodeToBpi(this.nodeLayer.loadNode(ownerId));
	}

	@Override
	public void setOwner(Node owner) {
		NodeImpl ownerImpl = (NodeImpl) owner;
		this.nodeLayer.setParentNode(node, ownerImpl.node);
	}

	@Override
	public boolean isPrototype() {
		if (this.node.isPrototype())
			return true;
		return this.node.getParentId() == null;
	}

	public Node getPrototypeNode() {
		if (!this.node.isPrototyped())
			return null;

		return nodeToBpi(this.nodeLayer.loadNode(this.node.getPrototypeId()));
	}

	@Override
	public boolean isGeoReferenced() {
		return this.node.getReference().isGeoReferenced();
	}

	@Override
	public String getCode() {
		return this.node.getCode();
	}

	@Override
	public String getName() {
		return this.node.getDefinition().getName();
	}

	@Override
	public String getLabel() {
		return this.node.getLabel();
	}

	@Override
	public void setLabel(String label) {
		this.node.setLabel(label);
	}

	@Override
	public String getDescription() {
		return this.node.getDescription();
	}

	@Override
	public void setDescription(String description) {
		this.node.setDescription(description);
	}

	@Override
	public String getColor() {
		return this.node.getColor();
	}

	@Override
	public void setColor(String color) {
		this.node.setColor(color);
	}

	protected IndexEntry getIndexEntry(String name) {
		Reference reference = this.node.getReference(name);
		IndexEntryImpl bpiIndexEntry = this.bpiClassLocator.instantiateBehaviour(reference.getDefinition());
		bpiIndexEntry.injectIndexEntry(reference);
		bpiIndexEntry.injectNode(this.node);
		return bpiIndexEntry;
	}

	@Override
	public Map<String, String> getFlags() {
		return this.node.getFlags();
	}

	@Override
	public String getFlag(String name) {
		return this.node.getFlag(name);
	}

	@Override
	public void setFlag(String name, String value) {
		this.node.addFlag(name, value);
		this.nodeLayer.saveNodeFlags(this.node);
	}

	@Override
	public void removeFlag(String name) {
		this.node.deleteFlag(name);
		this.nodeLayer.saveNodeFlags(this.node);
	}

	@Override
	public String getNote(String name) {
		return this.node.getNote(name);
	}

	@Override
	public void setNote(String name, String value) {
		this.node.addNote(name, value);
		this.nodeLayer.saveNodeNotes(this.node);
	}

	@Override
	public void removeNote(String name) {
		this.node.deleteNote(name);
		this.nodeLayer.saveNodeNotes(this.node);
	}

	@Override
	public void save() {
		if (this.isSaving || this.isCreating)
			throw new CantCallSaveOnSaveException();
		this.nodeLayer.saveNode(this.node);
		AgentNotifier.getInstance().notify(new MonetEvent(MonetEvent.NODE_SAVED, null, this.node));
	}

	@Override
	public void lock() {
		this.nodeLayer.makeUneditable(this.node);
		this.nodeLayer.makeUndeletable(this.node);
	}

	@Override
	public void unLock() {
		this.nodeLayer.makeEditable(this.node);
		this.nodeLayer.makeDeletable(this.node);
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

		this.nodeLayer.saveNodePartnerContext(this.node, context);
	}

	@Override
	public void setEditable(boolean value) {
		if (value)
			this.nodeLayer.makeEditable(this.node);
		else
			this.nodeLayer.makeUneditable(this.node);
	}

	@Override
	public boolean isEditable() {
		return this.node.isEditable();
	}

	@Override
	public void setDeletable(boolean value) {
		if (value)
			this.nodeLayer.makeDeletable(this.node);
		else
			this.nodeLayer.makeUndeletable(this.node);
	}

	@Override
	public boolean isDeletable() {
		return this.node.isDeletable();
	}

	@Override
	public Location getLocation() {
		org.monet.space.kernel.model.map.Location monetLocation = this.node.getLocation();
		Location location = new Location();
		String monetWkt = monetLocation.getGeometry().toString();

		location.setLabel(this.node.getLabel());
		location.setGeometry(GeometryFactory.build(monetWkt));

		return location;
	}

	@Override
	public void setLocation(Location bpiLocation) {
		String wkt = bpiLocation.getWkt();
		this.nodeLayer.updateNodeLocation(this.node, GeometryFactory.buildGeometry(wkt));
	}

	public Link toLink() {
		return new Link(this.node.getId(), this.getLabel());
	}

	public MonetLink toMonetLink() {
		return MonetLink.forNode(this.node.getId(), this.node.getLabel(), false);
	}

	public MonetLink toMonetLink(boolean editMode) {
		return MonetLink.forNode(this.node.getId(), this.node.getLabel(), editMode);
	}

	protected void setFlag(String ruleCode, org.monet.metamodel.NodeDefinitionBase.RuleNodeProperty.AddFlagEnumeration flag) {
		this.state.setFlag(flag);
	}

	protected void setFlag(String ruleCode, org.monet.metamodel.NodeDefinitionBase.RuleViewProperty.AddFlagEnumeration flag) {
		RuleViewProperty ruleProperty = (RuleViewProperty) this.definition.getRule(ruleCode);
		org.monet.space.kernel.model.Node mainNode = this.node.getMainNode();

		for (Ref viewRef : ruleProperty.getTo().getView()) {
			ViewProperty viewDefinition = definition.getNodeView(viewRef.getValue());
			String viewCode = viewDefinition.getCode();

			if (mainNode != null && mainNode.isContainer()) {
				for (ViewProperty containerAbstractViewDefinition : ((ContainerDefinition) mainNode.getDefinition()).getViewDefinitionList()) {
					ContainerDefinition.ViewProperty containerViewDefinition = (ContainerDefinition.ViewProperty) containerAbstractViewDefinition;
					if (containerViewDefinition.getShow() != null && containerViewDefinition.getShow().getComponent() != null) {
						ArrayList<Ref> componentRefs = containerViewDefinition.getShow().getComponent();
						for (Ref componentRef : componentRefs) {
							if (componentRef.getDefinition().equals(this.node.getDefinition().getName()) && componentRef.getValue().equals(viewRef.getValue())) {
								if (this.mainState != null)
									this.mainState.setViewFlag(containerViewDefinition.getCode(), flag);
								break;
							}
						}
					}
				}
			}

			this.state.setViewFlag(viewCode, flag);
		}
	}

	protected void setFlag(String ruleCode, org.monet.metamodel.DesktopDefinitionBase.RuleLinkProperty.AddFlagEnumeration flag) {
		RuleLinkProperty ruleProperty = (RuleLinkProperty) this.definition.getRule(ruleCode);
		for (Ref linkRef : ruleProperty.getTo().getLink()) {
			NodeDefinition definition = Dictionary.getInstance().getNodeDefinition(linkRef.getValue());
			this.state.setLinkFlag(definition.getCode(), flag);
		}
	}

	protected void setFlag(String ruleCode, org.monet.metamodel.NodeDefinitionBase.RuleOperationProperty.AddFlagEnumeration flag) {
		RuleOperationProperty ruleProperty = (RuleOperationProperty) this.definition.getRule(ruleCode);
		for (Ref operationRef : ruleProperty.getTo().getOperation()) {
			String operationCode = definition.getOperationMap().get(operationRef.getValue()).getCode();
			this.state.setOperationFlag(operationCode, flag);
		}
	}

	protected void setFlag(String ruleCode, org.monet.metamodel.FormDefinitionBase.RuleFormProperty.AddFlagEnumeration flag) {
		RuleFormProperty ruleProperty = (RuleFormProperty) this.definition.getRule(ruleCode);
		FormDefinition definition = (FormDefinition) this.definition;
		for (Ref fieldRef : ruleProperty.getTo().getField()) {
			String fieldCode = definition.getField(fieldRef.getValue()).getCode();
			this.state.setFieldFlag(fieldCode, flag);
		}
	}

	NodeState calculateNodeState() {
		NodeState calculatedState = new NodeState();
		this.state = calculatedState;
		this.evaluateRules();

		if (this.node.isContainer()) {
			ContainerDefinition containerDefinition = (ContainerDefinition) this.node.getDefinition();
			Dictionary dictionary = Dictionary.getInstance();
			for (Ref contain : containerDefinition.getContain().getNode()) {
				String childDefinitionCode = dictionary.getDefinitionCode(contain.getValue());
				String childId = node.getIndicatorValue("[" + childDefinitionCode + "].value");

				if (!this.nodeLayer.existsNode(childId))
					continue;

				NodeImpl bpiChildNode = nodeToBpi(this.nodeLayer.loadNode(childId));
				bpiChildNode.mainState = calculatedState;
				bpiChildNode.calculateNodeState();
			}
		}

		org.monet.space.kernel.model.Node mainNode = this.node.getMainNode();
		if (this.node.isForm() && mainNode != null && mainNode.isContainer())
			mainNode.setState(null);

		calculatedState.setDate(new java.util.Date());

		this.state = null;
		this.node.setState(calculatedState);

		return calculatedState;
	}

	public void evaluateRules() {
		// Must be implemented by behaviors that have rules to evaluate
	}

	@Override
	public Node clone(Node bpiParent) {
		NodeImpl bpiNode = nodeToBpi(this.node);
		org.monet.space.kernel.model.Node parent = ((NodeImpl) bpiParent).node;
		org.monet.space.kernel.model.Node node = this.nodeLayer.addNode(this.definition.getCode(), parent);
		node.clone(this.node);
		node.setParent(parent);
		bpiNode.injectNode(node);
		return bpiNode;
	}

	@Override
	public List<Node> getLinksIn() {
		String nodeId = node.getId();
		NodeDataRequest dataRequest = getNodeDataRequest();
		List<Node> result = new ArrayList<>();
		int count = nodeLayer.requestNodeSetItemsCount(nodeId, "linksin", dataRequest);

		if (count == 0)
			return result;

		dataRequest.setLimit(count);
		Map<String, org.monet.space.kernel.model.Node> linksIn = nodeLayer.requestNodeSetItems(nodeId, "linksin", dataRequest);
		for (org.monet.space.kernel.model.Node link : linksIn.values())
			result.add(nodeToBpi(link));

		return result;
	}

	@Override
	public List<Node> getLinksOut() {
		String nodeId = this.node.getId();
		NodeDataRequest dataRequest = getNodeDataRequest();
		List<Node> result = new ArrayList<>();
		int count = nodeLayer.requestNodeSetItemsCount(nodeId, "linksout", dataRequest);

		if (count == 0)
			return result;

		dataRequest.setLimit(count);
		Map<String, org.monet.space.kernel.model.Node> linksIn = nodeLayer.requestNodeSetItems(nodeId, "linksout", dataRequest);
		for (org.monet.space.kernel.model.Node link : linksIn.values())
			result.add(nodeToBpi(link));

		return result;
	}

	@Override
	public List<Task> getLinkedTasks() {
		TaskList taskList = this.node.getLinkedTasks();
		List<Task> result = new ArrayList<>();

		for (org.monet.space.kernel.model.Task task : taskList)
			result.add(taskToBpi(task));

		return result;
	}

	@Override
	public void merge(Node source) {
		this.node.merge(((NodeImpl) source).node, true);
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

	@Override
	public boolean executeCommandConfirmationWhen(String operation) {
		return false;
	}

	@Override
	public void executeCommandConfirmationOnCancel(String operation) {
	}

	private NodeDataRequest getNodeDataRequest() {
		NodeDataRequest dataRequest = new NodeDataRequest();
		dataRequest.setCodeReference(DescriptorDefinition.CODE);
		dataRequest.setStartPos(0);
		return dataRequest;
	}

	private NodeImpl nodeToBpi(org.monet.space.kernel.model.Node node) {
		NodeImpl result = bpiClassLocator.instantiateBehaviour(node.getDefinition());
		result.injectNode(node);
		return result;
	}

	private TaskImpl taskToBpi(org.monet.space.kernel.model.Task task) {
		TaskImpl result = bpiClassLocator.instantiateBehaviour(task.getDefinition());
		result.injectTask(task);
		return result;
	}

}
