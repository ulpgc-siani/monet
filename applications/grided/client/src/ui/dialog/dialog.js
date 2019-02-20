var Dialog = EventsSource.extend({
  init : function(element) {
	this._super();
	this.$el = $(element);	  
  }
});

//-----------------------------------------------------
Dialog.prototype.open = function() {
  this.$el.show();	
};

//-----------------------------------------------------
Dialog.prototype.close = function() {
 this.$el.hide();  
};

//-----------------------------------------------------
Dialog.prototype.getData = function() {
  throw new Error('this method has to be overrided');
};

//-----------------------------------------------------
Dialog.prototype.setFocus = function() {
  throw new Error('this method has to be overrided');
};
