
function DialogImportData(layername) {
  this.layername = layername;
  this.$layer = $(layername);
  var html = AppTemplate.DialogImportData;
  this.$layer.innerHTML = translate(html, Lang.Dialog.ImportData);
  
  this.bind(layername);
};

//------------------------------------------------------------------
DialogImportData.prototype.setTitle = function(title) {
  this.extTitle.dom.innerHTML = (title)? title : "";	
};

//------------------------------------------------------------------
DialogImportData.prototype.setMessage = function(message) {
  this.extMessage.dom.innerHTML = (message)? message : "";	
};

//------------------------------------------------------------------
DialogImportData.prototype._loadImporterDefinitions = function() {
  var url = Context.Config.Api + "?op=loadimporterdefinitions";
  new Ajax.Request(url, {
	method: 'get',
	  onSuccess: function(transport, json) {
	    var importerDefinitions = Ext.util.JSON.decode(transport.responseText);	 
		 if (importerDefinitions) {
		    this._setImporterDefinitions(importerDefinitions);
  	     }	   	   
	}.bind(this)
  });
};

//------------------------------------------------------------------
DialogImportData.prototype._setImporterDefinitions = function(importerDefinitions) {
  this.extImporterDefinitions.dom.innerHTML = "";
  var len = importerDefinitions.length;
  for (var i=0; i < len; i++) {
    var el = importerDefinitions[i];
    var option = '<option value="' + el.code + '">' + el.name + '</option>';
    this.extImporterDefinitions.dom.innerHTML += option;    
  }  
};

//------------------------------------------------------------------
DialogImportData.prototype.show = function() {
  this.$layer.show(); 	
};

//------------------------------------------------------------------
DialogImportData.prototype.hide = function() {
  this.$layer.hide();
};

//------------------------------------------------------------------
DialogImportData.prototype.enable = function() {
  var extInputs = Ext.get(this.layername).query('input, select');
  Ext.each(extInputs, function(item) {if (item.enable) item.enable();} );
};

//------------------------------------------------------------------
DialogImportData.prototype.disable = function() {
  var extInputs = Ext.get(this.layername).query('input, select');
  Ext.each(extInputs, function(item) {if (item.disable) item.disable();} );
};

//------------------------------------------------------------------
DialogImportData.prototype.setImportHandler = function(scope, handler) { 
  this.extImportButton.on('click', handler, scope);
};

//------------------------------------------------------------------
DialogImportData.prototype.bind = function(layername) {
  this.extTitle          = Ext.get(layername).select(".title").first();
  this.extMessage        = Ext.get(layername).select(".message").first();
  this.form              = Ext.get('importdata');  
  this.extImportButton   = this.form.select('.button').first();
  this.extFile           = this.form.select('.file').first();
  this.extUrl            = this.form.select('.url').first();
  this.extImporterDefinitions  = this.form.select('.importer-type').first();
  
  this.radio = this.form.select('.radio'); 
  this.radio.file = "0";
  this.radio.url = "1";
         
  this.radio.on('change', this._changeRadioHandler, this);  
  this._initRadio();
  this._loadImporterDefinitions();
};

//------------------------------------------------------------------
DialogImportData.prototype.check = function() {  
  switch (this.radio.selected) {
    case this.radio.file:
      if (this.extFile.dom.value == "") { Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Dialog.DataImport.file_required); return false; }
      else if (this.extFile.dom.value.indexOf(".xml") == -1) { Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Dialog.DataImport.xml_file_required); return false; }
      break;
    case this.radio.url:
      if (this.extUrl.dom.value == "") { Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Dialog.DataImport.url_required); return false; }	
  }
    
  return true;
};

//------------------------------------------------------------------
DialogImportData.prototype.submit = function() {
  if (! this.check()) return;
  
  var action = new CGActionStartImportData();
  action.SourceForm = this.form;
  action.execute();	
};

//------------------------------------------------------------------
DialogImportData.prototype._changeRadioHandler = function(event, target, options) {
  switch(event.target.value) {
    case this.radio.file: 
      this.extUrl.dom.disabled = true; 
      this.extImporterDefinitions.dom.disabled = false;
      this.extFile.dom.disabled = false;
      this.radio.elements[0].checked = true;
      this.extImporterDefinitions.focus();
      this.radio.selected = this.radio.file;
      break;

    case this.radio.url: 
      this.extFile.dom.disabled = true; 
      this.extImporterDefinitions.dom.disabled = true;
      this.extUrl.dom.disabled = false;
      this.radio.elements[1].checked = true;
      this.extUrl.focus();
      this.radio.selected = this.radio.url;
      break;
  };
};

//------------------------------------------------------------------
DialogImportData.prototype._initRadio = function() {
  var event = {};
  event.target = {};
  event.target.value = this.radio.file;
  this.radio.selected = this.radio.file;
  this._changeRadioHandler(event);  
};