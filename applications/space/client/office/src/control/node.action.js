//----------------------------------------------------------------------
// Show home
//----------------------------------------------------------------------
function CGActionShowHome() {
	this.base = CGAction;
	this.base(1);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionShowHome.prototype = new CGAction;
CGActionShowHome.constructor = CGActionShowHome;
CommandFactory.register(CGActionShowHome, null, true);

CGActionShowHome.prototype.enabled = function () {
	var IdNode = Account.getUser().getRootNode().id;
	return NodesCache.exists(IdNode);
};

CGActionShowHome.prototype.step_1 = function () {

	var initializerTask = Account.getUser().getInitializerTask();
	if (initializerTask != null) {
		CommandDispatcher.dispatch("showtask(" + initializerTask.id + ")");
		this.setActiveFurniture(Furniture.TASKBOARD);
	}
	else {
		if (Account.Links.length == 0) return;

		var link = Account.Links[0];
		var user = Account.getUser();
		var rootNode = user.getRootNode();

		if (link.type == Furniture.DESKTOP) {
			DefinitionView = Extension.getDefinitionDefaultView(rootNode.code, BUSINESS_MODEL_BROWSE);
			State.View = DefinitionView.Name;
			this.setActiveFurniture(Furniture.DESKTOP, rootNode.id);
			CommandDispatcher.dispatch("shownode(" + rootNode.id + "," + State.View + ")");
		}
		else {
			this.setActiveFurniture(link.type, link.id);
			CommandDispatcher.dispatch(ViewFurnitureSet.getCommand(link));
		}
	}

	this.terminate();
};

// ----------------------------------------------------------------------
// Action show node
// ----------------------------------------------------------------------
function CGActionShowNode() {
	this.base = CGActionShowBase;
	this.base(5);
	this.AvailableProcessClass = CGProcessCleanDirty;
	this.DOMViewActiveTab = null;
	this.Mode = null;
};

CGActionShowNode.prototype = new CGActionShowBase;
CGActionShowNode.constructor = CGActionShowNode;
CommandFactory.register(CGActionShowNode, {
	Id: 0,
	Mode: 1,
	Index: 2,
	Count: 3
}, true);

CGActionShowNode.prototype.getDOMElement = function () {
	var DOMElement = Extension.getDOMNode(this.DOMItem);

	if (!DOMElement)
		DOMElement = Extension.getDOMNodeCollection(this.DOMItem);
	if (!DOMElement)
		DOMElement = Extension.getDOMNodeForm(this.DOMItem);

	return DOMElement;
};

CGActionShowNode.prototype.step_1 = function () {
	var Node = new CGNode();
	var ViewNode;

	Node.setId(this.Id);

	ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.Id);
	if (ViewNode != null) {
		this.DOMViewActiveTab = ViewNode.getDOM().getActiveTab();
		if ((this.Mode == null) || (ViewNode.getMode() == this.Mode)) {
			var Process = new CGProcessActivateNode();
			Process.Id = this.Id;
			Process.execute();
			State.LastView = ViewNode;
			this.terminate();
			return;
		}
		ViewNode.destroy();
	}

	if (this.Mode)
		this.gotoStep(3);
	else
		Kernel.loadNodeType(this, this.Id);
};

CGActionShowNode.prototype.step_2 = function () {
	var Behaviour = Extension.getDefinitionBehaviour(this.data);

	if ((!Behaviour) || (!Behaviour.ShowNode) || (!Behaviour.ShowNode.Templates) || (!Behaviour.ShowNode.Templates.View)) {
		Desktop.hideReports();
		this.terminate();
		return;
	}

	this.Mode = Behaviour.ShowNode.Templates.View;
	this.execute();
};

CGActionShowNode.prototype.step_3 = function () {
	Kernel.loadNode(this, this.Id, this.Mode, this.Index, this.Count);
};

CGActionShowNode.prototype.step_4 = function () {
	var Node, ViewNode;
	var ProcessShowNode;

	Node = new CGNode();
	Node.unserialize(this.data);
	NodesCache.register(Node);

	if (Node.isPrototype()) {
		var CurrentNode = NodesCache.getCurrent();
		if (CurrentNode && CurrentNode.getId() != Node.getId()) {
			var CurrentViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, CurrentNode.getId());
			var PrototypeTemplate = new Template(Lang.ViewNode.ViewingPrototype);
			Desktop.reportBanner(PrototypeTemplate.evaluate({
				'ImagesPath': Context.Config.ImagesPath,
				'prototype': Node.getLabel(),
				'idnode': CurrentViewNode.getDOM().getId(),
				'node': CurrentViewNode.getDOM().getTitle()
			}));
			State.isShowingPrototype = true;
		}
	} else if (State.isShowingPrototype) {
		Desktop.hideBanner();
		State.isShowingPrototype = false;
	}

	ViewNode = this.getView(VIEW_NODE, Node);

	if (Desktop.Main.Center.Body.isContainerView(ViewNode) && Extension.isDefinitionComponent(Node.Code)) {
		CommandDispatcher.dispatch("shownode(" + Node.getIdParent() + ")");
		this.terminate();
		return;
	}

	if ((ViewNode == false) || (ViewNode == null)) {
		this.terminate();
		return;
	}

	if (Desktop.Main.Center.Body.isContainerView(ViewNode)) {
		State.LastView = ViewNode;
		State.LastObject.Id = this.Id;
		State.LastObject.Mode = this.Mode;
	}

	ProcessShowNode = new CGProcessShowNode();
	ProcessShowNode.ActivateNode = false;
	ProcessShowNode.Node = Node;
	ProcessShowNode.Mode = this.Mode;
	ProcessShowNode.ViewNode = ViewNode;
	ProcessShowNode.ReturnProcess = this;
	ProcessShowNode.execute();
};

CGActionShowNode.prototype.step_5 = function () {
	var Process = new CGProcessActivateNode();
	Process.Id = this.Id;
	Process.NotifyFocus = false;
	Process.DOMViewActiveTab = this.DOMViewActiveTab;
	Process.execute();
	this.terminate();
};

// ----------------------------------------------------------------------
// Action show set item
// ----------------------------------------------------------------------
function CGActionShowSetItem() {
	this.base = CGActionShowBase;
	this.base(1);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionShowSetItem.prototype = new CGActionShowBase;
CGActionShowSetItem.constructor = CGActionShowSetItem;
CommandFactory.register(CGActionShowSetItem, {
	Set: 0,
	Item: 1,
	Mode: 2,
	Index: 3,
	Count: 4
}, true);

CGActionShowSetItem.prototype.step_1 = function () {
	var state = State.SetsContext[this.Set];
	if (state == null) state = {};
	var view = this.findView();
	state.view = view != null ? view.getDOM().getActiveTab() : null;
	State.SetsContext[this.Set] = state;
	CommandDispatcher.dispatch("shownode(" + this.Item + "," + this.Mode + "," + this.Index + "," + this.Count + ")");
};

CGActionShowSetItem.prototype.findView = function () {
	var result = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.Set);
	if (result != null) return result;
	var views = Desktop.Main.Center.Body.getViews(VIEW_NODE,VIEW_NODE_TYPE_NODE,this.Set);
	return views.length > 0 ? views[0] : null;
};

// ----------------------------------------------------------------------
// Action show node child
// ----------------------------------------------------------------------
function CGActionShowNodeChild() {
	this.base = CGActionShowBase;
	this.base(3);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionShowNodeChild.prototype = new CGActionShowBase;
CGActionShowNodeChild.constructor = CGActionShowNodeChild;
CommandFactory.register(CGActionShowNodeChild, {
	Ancestor: 0,
	Mode: 1,
	Index: 2,
	Count: 3
}, true);

CGActionShowNodeChild.prototype.onFailure = function (sResponse) {
    Desktop.reportWarning(Lang.ViewNode.ChildView.Final);
};

CGActionShowNodeChild.prototype.step_1 = function () {
	var view = State.SetsContext[this.Ancestor].view;
	var filters = State.getListViewerFilters(this.Ancestor + view);
	Kernel.loadAncestorChildId(this, this.Ancestor, view, this.Index, filters);
};

CGActionShowNodeChild.prototype.step_2 = function () {
    if (this.data === "-1") {
        this.onFailure();
        return;
    }

	this.activeTab = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, NodesCache.getCurrent().getId()).getDOM().getActiveTab();
	var ActionShowNode = new CGActionShowNode();
	ActionShowNode.Id = this.data;
	ActionShowNode.Mode = this.Mode;
	ActionShowNode.Index = this.Index;
	ActionShowNode.Count = this.Count;
	ActionShowNode.ReturnProcess = this;
	ActionShowNode.execute();
};

CGActionShowNodeChild.prototype.step_3 = function () {
	Desktop.Main.Center.Body.getContainerView(VIEW_NODE, NodesCache.getCurrent().getId()).getDOM().activateTab(this.activeTab);
};

// ----------------------------------------------------------------------
// Show Embedded Node
// ----------------------------------------------------------------------
function CGActionShowEmbeddedNode() {
	this.base = CGAction;
	this.base(2);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionShowEmbeddedNode.prototype = new CGAction;
CGActionShowEmbeddedNode.constructor = CGActionShowEmbeddedNode;
CommandFactory.register(CGActionShowEmbeddedNode, {
	Id: 0,
	Mode: 1
}, false);

CGActionShowEmbeddedNode.prototype.step_1 = function () {
	var DOMNode = this.DOMItem.up(CSS_NODE);
	this.ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, DOMNode.IdView);

	if (this.ViewNode == null) {
		this.terminate();
		return;
	}

	var Process = new CGProcessShowNode();
	Process.Id = this.Id;
	Process.Mode = this.Mode;
	Process.ViewNode = this.ViewNode;
	Process.ActivateNode = false;
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionShowEmbeddedNode.prototype.step_2 = function () {
	this.terminate();
};

// ----------------------------------------------------------------------
// Show current node
// ----------------------------------------------------------------------
function CGActionShowCurrentNode() {
	this.base = CGAction;
	this.base(1);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionShowCurrentNode.prototype = new CGAction;
CGActionShowCurrentNode.constructor = CGActionShowCurrentNode;
CommandFactory.register(CGActionShowCurrentNode, {
	Mode: 0
}, true);

CGActionShowCurrentNode.prototype.step_1 = function () {
	var Node = NodesCache.getCurrent();

	if (!Node) {
		this.terminate();
		return;
	}

	CommandDispatcher.dispatch("shownode(" + Node.getId() + "," + this.Mode + ")");
	this.terminate();
};

// ----------------------------------------------------------------------
// Action refresh node
// ----------------------------------------------------------------------
function CGActionRefreshNode() {
	this.base = CGAction;
	this.base(2);
};

CGActionRefreshNode.prototype = new CGAction;
CGActionRefreshNode.constructor = CGActionRefreshNode;
CommandFactory.register(CGActionRefreshNode, {
	Id: 0
}, false);

CGActionRefreshNode.prototype.step_1 = function () {
	var DOMNode, Process;

	if (this.DOMItem != null) {
		DOMNode = this.DOMItem.up(CSS_NODE);
		this.ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, DOMNode.IdView);
	} else {
		this.ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.Id);
		DOMNode = this.ViewNode.getDOM();
	}

	if (this.ViewNode == null) {
		this.terminate();
		return;
	}

	Process = new CGProcessShowNode();
	Process.ReturnProcess = this;
	Process.Id = this.Id;
	Process.Mode = DOMNode.getControlInfo().Templates.Refresh;
	Process.Index = DOMNode.getControlInfo().SetIndex;
	Process.Count = DOMNode.getControlInfo().SetCount;
	Process.ViewNode = this.ViewNode;
	Process.ActivateNode = true;
	Process.execute();
};

CGActionRefreshNode.prototype.step_2 = function () {
	this.ViewNode.getDOM().executeOnloadCommands();
	this.terminate();
};

// ----------------------------------------------------------------------
// Action show field
// ----------------------------------------------------------------------
function CGActionShowField() {
	this.base = CGAction;
	this.base(2);
	this.AvailableProcessClass = CGProcessCleanDirty;
	this.ViewNode = false;
};

CGActionShowField.prototype = new CGAction;
CGActionShowField.constructor = CGActionShowField;
CommandFactory.register(CGActionShowField, {
	Path: 0,
	IdNode: 1,
	Mode: 2
}, true);

CGActionShowField.prototype.step_1 = function () {
	var DOMNode, Process;

	if ((this.Path == null) || (this.IdNode == null) || (this.Mode == null)) {
		this.terminate();
		return;
	}

	DOMNode = this.DOMItem.up(CSS_NODE);
	this.ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, DOMNode.IdView);

	if (this.ViewNode == null) {
		this.terminate();
		return;
	}

	Process = new CGProcessShowNode();
	Process.Id = this.IdNode;
	Process.Mode = this.Mode;
	Process.ViewNode = this.ViewNode;
	Process.ActivateNode = false;
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionShowField.prototype.step_2 = function () {
	CommandDispatcher.dispatch("loadeditors()");

	var Process = new CGProcessGotoField();
	Process.Path = this.Path;
	Process.ViewNode = this.ViewNode;
	Process.ReturnProcess = this;
	Process.execute();

	this.ViewNode = null;
	this.terminate();
};

// ----------------------------------------------------------------------
// Action show child field
// ----------------------------------------------------------------------
function CGActionShowChildField() {
	this.base = CGAction;
	this.base(2);
	this.AvailableProcessClass = CGProcessCleanDirty;
	this.ViewNode = false;
};

CGActionShowChildField.prototype = new CGAction;
CGActionShowChildField.constructor = CGActionShowChildField;
CommandFactory.register(CGActionShowChildField, {
	Path: 0,
	IdNode: 1,
	IdChild: 2,
	Mode: 3
}, true);

CGActionShowChildField.prototype.step_1 = function () {
	var Process;

	if ((this.Path == null) || (this.IdNode == null) || (this.IdChild == null) || (this.Mode == null)) {
		this.terminate();
		return;
	}

	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.IdNode);

	if (ViewNode == null) {
		this.terminate();
		return;
	}

	Process = new CGProcessShowNode();
	Process.Id = this.IdNode;
	Process.Mode = this.Mode;
	Process.ViewNode = ViewNode;
	Process.ActivateNode = false;
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionShowChildField.prototype.step_2 = function () {
	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.IdNode);
	var DOMNode = ViewNode.getDOM();
	var aDOMChildren = DOMNode.getChildrenNodes(this.IdChild);

	CommandDispatcher.dispatch("loadeditors()");

	for (var i = 0; i < aDOMChildren.length; i++) {
		var DOMChild = aDOMChildren[i];
		if (DOMChild.IdView) {
			var Process = new CGProcessGotoField();
			Process.Path = this.Path;
			Process.ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, DOMChild.IdView);
			Process.execute();
		}
	}

	this.terminate();
};

// ----------------------------------------------------------------------
// Activate node
// ----------------------------------------------------------------------
function CGActionActivateNode() {
	this.base = CGAction;
	this.base(1);
};

CGActionActivateNode.prototype = new CGAction;
CGActionActivateNode.constructor = CGActionActivateNode;
CommandFactory.register(CGActionActivateNode, {
	Id: 0
}, true);

CGActionActivateNode.prototype.step_1 = function () {
	var Process;

	Process = new CGProcessActivateNode();
	Process.Id = this.Id;
	Process.execute();

	this.terminate();
};

