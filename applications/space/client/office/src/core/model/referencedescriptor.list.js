CGDescriptorList = function () {
  this.aItems = new Array();
  this.aIndexes = new Array();
  this.sDefault = "";
};

CGDescriptorList.prototype.get = function () {
  return this.aItems;
};

CGDescriptorList.prototype.add = function (Descriptor) {
  this.aItems.push(Descriptor);
  this.aIndexes[Descriptor.sName] = this.aItems.length - 1;
};

CGDescriptorList.prototype.setDefault = function (sDescriptor) {
  this.sDefault = sDescriptor;
};

CGDescriptorList.prototype.getDefault = function () {
  if (this.aItems[this.aIndexes[this.sDefault]] == null) return false;
  return this.aItems[this.aIndexes[this.sDefault]];
};