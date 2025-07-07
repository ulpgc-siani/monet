var LineStringOptions = {
  strokeColor: '#0000FF',
  strokeOpacity: 0.5,
  strokeWeight: 1
};

var PolygonOptions = {
  clickable: true,
  fillColor: '#0000FF',
  fillOpacity: 0.2,
  strokeColor: '#0000FF',
  strokeOpacity: 0.5,
  strokeWeight: 1
};

CGViewMapLayer = function () {
  this.base = CGView;
  this.base();
  this.Type = VIEW_NODE_TYPE_NODE;
  this.aEditors = new Array();

  this.currentNodeId = null;
  this.executeIdle = true;
  this.condition = null;
  this.lastHeatMap = null;
  this.centerLat = 28;
  this.centerLng = -15;
};

CGViewMapLayer.prototype = new CGView;
CGViewMapLayer.MarkersPerPage = 1500;

CGViewMapLayer.prototype.initBehaviours = function (options) {
  var helper = ViewerSidebar.getHelper(Helper.MAP);
  var extSearch = Ext.get(helper.getSearchLayer());
  var searchTemplate = AppTemplate.ViewMapLayerSearch;
  var extNavigationToolbar = Ext.get(helper.getNavigationToolbarLayer());
  searchTemplate = translate(searchTemplate, Lang.ViewMapLayer);

  extSearch.dom.innerHTML = searchTemplate;
  extNavigationToolbar.dom.innerHTML = "";

  this.searchValue = extSearch.select('input').first();
  this.searchValue.on('keyup', this.atSearchKeyUp.bind(this));

  this.searchBtn = extSearch.select('.op.accept').first();
  this.searchBtn.on('click', this.atSearchClick.bind(this));

  Ext.EventManager.onWindowResize(this.atResize, this);
};

CGViewMapLayer.prototype.init = function (options) {
  this.options = options;
  this.count = 1;
  this.page = 0;
  this.infoWindowTemplate = new Ext.Template(this.options.infoTemplate ? this.options.infoTemplate.unescapeHTML() : AppTemplate.ViewPlacemarkInfoWindow);
  this.infoWindowTemplate.compile();

  this.initBehaviours();
};

CGViewMapLayer.prototype.filter = function (condition) {
  this.condition = condition;
  this.updateLayer();
};

CGViewMapLayer.prototype.setTarget = function (source) {
  this.sourceCountUrl = source.countUrl;
  this.sourceUrl = source.url;
};

CGViewMapLayer.prototype.setCenter = function (latitude, longitude) {
  this.centerLat = latitude;
  this.centerLng = longitude;
};

CGViewMapLayer.prototype.render = function () {
  window.monetMapOptions = this.atLoadMapOptions.bind(this);
  window.monetMapInit = this.atInitMap.bind(this);
  window.monetMapOnBoundsChanged = this.atBoundsChanged.bind(this);
  window.monetMapOnIdle = this.atIdle.bind(this);

  if (CGViewMapLayer.oms != null && CGViewMapLayer.projHelper != null) {
    CGViewMapLayer.oms.projHelper = CGViewMapLayer.projHelper;
    CGViewMapLayer.oms.projHelper.map = CGViewMapLayer.map;
  }

  this.options.zoom = 12;

  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(this.atGPSPositioned.bind(this));
  } else {
    this.options.zoom = 10;
  }

  this.options.center = { lat: parseFloat(this.centerLat), lng: parseFloat(this.centerLng) };
  this.DOMLayer.innerHTML = translate(AppTemplate.ViewMapLayer, Lang.ViewMapLayer);

  var extLayer = Ext.get(this.DOMLayer);
  extLayer.select(".next").first().on("click", this.atNextPage.bind(this));
  extLayer.select(".previous").first().on("click", this.atPreviousPage.bind(this));

  this.extLoading = extLayer.select(".loading-map").first();
  this.extPage = extLayer.select(".page").first();
  this.extCount = extLayer.select(".count").first();

  this.insertMap(extLayer.dom.querySelector(".map-container"));
};

CGViewMapLayer.prototype.insertMap = function(container) {
  this.clearLayers();

  const iframe = document.createElement("iframe");
  iframe.className = "map";
  iframe.style.border = "0";
  iframe.style.padding = "0";
  iframe.width = "100%";
  iframe.height = "100%";
  iframe.src = `${Context.Config.Url}/map.html?key=${Context.Config.Map.ApiKey}&m=${Math.random()}`;

  const mapFrame = container.querySelector("iframe.map");
  if (mapFrame != null) container.removeChild(mapFrame);
  container.appendChild(iframe);
}

