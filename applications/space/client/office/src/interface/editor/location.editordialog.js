CGEditorDialogLocation = function (extLayer) {
  this.base = CGEditorDialog;
  this.base(extLayer);
  this.init();
};

CGEditorDialogLocation.prototype = new CGEditorDialog;

CGEditorDialogLocation.prototype.init = function () {

  var Options = new Object();
  Options.mapTypeId = (typeof google != "undefined") ? google.maps.MapTypeId.ROADMAP : null;

  var extMapLayer = this.extLayer.select(".map").first();
  var extInfoLayer = this.extLayer.select(".info").first();

  this.locationPicker = new LocationPicker();
  this.locationPicker.onSelect = CGEditorDialogLocation.prototype.atSelectLocation.bind(this);
  this.locationPicker.init(Options, extMapLayer, extInfoLayer);
};

CGEditorDialogLocation.prototype.setConfiguration = function (Config) {
  this.Configuration = Config;
  this.locationPicker.setLocation(Config.Location);
};

// #############################################################################################################
CGEditorDialogLocation.prototype.atSelectLocation = function (locationPicker, location) {
  if (this.onSelect) this.onSelect(location);
};