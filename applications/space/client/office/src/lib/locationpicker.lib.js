LOCATIONPICKER_CLASS_EDITING = "editing";

var aLocationPickerMessages = new Array();
aLocationPickerMessages["es"] = new Array();
aLocationPickerMessages["es"]["LOCATE"] = "Localiza el elemento en el mapa. Puedes representar el elemento con un punto, una línea o un polígono.";
aLocationPickerMessages["es"]["LOCATED"] = "Si desea volver a representar el elemento en el mapa, haga click en borrar marcador.";
aLocationPickerMessages["es"]["FINISH_EDITION"] = "Ha finalizado la edición correctamente. Para deslocalizar el elemento haga click en Borrar localización.";
aLocationPickerMessages["es"]["SET_POINT"] = "Arrastra el marcador y colócalo en el punto donde desea ubicar el elemento.";
aLocationPickerMessages["es"]["SET_LINE"] = "Haga click en las esquinas de la línea que vaya a construir. Finalize la edición de la línea haciendo click en el botón derecho o pulsando sobre el botón de finalizar edición.";
aLocationPickerMessages["es"]["SET_POLYGON"] = "Haga click en los vértices del polígono que vaya a construir. Finalize la edición del polígono haciendo click en el botón derecho o pulsando sobre el botón de finalizar edición.";
aLocationPickerMessages["es"]["SEARCHING_GPS_POSITION"] = "Buscando posición GPS actual...";
aLocationPickerMessages["es"]["SEARCHING_GPS_POSITION_ERROR"] = "Su navegador no permite obtener la posición, actualize a una versión más reciente.";
aLocationPickerMessages["es"]["PLACE_NOT_FOUND"] = "No se ha encontrado la ubicación indicada.";

function WKTWriter() {
  this.writer;
};

WKTWriter.prototype.writeType = function (type) {
  this.writer = type + " ";
};

WKTWriter.prototype.writeStartArray = function () {
  this.writer += "(";
};

WKTWriter.prototype.writeEndArray = function () {
  if (this.writer.charAt(this.writer.length - 2) == ',')
    this.writer = this.writer.substring(0, this.writer.length - 2);
  this.writer += ")";
};

WKTWriter.prototype.writePoint = function (lat, lng) {
  this.writer += lat + " " + lng + ", ";
};

WKTWriter.prototype.get = function () {
  return this.writer;
};

LocationPicker = function () {
  this.base = CGView;
  this.base();
  this.Type = VIEW_NODE_TYPE_NODE;
  this.aEditors = new Array();
  this.Language = "es";
};

LocationPicker.prototype = new CGView;

LocationPicker.prototype.init = function (options, mapLayer, infoPanelLayer) {
  this.options = options;

  this.mapLayer = mapLayer;

  if (this.options.language && aLocationPickerMessages[this.options.language]) this.Language = this.options.language;

  this.infoPanelLayer = infoPanelLayer;
  this.infoPanelLayer.dom.innerHTML = aLocationPickerMessages[this.Language]["LOCATE"];

  Ext.EventManager.onWindowResize(this.atResize, this);
};

LocationPicker.prototype.resize = function () {
  if (this.map == null) return;
  google.maps.event.trigger(this.map, "resize");
};

LocationPicker.prototype.atResize = function () {
  this.resize();
};

LocationPicker.prototype.createPolygon = function (polygon) {
  var array = new Array();
  for (var i = 0; i < polygon.length; i++) {
    array.push(this.createLineString(polygon[i]));
  }
  return array;
};

LocationPicker.prototype.createLineString = function (lineString) {
  var array = new Array();
  for (var i = 0; i < lineString.length; i++) {
    var latlng = new google.maps.LatLng(lineString[i][0], lineString[i][1]);
    array.push(latlng);
  }
  return array;
};

