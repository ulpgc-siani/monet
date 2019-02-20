CGHelperItemConstructor = function () {
};

CGHelperItemConstructor.prototype.getDecorator = function (extObject) {
  if (extObject.hasClass(CLASS_TASK_LIST)) return new CGDecoratorHelperItemTaskList();
  return new CGDecoratorHelperItem();
};

CGHelperItemConstructor.prototype.initTaskListInstances = function (extObject, decorator) {
  if (!extObject.hasClass(CLASS_TASK_LIST)) return;

  var aExtTaskListInstances = extObject.select(CSS_TASK_LIST);
  aExtTaskListInstances.each(function (extInstance) {
    decorator.execute(extInstance.dom);
  }, this);
};

CGHelperItemConstructor.prototype.init = function (DOMObject) {
  var extObject = Ext.get(DOMObject);
  var decorator = this.getDecorator(extObject);

  decorator.execute(extObject.dom);

  this.initTaskListInstances(extObject, decorator);
};