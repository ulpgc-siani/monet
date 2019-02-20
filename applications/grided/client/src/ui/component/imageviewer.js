/*
 * ImageModel {caption:'', src=''} 
 */

function ImageViewer(layername) {
  this.layername = layername;
  var html = AppTemplate.ImageViewer;
  this.$layer = $(layername);
  this.$layer.innerHTML = translate(html, Lang.ImageViewer);
  
  this.imageModel = null;
  this.image = null;
  
  this.bind();
  this.hide();
};

//------------------------------------------------------------------------------------
ImageViewer.prototype.setImageModel = function(imageModel) {
  this.imageModel = imageModel;
  this.bindData(imageModel);
};

//------------------------------------------------------------------------------------
ImageViewer.prototype.hasImageModel = function() {
  return this.imageModel != null;	
};

//------------------------------------------------------------------------------------
ImageViewer.prototype.show = function() {
  this.$layer.show();
  this.extBody.on('click', this._closeViewportHandler, this);
};

//------------------------------------------------------------------------------------
ImageViewer.prototype.hide = function() {
  this.$layer.hide();
  this.extBody.un('click', this._closeViewportHandler);  
};

//------------------------------------------------------------------------------------
ImageViewer.prototype.bind = function() {
  this.extParent = Ext.get(this.layername);	
  this.extViewPort    = this.extParent.select('.viewport').first();
  this.extCloseButton = Ext.get(this.extParent.query('input[name="close-button"]').first());
  
  this.extBody = Ext.get(document.getElementsByTagName('body')[0]);  
  this.extCloseButton.on('click', this._closeHandler, this);  
};

//------------------------------------------------------------------------------------
ImageViewer.prototype.bindData = function(imageModel) {
  this.image = new Image();    
  Event.observe(this.image, 'load', this._loadHandler.bind(this), false); 
  this.image.src = imageModel.src;    
};

//------------------------------------------------------------------------------------
ImageViewer.prototype._loadHandler = function(event) {
  var image = event.target;	
  this.extParent.dom.style.width = image.width;
  this.extParent.dom.style.height = image.height;  
  this.extViewPort.dom.appendChild(image);
};

//------------------------------------------------------------------------------------
ImageViewer.prototype._closeHandler = function(event, target, options) {  	
  this.hide();
  Event.stopObserving(this.image, 'load', this._loadHandler.bind(this), false);  
};

//--------------------------------------------------------------------------------
ImageViewer.prototype._closeViewportHandler = function(event, target, options) {
  event.stopPropagation();	
  if (! this.extParent.contains(target)) this.hide();    	 
};
