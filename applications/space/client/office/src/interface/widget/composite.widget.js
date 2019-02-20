CGWidgetComposite = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);
  this.extOptionConditional = null;
  this.extOptionExpand = null;
  this.extOptionCollapse = null;
  this.bConditioned = false;
  this.bExtended = false;
  this.LabelFieldCode = null;

  if (!extWidget) return;

  this.extWidget.dom.focus = CGWidgetComposite.prototype.focus.bind(this);
  this.extWidget.dom.blur = CGWidgetComposite.prototype.blur.bind(this);

  this.extValue.dom.readOnly = true;
  this.extValue.addClass(CLASS_READONLY);

  this.addBehaviours();
  this.captureEvents();
};

CGWidgetComposite.prototype = new CGWidget;

CGWidgetComposite.prototype.init = function () {
  this.bIsReady = true;
};

CGWidgetComposite.prototype.clear = function () {
  var aFields = this.getFields();
  for (var iPos = 0; iPos < aFields.length; iPos++) {
    var DOMField = $(aFields[iPos]);
    DOMField.setData("");
  }
};

CGWidgetComposite.prototype.getFields = function () {
  var aFields = new Array();

  var extComposite = this.extWidget.up(CSS_FIELD);
  var extFieldList = this.extWidget.select(CSS_FIELD);
  extFieldList.each(function (extField) {
    var extParentComposite = extField.up(CSS_FIELD);
    if (extParentComposite == null) return;
    if (extParentComposite != extComposite) return;
    aFields.push(extField.dom.id);
  }, this);

  return aFields;
};

CGWidgetComposite.prototype.addBehaviours = function () {
  this.extWidget.on("click", this.atClick, this);
};

CGWidgetComposite.prototype.captureEvents = function () {
  var aFields = this.getFields();
  this.TargetIndex = new Array();
  for (var iPos = 0; iPos < aFields.length; iPos++) {
    var DOMField = $(aFields[iPos]);
    this.TargetIndex[DOMField.getCode()] = DOMField.id;
    DOMField.onBeforeChange = this.atFieldBeforeChange.bind(this, DOMField);
    DOMField.onChange = this.atFieldChange.bind(this, DOMField);
    DOMField.onRefresh = this.atFieldRefresh.bind(this, DOMField);
    DOMField.onUnLock = this.atFieldUnLock.bind(this, DOMField);
  }
};

CGWidgetComposite.prototype.createOptions = function () {

  if (this.Target.isExtensible()) {
    new Insertion.Bottom(this.extWidget.dom, WidgetCompositeExtensibleOptionsTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath}));
    var extOptions = this.extWidget.select(CSS_WIDGET_ELEMENT_OPTIONS).first();

    this.extOptionExpand = extOptions.down(CSS_EXPAND);
    this.extOptionCollapse = extOptions.down(CSS_COLLAPSE);

    this.extOptionExpand.on("click", this.atExpandClick, this);
    this.extOptionCollapse.on("click", this.atCollapseClick, this);

    this.addOptionFunctions(this.extOptionExpand);
    this.addOptionFunctions(this.extOptionCollapse);

    if (this.extWidget.hasClass(CLASS_EXTENDED)) this.expand();
    else this.collapse();
  }

  if (this.Target.isConditional()) {
    var DOMLabel = this.Target.getDOMLabel(), extLabel;
    var Id = Ext.id();
    var sLabel = DOMLabel.innerHTML;

    DOMLabel = replaceDOMElement(DOMLabel, '<table class="label"><tr><td><span>' + sLabel + '</span></td><td>' + WidgetCompositeConditionalOptionsTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath, 'id': Id}) + '</td></tr></table>');
    extLabel = Ext.get(DOMLabel);

    var extOptionConditionedList = extLabel.select(CSS_OPTION);
    extOptionConditionedList.each(function (extOptionConditioned) {
      if ((this.extWidget.hasClass(CLASS_CONDITIONED)) && (extOptionConditioned.dom.value == OPTION_YES)) extOptionConditioned.dom.checked = true;
      else if ((!this.extWidget.hasClass(CLASS_CONDITIONED)) && (extOptionConditioned.dom.value == OPTION_NO)) extOptionConditioned.dom.checked = true;
      else extOptionConditioned.dom.checked = false;
      Event.observe(extOptionConditioned.dom, "click", CGWidgetComposite.prototype.atConditionedClick.bind(this, extOptionConditioned.dom));
    }, this);

    if (this.extWidget.hasClass(CLASS_CONDITIONED)) this.setConditioned(true);
    else this.setConditioned(false);
  }

  if ((!this.Target.isExtensible()) && (!this.Target.isConditional())) this.expandAll();
};

