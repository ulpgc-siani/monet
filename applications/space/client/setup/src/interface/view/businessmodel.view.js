
ViewBusinessModel = {}; 
ViewBusinessModel.businessModel = null;
ViewBusinessModel.dialog = {};

ViewBusinessModel.LABEL = ".model-label";
ViewBusinessModel.VERSION = ".model-version";
ViewBusinessModel.LANGUAGES = '.model-languages';
ViewBusinessModel.INITIAL_IMAGE  = ".initial-image";
ViewBusinessModel.TOP_IMAGE  = ".top-image";


//PUBLIC
ViewBusinessModel.init = function(layername) {  
  var html = AppTemplate.ViewBusinessModel;
  ViewBusinessModel.$layer = $(layername);
  ViewBusinessModel.$layer.innerHTML = translate(html, Lang.ViewBusinessModel);

  ViewBusinessModel.bind();
  ViewBusinessModel.hide();       
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessModel.setTarget = function(model) {
  if (!model.hasListener(ViewBusinessModel)) model.registerListener(ViewBusinessModel);
  ViewBusinessModel.businessModel = model;
  this.bindData(model);
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessModel.show = function() {  	 
  ViewBusinessModel.$layer.show();        
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessModel.hide = function() {  
  ViewBusinessModel.$layer .hide();
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessModel.notify = function(model) {
  ViewBusinessModel.setTarget(model);
  ViewBusinessModel.refresh();
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessModel.changeApplicationState = function(state) {
  switch (state){
    case Application.States.RUNNING:
      ViewBusinessModel.extUpdateButton.fadeOut();      
    break;	
    case Application.States.STOPPED:
      ViewBusinessModel.extUpdateButton.fadeIn();    	
    break;
  }	
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessModel.bind = function() {     
  ViewBusinessModel.extUpdateButton = Ext.get(Literals.Views.BusinessModel).select('.update-model').first();
  ViewBusinessModel.extLastUpdate = Ext.get(Literals.Views.BusinessModel).select(".last-update").first();
};
  
//-------------------------------------------------------------------------------------------------------
ViewBusinessModel.bindData = function(model) {
  var extLabelContainer = Ext.get(Literals.Views.BusinessModel).select(ViewBusinessModel.LABEL).first();	   
  var extVersionContainer = Ext.get(Literals.Views.BusinessModel).select(ViewBusinessModel.VERSION).first();	   
  var extTopImage       = Ext.get(Literals.Views.BusinessModel).select(ViewBusinessModel.TOP_IMAGE).first();
//  var extInitialImage   = Ext.get(Literals.Views.BusinessModel).select(ViewBusinessModel.INITIAL_IMAGE).first();
    
  extTopImage.dom.src = Context.Config.Api + "?op=loadbusinessmodelfile&path=images/project.logo.png";
  extTopImage.dom.title = BusinessModel.label;  		

//  extInitialImage.dom.src = Context.Config.Api + "?op=loadbusinessmodelfile&path=" + BusinessModel.images.init;
//  extInitialImage.dom.title = BusinessModel.label;
  
  extLabelContainer.dom.innerHTML = model.label;
  
  if (!model.version) return;
  var versionDate = new Date(model.version).toLocaleDateString();     
  extVersionContainer.dom.innerHTML = versionDate;
  ViewBusinessModel.extLastUpdate.dom.innerHMTL = model.updateDate;
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessModel.setUpdateHandler = function(scope, handler) {  
  ViewBusinessModel.updateHandler = handler;
  ViewBusinessModel.extUpdateButton.on('click', ViewBusinessModel.updateHandler, scope, {preventDefault: true});
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessModel.onClickUpdate = function() {
   ViewBusinessModel.scope.updateHandler();
};