LocationPicker.prototype.refresh = function () {
  var located = this.isLocated();

  if (located)
    this.infoPanelLayer.dom.innerHTML = aLocationPickerMessages[this.Language]["LOCATED"];
  else
    this.infoPanelLayer.dom.innerHTML = aLocationPickerMessages[this.Language]["LOCATE"];

  if (this.onRefresh) this.onRefresh();
};

LocationPicker.prototype.setPlace = function (place) {
  if (place == "") return;
  if (this.geocoder) this.geocoder.geocode({address: place, language: this.Language}, LocationPicker.prototype.atSetPlaceInfoArrived.bind(this));
};

LocationPicker.prototype.setLocation = function (location) {
  window.monetMapOptions = this.atLoadMapOptions.bind(this);
  window.monetMapInit = this.atInitMap.bind(this);
  window.monetMapOnBoundsChanged = function() {};
  window.monetMapOnIdle = function() {};

  this.options.zoom = location ? location.zoom : 12;

  var centerLat = 15;
  var centerLng = 15;
  if (location) {
    this.location = location;
    centerLat = location.center[0];
    centerLng = location.center[1];
  } else {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(this.atGPSPositioned.bind(this));
    } else {
      this.options.zoom = 10;
    }
  }
  this.options.center = { lat: centerLat, lng: centerLng };

  this.location = location;
  this.insertMap();
};

LocationPicker.prototype.insertMap = function() {
  const iframe = document.createElement("iframe");
  iframe.className = "map";
  iframe.style.border = "0";
  iframe.style.padding = "0";
  iframe.width = "100%";
  iframe.height = "100%";
  iframe.src = `${Context.Config.Url}/map.html?key=${Context.Config.Map.ApiKey}&m=${Math.random()}`;

  const container = this.mapLayer.dom;
  if (container == null) {
    console.error(`Container for map not found.`);
    return;
  }

  const mapFrame = container.querySelector("iframe.map");
  if (mapFrame != null) container.removeChild(mapFrame);
  container.appendChild(iframe);
}

LocationPicker.prototype.atLoadMapOptions = function () {
    return this.options;
};

LocationPicker.prototype.atInitMap = function(map, googleInstance) {
  google = googleInstance;
  this.map = map;
  this.geocoder = new google.maps.Geocoder();
  const location = this.location;
  if (location) {
    if (location.type == G_POINT) {
      var latlng = new google.maps.LatLng(location.geometry[0], location.geometry[1]);
      this.marker = new google.maps.Marker({
        draggable: true,
        position: latlng,
        map: this.map
      });
      google.maps.event.addListener(this.marker, 'dragend', this.atMarkerMoved.bind(this));
    } else if (location.type == G_LINESTRING) {
      var polyOptions = {
        path: this.createLineString(location.geometry),
        strokeColor: '#0000FF',
        strokeOpacity: 0.5,
        strokeWeight: 3,
        map: this.map,
        cursor: "help"
      };
      this.polyline = new google.maps.Polyline(polyOptions);
    } else if (location.type == G_POLYGON) {
      var polyOptions = this.polygonOptions(location.geometry);
      this.polygon = new google.maps.Polygon(polyOptions);
    } else if (location.type == G_MULTIPOLYGON) {
        this.multiPolygons = [];
        for (var index in location.geometry) {
          if (isFunction(location.geometry[index])) continue;
          var polyOptions = this.polygonOptions(location.geometry[index]);
          this.multiPolygons.push(new google.maps.Polygon(polyOptions));
        }
    }
  }

  this.refresh();
};

LocationPicker.prototype.polygonOptions = function(geometry) {
    return {
        paths: this.createPolygon(geometry),
        fillColor: '#0000FF',
        fillOpacity: 0.2,
        strokeColor: '#0000FF',
        strokeOpacity: 0.5,
        strokeWeight: 3,
        map: this.map
    };
},