// ----------------------------------------------------------------------
// Close node
// ----------------------------------------------------------------------
function CGActionCloseNode() {
	this.base = CGAction;
	this.base(1);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionCloseNode.prototype = new CGAction;
CGActionCloseNode.constructor = CGActionCloseNode;
CommandFactory.register(CGActionCloseNode, {
	Id: 0
}, false);

CGActionCloseNode.prototype.step_1 = function () {
	var Process = new CGProcessCloseNode();
	Process.Id = this.Id;
	Process.execute();
	this.terminate();
};

// ----------------------------------------------------------------------
// Edit node
// ----------------------------------------------------------------------
function CGActionEditNode() {
	this.base = CGAction;
	this.base(4);
	this.dlgEditNode = null;
	this.ViewNode = null;
	this.sDummyLayer = null;
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionEditNode.prototype = new CGAction;
CGActionEditNode.constructor = CGActionEditNode;
CommandFactory.register(CGActionEditNode, {
	Id: 0,
	Mode: 1
}, false);

CGActionEditNode.prototype.isDummyView = function () {
	return this.sDummyLayer != null;
};

CGActionEditNode.prototype.createDummyView = function (Node) {
	this.sDummyLayer = Node.getId() + DUMMY;
	createLayer(this.sDummyLayer, EMPTY, $(Literals.NodesContainer));
	return Desktop.createView($(this.sDummyLayer), Node, null, this.Mode, true);
};

CGActionEditNode.prototype.deleteDummyView = function () {
	if (this.sDummyLayer != null) {
		$(this.sDummyLayer).remove();
		Desktop.Main.Center.Body.deleteView(VIEW_NODE, this.ViewNode.getId());
	}
};

CGActionEditNode.prototype.setViewNode = function (Node) {
	var DOMForm;

	DOMForm = $(Node.getId());
	if ((!DOMForm) && (this.DOMItem != null))
		DOMForm = Extension.getDOMNodeForm(this.DOMItem);

	if ((DOMForm) && (DOMForm.IdView) && (Desktop.Main.Center.Body.existsView(DOMForm.IdView))) {
		this.ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, DOMForm.IdView);
	} else {
		this.ViewNode = this.createDummyView(Node);
		this.ViewNode.getDOM().style.visibility = "hidden";
	}
};

CGActionEditNode.prototype.close = function () {
	this.deleteDummyView();
	this.resetState();
	this.terminate();
};

CGActionEditNode.prototype.onFailure = function (sResponse) {
	if (this.dlgEditNode)
		this.dlgEditNode.destroy();
	Desktop.reportError(Lang.Action.EditNode.Failure);
};

CGActionEditNode.prototype.step_1 = function () {
	var Node = new CGNode();

	this.dlgEditNode = new CGDialogEditNode();
	this.dlgEditNode.init();
	this.dlgEditNode.onAccept = this.execute.bind(this);
	this.dlgEditNode.onCancel = this.close.bind(this);
	this.dlgEditNode.show();

	Node.setId(this.Id);

	this.setViewNode(Node);
	if (this.ViewNode == null) {
		this.terminate();
		return;
	}

	if ((!NodesCache.exists(Node.getId())) || (this.isDummyView())) {
		var Process = new CGProcessShowNode();
		Process.Id = Node.getId();
		Process.Mode = this.Mode;
		Process.ViewNode = this.ViewNode;
		Process.ReturnProcess = this;
		Process.execute();
	} else {
		this.execute();
	}

};

CGActionEditNode.prototype.step_2 = function () {
	this.dlgEditNode.setNode(Ext.get(this.ViewNode.getDOM()));
	this.dlgEditNode.refresh();
};

CGActionEditNode.prototype.step_3 = function () {
	Kernel.saveNode(this, this.Id, this.dlgEditNode.Fields, null);
	this.dlgEditNode.destroy();
};

CGActionEditNode.prototype.step_4 = function () {
	var Node;

	Node = new CGNode();
	Node.setId(this.Id);

	this.deleteDummyView();

	this.addRefreshTask(RefreshTaskType.Forms, Node);

	Desktop.reportSuccess(Lang.Action.EditNode.Done);
	this.terminate();
};

// ----------------------------------------------------------------------
// Edit node descriptors
// ----------------------------------------------------------------------
// TODO. Refactorizar esta acción. Se llamará EditNodeDescriptor
function CGActionEditNodeDescriptors() {
	this.base = CGActionEditNode;
	this.base(4);
	this.dlgEditNodeDescriptors = null;
};

CGActionEditNodeDescriptors.prototype = new CGActionEditNode;
CGActionEditNodeDescriptors.constructor = CGActionEditNodeDescriptors;
CommandFactory.register(CGActionEditNodeDescriptors, {
	IdNode: 0,
	Mode: 1,
	Label: 2,
	Description: 3
}, false);

CGActionEditNodeDescriptors.prototype.onFailure = function (sResponse) {
	if (this.dlgEditNodeDescriptors)
		this.dlgEditNodeDescriptors.destroy();
	Desktop.reportError(Lang.Action.EditNodeDescriptors.Failure);
	this.terminate();
};

CGActionEditNodeDescriptors.prototype.step_1 = function () {
	var Node = new CGNode();

	this.dlgEditNodeDescriptors = new CGDialogEditNodeDescriptors();
	this.dlgEditNodeDescriptors.init();
	if (this.Report)
		this.dlgEditNodeDescriptors.showReport(this.Report);
	this.dlgEditNodeDescriptors.onAccept = this.execute.bind(this);
	this.dlgEditNodeDescriptors.onCancel = this.close.bind(this);
	this.dlgEditNodeDescriptors.show();

	Node.setId(this.IdNode);

	this.setViewNode(Node);

	if (this.ViewNode == null) {
		this.terminate();
		return;
	}

	if ((!NodesCache.exists(Node.getId())) || (this.isDummyView())) {
		var Process = new CGProcessShowNode();
		Process.Id = Node.getId();
		Process.Mode = this.Mode;
		Process.ViewNode = this.ViewNode;
		Process.ReturnProcess = this;
		Process.execute();
	} else {
		this.execute();
	}

};

CGActionEditNodeDescriptors.prototype.step_2 = function () {
	var Node = NodesCache.get(this.IdNode);

	var aDescriptors = new Array();
	aDescriptors[DESCRIPTOR_LABEL] = (this.Label) ? this.Label : Node.getReference().getLabel();
	aDescriptors[DESCRIPTOR_DESCRIPTION] = (this.Description) ? this.Description : Node.getReference().getDescription();

	this.dlgEditNodeDescriptors.setDescriptors(aDescriptors);
	this.dlgEditNodeDescriptors.refresh();
};

CGActionEditNodeDescriptors.prototype.step_3 = function () {
	var AttributeList = new CGAttributeList();

	var AttributeLabel = new CGAttribute();
	AttributeLabel.setCode(DESCRIPTOR_LABEL);
	AttributeLabel.addIndicatorByValue(CGIndicator.VALUE, -1, this.dlgEditNodeDescriptors.Descriptors[DESCRIPTOR_LABEL]);
	AttributeList.addAttribute(AttributeLabel);

	var AttributeDescription = new CGAttribute();
	AttributeDescription.setCode(DESCRIPTOR_DESCRIPTION);
	AttributeDescription.addIndicatorByValue(CGIndicator.VALUE, -1, this.dlgEditNodeDescriptors.Descriptors[DESCRIPTOR_DESCRIPTION]);
	AttributeList.addAttribute(AttributeDescription);

	Kernel.saveNodeDescriptor(this, this.IdNode, AttributeList, this.Mode);

	this.dlgEditNodeDescriptors.destroy();
};

CGActionEditNodeDescriptors.prototype.step_4 = function () {
	var Node;

	this.deleteDummyView();

	Node = new CGNode();
	Node.setId(this.IdNode);

	this.addRefreshTask(RefreshTaskType.Descriptors, Node);

	Desktop.reportSuccess(Lang.Action.EditNodeDescriptors.Done);
	this.terminate();
};

// ----------------------------------------------------------------------
// Save node
// ----------------------------------------------------------------------
function CGActionSaveNode() {
	this.base = CGAction;
	this.base(2);
};

CGActionSaveNode.prototype = new CGAction;
CGActionSaveNode.constructor = CGActionSaveNode;
CommandFactory.register(CGActionSaveNode, {
	Id: 0
}, false);

CGActionSaveNode.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Action.SaveNode.Failure);
	this.terminate();
};

CGActionSaveNode.prototype.step_1 = function () {
	State.discardNode = false;
	this.Process = new CGProcessSaveNode();
	this.Process.Id = this.Id;
	this.Process.ReturnProcess = this;
	this.Process.execute();
};

CGActionSaveNode.prototype.step_2 = function () {
	if (this.Process.success())
		Desktop.reportSuccess(Lang.Action.SaveNode.Done);
	else
		Desktop.reportError(Lang.Action.SaveNode.Failure);
	this.terminate();
};

// ----------------------------------------------------------------------
// Save Embedded Node
// ----------------------------------------------------------------------
function CGActionSaveEmbeddedNode() {
	this.base = CGAction;
	this.base(2);
};

CGActionSaveEmbeddedNode.prototype = new CGAction;
CGActionSaveEmbeddedNode.constructor = CGActionSaveEmbeddedNode;
CommandFactory.register(CGActionSaveEmbeddedNode, {
	Id: 0,
	Mode: 1
}, false);

CGActionSaveEmbeddedNode.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Action.SaveEmbeddedNode.Failure);
	this.terminate();
};

CGActionSaveEmbeddedNode.prototype.step_1 = function () {
	var DOMNode = this.DOMItem.up(CSS_NODE);
	DOMNode.setDirty(false);
	Kernel.saveEmbeddedNode(this, this.Id, DOMNode.getContent(), null);
};

CGActionSaveEmbeddedNode.prototype.step_2 = function () {
	Desktop.reportSuccess(Lang.Action.SaveEmbeddedNode.Done);
	this.terminate();
};

// ----------------------------------------------------------------------
// Save node descriptors
// ----------------------------------------------------------------------
// TODO. Refactorizar esta acción. ActionSaveNodeDescriptors ya no existe. Pasa
// a ser ActionSaveNodeDescriptor.
function CGActionSaveNodeDescriptor() {
	this.base = CGAction;
	this.base(2);
	this.dlgEditNodeDescriptors = null;
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionSaveNodeDescriptor.prototype = new CGAction;
CGActionSaveNodeDescriptor.constructor = CGActionSaveNodeDescriptor;
CommandFactory.register(CGActionSaveNodeDescriptor, {
	IdNode: 0,
	Name: 1,
	Value: 2
}, false);

CGActionSaveNodeDescriptor.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Action.SaveNodeDescriptors.Failure);
	this.terminate();
};

CGActionSaveNodeDescriptor.prototype.step_1 = function () {
	var AttributeList = new CGAttributeList();
	var Attribute = new CGAttribute();

	if ((this.IdNode == null) || (this.Name == null) || (this.Value == null)) {
		this.terminate();
		return;
	}

	Attribute.setCode(this.Name);
	Attribute.addIndicatorByValue(CGIndicator.VALUE, -1, HtmlUtil.encode(this.Value));
	AttributeList.addAttribute(Attribute);

	Kernel.saveNodeDescriptor(this, this.IdNode, AttributeList, this.Mode);
};

CGActionSaveNodeDescriptor.prototype.step_2 = function () {
	var Node = new CGNode;
	Node.setId(this.IdNode);
	this.addRefreshTask(RefreshTaskType.References, Node);
	this.terminate();
};

// ----------------------------------------------------------------------
// Back node
// ----------------------------------------------------------------------
function CGActionBackNode() {
	this.base = CGAction;
	this.base(4);
};

CGActionBackNode.prototype = new CGAction;
CGActionBackNode.constructor = CGActionBackNode;
CommandFactory.register(CGActionBackNode, null, false);

CGActionBackNode.prototype.step_1 = function () {
	var Process = new CGProcessShowLastView();
	Process.execute();
	this.terminate();
};

// ----------------------------------------------------------------------
// Cancel node
// ----------------------------------------------------------------------
function CGActionCancelNode() {
	this.base = CGAction;
	this.base(4);
};

CGActionCancelNode.prototype = new CGAction;
CGActionCancelNode.constructor = CGActionCancelNode;
CommandFactory.register(CGActionCancelNode, {
	Id: 0
}, false);

CGActionCancelNode.prototype.step_1 = function () {
	var Process = new CGProcessShowLastView();
	Process.execute();
	this.terminate();
};

// ----------------------------------------------------------------------
// Add node
// ----------------------------------------------------------------------
function CGActionAddNode() {
	this.base = CGAction;
	this.base(6);
	this.dlgAddNode = null;
	this.AvailableProcessClass = CGProcessCleanDirty;
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionAddNode.prototype = new CGAction;
CGActionAddNode.constructor = CGActionAddNode;
CommandFactory.register(CGActionAddNode, {
	DataSource: 0,
	Code: 1,
	Mode: 2,
	ContentMode: 3,
	Parent: 4
}, false);

CGActionAddNode.prototype.enabled = function () {
	var Node, aDefinitions;

	if ((Node = NodesCache.getCurrent()) == null)
		return false;
	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null)
		return false;

	aDefinitions = Extension.getDefinitions(ViewNode.getDOM(), Node.Code);

	return (aDefinitions.size() > 0);
};

CGActionAddNode.prototype.getIdParent = function () {
	var DOMNode = this.DOMItem.up(CSS_NODE);
	var IdParent;

	if (!DOMNode)
		return null;
	if (!DOMNode.getControlInfo)
		return null;

	IdParent = DOMNode.getControlInfo().IdNode;
	if (IdParent == ID_NODE_SEARCH) {
		IdParent = NodesCache.get(State.LastObject.Id).getId();
	}

	return IdParent;
};

CGActionAddNode.prototype.onFailure = function (sResponse) {
	if (this.dlgAddNode)
		this.dlgAddNode.destroy();
	Desktop.reportError(this.getErrorMessage(Lang.Action.AddNode.Failure, sResponse));
	this.terminate();
};

CGActionAddNode.prototype.refresh = function (Dialog) {
	var Process = new CGProcessRefreshNodeTypeDialog();
	Process.Dialog = Dialog;
	Process.execute();
};

CGActionAddNode.prototype.renderDialog = function (Node) {
	var aDefinitions, ViewNode;

	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null) {
		this.terminate();
		return;
	}

	aDefinitions = Extension.getDefinitions(ViewNode.getDOM(), Node.Code);

	if ((aDefinitions == null) || (aDefinitions.length == 0)) {
		Desktop.reportWarning(Lang.ViewNode.DialogAddNode.NoNodes);
		this.terminate();
		return;
	}

	this.dlgAddNode = new CGDialogAddNode();
	this.dlgAddNode.init();
	this.dlgAddNode.Target = {
		NodeTypes: aDefinitions,
		Definition: null,
		From: this.DataSource
	};
	this.dlgAddNode.onAccept = this.gotoStep.bind(this, 3);
	this.dlgAddNode.onSelectNodeType = this.refresh.bind(this);
	this.dlgAddNode.onCancel = this.resetState.bind(this);
	this.dlgAddNode.refresh();
	this.dlgAddNode.show();
};

CGActionAddNode.prototype.step_1 = function () {
	var Node;

	if ((Node = NodesCache.getCurrent()) == null) {
		this.terminate();
		return;
	}

	if ((!this.Mode) || (this.Mode == "") || (this.Mode == "null"))
		this.Mode = null;
	if ((!this.ContentMode) || (this.ContentMode == "") || (this.ContentMode == "null"))
		this.ContentMode = null;
	if ((!this.Parent) || (this.Parent == "") || (this.Parent == "null"))
		this.Parent = this.getIdParent();
	if (this.Parent == CURRENT_NODE)
		this.Parent = Node.getId();

	if (Node.getId() == ID_NODE_SEARCH)
		Node = NodesCache.get(State.LastObject.Id);

	if (this.Code && (this.DataSource == ADD_NODE_BLANK)) {
		this.Process = new CGProcessAddNodeBlank();
		this.Process.Code = this.Code;
		this.Process.Mode = this.Mode;
		this.Process.IdParent = this.Parent;
		this.Process.ContentMode = this.ContentMode;
		this.Process.ReturnProcess = this;
		this.Process.execute();
	} else
		this.renderDialog(Node);
};

CGActionAddNode.prototype.step_2 = function () {
	this.gotoStep(4);
};

CGActionAddNode.prototype.step_3 = function () {

	if (this.dlgAddNode.From == ADD_NODE_BLANK)
		this.Process = new CGProcessAddNodeBlank();
	else if (this.dlgAddNode.From == ADD_NODE_FROM_FILE)
		this.Process = new CGProcessAddNodeFromFile();
	else
		this.Process = new CGProcessAddNodeFromClipboard();

	this.Process.Code = this.dlgAddNode.NodeType.Code;
	this.Process.FileForm = this.dlgAddNode.FileForm;
	this.Process.Format = this.dlgAddNode.Format;
	this.Process.Option = this.dlgAddNode.Option;
	this.Process.Description = this.dlgAddNode.Description;
	this.Process.Data = this.dlgAddNode.Result;
	this.Process.IdParent = this.Parent;
	this.Process.ReturnProcess = this;
	this.Process.execute();
};

CGActionAddNode.prototype.step_4 = function () {
	if (this.Process.success())
		this.execute();
	else
		this.onFailure(this.Process.getFailure());
};

CGActionAddNode.prototype.step_5 = function () {
	var Process, ViewNode, Node = NodesCache.getCurrent();

	if (this.DataSource != ADD_NODE_BLANK) {
		Process = new CGProcessShowNode();

		if (Node.getId() == ID_NODE_SEARCH)
			Node = NodesCache.get(State.LastObject.Id);
		ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId());

		Process.Id = Node.getId();
		Process.Mode = ViewNode.getMode();
		Process.ViewNode = ViewNode;
		Process.ReturnProcess = this;
		Process.execute();
	} else {
		this.execute();
	}
};

CGActionAddNode.prototype.step_6 = function () {
	if (this.dlgAddNode)
		this.dlgAddNode.destroy();

	if (this.DataSource == ADD_NODE_BLANK) {
		var Node = NodesCache.getCurrent();
		var Definition = Extension.getDefinition(Node.Code);

		State.discardNode = (Definition.Type == DEFINITION_TYPE_FORM) ? true : false;
		Desktop.hideReports();

		if (Node.getId() == ID_NODE_SEARCH)
			Node = NodesCache.get(State.LastObject.Id);
		ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId());
		EventManager.notify(EventManager.CREATE_NODE, {
			"Node": Node,
			"DOMNode": ViewNode.getDOM()
		});
	} else
		Desktop.reportSuccess(Lang.Action.AddNode.Done);

	this.terminate();
};

// ----------------------------------------------------------------------
// Add prototype
// ----------------------------------------------------------------------
function CGActionAddPrototype() {
	this.base = CGAction;
	this.base(3);
	this.AvailableProcessClass = CGProcessCleanDirty;
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionAddPrototype.prototype = new CGAction;
CGActionAddPrototype.constructor = CGActionAddPrototype;
CommandFactory.register(CGActionAddPrototype, {
	CodeType: 0,
	Mode: 1,
	ContentMode: 2,
	IdParent: 3
}, false);

CGActionAddPrototype.prototype.onFailure = function (sResponse) {
	Desktop.reportError(this.getErrorMessage(Lang.Action.AddPrototype.Failure, sResponse));
	this.terminate();
};

CGActionAddPrototype.prototype.step_1 = function () {
	var Node;

	if ((Node = NodesCache.getCurrent()) == null) {
		this.terminate();
		return;
	}

	if ((!this.Mode) || (this.Mode == "") || (this.Mode == "null"))
		this.Mode = null;

	if ((!this.ContentMode) || (this.ContentMode == "") || (this.ContentMode == "null"))
		this.ContentMode = null;

	if ((!this.IdParent) || (this.IdParent == "") || (this.IdParent == "null"))
		this.IdParent = null;

	if (Node.getId() == ID_NODE_SEARCH)
		Node = NodesCache.get(State.LastObject.Id);

	this.Process = new CGProcessAddNodeBlank();
	this.Process.Code = this.CodeType;
	this.Process.Mode = this.Mode;
	this.Process.ContentMode = this.ContentMode;
	this.Process.IsPrototype = true;
	this.Process.IdParent = this.IdParent;
	this.Process.Shared = this.IdParent == null;
	this.Process.ReturnProcess = this;
	this.Process.execute();
};

CGActionAddPrototype.prototype.step_2 = function () {
	if (this.Process.success())
		this.execute();
	else
		this.onFailure(this.Process.getFailure());
};

CGActionAddPrototype.prototype.step_3 = function () {
	var Node = NodesCache.getCurrent();
	var Definition = Extension.getDefinition(Node.Code);

	State.discardNode = (Definition.Type == DEFINITION_TYPE_FORM) ? true : false;
	Desktop.hideReports();

	if (Node.getId() == ID_NODE_SEARCH)
		Node = NodesCache.get(State.LastObject.Id);
	ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId());
	EventManager.notify(EventManager.CREATE_NODE, {
		"Node": Node,
		"DOMNode": ViewNode.getDOM()
	});

	this.terminate();
};

