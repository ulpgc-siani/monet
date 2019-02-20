function Popup(layername) {
  this.layername = layername;
  var html = AppTemplate.Popup;
  this.$layer = $(this.layername);
  this.$layer.innerHTML = html;
  this.bind();
}

//--------------------------------------------------------------------
Popup.prototype.setMessage = function(message) {
  this.extMessage.dom.innerHTML = message;	
};

//--------------------------------------------------------------------
Popup.prototype.show = function() {
  this.$layer.show();
  this.extBody.on('click', this._closeHandler, this);
};

//--------------------------------------------------------------------
Popup.prototype.hide = function() {
  this.$layer.hide();
  this.extBody.un('click', this._closeHandler);
};

//--------------------------------------------------------------------
Popup.prototype.bind = function() {
  this.extParent = Ext.get(this.layername);	
  this.extMessage = this.extParent.select(".message").first();
  this.extBody = Ext.get(document.getElementsByTagName('body')[0]);  
};

//--------------------------------------------------------------------
Popup.prototype._closeHandler = function() {
  this.hide(); 	
};