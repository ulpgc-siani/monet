
Desktop = new Object;
Desktop.sLayerName = null;
Desktop.layout = null;
Desktop.aSections = new Array();
Desktop.aModes = new Array();

Desktop.TITLE = 'desktop-title';
Desktop.VERSION = 'desktop-version';

Desktop.Title = {
  version : '',
  label : ''
};

Desktop.init = function(sLayerName){

  var html = AppTemplate.Desktop;
  html = translate(html, Lang.Desktop);

  Desktop.sLayerName = sLayerName;
  document.body.innerHTML = html;

  this.initLayout();
};

Desktop.initLayout = function () {  
  if (Desktop.Header) return true;
  
  Desktop.Toolbar       = new Toolbar('toolbar');  
  Desktop.extErrorPanel = Ext.get("error_panel");
  Desktop.titleBar      = new TitleBar('desktop-title-bar');
  Desktop.loadingBar    = $('loading-bar');
  	
  Desktop.Top = new CGLayoutTop();
  Desktop.Top.init();

  Desktop.Bottom = new CGLayoutBottom();
  
  Desktop.loadingBar.show();
  Desktop.titleBar.show();
  return true;
};

Desktop.getMode = function(IdNode){
  if (Desktop.aModes[IdNode] == null) return false;
  return Desktop.aModes[IdNode];
};

Desktop.setMode = function(IdNode, Mode){
  Desktop.aModes[IdNode] = Mode;
};

Desktop.setLayerSize = function (iWidth, iHeight) {
  Desktop.iLayerWidth = iWidth;
  Desktop.iLayerHeight = iHeight;
};

//-----------------------------------------------------------------------------------
Desktop.showLoading = function (){  
  var loading = Desktop.loadingBar.down('.loading');  
  loading.show();
};

//-----------------------------------------------------------------------------------
Desktop.hideLoading = function (){
  var loading = Desktop.loadingBar.down('.loading');
  loading.hide();
};

//-----------------------------------------------------------------------------------
Desktop.modelLoaded = function(model) {	
  Desktop.Bottom.thesaurusTab.enable();
  Desktop.Bottom.spaceTab.enable();
  Desktop.Bottom.spaceTab.activate();  
};

//-----------------------------------------------------------------------------------
Desktop.spaceLoaded = function(space) {

  if (space.isRunning()) {
    Application.setState(Application.States.RUNNING);	 
  } else {
	Application.setState(Application.States.STOPPED);  
  }
  
  Desktop.titleBar.setTarget(space);	
	
  Desktop.Bottom.spaceTab.enable();	
  Desktop.Bottom.spaceTab.setTarget(space);
  Desktop.Bottom.spaceTab.refresh();
  
  Desktop.Bottom.providersTab.enable();
    
  Desktop.Bottom.consoleTab.enable();
  Desktop.Bottom.consoleTab.setTarget(space);
  Desktop.Bottom.consoleTab.refresh();  
};

//-----------------------------------------------------------------------------------
Desktop.spaceUpdated = function(space) {

  if (space.isRunning()) {
    Application.setState(Application.States.RUNNING);	 
  } else {
	Application.setState(Application.States.STOPPED);  
  }
  
  Desktop.titleBar.setTarget(space);	
    
  Desktop.Bottom.spaceTab.enable();	
  Desktop.Bottom.spaceTab.setTarget(space);
  Desktop.Bottom.spaceTab.refresh();
          
  Desktop.Bottom.consoleTab.setTarget(space);
  Desktop.Bottom.consoleTab.refresh();  
};


//-----------------------------------------------------------------------------------
Desktop.onStartImport = function() {
  Desktop.Bottom.consoleTab.consoleView.startLoadImportEvents();	   
};

//-----------------------------------------------------------------------------------
Desktop.onStopImport = function() {
  Desktop.Bottom.consoleTab.consoleView.stopLoadImportEvents();    
};

