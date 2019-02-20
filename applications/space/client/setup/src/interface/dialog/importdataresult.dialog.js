function DialogImportDataResult(layername) {
  this.layername = layername; 
  this.$layer = $(layername);  
  var html = AppTemplate.DialogImportDataResult;
  this.$layer.innerHTML = translate(html, Lang.Dialog.ImportDataResult);
  
  this.bind();
}

//------------------------------------------------------------------
DialogImportDataResult.prototype.setTitle = function(title) {
  this.extTitle.dom.innerHTML = (title)? title : "";
};

//------------------------------------------------------------------
DialogImportDataResult.prototype.setMessage = function(message) {
  this.extMessage.dom.innerHTML = (message)? message : "";
};

//------------------------------------------------------------------
DialogImportDataResult.prototype.setSummary = function(summary) {
  var extCount = Ext.get(this.extSummary.query('span[name="imported_nodes"]').first());
  var extTime  = Ext.get(this.extSummary.query('span[name="time"]').first());
	
  $(this.extError.dom).hide();
  extCount.dom.innerHTML = summary.count;
  extTime.dom.innerHTML = summary.time;
  $(this.extSummary.dom).show();
};

//------------------------------------------------------------------
DialogImportDataResult.prototype.setError = function(error) {  
  var extLabel = this.extError.down('.label');
  var extDescription = this.extError.down('.description');
  
  extLabel.dom.innerHTML = error.label;
  extDescription.dom.innerHTML = '<pre>' + error.description + '</pre>'; 
  this.extError.show();
};

//------------------------------------------------------------------
DialogImportDataResult.prototype.show = function() {
  this.$layer.show();
};

//------------------------------------------------------------------
DialogImportDataResult.prototype.hide = function() {
  this.$layer.hide();
};

//------------------------------------------------------------------
DialogImportDataResult.prototype.isVisible = function() {
  return this.$layer.visible();   
};

//------------------------------------------------------------------
DialogImportDataResult.prototype.setCloseHandler = function(scope, handler) {
  this.extCloseButton.on('click', handler, scope);  
};

//------------------------------------------------------------------
DialogImportDataResult.prototype.bind = function() {
  this.extParent = Ext.get(this.layername);
  this.extTitle  = this.extParent.select('.title').first();  
  this.extMessage = this.extParent.select('.message').first();
  this.extError   = this.extParent.select('.error').first();
  this.extSummary = this.extParent.select('.summary').first();
  
  this.extCloseButton = this.extParent.select('.close-button').first();  
  
  $(this.extError.dom).hide();
  $(this.extSummary.dom).hide();
};

//------------------------------------------------------------------
DialogImportDataResult.prototype.clear = function() {
  var extLabel = this.extError.down('.label');
  var extDescription = this.extError.down('.description');
  
  extLabel.dom.innerHTML = "";
  extDescription.dom.innerHTML = "";
};
