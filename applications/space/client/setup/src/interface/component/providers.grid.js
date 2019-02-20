function ProvidersGrid(panelId, id) {
  this.loaderUrl = '';  
  this.grid = this._createGrid(panelId, id);
  this.selection = null;
  this.adapter = new ProviderRecordAdapter(this._createRecordClass());
  this.providers = [];
  
  this.handlers = {
	'new' : {fn: '', scope: ''},
	'delete' : {fn: '', scope: ''},	
	'aftereditrow' : {fn: '', scope: ''},
	'select' : {fn: '', scope: ''}  
  };
  
  this.commandStates = {
    'add_state' : true,
    'delete_state' : false
  };  
}
//-----------------------------------------------------------------------------------
ProvidersGrid.prototype.getSelection = function() {
  var providers = [];
  if (!this.selection) return providers;
  
  var count = this.selection.length;
  for (var i=0; i < count; i++) {
    var record = this.selection[i];          
    var provider = this.adapter.toProvider(record);
    providers.push(provider);
  }
  return providers;
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype.remove = function(record) {
  this.ds.remove(record);	
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype.commit = function(provider) {
  this.ds.commitChanges();  	
};
//-----------------------------------------------------------------------------------
ProvidersGrid.prototype.on = function(eventName, handler, scope) {
  this.handlers[eventName].fn = handler;
  this.handlers[eventName].scope = scope;
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype.un = function(eventName) {
  this.handlers[eventName] = null;
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype.load = function() {
  this._showLoading();	
  this.ds.load();	
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype.refresh = function() {
  //this.grid.render();
  if (!this.grid.container.isVisible()) return; 
  this.grid.autoSize();  
  
  var marginTop    = this.layout.regions.center.margins.top;
  var marginBottom = this.layout.regions.center.margins.bottom;
  var marginLeft   = this.layout.regions.center.margins.left;
  var marginRight  = this.layout.regions.center.margins.right;
  
  var DOMGrid = this.grid.getGridEl();
  this.layout.getEl().down(".x-layout-panel.x-layout-panel-center").dom.style.width = this.layout.getEl().getWidth() - marginLeft - marginRight + "px"; 
  DOMGrid.dom.style.height = this.layout.getEl().getHeight() - marginBottom - marginTop + "px";
  DOMGrid.dom.style.width  = this.layout.getEl().getWidth()  - marginLeft   - marginRight + "px";
  
  this._refreshHeaderPanel(this.grid);
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype.getItemsCount = function() {
  return this.ds.getCount();
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype.setProxy = function(proxy) {
  this.ds = this._createDataStore(proxy);
  this.grid.reconfigure(this.ds, this.cm);	
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype.getRecordClass = function() {	
  return this.recordClass;
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._createGrid = function(panelId, id) {
  this.recordClass = this._createRecordClass(); 	
  this.ds     = this._createDataStore();  	
  this.cm     = this._createColumnModel();
  this.sm     = this._createSelectionModel();
  
  var grid = new Ext.grid.EditorGrid(id, {
    enableColLock: false,
    ds : this.ds,
    cm:  this.cm,
    sm: this.sm,    
    clicksToEdit: 1,
    autoExpandColumn: 'url',
    autoWidth: true,
    autoHeight: true,    
  });
  
  
  this.gridPanel = new Ext.GridPanel(grid, {fitContainer: true});
  this.layout = Ext.BorderLayout.create({
    center : {
      margins:{left:3,top:3,right:3,bottom:3},
      panels: [this.gridPanel]
    }	    
  }, panelId);
  
  grid.on('beforeedit', this._beforeEditHandler.bind(this));
  grid.on('afteredit', this._afterEditHandler.bind(this));    
  grid.render();  
    
  return grid;
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._refreshHeaderPanel = function(grid) {  
  this.addCommand = new Ext.Toolbar.Button({
	text: Lang.ProvidersGrid.Toolbar.addCmd,
	handler: this._newHandler.bind(this),
	disabled: ! this.commandStates.add_state
  });
       
  this.deleteCommand = new Ext.Toolbar.Button({
	 text : Lang.ProvidersGrid.Toolbar.deleteCmd,
	 handler: this._deleteHandler.bind(this),
	 disabled: ! this.commandStates.delete_state
  });
  
  var gridHead = grid.getView().getHeaderPanel(true);
  var toolbar = new Ext.Toolbar(gridHead);
  toolbar.insertButton(0, this.addCommand);     
  toolbar.insertButton(2, this.deleteCommand);    
};
	
//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._createRecordClass = function() {	
  var record = new Ext.data.Record.create([
    {name: 'id', type: 'string'},
    {name: 'label', type: 'string'},
    {name: 'name', type: 'string', mapping: 'label'},
    {name: 'url', type: 'string'},
    {name: 'type', type: 'string'}
  ]);
  return record;
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._createDataStore = function(proxy) {	  
  var ds = new Ext.data.Store({
    proxy : (proxy)? proxy : new Ext.data.HttpProxy({url: this.loaderUrl}),
    reader: new Ext.data.JsonReader({
      totalProperty: 'nrows',
      root: 'rows'
    }, this.recordClass),
    sortInfo: {field: 'name', direction : "ASC"}
  });
  
  ds.on('load', this._afterLoadHandler, this);
        
  return ds;
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._createColumnModel = function() {
  var fm = Ext.form, Ed = Ext.grid.GridEditor;
  
  var cm = new Ext.grid.ColumnModel(
    [{
      header: Lang.ProvidersGrid.label,
      dataIndex: 'label',
      width: 300,
      editor : new Ed(new fm.TextField({allowBlank: false}))    	
     }, {
      header: Lang.ProvidersGrid.name,
	  dataIndex: 'name',
	  width: 250,
	  editor : new Ed(new fm.TextField({allowBlank: false})) 
     }, {
      id : 'url',	  
	  header: Lang.ProvidersGrid.url,
	  dataIndex: 'url',
	  width: 130,
	  editor : new Ed(new fm.TextField({allowBlank: false}))	  
    }]);
  cm.defaultSortable = true;
  
  return cm;	
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._createSelectionModel = function() {
  var sm = new Ext.grid.RowSelectionModel({singleSelect:false});
  
  sm.on('selectionchange', function(selectionModel) {
    this.selection = selectionModel.getSelections();    
    if (this.selection.length > 0) { 
      this.commandStates.delete_state = true;
    } else {
      this.commandStates.delete_state = false; 
    }    
    this._refreshHeaderPanel(this.grid);
    
    var handler = this.handlers['select'];
    if (!handler) return;
    handler.fn.apply(handler.scope, [this.getSelection()]);
  }, this);
  
  sm.on('rowdeselect', function(selectionModel, rowIndex) {	 
    this.selection = selectionModel.getSelections();
    if (this.selection.length > 0) {
      this.commandStates.delete_state = true;	
    } else {
      this.commandStates.delete_state = false;	
    }
    this._refreshHeaderPanel(this.grid);
  }, this);
  
  return sm;
};


//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._beforeEditHandler = function(store, records, options) {
  console.log("before edit providers");	
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._afterLoadHandler = function(store, records, options) {
  this._hideLoading();	  
  this._refreshHeaderPanel(this.grid);  
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._afterEditHandler = function(e) {  
  var record = e.record;
    
  if (this._isRecordCompleted(record)) {
	if (!this._check(record)) return;  
    var handler = this.handlers['aftereditrow'];               
    if (handler) handler.fn.apply(handler.scope, [record]);
  }  
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._newHandler = function(e) {
  var recordClass = this._createRecordClass();
  var record = new recordClass({label:'',name:'',url:'',id:''});
  
  this.grid.stopEditing();
  this.ds.insert(0, record);
  this.grid.startEditing(0,0);  
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._deleteHandler = function(e) {
  var handler = this.handlers['delete'];
  if (!handler) return;
  var records = this.grid.getSelectionModel().getSelections();  
    
  handler.fn.apply(handler.scope, [records]);
  this._refreshHeaderPanel(this.grid);
  //this._refreshHeaderPanel();
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._showLoading = function() {
  Desktop.showLoading();
  console.log("Cargando......................");
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._hideLoading = function() {
  Desktop.hideLoading();
  console.log("Finalizado");
};

//-----------------------------------------------------------------------------------
//ProvidersGrid.prototype._refreshHeaderPanel = function() {
//  if (this.selection == null || this.selection.length == 0) this.deleteCommand.disable();
//  else this.deleteCommand.enable();
//};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._isRecordCompleted = function(record) {
  return record.data.label !== '' && record.data.name !== '' && record.data.url !== '';
};

//-----------------------------------------------------------------------------------
ProvidersGrid.prototype._check = function(record) {  
  if (! isUrl(record.data.url)) { Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.ProvidersGrid.bad_url); return false; } 
  return true;
};