CGViewMapLayer.prototype.refresh = function (condition) {
  this.updateLayer();
};

CGViewMapLayer.prototype.gotoPlace = function (place) {
  if (place == "") return;
  this.geocoder.geocode({address: place}, CGViewMapLayer.prototype.atGotoPlaceInfoArrived.bind(this));
};

CGViewMapLayer.prototype.isSameBounds = function (bounds) {

  if (bounds == null) return false;

  var currentBounds = CGViewMapLayer.map.getBounds();
  var currentNorthEast = currentBounds.getNorthEast();
  var currentSouthWest = currentBounds.getSouthWest();
  var northEast = bounds.getNorthEast();
  var southWest = bounds.getSouthWest();

  return currentNorthEast.lat() == northEast.lat() &&
      currentNorthEast.lng() == northEast.lng() &&
      currentSouthWest.lat() == southWest.lat() &&
      currentSouthWest.lng() == southWest.lng();
};

CGViewMapLayer.prototype.updatePagesToolbar = function () {
    this.countPages = Math.round(this.count / CGViewMapLayer.MarkersPerPage) + (this.count % CGViewMapLayer.MarkersPerPage > 0 ? 1 : 0);
    this.page = 0;
    this.refreshPagesToolbar();
};

CGViewMapLayer.prototype.refreshPagesToolbar = function() {
    this.extCount.dom.innerHTML = this.countPages > 0 ? this.countPages : 1;
    this.extPage.dom.innerHTML = this.page+1;
};

CGViewMapLayer.prototype.atNextPage = function () {
  this.page++;
  if (this.page >= this.countPages) this.page = this.countPages-1;
  this.refreshPagesToolbar();
  this.refreshLayer();
};

CGViewMapLayer.prototype.atPreviousPage = function () {
    this.page--;
    if (this.page <= 0) this.page = 0;
    this.refreshPagesToolbar();
    this.refreshLayer();
};

CGViewMapLayer.prototype.getBoundsQuery = function() {
    var bounds = CGViewMapLayer.map.getBounds();
    var northEast = bounds.getNorthEast();
    var southWest = bounds.getSouthWest();
    return "&nex=" + northEast.lat() + "&ney=" + northEast.lng() + "&swx=" + southWest.lat() + "&swy=" + southWest.lng();
};

CGViewMapLayer.prototype.getFilterQuery = function() {
    return this.condition != null ? "&query=" + this.condition : "";
};

CGViewMapLayer.prototype.getSourceCountUrl = function() {
    return this.sourceCountUrl + this.getBoundsQuery() + this.getFilterQuery();
};

CGViewMapLayer.prototype.getSourceUrl = function() {
    var boundsQuery = this.getBoundsQuery();
    var filterQuery = this.getFilterQuery();
    var start = this.page*CGViewMapLayer.MarkersPerPage;
    return this.sourceUrl + boundsQuery + filterQuery + "&start=" + start + "&limit=" + CGViewMapLayer.MarkersPerPage;
};

CGViewMapLayer.prototype.updateLayer = function () {
  var bounds = CGViewMapLayer.map.getBounds();
  if (!bounds)
    return;

  this.loadCount();
};

CGViewMapLayer.prototype.refreshLayer = function() {
    this.loadLayer();
};

CGViewMapLayer.prototype.loadCount = function() {
    Ext.Ajax.request({
        url: this.getSourceCountUrl(),
        method: "POST",
        callback: CGViewMapLayer.prototype.loadCountCallback.bind(this)
    }, this);
};

CGViewMapLayer.prototype.loadCountCallback = function (sOptions, bSuccess, Response) {
    this.count = parseInt(Response.responseText);
    this.updatePagesToolbar();
    this.loadLayer(this.getSourceUrl());
};

CGViewMapLayer.prototype.loadLayer = function () {

  if (CGViewMapLayer.layer) {
    for (var i = 0; i < CGViewMapLayer.layer.docs.length; i++) {
        CGViewMapLayer.layer.hideDocument(CGViewMapLayer.layer.docs[i]);
    }
  }

  if (this.options.layer == "heat")
    this.loadHeatLayer();
  else
    this.loadKmlLayer();
};

CGViewMapLayer.prototype.loadHeatLayer = function() {
  this.extLoading.dom.style.display = "block";
  Ext.Ajax.request({
    url: this.getSourceUrl(),
    method: "POST",
    callback: CGViewMapLayer.prototype.loadHeatLayerCallback.bind(this)
  }, this);
};

