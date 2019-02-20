function ThesaurusTab(extTabs, layername) {
  this.extTabs = extTabs;
  this.layername = layername;
  this.thesaurusTab = this.extTabs.addTab(layername, Lang.BottomLayout.Thesaurus);
  this.thesaurusTab.on('activate', this._activateHandler, this);
  this.initialized = false;
}

//------------------------------------------------------------------------------------
ThesaurusTab.prototype._activateHandler = function() {
  this.thesaurusView.refresh();
};

//------------------------------------------------------------------------------------
ThesaurusTab.prototype._init = function() {
  if (this.initialized) return;
	  
  var html = AppTemplate.ThesaurusTab;
  this.$layer = $(this.layername);
  this.$layer.innerHTML = html;
  this.initialized = true;
   
  this.thesaurusView = this._getThesaurusView(Literals.Views.Thesaurus);
      
  this._renderTab();
};

//------------------------------------------------------------------------------------
ThesaurusTab.prototype.setTarget = function(target) {
  this.thesaurusView.setThesaurusList(target);
};

//------------------------------------------------------------------------------------
ThesaurusTab.prototype.setInitialized = function(value) {
  this.initialized = value;
};

//------------------------------------------------------------------------------------
ThesaurusTab.prototype.activate = function() {
  this.extTabs.activate(this.thesaurusTab.id);	
};

//------------------------------------------------------------------------------------
ThesaurusTab.prototype.enable = function() {
  if (! this.initialized) this._init();	
  this.extTabs.enableTab(this.thesaurusTab.id);	
};

//------------------------------------------------------------------------------------
ThesaurusTab.prototype.disable = function() {
  this.extTabs.disableTab(this.thesaurusTab.id);
};

//------------------------------------------------------------------------------------
ThesaurusTab.prototype.refresh = function() {  
  this._renderTab();	
};

//------------------------------------------------------------------------------------
ThesaurusTab.prototype._renderTab = function() {
  this.thesaurusView.show();
};

//------------------------------------------------------------------------------------
ThesaurusTab.prototype._getThesaurusView = function(id) {
  var view = new ThesaurusView(id);
  return view;
};