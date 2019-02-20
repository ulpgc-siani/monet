function CGPageControl(Layout, sRegion) {
  this.Layout = Layout;
  this.sRegion = sRegion;
  this.aPages = new Array();
}

//---------------------------------------------------------------------
CGPageControl.prototype.existsPage = function(Id) {
  if (! this.aPages[Id]) return false;
  return true;
};

//---------------------------------------------------------------------
CGPageControl.prototype.addPage = function(Id) {
  var Page;

  if (this.aPages[Id]) return false;
  
  bCloseable = (this.aPages.length != 0);

  Page = new Ext.ContentPanel(Id, {autoCreate: true, closable: bCloseable});
  Page.Id = Id;
  this.Layout.add(this.sRegion, Page);
  this.aPages[Id] = Id;

  return true;
};

//---------------------------------------------------------------------
CGPageControl.prototype.deletePage = function(Id) { 
  if (! this.aPages[Id]) return false;
  this.Layout.getRegion(this.sRegion).hidePanel(Id);
  this.Layout.getRegion(this.sRegion).remove(Id);
  delete this.aPages[Id];
  return true; 
};

//---------------------------------------------------------------------
CGPageControl.prototype.setPageTitle = function(Id, sTitle) {
  if (! this.aPages[Id]) return false;
  this.Layout.getRegion(this.sRegion).getPanel(Id).setTitle(sTitle);
  return true;
};

//---------------------------------------------------------------------
CGPageControl.prototype.getActivePage = function() {
  return this.Layout.getRegion(this.sRegion).getActivePanel();
};

//---------------------------------------------------------------------
CGPageControl.prototype.showPage = function(Id) {
  if (! this.aPages[Id]) return false;
  this.Layout.getRegion(this.sRegion).showPanel(Id);
  return true;
};

//---------------------------------------------------------------------
CGPageControl.prototype.hidePage = function(Id) {
  if (! this.aPages[Id]) return false;
  this.Layout.getRegion(this.sRegion).hidePanel(Id);
  return true;
};