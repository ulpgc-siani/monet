CGEditorDialogLocations = function (extLayer) {
  this.base = CGEditorDialog;
  this.base(extLayer);
  this.init();
  this.map = null;
};

CGEditorDialogLocations.prototype = new CGEditorDialog;

CGEditorDialogLocations.prototype.init = function () {
};

CGEditorDialogLocations.prototype.show = function() {
  CGEditorDialog.prototype.show.call(this);
  this.createMapLayer();
};

CGEditorDialogLocations.prototype.doRefresh = function() {
  this.idTimeoutRefresh = null;
  this.map.filter(this.Data);
};

CGEditorDialogLocations.prototype.refresh = function() {

  if (this.idTimeoutRefresh != null)
    window.clearTimeout(this.idTimeoutRefresh);

  this.idTimeoutRefresh = window.setTimeout(this.doRefresh.bind(this), 300);
};

CGEditorDialogLocations.prototype.createMapLayer = function () {
  var options = {
    editable : false,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    place: "",
    infoTemplate: "<div class='infowindow'><div class='label'>\{label\}</div></div>"
  };

  if (this.map == null) {
    var extMapLayer = this.extLayer.select(".map").first();

    this.map = new CGViewMapLayer();
    this.map.setCenter(Context.Config.DefaultLocation.Latitude, Context.Config.DefaultLocation.Longitude);
    this.map.setDOMLayer(extMapLayer.dom);
    this.map.init(options);
    this.map.onLocationClick = CGEditorDialogLocations.prototype.atSelectLocation.bind(this);
  }

  this.map.setTarget({ infoUrl: this.Configuration.Store.sourceCountUrl, url: this.Configuration.Store.sourceUrl });
  this.map.render();
}

// #############################################################################################################
CGEditorDialogLocations.prototype.atSelectLocation = function (location) {
  if (this.onSelect) this.onSelect(location);
};