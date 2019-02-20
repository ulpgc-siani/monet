var View = Listener.extend({
  init : function(id) {
	this._super(id);
	this.id = id;
	this.$id = $(id);
  }
});

//------------------------------------------------------------------------
View.prototype.setPresenter = function(presenter) {
	throw new Error('View setPresenter method has to be overrided');
};

//------------------------------------------------------------------------
View.prototype.show = function() {
  this.$id.show(); 
};

//------------------------------------------------------------------------
View.prototype.hide = function() {
  this.$id.hide();	
};

//------------------------------------------------------------------------
View.prototype.destroy = function() {
};

//------------------------------------------------------------------------
View.prototype.merge = function(template, context) {
	return _.template(template, context);
};

