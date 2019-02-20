BPI = new Object;

//---------------------------------------------------------------------
BPI.dispatch = function (Process, Callback) {
  var ProcessBPIDispatch;
  ProcessBPIDispatch = new CGProcessBPIDispatch();
  ProcessBPIDispatch.Process = Process;
  ProcessBPIDispatch.Callback = Callback;
  ProcessBPIDispatch.execute();
};

//---------------------------------------------------------------------
BPI.debug = function (message) {
  if (Ext.isIE) return;
  if (!console) return;
  console.log(message);
};

//---------------------------------------------------------------------
BPI.saveAccount = function (AccountInfo, CallbackProcess) {

  if (arguments.length < 2) {
    Desktop.reportBPIError(Lang.BPI.SaveAccount.Parameters);
    return;
  }

  Account.getUser().setInfo(AccountInfo);

  var ProcessSaveAccount = new CGProcessSaveAccount();
  ProcessSaveAccount.Account = Account;

  BPI.dispatch(ProcessSaveAccount, BPI.saveAccountCallback.createCallback(CallbackProcess));
};
BPI.saveAccountCallback = function (CallbackProcess) {
  var Process = new CGProcessLoadAccount();
  Process.execute();
  CallbackProcess.Result = true;
  CallbackProcess.execute();
};

//---------------------------------------------------------------------
BPI.getNode = function (Id, CallbackProcess) {

  if (arguments.length < 2) {
    Desktop.reportBPIError(Lang.BPI.GetNode.Parameters);
    return;
  }

  var Node = NodesCache.get(Id);
  var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Id);
  if ((Node) && (ViewNode)) {
    BPI.getNodeCallback(Id, CallbackProcess);
    return;
  }

  var ProcessShowNode = new CGProcessShowNode();
  ProcessShowNode.Id = Id;
  ProcessShowNode.ActivateNode = false;

  BPI.dispatch(ProcessShowNode, BPI.getNodeCallback.createCallback(Id, CallbackProcess));
};
BPI.getNodeCallback = function (Id, CallbackProcess) {
  var Node, ViewNode;

  Node = NodesCache.get(Id);
  if (!Node) {
    Desktop.reportBPIError(Lang.BPI.GetNode.Failure.replace("#id#", Id));
    return;
  }

  ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId());
  if (!ViewNode) {
    Desktop.reportBPIError(Lang.BPI.GetNode.Failure.replace("#id#", Id));
    return;
  }

  CallbackProcess.Result = new CGBPINode(Node, ViewNode.getDOM());
  CallbackProcess.execute();
};

//---------------------------------------------------------------------
BPI.getNodeNotes = function (Node, CallbackFunction) {

  if (arguments.length < 2) {
    Desktop.reportBPIError(Lang.BPI.GetNodeNotes.Parameters);
    return;
  }

  var ProcessLoadNodeNotes = new CGProcessLoadNodeNotes();
  ProcessLoadNodeNotes.Node = Node;

  BPI.dispatch(ProcessLoadNodeNotes, BPI.getNodeNotesCallback.createCallback(Node, CallbackFunction));
};
BPI.getNodeNotesCallback = function (Node, CallbackFunction) {
  CallbackFunction(Node.Notes);
};

//---------------------------------------------------------------------
BPI.saveNode = function (Id, CallbackProcess) {

  if (arguments.length < 2) {
    Desktop.reportBPIError(Lang.BPI.SaveNode.Parameters);
    return;
  }

  var ProcessSaveNode = new CGProcessSaveNode();
  ProcessSaveNode.Id = Id;

  BPI.dispatch(ProcessSaveNode, BPI.saveNodeCallback.createCallback(Id, CallbackProcess));
};
BPI.saveNodeCallback = function (Id, CallbackProcess) {
  var Node, ViewNode;

  Node = NodesCache.get(Id);
  if (!Node) {
    Desktop.reportBPIError(Lang.BPI.SaveNode.Failure.replace("#id#", Id));
    return;
  }

  ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId());
  if (!ViewNode) {
    Desktop.reportBPIError(Lang.BPI.SaveNode.Failure.replace("#id#", Id));
    return;
  }

  if (CallbackProcess != null) {
    CallbackProcess.Result = new CGBPINode(Node, ViewNode.getDOM());
    CallbackProcess.execute();
  }
};

//---------------------------------------------------------------------
BPI.createNode = function (Code, IdParent, CallbackProcess) {

  if (arguments.length < 3) {
    Desktop.reportBPIError(Lang.BPI.CreateNode.Parameters);
    return;
  }

  var ProcessAddNodeBlank = new CGProcessAddNodeBlank();
  ProcessAddNodeBlank.Code = Code;
  ProcessAddNodeBlank.IdParent = IdParent;
  ProcessAddNodeBlank.ActivateNode = false;

  BPI.dispatch(ProcessAddNodeBlank, BPI.createNodeCallback.createDelegate(BPI, [CodeType, IdParent, CallbackProcess], true));
};
BPI.createNodeCallback = function (NewNode, CodeType, Parent, CallbackProcess) {
  BPI.getNode(NewNode.getId(), CallbackProcess);
};

//---------------------------------------------------------------------
BPI.removeNode = function (Id, CallbackProcess) {

  if (arguments.length < 2) {
    Desktop.reportBPIError(Lang.BPI.RemoveNode.Parameters);
    return;
  }

  var ProcessDeleteNodes = new CGProcessDeleteNodes();
  ProcessDeleteNodes.Nodes = [Id];

  BPI.dispatch(ProcessDeleteNodes, BPI.removeNodeCallback.createCallback(Id, CallbackProcess));
};
BPI.removeNodeCallback = function (Id, CallbackProcess) {
  if (CallbackProcess == null) return;
  CallbackProcess.execute();
};

//---------------------------------------------------------------------
BPI.executeCommand = function (sCommand) {
  CommandDispatcher.dispatch(sCommand);
};

//---------------------------------------------------------------------
BPI.redirect = function (sMonetLink) {
  var Expression = new RegExp(MONET_LINK_PATTERN, "g");
  var aResult = Expression.exec(sMonetLink);
  var sCommand = null;

  if (aResult != null) {
    if (aResult[1] == MONET_LINK_TYPE_NODE) {
      if (aResult.length == 3) sCommand = "shownode(" + aResult[2] + ",edit.html?mode=page)";
      else sCommand = "shownode(" + aResult[2] + ")";
    }
    else if (aResult[1] == MONET_LINK_TYPE_TASK) sCommand = "showtask(" + aResult[2] + ")";
  }

  if (sCommand == null) return;

  return BPI.executeCommand(sCommand);
};