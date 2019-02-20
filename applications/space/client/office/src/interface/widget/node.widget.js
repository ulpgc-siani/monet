CGWidgetNode = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);
  this.Item = new Object();
  this.Item.code = EMPTY;
  this.Item.value = EMPTY;

  if (!extWidget) return;

  this.extWidget.dom.blur = CGWidgetNode.prototype.blur.bind(this);
  this.extNodeBox = this.extWidget.down(CSS_WIDGET_ELEMENT_NODE_BOX);
  this.extAddButton = this.extWidget.down(CSS_WIDGET_ELEMENT_ADD);
  this.extAddButton.on("click", this.atAddButtonClick, this);

  this.extNodeContainer = null;
};

CGWidgetNode.prototype = new CGWidget;

CGWidgetNode.prototype.createOptions = function () {
  new Insertion.Bottom(this.extNodeBox.dom, WidgetLinkNodeBoxTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath}));
  this.extNodeContainer = this.extNodeBox.down(CSS_WIDGET_ELEMENT_NODE_CONTAINER);
  this.extNodeContainer.dom.innerHTML = WidgetLoadingTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath});
};

CGWidgetNode.prototype.init = function (code, value) {
  var allowAdd = this.Target.allowAdd();

  this.aCodeNodeTypes = this.Target.getNodeTypes();
  this.extAddButton.dom.style.display = (this.Item.code == "" && allowAdd) ? "block" : "none";

  if (this.Item.code == "" && !allowAdd) this.addNode();

  this.bIsReady = true;
};

CGWidgetNode.prototype.setItem = function (code, value) {
  this.Item.code = code;
  this.Item.value = value;
};

CGWidgetNode.prototype.getData = function () {
  if (!this.Target) return null;
  if (!this.Target.getCode) return null;

  var Result = new Object();
  Result.code = this.Target.getCode();
  Result.order = -1;
  Result.value = new Array();
  Result.value.push({code: CGIndicator.CODE, order: 1, value: this.Item.code});
  Result.value.push({code: CGIndicator.VALUE, order: 2, value: this.Item.value});
  Result.value.push({code: CGIndicator.NODE, order: 3, value: this.Item.code});
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

CGWidgetNode.prototype.setData = function (sData) {
  if (!this.Target) return;
  if (!this.extValue) return;

  var Attribute = new CGAttribute();
  Attribute.unserialize(sData);

  var IndicatorCode = Attribute.getIndicator(CGIndicator.CODE);
  var sValue = Attribute.getIndicatorValue(CGIndicator.VALUE);

  var code = null;
  if (IndicatorCode) code = IndicatorCode.getValue();
  if (code == null) {
    var IndicatorNode = Attribute.getIndicator(CGIndicator.NODE);
    code = IndicatorNode.getValue();
  }

  this.extValue.dom.name = code;
  this.extValue.dom.value = sValue;

  this.setItem(code, sValue);

  if (code == "") this.hideNode();
  else this.showNode(true);

  this.validate();

  if (this.onChange) this.onChange();
};

CGWidgetNode.prototype.setTarget = function (Target) {
  this.Target = Target;

  this.createRequiredWidget();
  this.createOptions();
  this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());
  this.setItem(this.extValue.dom.name, this.extValue.dom.value);

  this.extAddButton.dom.style.display = this.extValue.dom.name != "" ? "none" : "block";

  if (this.Item.code == "") this.hideNode();
  else this.showNode(true);

  //this.updateData();
};

CGWidgetNode.prototype.isExpanded = function () {
  return (this.extWidget.hasClass(CLASS_EXPANDED));
};

CGWidgetNode.prototype.expand = function () {
  if (this.isExpanded()) return;
  this.extWidget.addClass(CLASS_EXPANDED);
  this.extNodeBox.dom.style.display = "block";
};

CGWidgetNode.prototype.isCollapsed = function () {
  return (!this.extWidget.hasClass(CLASS_EXPANDED));
};

CGWidgetNode.prototype.collapse = function (bAnimate) {
  this.extWidget.removeClass(CLASS_EXPANDED);
  this.extNodeBox.dom.style.display = "none";
};

CGWidgetNode.prototype.hideLoading = function () {
  var extLoading = this.extNodeContainer.down(CSS_WIDGET_ELEMENT_LOADING);
  if (extLoading) extLoading.dom.style.display = "none";
};

CGWidgetNode.prototype.addNode = function () {
  if (!this.Target.getNodeTemplates) return;

  var Process = new CGProcessAddFieldNode();
  var Templates = this.Target.getNodeTemplates();

  if (Templates.Edit == null) return;

  this.NewItem = new Object();
  this.extNodeContainer.dom.innerHTML = WidgetLoadingTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath});

  Process.Code = this.aCodeNodeTypes[0];
  Process.Mode = Templates.Edit;
  Process.Container = this.extNodeContainer.dom;
  Process.onComplete = this.onNewItemComplete.bind(this);
  Process.execute();
};

