BehaviourDispatcher = new Object;

BehaviourDispatcher.apply = function(Behaviour, Object){
  
  var eElement = Ext.get(Object);
  if (! eElement) return false;

  if (! Behaviour) return false;
  Behaviour.init(eElement);
};