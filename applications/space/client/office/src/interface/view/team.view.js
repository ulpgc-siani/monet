CGViewTeam = function () {
  this.base = CGView;
  this.base();
  this.Type = VIEW_TEAM;
};

CGViewTeam.prototype = new CGView;

CGViewTeam.prototype.refreshTeam = function () {
  EventManager.disableNotifications();
  this.DOMLayer.init();
  EventManager.enableNotifications();
};

CGViewTeam.prototype.refresh = function () {
  var extLayer, IdDOMLayer;
  var Styles;
  var sContent = this.Target.getContent();

  if (!this.Target) return;

  if (sContent != null && sContent != "") {
    this.Target.setContent("");

    extLayer = Ext.get(this.DOMLayer);
    IdDOMLayer = (this.DOMLayer.id) ? this.DOMLayer.id : Ext.id();
    Styles = extLayer.getStyles("position", "visibility", "left", "top");

    sContent = setIdToElementContent(IdDOMLayer, sContent);
    extLayer.dom = replaceDOMElement(extLayer.dom, sContent);
    this.setDOMLayer($(IdDOMLayer));

    extLayer = Ext.get(this.DOMLayer);
    extLayer.applyStyles(Styles);
    extLayer.dom.IdView = this.Id;
  }

  if (this.Mode != null) {
    Constructor = Extension.getTeamConstructor();
    Constructor.init(this.DOMLayer);
    CommandListener.capture(this.DOMLayer);
  }

  this.refreshTeam();
};

//#############################################################################################################