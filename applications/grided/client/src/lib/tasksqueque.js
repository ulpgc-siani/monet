//---------------------------------------------------------------------
//
//TasksQueQue
//---------------------------------------------------------------------

var TasksQueQue = EventsSource.extend({
  init : function() {
    this._super();
    this._tasks = [];
    this._results = [];
    this.iter = 0;
  }
});

TasksQueQue.COMPLETE_EVENT = 'complete_event';

//---------------------------------------------------------------------
TasksQueQue.prototype.add = function(fn, scope) {
  this._tasks.push(new Task(fn, scope));
  return this;
};

//---------------------------------------------------------------------
TasksQueQue.prototype.clear = function() {
  this._tasks = [];
  this._results = [];
  return this;
};

//---------------------------------------------------------------------
TasksQueQue.prototype.execute = function() {  
  var task = this._tasks[this.iter++];    
  if (!task) return ;
  
  task.on(Task.FINISH_EVENT, {notify : function(event) { 
    var result = event.data.result;
    this._results.push(result);
    
    if (this._tasks.length == this._results.length) this._fireCompleteEvent();
    else this.execute();
  }}, this);
    
  task.execute(); 
};

//---------------------------------------------------------------------
TasksQueQue.prototype._fireCompleteEvent = function() {
  var event =  {name : TasksQueQue.COMPLETE_EVENT, data : {results : this._results}};
  this.fire(event);
};


//---------------------------------------------------------------------
//
//Task
//---------------------------------------------------------------------

var Task = EventsSource.extend({
  init : function(fn, scope) {
    this._super();
    this._fn = fn;    
    this._scope = scope;
  }
});

Task.FINISH_EVENT = 'finish_event';

//---------------------------------------------------------------------
Task.prototype.execute = function() {
  var self = this;
  var callback = {};
  
  callback.end = function(result) {
    var event =  {name : Task.FINISH_EVENT, data : {result : result}};
   self.fire(event);    
  };

  this._fn.call(this._scope, callback);          
};
