CGViewNodeLocation = function () {
  this.base = CGView;
  this.base();
  this.Type = VIEW_NODE_TYPE_NODE;
  this.aEditors = new Array();
  this.locationPicker = new LocationPicker();
};

CGViewNodeLocation.prototype = new CGView;

CGViewNodeLocation.prototype.initBehaviours = function () {
  var helper = ViewerSidebar.getHelper(Helper.MAP);
  var extEditionToolbar = Ext.get(helper.getEditionToolbarLayer());
  var extNavigationToolbar = Ext.get(helper.getNavigationToolbarLayer());
  var extSearch = Ext.get(helper.getSearchLayer());
  var editionTemplate = AppTemplate.ViewNodeLocationEditionToolbar;
  var navigationTemplate = AppTemplate.ViewNodeLocationNavigationToolbar;
  var searchTemplate = AppTemplate.ViewNodeLocationSearch;

  editionTemplate = translate(editionTemplate, Lang.ViewNodeLocation);
  navigationTemplate = translate(navigationTemplate, Lang.ViewNodeLocation);
  searchTemplate = translate(searchTemplate, Lang.ViewNodeLocation);

  extEditionToolbar.dom.innerHTML = editionTemplate;

  /*var positionBtn = DOMLayer.select('.op.position').first();
   positionBtn.on('click', this.atPositionClick.bind(this));*/

  this.pointBtn = extEditionToolbar.select('.op.point').first();
  this.pointBtn.on('click', this.atPointClick.bind(this));

  this.lineBtn = extEditionToolbar.select('.op.line').first();
  this.lineBtn.on('click', this.atLineClick.bind(this));

  this.polyBtn = extEditionToolbar.select('.op.poly').first();
  this.polyBtn.on('click', this.atPolygonClick.bind(this));

  this.cleanBtn = extEditionToolbar.select('.op.clean').first();
  this.cleanBtn.on('click', this.atCleanClick.bind(this));

  this.finishBtn = extEditionToolbar.select('.op.finish').first();
  this.finishBtn.on('click', this.atFinishClick.bind(this));

  this.cancelBtn = extEditionToolbar.select('.op.cancel').first();
  this.cancelBtn.on('click', this.atCancelClick.bind(this));

  extNavigationToolbar.dom.innerHTML = navigationTemplate;

  this.panToCenterBtn = extNavigationToolbar.select('.op.center').first();
  this.panToCenterBtn.on('click', this.atPanToCenterClick.bind(this));

  extSearch.dom.innerHTML = searchTemplate;

  this.searchValue = extSearch.select('input').first();
  this.searchValue.on('keyup', this.atSearchKeyUp.bind(this));

  this.searchBtn = extSearch.select('.op.accept').first();
  this.searchBtn.on('click', this.atSearchClick.bind(this));
};

CGViewNodeLocation.prototype.init = function (options, infoPanelLayer) {
  var helper = ViewerSidebar.getHelper(Helper.MAP);
  this.initBehaviours();
  this.locationPicker.init(options, Ext.get(this.DOMLayer), Ext.get(helper.getInfoLayer()));
  this.locationPicker.onDrawLocation = CGViewNodeLocation.prototype.atDrawLocation.bind(this);
  this.locationPicker.onCleanLocation = CGViewNodeLocation.prototype.atCleanLocation.bind(this);
  this.locationPicker.onRefresh = CGViewNodeLocation.prototype.atRefresh.bind(this);
};

CGViewNodeLocation.prototype.setTarget = function (nodeId, location) {
  this.nodeId = nodeId;
  this.locationPicker.setLocation(location);
};

CGViewNodeLocation.prototype.gotoPlace = function (place) {
  this.locationPicker.setPlace(place);
};

CGViewNodeLocation.prototype.resize = function () {
  this.locationPicker.resize();
};

// #############################################################################################################
CGViewNodeLocation.prototype.atDrawLocation = function (locationPicker, location) {
  var Process = new CGProcessUpdateNodeLocation();
  Process.Id = this.nodeId;
  Process.Location = location;
  Process.execute();
};

CGViewNodeLocation.prototype.atCleanLocation = function (locationPicker) {
  var Process = new CGProcessCleanNodeLocation();
  Process.Id = this.nodeId;
  Process.execute();
};

CGViewNodeLocation.prototype.atRefresh = function () {
  var located = this.locationPicker.isLocated();

  if (this.locationPicker.isEditing()) {
    this.pointBtn.dom.style.display = "none";
    this.lineBtn.dom.style.display = "none";
    this.polyBtn.dom.style.display = "none";
    this.finishBtn.dom.style.display = "block";
    this.cancelBtn.dom.style.display = "block";
    return;
  }

  this.finishBtn.dom.style.display = "none";
  this.cancelBtn.dom.style.display = "none";

  if (located) {
    this.pointBtn.dom.style.display = "none";
    this.lineBtn.dom.style.display = "none";
    this.polyBtn.dom.style.display = "none";
    this.cleanBtn.dom.style.display = "block";
  }
  else {
    this.pointBtn.dom.style.display = "block";
    this.lineBtn.dom.style.display = "block";
    this.polyBtn.dom.style.display = "block";
    this.cleanBtn.dom.style.display = "none";
  }

};

CGViewNodeLocation.prototype.atPositionClick = function () {
  this.locationPicker.positionate();
};

CGViewNodeLocation.prototype.atPointClick = function () {
  this.locationPicker.drawPoint();
};

CGViewNodeLocation.prototype.atLineClick = function () {
  this.locationPicker.drawLine();
};

CGViewNodeLocation.prototype.atPolygonClick = function () {
  this.locationPicker.drawPolygon();
};

CGViewNodeLocation.prototype.atPanToCenterClick = function () {
  this.locationPicker.panToCenter();
};

CGViewNodeLocation.prototype.atCleanClick = function () {
  this.locationPicker.clean();
};

CGViewNodeLocation.prototype.atFinishClick = function (LocatioPicker, Location) {
  this.locationPicker.finishDraw();
};

CGViewNodeLocation.prototype.atCancelClick = function () {
  this.locationPicker.cancelDraw();
};

CGViewNodeLocation.prototype.atSearchKeyUp = function (event) {
  var codeKey = event.getKey();
  if (codeKey == event.ENTER) this.gotoPlace(this.searchValue.dom.value);
  return false;
};

CGViewNodeLocation.prototype.atSearchClick = function () {
  this.gotoPlace(this.searchValue.dom.value);
};