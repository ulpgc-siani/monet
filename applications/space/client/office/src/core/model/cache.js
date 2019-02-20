CGCache = function () {
  this.aItems = new Array();
  this.IdCurrentItem = null;
};

CGCache.prototype.register = function (Item) {
  this.aItems[Item.getId()] = Item;
};

CGCache.prototype.unregister = function (IdItem) {
  if (!this.aItems[IdItem]) return true;
  delete this.aItems[IdItem];
};

CGCache.prototype.getCurrent = function () {
  return this.aItems[this.IdCurrentItem];
};

CGCache.prototype.setCurrent = function (IdItem) {
  this.IdCurrentItem = IdItem;
};

CGCache.prototype.exists = function (IdItem) {
  return (this.aItems[IdItem] != null);
};

CGCache.prototype.getCount = function () {
  return this.aItems.size();
};

CGCache.prototype.get = function (IdItem) {
  if (this.aItems[IdItem] == null) return false;
  return this.aItems[IdItem];
};

CGCache.prototype.getAll = function () {
  var aResult = new Array();

  for (var iPos in this.aItems) {
    if (isFunction(this.aItems[iPos])) continue;
    aResult.push(this.aItems[iPos]);
  }

  return aResult;
};