KEY_TAB = 9;
KEY_ENTER = 13;

CGWidgetSummation = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);
  this.aData = new Array();
  this.registerItems();
};

CGWidgetSummation.prototype = new CGWidget;

CGWidgetSummation.prototype.init = function () {
  this.initStores();
  this.bIsReady = true;
};

CGWidgetSummation.prototype.getAllItems = function () {
  var aResult = new Array();
  var extList = this.extWidget.down("ul");
  var aExtItems = extList.select(CSS_WIDGET_SUMMATION_ITEM);
  aExtItems.each(function (extItem) {
    var extParent = extItem.up(CSS_WIDGET);
    if (extParent.dom != this.extWidget.dom) return;
    if ((extItem.dom.id == null) || (extItem.dom.id == "")) extItem.dom.id = Ext.id();
    aResult.push(extItem.dom.id);
  }, this);
  return aResult;
};

CGWidgetSummation.prototype.getItems = function (extParent) {
  var aResult = new Array();

  if (extParent == null) return aResult;

  var aExtItems = extParent.select(CSS_WIDGET_SUMMATION_ITEM);
  aExtItems.each(function (extItem) {
    var extChildParent = extItem.up("ul");
    if (extChildParent.dom != extParent.dom) return;
    if ((extItem.dom.id == null) || (extItem.dom.id == "")) extItem.dom.id = Ext.id();
    aResult.push(extItem.dom.id);
  }, this);
  return aResult;
};

CGWidgetSummation.prototype.getItemType = function (extItem) {
  if (extItem.hasClass(SUMMATION_ITEM_SIMPLE.toLowerCase())) return SUMMATION_ITEM_SIMPLE;
  return "";
};

CGWidgetSummation.prototype.registerItems = function () {
  var aItems = this.getAllItems();

  this.aWidgets = new Array();

  for (var iPos = 0; iPos < aItems.length; iPos++) {
    var extItem = Ext.get(aItems[iPos]);
    this.registerItem(extItem);
  }
};

CGWidgetSummation.prototype.registerItem = function (extItem) {

  this.aData[extItem.dom.id] = { Label: "", Value: 0, IsNegative: extItem.hasClass(CLASS_NEGATIVE) };
  this.aData[extItem.dom.id].Type = extItem.hasClass(SUMMATION_ITEM_SIMPLE.toLowerCase()) ? SUMMATION_ITEM_SIMPLE : "-1";

  if (extItem.hasClass(SUMMATION_ITEM_SIMPLE.toLowerCase()))
    this.registerItemSimpleWidget(extItem);

  var Data = this.aData[extItem.dom.id];
  if (extItem.hasClass(CLASS_MULTIPLE) && Data.Label != "" && Data.Value != "") this.addItem(extItem.dom.id);
};

CGWidgetSummation.prototype.registerWidgetBehaviours = function (IdItem, Widget) {
  Widget.onFocused = this.atWidgetFocused.bind(this, IdItem, Widget);
  Widget.onBlur = this.atWidgetBlur.bind(this, IdItem, Widget);
  Widget.onChange = this.atWidgetChange.bind(this, IdItem, Widget);
  Widget.onSelect = this.atWidgetSelect.bind(this, IdItem, Widget);
  Widget.onKeyPress = this.atWidgetKeyPress.bind(this, IdItem, Widget);
  Widget.onEnter = this.atWidgetEnter.bind(this, IdItem, Widget);
  Widget.onEscape = this.atWidgetEscape.bind(this, IdItem, Widget);
  Widget.onClearValue = this.atWidgetClearValue.bind(this, IdItem, Widget);
};

