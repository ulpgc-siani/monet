function ItemsAdapter() {
  this.items = [];	
};

//------------------------------------------------------------------------
ItemsAdapter.prototype.adapt = function(elements, handler) {
  for (var i=0; i < elements.length; i++) {	  
    var item = handler(elements[i]);
    this.items.push(item);
  }
  return this.items;
};

