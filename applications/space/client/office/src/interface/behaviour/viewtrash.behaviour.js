function CGBehaviourViewTrash(Config) {
  this.base = CGBehaviour;
  this.base(Config);
  this.iTimeLeft = 25;
};

CGBehaviourViewTrash.prototype = new CGBehaviour;

CGBehaviourViewTrash.prototype.init = function (extObject) {

  this.initLayer();

  var aTrashNodes = extObject.select(".trashnode a");
  aTrashNodes.each(function (extTrashNode) {
    TrashNode = extTrashNode.dom;
    Event.observe(TrashNode, 'mouseover', CGBehaviourViewTrash.prototype.showDelayedContextualPanel.bind(this, TrashNode));
    Event.observe(TrashNode, 'mouseout', CGBehaviourViewTrash.prototype.hideDelayedContextualPanel.bind(this, TrashNode));
  }, this);

};

BehaviourViewTrash = new CGBehaviourViewTrash({cls: "trashnodefloatinglayer"});