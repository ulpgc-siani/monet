//----------------------------------------------------------------------
// Process Show Last View
//----------------------------------------------------------------------
function CGProcessShowLastView() {
	this.base = CGProcess;
	this.base(1);
};

CGProcessShowLastView.prototype = new CGProcess;
CGProcessShowLastView.constructor = CGProcessShowLastView;

CGProcessShowLastView.prototype.step_1 = function () {
	window.history.back();
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process Clean dirty
//----------------------------------------------------------------------
function CGProcessCleanDirty() {
	this.base = CGProcess;
	this.base(1);
};

CGProcessCleanDirty.prototype = new CGProcess;
CGProcessCleanDirty.constructor = CGProcessCleanDirty;

CGProcessCleanDirty.prototype.onFailure = function (sResponse) {
	this.terminateOnFailure(sResponse);
};

CGProcessCleanDirty.prototype.saveNode = function (ButtonResult) {
	if (ButtonResult == BUTTON_RESULT_CANCEL) {
		this.terminateOnFailure();
	}
	else if (ButtonResult == BUTTON_RESULT_YES) {
		State.discardNode = false;
		var ActionSaveNode = new CGActionSaveNode();
		ActionSaveNode.Id = State.LastView.getTarget().getId();
		ActionSaveNode.ReturnProcess = this;
		ActionSaveNode.execute();
	}
	else {
		if (State.discardNode) {
			this.discardNode();
		}
		else {
			this.terminateOnSuccess();
		}
	}
};

CGProcessCleanDirty.prototype.discardNode = function () {
	var ActionDiscardNode = new CGActionDiscardNode();
	var Node = State.LastView.getTarget();
	ActionDiscardNode.Id = Node.getId();
	ActionDiscardNode.ReturnProcess = this;
	ActionDiscardNode.execute();
	State.discardNode = false;
};

CGProcessCleanDirty.prototype.step_1 = function () {
	ViewerSidebar.hide();
	this.terminateOnSuccess();
	return; // mario disabled.
};

//----------------------------------------------------------------------
// Show node
//----------------------------------------------------------------------
function CGProcessShowNode() {
	this.base = CGProcess;
	this.base(5);
	this.ActivateNode = true;
	this.Code = null;
	this.DOMViewActiveTab = null;
	this.IdRevision = null;
};

CGProcessShowNode.prototype = new CGProcess;
CGProcessShowNode.constructor = CGProcessShowNode;

CGProcessShowNode.prototype.step_1 = function () {
	var IdNode = (this.Node) ? this.Node.getId() : this.Id;

	this.Id = IdNode;
	EventManager.notify(EventManager.BEFORE_OPEN_NODE, {"IdNode": IdNode});

	if (this.Mode) {
		this.gotoStep(3);
		return;
	}

	if ((this.Node) && (this.Node.Code != null)) {
		this.Code = this.Node.Code;
		this.execute();
		return;
	}

	Kernel.loadNodeType(this, IdNode);
};

CGProcessShowNode.prototype.step_2 = function () {
	if (this.Code == null) this.Code = this.data;

	var Behaviour = Extension.getDefinitionBehaviour(this.Code);

	if ((!Behaviour) || (!Behaviour.ShowNode) || (!Behaviour.ShowNode.Templates) || (!Behaviour.ShowNode.Templates.Edit)) {
		Desktop.hideReports();
		this.terminate();
		return;
	}

	this.Mode = Behaviour.ShowNode.Templates.Edit;
	this.execute();
};

CGProcessShowNode.prototype.step_3 = function () {

	if (this.ViewNode == null) {
		var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.Id);
		if (ViewNode != null) {
			if (((this.Mode == null) || (ViewNode.getMode() == this.Mode)) && (this.ActivateNode)) {
				var Process = new CGProcessActivateNode();
				Process.Id = this.Id;
				Process.execute();
				this.terminate();
				return;
			}
			else {
				ViewNode.destroy();
				ViewNode = null;
			}
		}
		if (ViewNode == null) {
			var IdTab = Desktop.Main.Center.Body.addTab(VIEW_NODE, {Id: this.Id, Background: !this.ActivateNode});
			var Node = null;
			if (!this.Node) {
				Node = new CGNode();
				Node.setId(this.Id);
			}
			else Node = this.Node;
			this.ViewNode = Desktop.createView($(IdTab), Node, null, this.Mode, true);
		}
	}
	else {
		if ((!this.DOMViewActiveTab) && (this.ViewNode) && (this.ViewNode.getDOM) && (this.ViewNode.getDOM().getActiveTab)) this.DOMViewActiveTab = this.ViewNode.getDOM().getActiveTab();
		this.ViewNode.setMode(this.Mode);
	}

	if (!this.Node) {
		if (this.IdRevision == null || this.IdRevision == "-1") Kernel.loadNode(this, this.Id, this.Mode, this.Index, this.Count);
		else Kernel.loadNodeRevision(this, this.IdRevision, this.Id, this.Mode, null);
	}
	else this.gotoStep(5);
};

CGProcessShowNode.prototype.step_4 = function () {

	this.Node = new CGNode();
	this.Node.unserialize(this.data);
	NodesCache.register(this.Node);

	this.ViewNode.setTarget(this.Node);
	this.ViewNode.refresh();

	this.execute();
};

CGProcessShowNode.prototype.step_5 = function () {

	Desktop.Main.Center.Header.refresh();
	if (Desktop.Main.Center.Body.existsTab(VIEW_NODE, this.Node.getId())) {
		Desktop.Main.Center.Body.updateTab(VIEW_NODE, this.Node.getId(), this.Node.getLabel());
		if (this.ActivateNode) Desktop.Main.Center.Body.activateTab(VIEW_NODE, this.Node.getId());
	}

	Desktop.markNodesReferences(this.ViewNode);

	if (this.ActivateNode) {
		if (this.DOMViewActiveTab) this.ViewNode.getDOM().activateTab(this.DOMViewActiveTab);
		else this.ViewNode.getDOM().activateDefaultTab();
	}

	EventManager.notify(EventManager.OPEN_NODE, {"Node": this.Node, "DOMNode": this.ViewNode.getDOM()});

	this.setActiveFurniture(Furniture.DESKTOP, Account.getUser().getRootNode().id);
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Load embedded node
//----------------------------------------------------------------------
function CGProcessLoadEmbeddedNode() {
	this.base = CGProcess;
	this.base(3);
	this.ViewNode = null;
	this.LoadHelperPage = false;
};

CGProcessLoadEmbeddedNode.prototype = new CGProcess;
CGProcessLoadEmbeddedNode.constructor = CGProcessLoadEmbeddedNode;

CGProcessLoadEmbeddedNode.prototype.step_1 = function () {
	var ControlInfo;
	var Node = null;
	var DOMNode = this.DOMNode;

	if (DOMNode == null) DOMNode = this.DOMItem;

	if (!DOMNode.getControlInfo) {
		this.gotoStep(3);
		return;
	}

	ControlInfo = DOMNode.getControlInfo();
	this.ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, DOMNode.IdView);

	if (this.ViewContainer == null) {
		Node = NodesCache.getCurrent();

		if (Node == null) {
			this.gotoStep(3);
			return;
		}

		this.ViewContainer = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId());
	}

	if (!this.ViewNode) {
		Node = new CGNode();
		Node.setId(ControlInfo.IdNode);
		this.ViewNode = Desktop.createView(DOMNode, Node, this.ViewContainer, ControlInfo.Templates.Refresh, true);
	}

	if (this.ViewNode.getDOM().isLoaded()) {
		this.execute();
		return;
	}

	var Process = new CGProcessShowNode();
	Process.Id = Node.getId();
	Process.IdRevision = ControlInfo.IdRevision;
	Process.Mode = ControlInfo.Templates.Refresh;
	Process.ViewNode = this.ViewNode;
	Process.ActivateNode = false;
	Process.ReturnProcess = this;
	Process.execute();
};

CGProcessLoadEmbeddedNode.prototype.step_2 = function () {
	var Process = new CGProcessLoadEmbeddedNodes();
	Process.ViewContainer = this.ViewNode;
	Process.ReturnProcess = this;
	Process.execute();
};

