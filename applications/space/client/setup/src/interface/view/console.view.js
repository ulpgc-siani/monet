function ConsoleView(layername) {
  this.layername = layername;
  this.$layer = $(layername);
  var html = AppTemplate.ViewConsole;
  this.$layer.innerHTML = translate(html, Lang.ConsoleView);
  
  this.importDataPanel = this._getImportDataPanel('import-panel');
  
  this.businessSpace = null;  
  this.bind(layername);
};

//----------------------------------------------------------------------------
ConsoleView.prototype.setTarget = function(model) {  
  this.businessSpace = model;
  this.bindData(model);
};

//----------------------------------------------------------------------------
ConsoleView.prototype.refresh = function() {   
  console.log("SE LLAMA AL REFRESH DEL CONSOLE VIEW");	  
};

//----------------------------------------------------------------------------
ConsoleView.prototype.show = function() {  	  	 
  this.$layer.show(); 
  this._drawRunButton();  
  this.importDataPanel.show();
};

//----------------------------------------------------------------------------
ConsoleView.prototype.hide = function() {
  this.$layer.hide();
};

//----------------------------------------------------------------------------
ConsoleView.prototype.changeApplicationState = function(state) {
  switch (state){
    case Application.States.RUNNING:
      this._drawStopButton();      
      this.importDataPanel.changeApplicationState(state);
    break;	
    case Application.States.STOPPED:
      this._drawRunButton();
      this.importDataPanel.changeApplicationState(state);
    break;
  }			
};

//-------------------------------------------------------------------------------------------------------
ConsoleView.prototype.startLoadImportEvents = function() {
  this.importDataPanel.startLoadImportEvents();
};

//-------------------------------------------------------------------------------------------------------
ConsoleView.prototype.stopLoadImportEvents = function() {
  this.importDataPanel.stopLoadImportEvents();
};

//----------------------------------------------------------------------------	
ConsoleView.prototype.bind = function(layername) {
  this.extParent = Ext.get(layername);
  this.extMessage = this.extParent.select(".message").first();
  this.extRunButton = this.extParent.select(".run-button").first();
  this.extText = Ext.get(this.extParent.query('span[name="state-text"]').first());
  this.extDateText = Ext.get(this.extParent.query('div[name="date-text"]').first());
  this.extDateTextLabel = Ext.get(this.extParent.query('span[name="date-text-label"]').first());
  
  this.extDateTextLabel.hide();  
  this.extMessage.dom.innerHTML = Lang.ConsoleView.message;
  this.extRunButton.on('click', this.runStopHandler, this); 
};

//----------------------------------------------------------------------------
ConsoleView.prototype.bindData = function(model) {    
  if (model.isRunning()){
	this._drawStopButton();  
  }	else {
	this._drawRunButton();  
  }
};

//----------------------------------------------------------------------------
ConsoleView.prototype.runStopHandler = function(event, target, options) {
  event.stopPropagation();
  var action;
  
  if (target.value === Lang.ConsoleView.Run) {	
	action = CommandFactory.getAction("runbusinessspace");	
	action.execute();	
  } 
  else {	      
    action = CommandFactory.getAction("stopbusinessspace");    
    action.execute();
  }
};

//------------------------------------------------------------------------------------
ConsoleView.prototype._getImportDataPanel = function(id) {
  var panel = new ImportDataPanel(id);
  return panel;
};

//----------------------------------------------------------------------------
ConsoleView.prototype._drawStopButton = function() {
  var date = this.businessSpace.getExecutionContext().date;  
  var runningDate = date.format('D d M Y');
  var runningTime = date.format('G:i:s');
    
  this.extRunButton.removeClass("play-button");
  this.extRunButton.addClass("stop-button");    
  this.extRunButton.dom.value = Lang.ConsoleView.Stop;    
  this.extText.dom.innerHTML = Lang.ConsoleView.Running;
  this.extText.removeClass("stopped");
  this.extText.addClass("running");
  this.extDateTextLabel.show();    
  this.extDateText.dom.innerHTML = runningDate + " " + Lang.DateText.At + " " + runningTime;  
};

//----------------------------------------------------------------------------
ConsoleView.prototype._drawRunButton = function() {
  this.extRunButton.dom.value = Lang.ConsoleView.Run;
  this.extText.dom.innerHTML = Lang.ConsoleView.Stopped;
  this.extText.removeClass("running");
  this.extText.addClass("stopped");
  this.extDateText.dom.innerHTML = "";  
  this.extDateTextLabel.hide();
};