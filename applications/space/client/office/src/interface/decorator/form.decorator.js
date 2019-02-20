CGDecoratorForm = function () {
};

CGDecoratorForm.prototype = new CGDecorator;

CGDecoratorForm.prototype.execute = function (DOMForm) {

  this.addCommonMethods(DOMForm);

  DOMForm.init = function (Editors) {
    var extForm = Ext.get(this);
    var aFields = this.getFields();

    this.initToolbar(".header .content .toolbar");
    this.initTabs(CSS_NODE);

    if (!this.isEditable()) {
      this.addTableViewBehaviours(aFields);
      this.addSummationBehaviours(aFields);
      return;
    }

    this.Editors = Editors;
    this.aMemento = new Array();
    this.IndexMemento = -1;

    this.initFields(aFields);

    extFormElement = extForm.select("form").first();
    if (extFormElement == null) extFormElement = extForm.select(".form").first();
    if (extFormElement == null) return false;
    extFormElement.dom.onsubmit = function () {
      return false;
    };
  };

  DOMForm.destroy = function () {
    if (!this.isEditable()) return;
    this.destroyFields(this.getFields());
    this.Editors = null;
    this.aMemento = new Array();
  };

  DOMForm.getContent = function () {
    var Node = new CGNode(), sNode = EMPTY;
    var sContent = EMPTY;
    var aResult = new Array();
    var ControlInfo = this.getControlInfo();
    var extNode = Ext.get(this);
    var aExtFields;
    var AttributeList = new CGAttributeList();

    aExtFields = extNode.select(".nodeFields " + CSS_FIELD_NODE_PREFIX + ControlInfo.IdNode + " input.root");
    aExtFields.each(function (extField) {
      if (!extField.dom.name) return;
      if (extField.up(CSS_NODE).dom != this) return;
      aResult.push({code: extField.dom.name, id: extField.dom.name, name: extField.dom.name, value: extField.dom.value});
    }, this);

    if (aResult.length == 0) return "";

    Node.setId(ControlInfo.IdNode);
    Node.setCode(ControlInfo.Code);
    for (var iPos = 0; iPos < aResult.length; iPos++) {
      sContent += aResult[iPos].value;
    }
    sContent = AttributeList.serializeWithData(sContent);
    sContent = Node.serializeWithData(sContent);

    return sContent;
  };

  DOMForm.update = function (sData) {
    if (!this.isEditable()) return false;
    if (sData == null) return true;

    var Attribute = new CGAttribute();
    var extNode = Ext.get(this);
    Attribute.unserialize(sData);
    var extField = extNode.select(".nodeFields " + CSS_FIELD_NODE_PREFIX + this.getId() + "." + Attribute.getCode()).first();
    if (extField) extField.dom.update(sData);

    return true;
  };

  DOMForm.executeOnloadCommands = function () {
    var extElement = Ext.get(this);
    var aExtOnloadCommands = extElement.select(CSS_ONLOAD_COMMAND);
    aExtOnloadCommands.each(function (extOnloadCommand) {
      var extElement = extOnloadCommand.up(CSS_NODE);
      if (extElement.dom != this) return;
      CommandListener.throwCommand(extOnloadCommand.dom.innerHTML);
    }, this);
  };

  DOMForm.setObservers = function (Observers) {
    var extElement = Ext.get(this);
    for (var i = 0; i < Observers.length; i++) {
      this.addObserver(Observers[i], i);
    }
  };

  DOMForm.getField = function (fieldPath) {
    if (fieldPath == "") return null;

    var DOMField = $(fieldPath);
    if (DOMField == null) {
      var extNode = Ext.get(this);
      var extFieldList = extNode.select(CSS_FIELD + "." + fieldPath);
      if (extFieldList.getCount() == 0) return null;
      DOMField = extFieldList.first().dom;
    }
    if (!DOMField.hasClassName(CLASS_FIELD)) DOMField = DOMField.up(CSS_FIELD);
    return DOMField;
  };

  DOMForm.addObserver = function (Observer, iPos) {
    var DOMField = this.getField(Observer.field);
    if (DOMField == null) return;
    DOMField.setObserver(Observer, iPos);
  };

  DOMForm.removeObserver = function (fieldPath) {
    var DOMField = this.getField(fieldPath);
    if (DOMField == null) return;
    DOMField.setObserver(null, -1);
  };

  DOMForm.focusField = function (fieldPath) {
    var DOMField = this.getField(fieldPath);
    if (DOMField == null) return;
    DOMField.focus();
  };

  DOMForm.blurField = function (fieldPath) {
    var DOMField = this.getField(fieldPath);
    if (DOMField == null) return;
    DOMField.blur();
  };

};