CGDecoratorSection = function () {
};

CGDecoratorSection.prototype = new CGDecorator;

CGDecoratorSection.prototype.execute = function (DOMSection) {

  DOMSection.open = function () {
    var extSection = Ext.get(this);
    extSection.addClass(CLASS_OPENED);
    extSection.removeClass(CLASS_CLOSED);
  };

  DOMSection.close = function () {
    var extSection = Ext.get(this);
    extSection.removeClass(CLASS_OPENED);
    extSection.addClass(CLASS_CLOSED);
  };

  DOMSection.toggle = function () {
    if (this.hasClassName(CLASS_OPENED)) {
      this.close();
    }
    else {
      this.open();
    }
  };

  DOMSection.registerBehaviours = function () {
    var toggleBehaviour = Ext.get(this).select(".behaviour.toggle").first();
    toggleBehaviour.on("click", DOMSection.atToggle.bind(this));
  };

  DOMSection.atToggle = function () {
    this.toggle();
  };

  DOMSection.registerBehaviours();

};
