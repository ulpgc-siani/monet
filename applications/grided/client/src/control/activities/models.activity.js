var ModelsActivity = Activity.extend({
  init : function() {
    this.view = this._createView();
    this.view.setPresenter(this);
    this.service = GridedService;
    this.modelsCollection = null;
  }
});

//---------------------------------------------------------------------------------
ModelsActivity.prototype.start = function(data) {
  if (! this.view) this.view = this._createView();
	  
  if (this.modelsCollection) {
    this._link(this.modelsCollection);
    this._showView();    
  }
  
  if (! this.modelsCollection) this.service.loadModels({
    context : this,
    success : function(modelsCollection) {
      this.modelsCollection = modelsCollection;
      this.view.setModels(this.modelsCollection.toArray());
      this._link(this.modelsCollection);
      this._showView();
    },
    failure : function(ex) {
      throw ex;
    }    
  });     
};

//---------------------------------------------------------------------------------
ModelsActivity.prototype.stop = function(data) {
  this._unlink(this.modelsCollection);  
  this.view.hide();
};

//---------------------------------------------------------------------------------
ModelsActivity.prototype.canStop = function(callback) {
  callback.success.call(callback.context, {});
};

//--------------------------------------------------------------
ModelsActivity.prototype.notify = function(event) {
  var modelsCollection = event.data.model;  
  this.view.setModels(modelsCollection.toArray());   
};

//--------------------------------------------------------------
ModelsActivity.prototype.openModel = function(id) {
  var event = {name: Events.OPEN_MODEL, token: new ModelPlace(id).toString(), data: {id:id}};
  EventBus.fire(event);
};

//--------------------------------------------------------------
ModelsActivity.prototype.addModel = function(data) {
  
  this.service.addModel(data.form, data.name, {
    context : this,
    success : function(model) {
      this.modelsCollection.add(model);
      this.view.clearAddModelDialog();      
      this.view.focusAddModelDialog();
    },
    failure : function(ex) {
      throw ex;
    }
  });
};

//--------------------------------------------------------------
ModelsActivity.prototype.removeModels = function(ids) {
  var modelIds = ids;
  this.service.removeModels(modelIds, {
    context : this,
    success : function(ids) {
      this.modelsCollection.remove(modelIds);    	
    },
    failure : function(ex) {
      throw ex;
    }
  });
};

//---------------------------------------------------------------------------------
ModelsActivity.prototype._createView = function() {
  var view = new ModelsView(Ids.Elements.MODELS_VIEW);
  return view;
};

//---------------------------------------------------------------------------------
ModelsActivity.prototype._showView = function() {
  this.view.show();  
};

//---------------------------------------------------------------------------------
ModelsActivity.prototype._link = function(model) {
  model.on(Events.ADDED, this, this);  
  model.on(Events.REMOVED, this, this);
};

//------------------------------------------------------------------------------------
ModelsActivity.prototype._unlink = function(model) {
  if (!model) return;  
  model.un(Events.REMOVED, this);
  model.un(Events.ADDED, this);
};

