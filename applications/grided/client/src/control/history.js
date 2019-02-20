
var History = {};

History.init = function() {
  dhtmlHistory.initialize();
  dhtmlHistory.fireOnNewListener = false;
};

History.addListener = function(listener) {
  this.listener = listener;
  dhtmlHistory.addListener(listener);
};

History.getLocation = function() {
  return dhtmlHistory.getCurrentLocation();
};

History.newLocation = function(location, data) {
  dhtmlHistory.add(location, data);
  if (this.listener) this.listener.apply(null, [location, data]);
};

History.goBack = function() {
  history.go(-1);
};
