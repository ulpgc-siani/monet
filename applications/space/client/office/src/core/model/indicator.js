function CGIndicator() {
  this.code = "";
  this.iOrder = -1;
  this.sValue = "";
};

CGIndicator.CODE = "code";
CGIndicator.CODE_OTHER = "otro";
CGIndicator.DETAILS = "details";
CGIndicator.VALUE = "value";
CGIndicator.SUPER = "super";
CGIndicator.OTHER = "other";
CGIndicator.EXTENDED = "extended";
CGIndicator.CONDITIONED = "conditioned";
CGIndicator.METRIC = "metric";
CGIndicator.FORMAT = "format";
CGIndicator.NODE_LINK = "nodelink";
CGIndicator.CHECKED = "checked";
CGIndicator.INTERNAL = "internal";
CGIndicator.RESULTTYPE = "resulttype";
CGIndicator.DESCRIPTOR = "descriptor";
CGIndicator.CAPTION = "caption";
CGIndicator.FULLCAPTION = "fullcaption";
CGIndicator.NODE = "node";
CGIndicator.STATE = "state";
CGIndicator.AUTHOR = "author";
CGIndicator.DATE = "date";
CGIndicator.FACT = "fact";
CGIndicator.FACTS_COUNT = "factcount";
CGIndicator.SOURCE = "source";
CGIndicator.FROM = "from";

CGIndicator.prototype.unserialize = function (sData) {
  this.unserializeFromXmlDocument(parseXml(sData).childNodes[0]);
};

CGIndicator.prototype.getValue = function () {
  return this.sValue;
};

CGIndicator.prototype.setValue = function (sValue) {
  this.sValue = (sValue) ? sValue : "";
};

CGIndicator.prototype.unserializeFromXmlDocument = function (XmlIndicator) {
  this.code = XmlIndicator.getAttribute("code");
  this.iOrder = XmlIndicator.getAttribute("order");
  this.sValue = (XmlIndicator.firstChild) ? XmlIndicator.firstChild.data : "";
};

CGIndicator.prototype.serialize = function () {
  var sContent = "<indicator code=\"" + this.code + "\" order=\"" + this.iOrder + "\">";
  sContent += HtmlUtil.encode(this.sValue);
  sContent += "</indicator>";
  return sContent;
};