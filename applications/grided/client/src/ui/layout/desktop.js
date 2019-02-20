
Desktop = {};

Desktop.init = function() {  
  var html = translate(AppTemplate.Desktop, Lang.Desktop);    
  document.body.innerHTML = html;
  
  this.extAppLogo = Ext.get('app-logo');    
  this.extAppLogo.on('click', Desktop._clickAppLogoHandler, Desktop);
  
  Desktop.headerPanel     = new HeaderPanel(Ids.Elements.HEADER_PANEL);    
};

//------------------------------------------------------------------------
Desktop.handle = function(event) {
  switch (event.data) {    
    case HomePlace:    
      Desktop.headerPanel.selectItem(Lang.Desktop.home); 
    break;

    case ServersPlace:
    case ServerPlace:
      Desktop.headerPanel.selectItem(Lang.Desktop.servers); 
      break;
      
    case DeploymentPlace:
    case FederationPlace:
    case SpacePlace:
      Desktop.headerPanel.selectItem(Lang.Desktop.deployment); 
    break;      
    
    case ModelsPlace:
    case ModelPlace:
      Desktop.headerPanel.selectItem(Lang.Desktop.models);
    break;    
  } 
};

//------------------------------------------------------------------------
Desktop._clickAppLogoHandler = function(event) {    
  var ev = {name: Events.OPEN_HOME, token: new HomePlace().toString(), data: {}};
  EventBus.fire(ev);     
};