CGProcessLoadEmbeddedNode.prototype.step_3 = function () {
	var DOMNode = this.DOMNode;

	if (this.onFinish) this.onFinish();

	if (DOMNode == null) DOMNode = this.DOMItem;

	if (DOMNode.getControlInfo() && this.LoadHelperPage)
		CommandListener.throwCommand("loadnodehelperpage(" + DOMNode.getControlInfo().Code + ")");

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Load embedded nodes
//----------------------------------------------------------------------
function CGProcessLoadEmbeddedNodes() {
	this.base = CGProcess;
	this.base(3);
	this.aDOMEmbeddedNodes = null;
	this.Index = 0;
	this.LoadHelperPage = false;
};

CGProcessLoadEmbeddedNodes.prototype = new CGProcess;
CGProcessLoadEmbeddedNodes.constructor = CGProcessLoadEmbeddedNodes;

CGProcessLoadEmbeddedNodes.prototype.step_1 = function () {
	var DOMContainer;

	if (!this.ViewContainer) return;

	DOMContainer = this.ViewContainer.getDOM();

	if (!DOMContainer) {
		this.terminateOnFailure();
		return;
	}

	if (!DOMContainer.getEmbeddedNodes) {
		this.terminateOnSuccess();
		return;
	}

	this.aDOMEmbeddedNodes = DOMContainer.getEmbeddedNodes();
	this.Index = 0;
	this.execute();
};

CGProcessLoadEmbeddedNodes.prototype.step_2 = function () {
	if (this.aDOMEmbeddedNodes[this.Index]) {
		var Process = new CGProcessLoadEmbeddedNode();
		Process.ViewContainer = this.ViewContainer;
		Process.DOMNode = this.aDOMEmbeddedNodes[this.Index];
		Process.LoadHelperPage = this.LoadHelperPage;
		Process.ReturnProcess = this;
		Process.execute();
	}
	else this.terminateOnSuccess();
};

CGProcessLoadEmbeddedNodes.prototype.step_3 = function () {
	this.Index++;
	if (this.Index < this.aDOMEmbeddedNodes.length) this.gotoStep(2);
	else this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Save node
//----------------------------------------------------------------------
function CGProcessSaveNode() {
	this.base = CGProcess;
	this.base(4);
	this.AttributeList = new CGAttributeList();
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGProcessSaveNode.prototype = new CGProcess;
CGProcessSaveNode.constructor = CGProcessSaveNode;

CGProcessSaveNode.prototype.onFailure = function (sResponse) {
	this.terminateOnFailure(sResponse);
};

CGProcessSaveNode.prototype.step_1 = function () {
	var ViewNode;
	var Node;

	if ((Node = NodesCache.get(this.Id)) == false) {
		this.terminate();
		return;
	}

	ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId());
	if (ViewNode == null) {
		this.terminate();
		return;
	}

	var Process = new CGProcessSaveEmbeddedNodes();
	Process.ViewContainer = ViewNode;
	Process.ReturnProcess = this;
	Process.execute();
};

CGProcessSaveNode.prototype.step_2 = function () {
	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.Id);
	var Process = new CGProcessSaveFieldNodes();
	Process.ViewContainer = ViewNode;
	Process.ReturnProcess = this;
	Process.execute();
};

CGProcessSaveNode.prototype.step_3 = function () {
	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.Id);
	var DOMNode = ViewNode.getDOM();
	var sContent;

	if (!DOMNode.getContent) {
		this.terminate();
		return;
	}

	sContent = DOMNode.getContent();

	if (sContent == "") this.execute();
	else Kernel.saveNode(this, this.Id, sContent, null);
};

CGProcessSaveNode.prototype.step_4 = function () {
	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.Id);
	var Node = NodesCache.get(this.Id);

	if (ViewNode.getDOM().setDirty) ViewNode.getDOM().setDirty(false);

	var aDOMNodesReferencesInfo = ViewNode.getDOM().getNodesReferencesInfo();
	for (var iPos = 0; iPos < aDOMNodesReferencesInfo.length; iPos++) {
		var DOMNodeReferenceInfo = aDOMNodesReferencesInfo[iPos];
		var NodeReference = NodesCache.get(DOMNodeReferenceInfo.idNode);
		if (NodeReference) this.addRefreshTask(RefreshTaskType.Descriptors, NodeReference);
	}

	this.addRefreshTask(RefreshTaskType.Forms, Node, ViewNode);

	IdParent = Node.getIdParent();
	while ((IdParent != null) && (IdParent != "-1") && (IdParent != "")) {
		var ParentNode = new CGNode();
		ParentNode.setId(IdParent);
		this.addRefreshTask(RefreshTaskType.References, ParentNode);
		ParentNode = NodesCache.get(IdParent);
		IdParent = (ParentNode) ? ParentNode.getIdParent() : null;
	}

	EventManager.notify(EventManager.SAVE_NODE, {"Node": Node, "DOMNode": ViewNode.getDOM()});

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Save embedded nodes
//----------------------------------------------------------------------
function CGProcessSaveEmbeddedNodes() {
	this.base = CGProcess;
	this.base(3);
	this.Index = 0;
	this.aDOMEmbeddedNodes = null;
	this.CurrentNode = null;
};

CGProcessSaveEmbeddedNodes.prototype = new CGProcess;
CGProcessSaveEmbeddedNodes.constructor = CGProcessSaveEmbeddedNodes;

CGProcessSaveEmbeddedNodes.prototype.step_1 = function () {
	var DOMNode;

	if (!this.ViewContainer) {
		this.terminateOnFailure();
		return;
	}

	DOMNode = this.ViewContainer.getDOM();
	if ((!DOMNode) || (!DOMNode.getEmbeddedNodes)) {
		this.terminateOnSuccess();
		return;
	}

	if (DOMNode.getEditableEmbeddedNodes)
		this.aDOMEmbeddedNodes = DOMNode.getEditableEmbeddedNodes();
	else
		this.aDOMEmbeddedNodes = DOMNode.getEmbeddedNodes();

	if (this.aDOMEmbeddedNodes.length == 0) this.terminate();
	else this.execute();
};

CGProcessSaveEmbeddedNodes.prototype.step_2 = function () {

	var DOMEmbeddedNode = this.aDOMEmbeddedNodes[this.Index];
	if ((!DOMEmbeddedNode.getControlInfo) || (DOMEmbeddedNode.IdView == null)) this.gotoStep(3);
	if (!DOMEmbeddedNode.isEditable()) this.gotoStep(3);

	var ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, DOMEmbeddedNode.IdView);
	if (!ViewNode.getDOM().getContent) this.gotoStep(3);

	this.CurrentNode = ViewNode.getTarget();

	Kernel.saveEmbeddedNode(this, this.CurrentNode.getId(), ViewNode.getDOM().getContent(), null);
};

CGProcessSaveEmbeddedNodes.prototype.step_3 = function () {
	this.Index++;
	if (this.Index < this.aDOMEmbeddedNodes.length) this.gotoStep(2);
	else this.terminate();
};

//----------------------------------------------------------------------
// Save Field nodes
//----------------------------------------------------------------------
function CGProcessSaveFieldNodes() {
	this.base = CGProcess;
	this.base(3);
	this.Index = 0;
	this.aDOMFieldNodes = null;
};

CGProcessSaveFieldNodes.prototype = new CGProcess;
CGProcessSaveFieldNodes.constructor = CGProcessSaveFieldNodes;

CGProcessSaveFieldNodes.prototype.step_1 = function () {
	var DOMNode;

	if (!this.ViewContainer) {
		this.terminateOnFailure();
		return;
	}

	DOMNode = this.ViewContainer.getDOM();
	if ((!DOMNode) || (!DOMNode.getFieldNodes)) {
		this.terminateOnSuccess();
		return;
	}

	this.aDOMFieldNodes = DOMNode.getFieldNodes();

	if (this.aDOMFieldNodes.length == 0) this.terminate();
	else this.execute();
};

