function CGField() {
  this.sContent = null;
};

//---------------------------------------------------------------------
CGField.prototype.getContent = function () {
  return this.sContent;
};

//---------------------------------------------------------------------
CGField.prototype.setContent = function (sContent) {
  this.sContent = sContent;
  this.bLoaded = true;
};

//---------------------------------------------------------------------
CGField.prototype.unserializeFromJSON = function (ItemStructure) {
  this.sContent = ItemStructure.content;
};

//---------------------------------------------------------------------
CGField.prototype.unserialize = function (serialized) {
  var jsonData = Ext.util.JSON.decode(serialized);
  this.unserializeFromJSON(jsonData);
};