// ----------------------------------------------------------------------
// Generate report
// ----------------------------------------------------------------------
function CGActionGenerateReport() {
	this.base = CGAction;
	this.base(4);
	this.AvailableProcessClass = CGProcessCleanDirty;
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionGenerateReport.prototype = new CGAction;
CGActionGenerateReport.constructor = CGActionGenerateReport;
CommandFactory.register(CGActionGenerateReport, {
	CodeType: 0,
	IdTarget: 1
}, false);

CGActionGenerateReport.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Action.GenerateReport.Failure);
	this.terminate();
};

CGActionGenerateReport.prototype.step_1 = function () {
	var Node, DefinitionView;

	if ((Node = NodesCache.getCurrent()) == null) {
		this.terminate();
		return;
	}

	if (Node.getId() == ID_NODE_SEARCH)
		Node = NodesCache.get(State.LastObject.Id);

	DefinitionView = Extension.getDefinitionDefaultView(this.Code, BUSINESS_MODEL_EXPORT);

	if (!DefinitionView) {
		Desktop.reportError(Lang.Action.GenerateReport.Failure);
		this.terminate();
		return;
	}

	this.Process = new CGProcessGenerateReport();
	this.Process.Code = this.Code;
	this.Process.Target = this.IdTarget;
	this.Process.DataSourceTemplate = DefinitionView.Name;
	this.Process.ReturnProcess = this;
	this.Process.execute();
};

CGActionGenerateReport.prototype.step_2 = function () {
	if (this.Process.success())
		this.execute();
	else
		this.onFailure(this.Process.getFailure());
};

CGActionGenerateReport.prototype.step_3 = function () {
	var Process, ViewNode, Node = NodesCache.getCurrent();

	Process = new CGProcessShowNode();

	if (Node.getId() == ID_NODE_SEARCH)
		Node = NodesCache.get(State.LastObject.Id);
	ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId());

	Process.Id = Node.getId();
	Process.Mode = ViewNode.getMode();
	Process.ViewNode = ViewNode;
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionGenerateReport.prototype.step_4 = function () {
	var Node = NodesCache.getCurrent();
	if (Node.getId() == ID_NODE_SEARCH)
		Node = NodesCache.get(State.LastObject.Id);
	ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId());
	EventManager.notify(EventManager.CREATE_NODE, {
		"Node": Node,
		"DOMNode": ViewNode.getDOM()
	});
	this.terminate();
};

