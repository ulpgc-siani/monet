CGWidgetTable = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);

  if (!extWidget) return;

  this.extWidget.dom.focus = CGWidgetTable.prototype.focus.bind(this);
  this.extMoreLink = this.extWidget.down(CSS_WIDGET_ELEMENT_TABLE_MORE_LINK);
  this.extRemoveLink = this.extWidget.down(CSS_WIDGET_ELEMENT_TABLE_REMOVE_LINK);
  this.extTable = this.extWidget.down(CSS_WIDGET_ELEMENT_TABLE);
  this.extTemplate = this.extWidget.down(CSS_WIDGET_ELEMENT_TEMPLATE);
  this.rowTemplate = this.extWidget.down(CSS_WIDGET_ELEMENT_TABLE_ROW_TEMPLATE).dom.innerHTML;

  this.Attribute = null;
  this.iPos = 0;
  this.ElementWidget = null;
  this.aObservers = new Array();

  if (this.extMoreLink) Event.observe(this.extMoreLink.dom, "click", this.atMoreLinkClick.bind(this));
  if (this.extRemoveLink) Event.observe(this.extRemoveLink.dom, "click", this.atRemoveLinkClick.bind(this));

  this.registerWidget();
  this.registerElements();
};

CGWidgetTable.prototype = new CGWidget;

CGWidgetTable.prototype.init = function () {
  if (this.ElementWidget != null)
    if (!this.ElementWidget.isInit) this.ElementWidget.init();
  this.bIsReady = true;
};

CGWidgetTable.prototype.destroy = function () {
  this.ElementWidget.destroy();
  WidgetManager.unregister(this.ElementWidget.getId());
  this.destroyOptions();
  this.destroyBehaviours();
};

CGWidgetTable.prototype.getElementId = function (extElement) {
  return extElement.dom.id.replace(TABLE_VIEW_ELEMENT_PREFIX, "");
};

CGWidgetTable.prototype.registerWidget = function (extWidget) {
  var Type;
  var Id = (this.Target) ? this.getId() : Ext.id();

  if ((extWidget = this.extWidget.select(CSS_EDITOR_DIALOG_ELEMENT_ITEM_WIDGET + " " + CSS_WIDGET).first()) == null) return;

  var Constructor = Extension.getEditNodeConstructor();
  Constructor.init(extWidget.dom);

  Type = extWidget.dom.getType();
  if (Type == WIDGET_COMPOSITE) this.extWidget.addClass(CLASS_WIDGET_ELEMENT_LIST_ITEM_OPTIONS_COMPOSITE);

  extWidget.dom.Id = Id;
  this.ElementWidget = WidgetFactory.get(extWidget.dom.getType(), extWidget);
  this.ElementWidget.setWidgetRequired(this.WidgetRequired);
  if (this.Target) {
    this.ElementWidget.setId(Id);
    this.ElementWidget.setEditor(EditorsFactory.get(this.Target.getType())); // Important. This line before setfield
    this.ElementWidget.setTarget(this.Target);
  }
  this.ElementWidget.onKeyPress = this.atWidgetKeyPress.bind(this);
  this.ElementWidget.onFocused = this.atWidgetFocused.bind(this);
  this.ElementWidget.onChange = this.atWidgetChange.bind(this);
  this.ElementWidget.onSelect = this.atWidgetChange.bind(this);
  this.ElementWidget.onRefresh = this.atWidgetRefresh.bind(this);
  this.ElementWidget.onEnter = this.atEnter.bind(this);
  this.ElementWidget.onEscape = this.atEscape.bind(this);
  this.ElementWidget.showClearValue();
  this.ElementWidget.lockClearValue();

  return this.ElementWidget;
};

CGWidgetTable.prototype.registerElement = function (extElement, iPos) {
  extElement.dom.onActivate = CGWidgetTable.prototype.atActivateElement.bind(this);
  extElement.dom.onMove = CGWidgetTable.prototype.atMoveElement.bind(this);

  var extCheckOption = extElement.down(CSS_WIDGET_ELEMENT_TABLE_ITEM_CHECK);
  if (extCheckOption != null) Event.observe(extCheckOption.dom, 'click', this.atElementCheckClick.bindAsEventListener(this, extCheckOption.dom, this.ElementWidget.getDOM()));
};

CGWidgetTable.prototype.registerElements = function () {
  var aExtTableElements = null, iPos = 0;

  aExtTableElements = this.extTable.select(CSS_WIDGET_ELEMENT_TABLE_ITEM);

  aExtTableElements.each(function (extElement) {
    var extParent = extElement.up("ul");
    var Id = Ext.id();

    if (extParent != this.extTable) return;
    if ((extElement.dom.id == null) || (extElement.dom.id == "")) extElement.dom.id = Id;

    this.registerElement(extElement, iPos);

    iPos++;
  }, this);

};

