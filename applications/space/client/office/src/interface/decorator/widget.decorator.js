CGDecoratorWidget = function () {
};

CGDecoratorWidget.prototype = new CGDecorator;

CGDecoratorWidget.prototype.execute = function (DOMWidget) {

  DOMWidget.getType = function () {
    var extWidget = Ext.get(this);
    if (extWidget.hasClass(CLASS_WIDGET_SELECT)) return WIDGET_SELECT;
    else if (extWidget.hasClass(CLASS_WIDGET_BOOLEAN)) return WIDGET_BOOLEAN;
    else if (extWidget.hasClass(CLASS_WIDGET_DATE)) return WIDGET_DATE;
    else if (extWidget.hasClass(CLASS_WIDGET_TEXT)) return WIDGET_TEXT;
    else if (extWidget.hasClass(CLASS_WIDGET_LIST)) return WIDGET_LIST;
    else if (extWidget.hasClass(CLASS_WIDGET_TABLE)) return WIDGET_TABLE;
    else if (extWidget.hasClass(CLASS_WIDGET_PICTURE)) return WIDGET_PICTURE;
    else if (extWidget.hasClass(CLASS_WIDGET_FILE)) return WIDGET_FILE;
    else if (extWidget.hasClass(CLASS_WIDGET_NUMBER)) return WIDGET_NUMBER;
    else if (extWidget.hasClass(CLASS_WIDGET_LINK)) return WIDGET_LINK;
    else if (extWidget.hasClass(CLASS_WIDGET_REQUIRED)) return WIDGET_REQUIRED;
    else if (extWidget.hasClass(CLASS_WIDGET_COMPOSITE)) return WIDGET_COMPOSITE;
    else if (extWidget.hasClass(CLASS_WIDGET_FORMULA)) return WIDGET_FORMULA;
    else if (extWidget.hasClass(CLASS_WIDGET_CHECK)) return WIDGET_CHECK;
    else if (extWidget.hasClass(CLASS_WIDGET_DESCRIPTOR)) return WIDGET_DESCRIPTOR;
    else if (extWidget.hasClass(CLASS_WIDGET_NODE)) return WIDGET_NODE;
    else if (extWidget.hasClass(CLASS_WIDGET_SERIAL)) return WIDGET_SERIAL;
    else if (extWidget.hasClass(CLASS_WIDGET_LOCATION)) return WIDGET_LOCATION;
    else if (extWidget.hasClass(CLASS_WIDGET_SUMMATION)) return WIDGET_SUMMATION;
    return null;
  };

  var Type = DOMWidget.getType();
  var Decorator = null;

  if (Type == WIDGET_COMPOSITE) Decorator = new CGDecoratorWidgetComposite();

  if (Decorator != null) Decorator.execute(DOMWidget);
};