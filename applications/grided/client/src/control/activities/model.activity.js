var ModelActivity = Activity.extend({
  init : function() {
    this.view = this._createView();
    this.view.setPresenter(this);
    this.service = GridedService;

    this.model = null;
  }
});

// ---------------------------------------------------------------------------------
ModelActivity.prototype.start = function(data) {
  if (!this.model || this.model.id != data.id)
    this._clearModel();

  if (!this.view) this.view = this._createView();
  if (this.model) this._showView(this.model);
  
  if (!this.model)
    
    this.service.loadModel(data.id, {
      context : this,
      success : function(model) {                
        this.model = model;        
        this.view.setModel(this.model); 
        
        this._loadSpaces(this.model.id);        
        this._showView(this.model);
      },
      failure : function(ex) {
        throw ex;
      }
    });
};

// ---------------------------------------------------------------------------------
ModelActivity.prototype.stop = function(data) {
  this._unlink(this.model);
  this.model = null;
  this.view.hide();
};

// ---------------------------------------------------------------------------------
ModelActivity.prototype.canStop = function(callback) {
  callback.success.call(callback.context, {});
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype.notify = function(event) {
  this.view.notify(event);
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype.clickUploadModelVersion = function() {
  this.view.openUploadModelVersionDialog();
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype.uploadModelVersion = function(form) {
  this.service.uploadModelVersion(form, {model_id : this.model.id}, {
    context : this,
    success : function(version) {
      this.view.closeUploadModelVersionDialog();
      this.model.get(BusinessModel.VERSIONS).add(version);
    },
    failure : function(ex) {
      this.view.closeUploadModelVersionDialog();
      ExceptionHandler.handle(ex);
    }
  });
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype.clickExpandModelVersion = function(versionId) {
  var versions = this.model.get(BusinessModel.VERSIONS);
  var selectedVersion = versions.getById(versionId);

  this.service.loadSpacesWithModel(this.model.id, selectedVersion, {
    context : this,
    success : function(spaces, version) {
      version.set(ModelVersion.SPACES, spaces, {
        silent : true
      });
      this.view.expandModelVersion(version.id, version.get(ModelVersion.SPACES)
          .toArray());
    },
    failure : function(ex) {
      throw ex;
    }
  });
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype.clickCollapseModelVersion = function(versionId) {
  this.view.collapseModelVersion(versionId);
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype.collapseAllVersions = function() {
  this.view.collapseAllModelVersions();
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype.upgradeSpaces = function(spaceIds, versionId) {
  
  this.service.upgradeSpaces(spaceIds, this.model.id, versionId, {
    context: this,
    success : function(spaceIds, modelId, versionId) {
      var versions = this.model.get(BusinessModel.VERSIONS);
      var oldVersion = versions.getById(versionId);
      var nextVersion = this.model.getNextVersion(oldVersion);
      
      for (var i=0, l=spaceIds.length; i < l; i++) {
        var spaceId = spaceIds[i];
        var space = this.modelSpaces.getById(spaceId);
        space.set(Space.MODEL_VERSION, nextVersion, {silent: true});        
      }
      
      oldVersionSpaces = [];
      this.modelSpaces.each(function(index, space) {
        if (space.get(Space.MODEL_VERSION).id === oldVersion.id) oldVersionSpaces.push(space);
      }, this);
      
      nextVersionSpaces = [];
      this.modelSpaces.each(function(index, space) {
        if (space.get(Space.MODEL_VERSION).id === nextVersion.id) nextVersionSpaces.push(space);
      }, this);
      
      this.view.refreshModelVersion(nextVersion, nextVersionSpaces);
      this.view.refreshModelVersion(oldVersion, oldVersionSpaces);
      
//      var spaces = oldVersion.get(ModelVersion.SPACES);
//      for ( var i = 0, l = spaceIds.length; i < l; i++) {
//        var id = spaceIds[i];
//        var space = spaces.getById(id);
//        var cloned = _.clone(space);
//        nextVersion.get(ModelVersion.SPACES).add(cloned);
//      }
//
//      spaces.remove(spaceIds);
                 
    },
    failure : function(ex) {
      throw ex;
    }
  });      
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype.openSpace = function(id) {
  var event = {
    name : Events.OPEN_SPACE,
    token : new SpacePlace(id).toString(),
    data : {
      id : id
    }
  };
  EventBus.fire(event);
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype.openFederation = function(id) {
  var event = {
    name : Events.OPEN_FEDERATION,
    token : new FederationPlace(id).toString(),
    data : {
      id : id
    }
  };
  EventBus.fire(event);
};

// --------------------------------------------------------------
ModelActivity.prototype.clickBackButton = function() {
  this._goBack();
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype._goBack = function() {
  var event = {name : Events.OPEN_MODELS, token : new ModelsPlace().toString(), data: {}};
  EventBus.fire(event);
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype._link = function(model) {
  model.on(Events.CHANGED, this, this);
  model.on(Events.ADDED, this, this);  
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype._unlink = function(model) {
  if (!model)
    return;
  model.un(Events.CHANGED, this);
};

//------------------------------------------------------------------------------------
ModelActivity.prototype._loadSpaces = function(modelId) {
  this.service.loadSpacesByModel(modelId, {
    context : this,
    success : function(spaces) {
      this.modelSpaces = spaces;
      this.view.setSpaces(this.modelSpaces);
    },
    failure : function(ex) {
      throw ex;
    }
  });
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype._createView = function(space) {
  var view = new ModelView(Ids.Elements.MODEL_VIEW, AppTemplate.ModelView);
  return view;
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype._showView = function(model) {
  this._link(model);
  this._link(model.get(BusinessModel.VERSIONS));
  this.view.show();
};

// ------------------------------------------------------------------------------------
ModelActivity.prototype._clearModel = function() {
  this.model = null;
};