LocationPicker.prototype.clean = function () {
    if (this.marker) {
      this.marker.setMap(null);
      this.marker = null;
    }
    if (this.polyline) {
      this.polyline.setMap(null);
      this.polyline = null;
    }
    if (this.polygon) {
      this.polygon.setMap(null);
      this.polygon = null;
    }
    if (this.multiPolygons) {
        for (var i=0; i<this.multiPolygons.length; i++)
          this.multiPolygons[i].setMap(null);
        this.multiPolygons = null;
    }

  this.atFinishEditing();
};

LocationPicker.prototype.save = function () {
  var writer = new WKTWriter();
  if (this.marker) {
    var pos = this.marker.getPosition();

    writer.writeType(G_POINT);
    writer.writeStartArray();
    writer.writePoint(pos.lat(), pos.lng());
    writer.writeEndArray();
  } else if (this.polyline) {
    writer.writeType(G_LINESTRING);
    writer.writeStartArray();

    var path = this.polyline.getPath();
    var pathLength = path.getLength();

    if (pathLength <= 1) {
      this.polyline.setMap(null);
      this.polyline = null;
      if (this.onCleanLocation) this.onCleanLocation();
      return;
    }

    for (var i = 0; i < pathLength; i++) {
      var pos = path.getAt(i);
      writer.writePoint(pos.lat(), pos.lng());
    }

    writer.writeEndArray();
  } else if (this.polygon) {
    writer.writeType(G_POLYGON);
    writer.writeStartArray();

    var paths = this.polygon.getPaths();
    var pathsLength = paths.getLength();

    if (pathsLength < 1) {
      this.polygon.setMap(null);
      this.polygon = null;
      if (this.onCleanLocation) this.onCleanLocation();
      return;
    }

    for (var j = 0; j < pathsLength; j++) {
      writer.writeStartArray();

      var path = paths.getAt(j);
      var pathLength = path.getLength();
      for (var i = 0; i < pathLength; i++) {
        var pos = path.getAt(i);
        writer.writePoint(pos.lat(), pos.lng());
      }

      if (pathLength > 0) {
        var pos = path.getAt(0);
        writer.writePoint(pos.lat(), pos.lng());
      }
      writer.writeEndArray();
    }

    writer.writeEndArray();
  } else {
    if (this.onCleanLocation) this.onCleanLocation(this);
    return;
  }

  var Location = writer.get();
  if (this.onDrawLocation) this.onDrawLocation(this, Location);
};


LocationPicker.prototype.setMarkerTo = function (position) {
  this.marker = new google.maps.Marker({
    draggable: true,
    position: position,
    map: this.map
  });

  google.maps.event.addListener(this.marker, 'dragend', this.atMarkerMoved.bind(this));
};

LocationPicker.prototype.positionate = function () {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(this.atGPSPositionChange.bind(this),
        this.atGPSPositionChangeError.bind(this),
        { enableHighAccuracy: true });
    this.infoPanelLayer.dom.innerHTML = aLocationPickerMessages[this.Language]["SEARCHING_GPS_POSITION"];
  } else {
    alert(aLocationPickerMessages[this.Language]["SEARCHING_GPS_POSITION_ERROR"]);
  }
};

LocationPicker.prototype.drawPoint = function () {
  this.clean();

  this.infoPanelLayer.dom.innerHTML = aLocationPickerMessages[this.Language]["SET_POINT"];
  this.mapLayer.addClass(LOCATIONPICKER_CLASS_EDITING);
  this.editing = true;
  this.refresh();

  google.maps.event.addListener(this.map, 'click', this.atAddMarker.bind(this));
};

LocationPicker.prototype.drawLine = function () {
  this.clean();

  this.infoPanelLayer.dom.innerHTML = aLocationPickerMessages[this.Language]["SET_LINE"];
  this.mapLayer.addClass(LOCATIONPICKER_CLASS_EDITING);
  this.editing = true;
  this.refresh();

  var polyOptions = {
    strokeColor: '#0000FF',
    strokeOpacity: 0.5,
    strokeWeight: 3,
    map: this.map
  };
  this.polyline = new google.maps.Polyline(polyOptions);

  google.maps.event.addListener(this.map, 'click', this.atAddPoint.bind(this));
  google.maps.event.addListener(this.map, 'rightclick', this.atFinishEditing.bind(this));
};