CGViewMapLayer.prototype.loadHeatLayerCallback = function (sOptions, bSuccess, Response) {
  eval("var data = " + Response.responseText);
  var pointArray = new google.maps.MVCArray(data);

  if (this.lastHeatMap != null)
    this.lastHeatMap.setMap(null);

  var heatMap = new google.maps.visualization.HeatmapLayer({data: pointArray});
  heatMap.setMap(CGViewMapLayer.map);

  this.lastHeatMap = heatMap;
  this.extLoading.dom.style.display = "none";
};

CGViewMapLayer.prototype.loadKmlLayer = function () {
  if (CGViewMapLayer.loadingKml || CGViewMapLayer.map == null) return;

  CGViewMapLayer.loadingKml = true;
  CGViewMapLayer.oms = new OverlappingMarkerSpiderfier(CGViewMapLayer.map, {keepSpiderfied: true});
  CGViewMapLayer.projHelper = CGViewMapLayer.oms.projHelper.getProjection() != null ? CGViewMapLayer.oms.projHelper : CGViewMapLayer.projHelper;
  if (CGViewMapLayer.projHelper != null) {
    CGViewMapLayer.projHelper.map = CGViewMapLayer.map;
    CGViewMapLayer.oms.projHelper = CGViewMapLayer.projHelper;
  }
  CGViewMapLayer.infoWindow = new google.maps.InfoWindow();

  CGViewMapLayer.layer = new geoXML3.parser({
      map: CGViewMapLayer.map,
      overlappingMarkerSpiderfier: CGViewMapLayer.oms,
      zoom: false,
      processStyles: true,
      polylineOptions: LineStringOptions,
      polygonOptions: PolygonOptions,
      singleInfoWindow: true,
      infoWindow: CGViewMapLayer.infoWindow,
      pmParseFn: this.atPlacemarkParsed.bind(this),
      afterParse: this.atKmlLoaded.bind(this)
  });

  geoXML3.onInfoWindowOpened = CGViewMapLayer.prototype.atInfoWindowOpened.bind(this);
  geoXML3.onInfoWindowClosed = CGViewMapLayer.prototype.atInfoWindowClosed.bind(this);
  geoXML3.onMarkerCreated = CGViewMapLayer.prototype.atMarkerCreated.bind(this);
  geoXML3.onMarkerClick = CGViewMapLayer.prototype.atMarkerClick.bind(this);

  google.maps.event.addListener(CGViewMapLayer.infoWindow, 'closeclick', CGViewMapLayer.prototype.atInfoWindowClosed.bind(this));

  this.extLoading.dom.style.display = "block";
  CGViewMapLayer.layer.parse(this.getSourceUrl());
};

CGViewMapLayer.prototype.atKmlLoaded = function () {
    this.extLoading.dom.style.display = "none";
    for (var i=0; i<CGViewMapLayer.oms.markers.length; i++) {
        const marker = CGViewMapLayer.oms.markers[i];
        marker.position = { lat: marker.position.lat(), lng: marker.position.lng() };
    }
    CGViewMapLayer.loadingKml = false;
};

CGViewMapLayer.prototype.idle = function () {
  if (this.boundsChanged) {
    this.boundsChanged = false;
    this.updatePagesToolbar();
    this.updateLayer();
  }
};

CGViewMapLayer.prototype.resize = function () {
  google.maps.event.trigger(CGViewMapLayer.map, "resize");
};

// #############################################################################################################
CGViewMapLayer.prototype.atResize = function () {
  this.resize();
};

CGViewMapLayer.prototype.atPlacemarkParsed = function (xmlNode, placemark) {
  var aExtendedData = xmlNode.getElementsByTagName('ExtendedData');

  if (placemark.point == null) placemark.point = { lat: placemark.latlng.lat(), lng: placemark.latlng.lng() };

  if (aExtendedData && (aExtendedData.length > 0)) {
    var extendedData = aExtendedData[0];
    var extendedDataObject = new Object();
    var extraDataValue = "";

    for (var i = 0; i < extendedData.childNodes.length; i++) {
      var dataNode = extendedData.childNodes[i];
      extendedDataObject[dataNode.localName] = dataNode.textContent;
      if (dataNode.localName == "id") {
        placemark.nodeId = dataNode.textContent;
        continue;
      }
      if (dataNode.textContent == "") continue;
      extraDataValue += "<div>" + dataNode.textContent + "</div>";
    }

    extendedDataObject.label = placemark.name;
    extendedDataObject.name = placemark.name;
    extendedDataObject.description = placemark.description;
    extendedDataObject.extraData = extraDataValue;
    placemark.extendedData = extendedDataObject;
  }
};

