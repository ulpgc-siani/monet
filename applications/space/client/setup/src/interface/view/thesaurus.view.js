function ThesaurusView(layername) {
  this.layername = layername;
  this.$layer = $(layername);
  var html = AppTemplate.ViewThesaurus;
  this.$layer.innerHTML = translate(html, Lang.ThesaurusView);
  
  this.thesaurus = [];
  this.listSelection = null;

  this.RequestParams = {start: 0, limit: 75, code : ''};
  this.GridConfig = {pageSize: 75};
  
  this.ds = null;
  this.list  = this._createListComponent('thesaurus-list');
  this.grid  = this._createGridComponent('thesaurus-grid-panel', 'thesaurus-grid');
  //this.panel = this._createThesaurusExternalPanel('thesaurus-external-panel');
  
  this._loadThesaurusList();  
  this.bind();  
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype.setThesaurusList = function(list) {
  this.thesaurusList = list;	
  for (var i=0; i < list.length; i++) {
    var model = {index: i, data:list[i]};
    this.list.add(model);
  }	  
  this.list.selectFirstItem();
};

//-----------------------------------------------------------------------------------
/*
ThesaurusView.prototype.setThesaurusProviders = function(list) {
  this.thesaurusProviders = list;
    
  for (var i=0; i < list.length; i++) {
	var provider = list[i];
	var html = '<option value="$1" $2>$3</option>';
	html = html.replace(/\$1/, provider.id);
	html = html.replace(/\$2/, (this.listSelection.providerName)? 'selected' : '');
	html = html.replace(/\$3/, provider.label);
	
    this.extProviders.dom.innerHTML += html;	   
  }  
};
*/

//-----------------------------------------------------------------------------------
ThesaurusView.prototype.show = function() {
  this.$layer.show();
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype.hide = function() {
  this.$layer.hide();
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype.refresh = function() {
  this.grid.autoSize();
    
  var marginTop    = this.layout.regions.center.margins.top;
  var marginBottom = this.layout.regions.center.margins.bottom;
  var marginLeft   = this.layout.regions.center.margins.left;
  var marginRight  = this.layout.regions.center.margins.right;
  
  var DOMGrid = this.grid.getGridEl();
  this.layout.getEl().down(".x-layout-panel.x-layout-panel-center").dom.style.width = this.layout.getEl().getWidth() - marginLeft - marginRight + "px"; 
  DOMGrid.dom.style.height = this.layout.getEl().getHeight() - marginBottom - marginTop + "px";
  DOMGrid.dom.style.width  = this.layout.getEl().getWidth()  - marginLeft   - marginRight + "px";  
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype.bind = function() {  
  this.extName = Ext.get(this.layername).select('.name').first();  
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype.bindData = function(thesaurus) {
/*	
  if (thesaurus.isExternal()) {
	this.panel.show();
	//this.panel.refresh(thesaurus);
  } 
  else {
	this.panel.hide();
  }
*/
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._selectionHandler = function(thesaurus) {  
  this._onSelectThesaurus(thesaurus);
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._onSelectThesaurus = function(thesaurus) {
  this.listSelection = thesaurus;
  this.bindData(thesaurus);
  this.RequestParams.code = (thesaurus)? thesaurus.code : 0;	  	    	  
  this.ds.load({params: {start: this.RequestParams.start, limit: this.RequestParams.limit, code: this.RequestParams.code}});      
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._refreshGrid = function(start, limit) {    
  this.RequestParams.start = start;
  this.RequestParams.limit = limit;
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._refreshFooterPanel = function() {
  this._createFooterPanel();	
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._createListComponent = function(id) {
  var list = new ListComponent(id);
  list.setItemViewClass(ThesaurusItemView);
  list.onSelect(this._selectionHandler, this);  
  return list;
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._createGridComponent = function(panelId, id) {        
  this.ds = this._createDataStore();
  this.cm = this._createColumnModel();  
  this.grid = this._createGrid(panelId, id);     
  this.grid.render();  
  
  this._createHeaderPanel();
  this._createFooterPanel();
   
  this.grid.getView().focusEl.focus();
  
  return this.grid;
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._createThesaurusExternalPanel = function(id) {
  var panel = new ThesaurusExternalPanel(id);
  return panel;
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._createDataStore = function() {
  var url = Context.Config.Api + "?op=loadthesaurus";
	    
  this.ThesaurusRecord = Ext.data.Record.create([
    {name: 'code', type: 'string'},
    {name: 'value', type: 'string'},
    {name: 'parent', type: 'string'},
    {name: 'enabled', type: 'boolean', defaultValue: 'true'}
  ]);

  var ds = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({url: url}),
    reader : new Ext.data.JsonReader({
      totalProperty: 'nrows',  
      root: 'rows'
    }, this.ThesaurusRecord),
    sortInfo: { field: "parent", direction: "ASC" }
  });
  
  ds.on('load', function(ds, records, options) {
    if (records.length == 0) return;
    this._refreshGrid(options.params.start, options.params.limit);
  }, this);
    
  return ds;
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._createColumnModel = function() {
  var fm = Ext.form, Ed = Ext.grid.GridEditor;
        
  var formatStateValue = function(value) {
	return (value)? 'Si' : 'No';  
  };
    
  var stateStore = new Ext.data.SimpleStore({
    fields: ['key', 'enabled'],
    data: [[true,'Si'],[false,'No']]
  });
  
  var cm = new Ext.grid.ColumnModel(
   [{header: Lang.ThesaurusView.Grid.code,
      dataIndex : 'code',
      width: 130,
      editor : new Ed(new fm.TextField({allowBlank: false}))      
    }, {
      id : 'value',	
      header: Lang.ThesaurusView.Grid.value,
      dataIndex : 'value',
      width: 280,
      editor : new Ed(new fm.TextField({allowBlank: false}))   
    }, {
      header: Lang.ThesaurusView.Grid.parent,
      dataIndex : 'parent',
      width: 70,           
      editor : new Ed(new fm.TextField({allowBlank: false}))
    }, {
        header: Lang.ThesaurusView.Grid.enabled,
        dataIndex : 'enabled',
        width: 70,           
        renderer: formatStateValue,
        editor : new Ext.form.ComboBox({
          store: stateStore,
          fieldLabel: 'enabled',
          displayField: 'enabled',   // what the user sees in the popup
          valueField: 'key',        // what is passed to the 'change' event
          typeAhead: true,
          forceSelection: true,
          mode: 'local',
          triggerAction: 'all',
          selectOnFocus: true,
          editable: true
        })                	
      }]
  );
  cm.defaultSortable = true;
  
  return cm;  
};	  

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._createGrid = function(panelId, id) {
  var grid = new Ext.grid.EditorGrid(id, {
    enableColLock: false,
    ds : this.ds,
    cm:  this.cm,
    sm: new Ext.grid.CellSelectionModel(),    
    clicksToEdit: 1,
    autoExpandColumn: 'value'    
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
  
  return grid;
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._createHeaderPanel = function() {
  var gridHead = this.grid.getView().getHeaderPanel(true);
  
  this.newCommand = new Ext.Toolbar.Button({
	text: Lang.ThesaurusView.Grid.Toolbar.newCmd,
	handler: this._newTermHandler.bind(this),
	disable: false
  });
     
  this.importCommand = new Ext.Toolbar.Button({
	 text : Lang.ThesaurusView.Grid.Toolbar.importCmd,
	 handler: this._importHandler.bind(this),
	 disable: false
  });
  
  this.exportCommand = new Ext.Toolbar.Button({
	 text : Lang.ThesaurusView.Grid.Toolbar.exportCmd,
	 handler: this._exportHandler.bind(this),
	 disable: false
  });
  
  var toolbar = new Ext.Toolbar(gridHead);
  toolbar.insertButton(0, this.newCommand);   
  toolbar.addSeparator();
  toolbar.insertButton(2, this.importCommand);
  toolbar.insertButton(3, this.exportCommand);  
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._createFooterPanel = function() {
  var gridFoot = this.grid.getView().getFooterPanel(true);
  var paging = new Ext.PagingToolbar(gridFoot, this.ds, {
    pageSize: this.GridConfig.pageSize,
    displayInfo: true
  });
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._newTermHandler = function() {
  var addHandler = function() {
	var record = dialog.getRecord();	 
    this._saveTerm(record);
  };
  
  var newHandler = function() {
	dialog.clear();	
  };
  
  var dialog = new DialogNewTerm(Literals.Dialogs.Modal);
  dialog.addButton(dialog.NEW_BUTTON, newHandler, this);
  dialog.addButton(dialog.SAVE_BUTTON, addHandler, this);
  dialog.addButton(dialog.CLOSE_BUTTON, dialog.hide, dialog);
  dialog.show();   
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._beforeEditHandler = function(e) {
  console.log("row: " + e.row);  
  var data = e.record.data;
  var thesaurus = this.listSelection;
  if (thesaurus.isExternal()) { Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.TheaurusView.no_editable); return false;}
  
  if (e.field === 'value' || e.field === 'enabled') return true;  
  if (e.row == 0 && data[e.field].trim().length > 0) return false;  
  if (e.row != 0 && (e.field !== 'value' || e.field !== 'enabled')) return false;
      
  return true;      
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._afterEditHandler = function(e) {
  var successHandler = function(response, request) {
    e.record.commit();    
  };
 
  var failureHandler = function(response, request) {    
    Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Action.UpdateTerm.Failure);
  };  
  
  var data = e.record.data;
  var enabled = (data.enabled == "yes")? true : false; 
  var term = {'code':data.code, 'value':data.value, 'parent':data.parent,'enabled':enabled};
  var jsonTerm = Ext.util.JSON.encode(term);
  
  Ext.Ajax.request({
	url: Context.Config.Api,
	method: 'post',
	params : {op: 'updatethesaurus', code: this.RequestParams.code, term: jsonTerm},
	success : successHandler.bind(this),
	failure : failureHandler.bind(this),
 });	
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._saveTerm = function(record) {
  var codes = this.ds.query('code', record.data['code'], true);	
  if (codes.getCount() > 0) {
    alert(Lang.ThesaurusView.duplicate_code);
    return;
  }	
	
  var successHandler = function(response, request) {
	if (response.responseText.indexOf("ERR_") != -1) {failureHandler.apply(this, [response, request]); return; }
    this.grid.stopEditing();
	this.ds.insert(0, record);
	this.grid.startEditing();	    
  };
 
  var failureHandler = function(response, request) {    
	Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Action.SaveTerm.Failure);
  };  
    
  var data = record.data;
  var enabled = data.enabled; 
  var term = {'code':data.code, 'value':data.value, 'parent':data.parent,'enabled': enabled};
  var jsonTerm = Ext.util.JSON.encode(term);
  
  Ext.Ajax.request({
	url: Context.Config.Api,
	method: 'post',
	params : {op: 'addterm', code: this.RequestParams.code, term: jsonTerm},
	success : successHandler.bind(this),
	failure : failureHandler.bind(this),
 });
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._importHandler = function() {
  
  var handler = function(btn) {
    switch (btn.text) {
    case dialog.IMPORT_BUTTON:
      if (!dialog.check()) return;
      dialog.submit();      
      break;
    case dialog.CANCEL_BUTTON:
      dialog.hide();
      break;        	 
    }
  };
  
  var resultHandler = {
	onSuccess: function(response, options) {
	  this.ds.load({params: {start: this.RequestParams.start, limit: this.RequestParams.limit, code: this.RequestParams.code}});	
	},
    onFailure: function(response, options) {
      Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Action.ImportThesaurus.Failure);
    }   
  };
  
  var dialog = new DialogImportThesaurus(Literals.Dialogs.Modal);
  dialog.setTitle(Lang.Dialog.ImportThesaurus.title);
  dialog.setMessage(Lang.Dialog.ImportThesaurus.message);
  dialog.setThesaurusList(this.thesaurusList);
  dialog.setResultHandler(resultHandler, this);
  dialog.addButton(Lang.Dialog.ImportThesaurus.import, handler, this);
  dialog.addButton(Lang.Dialog.ImportThesaurus.cancel, handler, this);  
  dialog.show();  
};

//-----------------------------------------------------------------------------------
ThesaurusView.prototype._exportHandler = function() {
  var url = Context.Config.Api + "?op=exportthesaurus&code=" + this.RequestParams.code + "&format=xml" + "&name=" + this.listSelection.label;
  window.location.href = url;
};

//------------------------------------------------------------------------------------
ThesaurusView.prototype._loadThesaurusList = function() {
  
  var successHandler = function(response, request) {
	if (response.responseText.indexOf("ERR_") != -1) {failureHandler.apply(this, [response, request]); return; }
	 var data = Ext.util.JSON.decode(response.responseText);
	 var thesaurusList = [];
	 for (var i=0; i < data.length; i++) {
	   thesaurus = new Thesaurus(data[i]);	
	   thesaurusList.push(thesaurus);
	 }
	    
     if (thesaurusList) {
       this.setThesaurusList(thesaurusList);
     }
  };
  
  var failureHandler = function(response, request) {	
	console.log(Lang.Action.LoadThesaurusList.Failure + "thesaurus.view.js");
  };
  
  Ext.Ajax.request({
	url: Context.Config.Api + "?op=loadthesauruslist",
	method: 'get',	
	success : successHandler.bind(this),
	failure : failureHandler.bind(this),
 });
};


//-----------------------------------------------------------------------------------
// ThesaurusItemView
//-----------------------------------------------------------------------------------
function ThesaurusItemView(target) {
  this.target = target;
  this.element = document.createElement('a');
  this.element.setAttribute('href', '');
  this.element.innerHTML = target.label;
};
////-----------------------------------------------------------------------------------
ThesaurusItemView.prototype.el = function() {
  return this.element;	
};