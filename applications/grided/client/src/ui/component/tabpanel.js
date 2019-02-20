function TabPanel(element) {
  this.el = element;	
  this.$el = $(element);	
  this.items = {};
  this.activeTab = null;
  
  this._bind();
};

TabPanel.CHANGE_TAB = 'changetab';
TabPanel.ACTIVATE = 'activate';

inherit(TabPanel, EventsSource.js);

//----------------------------------------------------------------------------------------
TabPanel.prototype.show = function() {
  this.$el.show();
  this.activate(this.activeTab.getAttribute("id"));
};

//----------------------------------------------------------------------------------------
TabPanel.prototype.hide = function() {
  this.$el.hide();	
};

//----------------------------------------------------------------------------------------
TabPanel.prototype.addTab = function(tabId, name) {
    
};

//----------------------------------------------------------------------------------------
TabPanel.prototype.getTab = function(itemId) {
  return this.items[itemId];	
};

//----------------------------------------------------------------------------------------
TabPanel.prototype.getActiveTab = function(itemId) {
  return this.activeTab;
};

//----------------------------------------------------------------------------------------
TabPanel.prototype.setActiveTab = function(itemId) {
  this.activate(itemId);	
};

//----------------------------------------------------------------------------------------
TabPanel.prototype._bind = function() {
  this.extParent = Ext.get(this.$el);  
  var tabElements = this.extParent.select('div');
  
  tabElements.each(function(tabElement, index) {
	var id = tabElement.getAttribute('id');
	var tabItem = new TabItem(tabElement, this);
	this.items[id] = tabItem;	
	if (index == 0) this.activeTab = tabItem;
  }, this);
    
};

//----------------------------------------------------------------------------------------
TabPanel.prototype._activateTab = function(itemId) {
  this._hideAll();	
  var tab = this.items[itemId];  
  this.activeTab = tab; 
  tab.show();  
};

//----------------------------------------------------------------------------------------
TabPanel.prototype._hideAll = function() {
  for (var i=0; i < this.items.length; i++) {
    var tab = this.items[i];
    tab.hide();
  }	
};

//----------------------------------------------------------------------------------------
TabPanel.prototype._clickTabHandler = function(event, target, options) {
  if (! target.parentNode == this.el)	return;
  var tabId = target.getAttribute("id");
  this.fire({name: Events.CHANGE_TAB, data:{tabId: tabId}});
};

////---------------------------------------------------------------------------------------
// TabItem
//-----------------------------------------------------------------------------------------

function TabItem(element, parent) {
  TabItem.uber.constructor.call(this, null);	
  this.el = element;
  this.$el = $(element);
  this.parent = parent;
  this._bind();
}

inhertit(TabItem, EventsSource);

TabItem.ACTIVATE = "activate";

//---------------------------------------------------------------------------------------
TabItem.prototype.show = function() {
  this.$el.show();	
};

//---------------------------------------------------------------------------------------
TabItem.prototype.hide = function() {
  this.$el.hide();	
};

//---------------------------------------------------------------------------------------
TabItem.prototype.activate = function() {
	
};

////---------------------------------------------------------------------------------------
//TabItem.prototype._bind = function() {
//  this.extParent = Ext.get(this.el);
//  this.extParent.on('click', this._clickHandler, this);
//};

//---------------------------------------------------------------------------------------
//TabItem.prototype._clickHandler = function(event, target, options) {
//  event.stopPropagation();
//  event.preventDefault();
//  var tabId = target.getAttribute("id");
//  
//  this.parent._activateTab(tabId);
//  this.fire({name: TabItem.ACTIVATE, data: {tabId: tabId}});  
//};


