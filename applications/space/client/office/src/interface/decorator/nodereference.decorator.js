CGDecoratorNodeReference = function () {
};

CGDecoratorNodeReference.prototype = new CGDecorator;

CGDecoratorNodeReference.prototype.execute = function (DOMNodeReference) {

  DOMNodeReference.isOpened = function () {
    var extNodeReference = Ext.get(this);
    if (!(eBody = extNodeReference.select(".body").first())) return false;
    return eBody.hasClass("fullscreen");
  };

  DOMNodeReference.open = function () {
    var extNodeReference = Ext.get(this);
    if (!(eBody = extNodeReference.select(".body").first())) return false;
    eBody.addClass("fullscreen");
    eBody.dom.style.display = "block";
  };

  DOMNodeReference.close = function () {
    var extNodeReference = Ext.get(this);
    if (!(eBody = extNodeReference.select(".body").first())) return false;
    eBody.removeClass("fullscreen");
    if (!eBody.hasClass("locked")) eBody.dom.style.display = "none";
  };

  DOMNodeReference.toggle = function () {
    var extNodeReference = Ext.get(this);
    if (!(eBody = extNodeReference.select(".body").first())) return false;
    if (eBody.isDisplayed()) this.close();
    else this.open();
  };

  DOMNodeReference.isHighlighted = function () {
    var extNodeReference = Ext.get(this);
    if (!(eHighlightedLink = extNodeReference.select(".command.star").first())) return false;
    return (eHighlightedLink.dom.hasClassName(CLASS_HIGHLIGHTED));
  };

  DOMNodeReference.toggleHighlighted = function () {
    var extNodeReference = Ext.get(this);
    if (!(eHighlightedLink = extNodeReference.select(".command.star").first())) return false;
    eHighlightedLink.dom.toggleClassName(CLASS_HIGHLIGHTED);
  };

  DOMNodeReference.isLoaded = function (html) {
    var extNodeReference = Ext.get(this);
    if (!(eBody = extNodeReference.select(".content").first())) return false;
    return (eBody.hasClass("loading") == false);
  };

  DOMNodeReference.load = function (html) {
    var extNodeReference = Ext.get(this);
    if (!(eBody = extNodeReference.select(".content").first())) return false;

    eBody.update(html);
    eBody.removeClass("loading");

    return eBody.dom;
  };

  DOMNodeReference.getInfo = function () {
    var extNodeReference = Ext.get(this);

    NodeReferenceInfo = new Object();
    NodeReferenceInfo.id = this.id.replace(NODEREFERENCE_ID_PREFIX, "");
    NodeReferenceInfo.idNode = (eId = extNodeReference.select(CSS_CONTROL_INFO + " > .idnode").first()) ? eId.dom.innerHTML : "-1";
    NodeReferenceInfo.title = (eTitle = extNodeReference.select(CSS_TITLE).first()) ? eTitle.dom.innerHTML : "Sin etiqueta";
    NodeReferenceInfo.description = (eDescription = extNodeReference.select(CSS_DESCRIPTION).first()) ? eDescription.dom.innerHTML : "Sin comentarios";
    NodeReferenceInfo.highlight = (eHighlight = extNodeReference.select(CSS_HIGHLIGHT).first()) ? (((eHighlight.dom.innerHTML == "true") || (eHighlight.dom.innerHTML == "yes")) ? true : false) : true;

    return NodeReferenceInfo;
  };

  DOMNodeReference.scrollTo = function (bAnimate) {
    if (bAnimate) this.highlight();
    //this.scrollTo();
  };

  DOMNodeReference.highlight = function () {
    var Info = this.getInfo();
    if (Info.highlight) new Effect.Highlight(this, {duration: HIGHLIGHT_DURATION});
  };

  DOMNodeReference.getSection = function () {
    var extNodeReference = Ext.get(this);
    var extElement = extNodeReference.up('.section');
    if (extElement) return extElement.dom;
    return null;
  };

  DOMNodeReference.getControlInfo = function () {
    var aResult = new Array();
    var extNodeReference = Ext.get(this);
    var ControlInfo;
    var extIdNode, extMode, extCode, extNodes, extResult;

    ControlInfo = new Object();
    ControlInfo.Id = this.id.replace(NODEREFERENCE_ID_PREFIX, "");
    ControlInfo.IdNode = (extIdNode = extNodeReference.select(CSS_CONTROL_INFO + " > .idnode").first()) ? extIdNode.dom.innerHTML : "-1";
    ControlInfo.Code = (extCode = extNodeReference.select(CSS_CONTROL_INFO + " > .code").first()) ? extCode.dom.innerHTML : "-1";
    ControlInfo.Mode = (extMode = extNodeReference.select(CSS_CONTROL_INFO + " > .mode").first()) ? extMode.dom.innerHTML : "-1";
    ControlInfo.Nodes = (extNodes = extNodeReference.select(CSS_CONTROL_INFO + " > .nodes").first()) ? extNodes.dom.innerHTML : null;
    ControlInfo.Templates = new Object();

    aResult = extNodeReference.select(".tpl.refresh");
    ControlInfo.Templates.Refresh = (extResult = aResult.first()) ? extResult.dom.innerHTML : null;

    aResult = extNodeReference.select(".tpl.edit");
    ControlInfo.Templates.Edit = (extResult = aResult.first()) ? extResult.dom.innerHTML : null;

    return ControlInfo;
  };

  DOMNodeReference.getParentNode = function () {
    var extNodeReference = Ext.get(this);
    var extElement = extNodeReference.up(CSS_NODE);
    if (extElement) return extElement.dom;
    return null;
  };

  DOMNodeReference.mark = function (Mark) {
    var extMark, sMessage = "", extNodeReference = Ext.get(this);
    if (Mark != null) eval("sMessage = (Lang.Decorator.NodeMark." + Mark + ")?Lang.Decorator.NodeMark." + Mark + ":''");
    if ((extMark = extNodeReference.select(CSS_MARK).first()) == null) return;
    extMark.dom.innerHTML = sMessage;
  };

  DOMNodeReference.getContent = function () {
    var Node = new CGNode();
    var sContent = EMPTY;
    var aResult = new Array();
    var ControlInfo = this.getControlInfo();
    var extNodeReference = Ext.get(this);
    var aExtFields;
    var AttributeList = new CGAttributeList();

    aExtFields = extNodeReference.select(CSS_FIELD + " input.root");
    aExtFields.each(function (extField) {
      if (!extField.dom.name) return;
      if (extField.up(CSS_REFERENCE).dom != this) return;
      aResult.push({code: extField.dom.name, id: extField.dom.name, name: extField.dom.name, value: extField.dom.value});
    }, this);

    if (aResult.length == 0) return "";

    Node.setId(ControlInfo.IdNode);
    Node.setCode(ControlInfo.Code);
    for (var iPos = 0; iPos < aResult.length; iPos++) {
      sContent += aResult[iPos].value;
    }
    sContent = AttributeList.serializeWithData(sContent);
    sContent = Node.serializeWithData(sContent);

    return sContent;
  };

};