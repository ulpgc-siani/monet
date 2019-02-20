BehaviourDispatcher = new Object;

BehaviourDispatcher.apply = function (Behaviour, Object) {
  var eElement = Ext.get(Object);
  if (!eElement) return;

  if (!Behaviour) return;
  Behaviour.init(eElement);
};