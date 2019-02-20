
function WizardDialogUpdateSpace(layername) {
  this.html = AppTemplate.WizardDialogUpdateSpace;
  this.layername = layername;
  this.$layer = $(layername);
  this.$layer.innerHTML = translate(this.html, Lang.WizardDialogUpdateSpace);
      
  this.handlers = {
	'result': null	  
  };
  
  this.bind();
  
  this.wizard = new Wizard();
  this.wizard.addPage(this._createUpdateSpacePage(this.extBody.dom));
  this.wizard.addPage(this._createUpdateModelPage(this.extBody.dom));
  this.$layer.hide();
}

//-----------------------------------------------------------------------------------
WizardDialogUpdateSpace.prototype.show = function(eventName, handler, scope) {
  this.$layer.show();
  this.wizard.start();
};

//-----------------------------------------------------------------------------------
WizardDialogUpdateSpace.prototype.hide = function(eventName, handler, scope) {
  this.$layer.hide();	
};

//-----------------------------------------------------------------------------------
WizardDialogUpdateSpace.prototype.on = function(eventName, handler, scope) {
  this.handlers[eventName] = {fn: handler, scope: scope};	
};

//-----------------------------------------------------------------------------------
WizardDialogUpdateSpace.prototype.un = function(eventName) {
  this.handlers[eventName] = null;	
};

//-----------------------------------------------------------------------------------
WizardDialogUpdateSpace.prototype.bind = function() {
  this.extParent = Ext.get(this.layername);
  this.extTitle  = this.extParent.select('.title').first();
  this.extBody   = this.extParent.select('.dialog-body').first();

  this.extPreviousButton = Ext.get(this.extParent.query('input[name="previous-button"]').first());
  this.extNextButton     = Ext.get(this.extParent.query('input[name="next-button"]').first());
  this.extFinishButton   = Ext.get(this.extParent.query('input[name="finish-button"]').first());
  
  this.extPreviousButton.on('click', this._clickPreviousHandler, this);
  this.extNextButton.on('click', this._clickNextHandler, this);
  this.extFinishButton.on('click', this._clickFinishHandler, this);
  
  this.extPreviousButton.hide();
  this.extNextButton.dom.disable();
  this.extFinishButton.dom.disable();
  
  this.extTitle.dom.innerHTML = Lang.WizardDialogUpdateSpace.title;
};

//-----------------------------------------------------------------------------------
WizardDialogUpdateSpace.prototype.refresh = function() {
  if (this.wizard.isInFirstPage()) {
	this.extFinishButton.dom.disable(); 
    this.extPreviousButton.hide();
    
	if (this.wizard.isCurrentPageCompleted()) {
 	  this.extNextButton.dom.enable();
  	} else {
	  this.extPreviousButton.hide();
	  this.extNextButton.dom.disable();
	}
	return;
  }

  if (this.wizard.isInLastPage()) {
	this.extPreviousButton.show(); 
	this.extNextButton.dom.disable();
	if (this.wizard.isCurrentPageCompleted()) {
		this.extFinishButton.dom.enable();
	} else {
		this.extFinishButton.dom.disable();
    }  
	return;
  } 
      
  this.extPreviousButton.show();
  this.extFinishButton.dom.disable();
  if (this.wizard.isCurrentPageCompleted()) {
    this.extNextButton.dom.enable();
  } else {
	this.extNextButton.dom.disable();    
  }
};

//-----------------------------------------------------------------------------------
WizardDialogUpdateSpace.prototype.submit = function() {
  var action = new CGActionUploadSpaceAndModel();
  action.forms = [this.wizard.pages[1].getForm(), this.wizard.pages[0].getForm()];
  action.execute();
};

//-----------------------------------------------------------------------------------
WizardDialogUpdateSpace.prototype._clickPreviousHandler = function(event, target, options) {
  this.wizard.getCurrentPage().un('change');	
  this.wizard.previous();
  this.wizard.getCurrentPage().on('change', this.refresh, this);
  this.refresh();
};

//-----------------------------------------------------------------------------------
WizardDialogUpdateSpace.prototype._clickNextHandler = function(event, target, options) {
  this.wizard.getCurrentPage().un('change');
  this.wizard.next();
  this.wizard.getCurrentPage().on('change', this.refresh, this);  
  this.refresh();  
};

//-----------------------------------------------------------------------------------
WizardDialogUpdateSpace.prototype._clickFinishHandler = function(event, target, options) {   	
  var handler = this.handlers['result'];
  if (!handler) return;
  handler.fn.call(handler.scope);
  this.wizard.getCurrentPage().un('change');  
};

//-----------------------------------------------------------------------------------
WizardDialogUpdateSpace.prototype._createUpdateSpacePage = function(parent) {
  var page = new UpdateSpacePage(parent);
  page.on('change', this.refresh, this);
  return page;
};