//-----------------------------------------------------------------------------------
Desktop.showReport = function(sTitle, sSummary, sClass, iMiliseconds) {
  if (! Desktop.extReportContainer) return;

  Desktop.extReportContainer.dom.style.opacity = 100;

  var extTemplate = Desktop.extReportContainer.select(".template").first();
  var extTop      = Desktop.extReportContainer.select(".x-box-tc").first();
  var extMiddle   = Desktop.extReportContainer.select(".x-box-mc").first();
  var extBottom   = Desktop.extReportContainer.select(".x-box-bc").first();
  var extTitle    = Desktop.extReportContainer.select(".title").first();
  var extSummary  = Desktop.extReportContainer.select(".summary").first();

  extTemplate.dom.className = "template " + sClass;
  extTitle.dom.innerHTML = sTitle;
  extSummary.dom.innerHTML = sSummary;

  if (Ext.isIE || Ext.isIE7) {
    extTop.setWidth(extMiddle.getWidth()-9);
    extBottom.setWidth(extMiddle.getWidth()-9);
  }

  Desktop.extReportContainer.alignTo(document, 't-t', [0,1]);
  Desktop.extReportContainer.slideIn('t');

  if (iMiliseconds) {
    if (iMiliseconds != -1) window.setTimeout(Desktop.hideReport.bind(this), iMiliseconds);
  }
  else Desktop.hideReport();
};

Desktop.showError = function(message) {
 var dialog = Ext.MessageBox;
 setTimeout(function() {dialog.alert('Error', message);}, 1000);  
};

Desktop.hideError = function() {
  var parent = Desktop.extErrorPanel.dom;
  $A(parent.childNodes).each(function(e){
	  e.parentNode.removeChild(e);
  });
};

Desktop.hideReport = function() {
  if (!Desktop.extReportContainer) return;
  Desktop.extReportContainer.ghost("t");
};

Desktop.reportProgress = function (sMessage) {
  Desktop.showReport(Lang.Information.Wait, sMessage, 'progress', -1);
};

Desktop.reportError = function (sMessage) {
  Desktop.showReport(Lang.Error.Title, sMessage, 'error', 3000);
};

Desktop.reportWarning = function (sMessage) {
  Desktop.showReport(Lang.Warning.Title, sMessage, 'warning', 3000);
};

Desktop.reportSuccess = function (sMessage) {
  Desktop.showReport(Lang.Information.Title, sMessage, 'success', 3000);
};

Desktop.hideReports = function() {
  Desktop.hideReport();
};

Desktop.showViewsContainer = function () {
  $(Literals.ViewsContainer).show();	
};

Desktop.hideViewsContainer = function () {
  $(Literals.ViewsContainer).hide();
};

Desktop.setTitle = function(title) {
  Desktop.Title = title;
  $(Desktop.TITLE).dom.innerHTML = title.label;
  $(Desktop.VERSION).dom.innerHTML = title.version;  
};

Desktop.refresh = function () {	
  Desktop.Top.refresh();
  Desktop.Bottom.refresh();
};

Desktop.show = function () {  
  Desktop.Top.show();
  Desktop.Bottom.show();
};

//--------------------------------------------------------------------------------
function TitleBar(layername) {
  this.layername = layername;  
  this.bind();
}; 

//--------------------------------------------------------------------------------
TitleBar.prototype.show = function() {  
  this.extParent.show();	
};
//--------------------------------------------------------------------------------
TitleBar.prototype.hide = function() {
  this.extParent.hide();
};

//--------------------------------------------------------------------------------
TitleBar.prototype.setTarget = function(space) { 	
  this.extOrganizationLink.set({'href' : space.organization.url});
  this.extOrganizationLink.set({'title': space.organization.name});
  this.extOrganizationLink.dom.innerHTML = space.organization.name;
  
  this.extBusinessUnitLink.set({'href' : space.businessUnit.url});
  this.extBusinessUnitLink.set({'title': space.businessUnit.name});
  this.extBusinessUnitLink.dom.innerHTML = space.businessUnit.name;
  
  this.extSeparator.show();
};

//--------------------------------------------------------------------------------
TitleBar.prototype.bind = function() {
  this.extParent = Ext.get(this.layername);
  this.extOrganizationLink = this.extParent.select('.org-name').first();
  this.extBusinessUnitLink = this.extParent.select('.unit-name').first();  
  this.extSeparator = Ext.get(this.extParent.query('span[class="separator"]').first());
  this.extSeparator.hide();
};
