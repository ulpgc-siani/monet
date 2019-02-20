CGWidgetDescriptor = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);
};

CGWidgetDescriptor.prototype = new CGWidget;

CGWidgetDescriptor.prototype.init = function () {
  this.bIsReady = true;
};

CGWidgetDescriptor.prototype.validate = function () {
  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";
};

CGWidgetDescriptor.prototype.setTarget = function (Target) {
  this.Target = Target;
  this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());
  this.validate();
  //this.updateData();
};

CGWidgetDescriptor.prototype.getData = function () {
  if (!this.Target) return null;
  if (!this.Target.getCode) return null;
  if (!this.extValue) return null;

  var Result = new Object();
  Result.code = this.Target.getCode();
  Result.order = -1;
  Result.value = new Array();
  Result.value.push({code: CGIndicator.DESCRIPTOR, order: 1, value: this.Target.getDescriptorName()});
  Result.value.push({code: CGIndicator.RESULTTYPE, order: 2, value: this.Target.getResultType()});
  Result.value.push({code: CGIndicator.VALUE, order: 3, value: this.extValue.dom.value});
  if (this.Target.isSuper() && (this.extSuper != null)) Result.value.push({code: CGIndicator.SUPER, order: 4, value: this.extSuper.dom.value});

  Result.toXml = function () {
    var Attribute = new CGAttribute();
    Attribute.code = this.code;
    Attribute.iOrder = this.order;
    for (var iPos = 0; iPos < this.value.length; iPos++) {
      Attribute.addIndicatorByValue(this.value[iPos].code, this.value[iPos].order, this.value[iPos].value);
    }
    return Attribute.serialize();
  };

  return Result;
};

// #############################################################################################################

CGWidgetDescriptor.prototype.atFocused = function (oEvent) {
  if (!this.Target) return;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused();

  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = "none";

  this.extValue.addClass(CLASS_FOCUS);
  this.extValue.dom.select();
};

CGWidgetDescriptor.prototype.atChange = function (oEvent) {
  this.validate();
  this.updateData();
};