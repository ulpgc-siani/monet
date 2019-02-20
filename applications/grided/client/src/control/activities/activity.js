var Activity = Listener.extend({
	init : function() {
		
	}
});

Activity.prototype.start = function(data) {
  throw new Error("Activity start method has to be overrided");
};

Activity.prototype.stop = function() {
  throw new Error("Activity stop metohd has to be overrided");
};

Activity.prototype.canStop = function(callback) {
  throw new Error("Activity stop method has to be overrided");
};