CGProcessSaveFieldNodes.prototype.step_2 = function () {
	var ControlInfo;

	DOMFieldNode = this.aDOMFieldNodes[this.Index];
	if (!DOMFieldNode.getControlInfo) this.gotoStep(3);

	ControlInfo = DOMFieldNode.getControlInfo();

	Kernel.saveEmbeddedNode(this, ControlInfo.IdNode, DOMFieldNode.getContent(), null);
};

CGProcessSaveFieldNodes.prototype.step_3 = function () {
	this.Index++;
	if (this.Index < this.aDOMFieldNodes.length) this.gotoStep(2);
	else this.terminate();
};

//----------------------------------------------------------------------
// Activate node
//----------------------------------------------------------------------
function CGProcessActivateNode() {
	this.base = CGProcess;
	this.base(1);
	this.NotifyFocus = true;
};

CGProcessActivateNode.prototype = new CGProcess;
CGProcessActivateNode.constructor = CGProcessActivateNode;

CGProcessActivateNode.prototype.step_1 = function () {
	var CurrentNode, Node = null;
	var CurrentViewNode, ViewNode, DOMNode;
	var bRememberTab = false;

	if (!(Node = NodesCache.get(this.Id))) {
		this.terminateOnFailure();
		return;
	}

	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId())) == null) {
		this.terminateOnFailure();
		return;
	}

	if (Node.isPrototype()) {
		Desktop.showBanner();
		State.isShowingPrototype = true;
	}
	else {
		if (State.isShowingPrototype) {
			Desktop.hideBanner();
			State.isShowingPrototype = false;
		}
	}

	if (NodesCache.getCurrent() != null) {
		CurrentNode = NodesCache.getCurrent();
		CurrentViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, CurrentNode.getId());
		if (CurrentViewNode != null) {
			var Definition = Extension.getDefinition(Node.Code);
			var CurrentDOMNode = CurrentViewNode.getDOM();
			bRememberTab = (Definition.isEnvironment() || (CurrentDOMNode.getId() == this.Id) || CurrentDOMNode.isAncestor(this.Id));
		}
		else bRememberTab = true;
	}

	NodesCache.setCurrent(this.Id);

	Desktop.Main.Center.Header.refresh();
	Desktop.Footer.refresh();

	Desktop.Main.Center.Body.disableNotifications();
	Desktop.Main.Center.Body.activateTab(VIEW_NODE, this.Id);
	Desktop.Main.Center.Body.enableNotifications();

	DOMNode = ViewNode.getDOM();

	if (DOMNode.getCollections) {
		aDOMCollections = DOMNode.getCollections();
		for (var iPos = 0; iPos < aDOMCollections.length; iPos++) {
			aDOMCollections[iPos].style.visibility = "";
		}
	}

	if (DOMNode.getNodesReferencesCount) Node.NodeList.setCount(DOMNode.getNodesReferencesCount());

	Desktop.markNodesReferences(ViewNode);

	this.setActiveFurniture(Furniture.DESKTOP, Account.getUser().getRootNode().id);

	ViewNode.show();
	DOMNode = ViewNode.getDOM();

	var ActiveTabId = this.DOMViewActiveTab ? this.DOMViewActiveTab : DOMNode.getActiveTab();
	if (ActiveTabId && bRememberTab) DOMNode.activateTab(ActiveTabId);
	else DOMNode.activateDefaultTab();

	EventManager.notify(EventManager.OPEN_NODE, {"Node": Node, "DOMNode": ViewNode.getDOM()});
	if (!DOMNode.hasTabs() || (DOMNode.hasTabs() && ViewNode.getContainer() == null)) EventManager.notify(EventManager.FOCUS_NODE, {"Node": Node, "DOMNode": ViewNode.getDOM()});

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Close node
//----------------------------------------------------------------------
function CGProcessCloseNode() {
	this.base = CGProcess;
	this.base(1);
};

CGProcessCloseNode.prototype = new CGProcess;
CGProcessCloseNode.constructor = CGProcessCloseNode;

CGProcessCloseNode.prototype.step_1 = function () {
	var IdNode = null;
	var Node = NodesCache.get(this.Id);

	if ((ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.Id)) == null) {
		this.terminateOnSuccess();
		return;
	}

	EventManager.notify(EventManager.CLOSE_NODE, {"Node": Node, "DOMNode": ViewNode.getDOM()});
	if (ViewNode.getContainer() == null) EventManager.notify(EventManager.BLUR_NODE, {"Node": Node, "DOMNode": ViewNode.getDOM()});

	ViewNode.destroy();
	Desktop.Main.Center.Body.deleteView(VIEW_NODE, ViewNode.getId());
	Desktop.Main.Center.Body.deleteTab(VIEW_NODE, this.Id);
	NodesCache.unregister(this.Id);

	State.deleteSelectedNodesReferences(IdNode);

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Refresh node type dialog
//----------------------------------------------------------------------
function CGProcessRefreshNodeTypeDialog() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessRefreshNodeTypeDialog.prototype = new CGProcess;
CGProcessRefreshNodeTypeDialog.constructor = CGProcessRefreshNodeTypeDialog;

CGProcessRefreshNodeTypeDialog.prototype.step_1 = function () {
	var Node = NodesCache.getCurrent();
	var Behaviour;

	if (!this.Dialog) {
		this.terminateOnFailure();
		return;
	}

	if (Node.getId() == ID_NODE_SEARCH) Node = NodesCache.get(State.LastObject.Id);

	Behaviour = Extension.getDefinitionBehaviour(Node.Code);
	if ((!Behaviour) || (!Behaviour.ShowNode) || (!Behaviour.ShowNode.Templates) || (!Behaviour.ShowNode.Templates.Edit)) return false;

	Kernel.loadNodeTemplate(this, this.Dialog.NodeType.Code, Behaviour.ShowNode.Templates.Edit);
};

CGProcessRefreshNodeTypeDialog.prototype.step_2 = function () {
	var IdDummyView = Ext.id();
	var Node, Behaviour;

	Node = new CGNode();
	Node.unserialize(this.data);

	Behaviour = Extension.getDefinitionBehaviour(Node.Code);
	createLayer(IdDummyView, EMPTY, $(Literals.NodesContainer));
	DummyView = Desktop.createView($(IdDummyView), Node, null, Behaviour.ShowNode.Templates.Edit, true);

	this.Dialog.Target.Definition = DummyView.getDOM().getDefinition();
	this.Dialog.refresh();

	$(IdDummyView).remove();
	Desktop.Main.Center.Body.deleteView(VIEW_NODE, DummyView.getId());
};

