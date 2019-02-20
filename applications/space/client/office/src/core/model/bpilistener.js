function CGListenerBPI() {
};

//---------------------------------------------------------------------
CGListenerBPI.prototype.nodeOpened = function (Sender) {
  var Behaviour, BPINode;

  if (!Sender.Node) return;
  if (!Sender.DOMNode) return;

  Behaviour = Extension.getDefinitionBehaviour(Sender.Node.Code);
  if (!Behaviour) return;

  BPINode = new CGBPINode(Sender.Node, Sender.DOMNode);

  if (Behaviour.Events.refresh) Behaviour.Events.refresh(BPINode);
};

//---------------------------------------------------------------------
CGListenerBPI.prototype.nodeFieldChanged = function (Sender) {
  var Behaviour, BPINode;

  if (!Sender.Node) return;
  if (!Sender.DOMNode) return;
  if (!Sender.DOMField) return;

  Behaviour = Extension.getDefinitionBehaviour(Sender.Node.Code);
  if (!Behaviour) return;

  BPINode = new CGBPINode(Sender.Node, Sender.DOMNode);

  if (Behaviour.Events.refresh) Behaviour.Events.refresh(BPINode);
};