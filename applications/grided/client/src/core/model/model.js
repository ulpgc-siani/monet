var Model = EventsSource.extend({
	init : function() {
	  this._super(arguments);
	  this.id = '';
	  this.validations = {};
	  this.error = null;
	} 
});

//---------------------------------------------
Model.prototype.get = function(propertyName) {
  return this[propertyName];	
};

//---------------------------------------------
Model.prototype.set = function(propertyName, value) {
 var oldValue = this[propertyName];  
 if (! this.validate(propertyName, value)) return false;
 
 this[propertyName] = value;

 this.fire({name: Events.CHANGED, propertyName: propertyName, data: {oldValue: oldValue, newValue: value, model: this}});
 return true;
};

//---------------------------------------------
Model.prototype.add = function(collectionName, value) {
  if (! this[collectionName]) this[collectionName] = [];
  this[collectionName].push(value);
  
  this.fire({name: Events.ADDED, collectionName: collectionName, value:value});   
};

//---------------------------------------------
Model.prototype.remove = function(collectionName, value) {
  if (! this[collectionName]) return false;
  var index = this[collectionName].indexOf(value);
  if (index == -1) return false;
  var result = this[collectionName].splice(index, 1);
  if (result.length <= 0) return false;

  this.fire({name: Events.REMOVED, collectionName: collectionName, value:value});  
  return true;
};

//---------------------------------------------
Model.prototype.validate = function(propertyName, value) {  
  var validator = this.validations[propertyName];
  if (!validator) return true;
  
  if (! validator.validate(propertyName, value)) {
  	this.error = validator.getError();
  	return false;
  } 
  return true;
};