//----------------------------------------------------------------------
// Add node blank
//----------------------------------------------------------------------
function CGProcessAddNodeBlank() {
	this.base = CGProcess;
	this.base(3);
	this.ActivateNode = true;
	this.IsPrototype = false;
	this.IdParent = null;
	this.Shared = false;
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGProcessAddNodeBlank.prototype = new CGProcess;
CGProcessAddNodeBlank.constructor = CGProcessAddNodeBlank;

CGProcessAddNodeBlank.prototype.onFailure = function (sResponse) {
	if (this.ActivateNode) Desktop.hideProgress();
	this.terminateOnFailure(sResponse);
};

CGProcessAddNodeBlank.prototype.step_1 = function () {
	var sCaption = Extension.getDefinition(this.Code).Caption;

	if (this.IdParent == null) {
		this.IdParent = NodesCache.getCurrent().getId();
	}
	if (this.IdParent == ID_NODE_SEARCH) {
		this.IdParent = NodesCache.get(State.LastObject.Id).getId();
	}

	if (this.ActivateNode) Desktop.reportProgress(this.generateMessage(Lang.Process.AddNodeBlank.Waiting, {caption: sCaption}), true);

	if (!this.Mode) {
		var Behaviour = Extension.getDefinitionBehaviour(this.Code);

		if ((!Behaviour) || (!Behaviour.AddNode) || (!Behaviour.AddNode.Templates) || (!Behaviour.AddNode.Templates.Edit)) {
			this.terminateOnFailure();
			return;
		}

		this.Mode = Behaviour.AddNode.Templates.Edit;
	}

	if (this.IsPrototype) Kernel.addPrototype(this, this.Code, this.Shared ? null : this.IdParent, this.Mode);
	else Kernel.addNode(this, this.Code, this.IdParent, this.Mode);
};

CGProcessAddNodeBlank.prototype.step_2 = function () {
	this.Result = new CGNode();
	this.Result.unserialize(this.data);
	NodesCache.register(this.Result);

	State.aMarkedNodesReferences = [this.Result.getId()];
	State.NodeReferenceMarkType = MarkType.Added;

	if (this.ActivateNode) {
		var ActionShowNode = new CGActionShowNode();
		ActionShowNode.Id = this.Result.getId();
		ActionShowNode.Mode = this.Mode;
		ActionShowNode.ReturnProcess = this;
		ActionShowNode.execute();
	}
	else {
		this.execute();
	}
};

CGProcessAddNodeBlank.prototype.step_3 = function () {
	this.addRefreshTask(RefreshTaskType.Added, [this.Result]);
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Generate report
//----------------------------------------------------------------------
function CGProcessGenerateReport() {
	this.base = CGProcess;
	this.base(3);
	this.ActivateNode = true;
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGProcessGenerateReport.prototype = new CGProcess;
CGProcessGenerateReport.constructor = CGProcessGenerateReport;

CGProcessGenerateReport.prototype.onFailure = function (sResponse) {
	Desktop.hideReports();
	this.terminateOnFailure(sResponse);
};

CGProcessGenerateReport.prototype.step_1 = function () {
	if (this.Target == TARGET_ENVIRONMENT) this.IdTarget = Account.getUser().getRootNode().id;

	if (this.IdParent == null) this.IdParent = NodesCache.getCurrent().getId();
	if (this.IdParent == ID_NODE_SEARCH) {
		this.IdParent = NodesCache.get(State.LastObject.Id).getId();
	}

	var sCaption = Extension.getDefinition(this.Code).Caption;

	if (this.ActivateNode) Desktop.reportProgress(this.generateMessage(Lang.Process.GenerateReport.Waiting, {caption: sCaption}), true);

	if (!this.Mode) {
		var Behaviour = Extension.getDefinitionBehaviour(this.Code);

		if ((!Behaviour) || (!Behaviour.AddNode) || (!Behaviour.AddNode.Templates) || (!Behaviour.AddNode.Templates.View)) {
			this.terminateOnFailure();
			return;
		}

		this.Mode = Behaviour.AddNode.Templates.View;
	}

	Kernel.generateReport(this, this.Code, this.IdParent, this.Mode, this.DataSourceTemplate, this.sNodes, this.sNodeTypes, this.sFromDate, this.sToDate);
};

CGProcessGenerateReport.prototype.step_2 = function () {
	this.Result = new CGNode();
	this.Result.unserialize(this.data);
	NodesCache.register(this.Result);

	State.aMarkedNodesReferences = [this.Result.getId()];
	State.NodeReferenceMarkType = MarkType.Added;

	if (this.ActivateNode) {
		var ActionShowNode = new CGActionShowNode();
		ActionShowNode.Id = this.Result.getId();
		ActionShowNode.Mode = this.Mode;
		ActionShowNode.ReturnProcess = this;
		ActionShowNode.execute();
	}
	else {
		this.execute();
	}
};

CGProcessGenerateReport.prototype.step_3 = function () {
	this.addRefreshTask(RefreshTaskType.Added, [this.Result]);
	Desktop.hideReports();
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Add node from file
//----------------------------------------------------------------------
function CGProcessAddNodeFromFile() {
	this.base = CGProcess;
	this.base(3);
};

CGProcessAddNodeFromFile.prototype = new CGProcess;
CGProcessAddNodeFromFile.constructor = CGProcessAddNodeFromFile;

CGProcessAddNodeFromFile.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	this.terminateOnFailure(sResponse);
};

CGProcessAddNodeFromFile.prototype.step_1 = function () {
	Desktop.reportProgress(Lang.Process.AddNodeFromFile.Waiting, true);
	Kernel.uploadNodeContent(this, this.FileForm);
};

CGProcessAddNodeFromFile.prototype.step_2 = function () {
	var Node = NodesCache.getCurrent();
	if (Node.getId() == ID_NODE_SEARCH) Node = NodesCache.get(State.LastObject.Id);
	Kernel.importNode(this, Node.getId(), null, this.Option, this.Description);
};

CGProcessAddNodeFromFile.prototype.step_3 = function () {
	State.aMarkedNodesReferences = (this.data == "") ? new Array() : this.data.split(COMMA);
	State.NodeReferenceMarkType = MarkType.Added;
	Desktop.hideReports();
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Add node from clipboard
//----------------------------------------------------------------------
function CGProcessAddNodeFromClipboard() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessAddNodeFromClipboard.prototype = new CGProcess;
CGProcessAddNodeFromClipboard.constructor = CGProcessAddNodeFromClipboard;

CGProcessAddNodeFromClipboard.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	this.terminateOnFailure(sResponse);
};

CGProcessAddNodeFromClipboard.prototype.step_1 = function () {
	var NodeResult = new CGNode();
	var Node = NodesCache.getCurrent();

	Desktop.reportProgress(Lang.Process.AddNodeFromClipboard.Waiting, true);

	if (Node.getId() == ID_NODE_SEARCH) Node = NodesCache.get(State.LastObject.Id);

	for (var iPos = 0; iPos < this.Data.length; iPos++) {
		var CurrentNode = new CGNode();
		CurrentNode.Code = this.Code;
		CurrentNode.copyFromFields(this.Data[iPos]);
		NodeResult.NodeList.addNode(CurrentNode);
	}

	Kernel.importNode(this, Node.getId(), NodeResult.serialize(), ADD_NODE_OPTION_REPLACE, this.Description);
};

CGProcessAddNodeFromClipboard.prototype.step_2 = function () {
	State.aMarkedNodesReferences = (this.data == "") ? new Array() : this.data.split(COMMA);
	State.NodeReferenceMarkType = MarkType.Added;
	Desktop.hideReports();
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Delete nodes
//----------------------------------------------------------------------
function CGProcessDeleteNodes() {
	this.base = CGProcess;
	this.base(2);
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGProcessDeleteNodes.prototype = new CGProcess;
CGProcessDeleteNodes.constructor = CGProcessDeleteNodes;

CGProcessDeleteNodes.prototype.onFailure = function (sResponse) {
	this.terminateOnFailure(sResponse);
};

CGProcessDeleteNodes.prototype.step_1 = function () {
	Kernel.deleteNodes(this, this.Nodes.toString());
};

CGProcessDeleteNodes.prototype.step_2 = function () {
	var aNodes = new Array();

	for (var index in this.Nodes) {
		if (isFunction(this.Nodes[index])) continue;
		var Node = new CGNode();
		Node.setId(this.Nodes[index]);
		aNodes.push(Node);
	}

	this.addRefreshTask(RefreshTaskType.Deleted, aNodes);

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Load field node
//----------------------------------------------------------------------
function CGProcessLoadFieldNode() {
	this.base = CGProcess;
	this.base(2);
	this.ViewNode = null;
};

CGProcessLoadFieldNode.prototype = new CGProcess;
CGProcessLoadFieldNode.constructor = CGProcessLoadFieldNode;

CGProcessLoadFieldNode.prototype.step_1 = function () {
	var IdLayer = Ext.id(), Node;
	var CurrentNode;
	var CurrentViewNode;
	var DOMLayer;

	if ((CurrentNode = NodesCache.getCurrent()) == null) {
		this.terminateOnFailure();
		return;
	}

	if ((CurrentViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, CurrentNode.getId())) == null) {
		this.terminateOnFailure();
		return;
	}

	EventManager.notify(EventManager.BEFORE_OPEN_NODE, {"IdNode": this.Id});

	Node = new CGNode();
	Node.setId(this.Id);

	DOMLayer = createLayer(IdLayer, EMPTY, this.Container);
	this.ViewNode = Desktop.createView(DOMLayer, Node, CurrentViewNode, this.Mode, true);

	var Process = new CGProcessShowNode();
	Process.Id = Node.getId();
	Process.Mode = this.Mode;
	Process.ViewNode = this.ViewNode;
	Process.ActivateNode = false;
	Process.ReturnProcess = this;
	Process.execute();
};

CGProcessLoadFieldNode.prototype.step_2 = function () {
	var Node = NodesCache.get(this.Id);
	if (this.onComplete) this.onComplete();
	EventManager.notify(EventManager.OPEN_NODE, {"Node": Node, "DOMNode": this.ViewNode.getDOM()});
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Save field node
//----------------------------------------------------------------------
function CGProcessSaveFieldNode() {
	this.base = CGProcess;
	this.base(1);
};

CGProcessSaveFieldNode.prototype = new CGProcess;
CGProcessSaveFieldNode.constructor = CGProcessSaveFieldNode;

CGProcessSaveFieldNode.prototype.step_1 = function () {
	var ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, this.DOMNode.IdView);

	if (ViewNode == null) {
		this.terminateOnFailure();
		return;
	}

	var Process = new CGProcessSaveNodeAttribute();
	Process.Node = ViewNode.getTarget();
	Process.DOMNode = ViewNode.getDOM();
	Process.Data = this.DOMField.getContent();
	Process.execute();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Add field node
//----------------------------------------------------------------------
function CGProcessAddFieldNode() {
	this.base = CGProcess;
	this.base(4);
	this.Node = null;
};

CGProcessAddFieldNode.prototype = new CGProcess;
CGProcessAddFieldNode.constructor = CGProcessAddFieldNode;

CGProcessAddFieldNode.prototype.onFailure = function (sResponse) {
	Desktop.reportError(this.generateMessage(Lang.Process.AddFieldNode.Failure, {caption: Extension.getDefinition(this.Code).Caption}));
	this.terminate();
};

CGProcessAddFieldNode.prototype.step_1 = function () {
	var CurrentNode;

	if ((CurrentNode = NodesCache.getCurrent()) == null) {
		this.terminateOnFailure();
		return;
	}

	Kernel.addNode(this, this.Code, CurrentNode.getId(), this.Mode, null);
};

CGProcessAddFieldNode.prototype.step_2 = function () {
	this.Node = new CGNode();
	this.Node.unserialize(this.data);
	NodesCache.register(this.Node);

	Kernel.saveNode(this, this.Node.getId(), "", null);
};

CGProcessAddFieldNode.prototype.step_3 = function () {
	this.Process = new CGProcessLoadFieldNode();
	this.Process.Id = this.Node.getId();
	this.Process.Mode = this.Mode;
	this.Process.Container = this.Container;
	this.Process.ReturnProcess = this;
	this.Process.execute();
};

CGProcessAddFieldNode.prototype.step_4 = function () {
	if (this.onComplete) this.onComplete(this.Node.getId());
	EventManager.notify(EventManager.CREATE_NODE, {"Node": this.Node, "DOMNode": this.Process.ViewNode.getDOM()});
	this.terminate();
};

//----------------------------------------------------------------------
// Load field node link
//----------------------------------------------------------------------
function CGProcessLoadFieldNodeLink() {
	this.base = CGProcess;
	this.base(2);
	this.ViewNode = null;
};

CGProcessLoadFieldNodeLink.prototype = new CGProcess;
CGProcessLoadFieldNodeLink.constructor = CGProcessLoadFieldNodeLink;

CGProcessLoadFieldNodeLink.prototype.step_1 = function () {
	var IdLayer = Ext.id(), Node;
	var CurrentNode;
	var CurrentViewNode;
	var DOMLayer;

	if ((CurrentNode = NodesCache.getCurrent()) == null) {
		this.terminateOnFailure();
		return;
	}

	if ((CurrentViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, CurrentNode.getId())) == null) {
		this.terminateOnFailure();
		return;
	}

	EventManager.notify(EventManager.BEFORE_OPEN_NODE, {"IdNode": this.Id});

	Node = new CGNode();
	Node.setId(this.Id);

	DOMLayer = createLayer(IdLayer, EMPTY, this.Container);
	this.ViewNode = Desktop.createView(DOMLayer, Node, CurrentViewNode, this.Mode, true);

	var Process = new CGProcessShowNode();
	Process.Id = Node.getId();
	Process.Mode = this.Mode;
	Process.ViewNode = this.ViewNode;
	Process.ActivateNode = false;
	Process.ReturnProcess = this;
	Process.execute();
};

CGProcessLoadFieldNodeLink.prototype.step_2 = function () {
	var Node = NodesCache.get(this.Id);
	if (this.onComplete) this.onComplete();
	EventManager.notify(EventManager.OPEN_NODE, {"Node": Node, "DOMNode": this.ViewNode.getDOM()});
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Add field node link
//----------------------------------------------------------------------
function CGProcessAddFieldNodeLink() {
	this.base = CGProcess;
	this.base(3);
	this.Node = null;
};

CGProcessAddFieldNodeLink.prototype = new CGProcess;
CGProcessAddFieldNodeLink.constructor = CGProcessAddFieldNodeLink;

CGProcessAddFieldNodeLink.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	Desktop.reportError(this.generateMessage(Lang.Process.AddFieldNodeLink.Failure, {caption: Extension.getDefinition(this.Code).Caption}));
	this.terminate();
};

CGProcessAddFieldNodeLink.prototype.step_1 = function () {
	var IdParent = this.IdParent;
	if (IdParent == null) IdParent = Account.getUser().getRootNode().id;
	Desktop.reportProgress(this.generateMessage(Lang.Process.AddFieldNodeLink.Waiting, {caption: Extension.getDefinition(this.CodeType).Caption}), true);
	Kernel.addNode(this, this.Code, IdParent, this.Mode, null);
};

CGProcessAddFieldNodeLink.prototype.step_2 = function () {

	this.Node = new CGNode();
	this.Node.unserialize(this.data);
	NodesCache.register(this.Node);

	if (this.onComplete) this.onComplete(this.Node.getId());

	this.Process = new CGProcessShowLinkNode();
	this.Process.IdLink = this.Node.getId();
	this.Process.IdNode = NodesCache.getCurrent().getId();
	this.Process.Mode = this.Mode;
	this.Process.ReturnProcess = this;
	this.Process.execute();
};

CGProcessAddFieldNodeLink.prototype.step_3 = function () {
	Desktop.hideProgress();
	this.terminate();
};

//----------------------------------------------------------------------
// Load default value
//----------------------------------------------------------------------
function CGProcessLoadDefaultValue() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessLoadDefaultValue.prototype = new CGProcess;
CGProcessLoadDefaultValue.constructor = CGProcessLoadDefaultValue;

CGProcessLoadDefaultValue.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Process.LoadDefaultValue.Failure);
};

CGProcessLoadDefaultValue.prototype.step_1 = function () {
	if ((!this.NodeType) || (!this.Property) || (!this.DOMField)) return;
	Kernel.loadDefaultValue(this, this.NodeType, this.Property);
};

CGProcessLoadDefaultValue.prototype.step_2 = function () {
	if (this.data != "") this.DOMField.setData(this.data);
	else this.DOMField.fillWithDefaultData();
	this.terminate();
};

//----------------------------------------------------------------------
// Add default value
//----------------------------------------------------------------------
function CGProcessAddDefaultValue() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessAddDefaultValue.prototype = new CGProcess;
CGProcessAddDefaultValue.constructor = CGProcessAddDefaultValue;

CGProcessAddDefaultValue.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Process.AddDefaultValue.Failure);
};

CGProcessAddDefaultValue.prototype.step_1 = function () {
	if ((!this.NodeType) || (!this.Property) || (!this.Data)) return;
	Kernel.addDefaultValue(this, this.NodeType, this.Property, this.Data);
};

CGProcessAddDefaultValue.prototype.step_2 = function () {
	Desktop.reportSuccess(Lang.Process.AddDefaultValue.Done);
	this.terminate();
};

//----------------------------------------------------------------------
// Goto Field
//----------------------------------------------------------------------
function CGProcessGotoField() {
	this.base = CGAction;
	this.base(1);
};

CGProcessGotoField.prototype = new CGProcess;
CGProcessGotoField.constructor = CGProcessGotoField;

CGProcessGotoField.prototype.step_1 = function () {

	if ((!this.Path) || (!this.ViewNode)) {
		this.terminateOnFailure();
		return;
	}

	if (!this.ViewNode.getDOM().gotoField) return false;
	this.ViewNode.getDOM().gotoField(this.Path);

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Load node notes
//----------------------------------------------------------------------
function CGProcessLoadNodeNotes() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessLoadNodeNotes.prototype = new CGProcess;
CGProcessLoadNodeNotes.constructor = CGProcessLoadNodeNotes;

CGProcessLoadNodeNotes.prototype.step_1 = function () {

	if (this.Node == null) {
		this.terminateOnFailure();
		return;
	}

	Kernel.loadNodeNotes(this, this.Node.getId());
};

CGProcessLoadNodeNotes.prototype.step_2 = function () {
	this.Node.Notes = Ext.util.JSON.decode(this.data);
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Focus node view
//----------------------------------------------------------------------
function CGProcessFocusNodeView() {
	this.base = CGProcess;
	this.base(3);
	this.RefreshNodeView = true;
};

CGProcessFocusNodeView.prototype = new CGProcess;
CGProcessFocusNodeView.constructor = CGProcessFocusNodeView;

CGProcessFocusNodeView.prototype.step_1 = function () {

	if (this.DOMNode.getControlInfo == null) {
		this.terminate();
		return;
	}

	var TimeStamp = this.DOMNode.getControlInfo().TimeStamp;
	this.ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, this.DOMNode.IdView);

	if (!this.ViewNode) {
		this.terminateOnFailure();
		return;
	}

	Kernel.focusNodeView(this, Account.getInstanceId(), this.Node.getId(), TimeStamp);
};

CGProcessFocusNodeView.prototype.step_2 = function () {
	var jsonData = Ext.util.JSON.decode(this.data);

	if (jsonData.node != null && this.RefreshNodeView) {
		var Process = new CGProcessShowNode();
		Process.Id = this.ViewNode.getTarget().getId();
		Process.Mode = this.ViewNode.getDOM().getControlInfo().Templates.Refresh;
		Process.ViewNode = this.ViewNode;
		Process.ActivateNode = false;
		Process.ReturnProcess = this;
		Process.execute();
		return;
	}

	this.execute();
};

CGProcessFocusNodeView.prototype.step_3 = function () {
	var jsonData = Ext.util.JSON.decode(this.data);
	var DOMNode = this.ViewNode.getDOM();

	DOMNode.setObservers(jsonData.observers);

	var Process = new CGProcessRefreshHelperObservers();
	Process.Observers = jsonData.observers;
	Process.execute();

	State.CurrentView = this.ViewNode;

	this.terminate();
};

//----------------------------------------------------------------------
// Blur node view
//----------------------------------------------------------------------
function CGProcessBlurNodeView() {
	this.base = CGProcess;
	this.base(2);
	this.RefreshNodeView = true;
};

CGProcessBlurNodeView.prototype = new CGProcess;
CGProcessBlurNodeView.constructor = CGProcessBlurNodeView;

CGProcessBlurNodeView.prototype.step_1 = function () {
	var TimeStamp = this.DOMNode.getControlInfo().TimeStamp;

	this.ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, this.DOMNode.IdView);

	if (!this.ViewNode) {
		this.terminateOnFailure();
		return;
	}

	Kernel.blurNodeView(this, Account.getInstanceId(), this.Node.getId(), TimeStamp);
};

CGProcessBlurNodeView.prototype.step_2 = function () {
	var jsonData = Ext.util.JSON.decode(this.data);
	var DOMNode = this.ViewNode.getDOM();
	DOMNode.setObservers(jsonData.observers);
	this.terminate();
};

//----------------------------------------------------------------------
// Focus node field
//----------------------------------------------------------------------
function CGProcessFocusNodeField() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessFocusNodeField.prototype = new CGAction;
CGProcessFocusNodeField.constructor = CGProcessFocusNodeField;

CGProcessFocusNodeField.prototype.step_1 = function () {
	var DOMNode = this.DOMField.up(CSS_NODE);
	var ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, DOMNode.IdView);

	if (ViewNode != State.CurrentView) {
		var Process = new CGProcessFocusNodeView();
		Process.Node = this.Node;
		Process.DOMNode = DOMNode;
		Process.ReturnProcess = this;
		Process.RefreshNodeView = false;
		Process.execute();
	}
	else this.execute();
};