CGWidgetComposite.prototype.destroyOptions = function () {
  if (this.Target.isExtensible()) {
    this.extOptionExpand.un("click", this.atExpandClick, this);
    this.extOptionCollapse.un("click", this.atCollapseClick, this);
  }
};

CGWidgetComposite.prototype.addOptionFunctions = function (extOption) {
  extOption.dom.focus = function () {
    var extOption = Ext.get(this);
    extOption.addClass(CLASS_FOCUS);
  };

  extOption.dom.blur = function () {
    var extOption = Ext.get(this);
    extOption.removeClass(CLASS_FOCUS);
  };
};

CGWidgetComposite.prototype.setTarget = function (Target) {
  this.Target = Target;
  this.createRequiredWidget();
  this.LabelFieldCode = this.Target.getLabelFieldCode();
  this.createOptions();
  //this.updateData();
};

CGWidgetComposite.prototype.setReadonly = function (bValue) {
  var aFields = this.getFields(), Widget;
  for (var iPos = 0; iPos < aFields.length; iPos++) {
    var DOMField = $(aFields[iPos]);
    if (!DOMField.IdWidget) continue;
    Widget = WidgetManager.get(DOMField.IdWidget);
    Widget.setReadonly(bValue);
  }
};

CGWidgetComposite.prototype.setWidgetRequired = function (WidgetRequired) {
  var aFields = this.getFields(), Widget;
  this.WidgetRequired = WidgetRequired;

  for (var iPos = 0; iPos < aFields.length; iPos++) {
    var DOMField = $(aFields[iPos]);
    if (!DOMField.IdWidget) continue;
    Widget = WidgetManager.get(DOMField.IdWidget);
    Widget.setWidgetRequired(this.WidgetRequired);
  }

};

CGWidgetComposite.prototype.validate = function () {
};

CGWidgetComposite.prototype.getData = function () {
  if (!this.Target) return null;
  if (!this.Target.getCode) return null;

  var Result = new Object();
  var sExtendedValue = (this.bExtended) ? CLASS_EXTENDED : EMPTY;
  var sConditionedValue = (this.bConditioned) ? CLASS_CONDITIONED : EMPTY;

  Result.code = this.Target.getCode();
  Result.order = -1;
  Result.value = new Array();
  Result.value.push({code: CGIndicator.EXTENDED, order: 1, value: sExtendedValue});
  Result.value.push({code: CGIndicator.CONDITIONED, order: 2, value: sConditionedValue});
  Result.value.push({code: CGIndicator.VALUE, order: 3, value: this.extValue.dom.value});
  if (this.Target.isSuper() && (this.extSuper != null)) Result.value.push({code: CGIndicator.SUPER, order: 4, value: this.extSuper.dom.value});

  Result.items = new Array();
  Result.content = EMPTY;

  var iCount = 0;
  var aFields = this.getFields();
  for (var iPos = 0; iPos < aFields.length; iPos++) {
    var DOMField = $(aFields[iPos]);
    if (!DOMField.IdWidget) continue;
    var Widget = WidgetManager.get(DOMField.IdWidget);
    var FieldData = Widget.getData();
    if (FieldData == null) continue;
    FieldData.order = iCount;
    Result.items.push(FieldData);
    iCount++;
  }

  Result.toXml = function () {
    var Attribute = new CGAttribute();
    var AttributeList = new CGAttributeList();
    var IndicatorList = new CGIndicatorList();
    var sAttributes, sData = EMPTY;

    Attribute.code = this.code;
    Attribute.iOrder = this.order;

    sAttributes = EMPTY;
    for (var iPos = 0; iPos < this.items.length; iPos++) {
      sAttributes += this.items[iPos].toXml();
    }
    sData += AttributeList.serializeWithData(sAttributes);

    for (var iPos = 0; iPos < this.value.length; iPos++) {
      IndicatorList.addIndicatorByValue(this.value[iPos].code, this.value[iPos].order, this.value[iPos].value);
    }
    sData += IndicatorList.serialize();

    return Attribute.serializeWithData(sData);
  };

  return Result;
};

