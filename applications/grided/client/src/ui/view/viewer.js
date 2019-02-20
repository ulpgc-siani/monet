var Viewer = Listener.extend({
  init : function() {
    this.super();
    this.element = null;    
  }	
});

//------------------------------------------------------------------------
Viewer.prototype.setPresenter = function(presenter) {
	throw new Error('View setPresenter method has to be overrided');
};

//------------------------------------------------------------------------
Viewer.prototype.setElement = function(element) {
  this.element = element;
  this.$element = $(element);
};

//------------------------------------------------------------------------
Viewer.prototype.show = function() {
  if (this.$element)	this.$element.show(); 
};

//------------------------------------------------------------------------
Viewer.prototype.hide = function() {
  if (this.$element) this.$element.hide();	
};

//------------------------------------------------------------------------
Viewer.prototype.merge = function(template, context) {
	return _.template(template, context);
};
