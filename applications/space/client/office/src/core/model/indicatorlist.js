function CGIndicatorList() {
  this.aIndicators = new Array;
  this.aIndexIndicators = new Array();
};

CGIndicatorList.prototype.getIndicator = function (code) {
  if (this.aIndexIndicators[code] == null) return null;
  return this.aIndicators[this.aIndexIndicators[code]];
};

CGIndicatorList.prototype.getIndicators = function () {
  return this.aIndicators;
};

CGIndicatorList.prototype.clear = function () {
  this.aIndicators = new Array();
  this.aIndexIndicators = new Array();
};

CGIndicatorList.prototype.addIndicatorByValue = function (code, iOrder, sValue) {
  var Indicator = new CGIndicator();
  Indicator.code = code;
  Indicator.iOrder = iOrder;
  Indicator.setValue(sValue);
  this.addIndicator(Indicator);
};

CGIndicatorList.prototype.addIndicator = function (Indicator) {
  this.aIndicators.push(Indicator);
  this.aIndexIndicators[Indicator.code] = this.aIndicators.length - 1;
};

CGIndicatorList.prototype.unserialize = function (sData) {
  this.unserializeFromXmlDocument(parseXml(sData).childNodes[0]);
};

CGIndicatorList.prototype.unserializeFromXmlDocument = function (XmlIndicatorList) {
  var aChildren = XmlIndicatorList.childNodes;

  if (aChildren == null) aChildren = XmlIndicatorList.children;
  if (aChildren == null) return;

  this.clear();
  for (var iPos = 0; iPos < aChildren.length; iPos++) {
    if (aChildren[iPos].tagName.toLowerCase() == "indicator") {
      var Indicator = new CGIndicator();
      Indicator.unserializeFromXmlDocument(aChildren[iPos]);
      this.addIndicator(Indicator);
    }
  }
};

CGIndicatorList.prototype.serialize = function () {
  var sIndicators = "";
  var iPos = 0;

  for (var iPos = 0; iPos < this.aIndicators.length; iPos++) {
    if (this.aIndicators[iPos].iOrder == -1) this.aIndicators[iPos].iOrder = iPos;
    sIndicators += this.aIndicators[iPos].serialize();
  }

  if (sIndicators == "") return "";

  return "<indicatorlist>" + sIndicators + "</indicatorlist>";
};