CGProcessFocusNodeField.prototype.step_2 = function () {
	Kernel.focusNodeField(this, Account.getInstanceId(), this.Node.getId(), this.DOMField.getPath(true));
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Save node attribute
//----------------------------------------------------------------------
function CGProcessSaveNodeAttribute() {
	this.base = CGProcess;
	this.base(2);
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGProcessSaveNodeAttribute.prototype = new CGAction;
CGProcessSaveNodeAttribute.constructor = CGProcessSaveNodeAttribute;

CGProcessSaveNodeAttribute.prototype.step_1 = function () {
	Kernel.saveNodeAttribute(this, Account.getInstanceId(), this.Node.getId(), this.Data);
};

CGProcessSaveNodeAttribute.prototype.step_2 = function () {
	var ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, this.DOMNode.IdView);

	if (!ViewNode) {
		this.terminateOnSuccess();
		return;
	}

	var DOMNode = ViewNode.getDOM();
	if (DOMNode.setDirty) DOMNode.setDirty(false);
	// Disabled to reduce number of collection refresh -> this.addRefreshTask(RefreshTaskType.References, this.Node);
	EventManager.notify(EventManager.SAVE_NODE, {"Node": this.Node, "DOMNode": DOMNode});

	var process = new CGProcessRefreshNodeState();
	process.nodeId = this.Node.getId();
	process.execute();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Show link node
//----------------------------------------------------------------------
function CGProcessShowLinkNode() {
	this.base = CGProcess;
	this.base(3);
};

CGProcessShowLinkNode.prototype = new CGProcess;
CGProcessShowLinkNode.constructor = CGProcessShowLinkNode;

CGProcessShowLinkNode.prototype.step_1 = function () {
	var ActionShowNode = new CGActionShowNode();
	ActionShowNode.Id = this.IdLink;
	ActionShowNode.ReturnProcess = this;
	ActionShowNode.Mode = this.Mode;
	ActionShowNode.execute();
};

CGProcessShowLinkNode.prototype.step_2 = function () {
	window.setTimeout(this.execute.bind(this), 250);
};

CGProcessShowLinkNode.prototype.step_3 = function () {
	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.IdLink);
	var Node = NodesCache.get(this.IdNode);

	if (Node) {
		State.LinkNode = {IdLink: this.IdLink, IdNode: this.IdNode};
		ViewNode.getDOM().showBackLinkCommand(this.IdNode);
	}

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Update Node Location
//----------------------------------------------------------------------
function CGProcessUpdateNodeLocation() {
	this.base = CGProcess;
	this.base(1);
};

CGProcessUpdateNodeLocation.prototype = new CGProcess;
CGProcessUpdateNodeLocation.constructor = CGProcessUpdateNodeLocation;

CGProcessUpdateNodeLocation.prototype.step_1 = function () {
	if (this.Id == null || this.Location == null)
		this.terminate();

	Kernel.updateNodeLocation(this, this.Id, this.Location);
};

//----------------------------------------------------------------------
// Clean Node Location
//----------------------------------------------------------------------
function CGProcessCleanNodeLocation() {
	this.base = CGProcess;
	this.base(1);
};

CGProcessCleanNodeLocation.prototype = new CGProcess;
CGProcessCleanNodeLocation.constructor = CGProcessCleanNodeLocation;

CGProcessCleanNodeLocation.prototype.step_1 = function () {
	if (this.Id == null)
		this.terminate();

	Kernel.cleanNodeLocation(this, this.Id);
};


//----------------------------------------------------------------------
// Show node revisions
//----------------------------------------------------------------------
function CGProcessShowNodeRevisions() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessShowNodeRevisions.prototype = new CGProcess;
CGProcessShowNodeRevisions.constructor = CGProcessShowNodeRevisions;

CGProcessShowNodeRevisions.prototype.atShowItem = function (ListViewer, Item) {
	var ActiveTab = null;
	var ListState = ListViewer.getState();
	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Item.idNode);

	if (ViewNode) ActiveTab = ViewNode.getDOM().getActiveTab();

	if (ListState.CurrentPage == 1 && Item.position == 0) CommandListener.dispatchCommand("shownoderevision(," + Item.idNode + "," + this.Template + (ActiveTab ? "," + ActiveTab : "") + ")");
	else CommandListener.dispatchCommand("shownoderevision(" + Item.id + "," + Item.idNode + "," + this.Template + (ActiveTab ? "," + ActiveTab : "") + ")");
};

CGProcessShowNodeRevisions.prototype.atBoundItem = function (ListViewer, Item) {
	var ListState = ListViewer.getState();
	var label = "";

	if (ListState.CurrentPage == 1 && Item.position == 0) label = Lang.ViewerHelperRevisionList.CurrentRevision;
	else label = Lang.ViewerHelperRevisionList.RevisionAt + getFormattedDateTime(parseServerDate(Item.label), Context.Config.Language, false, false);

	Item.label = label;
};

CGProcessShowNodeRevisions.prototype.step_1 = function () {
	var Process = new CGProcessLoadHelperRevisionListViewer();
	Process.ReturnProcess = this;
	Process.execute();
};

CGProcessShowNodeRevisions.prototype.step_2 = function () {
	var Options = new Object();
	var helper = ViewerSidebar.getHelper(Helper.REVISIONLIST);
	var DOMLayer = helper.getListViewerLayer();

	Options.Editable = false;
	Options.itemsPerPage = 8;
	Options.selfScroller = true;
	Options.DataSource = new Object();
	Options.DataSource.Remote = true;
	Options.Templates = new Object();
	Options.Templates.Item = HtmlUtil.encode(AppTemplate.ViewerHelperRevisionListItem);
	Options.Templates.NoItems = "&lt;div class='noitems'&gt;" + Lang.ViewerHelperRevisionList.NoRevisions + "&lt;/div&gt;";
	Options.Templates.CountItems = "\#\{count\} " + Lang.ViewerHelperRevisionList.Revisions;

	var ListViewer = new CGListViewer(Options, Context.Config.Language, Context.Config.ImagesPath);
	ListViewer.setDataSourceUrls(Kernel.getNodeRevisionItemsLink(this.Id), null);
	ListViewer.setWizardLayer(null);
	ListViewer.onShowItem = CGProcessShowNodeRevisions.prototype.atShowItem.bind(this);
	ListViewer.onBoundItem = CGProcessShowNodeRevisions.prototype.atBoundItem.bind(this);
	ListViewer.render(DOMLayer.id);

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Show node revision
//----------------------------------------------------------------------
function CGProcessShowNodeRevision() {
	this.base = CGProcess;
	this.base(3);
};

CGProcessShowNodeRevision.prototype = new CGProcess;
CGProcessShowNodeRevision.constructor = CGProcessShowNodeRevision;

CGProcessShowNodeRevision.prototype.atTabActivated = function () {
	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.IdNode);
	var Process = new CGProcessShowNodeRevision();
	Process.Id = this.Id;
	Process.IdNode = this.IdNode;
	Process.Template = this.Template;
	Process.ActiveTab = ViewNode.getDOM().getActiveTab();
	Process.execute();
};

CGProcessShowNodeRevision.prototype.step_1 = function () {
	if (this.Id == null || this.Id == "") Kernel.loadCurrentNodeRevision(this, this.IdNode, this.Template, this.ActiveTab);
	else Kernel.loadNodeRevision(this, this.Id, this.IdNode, this.Template, this.ActiveTab);
};

CGProcessShowNodeRevision.prototype.step_2 = function () {
	var Node = new CGNode();
	Node.unserialize(this.data);

	var Process = new CGProcessShowNode();
	Process.ActivateNode = true;
	Process.Node = Node;
	Process.RevisionId = this.Id;
	Process.Mode = this.Template + "&revision=" + this.Id;
	Process.DOMViewActiveTab = this.ActiveTab ? this.ActiveTab : null;
	Process.ReturnProcess = this;
	Process.execute();
};

CGProcessShowNodeRevision.prototype.step_3 = function () {
	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.IdNode);
	var DOMNode = ViewNode.getDOM();

	DOMNode.onTabActivated = CGProcessShowNodeRevision.prototype.atTabActivated.bind(this);

	var Process = new CGProcessLoadHelperRevisionListViewer();
	Process.ReturnProcess = this;
	Process.execute();
	this.terminate();
};