//-----------------------------------------------------------------------------------
WizardDialogUpdateSpace.prototype._createUpdateModelPage = function(parent) {
  var page = new UpdateModelPage(parent);
  page.on('change', this.refresh, this);
  return page;
};


//-----------------------------------------------------------------------------------------
// UpdateSpacePage 
//-----------------------------------------------------------------------------------------

function UpdateSpacePage(parent) {
  this.html = AppTemplate.UpdateSpacePage;	
  this.parent = parent;  
  
  this.handlers = {
    'change': null
  };    
  this.extContainer = Ext.get(document.createElement('div'));
  this.extContainer.set({'id':'update-space-page'});  
};

//-----------------------------------------------------------------------------------
UpdateSpacePage.prototype.show = function() {	
  if (this.extContainer.dom.innerHTML == "") {
	this.extContainer.dom.innerHTML = translate(this.html, Lang.UpdateSpacePage);
    this.parent.appendChild(this.extContainer.dom);
    this.bind();
  }
  this.extContainer.dom.style.display = 'block';       	  
};

//-----------------------------------------------------------------------------------
UpdateSpacePage.prototype.hide = function() {
  this.extContainer.dom.style.display = 'none';
};

//-----------------------------------------------------------------------------------
UpdateSpacePage.prototype.on = function(eventName, handler, scope) {
  this.handlers[eventName] = {fn: handler, scope: scope};	
};

//-----------------------------------------------------------------------------------
UpdateSpacePage.prototype.un = function(eventName) {
  this.handlers[eventName] = null;	
};

//-----------------------------------------------------------------------------------
UpdateSpacePage.prototype.isCompleted = function() {
  if (this.extFile.dom.value !== "") return true;
  return false;
};

//-----------------------------------------------------------------------------------
UpdateSpacePage.prototype.getForm = function() {
  return this.extForm.dom;	
};

//-----------------------------------------------------------------------------------
UpdateSpacePage.prototype.bind = function(eventName) {
  this.extParent  = Ext.get(this.parent);	  
  this.extForm    = Ext.get(this.extContainer.query('form').first());  
  this.extFile    = Ext.get(this.extContainer.query('input[type="file"]').first());
  this.extMessage = this.extContainer.select('.message').first(); 
  
  this.extMessage.dom.innerHTML = Lang.UpdateSpacePage.message;
  this.extFile.on('change', this._changeHandler, this);
};

//-----------------------------------------------------------------------------------
UpdateSpacePage.prototype._changeHandler = function(event, target, options) {
  var handler = this.handlers['change'];
  if (!handler) return;
  handler.fn.call(handler.scope);
};


//-----------------------------------------------------------------------------------------
//UpdateModelPage 
//-----------------------------------------------------------------------------------------

function UpdateModelPage(parent) {
  this.html = AppTemplate.UpdateModelPage;	
  this.parent = parent;
  
  this.handlers = {
    'change': null
  };
  this.extContainer = Ext.get(document.createElement('div'));
  this.extContainer.set({'id':'update-space-page'}); 
};

//-----------------------------------------------------------------------------------
UpdateModelPage.prototype.show = function() {
  if (this.extContainer.dom.innerHTML == "") {
    this.extContainer.dom.innerHTML = translate(this.html, Lang.UpdateModelPage);    
    this.parent.appendChild(this.extContainer.dom);
    this.bind();
  }	
  
  this.extContainer.dom.style.display = "block";
  this.bind();
};

//-----------------------------------------------------------------------------------
UpdateModelPage.prototype.hide = function() {
  this.extContainer.dom.style.display = 'none';
};

//-----------------------------------------------------------------------------------
UpdateModelPage.prototype.on = function(eventName, handler, scope) {
  this.handlers[eventName] = {fn: handler, scope: scope};	
};

//-----------------------------------------------------------------------------------
UpdateModelPage.prototype.un = function(eventName) {
  this.handlers[eventName] = null;	
};

//-----------------------------------------------------------------------------------
UpdateModelPage.prototype.isCompleted = function() {
  if (this.extFile.dom.value !== "") return true;
  return false;
};

//-----------------------------------------------------------------------------------
UpdateModelPage.prototype.getForm = function() {
  return this.extForm.dom;	
};

//-----------------------------------------------------------------------------------
UpdateModelPage.prototype.bind = function(eventName) {
  this.extParent  = Ext.get(this.parent);  
  this.extForm    = Ext.get(this.extContainer.query('form').first());  
  this.extFile    = Ext.get(this.extContainer.query('input[type="file"]').first());
  this.extMessage = this.extContainer.select('.message').first(); 
  
  this.extMessage.dom.innerHTML = Lang.UpdateModelPage.message;
  this.extFile.on('change', this._changeHandler, this);
};

//-----------------------------------------------------------------------------------
UpdateModelPage.prototype._changeHandler = function(event, target, options) {
  var handler = this.handlers['change'];
  if (!handler) return;
  handler.fn.call(handler.scope);
};
