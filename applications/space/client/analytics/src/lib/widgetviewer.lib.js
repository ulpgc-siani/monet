function WidgetViewer() {
  this.templates = new Object();
  this.widgets = new Array();
  this.templates = new Object();
  this.templates["es"] = new Array();
  this.templates["es"].Layout = "<div class='widgetviewer'><ul class='widgets'></ul></div>";
  this.templates["es"].Widget = "<li id='widget_${position}' class='widget ${predefined} ${css}'><a class='content'></a></li>";
}

WidgetViewer.prototype.init = function(config) {
  this.language = "es";
  if (config.language) this.language = config.language;
};

WidgetViewer.prototype.setTarget = function(widgets) {
  this.widgets = widgets;
};

WidgetViewer.prototype.getWidgetPosition = function(id) {
  for (var i=0; i<this.widgets.length; i++)
    if (id == this.widgets[i].id) return i;
  return -1;
};

WidgetViewer.prototype.get = function(id) {
  var widget = new Object();
  var position = this.getWidgetPosition(id);
  widget.DOMLayer = $("#widget_" + position).get(0);
  widget.id = this.widgets[position].id;
  widget.getDOMContent = function() {
    var jLayer = $(this.DOMLayer);
    return jLayer.find(".content").get(0);
  };
  widget.setColor = function(color) {
    var jLayer = $(this.DOMLayer);
    jLayer.css("border-color", color);
  };
  widget.activate = function() {
    var jLayer = $(this.DOMLayer);
    jLayer.addClass("active");
  };
  widget.deactivate = function() {
    var jLayer = $(this.DOMLayer);
    jLayer.removeClass("active");
  };
  widget.getColor = function() {
    var jLayer = $(this.DOMLayer);
    return jLayer.get(0).style.borderColor;
  };
  widget.getWidth = function() {
    var jLayer = $(this.DOMLayer);
    return jLayer.width();
  };
  return widget;
};

WidgetViewer.prototype.render = function(DOMLayer) {
  this.DOMLayer = DOMLayer;
  
  var layoutTemplate = this.templates[this.language].Layout;
  var jLayout = $(layoutTemplate);
  $(this.DOMLayer).append(jLayout);

  var jWidgets = jLayout.find(".widgets");
  for (var i=0; i<this.widgets.length; i++) {
    var widget = this.widgets[i];
    var predefined = (widget.predefined)?" predefined":"";
    widget.predefined = predefined;
    widget.position = i;
    $.tmpl(this.templates[this.language].Widget, widget).appendTo(jWidgets);
    var jWidget = $("#widget_" + widget.position);
    var jContent = jWidget.find(".content");
    jContent.html(widget.content);
    jContent.click(WidgetViewer.prototype.atWidgetClick.bind(this, jContent.get(0)));
    if (widget.color) jWidget.get(0).style.borderColor = widget.color;
  }
};

WidgetViewer.prototype.atWidgetClick = function(DOMWidgetLink) {
  var jWidget = $(DOMWidgetLink).parents("li:first");
  var position = jWidget.get(0).id.replace("widget_", "");
  if (this.onClick) this.onClick(this, this.get(this.widgets[position].id));
};