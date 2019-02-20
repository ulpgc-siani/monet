function CGAttributeList() {
  this.aAttributes = new Array;
  this.aIndexAttributes = new Array();
};

CGAttributeList.prototype.getAttribute = function (code) {
  var aIndexes = this.aIndexAttributes[code];
  if ((aIndexes == null) || (aIndexes.length <= 0)) return null;
  return this.aAttributes[aIndexes[0]];
};

CGAttributeList.prototype.getAttributes = function () {
  return this.aAttributes;
};

CGAttributeList.prototype.clear = function () {
  this.aAttributes = new Array();
  this.aIndexAttributes = new Array();
};

CGAttributeList.prototype.createAttribute = function (code) {
  var Attribute = new CGAttribute();
  Attribute.code = code;
  this.addAttribute(Attribute);
  return Attribute;
};

CGAttributeList.prototype.addAttribute = function (Attribute) {
  if (!this.aIndexAttributes[Attribute.code]) this.aIndexAttributes[Attribute.code] = new Array();
  var iPos = this.aAttributes.size();
  this.aAttributes[iPos] = Attribute;
  this.aIndexAttributes[Attribute.code].push(this.aAttributes.length - 1);
};

CGAttributeList.prototype.deleteAttribute = function (iPos) {
  var Attribute = this.aAttributes[iPos];
  var aIndexAttributes = new Array();
  var aAttributes = new Array();

  if (Attribute == null) return;

  for (var i = 0; i < this.aAttributes.length; i++) {
    if (iPos == i) continue;
    if (!aIndexAttributes[this.aAttributes[i].code]) aIndexAttributes[this.aAttributes[i].code] = new Array();
    var iNewPos = aAttributes.size();
    aAttributes[iNewPos] = this.aAttributes[i];
    aIndexAttributes[this.aAttributes[i].code].push(aAttributes.length - 1);
  }

  this.aIndexAttributes = aIndexAttributes;
  this.aAttributes = aAttributes;
};

CGAttributeList.prototype.getCount = function () {
  return this.aAttributes.length;
};

CGAttributeList.prototype.copyField = function (code, sValue) {
  var aParents;

  aParents = code.split(FIELD_CODE_SEPARATOR);
  if (aParents.length == 0) return;

  Attribute = this.getAttribute(aParents[0]);
  if (Attribute == null) Attribute = this.createAttribute(aParents[0]);

  if (aParents.length == 1) Attribute.addIndicatorByValue(CGIndicator.VALUE, -1, sValue);
  else {
    aParents = aParents.slice(1);
    code = aParents.join(FIELD_CODE_SEPARATOR);
    Attribute.AttributeList.copyField(code, sValue);
  }
};

CGAttributeList.prototype.copyFromFields = function (aFields) {
  for (var code in aFields) {
    if (isFunction(aFields[code])) continue;
    this.copyField(code, aFields[code]);
  }
};

CGAttributeList.prototype.unserialize = function (sData) {
  this.unserializeFromXmlDocument(parseXml(sData).childNodes[0]);
};

CGAttributeList.prototype.unserializeFromXmlDocument = function (XmlAttributeList) {
  var aChildren = XmlAttributeList.childNodes;

  if (aChildren == null) aChildren = XmlAttributeList.children;
  if (aChildren == null) return;

  this.clear();
  for (var iPos = 0; iPos < aChildren.length; iPos++) {
    if (aChildren[iPos].tagName.toLowerCase() == "attribute") {
      var Attribute = new CGAttribute();
      Attribute.unserializeFromXmlDocument(aChildren[iPos]);
      this.addAttribute(Attribute);
    }
  }
};

CGAttributeList.prototype.serialize = function () {
  var sAttributes = "";

  for (var iPos = 0; iPos < this.aAttributes.length; iPos++) {
    if (this.aAttributes[iPos].iOrder == -1) this.aAttributes[iPos].iOrder = iPos;
    sAttributes += this.aAttributes[iPos].serialize();
  }

  if (sAttributes == "") return "";

  return "<attributelist>" + sAttributes + "</attributelist>";
};

//---------------------------------------------------------------------
CGAttributeList.prototype.serializeWithData = function (sData) {
  return "<attributelist>" + sData + "</attributelist>";
};