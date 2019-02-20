var Pooler = EventsSource.extend({
  init : function(config) {
    this._super();
    this.config   = config;
    this.poolTime = this.config.poolTime || 1000;
    this.refreshTime = 1000;
    this.poolInterval = null;
    this.state = 'stopped';
    
    this.startTime = 0;
    this.completeTime = 0;
  }
});  
  
Pooler.ON_REQUEST  = 'on_request';
Pooler.ON_COMPLETE = 'on_complete';

//-----------------------------------------------------
Pooler.prototype.start = function(callback) {
  this.state = 'running';
  var context = this;
  
  var request = new Request({
    url    : this.config.url,
    params : this.config.params,
    method : 'get',

    callback : {
      context : this,
      success : function() { 
        context.startTime = new Date().getTime();
        context.fire({name : Pooler.ON_COMPLETE});
        
        var response = arguments[0];
        if (this.config.serializer) reponse = this.config.serializer.unserialize(arguments[0]);                    
        callback.success.call(callback.context, response); 
      },
        
      failure: function()  {
        context.stop();
        context.fire({name : Pooler.ON_COMPLETE});      
        callback.failure.apply(callback.context, arguments);       
      }
    }
  });
    
  this.poolInterval = window.setInterval(function() {
    context.completeTime = new Date().getTime();
    if (context.completeTime - context.startTime > context.refreshTime) context.fire({name : Pooler.ON_REQUEST});
    setTimeout(function() {context.fire({name : Pooler.ON_REQUEST}); }, context.refreshTime);
    request.send();
  }, this.poolTime);  
};

//-----------------------------------------------------
Pooler.prototype.stop = function(callback) {
  window.clearInterval(this.poolInterval);  
  this.state = 'stopped';
};

//-----------------------------------------------------
Pooler.prototype.isRunning = function() {
  return this.state = 'running';
};