function Dialog() {
}

//-----------------------------------------------------------
Dialog.prototype.show = function() {
  this.$layer.show();	
};

//-----------------------------------------------------------
Dialog.prototype.hide = function() {
  this.$layer.hide();	
};

//-----------------------------------------------------------
Dialog.prototype.setTitle = function(title) {
  this.extTitle.dom.innerHTML = (title)? title : "";	
};

//-----------------------------------------------------------
Dialog.prototype.setMessage = function(message) {
  var body = (message)? "<p>" + message + "</p>" : "";	
  this.extBody.dom.innerHTML = (body)? body : "";	  	
};

//-----------------------------------------------------------
Dialog.prototype.setBody = function(body) {
  this.extBody.dom.innerHTML = (body)? body : "";  	
};

//-----------------------------------------------------------
Dialog.prototype.bind = function() {
  this.extTitle = Ext.get(this.layername).select('.title').first();
  this.extBody = Ext.get(this.layername).select('.body').first();	  
};
