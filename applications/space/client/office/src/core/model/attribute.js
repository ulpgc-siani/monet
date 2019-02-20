function CGAttribute() {
  this.code = "";
  this.iOrder = -1;
  this.AttributeList = new CGAttributeList();
  this.IndicatorList = new CGIndicatorList();
};

CGAttribute.OPTION = "option";

CGAttribute.prototype.getCode = function () {
  return this.code;
};

CGAttribute.prototype.setCode = function (code) {
  this.code = code;
};

CGAttribute.prototype.getOrder = function () {
  return this.iOrder;
};

CGAttribute.prototype.setOrder = function (iOrder) {
  this.iOrder = iOrder;
};

CGAttribute.prototype.addIndicatorByValue = function (code, iOrder, sValue) {
  this.IndicatorList.addIndicatorByValue(code, iOrder, sValue);
};

CGAttribute.prototype.getAttributeList = function () {
  return this.AttributeList;
};

CGAttribute.prototype.getAttributes = function () {
  return this.AttributeList.getAttributes();
};

CGAttribute.prototype.getAttribute = function (code) {
  return this.AttributeList.getAttribute(code);
};

CGAttribute.prototype.setAttributeList = function (AttributeList) {
  this.AttributeList = AttributeList;
};

CGAttribute.prototype.getIndicatorList = function () {
  return this.IndicatorList;
};

CGAttribute.prototype.getIndicators = function () {
  return this.IndicatorList.getIndicators();
};

CGAttribute.prototype.getIndicator = function (code) {
  return this.IndicatorList.getIndicator(code);
};

CGAttribute.prototype.getIndicatorValue = function (code) {
  var Indicator = this.IndicatorList.getIndicator(code);
  if (Indicator == null) return "";
  return Indicator.getValue();
};

CGAttribute.prototype.setIndicatorList = function (IndicatorList) {
  this.IndicatorList = IndicatorList;
};

CGAttribute.prototype.unserialize = function (sData) {
  if (sData == "") return;
  this.unserializeFromXmlDocument(parseXml(sData).childNodes[0]);
};

CGAttribute.prototype.unserializeFromXmlDocument = function (XmlAttribute) {
  var aChildren = XmlAttribute.childNodes;

  this.code = XmlAttribute.getAttribute("code");
  this.iOrder = XmlAttribute.getAttribute("order");

  if (aChildren == null) aChildren = XmlAttribute.children;
  if (aChildren == null) return;

  for (var iPos = 0; iPos < aChildren.length; iPos++) {
    if (aChildren[iPos].tagName.toLowerCase() == "attributelist") this.AttributeList.unserializeFromXmlDocument(aChildren[iPos]);
    else if (aChildren[iPos].tagName.toLowerCase() == "indicatorlist") this.IndicatorList.unserializeFromXmlDocument(aChildren[iPos]);
  }
};

CGAttribute.prototype.serialize = function () {
  var sContent = "<attribute code=\"" + this.code + "\" order=\"" + this.iOrder + "\">";
  sContent += this.AttributeList.serialize();
  sContent += this.IndicatorList.serialize();
  sContent += "</attribute>";
  return sContent;
};

CGAttribute.prototype.serializeWithData = function (sData) {
  var sContent = "<attribute code=\"" + this.code + "\" order=\"" + this.iOrder + "\">";
  sContent += sData;
  sContent += "</attribute>";
  return sContent;
};