CGWidgetSummation.prototype.registerItemSimpleWidget = function (extItem) {
  var Id = Ext.id();
  var IdItem = extItem.dom.id;
  var extSelect = extItem.select(CSS_WIDGET_SELECT).first();
  var extNumber = extItem.select(CSS_WIDGET_NUMBER).first();
  var extParent = null;

  if (extNumber != null) {
    extParent = extNumber.up(CSS_WIDGET_SUMMATION_ITEM);
    if (extParent.dom != extItem.dom) extNumber = null;
  }

  if (extNumber == null) {
    var extValue = extItem.select(CSS_WIDGET_ELEMENT_VALUE).first();
    extParent = extValue.up(CSS_WIDGET_SUMMATION_ITEM);
    if (extParent.dom == extItem.dom) {
      this.aData[IdItem].Value = extValue.dom.value;
    }
  }
  else {
    this.aWidgets[Id] = WidgetFactory.get(WIDGET_NUMBER, extNumber);
    this.aWidgets[Id].setEditor(EditorsFactory.get(FIELD_TYPE_NUMBER));
    this.aWidgets[Id].setWidgetRequired(this.WidgetRequired);
    if (this.Target) this.aWidgets[Id].setTarget(this.Target);
    this.registerWidgetBehaviours(IdItem, this.aWidgets[Id]);
    this.aData[IdItem].Value = this.aWidgets[Id].getValue();
    extNumber.dom.IdWidget = Id;
  }

  if (extSelect != null) {
    extParent = extSelect.up(CSS_WIDGET_SUMMATION_ITEM);
    if (extParent.dom != extItem.dom) extSelect = null;
  }

  if (extSelect == null) {
    var extLabel = extItem.select(CSS_WIDGET_ELEMENT_LABEL + " span").first();
    extParent = extLabel.up(CSS_WIDGET_SUMMATION_ITEM);
    if (extParent.dom == extItem.dom) {
      this.aData[IdItem].Label = extLabel.dom.innerHTML;
    }
    return;
  }

  Id = Ext.id();
  this.aWidgets[Id] = WidgetFactory.get(WIDGET_SELECT, extSelect);
  this.aWidgets[Id].setEditor(EditorsFactory.get(FIELD_TYPE_SELECT));
  this.aWidgets[Id].setWidgetRequired(this.WidgetRequired);
  if (this.Target) this.aWidgets[Id].setTarget(this.Target);
  this.registerWidgetBehaviours(IdItem, this.aWidgets[Id]);
  this.aData[IdItem].Label = this.aWidgets[Id].getValue();
  extSelect.dom.IdWidget = Id;
};

CGWidgetSummation.prototype.unregisterItem = function (extItem) {

  if (extItem.hasClass(SUMMATION_ITEM_SIMPLE.toLowerCase()))
    this.unregisterItemSimpleWidget(extItem);

  delete(this.aData[extItem.dom.id]);
};

CGWidgetSummation.prototype.unregisterWidgetBehaviours = function (IdItem, Widget) {
  Widget.onFocused = null;
  Widget.onBlur = null;
  Widget.onChange = null;
  Widget.onSelect = null;
  Widget.onKeyPress = null;
  Widget.onEnter = null;
  Widget.onEscape = null;
  Widget.onClearValue = null;
};

CGWidgetSummation.prototype.unregisterItemSimpleWidget = function (extItem) {
  var extSelect = extItem.select(CSS_WIDGET_SELECT).first();
  var extNumber = extItem.select(CSS_WIDGET_NUMBER).first();
  var extParent = null;

  if (extNumber != null) {
    extParent = extNumber.up(CSS_WIDGET_SUMMATION_ITEM);
    if (extParent.dom != extItem.dom) extNumber = null;
  }

  if (extNumber != null) {
    var IdWidget = extNumber.dom.IdWidget;
    var Widget = this.aWidgets[IdWidget];
    Widget.setValue("0");
    this.setItemData("0", extItem.dom.id, Widget);
    this.unregisterWidgetBehaviours(extItem.dom.id, Widget);
    Widget.destroy();
    WidgetManager.unregister(Widget.getId());
    delete(this.aWidgets[IdWidget]);
  }

  if (extSelect != null) {
    extParent = extSelect.up(CSS_WIDGET_SUMMATION_ITEM);
    if (extParent.dom != extItem.dom) extSelect = null;
  }

  if (extSelect != null) {
    var IdWidget = extSelect.dom.IdWidget;
    var Widget = this.aWidgets[IdWidget];
    this.unregisterWidgetBehaviours(extItem.dom.id, Widget);
    Widget.destroy();
    WidgetManager.unregister(Widget.getId());
    delete(this.aWidgets[IdWidget]);
  }
};

CGWidgetSummation.prototype.refreshItem = function (extItem, iValue) {
  if (extItem.hasClass(SUMMATION_ITEM_SIMPLE.toLowerCase())) this.refreshItemSimpleWidget(extItem, iValue);
};

CGWidgetSummation.prototype.refreshItemSimpleWidget = function (extItem, iValue) {
  var extNumber = extItem.select(CSS_WIDGET_NUMBER).first();
  var extParent = null;

  if (extNumber != null) {
    extParent = extNumber.up(CSS_WIDGET_SUMMATION_ITEM);
    if (extParent.dom != extItem.dom) extNumber = null;
  }

  if (extNumber == null) {
    var extValue = extItem.select(CSS_WIDGET_ELEMENT_VALUE).first();
    extParent = extValue.up(CSS_WIDGET_SUMMATION_ITEM);
    if (extParent.dom != extItem.dom) return;
    extValue.dom.value = iValue;
  }
  else {
    extNumber.dom.value = iValue;
  }
};

