
function InputFile(layername, uploadHandler) {
  var html = AppTemplate.InputFile;
  this.$layer = $(layername);
  this.$layer.innerHTML = translate(html, Lang.InputFile);
  this._bind();
  this._show();
  
  this.uploadHandler = uploadHandler;
}

//-------------------------------------------------------------------------------------------------------
InputFile.prototype._bind = function(layername) {
  this.extForm     = Ext.get(layername).select(".form").first();
  this.extFile     = Ext.get(layername).select(".file").first();
  this.extFakeFile = Ext.get(layername).select("input.fakefile").first();
  this.extUploadButton = Ext.get(layername).select('upload').first();
  
  this.extFile.on('change', this._copyFilename, this);
  this.extUploadButton('click', this._onUpload, this);
}

//-------------------------------------------------------------------------------------------------------
InputFile.prototype._show = function() {
  this.$layer.show();
}

//-------------------------------------------------------------------------------------------------------
InputFile.prototype._onUpload = function(event, target, options) {
  if (this.uploadHandler) {
	this.extForm.dom.submit();  
  }	    
}

//-------------------------------------------------------------------------------------------------------
InputFile.prototype._copyFilename = function(event, target, options) {
  this.extFakeFile.dom.value = target.value;   	
}

