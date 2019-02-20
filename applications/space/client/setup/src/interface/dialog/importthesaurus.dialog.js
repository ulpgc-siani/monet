function DialogImportThesaurus(layername) {
  this.html = AppTemplate.DialogImportThesaurus;
  this.layername = layername;
  this.$layer = $(this.layername);
  this.$layer.innerHTML = "";
  this.width = '';
  this.heigth = '';
  this.resultHandler = {handler: '', scope: ''};
  
  this.IMPORT_BUTTON = Lang.Dialog.ImportThesaurus.import;
  this.CANCEL_BUTTON = Lang.Dialog.ImportThesaurus.cancel;
  
  this.bind();
}

//------------------------------------------------------------------
DialogImportThesaurus.prototype.setTitle = function(title) {
  this.extDialog.header.dom.innerHTML = (title)? title : "";	
};

//------------------------------------------------------------------
DialogImportThesaurus.prototype.setMessage = function(message) {
  this.extMessage.dom.innerHTML = (message)? message : "";	
};

//------------------------------------------------------------------
DialogImportThesaurus.prototype.setThesaurusList = function(list) {  
  for (var i=0; i < list.length; i++) {
	var thesaurus = list[i];  
    this.extThesaurusCb.dom.innerHTML += '<option value="' + thesaurus.code + '">' + thesaurus.label + '</option>';  	  
  }  
};

//------------------------------------------------------------------
DialogImportThesaurus.prototype.setResultHandler = function(handler, scope) {
  this.resultHandler.handler = handler;
  this.resultHandler.scope = scope;
};

//------------------------------------------------------------------
DialogImportThesaurus.prototype.addButton = function(text, handler, scope) {
  this.extDialog.addButton(text, handler, scope);
};

//------------------------------------------------------------------
DialogImportThesaurus.prototype.show = function() {
  this.extDialog.show();	
};

//------------------------------------------------------------------
DialogImportThesaurus.prototype.hide = function() {
  this.extDialog.hide();	
};

//------------------------------------------------------------------
DialogImportThesaurus.prototype.check = function() {
  if (this.extFile.dom.value == "") {
	var message = Lang.Dialog.ImportThesaurus.file_required;
	Ext.MessageBox.alert(Lang.Exceptions.Title, message);
	return false;
  }
  return true;	
};

//------------------------------------------------------------------
DialogImportThesaurus.prototype.submit = function() {
  this.form.action = Context.Config.Api;
  addInput(this.form, "op", "importthesaurus");
  
  var successHandler = function(response, options) {
	if (response.responseText.indexOf("ERR_") != -1) {failureHandler.apply(this, [response, options]); return; } 
	this.resultHandler.handler.onSuccess.apply(this.resultHandler.scope, [response, options]);      	  
  };
  
  var failureHandler = function(response, options) {
	this.hide();
    this.resultHandler.handler.onFailure.apply(this.resultHandler.scope, [response, options]);
  };
		 
  var elSelect = this.form.child('.thesaurus').dom;
  var code = elSelect.options[elSelect.selectedIndex].value;
  
  Ext.Ajax.request({
	url: Context.Config.Api + "?op=importthesaurus" + "&code=" + code,  
	method: 'post',	
	success : successHandler.bind(this),
	failure : failureHandler.bind(this),
	form : this.form,
	isUpload : true,
	scope: this
 });	  	
};

//-----------------------------------------------------------------
DialogImportThesaurus.prototype.bind = function() {      
  this.extDialog = this._createDialog(this.layername);  
  this.extDialog.body.dom.innerHTML = translate(this.html, Lang.Dialog.ImportThesaurus);   
  
  this.extMessage       = this.extDialog.body.select('.message').first();
  this.extThesaurusCb   = this.extDialog.body.select('.thesaurus').first();
  this.form             = Ext.get('importthesaurus');  
  this.extImportButton  = this.form.select('.button').first();
  this.extFile          = this.form.select('.file').first();
};

//-----------------------------------------------------------------
DialogImportThesaurus.prototype._createDialog = function(layername) {
  var dialog = new Ext.BasicDialog(layername, {
    width: 493,
	height: 236,
	minwidth: 490,
	minheight: 236,
	modal : true,
	shadow: false		    
  });
    
  return dialog;
};