CGWidgetSummation.prototype.itemToObject = function (extItem) {
  var Data = this.aData[extItem.dom.id];
  var Item = new CGSummationItem();

  Item.Label = Data.Label;
  Item.Value = Data.Value;
  Item.Type = this.getItemType(extItem);
  Item.IsMultiple = extItem.hasClass(CLASS_MULTIPLE);
  Item.IsNegative = extItem.hasClass(CLASS_NEGATIVE);

  var aItems = this.getItems(extItem.down("ul"));
  for (var i = 0; i < aItems.length; i++)
    Item.Children.push(this.itemToObject(Ext.get(aItems[i])));

  return Item;
};

CGWidgetSummation.prototype.itemsToObject = function () {
  var extList = this.extWidget.down("ul");
  var aItems = this.getItems(extList);
  var aResult = new Array();

  for (var i = 0; i < aItems.length; i++)
    aResult.push(this.itemToObject(Ext.get(aItems[i])));

  return aResult;
};

CGWidgetSummation.prototype.getData = function () {
  if (!this.Target) return null;
  if (!this.Target.getCode) return null;
  if (!this.extValue) return null;

  var aItems = this.itemsToObject();
  var ItemList = new CGSummationItemList();
  ItemList.Items = aItems;

  var Result = new Object();
  Result.code = this.Target.getCode();
  Result.order = -1;
  Result.value = new Array();
  Result.value.push({code: CGIndicator.VALUE, order: 1, value: this.extValue.dom.value});
  Result.value.push({code: CGIndicator.INTERNAL, order: 2, value: this.Target.getNumberFromFormattedValue(this.extValue.dom.value)});
  if (this.extMetrics && this.extMetrics.dom.selectedIndex != -1) Result.value.push({code: CGIndicator.METRIC, order: 3, value: this.extMetrics.dom.options[this.extMetrics.dom.selectedIndex].value});
  Result.value.push({code: CGIndicator.DETAILS, order: 1, value: ItemList.serialize()});
  if (this.Target.isSuper() && (this.extSuper != null)) Result.value.push({code: CGIndicator.SUPER, order: 2, value: this.extSuper.dom.value});

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

CGWidgetSummation.prototype.setTarget = function (Target) {
  this.Target = Target;

  this.createRequiredWidget();

  for (var Id in this.aWidgets) {
    if (isFunction(this.aWidgets[Id])) continue;
    var onChangeFunction = this.aWidgets[Id].onChange;
    this.aWidgets[Id].onChange = null;
    this.aWidgets[Id].setWidgetRequired(this.WidgetRequired);
    this.aWidgets[Id].setTarget(this.Target);
    this.aWidgets[Id].onChange = onChangeFunction;
  }

  //this.updateData();
};

CGWidgetSummation.prototype.updateTotal = function () {
  var aChildren = this.getItems(this.extWidget.down("ul"));
  var iValue = 0;

  for (var i = 0; i < aChildren.length; i++) {
    var extItem = Ext.get(aChildren[i]);
    if (extItem.hasClass(CLASS_TOTAL)) {
      extTotal = extItem;
      continue;
    }
    var Data = this.aData[aChildren[i]];
    iValue += parseInt(Data.Value);
  }

  this.extValue.dom.value = iValue;
};

CGWidgetSummation.prototype.updateItem = function (extItem) {
  var aChildren = this.getItems(extItem.down("ul"));
  var iValue = 0;

  for (var i = 0; i < aChildren.length; i++) {
    var Data = this.aData[aChildren[i]];
    iValue += parseInt(Data.Value);
  }

  this.aData[extItem.dom.id].Value = iValue;
  this.refreshItem(extItem, iValue);
};

CGWidgetSummation.prototype.updateItems = function (IdAffectedItem) {
  var extAffectedItem = Ext.get(IdAffectedItem);
  var extParentItem = extAffectedItem.up(CSS_WIDGET_SUMMATION_ITEM);
  while (extParentItem != null) {
    this.updateItem(extParentItem);
    extParentItem = extParentItem.up(CSS_WIDGET_SUMMATION_ITEM);
  }
  this.updateTotal();
};

CGWidgetSummation.prototype.setItemData = function (sData, IdItem, Widget) {
  if (Widget instanceof CGWidgetSelect) this.aData[IdItem].Label = Widget.getValue();
  else {
    this.aData[IdItem].Value = parseInt(Widget.getValue());
    this.updateItems(IdItem);
  }
  this.updateData();
};

CGWidgetSummation.prototype.addItem = function (IdItem) {
  var extItem = Ext.get(IdItem);
  var Data = this.aData[IdItem];
  var extTemplate = this.extWidget.select(".template." + Data.Type.toLowerCase()).first();

  new Insertion.After(extItem.dom, cleanContentIds(extTemplate.dom.innerHTML));
  var extNewSibling = Ext.get(extItem.getNextSibling());

  if (extItem.hasClass(CLASS_NEGATIVE)) extNewSibling.addClass(CLASS_NEGATIVE);
  if (extItem.hasClass(CLASS_MULTIPLE)) extNewSibling.addClass(CLASS_MULTIPLE);

  extNewSibling.dom.id = Ext.id();

  var Constructor = Extension.getEditNodeConstructor();
  Constructor.init(extNewSibling.dom);

  this.registerItem(extNewSibling);
};

CGWidgetSummation.prototype.deleteItem = function (IdItem) {
  var extItem = Ext.get(IdItem);
  this.unregisterItem(extItem);
  extItem.remove();
};

CGWidgetSummation.prototype.removeHighlights = function () {
  var aItems = this.getAllItems();

  this.extValue.removeClass(CLASS_HIGHLIGHTED);

  for (var i = 0; i < aItems.length; i++) {
    var extItem = Ext.get(aItems[i]);
    extItem.removeClass(CLASS_HIGHLIGHTED);
  }
};

CGWidgetSummation.prototype.highlightParents = function () {
  this.removeHighlights();

  this.extValue.addClass(CLASS_HIGHLIGHTED);
  var extCurrentItem = Ext.get(this.CurrentItem);
  var extParentItem = extCurrentItem.up(CSS_WIDGET_SUMMATION_ITEM);
  while (extParentItem != null) {
    extParentItem.addClass(CLASS_HIGHLIGHTED);
    extParentItem = extParentItem.up(CSS_WIDGET_SUMMATION_ITEM);
  }
};

CGWidgetSummation.prototype.setWidgetRequired = function (WidgetRequired) {
  this.WidgetRequired = WidgetRequired;

  for (var Id in this.aWidgets) {
    if (isFunction(this.aWidgets[Id])) continue;
    this.aWidgets[Id].setWidgetRequired(this.WidgetRequired);
  }
};

// #############################################################################################################
CGWidgetSummation.prototype.atFocused = function (oEvent) {
  if (!this.Target) return;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused();

  this.Editor.show();
  this.Editor.setConfiguration({Field: this.Target});
  this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
  this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
  this.Editor.onClearValue = this.atClearValue.bind(this);
  this.Editor.refresh();

  if (this.isLocked()) this.Editor.lock();
  else this.Editor.unLock();

  this.extValue.addClass(CLASS_FOCUS);
  this.extValue.dom.select();

  if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());
  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = "none";
};

