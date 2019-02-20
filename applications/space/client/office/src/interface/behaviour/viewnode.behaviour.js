function CGBehaviourViewNode(Config) {
  this.base = CGBehaviour;
  this.base(Config);
};

CGBehaviourViewNode.prototype = new CGBehaviour;

CGBehaviourViewNode.prototype.init = function (extObject) {

  this.initLayer();

  var aTitles = extObject.select("a.title");
  aTitles.each(function (extTitle) {
    this.addObservers(extTitle);
  }, this);

};

CGBehaviourViewNode.prototype.addObservers = function (extObject) {
  eObject = extObject.dom;
  Event.observe(eObject, 'mouseover', CGBehaviourViewNode.prototype.showDelayedContextualPanel.bind(this, eObject));
  Event.observe(eObject, 'mouseout', CGBehaviourViewNode.prototype.hideDelayedContextualPanel.bind(this, eObject));
  Event.observe(eObject, 'click', CGBehaviourViewNode.prototype.hideContextualPanel.bind(this, eObject));
};

BehaviourViewNode = new CGBehaviourViewNode({cls: "nodefloatinglayer"});