CGWidgetNode.prototype.onNewItemComplete = function (IdNode) {
  var extForm = this.extNodeBox.select(CSS_FORM).first();

  this.hideLoading();

  if (extForm) {
    extForm.addClass(CLASS_NODE_FIELD);
    extForm.dom.onFieldFocus = this.atFieldFocused.bind(this);
    extForm.dom.onFieldBlur = this.atFieldBlur.bind(this);
    extForm.dom.onFieldBeforeChange = this.atFieldBeforeChange.bind(this);
    extForm.dom.onFieldChange = this.atFieldChange.bind(this);
  }

  this.lastNode = this.Item.code;
  this.select({code: IdNode, value: this.NewItem.sLabel});
  this.extAddButton.dom.style.display = (this.Item.code == "" && allowAdd) ? "block" : "none";
};

CGWidgetNode.prototype.loadNode = function () {
  if (!this.Target.getNodeTemplates) return;

  var Process = new CGProcessLoadFieldNode();
  var Templates = this.Target.getNodeTemplates();

  if (Templates.Edit == null) return;

  Process.Id = this.Item.code;
  Process.Mode = Templates.Edit;
  Process.Container = this.extNodeContainer.dom;
  Process.onComplete = this.onLoadItemComplete.bind(this);
  Process.execute();
};

CGWidgetNode.prototype.onLoadItemComplete = function (IdNode) {
  var extForm = this.extNodeBox.select(CSS_FORM).first();

  this.hideLoading();

  if (extForm) {
    extForm.addClass(CLASS_NODE_FIELD);
    extForm.dom.onFieldFocus = this.atFieldFocused.bind(this);
    extForm.dom.onFieldBlur = this.atFieldBlur.bind(this);
    extForm.dom.onFieldBeforeChange = this.atFieldBeforeChange.bind(this);
    extForm.dom.onFieldChange = this.atFieldChange.bind(this);
  }

  this.lastNode = this.Item.code;
};

CGWidgetNode.prototype.select = function (Data) {
  this.setItem(Data.code, Data.value);
  this.extValue.dom.value = this.Item.value;
  this.validate();
  this.updateData();
  this.Editor.refresh();

  if (this.onSelect) this.onSelect(Data);
};

CGWidgetNode.prototype.showNode = function (bForceRefresh) {
  if (this.Item.code == EMPTY) return;

  if (!this.isExpanded()) this.expand();

  if ((bForceRefresh) || (this.lastNode != this.Item.code)) {
    this.extNodeContainer.dom.innerHTML = WidgetLoadingTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath});
    this.loadNode();
  }
};

CGWidgetNode.prototype.hideNode = function () {
  this.extNodeContainer.dom.innerHTML = "<div class='emptynode'>" + Lang.Widget.EmptyNode + "</div>";
};

CGWidgetNode.prototype.blur = function () {
  this.extValue.removeClass(CLASS_FOCUS);
  this.extNodeBox.removeClass(CLASS_FOCUS);
  //var extForm = this.extNodeBox.select(CSS_FORM).first();
  //if (extForm) extForm.dom.blur();
};

// #############################################################################################################

CGWidgetNode.prototype.atFocused = function () {
  if (!this.Target) return;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused();

  this.Editor.show();
  this.Editor.setConfiguration({Field: this.Target});
  this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
  this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
  this.Editor.refresh();

  if (this.isLocked()) this.Editor.lock();
  else this.Editor.unLock();

  this.extValue.addClass(CLASS_FOCUS);
  this.extValue.dom.select();
  this.extNodeBox.addClass(CLASS_FOCUS);

  if (!this.bSelectingNode) this.showNode();
  this.bSelectingNode = false;

  if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());
  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = "none";
};

CGWidgetNode.prototype.atKeyUp = function (oEvent) {
  var codeKey = oEvent.getKey();

  if ((codeKey == oEvent.ESC) && (this.onEscape)) this.onEscape();
  else if ((codeKey == oEvent.ENTER) || (codeKey == oEvent.TAB)) {
    var Dialog = this.Editor.getDialog(SOURCE_STORE);
    if (Dialog) {
      var Data = Dialog.getData();
      if (Data) this.select(Data);
    }
    if ((codeKey == oEvent.ENTER) && (this.onEnter)) this.onEnter();
  }
  else if (codeKey == oEvent.DOWN) this.Editor.moveDown(this.extValue.dom);
  else if (codeKey == oEvent.UP) this.Editor.moveUp(this.extValue.dom);
  else if (this.onKeyPress) this.onKeyPress(this.extValue.dom.value, codeKey);

  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";

  Event.stop(oEvent);
  return false;
};

CGWidgetNode.prototype.atFieldFocused = function (DOMField) {
  DOMField.blur();
  this.Target.gotoField(DOMField.getPath());
};

CGWidgetNode.prototype.atFieldBlur = function () {
  //if (this.onBlur) this.onBlur();
};

CGWidgetNode.prototype.atFieldBeforeChange = function (DOMField) {
  if (this.onBeforeChange) this.onBeforeChange(DOMField);
};

CGWidgetNode.prototype.atFieldChange = function (DOMField) {
  var extNode = this.extNodeContainer.down(CSS_NODE);
  var Process = new CGProcessSaveFieldNode();
  Process.DOMNode = extNode.dom;
  Process.DOMField = DOMField;
  Process.execute();
};

CGWidgetNode.prototype.atAddButtonClick = function () {
  if (!this.isReady()) this.init();
  this.addNode();
};