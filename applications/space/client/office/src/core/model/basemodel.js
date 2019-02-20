function CGBaseModel() {
  this.data = null;
  this.bIsDirty = false;
};

CGBaseModel.prototype.serialize = function () {
  if (this.data)
    return Ext.util.JSON.encode(this.data);
  else
    return null;
};

CGBaseModel.prototype.unserialize = function (serializedData) {
  this.data = Ext.util.JSON.decode(serializedData);
};

CGBaseModel.prototype.isLoaded = function () {
  return this.data != null;
};

CGBaseModel.prototype.isDirty = function () {
  return this.bIsDirty;
};