CGDecoratorFieldComposite = function () {
};

CGDecoratorFieldComposite.prototype = new CGDecorator;

CGDecoratorFieldComposite.prototype.execute = function (DOMField) {

  DOMField.isConditional = function () {
    var extField = Ext.get(this);
    return extField.hasClass(CLASS_CONDITIONAL);
  };

  DOMField.isExtensible = function () {
    var extField = Ext.get(this);
    return extField.hasClass(CLASS_EXTENSIBLE);
  };

  DOMField.getLabelFieldCode = function () {
    var extField = Ext.get(this);
    var extFieldCode = extField.select(CSS_FIELD_DEF_LABEL_FIELD).first();
    return (extFieldCode) ? extFieldCode.dom.innerHTML : "";
  };

  DOMField.getField = function (Code) {
    var DOMFields = this.getFieldsWithCode(Code);
    var DOMField = DOMFields[0];
    if (DOMField.belongsToTemplate()) return null;
    return DOMField;
  };

  DOMField.getFieldsWithCode = function (Code) {
      var Widget = WidgetManager.get(this.IdWidget);
      var extWidget = Ext.get(Widget.getDOM());
      var ControlInfo = extWidget.up(CSS_FORM).dom.getControlInfo();
      var extFields = extWidget.select(DOT + Code + CSS_FIELD_NODE_PREFIX + ControlInfo.IdNode);
      var fields = [];
      extFields.each(function (extField) { fields.push(extField.dom); });
      return fields;
  };

  DOMField.getFields = function () {
    var aFields = new Array();
    var aComposites = new Array();
    var Widget = WidgetManager.get(this.IdWidget);

    if (Widget == null) return aFields;
    if (Widget.getDOM() == null) return aFields;

    var extWidget = Ext.get(Widget.getDOM());
    var ControlInfo = extWidget.up(CSS_FORM).dom.getControlInfo();

    if ((extFieldList = extWidget.select(CSS_FIELD_NODE_PREFIX + ControlInfo.IdNode)) == null) return false;

    extFieldList.each(function (extField) {
      var extParent = extField.up(CSS_FIELD_NODE_PREFIX + ControlInfo.IdNode);
      if (extParent.dom != this) return;
      if (extField.dom.belongsToTemplate()) return;
      if (extField.hasClass(CLASS_FIELD_COMPOSITE)) aComposites.push(extField.dom);
      else aFields.push(extField.dom);
    }, this);

    // Priorize fields vs composites to minimize onchange fields calls
    for (var iPos = aComposites.length - 1; iPos >= 0; iPos--) aFields.push(aComposites[iPos]);

    return aFields;
  };

  DOMField.initFields = function (aDOMFields) {

    if (this.Editors == null) return;

    for (var iPos = 0; iPos < aDOMFields.length; iPos++) {
      var DOMField = aDOMFields[iPos];
      if (DOMField.isInit()) continue;
      if ((FieldType = DOMField.getType()) == null) continue;

      if (FieldType == FIELD_TYPE_COMPOSITE) DOMField.Editors = this.Editors;
      DOMField.onFocused = this.atFieldFocused.bind(this);
      DOMField.onBlur = this.atFieldBlur.bind(this);
      DOMField.onAdd = this.atFieldAdd.bind(this);
      DOMField.onRegister = this.atFieldRegister.bind(this);
      DOMField.onEnter = this.atFieldEnter.bind(this);
      DOMField.onEscape = this.atFieldEscape.bind(this);
      DOMField.onLabelClick = this.atFieldLabelClick.bind(this);
      DOMField.onGotoField = this.gotoField.bind(this);
      DOMField.onLoadDefaultValue = this.atFieldLoadDefaultValue.bind(this);
      DOMField.onAddDefaultValue = this.atFieldAddDefaultValue.bind(this);
      DOMField.getField = this.atGetField.bind(this, DOMField);
      DOMField.getFieldValue = this.atGetFieldValue.bind(this, DOMField);
      DOMField.getFieldValueCode = this.atGetFieldValueCode.bind(this, DOMField);
      DOMField.init();
      // avoid callings to onchange before initialization
//      DOMField.onBeforeChange = this.atFieldBeforeChange.bind(this);
//      DOMField.onChange = this.atFieldChange.bind(this);
    }
  };

  DOMField.destroyFields = function (aDOMFields) {

    for (var iPos = 0; iPos < aDOMFields.length; iPos++) {
      var DOMField = aDOMFields[iPos];
      DOMField.onFocused = null;
      DOMField.onBlur = null;
      DOMField.onAdd = null;
      DOMField.onRegister = null;
      DOMField.onEnter = null;
      DOMField.onEscape = null;
      DOMField.onLabelClick = null;
      DOMField.onGotoField = null;
      DOMField.onLoadDefaultValue = null;
      DOMField.onAddDefaultValue = null;
      DOMField.onBeforeChange = null;
      DOMField.onChange = null;
      DOMField.onRefresh = null;
      DOMField.destroy();
    }
  };

  DOMField.atGetField = function (DOMFieldSender, code) {
    var DOMTarget = DOMFieldSender.getBrother(code);
    if (!DOMTarget || DOMTarget == null) DOMTarget = this.findField(code, DOMFieldSender);
    if (DOMTarget == null && this.getField) return this.getField(code);
    return DOMTarget;
  };

  DOMField.atGetFieldValue = function (DOMFieldSender, code) {
    var DOMTarget = DOMFieldSender.getBrother(code);
    if (!DOMTarget || DOMTarget == null) DOMTarget = this.findField(code, DOMFieldSender);
    if (DOMTarget == null && this.getFieldValue) return this.getFieldValue(code);
    return DOMTarget.getValue();
  };

  DOMField.atGetFieldValueCode = function (DOMFieldSender, code) {
    var DOMTarget = DOMFieldSender.getBrother(code);
    if (!DOMTarget || DOMTarget == null) DOMTarget = this.findField(code, DOMFieldSender);
    if (DOMTarget == null && this.getFieldValueCode) return this.getFieldValueCode(code);
    return DOMTarget.getValueCode();
  };

  DOMField.findField = function(code, DOMFieldSender) {
    var DOMFields = this.getFieldsWithCode(code);
    for (var i=0; i<DOMFields.length; i++) {
      if (DOMFields[i].sameParent(DOMFieldSender)) return DOMFields[i];
    }
    return null;
  };

};