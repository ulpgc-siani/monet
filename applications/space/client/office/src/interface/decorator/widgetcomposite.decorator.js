CGDecoratorWidgetComposite = function () {
};

CGDecoratorWidgetComposite.prototype = new CGDecorator;

CGDecoratorWidgetComposite.prototype.execute = function (DOMWidget) {

  DOMWidget.getFields = function () {
    var aResult = new Array(), extWidget = Ext.get(this);
    var ControlInfo = extWidget.up(CSS_FORM).dom.getControlInfo();
    var extFieldList;

    if ((extFieldList = extWidget.select(CSS_FIELD_NODE_PREFIX + ControlInfo.IdNode)) == null) return false;

    extFieldList.each(function (extFieldItem) {
      if (!extFieldItem.dom.belongsToTemplate()) aResult.push(extFieldItem.dom);
    }, this);

    return aResult;
  };

  DOMWidget.getValue = function () {
    var extMainField, extFieldList, extWidget = Ext.get(this);
    var sResult = "";
    var Widget;

    extMainField = extWidget.select(CSS_FIELD + CSS_MAIN).first();
    if ((extMainField) && (extMainField.dom.IdWidget)) {
      Widget = WidgetManager.get(extMainField.dom.IdWidget);
      return Widget.getValue().toShort(MAX_SHORT_COMPOSITE_TITLE_LENGTH);
    }

    extFieldList = extWidget.select(CSS_FIELD);
    if (extFieldList.getCount() == 0) return Context.Config.DefaultLabel.toShort(MAX_SHORT_COMPOSITE_TITLE_LENGTH);

    extFieldList.each(function (extField) {
      var DOMField = extField.dom;
      if ((DOMField) && (DOMField.IdWidget)) {
        var Widget = WidgetManager.get(DOMField.IdWidget);
        sResult += (Widget.getValue() != "") ? Widget.getValue() + " + " : "";
      }
    }, this);

    sResult = sResult.substr(0, sResult.length - 3);

    return sResult.toShort(MAX_SHORT_COMPOSITE_TITLE_LENGTH);
  };

};