LocationPicker.prototype.drawPolygon = function () {
  this.clean();

  this.infoPanelLayer.dom.innerHTML = aLocationPickerMessages[this.Language]["SET_POLYGON"];
  this.mapLayer.addClass(LOCATIONPICKER_CLASS_EDITING);
  this.editing = true;
  this.refresh();

  var polyOptions = {
    fillColor: '#0000FF',
    fillOpacity: 0.2,
    strokeColor: '#0000FF',
    strokeOpacity: 0.5,
    strokeWeight: 3,
    map: this.map
  };
  this.polygon = new google.maps.Polygon(polyOptions);

  google.maps.event.addListener(this.map, 'click', this.atAddPoint.bind(this));
  google.maps.event.addListener(this.map, 'rightclick', this.atFinishEditing.bind(this));
};

LocationPicker.prototype.panToCenter = function () {
  if (!this.location) return;

  this.map.panTo({lat: this.location.center[0], lng: this.location.center[1]});
};

LocationPicker.prototype.finishDraw = function () {
  this.mapLayer.removeClass(LOCATIONPICKER_CLASS_EDITING);
  this.atFinishEditing();
};

LocationPicker.prototype.cancelDraw = function () {
  this.mapLayer.removeClass(LOCATIONPICKER_CLASS_EDITING);
  this.clean();
};

LocationPicker.prototype.isLocated = function () {
  return (this.marker != null || this.polyline != null || this.polygon != null || this.multiPolygons != null);
};

LocationPicker.prototype.isEditing = function () {
  return this.editing;
};

// #############################################################################################################
LocationPicker.prototype.atGPSPositioned = function (position) {
  var latitude = position.coords.latitude;
  var longitude = position.coords.longitude;

  this.map.panTo({ lat: latitude, lng: longitude});
};

LocationPicker.prototype.atMarkerMoved = function () {
  this.save();
};

LocationPicker.prototype.atAddMarker = function (event) {

  this.marker = new google.maps.Marker({
    draggable: true,
    position: event.latLng,
    map: this.map
  });

  google.maps.event.addListener(this.marker, 'dragend', this.atMarkerMoved.bind(this));

  this.atFinishEditing();
};

LocationPicker.prototype.atAddPoint = function (event) {
  var path = null;
  if (this.polyline)
    path = this.polyline.getPath();
  else
    path = this.polygon.getPath();
  path.push(event.latLng);
};

LocationPicker.prototype.atFinishEditing = function (event) {
  google.maps.event.clearListeners(this.map, "click");
  google.maps.event.clearListeners(this.map, "rightclick");

  this.save();
  this.infoPanelLayer.dom.innerHTML = aLocationPickerMessages[this.Language]["FINISH_EDITION"];

  this.mapLayer.removeClass(LOCATIONPICKER_CLASS_EDITING);
  this.editing = false;
  this.refresh();

  return false;
};

LocationPicker.prototype.atGPSPositionChange = function (position) {
  var latitude = position.coords.latitude;
  var longitude = position.coords.longitude;

  this.clean();

  var pos = new google.maps.LatLng(latitude, longitude);
  this.setMarkerTo(pos);
  this.map.panTo({ lat: latitude, lng: longitude});
  this.save();
};

LocationPicker.prototype.atGPSPositionChangeError = function (err) {
  this.infoPanelLayer.dom.innerHTML = err.message;
};

LocationPicker.prototype.atSetPlaceInfoArrived = function (geocoderResult) {
  if (!geocoderResult || (geocoderResult.length < 1)) this.infoPanelLayer.dom.innerHTML = aLocationPickerMessages["es"]["PLACE_NOT_FOUND"];
  this.map.panTo(geocoderResult[0].geometry.location);
};