CGViewMapLayer.prototype.atInfoWindowOpened = function (infoWindow, marker) {
  infoWindow.setContent(this.infoWindowTemplate.applyTemplate(marker.extendedData));
  this.currentNodeId = marker.nodeId;
};

CGViewMapLayer.prototype.atInfoWindowClosed = function () {
  this.currentNodeId = null;
};

CGViewMapLayer.prototype.atMarkerClick = function (marker) {
  if (!this.onLocationClick) return;
  var location = { code: marker.nodeId, value: marker.title};
  this.onLocationClick(location);
};

CGViewMapLayer.prototype.atMarkerCreated = function (marker, placemark) {

  if (CGViewMapLayer.oms != null)
    CGViewMapLayer.oms.addMarker(marker);

  if (this.currentNodeId && this.currentNodeId == placemark.nodeId) {
    placemark.marker.infoWindow.setContent(this.infoWindowTemplate.applyTemplate(marker.extendedData));
    placemark.marker.infoWindow.open(CGViewMapLayer.map, placemark.marker);
  }
};

CGViewMapLayer.prototype.atClickPlace = function (position) {
  alert(this.monetExtData.id);
};

CGViewMapLayer.prototype.atGPSPositioned = function (position) {
  if (CGViewMapLayer.map == null) return;

  var latitude = position.coords.latitude;
  var longitude = position.coords.longitude;

  CGViewMapLayer.map.panTo({ lat: latitude, lng: longitude });
};

CGViewMapLayer.prototype.atLoadMapOptions = function () {
    return this.options;
};

CGViewMapLayer.prototype.atInitMap = function(map, googleInstance) {
  google = googleInstance;
  CGViewMapLayer.map = map;
  this.geocoder = new google.maps.Geocoder();
  //this.updateLayer();
};

CGViewMapLayer.prototype.clearLayers = function () {
    if (CGViewMapLayer.oms != null) {
        CGViewMapLayer.oms.clearMarkers();
        CGViewMapLayer.oms.map = null;
    }
    CGViewMapLayer.oms = null;
    if (CGViewMapLayer.map != null) {
        google.maps.event.clearListeners(CGViewMapLayer.map, 'click');
        if (CGViewMapLayer.infoWindow != null) google.maps.event.clearListeners(CGViewMapLayer.infoWindow, 'closeclick');
    }
    CGViewMapLayer.loadingKml = false;
    if (CGViewMapLayer.layer == null) return;
    if (CGViewMapLayer.layer.docs) {
      CGViewMapLayer.layer.docs.forEach((doc) => {
        if (doc.markers) {
          doc.markers.forEach(marker => marker.setMap(null));
        }
        if (doc.groundOverlays) {
          doc.groundOverlays.forEach(overlay => overlay.setMap(null));
        }
        if (doc.polylines) {
          doc.polylines.forEach(line => line.setMap(null));
        }
        if (doc.polygons) {
          doc.polygons.forEach(poly => poly.setMap(null));
        }
        if (doc.infoWindows) {
          doc.infoWindows.forEach(info => info.close());
        }
      });
    }
    CGViewMapLayer.layer = null;
};

CGViewMapLayer.prototype.atBoundsChanged = function () {
  if (CGViewMapLayer.map == null) return;
  this.boundsChanged = (!this.isSameBounds(this.mapBounds));
  this.mapBounds = CGViewMapLayer.map.getBounds();
};

CGViewMapLayer.prototype.atIdle = function () {
  this.idle();
};

CGViewMapLayer.prototype.atSearchKeyUp = function (event) {
  var codeKey = event.getKey();
  if (codeKey == event.ENTER) this.gotoPlace(this.searchValue.dom.value);
  return false;
};

CGViewMapLayer.prototype.atSearchClick = function () {
  this.gotoPlace(this.searchValue.dom.value);
};

CGViewMapLayer.prototype.atGotoPlaceInfoArrived = function (geocoderResult) {
  if (!geocoderResult || (geocoderResult.length < 1)) return;
  CGViewMapLayer.map.setCenter(geocoderResult[0].geometry.location);
  this.updateLayer();
  this.addListeners();
};
