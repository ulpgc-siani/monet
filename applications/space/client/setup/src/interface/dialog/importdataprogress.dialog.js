function DialogImportDataProgress(layername) {
  this.layername = layername;
  this.$layer = $(layername);
  var html = AppTemplate.DialogImportDataProgress;
  this.$layer.innerHTML = translate(html, Lang.Dialog.ImportDataProgress);
   
  this.bind();
}

//------------------------------------------------------------------
DialogImportDataProgress.prototype.setTitle = function(title) {
  this.extTitle.dom.innerHTML = (title)? title : "";
};

//------------------------------------------------------------------
DialogImportDataProgress.prototype.setMessage = function(message) {
  this.extMessage.dom.innerHTML = (message)? message : "";	
};

//------------------------------------------------------------------
DialogImportDataProgress.prototype.setProgress = function(progress) {  	
  this.extNodes.dom.innerHTML = progress.currentCount;
  this.extProgressValue.dom.innerHTML = progress.value + ' %';
  
  var time = new Time(progress.time);
  var tuple = time.format();
  this.extTime.dom.innerHTML = this._formatDuration(tuple);
  
  var estimatedTime = new Time(progress.estimatedTime);
  var tuple = estimatedTime.format();
  this.extEstimatedTime.dom.innerHTML = this._formatDuration(tuple);    
};

//------------------------------------------------------------------
DialogImportDataProgress.prototype.show = function() {  
  this.$layer.show();	
};

//------------------------------------------------------------------
DialogImportDataProgress.prototype.hide = function() {  
  this.$layer.hide();  
};

//------------------------------------------------------------------
DialogImportDataProgress.prototype.setAbortProgressHandler = function(scope, handler) {    
  this.extAbortButton.on('click', handler, scope);
};

//------------------------------------------------------------------
DialogImportDataProgress.prototype.clear = function() {
  this.extProgressValue.dom.innerHTML = '';
  this.extNodes.dom.innerHTML = '';
  this.extTime.dom.innerHTML = '';
  this.extEstimatedTime.dom.innerHTML = '';    		  	
};

//------------------------------------------------------------------
DialogImportDataProgress.prototype.bind = function() {
  this.extParent = Ext.get(this.layername);
  this.extTitle = this.extParent.select('.title').first();
  this.extMessage = this.extParent.select('.message').first();
  
  this.extProgressValue = this.extParent.select('.value').first();
  this.extNodes = this.extParent.select('.imported-nodes').first();
  this.extTime  = this.extParent.select('.time').first();
  this.extEstimatedTime = this.extParent.select('.estimated-time').first();  
  this.extAbortButton = Ext.get(this.extParent.query('input[name="abort-button"]').first());
};


//------------------------------------------------------------------
DialogImportDataProgress.prototype._formatDuration = function(tuple) {
  var msg = '';	    
  msg += (tuple.hours)? ((tuple.hours == 1)? tuple.hours + ' ' + Lang.HOUR + ' ' : tuple.hours + ' ' + Lang.HOURS + ', ') : '';
  msg += (tuple.minutes)? ((tuple.minutes == 1)? tuple.minutes + ' ' + Lang.MINUTE + ' y ' : tuple.minutes + ' ' + Lang.MINUTES + ' y ') : '';
  msg += (tuple.seconds)? ((tuple.seconds == 1)? tuple.seconds + ' ' + Lang.SECOND: tuple.seconds + ' ' + Lang.SECONDS) : '';
  
  return msg;
};
