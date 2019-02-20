var ModelView = View.extend({
  init : function(id) {
    this._super(id);
    this.html = translate(AppTemplate.ModelView, Lang.ModelView);   
    this.model  = null;
    this.uploadModelVersionDialog = null;
    
    this.modelInitialized = false;
  }
});

ModelView.collapsed_image = "images/grided/collapsed-arrow.png";
ModelView.expanded_image  = "images/grided/expanded-arrow.png";

//-----------------------------------------------------------------------------------------------------------
ModelView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};

//-----------------------------------------------------------------------------------------------------------
ModelView.prototype.setModel = function(model) {
  this.model = model;  
};

//-----------------------------------------------------------------------------------------------------------
ModelView.prototype.setSpaces = function(spaces) {
  this.spaces = spaces;
  this._init();  
};

//-----------------------------------------------------------------------------------------------------------
ModelView.prototype.notify = function(event) {
  if (event.name == Events.ADDED) {
    if (event.data.collection == this.model.get(BusinessModel.VERSIONS)) {
      var version = event.data.item;
      this.modelVersionsView.addVersion(version);      
    }
  }
};

//-----------------------------------------------------------------------------------------------------------
ModelView.prototype.refreshModelVersion = function(modelVersion, spaces) {  
  this.modelVersionsView.refreshModelVersion(modelVersion, spaces);
};

//-----------------------------------------------------------------------------------------------------------
ModelView.prototype.hide = function() {
  this.$id.hide();
};

//-------------------------------------------------------------------------
ModelView.prototype.openUploadModelVersionDialog = function() {
  if (! this.uploadModelVersionDialog) {
    this.uploadModelVersionDialog = new UploadModelVersionDialog(this.extUpLoadModelVersionDialog.dom);
    this.uploadModelVersionDialog.addButton(Lang.Buttons.upload, function(event, target, options) {      
      var data = this.uploadModelVersionDialog.getData();      
      this.presenter.uploadModelVersion(data.form);
    }, this);          

    this.uploadModelVersionDialog.addButton(Lang.Buttons.cancel, function(event, target, options) {
      this.closeUploadModelVersionDialog();
    }, this);    
  }
  
  this.uploadModelVersionDialog.open();
};

//-------------------------------------------------------------------------
ModelView.prototype.closeUploadModelVersionDialog = function() {  
  if (this.uploadModelVersionDialog) this.uploadModelVersionDialog.close();
};

//-----------------------------------------------------------------------------------------------------------
ModelView.prototype._init = function() {
  this.extParent = Ext.get(this.id);
  var content = this.merge(this.html, {model : this.model});
  this.extParent.dom.innerHTML = content;
  this.uploadModelVersionDialog = null;
  
  this.extVersions = Ext.get(this.extParent.select('.versions').first());
    
  var versions = this.model.get(BusinessModel.VERSIONS).toArray();
  var spaces   = this.spaces.toArray();
  
  this.modelVersionsView = new ModelVersionsView(this.extVersions.dom, this.model);
  this.modelVersionsView.on(ModelVersionsView.ON_UPLOAD, {notify : this._clickUploadHandler}, this);
  this.modelVersionsView.on(ModelVersionsView.ON_OPEN_SPACE, {notify : this._clickSpaceHandler}, this);
  this.modelVersionsView.on(ModelVersionsView.ON_OPEN_FEDERATION, {notify : this._clickFederationHandler}, this);
  this.modelVersionsView.on(ModelVersionsView.ON_UPGRADE, {notify: this._clickUpgradeHandler}, this);
  
  this.modelVersionsView.setVersions(versions);
  this.modelVersionsView.setSpaces(spaces);
  this.modelVersionsView.expandAll();
    
  this.backButton     = Ext.get(this.extParent.query('a[name="back"]').first());
  this.extUpLoadModelVersionDialog = Ext.get('upload-model-version-dialog');
        
  this.extName  = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extLabel = Ext.get(this.extParent.query('input[name="label"]').first());
    
  this.backButton.on('click', this._backButtonHandler, this);
  
  this._modelInitialized = true;
};


//-----------------------------------------------------------------------------------------------------------
ModelView.prototype._backButtonHandler = function(event, target, options) {  
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickBackButton();
};

//-----------------------------------------------------------------------------------------------------------
ModelView.prototype._clickUploadHandler = function(event) {
  this.presenter.clickUploadModelVersion();  
};

//-----------------------------------------------------------------------------------------------------------
ModelView.prototype._clickSpaceHandler = function(event) {
  var id = event.data.id;
  this.presenter.openSpace(id);
};

//-----------------------------------------------------------------------------------------------------------
ModelView.prototype._clickFederationHandler = function(event) {
  var id = event.data.id;
  this.presenter.openFederation(id);
};

//-----------------------------------------------------------------------------------------------------------
ModelView.prototype._clickUpgradeHandler = function(event) {
  this.presenter.upgradeSpaces(event.data.spaceIds, event.data.versionId);
};