CGWidgetTable.prototype.getElements = function () {
  var aResult = new Array();
  var aExtElements = this.extTable.select(CSS_WIDGET_ELEMENT_LIST_ITEM);
  aExtElements.each(function (extElement) {
    var extParent = extElement.up("ul");
    if (extParent != this.extTable) return;
    aResult.push(extElement.dom.id);
  }, this);
  return aResult;
};

CGWidgetTable.prototype.getSelectedElements = function () {
  var aElements = this.getElements();
  var aResult = new Array();
  for (var i = 0; i < aElements.length; i++) {
    var extElement = Ext.get(aElements[i]);
    var extChecked = extElement.down(CSS_WIDGET_ELEMENT_TABLE_ITEM_CHECK);
    if (extChecked.dom.checked) aResult.push(aElements[i]);
  }
  return aResult;
};

CGWidgetTable.prototype.getFirstElement = function () {
  var aElements = this.getElements();
  if (aElements.length == 0) return null;
  return aElements[0];
};

CGWidgetTable.prototype.getLastElement = function () {
  var aElements = this.getElements();
  if (aElements.length == 0) return null;
  return aElements[aElements.length - 1];
};

CGWidgetTable.prototype.getData = function () {
  var aResult = new Array();
  var AttributeList = this.Attribute.getAttributeList();
  var aAttributes = AttributeList.getAttributes();

  aResult.code = this.Target.getCode();
  aResult.order = -1;
  aResult.attributes = aAttributes;

  aResult.toXml = function () {
    var Attribute = new CGAttribute();
    var AttributeList = Attribute.getAttributeList();

    Attribute.setCode(this.code);
    Attribute.setOrder(this.order);

    for (var i = 0; i < this.attributes.length; i++) {
      var CurrentAttribute = this.attributes[i];
      AttributeList.addAttribute(CurrentAttribute);
    }

    return Attribute.serialize();
  };

  return aResult;
};

CGWidgetTable.prototype.setData = function (sData) {
  if (!this.Target) return;

  this.clear();

  var Attribute = new CGAttribute();
  Attribute.unserialize(sData);

  var extElement = null;
  var aAttributes = Attribute.getAttributes();
  for (var i = 0; i < aAttributes.length; i++) {
    if (i == 0) extElement = this.addElement(aAttributes[i]);
    else this.addElement(aAttributes[i]);
  }

  if (extElement != null) this.activateElement(extElement);
  this.validate();
  if (this.onChange) this.onChange();
};

CGWidgetTable.prototype.getValue = function () {
  return this.ElementWidget.getValue();
};

CGWidgetTable.prototype.setTarget = function (Target) {
  this.Target = Target;

  this.Attribute = new CGAttribute();
  this.Attribute.unserialize(this.Target.getData());
  this.createRequiredWidget();

  this.ElementWidget.onChange = null;
  this.ElementWidget.setWidgetRequired(this.WidgetRequired);
  this.ElementWidget.setEditor(EditorsFactory.get(this.Target.getType())); // Important. This line before setfield
  this.ElementWidget.setTarget(this.Target);
  this.ElementWidget.onChange = this.atWidgetChange.bind(this);
};

CGWidgetTable.prototype.setReadonly = function (bValue) {
  this.ElementWidget.setReadonly(bValue);
};

CGWidgetTable.prototype.validate = function () {
  if (!this.WidgetRequired) return;
  if (this.countElements() == 0) this.WidgetRequired.show();
};

CGWidgetTable.prototype.getElementIdAtPos = function (iPos) {
  var aElements = this.getElements();
  return aElements[iPos];
};

CGWidgetTable.prototype.getAttributeValue = function (Attribute) {
  var aAttributes = Attribute.getAttributeList().getAttributes();

  if (aAttributes.length > 0) {
    var sValue = "";

    for (var i = 0; i < aAttributes.length; i++) {
      var CurrentAttribute = aAttributes[i];
      var isChecked = CurrentAttribute.getIndicatorValue(CGIndicator.CHECKED);
      if ((isChecked == "") || (isChecked == "true")) sValue += CurrentAttribute.getIndicatorValue(CGIndicator.VALUE) + ",&nbsp;";
    }
    if (sValue.length > 0) sValue = sValue.substring(0, sValue.length - ",&nbsp;".length);

    return sValue;
  }
  else {
    var details = Attribute.getIndicatorValue(CGIndicator.DETAILS);
    if (details != null && details != "") return details;

    return Attribute.getIndicatorValue(CGIndicator.VALUE);
  }
};

