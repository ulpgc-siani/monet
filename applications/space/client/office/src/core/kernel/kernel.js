Kernel = new Object;

Kernel.init = function () {
  Kernel.mode = (Context.Config.EncriptData == "true") ? true : false;
  if (Context.Config.TestCase == "true") Kernel.Stub = new CGStubTestCase(Kernel.mode, Literals.ActionMessage.Loading);
  else Kernel.Stub = new CGStubAjax(Kernel.mode, Literals.ActionMessage.Loading);
};

Kernel.loadAccount = function (Action, timeZone) {
  Kernel.Stub.request(Action, "loadaccount", {timezone: timeZone});
};

Kernel.loadUnits = function (Action) {
  Kernel.Stub.request(Action, "loadunits");
};

Kernel.saveAccount = function (Action, Account) {
  var sData = Account.serialize();
  Kernel.Stub.request(Action, "saveaccount", {data: escape(utf8Encode(sData))});
};

Kernel.searchUsers = function (Action, sCondition) {
  Kernel.Stub.request(Action, "searchusers", {query: sCondition});
};

Kernel.getLoadUsersLink = function () {
  var sParameters = "op=loadusers";
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.getLoadFederationUsersLink = function () {
  var sParameters = "op=loadfederationusers";
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.loadPartners = function (Action) {
  Kernel.Stub.request(Action, "loadpartners", null);
};

Kernel.changeRole = function (Action, sRole) {
  Kernel.Stub.request(Action, "changeRole", {role: sRole});
};

Kernel.logout = function (Action, sInstanceId) {
  Kernel.Stub.request(Action, "logout", {i: sInstanceId});
};

Kernel.loadNode = function (Action, Id, Mode, Index, Count) {
  Kernel.Stub.request(Action, "loadnode", {id: Id, mode: escape(Mode), index: Index, count: Count});
};

Kernel.loadMainNode = function (Action, Id) {
  Kernel.Stub.request(Action, "loadmainnode", {id: Id});
};

Kernel.loadNodeState = function (Action, Id) {
  Kernel.Stub.request(Action, "loadnodestate", {id: Id});
};

Kernel.loadNodeType = function (Action, Id) {
  Kernel.Stub.request(Action, "loadnodetype", {id: Id});
};

Kernel.loadNodeTemplate = function (Action, Code, Mode) {
  Kernel.Stub.request(Action, "loadnodetemplate", {code: Code, mode: escape(Mode)});
};

Kernel.loadNodeReference = function (Action, Id, Mode) {
  Kernel.Stub.request(Action, "loadnodereference", {id: Id, mode: escape(Mode)});
};

Kernel.loadNodePrintAttributes = function (Action, Id, CodeView) {
	Kernel.Stub.request(Action, "loadnodeprintattributes", {id: Id, view: CodeView});
};

Kernel.loadNodeNotes = function (Action, Id) {
  Kernel.Stub.request(Action, "loadnodenotes", {id: Id});
};

Kernel.addNodeNote = function (Action, nodeId, name, value) {
  Kernel.Stub.request(Action, "addnodenote", {idnode: nodeId, name: escape(utf8Encode(name)), value: escape(utf8Encode(value))});
};

Kernel.saveNodeNote = function (Action, nodeId, name, value) {
  Kernel.Stub.request(Action, "savenodenote", {idnode: nodeId, name: escape(utf8Encode(name)), value: escape(utf8Encode(value))});
};

Kernel.deleteNodeNote = function (Action, nodeId, name) {
  Kernel.Stub.request(Action, "deletenodenote", {idnode: nodeId, name: escape(utf8Encode(name))});
};

Kernel.addNode = function (Action, Code, IdParent, Mode) {
  var Parameters = new Object();

  Parameters.code = Code;
  Parameters.idparent = IdParent;
  if (Mode != null) Parameters.mode = escape(Mode);

  Kernel.Stub.request(Action, "addnode", Parameters);
};

Kernel.addPrototype = function (Action, Code, IdParent, Mode) {
  var Parameters = new Object();

  Parameters.code = Code;
  if (IdParent != null) Parameters.idparent = IdParent;
  if (Mode != null) Parameters.mode = escape(Mode);

  Kernel.Stub.request(Action, "addprototype", Parameters);
};

Kernel.generateReport = function (Action, Code, IdParent, Mode, DataSourceTemplate, sNodes, sNodeTypes, sFromDate, sToDate) {
  var Parameters = new Object();

  Parameters.code = Code;
  Parameters.idparent = IdParent;
  if (Mode != null) Parameters.mode = escape(Mode);
  if (DataSourceTemplate != null) Parameters.dstemplate = escape(DataSourceTemplate);
  if ((sNodes) && (sNodes != "")) Parameters.nodes = sNodes;
  if ((sNodeTypes) && (sNodeTypes != "")) Parameters.nodetypes = sNodeTypes;
  if ((sFromDate) && (sFromDate != "")) Parameters.from = sFromDate;
  if ((sToDate) && (sToDate != "")) Parameters.to = sToDate;

  Kernel.Stub.request(Action, "generatereport", Parameters);
};

Kernel.copyNode = function (Action, Id, IdParent, Mode) {
  var Parameters = new Object();

  Parameters.id = Id;
  Parameters.idparent = IdParent;
  if (Mode != null) Parameters.mode = escape(Mode);

  Kernel.Stub.request(Action, "copynode", Parameters);
};

Kernel.saveNode = function (Action, Id, sContent, Mode) {
  var Parameters = new Object();

  Parameters.id = Id;
  Parameters.data = escape(utf8Encode(sContent));
  if (Mode != null) Parameters.mode = escape(Mode);

  Kernel.Stub.request(Action, "savenode", Parameters);
};

Kernel.loadNodeAttribute = function (Action, IdNode, sPath) {
  var Parameters = new Object();
  Parameters.idnode = IdNode;
  Parameters.path = sPath;
  Kernel.Stub.request(Action, "loadnodeattribute", Parameters);
};

Kernel.saveNodeAttribute = function (Action, sInstanceId, IdNode, sContent) {
  var Parameters = new Object();

  Parameters.i = sInstanceId;
  Parameters.idnode = IdNode;
  Parameters.data = escape(utf8Encode(sContent));

  Kernel.Stub.request(Action, "savenodeattribute", Parameters);
};

Kernel.saveNodePartnerContext = function (action, instanceId, nodeId, context) {
  var parameters = new Object();

  parameters.i = instanceId;
  parameters.idnode = nodeId;
  parameters.context = escape(utf8Encode(context));

  Kernel.Stub.request(action, "savenodepartnercontext", parameters);
};

Kernel.saveEmbeddedNode = function (Action, Id, sContent, Mode) {
  var Parameters = new Object();

  Parameters.id = Id;
  Parameters.data = escape(utf8Encode(sContent));
  if (Mode != null) Parameters.mode = escape(Mode);

  Kernel.Stub.request(Action, "saveembeddednode", Parameters);
};

Kernel.loadNodeDescriptor = function (Action, IdNode) {
  var Parameters = new Object();
  Parameters.id = IdNode;
  Kernel.Stub.request(Action, "loadnodedescriptor", Parameters);
};

Kernel.saveNodeDescriptor = function (Action, IdNode, AttributeList, Mode) {
  var Parameters = new Object();

  Parameters.id = IdNode;
  Parameters.data = escape(utf8Encode(AttributeList.serialize()));
  if (Mode != null) Parameters.mode = escape(Mode);

  Kernel.Stub.request(Action, "savenodedescriptor", Parameters);
};

Kernel.deleteNodes = function (Action, sData) {
  Kernel.Stub.request(Action, "deletenodes", {data: sData});
};

Kernel.deleteNode = function (Action, Id) {
  Kernel.Stub.request(Action, "deletenode", {id: Id});
};

Kernel.discardNode = function (Action, Id) {
  Kernel.Stub.request(Action, "discardnode", {id: Id});
};

Kernel.importNode = function (Action, IdNode, sData, ImportOption, sDescription) {
  var Parameters = new Object();

  Parameters.id = IdNode;
  if (sData != null) Parameters.data = escape(utf8Encode(sData));
  if (ImportOption != "") Parameters.option = ImportOption;
  if (sDescription != "") Parameters.description = sDescription;

  Kernel.Stub.request(Action, "importnode", Parameters);
};

Kernel.exportNode = function (Action, Id, CodeFormat) {
  Kernel.Stub.request(Action, "exportnode", {id: Id, format: CodeFormat});
};

Kernel.fillNodeDocument = function (Action, nodeId, exporterCode, nodeScopeId) {
  Kernel.Stub.request(Action, "fillnodedocument", {id: nodeId, exporter: exporterCode, scope: nodeScopeId});
};

Kernel.getDownloadExportedNodeLink = function (Id, CodeFormat) {
  var sParameters = "op=downloadexportednodefile&id=" + Id + "&format=" + CodeFormat;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.shareNode = function (Action, Id, UserList, sDescription, sExpireDate) {
  var UsersIds = UserList.getUsersIds();
  Kernel.Stub.request(Action, "sharenode", {id: Id, users: UsersIds, description: sDescription, expire: sExpireDate});
};

Kernel.getPreviewNodesLink = function () {
  var sParameters = "op=previewnode";
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.getPreviewAttachmentsLink = function () {
  var sParameters = "op=previewattachment";
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.getDownloadNodeLink = function (IdNode) {
  var sParameters = "op=downloadnode&id=" + IdNode;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.printNodeTimeConsumption = function (Action, IdNode, Template, CodeView, Filters, Attributes, DateAttribute, FromDate, ToDate, InstanceId) {
    var link = Kernel.getPrintNodeTimeConsumptionLink(IdNode, Template, CodeView, Filters, Attributes, DateAttribute, FromDate, ToDate, InstanceId);
    Kernel.Stub.request(Action, link);
};

Kernel.printNode = function (Action, IdNode, Template, CodeView, Filters, Attributes, DateAttribute, FromDate, ToDate, InstanceId) {
    var link = Kernel.getPrintNodeLink(IdNode, Template, CodeView, Filters, Attributes, DateAttribute, FromDate, ToDate, InstanceId);
    Kernel.Stub.request(Action, link);
};

Kernel.getPrintNodeTimeConsumptionLink = function (IdNode, Template, CodeView, Filters, Attributes, DateAttribute, FromDate, ToDate, InstanceId) {
    return Kernel.getPrintLink("printnodetimeconsumption", IdNode, Template, CodeView, Filters, Attributes, DateAttribute, FromDate, ToDate, InstanceId);
};

Kernel.getPrintNodeLink = function (IdNode, Template, CodeView, Filters, Attributes, DateAttribute, FromDate, ToDate, InstanceId) {
  return Kernel.getPrintLink("printnode", IdNode, Template, CodeView, Filters, Attributes, DateAttribute, FromDate, ToDate, InstanceId);
};

Kernel.getPrintLink = function (op, IdNode, Template, CodeView, Filters, Attributes, DateAttribute, FromDate, ToDate, InstanceId) {
    var sParameters = "op=" + op + "&id=" + IdNode + "&view=" + CodeView + "&template=" + escape(Template);

    for (var CodeFilter in Filters) {
        if (isFunction(Filters[CodeFilter])) continue;
        var Filter = Filters[CodeFilter];
        sParameters += "&" + CodeFilter + "=" + escape(utf8Encode(Filter));
    }

    for (var i=0; i<Attributes.length; i++) {
        if (i==0) sParameters += "&attributes=";
        else sParameters += ",";
        sParameters += escape(utf8Encode(Attributes[i]));
    }

    if (DateAttribute != null) sParameters += "&date=" + DateAttribute;
    if (FromDate != null) sParameters += "&from=" + FromDate;
    if (ToDate != null) sParameters += "&to=" + ToDate;
    if (InstanceId != null) sParameters += "&i=" + InstanceId;

    return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.loadAncestorChildId = function (Action, IdAncestor, CodeView, Index, Filters) {
  var aParameters = {ancestor: IdAncestor, index: Index, view: CodeView};

  for (var CodeFilter in Filters) {
    if (isFunction(Filters[CodeFilter])) continue;
    var Filter = Filters[CodeFilter];
    aParameters[CodeFilter] = escape(utf8Encode(Filter));
  }

  Kernel.Stub.request(Action, "loadancestorchildid", aParameters);
};

Kernel.getDownloadPrintedNodeLink = function (IdNode, Template) {
    var sParameters = "op=downloadprintednode&id=" + IdNode + "&template=" + escape(Template);
    return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.loadTaskListPrintAttributes = function (Action) {
  Kernel.Stub.request(Action, "loadtasklistprintattributes", null);
};

Kernel.loadTaskComments = function (Action, Id) {
  Kernel.Stub.request(Action, "loadtaskcomments", {id: Id});
};

Kernel.getPrintTaskListLink = function (Template, Inbox, Folder, Filters, Attributes, DateAttribute, FromDate, ToDate) {
	var sParameters = "op=printtasklist&template=" + escape(Template) + "&inbox=" + escape(Inbox) + "&folder=" + escape(Folder);

	for (var CodeFilter in Filters) {
		if (isFunction(Filters[CodeFilter])) continue;
		var Filter = Filters[CodeFilter];
		sParameters += "&" + CodeFilter + "=" + escape(Filter);
	}

    for (var i=0; i<Attributes.length; i++) {
      if (i==0) sParameters += "&attributes=";
      else sParameters += ",";
      sParameters += escape(Attributes[i]);
    }

    if (DateAttribute != null) sParameters += "&date=" + DateAttribute;
    if (FromDate != null) sParameters += "&from=" + FromDate;
    if (ToDate != null) sParameters += "&to=" + ToDate;

	return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.replaceNodeDocument = function (Action, IdNode, DOMForm) {
  DOMForm.enctype = "multipart/form-data";
  Kernel.Stub.upload(Action, "replacenodedocument&id=" + IdNode, DOMForm);
};

Kernel.uploadNodeContent = function (Action, DOMForm) {
  DOMForm.enctype = "multipart/form-data";
  Kernel.Stub.upload(Action, "uploadnodecontent", DOMForm);
};

Kernel.prepareNodeDocumentSignature = function (Action, Id, CodeSignature, Signature, sReason, sLocation) {
  Kernel.Stub.request(Action, "preparenodedocumentsignature", {id: Id, code: CodeSignature, signature: escape(utf8Encode(Signature)), reason: escape(utf8Encode(sReason)), location: escape(utf8Encode(sLocation))});
};

Kernel.deleteNodeDocumentSignature = function (Action, Id, SignatureCode) {
  Kernel.Stub.request(Action, "deletenodedocumentsignature", {id: Id, signature: escape(utf8Encode(SignatureCode))});
};

Kernel.stampNodeDocumentSignature = function (Action, Id, CodeSignature, IdSignature, Signature) {
  Kernel.Stub.request(Action, "stampnodedocumentsignature", {id: Id, code: CodeSignature, signid: IdSignature, signature: escape(utf8Encode(Signature))});
};

Kernel.getSignatureItemsLink = function (IdNode) {
  var sParameters = "op=loadsignatureitems&id=" + IdNode;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.uploadNodeFile = function (Action, DOMForm) {
  DOMForm.enctype = "multipart/form-data";
  Kernel.Stub.upload(Action, "uploadnodefile", DOMForm);
};

Kernel.searchNodes = function (Action, sCondition, Id, CodeType, Mode) {
  Kernel.Stub.request(Action, "searchnodes", {query: escape(utf8Encode(sCondition)), id: Id, code: CodeType, mode: escape(Mode)});
};

Kernel.executeNodeCommand = function (Action, IdNode, sCommand, sParameters) {
  Kernel.Stub.request(Action, "executenodecommand", {id: IdNode, name: sCommand, data: escape(utf8Encode(sParameters))});
};

Kernel.executeNodeCommandOnAccept = function (Action, IdNode, sCommand, sParameters) {
  Kernel.Stub.request(Action, "executenodecommandonaccept", {id: IdNode, name: sCommand, data: escape(utf8Encode(sParameters))});
};

Kernel.executeNodeCommandOnCancel = function (Action, IdNode, sCommand, sParameters) {
  Kernel.Stub.request(Action, "executenodecommandoncancel", {id: IdNode, name: sCommand, data: escape(utf8Encode(sParameters))});
};

Kernel.getDownloadNodeCommandFileLink = function (Name) {
  var sParameters = "op=downloadnodecommandfile&name=" + escape(utf8Encode(Name));
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.loadNodesFromTrash = function (Action) {
  Kernel.Stub.request(Action, "loadnodesfromtrash");
};

Kernel.loadNodeFieldCompositeItem = function (Action, Id, sCode, iPosition) {
  Kernel.Stub.request(Action, "loadnodefieldcompositeitem", {id: Id, code: sCode, position: iPosition});
};

Kernel.loadNodeFieldCheckTerms = function (Action, id, code) {
  Kernel.Stub.request(Action, "loadnodefieldcheckterms", { id: id, code: code });
};

Kernel.loadNodeContext = function (Action, Id) {
  Kernel.Stub.request(Action, "loadnodecontext", {id: Id});
};

Kernel.getLoadNodesFromTrashLink = function (Action) {
  var sParameters = "op=loadnodesfromtrash";
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.focusNodeView = function (Action, sInstanceId, IdNode, sTimestampValue) {
  Kernel.Stub.request(Action, "focusnodeview", {i: sInstanceId, idnode: IdNode, timestamp: sTimestampValue});
};

Kernel.focusNodeField = function (Action, sInstanceId, IdNode, sFieldPath) {
  Kernel.Stub.zoombieRequest("focusnodefield", {i: sInstanceId, idnode: IdNode, field: sFieldPath});
};

Kernel.blurNodeField = function (Action, sInstanceId, IdNode, sFieldPath) {
  Kernel.Stub.request(Action, "blurnodefield", {i: sInstanceId, idnode: IdNode, field: sFieldPath});
};

Kernel.emptyTrash = function (Action) {
  Kernel.Stub.request(Action, "emptytrash");
};

Kernel.recoverNodeFromTrash = function (Action, Id) {
  Kernel.Stub.request(Action, "recovernodefromtrash", {id: Id});
};

Kernel.recoverNodesFromTrash = function (Action, Data) {
  Kernel.Stub.request(Action, "recovernodesfromtrash", {data: Data});
};

Kernel.getNodeItemsLink = function (IdNode, Code, CodeView) {
  var sParameters = "op=loadnodeitems&id=" + IdNode + "&code=" + Code + "&view=" + CodeView;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.getNodeItemsLocationsCountLink = function (IdNode, Code, CodeView, Filters) {
    var sParameters = "op=loadnodeitemslocationscount&id=" + IdNode + "&code=" + Code + "&view=" + CodeView + Filters;
    return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.getNodeItemsLocationsLink = function (IdNode, Code, CodeView, Filters) {
  var sParameters = "op=loadnodeitemslocations&id=" + IdNode + "&code=" + Code + "&view=" + CodeView + Filters;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.getNodeSetItemsLink = function (Id, Set, Code, CodeView) {
  var sParameters = "op=loadsetitems&id=" + Id + "&set=" + Set + "&code=" + Code + "&view=" + CodeView;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.getNodeAttachmentItemsLink = function (Id, Code, CodeView) {
  var sParameters = "op=loadattachmentitems&id=" + Id + "&code=" + Code + "&view=" + CodeView;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.getNodeGroupByOptionsLink = function (IdNode, CodeView) {
	var sParameters = "op=loadnodegroupbyoptions&id=" + IdNode + "&view=" + CodeView;
	return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.loadNodeRevision = function (Action, Id, IdNode, Mode, CodeView) {
  var Parameters = {id: Id, idnode: IdNode, mode: escape(Mode)};
  if (CodeView != null) Parameters.view = CodeView;
  Kernel.Stub.request(Action, "loadnoderevision", Parameters);
};

Kernel.loadCurrentNodeRevision = function (Action, IdNode, Mode, CodeView) {
  var Parameters = { idnode: IdNode, mode: escape(Mode) };
  if (CodeView != null) Parameters.view = CodeView;
  Kernel.Stub.request(Action, "loadcurrentnoderevision", Parameters);
};

Kernel.restoreNodeRevision = function (Action, Id, IdNode, Mode) {
  Kernel.Stub.request(Action, "restorenoderevision", {id: Id, idnode: IdNode, mode: escape(Mode)});
};

Kernel.getNodeRevisionItemsLink = function (IdNode) {
  var sParameters = "op=loadnoderevisionitems&id=" + IdNode;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.loadDefaultValue = function (Action, CodeNodeType, sProperty) {
  Kernel.Stub.request(Action, "loaddefaultvalue", {nodetype: CodeNodeType, property: sProperty});
};

Kernel.addDefaultValue = function (Action, CodeNodeType, sProperty, sData) {
  Kernel.Stub.request(Action, "adddefaultvalue", {nodetype: CodeNodeType, property: sProperty, data: escape(utf8Encode(sData))});
};

Kernel.createSequenceValue = function (Action, Sequence) {
  Kernel.Stub.request(Action, "createsequencevalue", {code: Sequence});
};

Kernel.loadNodeHelperPage = function (Action, Code) {
  Kernel.Stub.request(Action, "loadnodehelperpage", {code: Code});
};

Kernel.loadHelperPage = function (Action, Path) {
  Kernel.Stub.request(Action, "loadhelperpage", {path: Path});
};

Kernel.loadTemplate = function (Action, Code) {
  Kernel.Stub.request(Action, "loadtemplate", {code: Code});
};

Kernel.loadTask = function (Action, Id, Mode) {
  Kernel.Stub.request(Action, "loadtask", {id: Id, mode: escape(Mode)});
};

Kernel.loadTaskFilters = function (Action) {
  Kernel.Stub.request(Action, "loadtaskfilters");
};

Kernel.createTask = function (Action, Code, Mode, sTitle) {
  var Parameters = new Object();

  Parameters.code = Code;
  if (Mode != null) Parameters.mode = escape(Mode);
  if (sTitle != null) Parameters.title = escape(utf8Encode(sTitle));

  Kernel.Stub.request(Action, "createtask", Parameters);
};

Kernel.alertEntity = function (Action, idTask, UserList, sNotes, sType) {
  var UsersIds = UserList.getUsersIds();
  Kernel.Stub.request(Action, "alertentity", {id: idTask, users: UsersIds, data: sNotes, type: sType});
};

Kernel.selectTaskDelegationRole = function (Action, idTask, idRole) {
  Kernel.Stub.request(Action, "selecttaskdelegationrole", {id: idTask, role: idRole});
};

Kernel.setupTaskDelegation = function (Action, idTask) {
  Kernel.Stub.request(Action, "setuptaskdelegation", {id: idTask});
};

Kernel.selectTaskSendJobRole = function (Action, idTask, idRole) {
  var parameters = new Object();
  parameters.id = idTask;
  if (idRole != null) parameters.role = idRole;
  Kernel.Stub.request(Action, "selecttasksendjobrole", parameters);
};

Kernel.setupTaskSendJob = function (Action, idTask) {
  Kernel.Stub.request(Action, "setuptasksendjob", {id: idTask});
};

Kernel.setupTaskEnroll = function (Action, idTask, idContest) {
  Kernel.Stub.request(Action, "setuptaskenroll", {id: idTask, contest: idContest});
};

Kernel.setupTaskWait = function (Action, idTask, quantity, command) {
  Kernel.Stub.request(Action, "setuptaskwait", {id: idTask, quantity: quantity, cmd: command});
};

Kernel.solveTaskLine = function (Action, idTask, codePlace, codeStop) {
  Kernel.Stub.request(Action, "solvetaskline", {id: idTask, place: codePlace, stop: codeStop});
};

Kernel.solveTaskEdition = function (Action, idTask) {
  Kernel.Stub.request(Action, "solvetaskedition", {id: idTask});
};

Kernel.toggleTaskUrgency = function (Action, idTask) {
  Kernel.Stub.request(Action, "toggletaskurgency", {id: idTask});
};

Kernel.setTaskOwner = function (Action, username, reason, idTask) {
  Kernel.Stub.request(Action, "settaskowner", {name: username, reason: escape(utf8Encode(reason)), idtask: idTask});
};

Kernel.setTasksOwner = function (Action, username, reason, data) {
  Kernel.Stub.request(Action, "settasksowner", {name: username, reason: escape(utf8Encode(reason)), data: data});
};

Kernel.unsetTaskOwner = function (Action, idTask) {
  Kernel.Stub.request(Action, "unsettaskowner", {id: idTask});
};

Kernel.unsetTasksOwner = function (Action, data) {
  Kernel.Stub.request(Action, "unsettasksowner", {data: data});
};

Kernel.getTaskOrderItemsLink = function (Id) {
  var sParameters = "op=loadtaskorderitems&id=" + Id;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.resetTaskOrderNewMessages = function (idTask, idOrder) {
  Kernel.Stub.zoombieRequest("resettaskordernewmessages", {id: idTask, order: idOrder});
};

Kernel.getBusinessModelFileLink = function () {
  var sParameters = "op=loadbusinessmodelfile";
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.getBusinessModelDefinitionLink = function () {
  var sParameters = "op=loadbusinessmodeldefinition&lang=" + Context.Config.Language;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.loadTaskHistory = function (Action, taskId, startpos, limit) {
  Kernel.Stub.request(Action, "loadtaskhistory", {id: taskId, start: startpos, limit: limit});
};

Kernel.abortTask = function (Action, Id) {
  Kernel.Stub.request(Action, "aborttask", {id: Id});
};

Kernel.getTaskOrderChatItemsLink = function (IdTask, IdOrder) {
  var sParameters = "op=loadtaskorderchatitems&id=" + IdTask + "&order=" + IdOrder;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.sendTaskOrderChatMessage = function (Action, IdTask, IdOrder, message) {
  Kernel.Stub.request(Action, "sendtaskorderchatmessage", {id: IdTask, order: IdOrder, message: escape(utf8Encode(message))});
};

Kernel.sendTaskRequest = function (Action, IdTask) {
  Kernel.Stub.request(Action, "sendtaskrequest", {id: IdTask});
};

Kernel.sendTaskResponse = function (Action, IdTask) {
  Kernel.Stub.request(Action, "sendtaskresponse", {id: IdTask});
};

Kernel.loadNodeLocation = function (Action, Id) {
  Kernel.Stub.request(Action, "loadnodelocation", {id: Id});
};

Kernel.updateNodeLocation = function (Action, Id, Location) {
  Kernel.Stub.request(Action, "updatenodelocation", {id: Id, location: Location});
};

Kernel.cleanNodeLocation = function (Action, Id) {
  Kernel.Stub.request(Action, "cleannodelocation", {id: Id});
};

Kernel.addCommentToPost = function (Action, PostId, Text) {
  Kernel.Stub.request(Action, "addcommenttopost", {postid: PostId, text: Text});
};

Kernel.addPost = function (Action, Text) {
  Kernel.Stub.request(Action, "addpost", {text: Text});
};

Kernel.loadNewsNextPage = function (Action, Start, Limit) {
  Kernel.Stub.request(Action, "loadnewsnextpage", {start: Start, limit: Limit});
};

Kernel.addFilter = function (Action, PostId, Filter) {
  Kernel.Stub.request(Action, "addfilter", {postid: PostId, filter: Filter});
};

Kernel.getLoadTasksLink = function (Inbox) {
  var sParameters = "op=loadtasks&inbox=" + Inbox;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.loadAccountPendingTasks = function(Action) {
    Kernel.Stub.request(Action, "loadaccountpendingtasks");
};

Kernel.sendMail = function (sSubject, sBody) {
  Kernel.Stub.zoombieRequest("sendmail", {subject: escape(sSubject), body: escape(utf8Encode(sBody))});
};

Kernel.sendSuggestion = function (Action, sBody) {
  Kernel.Stub.request(Action, "sendsuggestion", {body: escape(utf8Encode(sBody))});
};

Kernel.exiting = function (sInstanceId) {
  Kernel.Stub.zoombieRequest("exiting", {i: sInstanceId});
};

Kernel.notificationsRead = function (ids) {
  Kernel.Stub.zoombieRequest("notificationsread", {ids: ids});
};

Kernel.notificationsReadAll = function (ids) {
  Kernel.Stub.zoombieRequest("notificationsreadall", {});
};

Kernel.loadSystemTemplate = function (Action, Code, View, Extra) {
  Kernel.Stub.request(Action, "loadsystemtemplate", {code: Code, view: View, extra: Extra});
};

Kernel.loadDashboardTemplate = function (Action, Code, View) {
  Kernel.Stub.request(Action, "loaddashboardtemplate", {code: Code, view: View});
};

Kernel.loadNotifications = function (Action, iStart, iLimit) {
  Kernel.Stub.request(Action, "loadnotifications", {start: iStart, limit: iLimit});
};

Kernel.getLoadNotificationsLink = function () {
  var sParameters = "op=loadnotifications";
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.loadSource = function (Action, Id) {
  Kernel.Stub.request(Action, "loadsource", {id: Id});
};

Kernel.getLoadSourceListLink = function () {
  var sParameters = "op=loadsourcelist";
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.loadSourceNewTerms = function (Action, Id) {
  Kernel.Stub.request(Action, "loadsourcenewterms", {id: Id});
};

Kernel.getLoadSourceTermsLink = function (IdSource, CodeFromTerm, Mode, iDepth) {
  var sParameters = "op=loadsourceterms&id=" + IdSource + "&from=" + CodeFromTerm + "&mode=" + Mode + "&depth=" + iDepth;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.addSourceTerm = function (Action, IdSource, CodeParent, Code, sLabel) {
  Kernel.Stub.request(Action, "addsourceterm", {idsource: IdSource, parent: CodeParent, code: Code, label: sLabel});
};

Kernel.saveSourceTermAttribute = function (Action, IdSource, Code, Attribute, sValue) {
  Kernel.Stub.request(Action, "savesourcetermattribute", {idsource: IdSource, code: Code, attribute: Attribute, data: escape(utf8Encode(sValue))});
};

Kernel.deleteSourceTerm = function (Action, IdSource, Code) {
  Kernel.Stub.request(Action, "deletesourceterm", {idsource: IdSource, code: Code});
};

Kernel.existsSourceTerm = function (Action, IdSource, Code) {
  Kernel.Stub.request(Action, "existssourceterm", {idsource: IdSource, code: Code});
};

Kernel.publishSourceTerms = function (Action, IdSource, sTerms) {
  Kernel.Stub.request(Action, "publishsourceterms", {idsource: IdSource, terms: escape(utf8Encode(sTerms))});
};

Kernel.loadRoleDefinitionList = function (Action) {
  Kernel.Stub.request(Action, "loadroledefinitionlist", {start: 0, limit: -1});
};

Kernel.getLoadRoleDefinitionListLink = function () {
  var sParameters = "op=loadroledefinitionlist";
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.getLoadRoleListLink = function (CodeDefinition, CodeView) {
  var sParameters = "op=loadrolelist&code=" + CodeDefinition + "&view=" + CodeView;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.getLoadGroupedRoleListLink = function (CodeDefinition, GroupedId) {
  var sParameters = "op=loadgroupedrolelist&code=" + CodeDefinition + "&groupedid=" + GroupedId;
  return Context.Config.Api + writeServerRequest(Kernel.mode, sParameters);
};

Kernel.addUserRole = function (Action, Code, IdUser, Username, sBeginDate, sExpireDate) {
  var Parameters = {code: Code, iduser: IdUser, name: Username, begin: sBeginDate};
  if (sExpireDate != null && sExpireDate != "") Parameters.expire = sExpireDate;
  Kernel.Stub.request(Action, "adduserrole", Parameters);
};

Kernel.addServiceRole = function (Action, code, partnerId, partnerServiceName, beginDate, expireDate) {
  var Parameters = {code: code, begin: beginDate};
  if (partnerId != null) Parameters.partnerid = partnerId;
  if (partnerServiceName != null) Parameters.partnerservicename = partnerServiceName;
  if (expireDate != null && expireDate != "") Parameters.expire = expireDate;
  Kernel.Stub.request(Action, "addservicerole", Parameters);
};

Kernel.addFeederRole = function (Action, code, partnerId, partnerServiceName, beginDate, expireDate) {
  var Parameters = {code: code, begin: beginDate};
  if (partnerId != null) Parameters.partnerid = partnerId;
  if (partnerServiceName != null) Parameters.partnerservicename = partnerServiceName;
  if (expireDate != null && expireDate != "") Parameters.expire = expireDate;
  Kernel.Stub.request(Action, "addfeederrole", Parameters);
};

Kernel.saveUserRole = function (Action, IdRole, IdUser, sBeginDate, sExpireDate) {
  var Parameters = {id: IdRole, iduser: IdUser, begin: sBeginDate};
  if (sExpireDate != null && sExpireDate != "") Parameters.expire = sExpireDate;
  Kernel.Stub.request(Action, "saveuserrole", Parameters);
};

Kernel.saveServiceRole = function (Action, IdRole, partnerId, partnerServiceName, beginDate, expireDate) {
  var Parameters = {id: IdRole, partnerid: partnerId, partnerservicename: partnerServiceName, begin: beginDate};
  if (expireDate != null && expireDate != "") Parameters.expire = expireDate;
  Kernel.Stub.request(Action, "saveservicerole", Parameters);
};

Kernel.saveFeederRole = function (Action, IdRole, partnerId, partnerServiceName, beginDate, expireDate) {
  var Parameters = {id: IdRole, partnerid: partnerId, partnerservicename: partnerServiceName, begin: beginDate};
  if (expireDate != null && expireDate != "") Parameters.expire = expireDate;
  Kernel.Stub.request(Action, "savefeederrole", Parameters);
};

Kernel.loadTeam = function (Action) {
  Kernel.Stub.request(Action, "loadteam", {});
};

Kernel.registerException = function (exception) {
  var sException = "";
  Desktop.hideReports();
  if (exception.name) sException += "name: " + exception.name + "\n";
  if (exception.fileName) sException += "filename: " + exception.fileName + "\n";
  if (exception.message) sException += "message: " + exception.message + "\n";
  sException += "stack:\n" + getStackTrace(exception);
  if (Context.Debugging) RequestExceptionWithMessage(exception.message, getStackTrace(exception));
  else Kernel.Stub.zoombieRequest("registerexception", {data: escape(utf8Encode(sException))});
};