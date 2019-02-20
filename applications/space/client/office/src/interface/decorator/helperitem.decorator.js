CGDecoratorHelperItem = function () {
};

CGDecoratorHelperItem.prototype = new CGDecorator;

CGDecoratorHelperItem.prototype.execute = function (DOMHelperItem) {

  this.addCommonMethods(DOMHelperItem);

  DOMHelperItem.init = function () {
    this.executeOnloadCommands();
    this.initToolbar(".header .content .toolbar");

    this.resize();
    Ext.EventManager.onWindowResize(DOMHelperItem.atWindowResize.bind(this));
  };

  DOMHelperItem.resize = function () {
    var extElement = Ext.get(this);
    var extLayer = extElement.select("div.content").first();
    if (extLayer) {
      var extTabs = extElement.up(".x-tabs-body.x-layout-tabs-body");
      var iHeight = (extTabs != null) ? extTabs.getHeight() : 0;
      var iOffsetHeight = extElement.getHeight() - extLayer.getHeight();
      extLayer.dom.style.height = (iHeight - iOffsetHeight) + "px";
      extLayer.dom.style.overflowY = "auto";
      extLayer.dom.style.overflowX = "hidden";
    }
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