CGWidgetTable.prototype.getAttributeValues = function (Attribute) {
  var aAttributes = Attribute.getAttributeList().getAttributes();
  var aValues = new Object();

  for (var i = 0; i < aAttributes.length; i++) {
    if (isFunction(aAttributes[i])) continue;
    var ChildAttribute = aAttributes[i];
    aValues[ChildAttribute.getCode()] = this.getAttributeValue(ChildAttribute);
  }

  return aValues;
};

CGWidgetTable.prototype.addElement = function (Attribute) {
  var iPos;
  var extElement;
  var Id = Ext.id();
  var Constructor = Extension.getEditNodeConstructor();
  var RowTemplate = new Template(this.rowTemplate);
  var aValues = this.getAttributeValues(Attribute, Id);

  this.Attribute.getAttributeList().addAttribute(Attribute);

  aValues["id"] = Id;
  aValues["position"] = this.Attribute.getAttributeList().getAttributes().size();
  new Insertion.Bottom(this.extTable.dom, RowTemplate.evaluate(aValues));
  extElement = Ext.get(Id);
  extLink = extElement.select(CSS_WIDGET_ELEMENT_LINK).first();

  if (extLink.dom.innerHTML == "" || extLink.dom.innerHTML == "&nbsp;") extLink.dom.innerHTML = Lang.Widget.Table.ElementLabel;

  iPos = Constructor.addTableViewElementBehaviours(extElement.dom);
  Constructor.init(extElement.dom);

  this.registerElement(extElement, iPos);
  this.validate();
  this.updateData();

  this.Target.activateTableViewElement(Ext.get(this.Target), extElement);
  //extElement.dom.click();
  Attribute.unserialize(this.ElementWidget.getData().toXml());

  return extElement;
};

CGWidgetTable.prototype.removeElement = function (Id) {
  var extElement = Ext.get(Id);
  var iPos = this.getElementPos(extElement);

  this.Attribute.getAttributeList().deleteAttribute(iPos);
  extElement.dom.doRemove();

  this.validate();
  this.updateData();
  this.refreshToolbar();
};

CGWidgetTable.prototype.clear = function () {
  var aElements = this.getElements();
  for (var iPos = 0; iPos < aElements.length; iPos++) {
    this.removeElement(aElements[iPos]);
  }
  this.Attribute.getAttributeList().clear();
};

CGWidgetTable.prototype.removeSelection = function () {
  var aElements = this.getSelectedElements();
  for (var i = 0; i < aElements.length; i++) this.removeElement(aElements[i]);
};

CGWidgetTable.prototype.buryElementToLast = function (IdElement) {
  var DOMElement = $(IdElement);

  if (IdElement != this.getLastElement()) {
    this.extTable.dom.removeChild(DOMElement);
    this.extTable.dom.appendChild(DOMElement);
    this.updateData();
  }
};

CGWidgetTable.prototype.countElements = function () {
  return this.getElements().length;
};

CGWidgetTable.prototype.focus = function () {
  if (!this.Target) return;
  if (!this.isReady()) this.init();

  if (this.isLocked()) this.Editor.lock();
  else this.Editor.unLock();

  this.bFocused = true;
  this.extWidget.addClass(CLASS_FOCUS);

  if (this.onFocused) this.onFocused();
};

CGWidgetTable.prototype.setObserver = function (Observer, iPos) {
  this.aObservers[iPos] = Observer;
  if (iPos == this.iPos) {
    this.ElementWidget.setObserver(Observer, iPos);
  }
};

CGWidgetTable.prototype.getElementPos = function (extElement) {
  var aElements = this.getElements();
  for (var i = 0; i < aElements.length; i++) {
    if (extElement.dom.id == aElements[i]) return i;
  }
  return -1;
};

CGWidgetTable.prototype.activateElement = function (extElement) {
  var iPos = this.getElementPos(extElement);
  var iCountElements = this.countElements();

  if (extElement == null) return;

  //if (this.iPos == iPos) return;
  if (iPos < 0) iPos = 0;
  if (iPos > iCountElements) iPos = iCountElements - 1;

  this.iPos = iPos;

  var Attribute = this.Attribute.getAttributeList().getAttributes()[this.iPos];

  var extTitle = this.extWidget.select(CSS_EDITOR_DIALOG_ELEMENT_ITEM_WIDGET + " " + CSS_WIDGET_ELEMENT_TITLE).first();
  if (extTitle != null) extTitle.dom.innerHTML = extElement.select(CSS_WIDGET_ELEMENT_LINK).first().dom.innerHTML;

  this.ElementWidget.onChange = null;
  this.ElementWidget.setData(Attribute.serialize());
  this.ElementWidget.setObserver(this.aObservers[iPos]);
  this.ElementWidget.onChange = this.atWidgetChange.bind(this);
};

