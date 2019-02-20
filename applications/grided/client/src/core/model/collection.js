
var Collection = Model.extend({
	init : function() {
	  this._super();
	  this.models = {};
	}
});

//-------------------------------------------------------------------------
Collection.prototype.add = function(model, options) {
  this.models[model.id] = model;
  if (! options || !options.silent) this.fire({name: Events.ADDED, data: {model: this, collection: this, item: model}});
};

//-------------------------------------------------------------------------
Collection.prototype.remove = function(ids, options) {
  if (_.isArray(ids)) {
    for (var i=0; i < ids.length; i++) {
      var id = ids[i];	
	  delete this.models[id];	  
    }
  }	  	
  if (! options || !options.silent) this.fire({name: Events.REMOVED, data: {model : this, collection: this.models}}); 
};

//-------------------------------------------------------------------------
Collection.prototype.getById = function(id) {
  return this.models[id];	
};

//-------------------------------------------------------------------------
Collection.prototype.get = function(index) {
  var array = this.toArray();
  return array[index];
};

//-------------------------------------------------------------------------
Collection.prototype.include = function(federation) {
  return _(this.toArray()).include(federation);
};

//-------------------------------------------------------------------------
Collection.prototype.size = function() {
  if (Object.keys) return Object.keys(this.models).length;
  var count = 0;
  for (var i in this.models) {
    var model = this.models[i];
    if (this.models.hasOwnProperty(model)) count++;
  }
  return count;
};

//-------------------------------------------------------------------------
Collection.prototype.each = function(callback, scope) {
  var size = this.size();	
  for (var i=0; i < size; i++) {
	var item = this.get(i);  
    callback.apply(scope, [i, item]);	  
  }	
};

//-------------------------------------------------------------------------
Collection.prototype.clear = function() {
  this.models = {};
};

//-------------------------------------------------------------------------
Collection.prototype.toArray = function() {
  var array = [];
  for (var i in this.models) {
    var model = this.models[i];
    array.push(model);
  }
  return array;	
};