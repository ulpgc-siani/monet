WidgetManager = new Object();
WidgetManager.aWidgets = new Array();

WidgetManager.register = function (Widget) {
  WidgetManager.aWidgets[Widget.getId()] = Widget;
};

WidgetManager.unregister = function (Id) {
  if (!WidgetManager.aWidgets[Id]) return;
  delete (WidgetManager.aWidgets[Id]);
};

WidgetManager.get = function (Id) {
  return WidgetManager.aWidgets[Id];
};