// ----------------------------------------------------------------------
// Preview Node
// ----------------------------------------------------------------------
function CGActionPreviewNode() {
	this.base = CGAction;
	this.base(2);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionPreviewNode.prototype = new CGAction;
CGActionPreviewNode.constructor = CGActionPreviewNode;
CommandFactory.register(CGActionPreviewNode, {
	Id: 0,
	IdDOMLayer: 1
}, true);

CGActionPreviewNode.prototype.destroyDocumentViewer = function () {
	if (State.DocumentViewer == null)
		return;
	State.DocumentViewer.dispose();
	$(Literals.PreviewThumbnails).innerHTML = "";
	$(Literals.PreviewButtons).innerHTML = "";
	Desktop.Main.Center.Body.setScrollListener(Desktop.Main.Center.Body.getActiveTab(VIEW_NODE), null, null);
};

CGActionPreviewNode.prototype.createDocumentViewer = function () {
	var DOMLayer;

	this.destroyDocumentViewer();

	DOMLayer = $(this.IdDOMLayer);
	DOMLayer.innerHTML = "";
	DOMLayer.addClassName(CLASS_PREVIEW_PAGES_VIEW);

	Ext.get(Literals.PreviewThumbnails).setHeight(Desktop.Main.Right.getHeight() - 40);

	State.DocumentViewer = new DocumentViewer(Literals.PreviewThumbnails, this.IdDOMLayer, Literals.PreviewButtons, Account.getUser().Language);
	State.DocumentViewer.setDocumentId(this.Id);
	State.DocumentViewer.setViewport(Desktop.Main.Center.Body.getViewport(DOMLayer));
	State.DocumentViewer.setBaseUrl(Kernel.getPreviewNodesLink());

	Desktop.Main.Center.Body.setScrollListener(Desktop.Main.Center.Body.getActiveTab(VIEW_NODE), State.DocumentViewer.onScrollPageView.createDelegate(State.DocumentViewer), DOMLayer);

	State.DocumentViewer.load();
};

CGActionPreviewNode.prototype.step_1 = function () {

	if ((this.Id == null) || (this.IdDOMLayer == null)) {
		this.terminate();
		return;
	}

	var Process = new CGProcessLoadHelperPreview();
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionPreviewNode.prototype.step_2 = function () {
	this.createDocumentViewer();
	this.terminate();
};

// ----------------------------------------------------------------------
// Render Node List
// ----------------------------------------------------------------------
function CGActionRenderNodeList() {
	this.base = CGAction;
	this.base(2);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderNodeList.prototype = new CGAction;
CGActionRenderNodeList.constructor = CGActionRenderNodeList;
CommandFactory.register(CGActionRenderNodeList, {
	Id: 0,
	Code: 1,
	CodeView: 2,
	IdDOMViewerLayer: 3,
	IdDOMViewerLayerOptions: 4
}, false);

CGActionRenderNodeList.prototype.atAddItem = function (DOMNode, Sender, Item) {
	CommandListener.throwCommand(Item.Command);
};

CGActionRenderNodeList.prototype.atDeleteItem = function (Sender, Id) {
	CommandListener.throwCommand("deletenodes(" + Id + "," + this.Id + ")");
};

CGActionRenderNodeList.prototype.atDeleteItems = function (Sender, Items) {
	CommandListener.throwCommand("deletenodes(" + Items.toString('#') + "," + this.Id + ")");
};

CGActionRenderNodeList.prototype.atBoundItem = function (Sender, Item) {
	Item.locked = (Item.deletable == "false");

	var Dummy = Item;
	for (var index in Dummy) {
		if (isFunction(Dummy[index]))
			continue;
		Item[index + "_short"] = shortValue(Dummy[index]);
		if (Item[index] == "true") Item[index] = Item[index + "_short"] = Lang.Response.Yes;
		else if (Item[index] == "false") Item[index] = Item[index + "_short"] = Lang.Response.No;
		try {
			Item[index + "_length"] = Dummy[index].length;
		} catch (e) {
		}
	}
};

CGActionRenderNodeList.prototype.atRenderItem = function (Sender, Item, DOMItem) {
	CommandListener.capture(DOMItem);
};

CGActionRenderNodeList.prototype.atSelectItem = function (bSelected, Sender, Id) {
	if (bSelected)
		State.addNodeReferenceToSelection(this.Id, Id, bSelected);
	else
		State.deleteSelectedNodeReference(this.Id, Id);
};

CGActionRenderNodeList.prototype.atSelectItems = function (bSelected, Sender, Items) {
	if (bSelected) {
		State.addNodesReferencesToSelection(this.Id, Items);
	} else {
		State.deleteSelectedNodesReferences(this.Id);
	}
};

CGActionRenderNodeList.prototype.atUpdateState = function (NewState) {
	State.registerListViewerState(this.Id + this.CodeView, NewState);
    State.registerListViewerState(this.Id, NewState);
};

CGActionRenderNodeList.prototype.destroyViewer = function () {
	if (State.NodeListViewer == null)
		return;
	State.NodeListViewer.dispose();
	$(Literals.ListViewerWizard).innerHTML = "";
};

CGActionRenderNodeList.prototype.createViewer = function () {
	var Options, DOMNode;

	this.destroyViewer();

	DOMNode = $(this.IdDOMViewerLayer).up(CSS_NODE);
	eval($(this.IdDOMViewerLayerOptions).innerHTML);

	State.NodeListViewer = new CGListViewer(Options, Context.Config.Language, Context.Config.ImagesPath);
	State.NodeListViewer.setDataSourceUrls(Kernel.getNodeItemsLink(this.Id, this.Code, this.CodeView), Kernel.getNodeGroupByOptionsLink(this.Id, this.CodeView));
	State.NodeListViewer.setWizardLayer(Literals.ListViewerWizard);
	State.NodeListViewer.onAddItem = CGActionRenderNodeList.prototype.atAddItem.bind(this, DOMNode);
	State.NodeListViewer.onDeleteItem = CGActionRenderNodeList.prototype.atDeleteItem.bind(this);
	State.NodeListViewer.onDeleteItems = CGActionRenderNodeList.prototype.atDeleteItems.bind(this);
	State.NodeListViewer.onBoundItem = CGActionRenderNodeList.prototype.atBoundItem.bind(this);
	State.NodeListViewer.onRenderItem = CGActionRenderNodeList.prototype.atRenderItem.bind(this);
	State.NodeListViewer.onSelectItem = CGActionRenderNodeList.prototype.atSelectItem.bind(this, true);
	State.NodeListViewer.onUnselectItem = CGActionRenderNodeList.prototype.atSelectItem.bind(this, false);
	State.NodeListViewer.onSelectAllItems = CGActionRenderNodeList.prototype.atSelectItems.bind(this, true);
	State.NodeListViewer.onUnselectAllItems = CGActionRenderNodeList.prototype.atSelectItems.bind(this, false);
	State.NodeListViewer.onUpdateState = CGActionRenderNodeList.prototype.atUpdateState.bind(this);
	State.NodeListViewer.setState(State.getListViewerState(this.Id + this.CodeView));
	State.NodeListViewer.render(this.IdDOMViewerLayer);
	State.NodeListViewer.Id = this.Id;
};

CGActionRenderNodeList.prototype.step_1 = function () {

	if ((this.Id == null) || (this.Code == null) || (this.CodeView == null) || (this.IdDOMViewerLayer == null) || (this.IdDOMViewerLayerOptions == null)) {
		this.terminate();
		return;
	}

	var Process = new CGProcessLoadHelperListViewer();
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionRenderNodeList.prototype.step_2 = function () {
	this.createViewer();
	this.terminate();
};

// ----------------------------------------------------------------------
// Render Node Content
// ----------------------------------------------------------------------
function CGActionRenderNodeContent() {
	this.base = CGAction;
	this.base(2);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderNodeContent.prototype = new CGAction;
CGActionRenderNodeContent.constructor = CGActionRenderNodeContent;
CommandFactory.register(CGActionRenderNodeContent, {
	Id: 0,
	Content: 1,
	Code: 2,
	CodeView: 3,
	IdDOMViewerLayer: 4,
	IdDOMViewerLayerOptions: 5
}, false);

CGActionRenderNodeContent.prototype.atAddItem = function (DOMNode, Sender, Item) {
	if (this.Content == "ownedprototypes")
		CommandListener.throwCommand("addprototype(" + Item.Code + ",null,null," + this.Id + ")");
	else if (this.Content == "sharedprototypes")
		CommandListener.throwCommand("addprototype(" + Item.Code + ",null,null,null)");
};

CGActionRenderNodeContent.prototype.atDeleteItem = function (Sender, Id) {
	CommandListener.throwCommand("deletenodes(" + Id + "," + this.Id + ")");
};

CGActionRenderNodeContent.prototype.atDeleteItems = function (Sender, Items) {
	CommandListener.throwCommand("deletenodes(" + Items.toString('#') + "," + this.Id + ")");
};

CGActionRenderNodeContent.prototype.atBoundItem = function (Sender, Item) {
	var Dummy = Item;
	for (var index in Dummy) {
		if (isFunction(Dummy[index]))
			continue;
		Item[index + "_short"] = shortValue(Dummy[index]);
		try {
			Item[index + "_length"] = Dummy[index].length;
		} catch (e) {
		}
	}
};

CGActionRenderNodeContent.prototype.atSelectItem = function (bSelected, Sender, Id) {
	if (bSelected)
		State.addNodeReferenceToSelection(this.Id, Id, bSelected);
	else
		State.deleteSelectedNodeReference(this.Id, Id);
};

CGActionRenderNodeContent.prototype.atSelectItems = function (bSelected, Sender, Items) {
	if (bSelected) {
		State.addNodesReferencesToSelection(this.Id, Items);
	} else {
		State.deleteSelectedNodesReferences(this.Id);
	}
};

CGActionRenderNodeContent.prototype.atUpdateState = function (NewState) {
	State.registerListViewerState(this.Id + this.CodeView, NewState);
    State.registerListViewerState(this.Id, NewState);
};

CGActionRenderNodeContent.prototype.destroyViewer = function () {
	if (State.SetListViewer == null)
		return;
	State.SetListViewer.dispose();
	$(Literals.ListViewerWizard).innerHTML = "";
};

CGActionRenderNodeContent.prototype.createViewer = function () {
	var Options, DOMNode;

	this.destroyViewer();

	DOMNode = $(this.IdDOMViewerLayer).up(CSS_NODE);

	eval($(this.IdDOMViewerLayerOptions).innerHTML);
	State.SetListViewer = new CGListViewer(Options, Context.Config.Language, Context.Config.ImagesPath);
	State.SetListViewer.setDataSourceUrls(Kernel.getNodeSetItemsLink(this.Id, this.Content, this.Code, this.CodeView), null);
	State.SetListViewer.setWizardLayer(Literals.ListViewerWizard);
	State.SetListViewer.onAddItem = CGActionRenderNodeContent.prototype.atAddItem.bind(this, DOMNode);
	State.SetListViewer.onDeleteItem = CGActionRenderNodeContent.prototype.atDeleteItem.bind(this);
	State.SetListViewer.onDeleteItems = CGActionRenderNodeContent.prototype.atDeleteItems.bind(this);
	State.SetListViewer.onBoundItem = CGActionRenderNodeContent.prototype.atBoundItem.bind(this);
	State.SetListViewer.onSelectItem = CGActionRenderNodeContent.prototype.atSelectItem.bind(this, true);
	State.SetListViewer.onUnselectItem = CGActionRenderNodeContent.prototype.atSelectItem.bind(this, false);
	State.SetListViewer.onSelectAllItems = CGActionRenderNodeContent.prototype.atSelectItems.bind(this, true);
	State.SetListViewer.onUnselectAllItems = CGActionRenderNodeContent.prototype.atSelectItems.bind(this, false);
	State.SetListViewer.onUpdateState = CGActionRenderNodeContent.prototype.atUpdateState.bind(this);
	State.SetListViewer.render(this.IdDOMViewerLayer);
	State.SetListViewer.Id = this.Id;
};

CGActionRenderNodeContent.prototype.step_1 = function () {

	if ((this.Id == null) || (this.Content == null) || (this.Code == null) || (this.CodeView == null) || (this.IdDOMViewerLayer == null) || (this.IdDOMViewerLayerOptions == null)) {
		this.terminate();
		return;
	}

	var Process = new CGProcessLoadHelperListViewer();
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionRenderNodeContent.prototype.step_2 = function () {
	this.createViewer();
	this.terminate();
};

// ----------------------------------------------------------------------
// Render Node Attachments
// ----------------------------------------------------------------------
function CGActionRenderNodeAttachments() {
	this.base = CGAction;
	this.base(2);
	this.idDocument = null;
};

CGActionRenderNodeAttachments.prototype = new CGAction;
CGActionRenderNodeAttachments.constructor = CGActionRenderNodeAttachments;
CommandFactory.register(CGActionRenderNodeAttachments, {
	Id: 0,
	Code: 1,
	CodeView: 2,
	IdDOMLayer: 3,
	IdDOMLayerOptions: 4
}, false);

CGActionRenderNodeAttachments.prototype.atSelectAttachment = function (id) {
	State.CurrentAttachment = id;
};

CGActionRenderNodeAttachments.prototype.destroyViewer = function () {
	if (State.AttachmentsViewer == null)
		return;

	if (State.AttachmentsViewer.target.idNode == this.Id)
		this.idDocument = State.CurrentAttachment;

	State.AttachmentsViewer.dispose();
	$(Literals.PreviewThumbnails).innerHTML = "";
	$(Literals.PreviewButtons).innerHTML = "";
	Desktop.Main.Center.Body.setScrollListener(Desktop.Main.Center.Body.getActiveTab(VIEW_NODE), null, null);
};

CGActionRenderNodeAttachments.prototype.atLabelChange = function (newLabel) {
	CommandListener.throwCommand("renameattachment(" + this.Id + "," + newLabel + ")");
};

CGActionRenderNodeAttachments.prototype.atShowDocument = function () {
	var document = State.AttachmentsViewer.getDocument();
	eval($(this.IdDOMLayerOptions).innerHTML);

	if (Options.Editable) {
		CommandListener.throwCommand("replaceattachment(" + this.Id + ")");
		return;
	}

	if (document.targetType == "file") window.location.href = State.AttachmentsViewer.getDownloadDocumentUrl();
	else {
		var process = new CGProcessShowNodeAttachment();
		process.Id = document.id;
		process.execute();
	}
};

CGActionRenderNodeAttachments.prototype.atFactoryBeforeGet = function (document, documentViewer) {
	documentViewer.showDocumentLabel();
};

CGActionRenderNodeAttachments.prototype.createViewer = function () {
	var DOMLayer;

	this.destroyViewer();

	DOMLayer = $(this.IdDOMLayer);
	DOMLayer.innerHTML = "";

	eval($(this.IdDOMLayerOptions).innerHTML);

	Ext.get(Literals.PreviewThumbnails).setHeight(Desktop.Main.Right.getHeight() - 40);

	var documentViewerFactory = new CGDocumentViewerFactory(Kernel.getPreviewAttachmentsLink(), Options.Editable);
	documentViewerFactory.onDocumentLabelChange = CGActionRenderNodeAttachments.prototype.atLabelChange.bind(this);
	documentViewerFactory.onDocumentClick = CGActionRenderNodeAttachments.prototype.atShowDocument.bind(this);
	documentViewerFactory.onBeforeGet = CGActionRenderNodeAttachments.prototype.atFactoryBeforeGet.bind(this);

	State.AttachmentsViewer = new CGViewerDocumentList(documentViewerFactory);
	State.AttachmentsViewer.target = { idNode: this.Id, idDocument: this.idDocument };
	State.AttachmentsViewer.setBaseUrl(Kernel.getNodeAttachmentItemsLink(this.Id, this.Code, this.CodeView));
	State.AttachmentsViewer.onSelect = CGActionRenderNodeAttachments.prototype.atSelectAttachment.bind(this);
	State.AttachmentsViewer.render(this.IdDOMLayer);
};

CGActionRenderNodeAttachments.prototype.step_1 = function () {

	if ((this.Id == null) || (this.Code == null) || (this.CodeView == null) || (this.IdDOMLayer == null) || (this.IdDOMLayerOptions == null)) {
		this.terminate();
		return;
	}

	var Process = new CGProcessLoadHelperPreview();
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionRenderNodeAttachments.prototype.step_2 = function () {
	this.createViewer();
	this.terminate();
};

// ----------------------------------------------------------------------
// Add node attachment
// ----------------------------------------------------------------------
function CGActionAddNodeAttachment() {
	this.base = CGAction;
	this.base(7);
	this.node = null;
};

CGActionAddNodeAttachment.prototype = new CGAction;
CGActionAddNodeAttachment.constructor = CGActionAddNodeAttachment;
CommandFactory.register(CGActionAddNodeAttachment, {
	Id: 0,
	Code: 1,
	Path: 2,
	IsMultiple: 3,
	CodeNode: 4,
	ExporterCode: 5
}, false);

CGActionAddNodeAttachment.prototype.step_1 = function () {

	if (State.AttachmentsViewer == null) {
		this.terminate();
		return;
	}

	var Behaviour = Extension.getDefinitionBehaviour(this.CodeNode);

	if ((!Behaviour) || (!Behaviour.AddNode) || (!Behaviour.AddNode.Templates) || (!Behaviour.AddNode.Templates.Edit)) {
		this.terminate();
		return;
	}

	Kernel.addNode(this, this.CodeNode, this.Id, Behaviour.AddNode.Templates.Edit);
};

CGActionAddNodeAttachment.prototype.step_2 = function () {
	this.node = new CGNode();
	this.node.unserialize(this.data);
	Kernel.fillNodeDocument(this, this.node.getId(), this.ExporterCode, this.Id);
};

CGActionAddNodeAttachment.prototype.step_3 = function () {
	Kernel.saveNode(this, this.node.getId(), "", null);
};

CGActionAddNodeAttachment.prototype.step_4 = function () {
	Kernel.loadNodeAttribute(this, this.Id, this.Path);
};

CGActionAddNodeAttachment.prototype.step_5 = function () {
	var attribute = new CGAttribute();
	attribute.setCode(this.Code);
	attribute.addIndicatorByValue(CGIndicator.CODE, 0, this.node.getId());
	attribute.addIndicatorByValue(CGIndicator.NODE, 1, this.node.getId());
	attribute.addIndicatorByValue(CGIndicator.VALUE, 2, this.node.getLabel());

	State.CurrentAttachment = this.node.getId();
	State.AttachmentsViewer.clear();

	var pathAttribute = new CGAttribute();
	pathAttribute.unserialize(this.data);

	var originalAttribute = this.locateAttribute(pathAttribute, this.Path);
	if (this.IsMultiple == "true")
		originalAttribute.getAttributeList().addAttribute(attribute);
	else {
		originalAttribute.setCode(this.Code);
		originalAttribute.setIndicatorList(attribute.getIndicatorList());
	}

	Kernel.saveNodeAttribute(this, Account.getInstanceId(), this.Id, pathAttribute.serialize());
};

CGActionAddNodeAttachment.prototype.step_6 = function () {
	this.loadMainNode(this.Id);
};

CGActionAddNodeAttachment.prototype.step_7 = function () {
	this.setMainNode(this.Id, this.data);

	var nodeId = this.Id;
	if (this.data != "null") {
		var mainNode = new CGNode();
		mainNode.unserialize(this.data);
		nodeId = mainNode.getId();
	}

	CommandListener.throwCommand("refreshnode(" + nodeId + ")");

	this.terminate();
};

//----------------------------------------------------------------------
// Add file attachment
//----------------------------------------------------------------------
function CGActionAddFileAttachment() {
	this.base = CGAction;
	this.base(5);
};

CGActionAddFileAttachment.prototype = new CGAction;
CGActionAddFileAttachment.constructor = CGActionAddFileAttachment;
CommandFactory.register(CGActionAddFileAttachment, {
	Id: 0,
	Code: 1,
	Path: 2,
	IsMultiple: 3,
	Label: 4,
	Filename: 5
}, false);

CGActionAddFileAttachment.prototype.step_1 = function () {

	if (State.AttachmentsViewer == null) {
		this.terminate();
		return;
	}

	if (this.Label == null || this.Label == "null")
		State.AttachmentsViewer.showAddDialog("file", CGActionAddFileAttachment.prototype.execute.bind(this), CGActionAddFileAttachment.prototype.terminate.bind(this));
	else this.execute();
};

CGActionAddFileAttachment.prototype.step_2 = function () {
	Kernel.loadNodeAttribute(this, this.Id, this.Path);
};

CGActionAddFileAttachment.prototype.step_3 = function () {
	var attribute = new CGAttribute();
	attribute.setCode(this.Code);
	attribute.addIndicatorByValue(CGIndicator.VALUE, 0, State.AttachmentsViewer.getFilename());
	attribute.addIndicatorByValue(CGIndicator.FORMAT, 1, getFileExtension(State.AttachmentsViewer.getFilename()).toUpperCase());
	attribute.addIndicatorByValue(CGIndicator.DETAILS, 2, State.AttachmentsViewer.getLabel());

	State.CurrentAttachment = State.AttachmentsViewer.getFilename();
	State.AttachmentsViewer.clear();

	var pathAttribute = new CGAttribute();
	pathAttribute.unserialize(this.data);

	var originalAttribute = this.locateAttribute(pathAttribute, this.Path);
	var originalAttributes = originalAttribute.getAttributes();

	var found = false;
	for (var i = 0; i < originalAttributes.length; i++) {
		var childAttribute = originalAttributes[i];
		if (childAttribute.getIndicatorValue(CGIndicator.VALUE) == attribute.getIndicatorValue(CGIndicator.VALUE)) {
			found = true;
			break;
		}
	}

	if (found) {
		Desktop.reportWarning(Lang.Action.AddFileAttachment.Failure);
		State.AttachmentsViewer.clear();
		this.gotoStep(1);
		return;
	}

	if (this.IsMultiple == "true")
		originalAttribute.getAttributeList().addAttribute(attribute);
	else {
		originalAttribute.setCode(this.Code);
		originalAttribute.setIndicatorList(attribute.getIndicatorList());
	}

	Kernel.saveNodeAttribute(this, Account.getInstanceId(), this.Id, pathAttribute.serialize());
};

CGActionAddFileAttachment.prototype.step_4 = function () {
	this.loadMainNode(this.Id);
};

CGActionAddFileAttachment.prototype.step_5 = function () {
	this.setMainNode(this.Id, this.data);

	var nodeId = this.Id;
	if (this.data != "null") {
		var mainNode = new CGNode();
		mainNode.unserialize(this.data);
		nodeId = mainNode.getId();
	}

	CommandListener.throwCommand("refreshnode(" + nodeId + ")");

	this.terminate();
};

//----------------------------------------------------------------------
// Rename attachment
//----------------------------------------------------------------------
function CGActionRenameAttachment() {
	this.base = CGAction;
	this.base(7);
	this.node = null;
};

CGActionRenameAttachment.prototype = new CGAction;
CGActionRenameAttachment.constructor = CGActionRenameAttachment;
CommandFactory.register(CGActionRenameAttachment, {
	Id: 0,
	Label: 1
}, false);

CGActionRenameAttachment.prototype.step_1 = function () {

	if (State.AttachmentsViewer == null) {
		this.terminate();
		return;
	}

	if (!State.AttachmentsViewer.hasDocuments()) {
		Desktop.reportWarning(Lang.Action.RenameAttachment.Failure);
		this.terminate();
		return;
	}

	if (this.Label == null || this.Label == "")
		State.AttachmentsViewer.showRenameDialog(CGActionRenameAttachment.prototype.execute.bind(this), CGActionRenameAttachment.prototype.terminate.bind(this));
	else
		this.gotoStep(3);
};

CGActionRenameAttachment.prototype.step_2 = function () {
	this.Label = State.AttachmentsViewer.getLabel();
	this.execute();
};

CGActionRenameAttachment.prototype.step_3 = function () {
	var document = State.AttachmentsViewer.getDocument();
	Kernel.loadNodeAttribute(this, this.Id, document.path);
};

CGActionRenameAttachment.prototype.step_4 = function () {
	var document = State.AttachmentsViewer.getDocument();

	var pathAttribute = new CGAttribute();
	pathAttribute.unserialize(this.data);

	this.originalAttribute = this.locateAttribute(pathAttribute, document.path);

	if (document.targetType == "file") {
		this.execute();
		return;
	}

	var AttributeList = new CGAttributeList();

	var AttributeLabel = new CGAttribute();
	AttributeLabel.setCode(DESCRIPTOR_LABEL);
	AttributeLabel.addIndicatorByValue(CGIndicator.VALUE, -1, this.Label);
	AttributeList.addAttribute(AttributeLabel);

	Kernel.saveNodeDescriptor(this, document.id, AttributeList, null);
};

CGActionRenameAttachment.prototype.step_5 = function () {
	var document = State.AttachmentsViewer.getDocument();
	var attributes = this.originalAttribute.getAttributes();

	for (var i = 0; i < attributes.length; i++) {
		var attribute = attributes[i];
		var indicator = null;
		if (document.targetType == "node" && attribute.getIndicatorValue(CGIndicator.NODE) == document.id) indicator = attribute.getIndicator(CGIndicator.VALUE);
		else if (document.targetType == "file" && attribute.getIndicatorValue(CGIndicator.VALUE) == document.id) indicator = attribute.getIndicator(CGIndicator.DETAILS);
		if (indicator == null) continue;
		indicator.setValue(this.Label);
	}

	State.AttachmentsViewer.clear();

	Kernel.saveNodeAttribute(this, Account.getInstanceId(), this.Id, this.originalAttribute.serialize());
};

CGActionRenameAttachment.prototype.step_6 = function () {
	this.loadMainNode(this.Id);
};

CGActionRenameAttachment.prototype.step_7 = function () {
	this.setMainNode(this.Id, this.data);

	var nodeId = this.Id;
	if (this.data != "null") {
		var mainNode = new CGNode();
		mainNode.unserialize(this.data);
		nodeId = mainNode.getId();
	}

	CommandListener.throwCommand("refreshnode(" + nodeId + ")");

	this.terminate();
};

//----------------------------------------------------------------------
// Replace attachment
//----------------------------------------------------------------------
function CGActionReplaceAttachment() {
	this.base = CGAction;
	this.base(5);
};

CGActionReplaceAttachment.prototype = new CGAction;
CGActionReplaceAttachment.constructor = CGActionReplaceAttachment;
CommandFactory.register(CGActionReplaceAttachment, {
	Id: 0
}, false);

CGActionReplaceAttachment.prototype.step_1 = function () {

	if (State.AttachmentsViewer == null) {
		this.terminate();
		return;
	}

	var document = State.AttachmentsViewer.getDocument();

	if (document.targetType == "node") {
		var process = new CGProcessReplaceNodeAttachment();
		process.Id = document.id;
		process.execute();
		return;
	}

	State.AttachmentsViewer.setLabel(document.label);
	State.AttachmentsViewer.showAddDialog("file", CGActionReplaceAttachment.prototype.execute.bind(this), CGActionReplaceAttachment.prototype.terminate.bind(this));
};

CGActionReplaceAttachment.prototype.step_2 = function () {
	var document = State.AttachmentsViewer.getDocument();
	Kernel.loadNodeAttribute(this, this.Id, document.path);
};

CGActionReplaceAttachment.prototype.step_3 = function () {
	var document = State.AttachmentsViewer.getDocument();

	var pathAttribute = new CGAttribute();
	pathAttribute.unserialize(this.data);

	originalAttribute = this.locateAttribute(pathAttribute, document.path);
	var attributes = originalAttribute.getAttributes();

	for (var i = 0; i < attributes.length; i++) {
		var attribute = attributes[i];
		var indicator = null;

		if (document.targetType == "file" && attribute.getIndicatorValue(CGIndicator.VALUE) == document.id) indicator = attribute.getIndicator(CGIndicator.VALUE);
		if (indicator == null) continue;

		indicator.setValue(State.AttachmentsViewer.getFilename());

		var indicatorFormat = attribute.getIndicator(CGIndicator.FORMAT);
		indicatorFormat.setValue(getFileExtension(State.AttachmentsViewer.getFilename()).toUpperCase());

		var indicatorDetails = attribute.getIndicator(CGIndicator.DETAILS);
		indicatorDetails.setValue(State.AttachmentsViewer.getLabel());
	}

	var nodeId = originalAttribute.getIndicatorValue(CGIndicator.NODE);
	var value = originalAttribute.getIndicatorValue(CGIndicator.VALUE);
	if (nodeId != "" || value != "") {
		var indicatorList = new CGIndicatorList();

		if (nodeId != "")
			indicatorList.addIndicatorByValue(CGIndicator.NODE, 0, State.AttachmentsViewer.getFilename());
		else
		    indicatorList.addIndicatorByValue(CGIndicator.VALUE, 0, State.AttachmentsViewer.getFilename());

		indicatorList.addIndicatorByValue(CGIndicator.FORMAT, 1, getFileExtension(State.AttachmentsViewer.getFilename()).toUpperCase());
		indicatorList.addIndicatorByValue(CGIndicator.DETAILS, 2, State.AttachmentsViewer.getLabel());
		originalAttribute.setIndicatorList(indicatorList);
	}

	State.AttachmentsViewer.clear();

	Kernel.saveNodeAttribute(this, Account.getInstanceId(), this.Id, originalAttribute.serialize());
};

CGActionReplaceAttachment.prototype.step_4 = function () {
	this.loadMainNode(this.Id);
};

CGActionReplaceAttachment.prototype.step_5 = function () {
	this.setMainNode(this.Id, this.data);

	var nodeId = this.Id;
	if (this.data != "null") {
		var mainNode = new CGNode();
		mainNode.unserialize(this.data);
		nodeId = mainNode.getId();
	}

	CommandListener.throwCommand("refreshnode(" + nodeId + ")");

	this.terminate();
};

//----------------------------------------------------------------------
// Delete attachment
//----------------------------------------------------------------------
function CGActionDeleteAttachment() {
	this.base = CGAction;
	this.base(5);
	this.node = null;
};

CGActionDeleteAttachment.prototype = new CGAction;
CGActionDeleteAttachment.constructor = CGActionDeleteAttachment;
CommandFactory.register(CGActionDeleteAttachment, {
	Id: 0
}, false);

CGActionDeleteAttachment.prototype.step_1 = function () {

	if (State.AttachmentsViewer == null) {
		this.terminate();
		return;
	}

	if (!State.AttachmentsViewer.hasDocuments()) {
		Desktop.reportWarning(Lang.Action.DeleteAttachment.Failure);
		this.terminate();
		return;
	}

	Ext.MessageBox.confirm(Lang.ViewNode.DialogDeleteAttachment.Title, Lang.ViewNode.DialogDeleteAttachment.Description, CGActionDeleteAttachment.prototype.checkOption.bind(this));
};

CGActionDeleteAttachment.prototype.step_2 = function () {
	var document = State.AttachmentsViewer.getDocument();
	Kernel.loadNodeAttribute(this, this.Id, document.path);
};

CGActionDeleteAttachment.prototype.step_3 = function () {
	var document = State.AttachmentsViewer.getDocument();
	var originalAttribute = new CGAttribute();
	var found = false;

	var pathAttribute = new CGAttribute();
	pathAttribute.unserialize(this.data);

	originalAttribute = this.locateAttribute(pathAttribute, document.path);
	var attributes = originalAttribute.getAttributes();

	var attributeList = new CGAttributeList();
	for (var i = 0; i < attributes.length; i++) {
		var attribute = attributes[i];
		found = false;
		if (document.targetType == "node" && attribute.getIndicatorValue(CGIndicator.NODE) == document.id) found = true;
		else if (document.targetType == "file" && attribute.getIndicatorValue(CGIndicator.VALUE) == document.id) found = true;
		if (found) continue;
		attributeList.addAttribute(attribute);
	}
	originalAttribute.setAttributeList(attributeList);

	found = false;
	if (document.targetType == "node" && originalAttribute.getIndicatorValue(CGIndicator.NODE) == document.id) found = true;
	else if (document.targetType == "file" && originalAttribute.getIndicatorValue(CGIndicator.VALUE) == document.id) found = true;

	if (found)
		originalAttribute.setIndicatorList(new CGIndicatorList());

	State.AttachmentsViewer.clear();

	Kernel.saveNodeAttribute(this, Account.getInstanceId(), this.Id, pathAttribute.serialize());
};

CGActionDeleteAttachment.prototype.step_4 = function () {
	this.loadMainNode(this.Id);
};

CGActionDeleteAttachment.prototype.step_5 = function () {
	this.setMainNode(this.Id, this.data);

	var nodeId = this.Id;
	if (this.data != "null") {
		var mainNode = new CGNode();
		mainNode.unserialize(this.data);
		nodeId = mainNode.getId();
	}

	CommandListener.throwCommand("refreshnode(" + nodeId + ")");

	this.terminate();
};

//----------------------------------------------------------------------
// Download attachment
//----------------------------------------------------------------------
function CGActionDownloadAttachment() {
	this.base = CGAction;
	this.base(1);
	this.node = null;
};

CGActionDownloadAttachment.prototype = new CGAction;
CGActionDownloadAttachment.constructor = CGActionDownloadAttachment;
CommandFactory.register(CGActionDownloadAttachment, {
	Id: 0
}, false);

CGActionDownloadAttachment.prototype.step_1 = function () {

	if (State.AttachmentsViewer == null) {
		this.terminate();
		return;
	}

	if (!State.AttachmentsViewer.hasDocuments()) {
		Desktop.reportWarning(Lang.Action.DownloadAttachment.Failure);
		this.terminate();
		return;
	}

	window.location.href = State.AttachmentsViewer.getDownloadDocumentUrl();

	this.terminate();
};

// ----------------------------------------------------------------------
// Render Node Location
// ----------------------------------------------------------------------
function CGActionRenderNodeLocation() {
	this.base = CGAction;
	this.base(3);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderNodeLocation.prototype = new CGAction;
CGActionRenderNodeLocation.constructor = CGActionRenderNodeLocation;
CommandFactory.register(CGActionRenderNodeLocation, {
	Id: 0,
	Code: 1,
	CodeView: 2,
	IdDOMViewerLayer: 3,
	IdDOMViewerLayerOptions: 4
}, false);

CGActionRenderNodeLocation.prototype.step_1 = function () {

	Desktop.Main.Right.registerCollapse("CGActionRenderNodeLocation", CGActionRenderNodeLocation.prototype.atCollapse.bind(this));

	if ((this.Id == null) || (this.Code == null) || (this.CodeView == null) || (this.IdDOMViewerLayer == null) || (this.IdDOMViewerLayerOptions == null)) {
		this.terminate();
		return;
	}

	var Process = new CGProcessLoadHelperMapViewer();
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionRenderNodeLocation.prototype.step_2 = function () {
	Kernel.loadNodeLocation(this, this.Id);
};

CGActionRenderNodeLocation.prototype.step_3 = function () {
	var location = this.data ? Ext.util.JSON.decode(this.data) : null;
	var extLocationLayer = Ext.get(this.IdDOMViewerLayer);
	var extNodeLayer = extLocationLayer.up(CSS_NODE);
	var helper = ViewerSidebar.getHelper(Helper.MAP);

	extLocationLayer.setHeight(extNodeLayer.getHeight());
	eval($(this.IdDOMViewerLayerOptions).innerHTML);

	this.view = new CGViewNodeLocation();
	this.view.setDOMLayer(extLocationLayer.dom);
	this.view.init(Options);
	this.view.setTarget(this.Id, location);

	if (Options.editable)
		helper.showEditionLayer();
	else
		helper.hideEditionLayer();

	this.terminate();
};

CGActionRenderNodeLocation.prototype.atCollapse = function () {
	this.view.resize();
};

//----------------------------------------------------------------------
// Render Node Notes
//----------------------------------------------------------------------
function CGActionRenderNodeNotes() {
	this.base = CGAction;
	this.base(2);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderNodeNotes.prototype = new CGAction;
CGActionRenderNodeNotes.constructor = CGActionRenderNodeNotes;
CommandFactory.register(CGActionRenderNodeNotes, {
	Id: 0,
	IdDOMViewerLayer: 1,
	IdDOMViewerLayerOptions: 2
}, false);

CGActionRenderNodeNotes.prototype.step_1 = function () {

	if (this.Id == null || this.IdDOMViewerLayer == null || this.IdDOMViewerLayerOptions == null) {
		this.terminate();
		return;
	}

	Kernel.loadNodeNotes(this, this.Id);
};

CGActionRenderNodeNotes.prototype.step_2 = function () {
	var notes = Ext.util.JSON.decode(this.data);
	var extNotesLayer = Ext.get(this.IdDOMViewerLayer);
	var extNodeLayer = extNotesLayer.up(CSS_NODE);
	var options = null;

	extNotesLayer.setHeight(extNodeLayer.getHeight());
	eval($(this.IdDOMViewerLayerOptions).innerHTML);

	var view = new CGViewNodeNotes();
	view.setDOMLayer(extNotesLayer.dom);
	view.init();
	view.setTarget({nodeId: this.Id, notes: notes, options: options});
	view.refresh();

	this.terminate();
};

//----------------------------------------------------------------------
// Add node note
//----------------------------------------------------------------------
function CGActionAddNodeNote() {
	this.base = CGAction;
	this.base(2);
};

CGActionAddNodeNote.prototype = new CGAction;
CGActionAddNodeNote.constructor = CGActionAddNodeNote;
CommandFactory.register(CGActionAddNodeNote, {
	nodeId: 0,
	name: 1,
	value: 2
}, false);

CGActionAddNodeNote.prototype.step_1 = function () {
	Kernel.addNodeNote(this, this.nodeId, this.name, this.value);
};

CGActionAddNodeNote.prototype.step_2 = function () {
	this.terminate();
};

//----------------------------------------------------------------------
// Delete node note
//----------------------------------------------------------------------
function CGActionDeleteNodeNote() {
	this.base = CGAction;
	this.base(2);
};

CGActionDeleteNodeNote.prototype = new CGAction;
CGActionDeleteNodeNote.constructor = CGActionDeleteNodeNote;
CommandFactory.register(CGActionDeleteNodeNote, {
	nodeId: 0,
	name: 1
}, false);

CGActionDeleteNodeNote.prototype.step_1 = function () {
	Kernel.deleteNodeNote(this, this.nodeId, this.name);
};

CGActionDeleteNodeNote.prototype.step_2 = function () {
	this.terminate();
};

//----------------------------------------------------------------------
// Render Node Notes
//----------------------------------------------------------------------
function CGActionRenderRecentTask() {
	this.base = CGActionShowBase;
	this.base(1);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderRecentTask.prototype = new CGActionShowBase;
CGActionRenderRecentTask.constructor = CGActionRenderRecentTask;
CommandFactory.register(CGActionRenderRecentTask, {
	Id: 0,
	IdTask: 1,
	Mode: 2,
	IdDOMViewerLayer: 3
}, false);

CGActionRenderRecentTask.prototype.step_1 = function () {
	var DOMElement = Ext.get(this.IdDOMViewerLayer).dom;
	var Task = new CGTask();
	Task.setId(this.IdTask);

	//var ContainerView = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.Id);
	var ViewTask = this.createView(VIEW_TASK, Task, DOMElement, null);
	ViewTask.NodeDependant = true;

	var Process = new CGProcessShowTask();
	Process.Id = this.IdTask;
	Process.Mode = ViewTask.getMode();
	Process.ViewTask = ViewTask;
	Process.execute();

	this.terminate();
};

// ----------------------------------------------------------------------
// Copy node
// ----------------------------------------------------------------------
function CGActionCopyNode() {
	this.base = CGAction;
	this.base(2);
	this.AvailableProcessClass = CGProcessCleanDirty;
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionCopyNode.prototype = new CGAction;
CGActionCopyNode.constructor = CGActionCopyNode;
CommandFactory.register(CGActionCopyNode, {
	Id: 0,
	Mode: 1,
	IdParent: 2
}, false);

CGActionCopyNode.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Action.CopyNode.Failure);
	this.terminate();
};

CGActionCopyNode.prototype.getIdParent = function () {
	var DOMNode = this.DOMItem.up(CSS_NODE);
	var IdParent;

	if (!DOMNode)
		return null;
	if (!DOMNode.getControlInfo)
		return null;

	IdParent = DOMNode.getControlInfo().IdNode;
	if (IdParent == ID_NODE_SEARCH) {
		IdParent = NodesCache.get(State.LastObject.Id).getId();
	}

	return IdParent;
};

CGActionCopyNode.prototype.step_1 = function () {
	if (this.IdParent == null)
		this.IdParent = this.getIdParent();
	Kernel.copyNode(this, this.Id, this.IdParent, this.Mode);
};

CGActionCopyNode.prototype.step_2 = function () {
	var Node, Behaviour;
	var sMode = null;

	Node = new CGNode();
	Node.unserialize(this.data);
	Behaviour = Extension.getDefinitionBehaviour(Node.Code);

	if (Behaviour && Behaviour.ShowNode && Behaviour.ShowNode.Templates && Behaviour.ShowNode.Templates.Edit) {
		sMode = Behaviour.ShowNode.Templates.Edit;
	}

	State.aMarkedNodesReferences = [ Node.getId() ];
	State.NodeReferenceMarkType = MarkType.Copied;

	this.addRefreshTask(RefreshTaskType.Copied, [ Node ]);

	CommandDispatcher.dispatch("shownode(" + Node.getId() + (sMode != null ? "," + sMode : "") + ")");

	this.terminate();
};

// ----------------------------------------------------------------------
// Copy nodes
// ----------------------------------------------------------------------
function CGActionCopyNodes() {
	this.base = CGAction;
	this.base(4);
	this.sMode = null;
	this.aIdNodes = new Array();
	this.aNodes = new Array();
	this.Index = 0;
	this.AvailableProcessClass = CGProcessCleanDirty;
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionCopyNodes.prototype = new CGAction;
CGActionCopyNodes.constructor = CGActionCopyNodes;
CommandFactory.register(CGActionCopyNodes, {
	IdParent: 0
}, false);

CGActionCopyNodes.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Action.CopyNodes.Failure);
	this.terminate();
};

CGActionCopyNodes.prototype.getIdParent = function () {
	var DOMNode = this.DOMItem.up(CSS_NODE);
	var IdParent;

	if (!DOMNode)
		return null;
	if (!DOMNode.getControlInfo)
		return null;

	IdParent = DOMNode.getControlInfo().IdNode;
	if (IdParent == ID_NODE_SEARCH) {
		IdParent = NodesCache.get(State.LastObject.Id).getId();
	}

	return IdParent;
};

CGActionCopyNodes.prototype.step_1 = function () {

	if (this.DOMElement == null) {
		if (this.DOMItem == null) {
			this.terminate();
			return;
		}

		this.DOMElement = Extension.getDOMNode(this.DOMItem);
		if (!this.DOMElement)
			this.DOMElement = Extension.getDOMNodeCollection(this.DOMItem);

		if (!this.DOMElement) {
			this.terminate();
			return;
		}
	}

	var ControlInfo = this.DOMElement.getControlInfo();
	this.IdNode = ControlInfo.IdNode;
	this.sMode = ControlInfo.Templates.NodeReferenceAdded;

	if (this.sMode == null) {
		this.terminate();
		return;
	}

	var aSelectedReferences = State.getSelectedNodesReferences(this.IdNode);
	for (var index in aSelectedReferences) {
		if (isFunction(aSelectedReferences[index]))
			continue;
		this.aIdNodes.push(aSelectedReferences[index]);
	}

	if (this.aIdNodes.length <= 0) {
		Desktop.reportWarning(Lang.ViewNode.DialogCopyNodes.NoNodesReferencesSelected);
		this.terminate();
		return;
	}

	this.execute();
};

CGActionCopyNodes.prototype.step_2 = function () {
	if (this.IdParent == null)
		this.IdParent = this.getIdParent();
	if (this.aIdNodes[this.Index] != null)
		Kernel.copyNode(this, this.aIdNodes[this.Index], this.IdParent, this.sMode);
};

CGActionCopyNodes.prototype.step_3 = function () {

	var Node = new CGNode();
	Node.unserialize(this.data);
	this.aNodes.push(Node);

	this.Index++;

	if (this.Index < this.aIdNodes.length)
		this.gotoStep(2);
	else
		this.execute();
};

CGActionCopyNodes.prototype.step_4 = function () {

	State.aMarkedNodesReferences = new Array();
	for (var iPos = 0; iPos < this.aNodes.length; iPos++) {
		State.aMarkedNodesReferences.push(this.aNodes[iPos].getId());
	}
	State.NodeReferenceMarkType = MarkType.Copied;

	this.addRefreshTask(RefreshTaskType.Copied, this.aNodes);

	Desktop.reportSuccess(Lang.Action.CopyNodes.Done);

	this.terminate();
};

// ----------------------------------------------------------------------
// Share node
// ----------------------------------------------------------------------
function CGActionShareNode() {
	this.base = CGAction;
	this.base(3);
	this.dlgShareNode = null;
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionShareNode.prototype = new CGAction;
CGActionShareNode.constructor = CGActionShareNode;
CommandFactory.register(CGActionShareNode, {
	Id: 0
}, false);

CGActionShareNode.prototype.onFailure = function (sResponse) {
	if (this.dlgShareNode)
		this.dlgShareNode.destroy();
	Desktop.reportError(Lang.Action.ShareNode.Failure);
	this.terminate();
};

CGActionShareNode.prototype.step_1 = function () {
	var Node = new CGNode();

	Node.setId(this.Id);

	this.dlgShareNode = new CGDialogShareNode();
	this.dlgShareNode.init();
	this.dlgShareNode.onAccept = this.execute.bind(this);
	this.dlgShareNode.onCancel = this.resetState.bind(this);
	this.dlgShareNode.show();
	this.dlgShareNode.refresh();
};

CGActionShareNode.prototype.step_2 = function () {
	Kernel.shareNode(this, this.Id, this.dlgShareNode.UserList, this.dlgShareNode.Description, this.dlgShareNode.ExpireDate);
	this.dlgShareNode.destroy();
};

CGActionShareNode.prototype.step_3 = function () {
	Desktop.reportSuccess(Lang.Action.ShareNode.Done);
	this.terminate();
};

// ----------------------------------------------------------------------
// Delete node
// ----------------------------------------------------------------------
function CGActionDeleteNode() {
	this.base = CGAction;
	this.base(3);
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionDeleteNode.prototype = new CGAction;
CGActionDeleteNode.constructor = CGActionDeleteNode;
CommandFactory.register(CGActionDeleteNode, {
	Id: 0
}, false);

CGActionDeleteNode.prototype.onFailure = function () {
	Desktop.reportError(this.Process.getFailure());
	this.terminate();
};

CGActionDeleteNode.prototype.step_1 = function () {
	var sDescription = Lang.ViewNode.DialogDeleteNode.Description;
	Ext.MessageBox.confirm(Lang.ViewNode.DialogDeleteNode.Title, sDescription, CGActionDeleteNode.prototype.checkOption.bind(this));
};

CGActionDeleteNode.prototype.step_2 = function () {
	this.Process = new CGProcessDeleteNodes();
	this.Process.Nodes = [ this.Id ];
	this.Process.ReturnProcess = this;
	this.Process.execute();
};

CGActionDeleteNode.prototype.step_3 = function () {

	if (!this.Process.success()) {
		this.onFailure();
		return;
	}

	Desktop.reportSuccess(Lang.Action.DeleteNode.Done);

	var CurrentNode = NodesCache.getCurrent();
	if (CurrentNode.getId() == this.Id) {
		var ProcessCloseNode = new CGProcessCloseNode();
		ProcessCloseNode.Id = this.Id;
		ProcessCloseNode.execute();
		history.back();
	}

	this.terminate();
};

// ----------------------------------------------------------------------
// Delete nodes
// ----------------------------------------------------------------------
function CGActionDeleteNodes() {
	this.base = CGAction;
	this.base(3);
	this.AvailableProcessClass = CGProcessCleanDirty;
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionDeleteNodes.prototype = new CGAction;
CGActionDeleteNodes.constructor = CGActionDeleteNodes;
CommandFactory.register(CGActionDeleteNodes, {
	Nodes: 0,
	IdParent: 1
}, false);

CGActionDeleteNodes.prototype.enabled = function () {
	var Node, aDefinitions;

	if ((Node = NodesCache.getCurrent()) == null)
		return false;
	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null)
		return false;

	aDefinitions = Extension.getDefinitions(ViewNode.getDOM(), Node.Code);

	return (aDefinitions.size() > 0);
};

CGActionDeleteNodes.prototype.onFailure = function () {
	Desktop.reportError(this.Process.getFailure());
	this.terminate();
};

CGActionDeleteNodes.prototype.step_1 = function () {

	if (this.IdParent == null)
		this.IdParent = NodesCache.getCurrent().getId();

	if (this.Nodes != null) this.Nodes = this.Nodes.split("#");
	else this.Nodes = State.getSelectedNodesReferences(this.IdParent);

	if (this.Nodes.size() <= 0)
		Desktop.reportWarning(Lang.ViewNode.DialogDeleteNodes.NoNodesReferencesSelected);
	else {
		Ext.MessageBox.confirm(Lang.ViewNode.DialogDeleteNodes.Title, Lang.ViewNode.DialogDeleteNodes.Description, CGActionDeleteNodes.prototype.checkOption.bind(this));
	}
};

CGActionDeleteNodes.prototype.step_2 = function () {
	this.Process = new CGProcessDeleteNodes();
	this.Process.Nodes = this.Nodes;
	this.Process.ReturnProcess = this;
	this.Process.execute();
};

CGActionDeleteNodes.prototype.step_3 = function () {

	if (!this.Process.success()) {
		this.onFailure();
		return;
	}

	State.deleteSelectedNodesReferences(this.IdParent);
	Desktop.reportSuccess(Lang.Action.DeleteNodes.Done);

	this.terminate();
};

// ----------------------------------------------------------------------
// Discard node
// ----------------------------------------------------------------------
function CGActionDiscardNode() {
	this.base = CGAction;
	this.base(2);
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionDiscardNode.prototype = new CGAction;
CGActionDiscardNode.constructor = CGActionDiscardNode;
CommandFactory.register(CGActionDiscardNode, {
	Id: 0
}, false);

CGActionDiscardNode.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Action.DeleteNode.Failure);
	this.terminate();
};

CGActionDiscardNode.prototype.step_1 = function () {
	Kernel.discardNode(this, this.Id);
};

CGActionDiscardNode.prototype.step_2 = function () {
	var Node = new CGNode();

	Node.setId(this.Id);

	this.addRefreshTask(RefreshTaskType.Discarted, [ Node ]);

	this.terminate();
};

// ----------------------------------------------------------------------
// Select nodes
// ----------------------------------------------------------------------
function CGActionSelectNodes() {
	this.base = CGAction;
	this.base(1);
};

CGActionSelectNodes.prototype = new CGAction;
CGActionSelectNodes.constructor = CGActionSelectNodes;
CommandFactory.register(CGActionSelectNodes, {
	Type: 0
}, false);

CGActionSelectNodes.prototype.step_1 = function () {
	if (!State.NodeListViewer)
		return;

	if (this.Type == "none")
		State.NodeListViewer.unselectAll();

	this.terminate();
};

// ----------------------------------------------------------------------
// Edit node labels
// ----------------------------------------------------------------------
function CGActionEditNodeLabels() {
	this.base = CGAction;
	this.base(2);
	this.DOMElement = null;
};

CGActionEditNodeLabels.prototype = new CGAction;
CGActionEditNodeLabels.constructor = CGActionEditNodeLabels;
CommandFactory.register(CGActionEditNodeLabels, null, false);

CGActionEditNodeLabels.prototype.step_1 = function () {
	var DOMObject = null;
	var ContainerView;

	if (this.DOMItem != null)
		this.DOMElement = Extension.getDOMNodeCollection(this.DOMItem);

	if (!DOMObject) {
		ContainerView = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, NodesCache.getCurrent().getId());

		if (!ContainerView) {
			this.terminate();
			return;
		}

		this.DOMElement = ContainerView.getDOM();
	}

	this.DOMElement.renderAsDialog();
};

CGActionEditNodeLabels.prototype.step_2 = function () {
	this.terminate();
};

// ----------------------------------------------------------------------
// Set node view
// ----------------------------------------------------------------------
function CGActionSetNodeView() {
	this.base = CGAction;
	this.base(1);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionSetNodeView.prototype = new CGAction;
CGActionSetNodeView.constructor = CGActionSetNodeView;
CommandFactory.register(CGActionSetNodeView, {
	IdNode: 0,
	View: 1
}, false);

CGActionSetNodeView.prototype.enabled = function () {
	var Node, aViews;

	if ((Node = NodesCache.getCurrent()) == null)
		return false;
	if (Node.getId() == ID_NODE_SEARCH)
		Node = NodesCache.get(State.LastObject.Id);
	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null)
		return false;

	aViews = Extension.getDefinitionViews(ViewNode.getDOM(), Node.Code, BUSINESS_MODEL_BROWSE);

	return ((aViews) && (aViews.size() > 1));
};

CGActionSetNodeView.prototype.refresh = function (Menu) {
	var Node, aDefinitions;

	if ((Node = NodesCache.getCurrent()) == null)
		return;
	if (Node.getId() == ID_NODE_SEARCH)
		Node = NodesCache.get(State.LastObject.Id);
	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null)
		return;

	aDefinitions = Extension.getDefinitionViews(ViewNode.getDOM(), Node.Code, BUSINESS_MODEL_BROWSE);

	DialogNodeViewList = new CGDialogNodeViewList();
	DialogNodeViewList.init();
	DialogNodeViewList.Target = {
		NodeViews: aDefinitions,
		Node: Node,
		Menu: Menu
	};
	DialogNodeViewList.refresh();
};

CGActionSetNodeView.prototype.step_1 = function () {
	State.View = this.View;
	CommandDispatcher.dispatch("shownode(" + this.IdNode + "," + this.View + ")");
	this.terminate();
};

// ----------------------------------------------------------------------
// Toggle Highlight node
// ----------------------------------------------------------------------
function CGActionToggleHighlightNode() {
	this.base = CGAction;
	this.base(2);
	this.DOMNode = null;
	this.AvailableProcessClass = CGProcessCleanDirty;
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionToggleHighlightNode.prototype = new CGAction;
CGActionToggleHighlightNode.constructor = CGActionToggleHighlightNode;
CommandFactory.register(CGActionToggleHighlightNode, {
	Id: 0,
	Mode: 1
}, false);

CGActionToggleHighlightNode.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Action.ToggleHighlightNode.Failure);
	this.terminate();
};

CGActionToggleHighlightNode.prototype.step_1 = function () {
	var DOMNode, ViewContainer;
	var AttributeList = new CGAttributeList();
	var Attribute = new CGAttribute();

	ViewContainer = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, NodesCache.getCurrent().getId());
	if (!ViewContainer)
		return;

	DOMNode = ViewContainer.getDOM();

	if ((!DOMNode) || (!DOMNode.getNodeReferences) || (!DOMNode.isHighlighted)) {
		this.terminate();
		return;
	}

	sValue = 0;
	aDOMNodeReferences = DOMNode.getNodeReferences(this.Id);
	if (aDOMNodeReferences.length > 0)
		sValue = ((aDOMNodeReferences[0].isHighlighted) && (aDOMNodeReferences[0].isHighlighted())) ? 0 : 1;
	else {
		sValue = (DOMNode.isHighlighted()) ? 0 : 1;
		if (DOMNode.setHighlighted)
			DOMNode.setHighlighted(sValue);
	}

	Attribute.setCode(DESCRIPTOR_HIGHLIGHTED);
	Attribute.addIndicatorByValue(CGIndicator.VALUE, -1, sValue);
	AttributeList.addAttribute(Attribute);

	Kernel.saveNodeDescriptor(this, this.Id, AttributeList, this.Mode);
};

CGActionToggleHighlightNode.prototype.step_2 = function () {
	var Node = new CGNode;

	try {
		Node.unserialize(this.data);
		this.addRefreshTask(RefreshTaskType.Descriptors, Node);
	} catch (e) {
	}

	this.terminate();
};

// ----------------------------------------------------------------------
// Download node
// ----------------------------------------------------------------------
function CGActionDownloadNode() {
	this.base = CGAction;
	this.base(1);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionDownloadNode.prototype = new CGAction;
CGActionDownloadNode.constructor = CGActionDownloadNode;
CommandFactory.register(CGActionDownloadNode, {
	Id: 0
}, false);

CGActionDownloadNode.prototype.enabled = function () {
	var Node = NodesCache.getCurrent();
	if (!Node)
		return false;
	if (Node.getId() == ID_NODE_SEARCH)
		Node = NodesCache.get(State.LastObject.Id);
	return Extension.isDefinitionExportable(Node.Code);
};

CGActionDownloadNode.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	Desktop.reportError(Lang.Action.DownloadNode.Failure);
	this.terminate();
};

CGActionDownloadNode.prototype.step_1 = function () {
	document.location = Kernel.getDownloadNodeLink(this.Id);
	this.terminate();
};

// ----------------------------------------------------------------------
// Export node
// ----------------------------------------------------------------------
function CGActionExportNode() {
	this.base = CGAction;
	this.base(2);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionExportNode.prototype = new CGAction;
CGActionExportNode.constructor = CGActionExportNode;
CommandFactory.register(CGActionExportNode, {
	Id: 0,
	Format: 1
}, false);

CGActionExportNode.prototype.enabled = function () {
	var Node = NodesCache.getCurrent();
	if (!Node)
		return false;
	if (Node.getId() == ID_NODE_SEARCH)
		Node = NodesCache.get(State.LastObject.Id);
	return Extension.isDefinitionExportable(Node.Code);
};

CGActionExportNode.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	Desktop.reportError(Lang.Action.ExportNode.Failure);
	this.terminate();
};

CGActionExportNode.prototype.step_1 = function () {
	var Node = NodesCache.get(this.Id);

	if (!Node) {
		Desktop.reportError(Lang.Action.Export.ParametersWrong);
		this.terminate();
		return;
	}

	Kernel.exportNode(this, this.Id, this.Format);
};

CGActionExportNode.prototype.step_2 = function () {
	this.terminate();
	document.location = Kernel.getDownloadExportedNodeLink(this.Id, this.Format);
};

// ----------------------------------------------------------------------
// Search Nodes
// ----------------------------------------------------------------------
function CGActionSearchNodes() {
	this.base = CGAction;
	this.base(2);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionSearchNodes.prototype = new CGAction;
CGActionSearchNodes.constructor = CGActionSearchNodes;
CommandFactory.register(CGActionSearchNodes, {
	Condition: 0,
	Code: 1,
	Mode: 2
}, true);

CGActionSearchNodes.prototype.enabled = function () {
	return true;
	/*
	 * var Node, Behaviour; if ((Node = NodesCache.getCurrent()) == null) return
	 * false; if (Node.getId() == ID_NODE_SEARCH) return true; Behaviour =
	 * Extension.getDefinitionBehaviour(Node.Code); return (Behaviour.Search !=
	 * null);
	 */
};

CGActionSearchNodes.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Action.SearchNodes.Failure);
	State.Searching = false;
	Desktop.hideReports();
	Desktop.Main.Center.Header.refresh();
	this.terminate();
};

CGActionSearchNodes.prototype.loadCollections = function (ViewNode, Node) {
	var ViewNodeCollection;
	var ControlInfo;
	var DOMNode;

	if ((DOMNode = ViewNode.getDOM()) == null)
		return;
	if (!DOMNode.getCollections)
		return;

	aDOMCollections = DOMNode.getCollections();
	for (var iPos = 0; iPos < aDOMCollections.length; iPos++) {
		DOMCollection = aDOMCollections[iPos];
		if (!DOMCollection.getControlInfo)
			return;

		ControlInfo = DOMCollection.getControlInfo();

		ViewNodeCollection = Desktop.createView(DOMCollection, Node, ViewNode, ControlInfo.Templates.Refresh, false);
		ViewNodeCollection.setType(VIEW_NODE_TYPE_COLLECTION);
	}
};

CGActionSearchNodes.prototype.step_1 = function () {
	State.Searching = true;
	Desktop.Main.Center.Header.refresh();

	this.IdNode = Account.getUser().getRootNode().id;
	/*
	 * if ((this.IdNode == null) || (this.IdNode == "") || (this.IdNode ==
	 * "null")) { var Node = NodesCache.getCurrent(); if (Node.getId() ==
	 * ID_NODE_SEARCH) Node = State.LastObject.Id; var Behaviour =
	 * Extension.getDefinitionBehaviour(Node.Code); if (Behaviour.Search != null)
	 * this.IdNode = Node.getId(); else this.IdNode = State.LastSearch.IdNode; }
	 *
	 * if (this.IdNode == null) { this.onFailure(); return; }
	 */

	Node = NodesCache.get(this.IdNode);

	if (((this.Condition == null) || (this.Condition == "") || (this.Condition == "null")) && (State.LastSearch.Condition)) {
		this.Condition = State.LastSearch.Condition;
	}
	if (this.Condition == null) {
		this.onFailure();
		return;
	}

	if ((this.Code == null) || (this.Code == "null")) {
		this.Code = Node.Code;
	}

	if ((this.Mode == null) || (this.Mode == "null")) {
		var Behaviour = Extension.getDefinitionBehaviour(this.Code);

		if (Behaviour && Behaviour.Search && Behaviour.Search.Templates && Behaviour.Search.Templates.View)
			this.Mode = Behaviour.Search.Templates.View;
		else if (State.LastSearch.Mode)
			this.Mode = State.LastSearch.Mode;
		else {
			this.onFailure();
			return;
		}
	}

	var LastSearch = State.LastSearch;
	if ((this.Condition == LastSearch.Condition) && (this.Code == LastSearch.Code) && (this.Mode == LastSearch.Mode)) {
		Desktop.Main.Center.Body.activateTab(VIEW_NODE, ID_NODE_SEARCH);
		State.Searching = false;
		Desktop.Main.Center.Header.refresh();
		this.terminate();
	}

	Kernel.searchNodes(this, this.Condition, this.IdNode, this.Code, this.Mode);
};

CGActionSearchNodes.prototype.step_2 = function () {
	var Node, ViewNode, IdTab;

	Node = new CGNode();
	Node.unserialize(this.data);
	Node.setId(ID_NODE_SEARCH);
	Node.Code = this.Code;

	NodesCache.register(Node);
	NodesCache.setCurrent(ID_NODE_SEARCH);

	ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId());

	if (ViewNode == null) {
		IdTab = Desktop.Main.Center.Body.addTab(VIEW_NODE, {Id: Node.getId()});
		ViewNode = Desktop.createView($(IdTab), Node, null, this.Mode, true);
	} else {
		Desktop.Main.Center.Body.deleteView(VIEW_NODE, ViewNode.getId());
		IdTab = Desktop.Main.Center.Body.getTabId(VIEW_NODE, Node.getId());
		ViewNode = Desktop.createView($(IdTab), Node, null, this.Mode, true);
		Desktop.Main.Center.Body.activateTab(VIEW_NODE, Node.getId());
	}

	this.loadCollections(ViewNode, Node);

	ViewNode.selectNodesReferences(State.getSelectedNodesReferences(ID_NODE_SEARCH));

	State.LastSearch.IdNode = this.IdNode;
	State.LastSearch.Condition = this.Condition;
	State.LastSearch.Code = this.Code;
	State.LastSearch.Mode = this.Mode;

	State.Searching = false;
	Desktop.Main.Center.Header.refresh();

	this.terminate();
};

// ----------------------------------------------------------------------
// First Field
// ----------------------------------------------------------------------
function CGActionFirstField() {
	this.base = CGAction;
	this.base(1);
};

CGActionFirstField.prototype = new CGAction;
CGActionFirstField.constructor = CGActionFirstField;
CommandFactory.register(CGActionFirstField, null, false);

CGActionFirstField.prototype.step_1 = function () {
	var Node, ViewNode;

	if ((Node = NodesCache.getCurrent()) == null)
		return;
	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null)
		return;
	if (!ViewNode.getDOM().firstField)
		return;

	ViewNode.getDOM().firstField();

	this.terminate();
};

// ----------------------------------------------------------------------
// Previous Field
// ----------------------------------------------------------------------
function CGActionPreviousField() {
	this.base = CGAction;
	this.base(1);
};

CGActionPreviousField.prototype = new CGAction;
CGActionPreviousField.constructor = CGActionPreviousField;
CommandFactory.register(CGActionPreviousField, null, false);

CGActionPreviousField.prototype.step_1 = function () {
	var Node, ViewNode;

	if ((Node = NodesCache.getCurrent()) == null)
		return;
	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null)
		return;
	if (!ViewNode.getDOM().previousField)
		return;

	ViewNode.getDOM().previousField();

	this.terminate();
};

// ----------------------------------------------------------------------
// Next Field
// ----------------------------------------------------------------------
function CGActionNextField() {
	this.base = CGAction;
	this.base(1);
};

CGActionNextField.prototype = new CGAction;
CGActionNextField.constructor = CGActionNextField;
CommandFactory.register(CGActionNextField, null, false);

CGActionNextField.prototype.step_1 = function () {
	var Node, ViewNode;

	if ((Node = NodesCache.getCurrent()) == null)
		return;
	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null)
		return;
	if (!ViewNode.getDOM().nextField)
		return;

	ViewNode.getDOM().nextField();

	this.terminate();
};

// ----------------------------------------------------------------------
// Last Field
// ----------------------------------------------------------------------
function CGActionLastField() {
	this.base = CGAction;
	this.base(1);
};

CGActionLastField.prototype = new CGAction;
CGActionLastField.constructor = CGActionLastField;
CommandFactory.register(CGActionLastField, null, false);

CGActionLastField.prototype.step_1 = function () {
	var Node, ViewNode;

	if ((Node = NodesCache.getCurrent()) == null)
		return;
	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null)
		return;
	if (!ViewNode.getDOM().lastField)
		return;

	ViewNode.getDOM().lastField();

	this.terminate();
};

