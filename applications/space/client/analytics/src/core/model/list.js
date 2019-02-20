function List() {
  this.items = new Array();
  this.positions = new Array();
}

List.prototype.clear = function() {
  this.items = new Array();
  this.positions = new Array();
};

List.prototype.size = function() {
  return this.items.length;
};

List.prototype.getAll = function() {
  return this.items;
};

List.prototype.getKeys = function() {
  var result = new Array();
  for (var i=0; i<this.items.length; i++) {
    var item = this.items[i];
    result.push(item.id);
  }
  return result;
};

List.prototype.get = function(id, value) {
  return this.items[this.positions[id + value]];
};

List.prototype.getItemKey = function(item) {
  return item.id + (item.value!=null?item.value:"");
}

List.prototype.addItem = function(item) {
  this.items.push(item);
  this.positions[this.getItemKey(item)] = this.items.length-1;
};

List.prototype.regeneratePositions = function() {
  this.positions = new Array();
  for (var i=0; i<this.items.length; i++) {
    var item = this.items[i];
    this.positions[this.getItemKey(item)] = i;
  }
};

List.prototype.deleteItem = function(id, value) {
  this.items.splice(this.positions[id + value],1);
  this.regeneratePositions();
};

List.prototype.clone = function(classAction) {
  var list = new classAction;
  for (var i=0; i<this.items.length; i++)
    list.addItem(this.items[i]);
  return list;
};

List.prototype.toJson = function() {
  var rows = "";
  
  for (var i=0; i<this.items.length; i++) {
    if (i != 0) rows += ",";
    rows += this.items[i].toJson();
  }
  
  return "{\"rows\":[" + rows + "],\"nrows\":\"" + this.items.length + "\"}";
};

List.prototype.fromJson = function(jsonObject) {
};