CGWidgetTable.prototype.refreshToolbar = function () {
  var aSelectedElements = this.getSelectedElements();
  this.extRemoveLink.dom.style.display = (aSelectedElements.length > 0) ? "" : "none";
};

// #############################################################################################################
CGWidgetTable.prototype.atWidgetKeyPress = function (sValue, codeKey) {
  if (this.onKeyPress) this.onKeyPress(sValue, codeKey);
};

CGWidgetTable.prototype.atWidgetFocused = function () {
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused(this.ElementWidget);
};

CGWidgetTable.prototype.atEnter = function () {
  if (this.onEnter) this.onEnter();
};

CGWidgetTable.prototype.atEscape = function () {
  if (this.onEscape) this.onEscape();
};

CGWidgetTable.prototype.atWidgetRefresh = function () {
  this.Target.resizeTableView(Ext.get(this.Target));
};

CGWidgetTable.prototype.atMoreLinkClick = function (oEvent) {
  var Attribute = new CGAttribute();
  Attribute.setCode(this.Target.getCode());
  this.addElement(Attribute);
  if (oEvent) Event.stop(oEvent);
  return false;
};

CGWidgetTable.prototype.atRemoveLinkClick = function (oEvent) {
  this.removeSelection();
  if (oEvent) Event.stop(oEvent);
  return false;
};

CGWidgetTable.prototype.atElementDeleteClick = function (oEvent, DOMTarget, DOMWidget) {
  var extTarget = Ext.get(DOMTarget);
  var DOMElement = extTarget.up(CSS_WIDGET_ELEMENT_LIST_ITEM).dom;
  this.focus();
  this.removeElement(DOMElement.id);
  Event.stop(oEvent);
};

CGWidgetTable.prototype.atElementCheckClick = function (oEvent, DOMTarget, DOMWidget) {
  this.refreshToolbar();
};

CGWidgetTable.prototype.atWidgetChange = function () {
  var extElement = Ext.get(this.getElementIdAtPos(this.iPos));

  if (extElement == null) return;

  var Attribute = this.Attribute.getAttributeList().getAttributes()[this.iPos];
  if (Attribute == null) return;
  Attribute.unserialize(this.ElementWidget.getData().toXml());

  var aValues = this.getAttributeValues(Attribute);

  for (var code in aValues) {
    if (isFunction(aValues[code])) continue;
    var extValue = extElement.select("." + code).first();
    var sValue = (aValues[code] != "") ? aValues[code] : "&nbsp;";
    if (code == this.Target.getLabelFieldCode() && aValues[code] == "") sValue = Lang.Widget.Table.ElementLabel;
    if (extValue) extValue.dom.innerHTML = sValue;
  }

  var extTitle = this.extWidget.select(CSS_EDITOR_DIALOG_ELEMENT_ITEM_WIDGET + " " + CSS_WIDGET_ELEMENT_TITLE).first();
  if (extTitle != null) extTitle.dom.innerHTML = extElement.select(CSS_WIDGET_ELEMENT_LINK).first().dom.innerHTML;

  this.updateData();
};

CGWidgetTable.prototype.atActivateElement = function (extElement) {
  this.activateElement(extElement);
};

CGWidgetTable.prototype.atMoveElement = function (extElement, previousPos, newPos) {
  var aAttributes = this.Attribute.getAttributeList().getAttributes();
  var AttributeList = this.Attribute.getAttributeList();

  if (previousPos == newPos) return;

  this.Attribute.getAttributeList().clear();

  if (previousPos > newPos) {
    var offset = 0;
    for (var i = 0; i < aAttributes.length; i++) {
      var Attribute = null;

      if (i == newPos) Attribute = aAttributes[previousPos];
      else if (i > newPos && i <= previousPos) {
        Attribute = aAttributes[newPos + offset];
        offset++;
      }
      else Attribute = aAttributes[i];

      AttributeList.addAttribute(Attribute);
    }
  }
  else {
    var previousAttribute = aAttributes[previousPos];

    for (var i = 0; i < aAttributes.length; i++) {
      var Attribute = null;

      if (i == newPos) Attribute = previousAttribute;
      else if (i >= previousPos && i < newPos) Attribute = aAttributes[i + 1];
      else Attribute = aAttributes[i];

      AttributeList.addAttribute(Attribute);
    }
  }

  this.updateData();
};