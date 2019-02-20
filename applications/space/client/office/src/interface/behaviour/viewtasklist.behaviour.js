function CGBehaviourViewTaskList(Config) {
  this.base = CGBehaviour;
  this.base(Config);
  this.iTimeLeft = 25;
};

CGBehaviourViewTaskList.prototype = new CGBehaviour;

CGBehaviourViewTaskList.prototype.init = function (extObject) {

  this.initLayer();

  var aTasks = extObject.select(".task a");
  aTasks.each(function (extTask) {
    Task = extTask.dom;
    Event.observe(Task, 'mouseover', CGBehaviourViewTaskList.prototype.showDelayedContextualPanel.bind(this, Task));
    Event.observe(Task, 'mouseout', CGBehaviourViewTaskList.prototype.hideDelayedContextualPanel.bind(this, Task));
  }, this);

};

BehaviourViewTaskList = new CGBehaviourViewTaskList({cls: "taskfloatinglayer"});