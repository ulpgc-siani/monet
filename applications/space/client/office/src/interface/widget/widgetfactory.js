var WidgetFactory = new Object();

WidgetFactory.init = function () {
};

WidgetFactory.get = function (Type, extWidget) {
  var Widget = null;

  if (Type == WIDGET_REQUIRED) Widget = new CGWidgetRequired(extWidget);
  else if (Type == WIDGET_TEXT) Widget = new CGWidgetText(extWidget);
  else if (Type == WIDGET_LIST) Widget = new CGWidgetList(extWidget);
  else if (Type == WIDGET_TABLE) Widget = new CGWidgetTable(extWidget);
  else if (Type == WIDGET_SELECT) Widget = new CGWidgetSelect(extWidget);
  else if (Type == WIDGET_DATE) Widget = new CGWidgetDate(extWidget);
  else if (Type == WIDGET_BOOLEAN) Widget = new CGWidgetBoolean(extWidget);
  else if (Type == WIDGET_PICTURE) Widget = new CGWidgetPicture(extWidget);
  else if (Type == WIDGET_FILE) Widget = new CGWidgetFile(extWidget);
  else if (Type == WIDGET_COMPOSITE) Widget = new CGWidgetComposite(extWidget);
  else if (Type == WIDGET_LINK) Widget = new CGWidgetLink(extWidget);
  else if (Type == WIDGET_NUMBER) Widget = new CGWidgetNumber(extWidget);
  else if (Type == WIDGET_FORMULA) Widget = new CGWidgetFormula(extWidget);
  else if (Type == WIDGET_CHECK) Widget = new CGWidgetCheck(extWidget);
  else if (Type == WIDGET_DESCRIPTOR) Widget = new CGWidgetDescriptor(extWidget);
  else if (Type == WIDGET_NODE) Widget = new CGWidgetNode(extWidget);
  else if (Type == WIDGET_SERIAL) Widget = new CGWidgetSerial(extWidget);
  else if (Type == WIDGET_LOCATION) Widget = new CGWidgetLocation(extWidget);
  else if (Type == WIDGET_SUMMATION) Widget = new CGWidgetSummation(extWidget);

  WidgetManager.register(Widget);

  return Widget;
};