// ----------------------------------------------------------------------
// Goto Field
// ----------------------------------------------------------------------
function CGActionGotoField() {
	this.base = CGAction;
	this.base(1);
};

CGActionGotoField.prototype = new CGAction;
CGActionGotoField.constructor = CGActionGotoField;
CommandFactory.register(CGActionGotoField, {
	Path: 0
}, false);

CGActionGotoField.prototype.step_1 = function () {
	if (this.Path == null)
		return;
	if ((Node = NodesCache.getCurrent()) == null)
		return;
	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null)
		return;

	var Process = new CGProcessGotoField();
	Process.Path = this.Path;
	Process.ViewNode = ViewNode;
	Process.execute();

	this.terminate();
};

// ----------------------------------------------------------------------
// Undo node
// ----------------------------------------------------------------------
function CGActionUndoNode() {
	this.base = CGAction;
	this.base(1);
};

CGActionUndoNode.prototype = new CGAction;
CGActionUndoNode.constructor = CGActionUndoNode;
CommandFactory.register(CGActionUndoNode, null, false);

CGActionUndoNode.prototype.step_1 = function () {
	var Node, ViewNode;

	if ((Node = NodesCache.getCurrent()) == null)
		return;
	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null)
		return;
	if (!ViewNode.getDOM().undo)
		return;

	ViewNode.getDOM().undo();

	this.terminate();
};

