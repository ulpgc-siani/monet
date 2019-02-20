var ModelVersionsView = EventsSource.extend({
  init : function(element, model) {
    this._super();
    this.element = element;
    this.model = model;
    this.views = [];
  }
});

ModelVersionsView.ON_OPEN_FEDERATION = 'open_federation';
ModelVersionsView.ON_OPEN_SPACE      = 'open_space';
ModelVersionsView.ON_UPLOAD          = 'click_upload';
ModelVersionsView.ON_UPGRADE         = 'click_upgrade';

//-----------------------------------------------------------------------
ModelVersionsView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype.setVersions = function(versions) {
  this.versions = versions;  
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype.setSpaces = function(spaces) {
  this.spaces = spaces;  
  this._init();
};


//-----------------------------------------------------------------------
ModelVersionsView.prototype.addVersion = function(version) {
  var view = this._createModelVersionView();
  view.setVersion(version);  
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype.refreshModelVersion = function(modelVersion, spaces) {
  var view = this._getView(modelVersion.id);
  view.setSpaces(spaces);
  if (view) view.refresh(modelVersion);
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype.expandAll = function() {
  for (var i=0, l= this.views.length; i < l; i++) {
    var view = this.views[i];
    view.expand();    
  }
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype._init = function() {
  this.extParent = Ext.get(this.element);
  
  var html = translate(AppTemplate.ModelVersionsView, Lang.ModelVersionsView);
  this.extParent.dom.innerHTML = html;
  
  this.root = this.extParent.query('ul').first();
  
  
  for (var i=0, l=this.versions.length; i < l; i++) {
    var version = this.versions[i];
    var versionSpaces = [];
    var view = this._createModelVersionView();  
    view.setVersion(version);
    
    for (var j=0, s=this.spaces.length; j < s; j++) {
      var space = this.spaces[j];
      if (space.get(Space.MODEL_VERSION).id == version.id) {
        versionSpaces.push(space);        
      }
    }
    view.setSpaces(versionSpaces);
    view.refresh();
  }
  
  this.extUploadModelVersion = Ext.get(this.extParent.query('a[name="upload"]').first());
  this.extCollapseAll        = Ext.get(this.extParent.query('img[name="collapse-all"]').first());
  this.extExpandAll          = Ext.get(this.extParent.query('img[name="expand-all"]').first());

  this.extUploadModelVersion.on('click', this._clickUploadHandler, this);
  this.extCollapseAll.on('click', this._clickCollapseAllHandler, this);  
  this.extExpandAll.on('click', this._clickExpandAllHandler, this);  
  
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype._getView = function(versionId) {
  var result = null;
  for (var i=0, l=this.views.length; i < l; i++) {
    var view = this.views[i];
    if (view.getVersion().id == versionId) result = view; 
  }
  return result;
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype._createModelVersionView = function() {
  var view = new ModelVersionView(this.root, this.model);
  
  view.on(ModelVersionView.TOGGLE_VERSION , {notify: this._toggleVersionHandler}, this);
  view.on(ModelVersionView.ON_OPEN_SPACE, {notify: this._openSpaceHandler}, this);
  view.on(ModelVersionView.ON_OPEN_FEDERATION, {notify: this._openFederationHandler}, this);
  view.on(ModelVersionView.ON_UPGRADE, {notify: this._upgradeHandler }, this);  
  
  this.views.push(view);
  return view;
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype._toggleVersionHandler = function(event) {
  var view = event.data.view;
  if (view.isExpanded()) view.collapse();
  else view.expand();
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype._clickCollapseAllHandler = function(event) {
  for (var i=0, l = this.views.length; i < l; i++) {
    var view = this.views[i];
    view.collapse();
  }
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype._clickExpandAllHandler = function(event) {
  for (var i=0, l = this.views.length; i < l; i++) {
    var view = this.views[i];
    view.expand();
  }
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype._openSpaceHandler = function(event) {
  this.fire(event);
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype._openFederationHandler = function(event) {
  this.fire(event);    
};

//-----------------------------------------------------------------------
ModelVersionsView.prototype._upgradeHandler = function(event) {
  this.fire(event);
};

//-----------------------------------------------------------------------------------------------------------
ModelVersionsView.prototype._clickUploadHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  evt = {name: ModelVersionsView.ON_UPLOAD, data: {}};
  this.fire(evt);
};