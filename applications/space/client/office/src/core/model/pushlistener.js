PushListener = new Object;

PushListener.nodeFieldFocused = function (Sender) {
  var Process = new CGProcessFocusNodeField();
  Process.Node = Sender.Node;
  Process.DOMField = Sender.DOMField;
  Process.execute();
};

PushListener.nodeFieldCompositeChanged = function (Sender) {
  PushListener.resetObserverFields(Sender.DOMNode, Sender.DOMField.getCode());
};

PushListener.nodeFieldChanged = function (Sender) {

  var Process = new CGProcessSaveNodeAttribute();
  Process.Node = Sender.Node;
  Process.DOMNode = Sender.DOMNode;
  Process.Data = Sender.DOMField.getContent();
  Process.execute();

  PushListener.resetObserverFields(Sender.DOMNode, Sender.DOMField.getCode());
};

PushListener.resetObserverFields = function (DOMElement, fieldCode) {
  var DOMFields = DOMElement.getFields();
  for (var i = 0; i < DOMFields.length; i++) {
    var DOMField = DOMFields[i];
    if (DOMField.isObserver(fieldCode))
      DOMField.reset();
    if (DOMField.isComposite())
      PushListener.resetObserverFields(DOMField, fieldCode);
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