// ----------------------------------------------------------------------
// Redo node
// ----------------------------------------------------------------------
function CGActionRedoNode() {
	this.base = CGAction;
	this.base(1);
};

CGActionRedoNode.prototype = new CGAction;
CGActionRedoNode.constructor = CGActionRedoNode;
CommandFactory.register(CGActionRedoNode, null, false);

CGActionRedoNode.prototype.step_1 = function () {
	var Node, ViewNode;

	if ((Node = NodesCache.getCurrent()) == null)
		return;
	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null)
		return;
	if (!ViewNode.getDOM().redo)
		return;

	ViewNode.getDOM().redo();

	this.terminate();
};

// ----------------------------------------------------------------------
// Execute Node Command
// ----------------------------------------------------------------------
function CGActionExecuteNodeCommand() {
	this.base = CGAction;
	this.base(3);
};

CGActionExecuteNodeCommand.prototype = new CGAction;
CGActionExecuteNodeCommand.constructor = CGActionExecuteNodeCommand;
CommandFactory.register(CGActionExecuteNodeCommand, {
	Context: 0,
	Name: 1,
	Parameters: 2
}, false);

CGActionExecuteNodeCommand.prototype.onFailure = function (sResponse) {
	Desktop.hideReports();
	Desktop.reportError(sResponse);
	this.terminate();
};

