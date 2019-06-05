PushListener = new Object;

PushListener.nodeFieldFocused = function (Sender) {
  var Process = new CGProcessFocusNodeField();
  Process.Node = Sender.Node;
  Process.DOMField = Sender.DOMField;
  Process.execute();
};

PushListener.nodeFieldCompositeChanged = function (Sender) {
  PushListener.resetObserverFields(Sender.DOMNode, Sender.DOMField);
};

PushListener.nodeFieldChanged = function (Sender) {

  var Process = new CGProcessSaveNodeAttribute();
  Process.Node = Sender.Node;
  Process.DOMNode = Sender.DOMNode;
  Process.Data = Sender.DOMField.getContent();
  Process.execute();

  PushListener.resetObserverFields(Sender.DOMNode, Sender.DOMField);
};

PushListener.resetObserverFields = function (DOMElement, DOMSender) {
  var DOMChildren = DOMElement.getFields();
  var fieldCode = DOMSender.getCode();
  for (var i = 0; i < DOMChildren.length; i++) {
    var DOMChild = DOMChildren[i];
    var DOMBrother = DOMChild.getBrother(fieldCode);
    var childBrotherId = DOMBrother ? DOMBrother.id : null;
    var sameParent = childBrotherId !== DOMChild.id && childBrotherId === DOMSender.id && DOMElement.isComposite();
    if (sameParent && DOMChild.isObserver(fieldCode))
      DOMChild.reset();
    if (DOMChild.isComposite())
      PushListener.resetObserverFields(DOMChild, DOMSender);
  }
};

PushListener.nodeFocused = function (Sender) {
  /*var Process = new CGProcessFocusNodeView();
   Process.Node = Sender.Node;
   Process.DOMNode = Sender.DOMNode;
   Process.RefreshNode = false;
   Process.execute();*/
};

PushListener.nodeViewFocused = function (Sender) {
  var Process = new CGProcessFocusNodeView();
  Process.Node = Sender.Node;
  Process.DOMNode = Sender.DOMNode;
  Process.RefreshNodeView = false;
  Process.execute();
};

PushListener.nodeBlur = function (Sender) {
  var Process = new CGProcessBlurNodeView();
  Process.Node = Sender.Node;
  Process.DOMNode = Sender.DOMNode;
  Process.execute();
};