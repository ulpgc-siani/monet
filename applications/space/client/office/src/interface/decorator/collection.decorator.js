CSS_UPDATE_MESSAGE = ".message.update";

CGDecoratorCollection = function () {
};

CGDecoratorCollection.prototype = new CGDecorator;

CGDecoratorCollection.prototype.execute = function (DOMCollection) {

  this.addCommonMethods(DOMCollection);

  DOMCollection.getFields = function () {
    var aFields = new Array();
    var extNode = Ext.get(this);
    var extFieldList;

    if (!(extFieldList = extNode.select(CSS_FIELD))) return false;

    extFieldList.each(function (extField) {
      if (extField.dom.belongsToTemplate()) return;
      aFields.push(extField.dom);
    }, this);

    return aFields;
  };

  DOMCollection.init = function (Editors) {
    this.initToolbar(".header .content .toolbar");
    this.initTabs(CSS_NODE);

    if (!this.isEditable()) return;

    this.Editors = Editors;
    this.aMemento = new Array();
    this.IndexMemento = -1;

    this.initFields(this.getFields());
  };

  DOMCollection.destroy = function () {
    if (!this.isEditable()) return;
    this.destroyFields(this.getFields());
    this.Editors = null;
    this.aMemento = new Array();
  };

  DOMCollection.refresh = function () {
    var extCollection = Ext.get(this);
    if (!(NodeReferenceList = extCollection.select(CSS_REFERENCE))) return false;
    if (!(HiddenableList = extCollection.select(CSS_HIDDENABLE))) return false;

    var iCountNodesReferences = NodeReferenceList.getCount();
    HiddenableList.each(function (Hiddenable) {
      sVisibleClass = (Hiddenable.hasClass('inline')) ? "inline" : "block";
      Hiddenable.dom.style.display = (iCountNodesReferences != 0) ? sVisibleClass : "none";
    });
  };

  DOMCollection.getContent = function () {
    var ControlInfo = this.getControlInfo();
    var extCollection = Ext.get(this);
    var aDOMNodesReferences = this.getNodesReferences();
    var sContent = "";
    var NodeList = new CGNodeList();
    var Node = new CGNode();

    for (var iPos = 0; iPos < aDOMNodesReferences.length; iPos++) {
      var DOMNodeReference = aDOMNodesReferences[iPos];
      if (DOMNodeReference.isEditable()) sContent += DOMNodeReference.getContent();
    }

    if (sContent == "") return "";

    Node.setId(ControlInfo.IdNode);
    Node.setCode(ControlInfo.Code);
    sContent = NodeList.serializeWithData(sContent);
    sContent = Node.serializeWithData(sContent);

    return sContent;
  };

  DOMCollection.addNodeReference = function (sNodeReferenceContent) {
    var extNodeReference, extNodeReferenceList, extCollection = Ext.get(this);
    if (!(extNodeReferenceList = extCollection.select(CSS_REFERENCE_LIST).first())) return false;
    extNodeReference = new Insertion.Bottom(extNodeReferenceList.dom, sNodeReferenceContent).element.immediateDescendants().last();
    this.refresh();
    return extNodeReference;
  };

  DOMCollection.deleteNodeReferences = function (Id) {
    var aExtNodeReferences;
    var extCollection = Ext.get(this);

    if ((aExtNodeReferences = extCollection.select(CSS_REFERENCE + DOT + Id)) == null) return false;

    aExtNodeReferences.each(function (extNodeReference) {
      extNodeReference.remove();
      this.refresh();
    }, this);

  };

  DOMCollection.isMagnetized = function (CodeType) {
    var extCollection = Ext.get(this);
    var extMagnetList = extCollection.select(".magnet");
    var bMagnetized = false;

    extMagnetList.each(function (extMagnet) {
      if (bMagnetized) return;
      if (extMagnet.dom.innerHTML == CodeType) bMagnetized = true;
    }, this);

    return bMagnetized;
  };

  DOMCollection.isEditionMode = function () {
    var ControlInfo = this.getControlInfo();
    return (ControlInfo.Mode == MODE_EDITION);
  };

  DOMCollection.isRefreshable = function () {
    var ControlInfo = this.getControlInfo();
    return (ControlInfo.Templates.Refresh != null);
  };

  DOMCollection.getData = function () {
  };

  DOMCollection.scrollTo = function (bAnimate) {
    if (bAnimate) this.highlight();
    //this.scrollTo();
  };

  DOMCollection.highlight = function () {
    var extCollection = Ext.get(this);
    new Effect.Highlight(extCollection.dom, {duration: HIGHLIGHT_DURATION});
  };

  DOMCollection.update = function () {
    var extCollection = Ext.get(this);
    var extUpdateMessage = extCollection.down(CSS_UPDATE_MESSAGE);
    if (!extUpdateMessage) return true;
    extUpdateMessage.dom.style.display = "block";
    return true;
  };

};