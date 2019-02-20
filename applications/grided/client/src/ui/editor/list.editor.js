var ListEditor = Editor.extend({
  
  init : function(id, element, object, propertyName, adapter) {
    this._super();
    this.id = id;
    this.element = element;
    this.object = object;
    this.propertyName = propertyName;
    this.adapter = adapter;
    
    this.oldArray = this._getItems();
    this.newArray = this._getItems();
  }
});

//----------------------------------------------------------------------------------
ListEditor.prototype._getItems = function(index) {
  var items = [];
  var children = this.element.children;
  for (var i=0, l = children.length; i < l; i++) {
    var li = children[i];
    var item = this.adapter.adapt(li);
    items.push(item);
  }
  return items;
};

//----------------------------------------------------------------------------------
ListEditor.prototype.open = function() {
  this.state = Editor.OPENED;
};

//----------------------------------------------------------------------------------
ListEditor.prototype.close = function() {  
  this.state = Editor.CLOSED;
};

//----------------------------------------------------------------------------------
ListEditor.prototype.isDirty = function() {
  this.newArray = this._getItems();
  return _.isEqual(this.oldArray, this.newArray) == false;    
};

//----------------------------------------------------------------------------------
ListEditor.prototype.flush = function() {
  var collection = this.object.get(this.propertyName);
  collection.clear();
  this.newArray = this._getItems();
  
  var l = this.newArray.length;
  for (var i=0; i < l; i++) {
    var item = this.newArray[i];
    collection.add(item);
  }
};

//----------------------------------------------------------------------------------
ListEditor.prototype.reset = function() {
  var collection = this.object.get(this.propertyName);
  collection.clear();

  var l = this.oldArray.length;
  for (var i=0; i < l; i++) {
    var item = this.oldArray[i];
    collection.add(item);
  }
};