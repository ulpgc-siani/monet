var Spaces = Collection.extend({
  init : function() {
	this._super();
  }
});

////-------------------------------------------------------------------
//Spaces.prototype.addSpace = function(server, options) {
//  this.add(server, {silent : true});
//  if (! options || !options.silent) this.fire({name: Events.ADDED, data: {model : this}});
//};
//
////-------------------------------------------------------------------
//Spaces.prototype.removeSpaces = function(ids, options) {
//  this.remove(ids, {silent: true});
//  if (! options || !options.silent) this.fire({name: Events.REMOVED, data: {model : this}});
//};