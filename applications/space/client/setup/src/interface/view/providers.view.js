function ProvidersView(layername) {
  var html = AppTemplate.ViewProviders;	
  this.layername = layername;
  this.$layer = $(layername);
  this.$layer.innerHTML = translate(html, Lang.ViewProviders);
    
  this.tree = this._createTree('providers-tree');    
  this.grid = this._createGrid('providers-grid-panel', 'providers-grid');
  
  
  this.definitions = [];
  this.treeNodeAdapter = new ProviderDefinitionTreeNodeAdapter();
  this.recordAdapter   = new ProviderRecordAdapter(this.grid.getRecordClass());
  this.bind();
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype.show = function() {
  this.$layer.show();  	
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype.hide = function() {
  this.$layer.hide();	
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype.refresh = function() {
  this.grid.refresh();
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype.bind = function() {
  this.extMessageBox = Ext.get('providers-message-box');
  this.extMessage    = this.extMessageBox.select('.message').first();
  this.extList       = this.extMessageBox.select('.list').first();
  
  this.extMessage.dom.innerHTML = Lang.ViewProviders.definitions_not_instanced;    
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype._createTree = function(id) {
  var url = Context.Config.Api + "?op=loadproviderdefinitions";	
  var tree = new ProvidersTree(id, url);
  tree.on('select', this._treeSelectionHandler, this);
  tree.on('load', this._treeLoadHandler, this);  
  return tree;	
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype._createGrid = function(panelId, id) {  
  var grid = new ProvidersGrid(panelId, id);
    
  grid.on('delete', this._deleteRecordHandler, this);
  grid.on('aftereditrow', this._afterEditRecordHandler, this);
  grid.on('select', this._gridSelectionHandler, this); 
   
  return grid;
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype._treeSelectionHandler = function(treeNode) {	
  if (! treeNode.isLeaf()) {this.grid.addCommand.disable(); return;};
  var providerDefinition = this._getDefinitionByCode(treeNode.id);	
  
  if (providerDefinition) {
    var url = Context.Config.Api + "?op=loadserviceproviders" + "&code=" + providerDefinition.code;
    var proxy = new Ext.data.HttpProxy({url: url});	  
	this.grid.setProxy(proxy);
	this.grid.load();
  }
  else {
    this.grid.addCommand.disable();
  }
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype._treeLoadHandler = function(tree, treeNodes) {
  var newDefinitions = [];
  for (var i=0; i < treeNodes.length; i++) {
	var node = treeNodes[i];
	if (this._getDefinitionByCode(node.id)) continue;
	var definition = this.treeNodeAdapter.toProviderDefinition(node);
    if (definition) newDefinitions.push(definition); 	  
  }

  this.definitions = this.definitions.concat(newDefinitions);
  this._checkDefinitionInstances();
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype._gridSelectionHandler = function(providers) {  
  //if (providers.length > 0) this.grid.deleteCommand.enable();
  //else this.grid.deleteCommand.disable();
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype._afterEditRecordHandler = function(record) {
  var treeNodeSelected = this.tree.getSelection();  	
  var providerDefinition = this._getDefinitionByCode(treeNodeSelected.id);
  this._saveProvider(providerDefinition, record);    
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype._deleteRecordHandler = function(records) {
  var successHandler = function(response, request) {
	if (response.responseText.indexOf("ERR_") != -1 || response.responseText == "") {failureHandler.apply(this, [response, request]); return; }
	
	for (var i=0; i < records.length; i++) {		
	  this.grid.remove(records[i]);	    
	}
	
	var node = this.tree.getSelection();
	var providerDefinition = this._getDefinitionByCode(node.id); 
			
	if (this.grid.getItemsCount() == 0) { 
	  providerDefinition.isInstanced = false;
	  this._checkDefinitionInstances();
	}
  };
							 
  var failureHandler = function(response, request) {	    
    Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Action.DeleteProvider.Failure);
  };  
  
  var ids = "";
  for (var i=0; i < records.length; i++) {	
	var provider = this.recordAdapter.toProvider(records[i]);
	ids += provider.id;
	if (i != records.length) ids += ",";
  }
    
  Ext.Ajax.request({
	url: Context.Config.Api,
	method: 'post',
	params : {op: 'deleteserviceprovider', ids: ids},
	success : successHandler.bind(this),
	failure : failureHandler.bind(this),
 });			 	     
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype._saveProvider = function(providerDefinition, record) {
  var successHandler = function(response, request) {
	if (response.responseText.indexOf("ERR_") != -1 || response.responseText == "") {failureHandler.apply(this, [response, request]); return; }
	var serializer = new ProviderSerializer();
	var savedProvider = serializer.unserialize(response.responseText);
	record.data.id = savedProvider.id;	
    this.grid.commit();
    
    providerDefinition.isInstanced = true;
    this._checkDefinitionInstances();
  };
					 
  var failureHandler = function(response, request) {    
    Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Action.SaveProvider.Failure);
  };  
  
  var provider = this.recordAdapter.toProvider(record);
  var code = providerDefinition.code;
  provider.type = providerDefinition.type;
      
  Ext.Ajax.request({
	url: Context.Config.Api,
	method: 'post',
	params : {op: 'saveserviceprovider', id:provider.id, code:code, name:provider.name, label:provider.label, url:provider.url, type:provider.type},
	success : successHandler.bind(this),
	failure : failureHandler.bind(this),
 });			  	
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype._checkDefinitionInstances = function() {
  var definitionsNotInstanced = [];
  for (var i=0; i < this.definitions.length; i++) {
    var definition = this.definitions[i];
    if (! definition.isInstanced) definitionsNotInstanced.push(definition);
  }
  
  this._refreshMessageBox(definitionsNotInstanced);
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype._refreshMessageBox = function(definitionsNotInstanced) {  
  if (definitionsNotInstanced.length == 0) this.extMessageBox.hide();
  else this.extMessageBox.show();
  
  children = "";
  for (var i=0; i < definitionsNotInstanced.length; i++) {
    var definition = definitionsNotInstanced[i];
    children += '<li class="' + definition.code + '">' + definition.label + '</li>';
  }
  this.extList.dom.innerHTML = children;
};

//-----------------------------------------------------------------------------------
ProvidersView.prototype._getDefinitionByCode = function(code) {
  for (var i=0; i < this.definitions.length; i++) {
    definition = this.definitions[i];
    if (definition.code === code) return definition;    
  }
  return null;
};

//-----------------------------------------------------------------------------------
//ProviderRecordAdapter
//-----------------------------------------------------------------------------------

function ProviderRecordAdapter(recordClass) {
  this.recordClass = recordClass;  
}

//-----------------------------------------------------------------------------------
ProviderRecordAdapter.prototype.toRecord = function(provider) { 	
  var record = new this.recordClass({name: provider.name, label:provider.label, url: provider.url, type:provider.type, id: provider.id});	
  return record;
};

//-----------------------------------------------------------------------------------
ProviderRecordAdapter.prototype.toProvider = function(record) {
  var data = {name: record.data.name, label: record.data.label, url: record.data.url, type: record.data.type, id: record.data.id};	
  var provider = new Provider(data);
  return provider;  
};