CGActionExecuteNodeCommand.prototype.titleOf = function (confirmation) {
	if (confirmation.title == null)
		return Lang.ViewNode.DialogExecuteNodeCommand.Title;

	return confirmation.title;
};

CGActionExecuteNodeCommand.prototype.descriptionOf = function (confirmation) {
	if (confirmation.description == null)
		return Lang.ViewNode.DialogExecuteNodeCommand.Description;

	return confirmation.description;
};

CGProcess.prototype.checkConfirmation = function (ButtonResult) {
	if (ButtonResult == BUTTON_RESULT_YES)
		Kernel.executeNodeCommandOnAccept(this, this.Context, this.Name, this.Parameters);
	else
		Kernel.executeNodeCommandOnCancel(this, this.Context, this.Name, this.Parameters);
};

CGActionExecuteNodeCommand.prototype.step_1 = function () {
	var node = NodesCache.get(this.Context);

	if (this.DOMItem.hasClassName(CLASS_DISABLED)) {
		this.terminate();
		return;
	}

	if (this.Name == null)
		return;

	if (node == null)
		return;

	State.discardNode = false;
	Desktop.reportProgress(Lang.Action.ExecuteNodeCommand.Waiting, true);
	Kernel.executeNodeCommand(this, this.Context, this.Name, this.Parameters);
};

CGActionExecuteNodeCommand.prototype.step_2 = function () {
	var response = Ext.util.JSON.decode(this.data);

	if (response.type == "message" && response.data.content == "done") {
		this.execute();
		return;
	}

	var confirmation = response.data;
	if (confirmation == null || !confirmation.required) {
		this.execute();
		return;
	}

	Desktop.hideReports();
	Ext.MessageBox.confirm(this.titleOf(confirmation), this.descriptionOf(confirmation), CGActionExecuteNodeCommand.prototype.checkConfirmation.bind(this));
};

CGActionExecuteNodeCommand.prototype.report = function (message) {
	if (message.content == "")
		return;

	if (message.code == "success") {
		var content = message.content == "done" ? Lang.Action.ExecuteNodeCommand.Done : message.content;
		Desktop.reportSuccess(content);
	}
	else
		Desktop.reportError(message.content);
};

CGActionExecuteNodeCommand.prototype.step_3 = function () {
	var response = Ext.util.JSON.decode(this.data);

	Desktop.hideReports();

	if (response.type == "message") {
		this.report(response.data);
		return;
	}
	else if (response.type == "operation") {
		var operation = response.data;
        CommandDispatcher.execute(operation.name, null, operation.data);
	}
	else Desktop.reportSuccess(Lang.Action.ExecuteNodeCommand.Done);

	var node = NodesCache.getCurrent();
	if (node == null) {
		this.terminate();
		return;
	}

	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, node.getId());
	EventManager.notify(EventManager.POST_EXECUTE_NODE_COMMAND, {
		"Node": node,
		"DOMNode": ViewNode.getDOM(),
		"Command": this.Name,
		"Parameters": this.Parameters
	});

	this.terminate();
};

// ----------------------------------------------------------------------
// Download Node Command File
// ----------------------------------------------------------------------
function CGActionDownloadNodeCommandFile() {
	this.base = CGAction;
	this.base(2);
};

CGActionDownloadNodeCommandFile.prototype = new CGAction;
CGActionDownloadNodeCommandFile.constructor = CGActionDownloadNodeCommandFile;
CommandFactory.register(CGActionDownloadNodeCommandFile, {
	Name: 0
}, false);

CGActionDownloadNodeCommandFile.prototype.step_1 = function () {
	window.location.href = Kernel.getDownloadNodeCommandFileLink(this.Name);
};

// ----------------------------------------------------------------------
// Request Node Field Control
// ----------------------------------------------------------------------
function CGActionRequestNodeFieldControl() {
	this.base = CGAction;
	this.base(2);
	this.sFieldPath = null;
};

CGActionRequestNodeFieldControl.prototype = new CGAction;
CGActionRequestNodeFieldControl.constructor = CGActionRequestNodeFieldControl;
CommandFactory.register(CGActionRequestNodeFieldControl, null, false);

CGActionRequestNodeFieldControl.prototype.step_1 = function () {
	var DOMNode = this.DOMItem.up(CSS_NODE);
	var DOMComponent = this.DOMItem.up(CSS_WIDGET).down(CSS_WIDGET_ELEMENT_COMPONENT);
	if (!DOMComponent)
		return;
	this.sFieldPath = DOMComponent.id;
	Kernel.blurNodeField(this, Account.getInstanceId(), DOMNode.getControlInfo().IdNode, this.sFieldPath);
};

CGActionRequestNodeFieldControl.prototype.step_2 = function () {
	var Widget = WidgetManager.get(this.sFieldPath);
	Widget.setObserver(null, 0);
	Widget.focus();
	this.terminate();
};

// ----------------------------------------------------------------------
// Show node view
// ----------------------------------------------------------------------
function CGActionShowNodeView() {
	this.base = CGAction;
	this.base(2);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionShowNodeView.prototype = new CGAction;
CGActionShowNodeView.constructor = CGActionShowNodeView;
CommandFactory.register(CGActionShowNodeView, {
	IdMainNode: 0,
	IdNode: 1,
	IdView: 2,
	GroupByCode: 3,
	GroupByValue: 4
}, true);

CGActionShowNodeView.prototype.step_1 = function () {
	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.IdMainNode);
	var DOMNode = ViewNode.getDOM();

	DOMNode.activateTab(this.IdView);
	window.setTimeout(this.execute.bind(this), 200);
};

CGActionShowNodeView.prototype.step_2 = function () {

	if (State.NodeListViewer) {
		var ListViewerState = State.getListViewerState(this.IdNode);
        if (ListViewerState == null) {
            this.terminate();
            return;
        }
		ListViewerState.Groups = [
			{
				"Code": this.GroupByCode,
				"Value": this.GroupByValue
			}
		];
		State.NodeListViewer.setState(ListViewerState);
		State.NodeListViewer.refresh();
	}

	this.terminate();
};

// ----------------------------------------------------------------------
// Print node
// ----------------------------------------------------------------------
function CGActionPrintNode() {
	this.base = CGAction;
	this.base(6);
	this.allAttributes = new Array();
};

CGActionPrintNode.prototype = new CGAction;
CGActionPrintNode.constructor = CGActionPrintNode;
CommandFactory.register(CGActionPrintNode, {
	IdNode: 0,
	Mode: 1,
	CodeView: 2
}, false);

CGActionPrintNode.prototype.getPrintNodeCookieName = function () {
	return "monet-printnode-" + this.IdNode;
};

CGActionPrintNode.prototype.getAttributes = function () {
	if (this.Mode == "pdf")
		return CGActionPrintNode.Attributes;

	var result = new Array();
	for (var i=0; i<this.allAttributes.length; i++)
		result.push(this.allAttributes[i].code);

	return result;
};

CGActionPrintNode.prototype.getCodeView = function () {

	if (this.CodeView != null && this.CodeView != "")
		return this.CodeView;

    var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.IdNode);
	var CodeView;

	if (ViewNode == null) {
		var aViews = Desktop.Main.Center.Body.getViews(VIEW_NODE, VIEW_NODE_TYPE_NODE, this.IdNode);
		if (aViews.length == 0) {
			this.terminate();
			return;
		}

		ViewNode = aViews[0];
		CodeView = ViewNode.getDOM().getControlInfo().CodeView;
	}
	else CodeView = ViewNode.getDOM().getActiveTab();

	return CodeView;
};

CGActionPrintNode.prototype.saveDialogResult = function () {
	this.savePrintNodeCookie();
	CGActionPrintNode.Attributes = this.dialog.Attributes;
	CGActionPrintNode.DateAttribute = this.dialog.DateAttribute;
	CGActionPrintNode.FromDate = this.dialog.FromDate;
	CGActionPrintNode.ToDate = this.dialog.ToDate;
};

CGActionPrintNode.prototype.loadPrintNodeCookie = function() {
    var value = getCookie(this.getPrintNodeCookieName());
    return value != null && value != "" ? JSON.parse(value) : null;
};

CGActionPrintNode.prototype.savePrintNodeCookie = function() {
    var value = {
        attributes : this.dialog.Attributes,
        dateAttribute : this.dialog.DateAttribute,
        fromDate : this.dialog.FromDate,
        toDate : this.dialog.ToDate
    };
    setCookie(this.getPrintNodeCookieName(), JSON.stringify(value), 30);
};

CGActionPrintNode.prototype.destroy = function (sResponse) {
	if (this.dialog) this.dialog.destroy();
	this.terminate();
};

CGActionPrintNode.prototype.step_1 = function () {
    var value = this.loadPrintNodeCookie();
    CGActionPrintNode.Attributes = value != null && value.attributes != null ? value.attributes : null;
    CGActionPrintNode.DateAttribute = value != null && value.dateAttribute != null ? value.dateAttribute : null;
    CGActionPrintNode.FromDate = value != null && value.fromDate != null ? value.fromDate : new Date(new Date().getFullYear(), 0, 1);
    CGActionPrintNode.ToDate = value != null && value.toDate != null ? value.toDate : new Date();
	Kernel.loadNodePrintAttributes(this, this.IdNode, this.getCodeView());
};

CGActionPrintNode.prototype.step_2 = function () {
	var extItem = Ext.get(this.DOMItem);
	this.allAttributes = Ext.util.JSON.decode(this.data);

	this.dialogLayerId = Ext.id();
	new Insertion.After(extItem.dom, "<div class='dialog embedded' id='" + this.dialogLayerId + "'></div>");

	this.dialog = new CGDialogPrintEntity();
	this.dialog.onAccept = this.execute.bind(this);
	this.dialog.onCancel = this.destroy.bind(this);
	this.dialog.Attributes = CGActionPrintNode.Attributes != null ? CGActionPrintNode.Attributes : new Array();
	this.dialog.DateAttribute = CGActionPrintNode.DateAttribute;
	this.dialog.FromDate = CGActionPrintNode.FromDate;
	this.dialog.ToDate = CGActionPrintNode.ToDate;
	this.dialog.target = { Attributes: this.allAttributes, Mode : this.Mode };
	this.dialog.init(this.dialogLayerId);
	this.dialog.show();
};

CGActionPrintNode.prototype.step_3 = function () {
    var Filters = new Object();

    $(this.dialogLayerId).remove();

    if ((this.IdNode == null) || (this.Mode == null)) {
        this.terminate();
        return;
    }

    this.saveDialogResult();

    this.CodeView = this.getCodeView();
    this.Filters = State.getListViewerFilters(this.IdNode + this.CodeView);
    this.Attributes = this.getAttributes();

    Kernel.printNodeTimeConsumption(this, this.IdNode, this.Mode, this.CodeView, this.Filters, this.Attributes, CGActionPrintNode.DateAttribute, CGActionPrintNode.FromDate, CGActionPrintNode.ToDate, Account.getInstanceId());
};

CGActionPrintNode.prototype.step_4 = function () {
	var timeConsumption = this.data == "true" ? true : false;

	if (!timeConsumption) {
		this.execute();
		return;
    }

    var PrintNodeMsgs = Lang.Action.PrintNode;
    Ext.MessageBox.confirm(PrintNodeMsgs.TimeConsumption.Title, PrintNodeMsgs.TimeConsumption.Description, CGActionPrintNode.prototype.checkOption.bind(this));
};

CGActionPrintNode.prototype.step_5 = function () {
    Kernel.printNode(this, this.IdNode, this.Mode, this.CodeView, this.Filters, this.Attributes, CGActionPrintNode.DateAttribute, CGActionPrintNode.FromDate, CGActionPrintNode.ToDate, Account.getInstanceId());
};

CGActionPrintNode.prototype.step_6 = function () {
	Desktop.reportProgress(Lang.Action.PrintNode.Waiting);
	window.setTimeout(function() { Desktop.hideProgress(); }, 300000);
	this.terminate();
};

// ----------------------------------------------------------------------
// Print node result
// ----------------------------------------------------------------------
function CGActionDownloadPrintedNode() {
    this.base = CGAction;
    this.base(1);
};

CGActionDownloadPrintedNode.prototype = new CGAction;
CGActionDownloadPrintedNode.constructor = CGActionDownloadPrintedNode;
CommandFactory.register(CGActionDownloadPrintedNode, {
    Id: 0,
    Template: 1
}, false);

CGActionDownloadPrintedNode.prototype.step_1 = function () {
    Desktop.hideProgress();
	if (CGActionDownloadPrintedNode.downloading) return;
	CGActionDownloadPrintedNode.downloading = true;
	window.location = Kernel.getDownloadPrintedNodeLink(this.Id, this.Template);
    window.setTimeout(function() { CGActionDownloadPrintedNode.downloading = false; }, 2000);
	this.terminate();
};

// ----------------------------------------------------------------------
// Alert Node
// ----------------------------------------------------------------------
function CGActionAlertNode() {
	this.base = CGAction;
	this.base(3);
	this.AvailableProcessClass = CGProcessCleanDirty;
	this.RefreshProcessClass = CGProcessRefreshDOM;
	this.Dialog = null;
};

CGActionAlertNode.prototype = new CGAction;
CGActionAlertNode.constructor = CGActionAlertNode;
CommandFactory.register(CGActionAlertNode, {
	Id: 0
}, false);

CGActionAlertNode.prototype.onFailure = function (sResponse) {
	if (this.Dialog)
		this.Dialog.destroy();
	Desktop.reportError(sResponse);
	this.terminate();
};

CGActionAlertNode.prototype.step_1 = function () {
	this.Dialog = new CGDialogAlertEntity();
	this.Dialog.init();
	this.Dialog.onAccept = this.execute.bind(this);
	this.Dialog.onCancel = this.resetState.bind(this);
	this.Dialog.show();
};

CGActionAlertNode.prototype.step_2 = function () {
	Kernel.alertEntity(this, this.Id, this.Dialog.UserList, this.Dialog.Message, MONET_LINK_TYPE_NODE);
	this.Dialog.destroy();
};

CGActionAlertNode.prototype.step_3 = function () {
	Desktop.reportSuccess(Lang.Action.AlertNode.Done);
	this.terminate();
};

