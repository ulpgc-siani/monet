EventBus = {
  listeners : {},
  
  on : function(eventName, handler, scope) {
    if (!this.listeners[eventName]) this.listeners[eventName] = [];        
	   this.listeners[eventName].push({scope: scope, fn: handler});
  }, 

  un : function(eventName, handler) {
   if (! this.listeners[eventName]) return;
	    
   if (handler) {     
     for (var i=0; i < this.listeners[eventName].length; i++) {
	   var listener = this.listeners[eventName][i];
	   if (listener.fn === handler) this.listeners[eventName].splice(i, 1);
	 }
   }
   else { this.listeners[eventName] = []; delete this.listeners[eventName]; }
  },

  fire : function(event) {
    if (! this.listeners[event.name]) return;

	for (var i=0; i < this.listeners[event.name].length; i++) {
	  var listener = this.listeners[event.name][i];
	  listener.fn.call(listener.scope, event);
    }
  }
};

