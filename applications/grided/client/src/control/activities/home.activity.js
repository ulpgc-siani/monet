var HomeActivity = Activity.extend({
  init : function() {
	  
  }	
});

//---------------------------------------------------------------------------------
HomeActivity.prototype.start = function(data) {
	alert("TODO not implemented home activity");
};

//---------------------------------------------------------------------------------
HomeActivity.prototype.stop = function() {
	
};

//---------------------------------------------------------------------------------
HomeActivity.prototype.canStop = function(callback) {
	callback.success.call(callback.context, {});
};