CGWidgetSummation.prototype.atWidgetFocused = function (IdItem, Widget) {
  this.CurrentItem = IdItem;
  this.CurrentWidget = Widget;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused(Widget);
  this.highlightParents();
};

CGWidgetSummation.prototype.atWidgetBlur = function (IdItem, Widget) {
  this.removeHighlights();
};

CGWidgetSummation.prototype.atWidgetKeyPress = function (IdItem, Widget, sValue, codeKey) {
  if (Widget instanceof CGWidgetNumber && (codeKey == KEY_TAB || codeKey == KEY_ENTER)) {
    var Data = this.aData[IdItem];
    var extItem = Ext.get(IdItem);
    if (extItem.hasClass(CLASS_MULTIPLE)) {
      var extSibling = Ext.get(IdItem).getNextSibling();
      if (extSibling == null && Data.Label != "" && Data.Value != "") this.addItem(IdItem);
    }
  }
  if (this.onKeyPress) this.onKeyPress(sValue, codeKey);
};

CGWidgetSummation.prototype.atWidgetChange = function (IdItem, Widget) {
  var sValue = Widget.getValue();

  if (sValue == "") {
    this.deleteItem(IdItem);
    return;
  }

  if (this.aData[IdItem].IsNegative && sValue.indexOf("-") == -1) {
    sValue = "-" + sValue;
    Widget.setValue(sValue);
  }
  this.setItemData(sValue, IdItem, Widget);
};

CGWidgetSummation.prototype.atWidgetSelect = function (IdItem, Widget, Data) {
  var extItem = Ext.get(IdItem);

  this.setItemData(Data.value, IdItem, Widget);

  if (extItem.hasClass(CLASS_MULTIPLE)) {
    var Data = this.aData[IdItem];
    var extSibling = Ext.get(IdItem).getNextSibling();
    if (extSibling == null && Data.Label != "" && Data.Value != "") this.addItem(IdItem);
  }
};

CGWidgetSummation.prototype.atWidgetEnter = function () {
  if (this.onEnter) this.onEnter();
};

CGWidgetSummation.prototype.atWidgetEscape = function () {
  if (this.onEscape) this.onEscape();
};

CGWidgetSummation.prototype.atWidgetClearValue = function (IdItem, Widget) {
  this.deleteItem(IdItem);
};