CGWidgetFormula = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);
};

CGWidgetFormula.prototype = new CGWidget;

CGWidgetFormula.prototype.init = function () {
  this.bIsReady = true;
};

// #############################################################################################################

CGWidgetFormula.prototype.atFocused = function (oEvent) {
  if (!this.isReady()) this.init();
  this.extValue.addClass(CLASS_FOCUS);
};

CGWidgetFormula.prototype.atChange = function (oEvent) {
};