// ----------------------------------------------------------------------
// Render Map Layer
// ----------------------------------------------------------------------
function CGActionRenderMapLayer() {
	this.base = CGAction;
	this.base(2);
	this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderMapLayer.prototype = new CGAction;
CGActionRenderMapLayer.constructor = CGActionRenderMapLayer;
CommandFactory.register(CGActionRenderMapLayer, {
	Id: 0,
	Code: 1,
	CodeView: 2,
	IdDOMViewerLayer: 3,
	IdDOMViewerLayerOptions: 4,
	IdDOMFiltersWizardOptions: 5
}, false);

CGActionRenderMapLayer.prototype.step_1 = function () {

	Desktop.Main.Right.registerCollapse("CGActionRenderMapLayer", CGActionRenderMapLayer.prototype.atCollapse.bind(this));

	if ((this.Id == null) || (this.Code == null) || (this.CodeView == null) || (this.IdDOMViewerLayer == null) || (this.IdDOMViewerLayerOptions == null)) {
		this.terminate();
		return;
	}

	var Process = new CGProcessLoadHelperMapViewer();
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionRenderMapLayer.prototype.step_2 = function () {
	this.createFiltersWizard();
	this.createMapLayer();

	var helper = ViewerSidebar.getHelper(Helper.MAP);
	helper.hideEditionLayer();
};

CGActionRenderMapLayer.prototype.createFiltersWizard = function () {
	var Options;

	eval($(this.IdDOMFiltersWizardOptions).innerHTML);

	var filtersWizard = new CGFiltersWizard(Options, Context.Config.Language, Context.Config.ImagesPath);
	filtersWizard.setDataSourceUrls(Kernel.getNodeItemsLink(this.Id, this.Code, this.CodeView), Kernel.getNodeGroupByOptionsLink(this.Id, this.CodeView));
	filtersWizard.setWizardLayer(Literals.ListViewerWizard);
	filtersWizard.onUpdateState = CGActionRenderMapLayer.prototype.atUpdateState.bind(this);
	filtersWizard.setState(State.getListViewerState(this.Id + this.CodeView));
	filtersWizard.render(this.IdDOMViewerLayer);
	filtersWizard.Id = this.Id;
};

CGActionRenderMapLayer.prototype.createMapLayer = function () {
	var extMapLayer = Ext.get(this.IdDOMViewerLayer);
	var extNodeLayer = extMapLayer.up(CSS_NODE);
	var Options;

	eval($(this.IdDOMViewerLayerOptions).innerHTML);
	extMapLayer.setHeight(extNodeLayer.getHeight());

	this.view = new CGViewMapLayer();
	this.view.setCenter(Context.Config.DefaultLocation.Latitude, Context.Config.DefaultLocation.Longitude);
	this.view.setDOMLayer(extMapLayer.dom);
	this.view.init(Options);
	this.view.setTarget({ countUrl: this.getSourceCountUrl(), url: this.getSourceUrl() });
	this.view.render();
};

CGActionRenderMapLayer.prototype.atUpdateState = function (NewState) {
	State.registerListViewerState(this.Id + this.CodeView, NewState);
	State.registerListViewerState(this.Id, NewState);

	this.view.setTarget({ countUrl: this.getSourceCountUrl(), url: this.getSourceUrl() });
	this.view.refresh();
};

CGActionRenderMapLayer.prototype.getSourceCountUrl = function () {
    var state = State.getListViewerState(this.Id + this.CodeView);
    return Kernel.getNodeItemsLocationsCountLink(this.Id, this.Code, this.CodeView, (state != null ? state.toEscapeUrl() : ""));
};

CGActionRenderMapLayer.prototype.getSourceUrl = function () {
	var state = State.getListViewerState(this.Id + this.CodeView);
	return Kernel.getNodeItemsLocationsLink(this.Id, this.Code, this.CodeView, (state != null ? state.toEscapeUrl() : ""));
};

CGActionRenderMapLayer.prototype.atCollapse = function () {
	this.view.resize();
};

// ----------------------------------------------------------------------
// Show link node
// ----------------------------------------------------------------------
function CGActionShowLinkNode() {
	this.base = CGAction;
	this.base(1);
};

CGActionShowLinkNode.prototype = new CGAction;
CGActionShowLinkNode.constructor = CGActionShowLinkNode;
CommandFactory.register(CGActionShowLinkNode, {
	IdNode: 0,
	IdLink: 1
}, true);

CGActionShowLinkNode.prototype.step_1 = function () {
	var Process = new CGProcessShowLinkNode();
	Process.IdNode = this.IdNode;
	Process.IdLink = this.IdLink;
	Process.execute();
	this.terminate();
};

// ----------------------------------------------------------------------
// Edit node document
// ----------------------------------------------------------------------
function CGActionEditNodeDocument() {
	this.base = CGAction;
	this.base(4);
};

CGActionEditNodeDocument.prototype = new CGAction;
CGActionEditNodeDocument.constructor = CGActionEditNodeDocument;
CommandFactory.register(CGActionEditNodeDocument, {
	Id: 0,
	IdDOMLayer: 1
}, false);

CGActionEditNodeDocument.prototype.onFailure = function (sResponse) {
	Desktop.reportError(this.getErrorMessage(Lang.Action.EditNodeDocument.Failure, sResponse));
};

CGActionEditNodeDocument.prototype.close = function () {
	this.gotoStep(4);
};

CGActionEditNodeDocument.prototype.step_1 = function () {
	var Node = NodesCache.get(this.Id);
	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.Id);
	var aPreferences = Account.getUser().getInfo().getPreferences();
	var bDownloadPreference = aPreferences["DownloadOnEditDocument"] ? aPreferences["DownloadOnEditDocument"] == "true" : false;
	var bEditPreference = aPreferences["GotoEditOnEditDocument"] ? aPreferences["GotoEditOnEditDocument"] == "true" : false;

	if (!Node || !ViewNode) {
		this.terminate();
		return;
	}

	this.dialog = new CGDialogEditNodeDocument();
	this.dialog.init($(this.IdDOMLayer));
	this.dialog.Target = {
		IdNode: this.Id,
		DownloadPreference: bDownloadPreference,
		EditPreference: bEditPreference
	};
	this.dialog.onAccept = CGActionEditNodeDocument.prototype.execute.bind(this);
	this.dialog.onCancel = CGActionEditNodeDocument.prototype.close.bind(this);
	this.dialog.refresh();
	this.dialog.show();
};

CGActionEditNodeDocument.prototype.step_2 = function () {
	Desktop.reportProgress(Lang.Action.EditNodeDocument.Waiting, true);
	Kernel.replaceNodeDocument(this, this.Id, this.dialog.FileForm);
};

CGActionEditNodeDocument.prototype.step_3 = function () {
	var aPreferences = Account.getUser().getInfo().getPreferences();

	aPreferences["DownloadOnEditDocument"] = this.dialog.DownloadPreference ? "true" : "false";
	aPreferences["GotoEditOnEditDocument"] = this.dialog.EditPreference ? "true" : "false";

	Account.getUser().getInfo().setPreferences(aPreferences);

	Process = new CGProcessSaveAccount();
	Process.Account = Account;
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionEditNodeDocument.prototype.step_4 = function () {
	var Node = NodesCache.get(this.Id);

	Desktop.hideReports();

	var Behaviour = Extension.getDefinitionBehaviour(Node.Code);
	if ((!Behaviour) || (!Behaviour.ShowNode) || (!Behaviour.ShowNode.Templates) || (!Behaviour.ShowNode.Templates.View)) {
		this.terminate();
		return;
	}

	CommandListener.dispatchCommand("shownode(" + this.Id + "," + Behaviour.ShowNode.Templates.View + ")");
	this.terminate();
};

// ----------------------------------------------------------------------
// Sign node document
// ----------------------------------------------------------------------
function CGActionSignNodeDocument() {
	this.base = CGAction;
	this.base(2);
};

CGActionSignNodeDocument.prototype = new CGAction;
CGActionSignNodeDocument.constructor = CGActionSignNodeDocument;
CommandFactory.register(CGActionSignNodeDocument, {
	Id: 0,
	CodeSignature: 1
}, false);

CGActionSignNodeDocument.prototype.close = function () {
	this.resetState();
};

CGActionSignNodeDocument.prototype.step_1 = function () {
	this.process = new CGProcessSignNodeDocument();
	this.process.Id = this.Id;
	this.process.CodeSignature = this.CodeSignature;
	this.process.ReturnProcess = this;
	this.process.execute();
};

CGActionSignNodeDocument.prototype.step_2 = function (sResponse) {
	if (this.process.success())
		CommandListener.throwCommand("refreshnode(" + this.Id + ")");
	else
		Desktop.reportError(Lang.Action.SignNodeDocument.Failure);
	this.terminate();
};

// ----------------------------------------------------------------------
// Render signature list
// ----------------------------------------------------------------------
function CGActionRenderSignatureList() {
	this.base = CGAction;
	this.base(2);
};

CGActionRenderSignatureList.prototype = new CGAction;
CGActionRenderSignatureList.constructor = CGActionRenderSignatureList;
CommandFactory.register(CGActionRenderSignatureList, {
	Id: 0,
	IdDOMViewerLayer: 1,
	IdDOMViewerLayerOptions: 2
}, false);

CGActionRenderSignatureList.prototype.atBoundItem = function (Sender, Item) {
	var Dummy = Item;
	for (var index in Dummy) {
		if (isFunction(Dummy[index]))
			continue;
		Item[index + "_short"] = shortValue(Dummy[index]);
		try {
			Item[index + "_length"] = Dummy[index].length;
		} catch (e) {
		}
	}

	if (Item.state == "pending" && !this.canSign(Item))
		Item.state = "waiting";

	if (Item.state == "pending")
		Item.stateLabel = Lang.ViewNode.SignatureState.Pending;
	else if (Item.state == "waiting")
		Item.stateLabel = Lang.ViewNode.SignatureState.Waiting;
	else if (Item.state == "signed")
		Item.stateLabel = Lang.ViewNode.SignatureState.Signed;
	else if (Item.state == "delayed")
		Item.stateLabel = Lang.ViewNode.SignatureState.Delayed;
	else if (Item.state == "disabled")
		Item.stateLabel = Lang.ViewNode.SignatureState.Disabled;
	else if (Item.state == "signing")
		Item.stateLabel = Lang.ViewNode.SignatureState.Signing;

	Item.restricted = Item.users_count > 0 ? 1 : 0;
	Item.css = Item.state;
	Item.date = getFormattedDateTime(parseServerDate(Item.date), Context.Config.Language, false);
};

CGActionRenderSignatureList.prototype.atShowItem = function (ListViewer, Item) {
	if (this.canSign(Item)) {
		Item.state = "signing";
		ListViewer.updateItem(Item);
		CommandListener.throwCommand("signnodedocument(" + this.Id + "," + Item.code + ")");
	}
};

CGActionRenderSignatureList.prototype.canSign = function (Item) {
	var userId = Account.getUser().getId();

	if (Item.users_count > 0 && Item.users_ids.indexOf("#" + userId + "#") == -1)
		return false;

	return Item.state == "pending";
};

CGActionRenderSignatureList.prototype.destroyViewer = function () {
	if (State.SignatureListViewer == null)
		return;
	State.SignatureListViewer.dispose();
	$(Literals.ListViewerWizard).innerHTML = "";
};

CGActionRenderSignatureList.prototype.createViewer = function () {
	var Options;

	this.destroyViewer();

	DOMNode = $(this.IdDOMViewerLayer).up(CSS_NODE);
	eval($(this.IdDOMViewerLayerOptions).innerHTML);

	State.SignatureListViewer = new CGListViewer(Options, Context.Config.Language, Context.Config.ImagesPath);
	State.SignatureListViewer.setDataSourceUrls(Kernel.getSignatureItemsLink(this.Id), null);
	State.SignatureListViewer.setWizardLayer(Literals.ListViewerWizard);
	State.SignatureListViewer.onShowItem = CGActionRenderSignatureList.prototype.atShowItem.bind(this);
	State.SignatureListViewer.onBoundItem = CGActionRenderSignatureList.prototype.atBoundItem.bind(this);
	State.SignatureListViewer.render(this.IdDOMViewerLayer);
	State.SignatureListViewer.Id = this.Id;
};

CGActionRenderSignatureList.prototype.step_1 = function () {

	if ((this.Id == null) || (this.IdDOMViewerLayer == null) || (this.IdDOMViewerLayerOptions == null)) {
		this.terminate();
		return;
	}

	var Process = new CGProcessLoadHelperListViewer();
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionRenderSignatureList.prototype.step_2 = function () {
	this.createViewer();
	this.terminate();
};

// ----------------------------------------------------------------------
// Action Load Node Field Composite Item
// ----------------------------------------------------------------------
function CGActionLoadNodeFieldCompositeItem() {
	this.base = CGAction;
	this.base(2);
};

CGActionLoadNodeFieldCompositeItem.prototype = new CGAction;
CGActionLoadNodeFieldCompositeItem.constructor = CGActionLoadNodeFieldCompositeItem;
CommandFactory.register(CGActionLoadNodeFieldCompositeItem, {
	Id: 0,
	Code: 1,
	Position: 2
}, false);

CGActionLoadNodeFieldCompositeItem.prototype.step_1 = function () {
	var extItem = Ext.get(this.DOMItem);
	var extElement = extItem.up(".element");
	var extList = extElement.up("ul");
	var extField = extElement.select(CSS_FIELD).first();

	if (extList) {
		var extActive = extList.down(".element." + CLASS_ACTIVE);
		if (extActive != null && extActive != extElement) {
			extActive.removeClass(CLASS_ACTIVE);
			var extBox = extActive.select(".box").first();
			extBox.dom.style.display = "none";
			var extReference = extActive.select(".reference").first();
			extReference.dom.style.display = "";
		}
	}

	if (extField != null) {
		if (extElement.hasClass(CLASS_ACTIVE))
			extElement.removeClass(CLASS_ACTIVE);
		else
			extElement.addClass(CLASS_ACTIVE);

		var extBox = extElement.select(".box").first();
		extBox.dom.style.display = extElement.hasClass(CLASS_ACTIVE) ? "" : "none";

		var extReference = extElement.select(".reference").first();
		extReference.dom.style.display = extElement.hasClass(CLASS_ACTIVE) ? "none" : "";

		this.terminate();
		return;
	}

	Kernel.loadNodeFieldCompositeItem(this, this.Id, this.Code, this.Position);
};

CGActionLoadNodeFieldCompositeItem.prototype.step_2 = function () {
	var Field = new CGField();
	Field.unserialize(this.data);

	var extItem = Ext.get(this.DOMItem);
	var extElement = extItem.up(".element");
	var extBox = extElement.select(".box").first();
	var extReference = extElement.select(".reference").first();
	var extClose = extElement.select(".close").first();
	var extContent = extBox.select(".content").first();
	new Insertion.Bottom(extContent.dom, Field.getContent());

	extElement.addClass(CLASS_ACTIVE);
	extBox.dom.style.display = "";
	extReference.dom.style.display = "none";
	extClose.setBottom(0);

	CommandListener.capture(extContent.dom);
};

// ----------------------------------------------------------------------
// Show node revisions
// ----------------------------------------------------------------------
function CGActionShowNodeRevisions() {
	this.base = CGAction;
	this.base(2);
};

CGActionShowNodeRevisions.prototype = new CGAction;
CGActionShowNodeRevisions.constructor = CGActionShowNodeRevisions;
CommandFactory.register(CGActionShowNodeRevisions, {
	Id: 0,
	Template: 1
}, true);

CGActionShowNodeRevisions.prototype.step_1 = function () {
	var Process = new CGProcessShowNodeRevisions();
	Process.Id = this.Id;
	Process.Template = this.Template;
	Process.ReturnProcess = this;
	Process.execute();
};

CGActionShowNodeRevisions.prototype.step_2 = function () {
	var Process = new CGProcessShowNodeRevision();
	Process.IdNode = this.Id;
	Process.Template = this.Template;
	Process.execute();
	this.terminate();
};

// ----------------------------------------------------------------------
// Show node revision
// ----------------------------------------------------------------------
function CGActionShowNodeRevision() {
	this.base = CGAction;
	this.base(1);
};

CGActionShowNodeRevision.prototype = new CGAction;
CGActionShowNodeRevision.constructor = CGActionShowNodeRevision;
CommandFactory.register(CGActionShowNodeRevision, {
	Id: 0,
	IdNode: 1,
	Template: 2,
	ActiveTab: 3
}, true);

CGActionShowNodeRevision.prototype.step_1 = function () {
	var Process = new CGProcessShowNodeRevision();
	Process.Id = this.Id;
	Process.IdNode = this.IdNode;
	Process.Template = this.Template;
	Process.ActiveTab = this.ActiveTab;
	Process.execute();
	this.terminate();
};

// ----------------------------------------------------------------------
// Close node revisions
// ----------------------------------------------------------------------
function CGActionCloseNodeRevisions() {
	this.base = CGAction;
	this.base(1);
};

CGActionCloseNodeRevisions.prototype = new CGAction;
CGActionCloseNodeRevisions.constructor = CGActionCloseNodeRevisions;
CommandFactory.register(CGActionCloseNodeRevisions, {
	IdNode: 0,
	Template: 1
}, false);

CGActionCloseNodeRevisions.prototype.step_1 = function () {
	CommandListener.dispatchCommand("shownode(" + this.IdNode + "," + this.Template + ")");
	this.terminate();
};

// ----------------------------------------------------------------------
// Restore node revision
// ----------------------------------------------------------------------
function CGActionRestoreNodeRevision() {
	this.base = CGAction;
	this.base(2);
};

CGActionRestoreNodeRevision.prototype = new CGAction;
CGActionRestoreNodeRevision.constructor = CGActionRestoreNodeRevision;
CommandFactory.register(CGActionRestoreNodeRevision, {
	Id: 0,
	IdNode: 1,
	Template: 2
}, false);

CGActionRestoreNodeRevision.prototype.step_1 = function () {
	if (this.Id == "-1") this.execute();
	else Kernel.restoreNodeRevision(this, this.Id, this.IdNode, this.Template);
};

CGActionRestoreNodeRevision.prototype.step_2 = function () {
	CommandListener.throwCommand("closenoderevisions(" + this.IdNode + "," + this.Template + ")");
	this.terminate();
};

//----------------------------------------------------------------------
// Save node partner context
//----------------------------------------------------------------------
function CGActionSaveNodePartnerContext() {
	this.base = CGAction;
	this.base(2);
};

CGActionSaveNodePartnerContext.prototype = new CGAction;
CGActionSaveNodePartnerContext.constructor = CGActionSaveNodePartnerContext;
CommandFactory.register(CGActionSaveNodePartnerContext, {
	IdNode: 0,
	Context: 1
}, false);

CGActionSaveNodePartnerContext.prototype.step_1 = function () {
	Kernel.saveNodePartnerContext(this, Account.getInstanceId(), this.IdNode, this.Context);
};

CGActionSaveNodePartnerContext.prototype.step_2 = function () {
	CommandListener.throwCommand("refreshnode(" + this.IdNode + ")");
	this.terminate();
};

//----------------------------------------------------------------------
// Refresh node state
//----------------------------------------------------------------------
function CGActionRefreshNodeState() {
	this.base = CGAction;
	this.base(2);
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionRefreshNodeState.prototype = new CGAction;
CGActionRefreshNodeState.constructor = CGActionRefreshNodeState;
CommandFactory.register(CGActionRefreshNodeState, { IdView: 0 }, false);

CGActionRefreshNodeState.prototype.step_1 = function () {
	var viewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, this.IdView);

	if (!viewNode) {
		this.terminate();
		return;
	}

	var controlInfo = viewNode.getDOM().getControlInfo();

	if (controlInfo.IdNode == null) {
		this.terminate();
		return;
	}

	var nodeId = controlInfo.IdNode;
	var process = new CGProcessRefreshNodeState();
	process.nodeId = nodeId;
	process.ReturnProcess = this;
	process.execute();
};

CGActionRefreshNodeState.prototype.step_2 = function () {
	var viewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, this.IdView);
	var controlInfo = viewNode.getDOM().getControlInfo();
	var nodeId = controlInfo.IdNode;
	controlInfo.State = State.NodesStates[nodeId];
	this.terminate();
};