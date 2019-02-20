CGDecoratorHelperItemTaskList = function () {
};

CGDecoratorHelperItemTaskList.prototype = new CGDecorator;

CGDecoratorHelperItemTaskList.prototype.execute = function (DOMHelperItem) {

  this.addCommonMethods(DOMHelperItem);

  DOMHelperItem.getId = function () {
    return DOMHelperItem.id;
  };

  DOMHelperItem.init = function () {
    this.initToolbar(".info .toolbar");
    this.initTabs(CSS_TASK_LIST);
  };

  DOMHelperItem.executeOnloadCommands = function () {
    var extElement = Ext.get(this);

    var aExtOnloadCommands = extElement.select(CSS_ONLOAD_COMMAND);
    aExtOnloadCommands.each(function (extOnloadCommand) {
      CommandListener.throwCommand(extOnloadCommand.dom.innerHTML);
    }, this);
  };

  // #############################################################################################################

  DOMHelperItem.atWindowResize = function () {
    this.resize();
  };

};