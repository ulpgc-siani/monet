function CGBehaviourViewTask(Config) {
  this.base = CGBehaviour;
  this.base(Config);
};

CGBehaviourViewTask.prototype = new CGBehaviour;

CGBehaviourViewTask.prototype.init = function (extObject) {
  this.initLayer();
  var aTitles = extObject.select("a.title");
  aTitles.each(function (extTitle) {
    this.addObservers(extTitle);
  }, this);
};

CGBehaviourViewTask.prototype.addObservers = function (extObject) {
  eObject = extObject.dom;
  Event.observe(eObject, 'mouseover', CGBehaviourViewTask.prototype.showDelayedContextualPanel.bind(this, eObject));
  Event.observe(eObject, 'mouseout', CGBehaviourViewTask.prototype.hideDelayedContextualPanel.bind(this, eObject));
  Event.observe(eObject, 'click', CGBehaviourViewTask.prototype.hideContextualPanel.bind(this, eObject));
};

BehaviourViewTask = new CGBehaviourViewTask({cls: "taskfloatinglayer"});