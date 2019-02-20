
ViewBusinessSpace = new Object;

ViewBusinessSpace.businessSpace = null;

ViewBusinessSpace.images = {};
ViewBusinessSpace.TOP_IMAGE = ".top-image";
ViewBusinessSpace.INITIAL_IMAGE = ".initial-image";

ViewBusinessSpace.FRONT      = "front";
ViewBusinessSpace.handlers = {
  'update-model': {fn:'', scope:''},
  'update-space': {fn:'', scope:''},
  'show-model': {fn:'', scope:''}  
};

//PUBLIC
ViewBusinessSpace.init = function(layername) {  
  var html = AppTemplate.ViewBusinessSpace;
  ViewBusinessSpace.$layer = $(layername);
  ViewBusinessSpace.$layer.innerHTML = translate(html, Lang.ViewBusinessSpace);
  
  ViewBusinessSpace.businessUnit = null;
  ViewBusinessSpace.organization = null;
  ViewBusinessSpace.modelName    = '';  
  
  ViewBusinessSpace.componentsView = this._getComponentsView(ViewBusinessSpace.FRONT);
      
  ViewBusinessSpace.bind();  
  ViewBusinessSpace.hide();        
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace.setTarget = function(space) {  	
  if (! space.hasListener(ViewBusinessSpace)) space.registerListener(ViewBusinessSpace);
  if (space != null) {    
	ViewBusinessSpace.businessSpace = space;	
	ViewBusinessSpace.bindData(space);
	
	if (ViewBusinessSpace.componentsView) ViewBusinessSpace.componentsView.refresh();	
  }  
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace.show = function() {   
  ViewBusinessSpace.$layer.show();	     
  ViewBusinessSpace.componentsView.show();   
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace.hide = function() {    
  ViewBusinessSpace.$layer.hide();  
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace.notify = function(model) {  
  ViewBusinessSpace.setTarget(model);
  ViewBusinessSpace.refresh();  
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace.changeApplicationState = function(state) {
  switch (state){
    case Application.States.RUNNING:
      ViewBusinessSpace.extUpdateModel.fadeOut();   
      ViewBusinessSpace.extUpdateSpace.fadeOut();           
    break;	
    case Application.States.STOPPED:
      ViewBusinessSpace.extUpdateModel.fadeIn();
      ViewBusinessSpace.extUpdateSpace.fadeIn();    	
    break;
  }
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace.bind = function() {
  var parent = Ext.get(Literals.Views.BusinessSpace);
  ViewBusinessSpace.extBusinessUnit = Ext.get(parent.query('a[name="business-unit"]').first());
  ViewBusinessSpace.extModel        = Ext.get(parent.query('p[name="business-model"]').first());
  ViewBusinessSpace.extOrganization = Ext.get(parent.query('a[name="space-organization"]').first());
  ViewBusinessSpace.extUpdateSpace  = Ext.get(parent.query('a[name="update-space"]').first());
  ViewBusinessSpace.extUpdateModel  = Ext.get(parent.query('a[name="update-model"]').first());
    
  ViewBusinessSpace.extTopImage      = Ext.get(parent.select(ViewBusinessSpace.TOP_IMAGE).first());
  ViewBusinessSpace.extLastUpdate = Ext.get(Literals.Views.BusinessSpace).select('.last-update').first();
  
  //ViewBusinessSpace.extModel.on('click', ViewBusinessSpace._clickModelHandler, ViewBusinessSpace);
  
  ViewBusinessSpace.extTopImage.on('click', ViewBusinessSpace._clickImageHandler, ViewBusinessSpace);

  ViewBusinessSpace.extUpdateSpace.on('click', ViewBusinessSpace._clickUpdateSpaceHandler, ViewBusinessSpace);
  ViewBusinessSpace.extUpdateModel.on('click', ViewBusinessSpace._clickUpdateModelHandler, ViewBusinessSpace);
  
  ViewBusinessSpace.extContent = Ext.get(Literals.Views.BusinessSpace).select('.space-content');
  
  ViewBusinessSpace.imageViewer = new ImageViewer('image-viewer');  
};
  
//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace.bindData = function(space) {
  	  	     	    
  ViewBusinessSpace.extTopImage.dom.src = Context.Config.Api + "?op=loadbusinessunitfile&path=images/logo.png" + "&rand=" + Math.random();
  ViewBusinessSpace.extTopImage.dom.title = BusinessModel.label;
            
  ViewBusinessSpace.extBusinessUnit.set({'href': space.businessUnit.url});  
  
  ViewBusinessSpace.extBusinessUnit.set({'href': space.businessUnit.url});
  ViewBusinessSpace.extBusinessUnit.set({'title': space.businessUnit.name});
  ViewBusinessSpace.extBusinessUnit.dom.innerHTML = space.businessUnit.name;  
  
  ViewBusinessSpace.extOrganization.set({'href' : space.organization.url});
  ViewBusinessSpace.extOrganization.set({'title': space.organization.name});
  ViewBusinessSpace.extOrganization.dom.innerHTML = space.organization.name;
   
  ViewBusinessSpace.extModel.set({'title': space.modelName});
  ViewBusinessSpace.extModel.dom.innerHTML = space.modelName;
  
  ViewBusinessSpace.extLastUpdate.dom.innerHTML = ViewBusinessSpace._formatDate(space.updateDate);    
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace.on = function(eventName, handler, scope) {
  ViewBusinessSpace.handlers[eventName] = {fn: handler, scope: scope}; 	
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace.un = function(eventName, handler, scope) {
  ViewBusinessSpace.handlers[evnetName] = null;	
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace._clickUpdateSpaceHandler = function(event, target, scope) {
  event.preventDefault();	
  var handler = ViewBusinessSpace.handlers['update-space'];
  handler.fn.call(handler.scope);
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace._clickUpdateModelHandler = function(event, target, scope) {
  event.preventDefault();	
  var handler = ViewBusinessSpace.handlers['update-model'];
  handler.fn.call(handler.scope);
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace._clickModelHandler = function(event, target, options) {
  event.preventDefault();
  var handler = ViewBusinessSpace.handlers['show-model'];
  handler.fn.call(handler.scope);
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace._clickImageHandler = function(event, target, options) {
  event.stopPropagation();
  var imageModel = {caption: 'caption', src: Context.Config.Api + "?op=loadbusinessunitfile&path=" + BusinessSpace.images.init + "&rand=" + Math.random() };
  if (! ViewBusinessSpace.imageViewer.hasImageModel()) {
    ViewBusinessSpace.imageViewer.setImageModel(imageModel);
  }
  ViewBusinessSpace.imageViewer.show();  
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace._getComponentsView = function(id) {  
  var view = new ComponentsView(id);
  view.on(ComponentsViewEvents.change, ViewBusinessSpace._changeComponentHandler, this);
  return view;
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace._changeComponentHandler = function(component) {  
  var successHandler = function(response, request) {
    if (response.responseText.indexOf("ERR_") != -1) {failureHandler.apply(this, [response, request]); return; }
	  //var data = Ext.util.JSON.decode(response.responseText);	  	
	};
				  
  var failureHandler = function(response, request) {
    Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Action.PublishFrontComponent.Failure); 
  };
				  
  Ext.Ajax.request({
    url: Context.Config.Api + "?op=publishfrontcomponent",
	method: 'get',	
	params: {id: component.id, enabled: component.enabled},
	success : successHandler.bind(this),
	failure : failureHandler.bind(this),
  });	
};

//-------------------------------------------------------------------------------------------------------
ViewBusinessSpace._formatDate = function(milliseconds) {
  var date = new Date(milliseconds);
  var month = aMonths[Context.Config.Language][date.getMonth()];
  var day = aDays[Context.Config.Language][date.getDay()];
  
  console.log(date.toDateString());
  return day + ' ' + date.getDay() + ' de ' + month + ' de ' + date.getFullYear() + ' a las ' +   date.getHours() + ':' + date.getMinutes();  
};
