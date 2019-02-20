function CGNodeReference(Data) {
  this.bIsDirty = false;
  this.aAttributes = new Array();
  for (var i = 0; i < Data.attributes.length; i++) {
    Item = Data.attributes[i];
    this.aAttributes[Item.code] = Item.value;
  }
};

/*********************************************************************/
/*  Methods                                                          */
/*********************************************************************/
CGNodeReference.prototype.isDirty = function () {
  return this.bIsDirty;
};

CGNodeReference.prototype.getLabel = function () {
  return this.aAttributes["label"];
};

CGNodeReference.prototype.setLabel = function (sValue) {
  return this.setAttributeValue("label", sValue);
};

CGNodeReference.prototype.getDescription = function () {
  return this.aAttributes["description"];
};

CGNodeReference.prototype.setDescription = function (sValue) {
  return this.setAttributeValue("description", sValue);
};

CGNodeReference.prototype.getDeleteDate = function () {
  return this.aAttributes["delete_date"];
};

CGNodeReference.prototype.getAttributes = function () {
  return this.aAttributes;
};

CGNodeReference.prototype.getAttributeValue = function (code) {
  return this.aAttributes[code];
};

CGNodeReference.prototype.setAttributeValue = function (code, sValue) {
  this.bIsDirty = true;
  this.aAttributes[code.toLowerCase()] = sValue;
};

CGNodeReference.prototype.serialize = function () {
  var sAttributes = "";

  for (var code in this.aAttributes) {
    if (isFunction(this.aAttributes[code])) continue;
    sAttributes += "{\"code\":\"" + code + "\",\"value\":\"" + this.aAttributes[code] + "\"},";
  }
  if (sAttributes.length > 0) sAttributes = sAttributes.substring(0, sAttributes.length - 1);

  return "{\"attributes\":[" + sAttributes + "]}";
};