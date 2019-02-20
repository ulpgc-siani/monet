var ServicesView = View.extend({	
	init : function(id) {
	  this._super(id);
	  this.html = AppTemplate.ServicesView;
	  
	  this.servicesTypes = null;
	  this.services = null;
	  this.editor = null;
	  
	  this.initialized = false;
	}
});

//-------------------------------------------------------------------------
ServicesView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};

//-------------------------------------------------------------------------
ServicesView.prototype.getEditor = function() {
  return this.editor;	
};

//-------------------------------------------------------------------------
ServicesView.prototype.setServices = function(services) {  	
  this.services = services;
  if (! this.initialized) this._init();  
  this.refresh();
};

//-------------------------------------------------------------------------
ServicesView.prototype.filterServiceByType = function(type) {
  if (type == ServiceTypes.ALL) this.table.showAll();
  else this.table.filterBy('type', type);	
};

//-------------------------------------------------------------------------
ServicesView.prototype.refresh = function() {
  elements = this._adapt(this.services);
  this.table.setData(elements);	
  
  if (! this.editor) {
    this.editor = this._createEditor(this.services);
  } else {
    this.editor.setServices(this.services);
  }
};

//-------------------------------------------------------------------------
ServicesView.prototype._init = function() {    
  this.extParent = Ext.get(this.id);

  var servicesTypes = [];
  servicesTypes.push({code: ServiceTypes.ALL, name: 'All'});
  servicesTypes.push({code: ServiceTypes.THESAURUS, name: 'Thesaurus'});
  servicesTypes.push({code: ServiceTypes.SERVICE, name: 'Service'});
  servicesTypes.push({code: ServiceTypes.MAP, name: 'Map'});
  servicesTypes.push({code: ServiceTypes.CUBE, name: 'Cube'});
    
  var content = this.merge(this.html, {servicesTypes : servicesTypes});
  this.extParent.dom.innerHTML = content;
  
  this.extMessage = this.extParent.select('.message').first();
  this.extMessage.dom.innerHTML = Lang.ServicesView.text;
  this.extSelect = Ext.get(this.extParent.query('select[name="service_type"]'));
  
  var columns = [PublicationService.NAME];
  var element = this.extParent.select('.elements-table').first();  
  
  this.table = new Table(element, columns, {checkbox: true, clickable : false});  
  this.extSelect.on('change', this._changeServiceTypeHandler, this);
  
  this.initialized = true;  
};

//-------------------------------------------------------------------------
ServicesView.prototype._changeServiceTypeHandler = function(event, target, options) {
  var option = target.options[target.selectedIndex];
  var type = option.value;
  this.presenter.changeServiceType(type);  
};

//-------------------------------------------------------------------------
ServicesView.prototype._createEditor = function(elements) {	
  var editor = new ServicesEditor();
  
  this.table.each('input[type="checkbox"]', function(element, index) {
	  var service = this.services[index];  
	  var checkboxEditor = new CheckboxEditor(element, service, PublicationService.PUBLISHED);	 
	  editor.addEditor(checkboxEditor);
  }, this);
      
  editor.open();
  return editor;  
};

//-------------------------------------------------------------------------
ServicesView.prototype._adapt = function(services) {
  var elements = [];
  this.services.each(function(service, index) {
    var element = {id: service.get(PublicationService.NAME), name: service.get(PublicationService.NAME), type : service.get(PublicationService.TYPE)};
    elements.push(element);
  }, this);
    
  return elements;
};