//----------------------------------------------------------------------
// Sign node document
//----------------------------------------------------------------------
function CGProcessSignNodeDocument() {
	this.base = CGProcess;
	this.base(5);
	this.undoSignature = false;
};

CGProcessSignNodeDocument.prototype = new CGProcess;
CGProcessSignNodeDocument.constructor = CGProcessSignNodeDocument;

CGProcessSignNodeDocument.prototype.onFailure = function (sResponse) {
	this.terminateOnFailure(sResponse);
};

CGProcessSignNodeDocument.prototype.step_1 = function () {
	var reason = "", location = "";
	var signer = Desktop.getDigitalSignatureApplication();
	var process = this;

	signer.signText("getcertificate_" + Math.random(), function(signature) {
		Kernel.prepareNodeDocumentSignature(process, process.Id, process.CodeSignature, signature, reason, location);
	}, function(code, message) {
		Desktop.reportError(message);
	});
};

CGProcessSignNodeDocument.prototype.checkOption = function (ButtonResult) {
	if (ButtonResult == BUTTON_RESULT_YES) {
		this.execute();
		return;
	}

	this.undoSignature = true;
	this.gotoStep(4);
};

CGProcessSignNodeDocument.prototype.step_2 = function () {
	this.signatureField = Ext.util.JSON.decode(this.data);
	Ext.MessageBox.confirm(Lang.ViewNode.DialogSignNode.Title, Lang.ViewNode.DialogSignNode.Description, CGProcessSignNodeDocument.prototype.checkOption.bind(this));
};