CGWidgetComposite.prototype.setData = function (sData) {
  if (!this.Target) return;
  if (!this.extValue) return;

  this.clear();

  var Attribute = new CGAttribute();
  Attribute.unserialize(sData);

  var IndicatorConditioned = Attribute.getIndicator(CGIndicator.CONDITIONED);
  var bConditioned = (IndicatorConditioned && (IndicatorConditioned.getValue() == CLASS_CONDITIONED)) ? true : false;

  var IndicatorExtended = Attribute.getIndicator(CGIndicator.EXTENDED);
  this.bExtended = (IndicatorExtended && (IndicatorExtended.getValue() == CLASS_EXTENDED)) ? true : false;

  var IndicatorValue = Attribute.getIndicator(CGIndicator.VALUE);
  if (IndicatorValue) this.extValue.dom.value = IndicatorValue.getValue();

  if (this.Target.isConditional()) {
    var DOMLabel = this.Target.getDOMLabel(), extLabel;

    extLabel = Ext.get(DOMLabel);

    var extOptionConditionedList = extLabel.select(CSS_OPTION);
    extOptionConditionedList.each(function (extOptionConditioned) {
      if ((this.bConditioned) && (extOptionConditioned.dom.value == OPTION_YES)) extOptionConditioned.dom.checked = true;
      else if ((!this.bConditioned) && (extOptionConditioned.dom.value == OPTION_NO)) extOptionConditioned.dom.checked = true;
      else extOptionConditioned.dom.checked = false;
    }, this);

    this.setConditioned(bConditioned);
  }

  if ((!this.Target.isExtensible()) && (!this.Target.isConditional())) this.expandAll();

  var aAttributes = Attribute.getAttributes();
  for (var iPos = 0; iPos < aAttributes.length; iPos++) {
    var CurrentAttribute = aAttributes[iPos];
    var DOMField = $(this.TargetIndex[CurrentAttribute.code]);
    if (DOMField == null) continue;
    DOMField.onBeforeChange = null;
    DOMField.onChange = null;
    DOMField.setData(CurrentAttribute.serialize());
    DOMField.onChange = this.atFieldChange.bind(this, DOMField);
  }

  this.validate();

  if (this.onChange) this.onChange();
};

CGWidgetComposite.prototype.expandAll = function () {
  var aFields = this.getFields();
  for (var iPos = 0; iPos < aFields.length; iPos++) {
    var DOMField = $(aFields[iPos]);
    DOMField.style.display = "";
  }
};

CGWidgetComposite.prototype.expand = function () {
  var aFields = this.getFields();

  this.bExtended = true;

  if (this.extOptionExpand) this.extOptionExpand.hide();
  if (this.extOptionCollapse) this.extOptionCollapse.show();

  for (var iPos = 0; iPos < aFields.length; iPos++) {
    var DOMField = $(aFields[iPos]);
    DOMField.style.display = "";
  }
};

