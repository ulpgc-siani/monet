var Listener = Class.extend({
	init : function() {
		
	}
});

Listener.prototype.notify = function(event) {
  throw new Error('notify must be override');  
};


