
function Collection() {
  this.array = new Array();
}

Collection.prototype.add = function(el) {
  if (! this.contains(el))
    this.array.push(el);
};

Collection.prototype.remove = function(el) {
  var index = this.array.indexOf(el);
  if (index != -1) delete this.array[index];
};

Collection.prototype.removeByIndex = function(index) {  	
  if (index != -1) delete this.array[index];  	
};

Collection.prototype.contains = function(el) {
  return this.array.indexOf(el) != -1;
};

Collection.prototype.containsElementWithId = function(id) {
  for (var i=0, count=this.array.length; i < count; i++) {
    var el = this.array[i];
    if (el.id === id) return true;
  } 
  return false;
};

Collection.prototype.items = function() {
  return this.array;
};

Collection.prototype.size = function() {
  return this.array.length;
};