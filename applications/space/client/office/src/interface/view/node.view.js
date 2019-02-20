CGViewNode = function () {
  this.base = CGView;
  this.base();
  this.Type = VIEW_NODE;
  this.aEditors = new Array();
};

CGViewNode.prototype = new CGView;

CGViewNode.prototype.selectNodesReferences = function (aNodes) {
  if (this.DOMLayer == null) return false;
  if (!this.DOMLayer.setSelectedNodesReferences) return false;
  return this.DOMLayer.setSelectedNodesReferences(aNodes);
};

CGViewNode.prototype.refreshNode = function () {
  EventManager.disableNotifications();
  if (this.DOMLayer.init) this.DOMLayer.init(ViewerSidebar.getHelper(Helper.EDITORS));
  this.DOMLayer.onFieldFocus = CGViewNode.prototype.atNodeFieldFocus.bind(this);
  this.DOMLayer.onFieldChange = CGViewNode.prototype.atNodeFieldChange.bind(this);
  this.DOMLayer.onFieldBlur = CGViewNode.prototype.atNodeFieldBlur.bind(this);
  this.DOMLayer.onTabFocus = CGViewNode.prototype.atNodeTabFocus.bind(this);
  EventManager.enableNotifications();
};

CGViewNode.prototype.parseMonetLinks = function (sContent) {
  var Expression = new RegExp(MONET_LINK_PATTERN, "g");
  var aResult = Expression.exec(sContent);

  while (aResult != null) {
    sContent = sContent.replace(aResult[0], "<a class='command' href='" + getMonetLinkAction(sContent) + "'>" + Lang.ViewNode.Show + "</a>");
    aResult = Expression.exec(sContent);
  }

  return sContent;
};

CGViewNode.prototype.refresh = function () {
  var extLayer, IdDOMLayer;
  var Styles, DOMLayer;
  var sContent = this.Target.getContent();

  if (!this.Target) return;
  if (sContent != null && sContent != "") {
    this.Target.setContent("");

    extLayer = Ext.get(this.DOMLayer);
    IdDOMLayer = (this.DOMLayer.id) ? this.DOMLayer.id : Ext.id();
    Styles = extLayer.getStyles("position", "visibility", "left", "top");

    sContent = setIdToElementContent(IdDOMLayer, sContent);
    sContent = this.parseMonetLinks(sContent);
    extLayer.dom = replaceDOMElement(extLayer.dom, sContent);
    DOMLayer = $(IdDOMLayer);

    if (DOMLayer == null) return;
    this.setDOMLayer(DOMLayer);

    extLayer = Ext.get(this.DOMLayer);
    if (this.ViewContainer == null) extLayer.applyStyles(Styles);
  }

  if (this.Mode != null) {
    var Constructor = Extension.getNodeConstructor();
    Constructor.onSelectNodeReference = CGViewNode.prototype.atSelectNodeReference.bind(this);
    Constructor.init(this.DOMLayer);

    if (sContent != null && sContent != "")
      CommandListener.capture(this.DOMLayer);

    BehaviourDispatcher.apply(BehaviourViewNode, this.DOMLayer);
  }

  this.refreshNode();
};

// #############################################################################################################

CGViewNode.prototype.atSelectNodeReference = function (IdCollection, IdSelectedNode, bSelected) {
  if (bSelected) State.addNodeReferenceToSelection(IdCollection, IdSelectedNode);
  else delete State.deleteSelectedNodeReference(IdCollection, IdSelectedNode);
};

CGViewNode.prototype.atNodeFieldFocus = function (DOMField) {
  EventManager.notify(EventManager.FOCUS_FIELD, {"Node": this.Target, "DOMNode": this.DOMLayer, "DOMField": DOMField});
};

CGViewNode.prototype.atNodeFieldChange = function (DOMField) {
  EventManager.notify(EventManager.CHANGE_FIELD, {"Node": this.Target, "DOMNode": this.DOMLayer, "DOMField": DOMField});
};

CGViewNode.prototype.atNodeFieldBlur = function (DOMField) {
  var helper = ViewerSidebar.getHelper(Helper.EDITORS);
  EventManager.notify(EventManager.BLUR_FIELD, {"Node": this.Target, "DOMNode": this.DOMLayer, "DOMField": DOMField});
  helper.hideCurrentEditor();
};

CGViewNode.prototype.atNodeTabFocus = function (DOMNode, DOMTab, DOMView) {
  ViewerSidebar.hide();
  EventManager.notify(EventManager.FOCUS_NODE_VIEW, {"Node": this.Target, "DOMNode": DOMView});
};