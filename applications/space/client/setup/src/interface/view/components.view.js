
ComponentsViewEvents = {
  change : 'change'    
};

ComponentTypes = {  
  All : 'All',		
  SERVICE : 'Service',
  THESAURUS : 'Thesaurus',
  MAP : 'Map',
  CUBE : 'Cube'		  
};


function ComponentsView(layername) {
  this.layername = layername;
  this.$layer = $(layername);
  var html = AppTemplate.ViewComponents;
  this.$layer.innerHTML = translate(html, Lang.ViewComponents);
    
  this.handlers = { fn: null, scope: null};  
  this.bind(layername);
  
  this.components = [];
  this._loadComponents();
};

//------------------------------------------------------------------
ComponentsView.prototype.show = function() {
  this.$layer.show(); 	
};

//------------------------------------------------------------------
ComponentsView.prototype.hide = function() {
  this.$layer.hide();
};

//------------------------------------------------------------------
ComponentsView.prototype.on = function(eventName, handler, scope) {
  this.handlers[eventName] = {fn: handler, scope: scope};  	
};

//------------------------------------------------------------------
ComponentsView.prototype.un = function(eventName, handler) {
  this.handlers[eventName] = null;	
};

//------------------------------------------------------------------
ComponentsView.prototype.bind = function(layername) {
  this.parent = Ext.get(layername);	
  this.extMessage   = this.parent.select('.message').first();
  this.extServicesCb = Ext.get('services-type');
  this.extServicesList  = Ext.get('services-list');
  this.extEmptyListMessage = this.extServicesList.select('.message').first();  
      
  this.extServicesCb.on('change', this._changeComponentsTypeHandler, this);
  this.extServicesList.on('change', this._changeComponentHandler, this);   
};

//------------------------------------------------------------------
ComponentsView.prototype._changeComponentHandler = function(event, target, options) {
  var el = null;	
  switch (target.nodeName.toLowerCase()) {
    case 'li': el = target.firstChild;  el.checked = !el.checked; break;
    case 'input': el = target; break;
  }	
  
  if (!el) return;    	  
  var handler = this.handlers[ComponentsViewEvents.change];
  //var component = this._getComponent(el.attributes['value'].value);
  var component = this._getComponent(el.attributes['id'].value);
  component.enabled = el.checked;
  handler.fn.apply(handler.scope, [component]);
};

//------------------------------------------------------------------
ComponentsView.prototype._changeComponentsTypeHandler = function(event, target, options) {
  var select = target;  
  var option = select.options[select.selectedIndex];
  
  this._filterServicesList(option.value);
};

//------------------------------------------------------------------
ComponentsView.prototype._filterServicesList = function(type) {
  var extParent = Ext.get(this.extServicesList.query('ul').first());  
  extParent.dom.innerHTML = "";
    
  for (var i=0; i < this.components.length; i++) {
    var component = this.components[i];
    if (component.type === type || type === 'All') {
      this._addComponent(extParent.dom, component);	
    } 
  }
  if (extParent.dom.children.length > 0) {
    extParent.show(); 
    this.extEmptyListMessage.dom.style.display = 'none'; 
  }
  else { 
	this.extEmptyListMessage.dom.innerHTML = eval("Lang.ViewComponents.no_items_" + type);   
	this.extEmptyListMessage.show(); 
    extParent.hide(); 
  }
};

//------------------------------------------------------------------
ComponentsView.prototype.refresh = function() {
	this._loadComponents();  	
};

//------------------------------------------------------------------
ComponentsView.prototype._setComponents = function(components) {
  this.components = components;  
  this._filterServicesList(ComponentTypes.All);
};

//------------------------------------------------------------------
ComponentsView.prototype._loadComponents = function() {
  var successHandler = function(response, request) {
	if (response.responseText.indexOf("ERR_") != -1) {failureHandler.apply(this, [response, request]); return; }
	 var data = Ext.util.JSON.decode(response.responseText);
	 
	 var components = [];	 
	 for (var i=0; i < data.length; i++) {
	   var group = data[i];
	   for (var j=0; j < group.length; j++) {
	     var component = new Service(group[j]);
	     components.push(component);
	   }	    
	 }	 
	 if (components) this._setComponents(components);	
  };
		  
  var failureHandler = function(response, request) {
	Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Action.LoadServices.Failure); 
  };
		  
  Ext.Ajax.request({
    url: Context.Config.Api + "?op=loadfrontcomponents",
	method: 'get',	
	success : successHandler.bind(this),
	failure : failureHandler.bind(this),
 });	
};

//------------------------------------------------------------------
ComponentsView.prototype._getComponent = function(id) {
  var count = this.components.length;
  for (var i=0; i < count; i++) {
    var component = this.components[i];
    if (component.id === id) return component;
  }
  return null;
};

//------------------------------------------------------------------
ComponentsView.prototype._addComponent = function(parent, component) {
  var label_html = '<label for="$1">$2</label>';
  var input_html = '<input type="checkbox" id="$1" value="$2" $3></input>';
  var html = '<li>' + input_html + label_html + '</li>'; 		
  
  html = html.replace(/\$1/g, component.id);
  html = html.replace(/\$2/g, component.name);
  html = html.replace(/\$3/g, (component.enabled)? 'checked' : '');  
  
  parent.innerHTML += html;    
};