CGProcessSignNodeDocument.prototype.step_3 = function () {
	var signer = Desktop.getDigitalSignatureApplication();
	var process = this;

	signer.signTextWithCertificate(this.signatureField.hash, this.signatureField.certificateSN, function(signature, certificate) {
		Kernel.stampNodeDocumentSignature(process, process.Id, process.CodeSignature, process.signatureField.id, signature);
	}, function(code, message) {
		Desktop.reportError(message);
	});
};

CGProcessSignNodeDocument.prototype.step_4 = function () {
	if (this.undoSignature)
		Kernel.deleteNodeDocumentSignature(this, this.Id, this.CodeSignature);
	else
		this.execute();
};

CGProcessSignNodeDocument.prototype.step_5 = function () {
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Show node attachment
//----------------------------------------------------------------------
function CGProcessShowNodeAttachment() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessShowNodeAttachment.prototype = new CGProcess;
CGProcessShowNodeAttachment.constructor = CGProcessReplaceNodeAttachment;

CGProcessShowNodeAttachment.prototype.step_1 = function () {
	Kernel.loadNodeType(this, this.Id);
};

CGProcessShowNodeAttachment.prototype.step_2 = function () {
	var Behaviour = Extension.getDefinitionBehaviour(this.data);

	if ((!Behaviour) || (!Behaviour.ShowNode) || (!Behaviour.ShowNode.Templates) || (!Behaviour.ShowNode.Templates.View)) {
		Desktop.hideReports();
		this.terminate();
		return;
	}

	CommandListener.dispatchCommand("shownode(" + this.Id + "," + Behaviour.ShowNode.Templates.View + ")");

	this.terminate();
};

//----------------------------------------------------------------------
// Replace node attachment
//----------------------------------------------------------------------
function CGProcessReplaceNodeAttachment() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessReplaceNodeAttachment.prototype = new CGProcess;
CGProcessReplaceNodeAttachment.constructor = CGProcessReplaceNodeAttachment;

CGProcessReplaceNodeAttachment.prototype.step_1 = function () {
	Kernel.loadNodeType(this, this.Id);
};

CGProcessReplaceNodeAttachment.prototype.step_2 = function () {
	var Behaviour = Extension.getDefinitionBehaviour(this.data);

	if ((!Behaviour) || (!Behaviour.ShowNode) || (!Behaviour.ShowNode.Templates) || (!Behaviour.ShowNode.Templates.Edit)) {
		Desktop.hideReports();
		this.terminate();
		return;
	}

	CommandListener.dispatchCommand("shownode(" + this.Id + "," + Behaviour.ShowNode.Templates.Edit + ")");

	this.terminate();
};

//----------------------------------------------------------------------
// Refresh node state
//----------------------------------------------------------------------
function CGProcessRefreshNodeState() {
	this.base = CGProcess;
	this.base(4);
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGProcessRefreshNodeState.prototype = new CGAction;
CGProcessRefreshNodeState.constructor = CGProcessRefreshNodeState;

CGProcessRefreshNodeState.prototype.applyNodeFlags = function (DOMView, aFlags) {
	for (var i = 0; i < aFlags.length; i++) {
		var flag = aFlags[i];
		if (flag == "READ_ONLY") /*alert("todo read_only en node")*/;
		else if (flag == "INVALID") /*alert("todo invalid en node")*/;
	}
};

CGProcessRefreshNodeState.prototype.resetState = function (DOMView) {
	this.resetFieldsState(DOMView);
	this.resetViewsState(DOMView);
	this.resetLinksState(DOMView);
	this.resetOperationsState(DOMView);
};

CGProcessRefreshNodeState.prototype.resetFieldsState = function (DOMView) {
	var DOMFields = State.NodesStates[this.nodeId].fields;
	for (var code in DOMFields) {
		if (isFunction(DOMFields[code])) continue;
		var DOMField = DOMView.getField(code);
		if (DOMField != null) {
			DOMField.unLock();
			DOMField.show();
		}
	}
};

CGProcessRefreshNodeState.prototype.resetViewsState = function (DOMView) {
	var DOMViews = State.NodesStates[this.nodeId].views;
	for (var code in DOMViews) {
		if (isFunction(DOMViews[code])) continue;
		DOMView.showTab(code);
	}
};

CGProcessRefreshNodeState.prototype.resetLinksState = function (DOMView) {
	var DOMLinks = State.NodesStates[this.nodeId].links;
	for (var code in DOMLinks) {
		if (isFunction(DOMLinks[code])) continue;
		DOMView.showLink(code);
	}
};

CGProcessRefreshNodeState.prototype.resetOperationsState = function (DOMView) {
	var DOMOperations = State.NodesStates[this.nodeId].operations;
	for (var code in DOMOperations) {
		if (isFunction(DOMOperations[code])) continue;
		DOMView.showOperation(code);
		DOMView.enableOperation(code);
	}
};

CGProcessRefreshNodeState.prototype.applyFlags = function (DOMView) {
	this.applyNodeFlags(DOMView, this.state.flags);
	this.applyNodeFieldsFlags(DOMView, this.state.fieldsFlags);
	this.applyNodeViewsFlags(DOMView, this.state.viewsFlags);
	this.applyNodeLinkFlags(DOMView, this.state.linksFlags);
	this.applyNodeOperationsFlags(DOMView, this.state.operationsFlags);
};

CGProcessRefreshNodeState.prototype.applyNodeFieldsFlags = function (DOMView, aFlags) {

	if (!DOMView.getField) return;

	for (var code in aFlags) {
		if (isFunction(aFlags[code])) continue;
		var aFieldFlags = aFlags[code];
		var DOMField = DOMView.getField(code);

		State.NodesStates[this.nodeId].fields[code] = code;

		if (DOMField != null) {
			for (var j = 0; j < aFieldFlags.length; j++) {
				var flag = aFieldFlags[j];
				if (flag == "REQUIRED") /*alert("todo required en field")*/;
				else if (flag == "READ_ONLY") DOMField.lock();
				else if (flag == "INVALID") /*alert("todo invalid en field")*/;
				else if (flag == "HIDDEN") DOMField.hide();
			}
		}
	}
};

CGProcessRefreshNodeState.prototype.applyNodeViewsFlags = function (DOMView, aFlags) {
	var tabsCodes = DOMView.getTabsCodes();

	for (var i=0; i<tabsCodes.length; i++) {
		var tabCode = tabsCodes[i];

		if (aFlags[tabCode] != null) {
			State.NodesStates[this.nodeId].views[tabCode] = tabCode;

			for (var j = 0; j < aFlags[tabCode].length; j++) {
				var flag = aFlags[tabCode][j];
				if (flag == "HIDDEN") DOMView.hideTab(tabCode);
				else DOMView.showTab(tabCode);
			}
		}
		else DOMView.showTab(tabCode);
	}
	/*
	for (var code in aFlags) {
		if (isFunction(aFlags[code])) continue;
		var aViewFlags = aFlags[code];

		State.NodesStates[this.nodeId].views[code] = code;

		for (var j = 0; j < aViewFlags.length; j++) {
			var flag = aViewFlags[j];
			if (flag == "HIDDEN") DOMView.hideTab(code);
		}
	}*/
};

CGProcessRefreshNodeState.prototype.applyNodeLinkFlags = function (DOMView, aFlags) {
	for (var code in aFlags) {
		if (isFunction(aFlags[code])) continue;
		var aLinkFlags = aFlags[code];

		State.NodesStates[this.nodeId].links[code] = code;

		for (var j = 0; j < aLinkFlags.length; j++) {
			var flag = aLinkFlags[j];
			if (flag == "HIDDEN") DOMView.hideLink(code);
		}
	}
};

CGProcessRefreshNodeState.prototype.applyNodeOperationsFlags = function (DOMView, aFlags) {
	for (var code in aFlags) {
		if (isFunction(aFlags[code])) continue;
		var aOperationFlags = aFlags[code];

		State.NodesStates[this.nodeId].operations[code] = code;

		for (var j = 0; j < aOperationFlags.length; j++) {
			var flag = aOperationFlags[j];
			if (flag == "HIDDEN") DOMView.hideOperation(code);
			else if (flag == "DISABLED") DOMView.disableOperation(code);
		}
	}
};

CGProcessRefreshNodeState.prototype.step_1 = function () {

	if (this.state != null) {
		this.gotoStep(3);
		return;
	}

	Kernel.loadNodeState(this, this.nodeId);
};

CGProcessRefreshNodeState.prototype.step_2 = function () {
	this.state = Ext.util.JSON.decode(this.data);
	this.execute();
};

CGProcessRefreshNodeState.prototype.step_3 = function () {
	var aViews = Desktop.Main.Center.Body.getViews(VIEW_NODE, VIEW_NODE_TYPE_NODE, this.nodeId);

	if (State.NodesStates[this.nodeId] == null)
		State.NodesStates[this.nodeId] = { fields: new Array(), views: new Array(), operations: new Array(), links: new Array(), date: this.state.date };

	for (var i = 0; i < aViews.length; i++) {
		var DOMView = aViews[i].getDOM();
		this.resetState(DOMView);
		this.applyFlags(DOMView);
	}

	this.loadMainNode(this.nodeId);
};

CGProcessRefreshNodeState.prototype.step_4 = function () {

	if (this.data == "null") {
		this.setMainNode(this.nodeId, this.data);
		this.terminateOnSuccess();
		return;
	}

	var mainNode = new CGNode();
	mainNode.unserialize(this.data);
	this.setMainNode(this.nodeId, this.data);

	var process = new CGProcessRefreshNodeState();
	process.nodeId = mainNode.getId();
	process.execute();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Load node context
//----------------------------------------------------------------------
function CGProcessLoadNodeContext() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessLoadNodeContext.prototype = new CGProcess;
CGProcessLoadNodeContext.constructor = CGProcessLoadNodeContext;

CGProcessLoadNodeContext.prototype.step_1 = function () {
	Kernel.loadNodeContext(this, this.Id);
};

CGProcessLoadNodeContext.prototype.step_2 = function () {
	if (this.onComplete) this.onComplete(this.data);
	this.terminate();
};