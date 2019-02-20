CGLayoutBottom = function() {
  this.tabsPanel = $('bottom-layout-tabs');
  this.controlsCreated = false;  
};

CGLayoutBottom.prototype.init = function() {  	
  this.extTabs = new Ext.TabPanel(this.tabsPanel, {layoutOnTabChange: true});
      
  this.createTabs();

  this.spaceTab.activate();
  this.thesaurusTab.disable();
  this.providersTab.disable();    
  this.consoleTab.disable();  
};

CGLayoutBottom.prototype.createTabs = function() {
  this.spaceTab     = new SpaceTab(this.extTabs, Literals.Tabs.Space);
  this.thesaurusTab = new ThesaurusTab(this.extTabs, Literals.Tabs.Thesaurus);
  this.providersTab = new ProvidersTab(this.extTabs, Literals.Tabs.Providers);
  this.consoleTab   = new ConsoleTab(this.extTabs, Literals.Tabs.Console);
  
  this.spaceTab.on('change-tab', this._changeTabHandler, this);
};

//---------------------------------------------------------------------------------------------
CGLayoutBottom.prototype.show = function() {  	
  this.tabsPanel.show();
};

//-----------------------------------------------------------------------------------------
CGLayoutBottom.prototype._changeTabHandler = function(tabId) {
  var tab = this.extTabs.getTab(tabId);
  if (!tab) return;
  tab.activate();   	
};

//-----------------------------------------------------------------------------------------
CGLayoutBottom.prototype.changeApplicationState = function(state) {
  switch (state) {
  case Application.States.UPLOADING_MODEL:
    this.spaceTab.disable();
    break;  
  case Application.States.RUNNING:       
    this.consoleTab.changeApplicationState(state);
    break;
  case Application.States.STOPPED:
	this.consoleTab.changeApplicationState(state);
	break;
  }	
  this.spaceTab.changeApplicationState(state); 
};