CGWidgetComposite.prototype.collapse = function () {
  var aFields = this.getFields();

  this.bExtended = false;

  if (this.extOptionExpand) this.extOptionExpand.show();
  if (this.extOptionCollapse) this.extOptionCollapse.hide();

  for (var iPos = 0; iPos < aFields.length; iPos++) {
    var DOMField = $(aFields[iPos]);
    if (DOMField.isExtended()) DOMField.style.display = "none";
    else DOMField.style.display = "";
  }
};

CGWidgetComposite.prototype.collapseAll = function () {
  var aFields = this.getFields();
  for (var iPos = 0; iPos < aFields.length; iPos++) {
    var DOMField = $(aFields[iPos]);
    DOMField.style.display = "none";
  }
};

CGWidgetComposite.prototype.setConditioned = function (bValue) {
  var sValue = Lang.Response.Yes;

  this.bConditioned = bValue;

  if (bValue) {
    sValue = Lang.Response.No;

    if (!this.Target.isExtensible()) this.expandAll();
    else {
      if (this.bExtended) this.expand();
      else this.collapse();
    }

    this.extWidget.dom.style.display = "";
  }
  else {
    this.collapseAll();
    this.extWidget.dom.style.display = "none";
  }

};

CGWidgetComposite.prototype.focus = function () {
  if (!this.Target) return;
  if (!this.isReady()) this.init();

  this.Editor.show();
  this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
  this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
  this.Editor.onClearValue = this.atClearValue.bind(this);
  this.Editor.refresh();

  this.bFocused = true;
  this.extWidget.addClass(CLASS_FOCUS);

  if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());

  if (this.onFocused) this.onFocused();
};

CGWidgetComposite.prototype.blur = function () {
  this.bFocused = false;
  this.extWidget.removeClass(CLASS_FOCUS);
  if (this.onBlur) this.onBlur();
};

CGWidgetComposite.prototype.lock = function () {
  if (this.extWidget.hasClass(CLASS_FOCUS)) this.extWidget.dom.blur();
  this.extWidget.addClass(CLASS_LOCKED);

  var aFields = this.getFields();
  for (var iPos = 0; iPos < aFields.length; iPos++) {
    var DOMField = $(aFields[iPos]);
    DOMField.lock();
  }
};

CGWidgetComposite.prototype.unLock = function () {
  if (this.Target.isLockedByDefinition()) return;
  this.extWidget.removeClass(CLASS_LOCKED);

  var aFields = this.getFields();
  for (var iPos = 0; iPos < aFields.length; iPos++) {
    var DOMField = $(aFields[iPos]);
    DOMField.unLock();
  }

  if (this.onUnLock) this.onUnLock();
};

// #############################################################################################################
CGWidgetComposite.prototype.atFocused = function () {
};

CGWidgetComposite.prototype.atFieldBeforeChange = function () {
};

CGWidgetComposite.prototype.atFieldChange = function (DOMField) {
  if (DOMField.getCode() == this.LabelFieldCode) this.extValue.dom.value = DOMField.getValue();

  this.validate();
  this.updateData();

  if (this.onChange != null)
    PushListener.nodeFieldCompositeChanged({ DOMNode: DOMField.getNode(), DOMField: DOMField });
};

CGWidgetComposite.prototype.atFieldRefresh = function (DOMField) {

  if (this.onRefresh) this.onRefresh(DOMField);
};

CGWidgetComposite.prototype.atFieldUnLock = function (DOMField) {
  if (this.isLocked()) DOMField.lock();
};

CGWidgetComposite.prototype.atClick = function () {
};

CGWidgetComposite.prototype.atExpandClick = function () {
  this.focus();
  this.expand();
  this.updateData();
};

CGWidgetComposite.prototype.atCollapseClick = function () {
  this.focus();
  this.collapse();
  this.updateData();
};

CGWidgetComposite.prototype.atConditionedClick = function (DOMOption) {
  this.focus();
  if (DOMOption.value == OPTION_YES) this.setConditioned(true);
  else this.setConditioned(false);
  DOMOption.checked = true;
  this.updateData();
  return true;
};