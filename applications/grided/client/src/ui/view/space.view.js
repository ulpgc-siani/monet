var SpaceView = View.extend({
  init : function(id) {
    this._super(id);    
    this.html = translate(AppTemplate.SpaceView, Lang.SpaceView);
    
    this.space = null;
    this.editor = null;
    this.saveDialog = null;
    this.uploadDialog = null;
    
    this._spaceInitialized = false;
    this._publishTabInitialized = false;
  }
}); 

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.getEditor = function() {
  return this.editor;   
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.setSpace = function(space) {
  this.space = space;  
  if (! this._spaceInitialized) this._init();
  this._refresh();
  //this._init(); //has a problem with tabs
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.setServices = function(services) {
  this.services = services; 
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.setProgress = function(progress) {
  this.progress = progress;
  if (this.importSpaceDataView) this.importSpaceDataView.setProgress(this.progress);     
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.setImporterTypes = function(importerTypes) {
  this.importerTypes = importerTypes;
  if (this.importSpaceDataView) this.importSpaceDataView.setImporterTypes(importerTypes);  
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.finishImportation = function() {
  if (this.importSpaceDataView) this.importSpaceDataView.setResult(Lang.ImportSpaceDataView.importation_finised);
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.hide = function() {
  this.hideUploadImageDialog();
  this.$id.hide();
};

//-------------------------------------------------------------------------
SpaceView.prototype.refreshState = function() {
  if (this.space.state.running) 
    this.extStartStopButton.dom.innerHTML = Lang.SpaceView.stop;
  else 
    this.extStartStopButton.dom.innerHTML = Lang.SpaceView.start;
  
  var $runningTime = $(this.extRunningTime.dom);  
  
  if (this.space.state.running) {
    $runningTime.innerHTML = "Running from " + new Date(this.space.state.time).format('d M Y - G:i');
    $runningTime.show();
  }
  else {
    $runningTime.innerHTML = "";
    $runningTime.hide();  
  }
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.filterServiceByType = function(type) {
  if (this.servicesView) this.servicesView.filterServiceByType(type);	
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.openSaveDialog = function(saveHandler, discardHandler, scope) {
  if (! this.saveDialog) {
    this.saveDialog = new SaveChangesDialog(this.extSaveDialog.dom);
    this.saveDialog.addButton(Lang.Buttons.save, saveHandler, scope);
    this.saveDialog.addButton(Lang.Buttons.discard, discardHandler, scope);
  }
  this.saveDialog.open();
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.closeSaveDialog = function() {
  if (this.saveDialog) this.saveDialog.close(); 
};

//-------------------------------------------------------------------------
SpaceView.prototype.showUploadImageDialog = function() {
  if (! this.uploadDialog) {
    this.uploadDialog = new UploadDialog(this.extUploadImageDialog.dom);
    this.uploadDialog.on(UploadDialog.CLICK_CLOSE, {
      notify : function(event) {
        this._showLogo();
        this.hideUploadImageDialog();
      }    	
    }, this);
    
	this.uploadDialog.on(UploadDialog.CLICK_SUBMIT, {
		notify : function(event) {
		  var form = event.data.form;
		  var callback = {scope: this, fn: function(filename, source) {
		    this.editor.setImage(filename, source);  
		  }};		  
		  this.presenter.uploadImage(form, callback);
		}
    }, this);
  }
  
  this._hideImage();
  this.uploadDialog.open();
};

//-------------------------------------------------------------------------
SpaceView.prototype.hideUploadImageDialog = function() {
  if (this.uploadDialog) this.uploadDialog.close();
  this._showLogo();
};

//-------------------------------------------------------------------------
SpaceView.prototype._showLogo = function() {
  $(this.extChangeLogo.dom).show();  
  $(this.extLogo.dom).show();	  	
};

//-------------------------------------------------------------------------
SpaceView.prototype._hideImage = function() {
  $(this.extChangeLogo.dom).hide();  
  $(this.extLogo.dom).hide();
};

//-------------------------------------------------------------------------
SpaceView.prototype._refresh = function() {
  this.extName.dom.value = this.space.name;
  this.extLabel.dom.value = this.space.label;
  
  var serverSection = this.merge(AppTemplate.ServerSection, {server : this.space.federation.server});
  serverSection = translate(serverSection, Lang.SpaceView);
  this.extServerSection.dom.innerHTML = serverSection;
  
  var federationSection = this.merge(AppTemplate.FederationSection, {federation: this.space.federation});
  federationSection = translate(federationSection, Lang.SpaceView);
  this.extFederationSection.dom.innerHTML = federationSection;
  
  this._activatePublishTabHandler();
  this.refreshState();
  
//  this.editor = this._createEditor(this.space);
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype._init = function() {
  this.extParent = this.extParent || Ext.get(this.id);
  var content = this.merge(this.html, {space : this.space});
  this.extParent.dom.innerHTML = content;      
  
  this.backButton     = Ext.get(this.extParent.query('a[name="back"]').first());
  this.saveButton     = Ext.get(this.extParent.query('a[name="save"]').first());
  this.discardButton  = Ext.get(this.extParent.query('a[name="discard"]').first());

  this.extUploadImageDialog   = Ext.get('upload-space-image-dialog');
    
  this.extChangeLogo  = Ext.get(this.extParent.query('a[name="change-image"]').first());
  this.extLogo        = Ext.get(this.extParent.query('img[name="image"]').first()); 
  
  this.extServerSection     = Ext.get(this.extParent.query('div[name="server"]').first());       
  this.extFederationSection = Ext.get(this.extParent.query('div[name="federation"]').first());  
  this.extModelSection      = Ext.get(this.extParent.query('div[name="model"]').first());  
  
  this.extName  = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extLabel = Ext.get(this.extParent.query('input[name="label"]').first());    
  this.extDatawarehouse = Ext.get(this.extParent.query('input[name="datawarehouse"]').first()); 
  
  this.extTabs = new Ext.TabPanel("tabs");
  this.publishTab = this.extTabs.addTab(Ids.Elements.PUBLISH_TAB, Lang.SpaceView.publish);  
  this.importTab  = this.extTabs.addTab(Ids.Elements.IMPORT_TAB, Lang.SpaceView.import);    

  this.extSaveDialog = Ext.get(Ids.Elements.DIALOG); 
  
  this.extServerSection.on('click', this._clickServerHandler, this);
  this.extFederationSection.on('click', this._clickFederationHandler, this);
  this.extModelSection.on('click', this._clickModelHandler, this);
  
  this.backButton.on('click', this._backButtonHandler, this);
  this.saveButton.on('click', this._saveButtonHandler, this);
  this.discardButton.on('click', this._discardButtonHandler, this);   
  this.extChangeLogo.on('click', this._changeLogoHandler, this);
  
  this.publishTab.on('activate', this._activatePublishTabHandler, this);
  this.importTab.on('activate', this._activateImportTabHandler, this);
      
  this.extTabs.on('tabchange', function(tabPanel) {
    var tab = tabPanel.getActiveTab();
    if (!tab) return;
    tab.activate();
  }, this);
  
  $(this.extTabs.el).show();
  this.publishTab.activate();
  $(this.extUploadImageDialog.dom).hide();
  this.extTabs.endUpdate();
    
  this.editor = this._createEditor(this.space);
  $(this.saveButton.dom).addClassName('disable');
  
  this._initState();
  
  this._spaceInitialized = true;
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype._clickServerHandler = function(event, target, options) {  	
  if (target.nodeName.toLowerCase() != 'a') return;

  event.preventDefault();
  event.stopPropagation();  
  this.presenter.openServer(target.getAttribute('code')); 
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype._clickFederationHandler = function(event, target, options) {
  if (target.nodeName.toLowerCase() != 'a') return;
	  
  event.preventDefault();
  event.stopPropagation();  
  this.presenter.openFederation(target.getAttribute('code'));    	
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype._clickModelHandler = function(event, target, options) {
  if (target.nodeName.toLowerCase() != 'a') return;
	  
  event.preventDefault();
  event.stopPropagation();  
  this.presenter.openModel(target.getAttribute('code'));    	
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype._backButtonHandler = function(event, target, options) {  
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickBackButton();
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype._saveButtonHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickSaveButton();	
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype._discardButtonHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickDiscardButton();	
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype._changeLogoHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickChangeLogo();	  	
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype._activatePublishTabHandler = function(event, target, options) {
  this.presenter.clickPublishTab();	
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype._activateImportTabHandler = function(event, target, options) {
  this.presenter.clickImportTab();	
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.showPublishTab = function() {
  if (this.servicesView == null) {
    this.servicesView = new ServicesView(this.publishTab.bodyEl.id);
    this.servicesView.setPresenter(this.presenter);
    this.servicesView.setServices(this.services);
    
    var servicesEditor = this.servicesView.getEditor();
    servicesEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
    servicesEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
    
    this.editor.setServicesEditor(this.servicesView.getEditor());  
  }
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype.showImportTab = function() {
  if (this.importSpaceDataView == null) {
    this.importSpaceDataView = new ImportSpaceDataView(this.importTab.bodyEl.id);
    this.importSpaceDataView.setPresenter(this.presenter);
    this.importSpaceDataView.setImporterTypes(this.importerTypes);    
  }
  this.importSpaceDataView.show();
};

//-------------------------------------------------------------------------
SpaceView.prototype._initState = function() {
  this.extRunningTime     = this.extParent.select('.running_time').first();
  this.extStartStopButton = Ext.get(this.extParent.query('a[name="start_stop"]').first()); 
    
  this.extStartStopButton.on('click', function(event, target, options) {
    event.stopPropagation();
    event.preventDefault();
    
    if (this.space.state.running) this.presenter.stopSpace();
    else this.presenter.startSpace();
  }, this);
   
  this.refreshState();
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype._createEditor = function(space) {
  var map = {};
  //map[Space.NAME]  = {element: this.extName.dom, propertyName: Space.NAME};
  map[Space.LABEL] = {element: this.extLabel.dom, propertyName: Space.LABEL};
  map[Space.LOGO]  = {element: this.extLogo.dom, propertyName: Space.LOGO};
  map[Space.DATAWAREHOUSE] = {element: this.extDatawarehouse.dom, propertyName: Space.DATAWAREHOUSE};
    
  this.editor = new SpaceEditor(space, {map: map});
  this.editor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.editor.on(Events.RESTORED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  
  return this.editor;
};

//-----------------------------------------------------------------------------------------------------------
SpaceView.prototype._changeEditorHandler = function(event) {
  if (this.editor.isDirty()) $(this.saveButton.dom).removeClassName('disabled');
  else $(this.saveButton.dom).addClassName('disabled');  
};
