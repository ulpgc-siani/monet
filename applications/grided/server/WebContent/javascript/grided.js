/*
    Monet Manager Application
    (c) 2009 Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    Manager is free software under the terms of the GNU Affero General Public License.
    For details, see web site: http://www.gnu.org/licenses/
*/

AppTemplate.Toolbar='<div id="account-menu"/>';
AppTemplate.SaveChangesDialog='<div><div class="message">::no_saved_message::</div></div>';
AppTemplate.ServersView='<div class="left-section"><div class="meta italic">::servers::</div><div class="table"></div><div class="toolbar"><input type="button" name="remove_button" value="Delete"></input></div></div><div class="right-section"><div id="add-server-dialog"></div></div>';
AppTemplate.AddServerDialog='<div class="dialog add-server-dialog"><div class="title">::add_servers::</div><form name="add-server" method="post"><div class="meta">::name::</div><input type="text" name="name"></input><div class="meta">::ip::</div><input type="text" name="ip"></input><div class="toolbar"><input type="button" name="add-button" value="::add::"></input></div></form></div>';
AppTemplate.Desktop='<div id="desktop"><div id="notification-bar"></div><div id="dialog-exception"></div><div id="dialog"></div><div id="header-panel"><div id="app-logo">grided</div><div id="navigation-bar"><div class="nav-item"><a href="" name="::home::">::home::</a></div><div class="nav-item"><a href="" name="::servers::">::servers::</a></div><div class="nav-item"><a href="" name="::models::">::models::</a></div><div class="nav-item"><a href="" name="::deployment::">::deployment::</a></div></div><div id="toolbar"><div class="account-menu"><div class="menu-title"></div><div class="menu-body"><ul></ul></div></div></div></div><div id="navigation-panel"></div><div id="content-panel"><div id="servers-view"></div><div id="server-view"></div><div id="deployment-view"></div><div id="federation-view"></div><div id="space-view"></div><div id="models-view"></div><div id="model-view"></div></div></div>';
AppTemplate.ServerState='<div class="state-content"><div class="horizontal-panel"><div class="vertical-panel"><div class="section">::performance::</div><ul><li><div class="meta">::load::</div><div class="value"><%= state.performance.load %></div></li><li><div class="meta">::cpu::</div><div class="value"><%= state.performance.cpu %></div></li><li><div class="meta">::users::</div><div class="value"><%= state.performance.users %></div></li></ul></div><div class="vertical-panel"><div class="section">::tasks::</div><ul><li><div class="meta">::total::</div><div class="value"><%= state.task.total %></div></li><li><div class="meta">::execution::</div><div class="value"><%= state.task.execution %></div></li><li><div class="meta">::sleeped::</div><div class="value"><%= state.task.sleeped %></div></li><li><div class="meta">::stopped::</div><div class="value"><%= state.task.stopped %></div></li><li><div class="meta">::zoombies::</div><div class="value"><%= state.task.zoombies %></div></li></ul></div></div><div class="horizontal-panel"><div class="vertical-panel"><div class="section">::memory::</div><ul><li><div class="meta">::total::</div><div class="value"><%= state.memory.total %></div></li><li><div class="meta">::used::</div><div class="value"><%= state.memory.used %></div></li><li><div class="meta">::available::</div><div class="value"><%= state.memory.available %></div></li></ul></div><div class="vertical-panel"><div class="section">::swap::</div><ul><li><div class="meta">::total::</div><div class="value"><%= state.swap.total %></div></li><li><div class="meta">::used::</div><div class="value"><%= state.swap.used %></div></li><li><div class="meta">::available::</div><div class="value"><%= state.swap.available %></div></li></ul></div></div></div>';
AppTemplate.ServerView='<div class="toolbar"><a href="" class="button" name="back">Servers</a><a href="" class="button" name="save">Save</a><a href="" class="button" name="discard">Discard changes</a></div><div class="top"><div class="meta italic">::server::</div><input type="text" name="name" value="<%= server.name %>"></input></div><div class="bottom"><div class="section-left"><div class="section" name="federations"><div class="title">::federations::</div><div class="section-list"><% if (federations.length>0) { %><ul><% _.each(federations, function(federation) { %><li><a href="" code="<%= federation.id %>"><%= federation.name %></a></li><% }); %></ul><% }; %><% if (federations.length == 0) { %><p>::no_federations::</p><% } %></div></div><div class="section" name="spaces"></div></div><div class="section-right"><div class="message">::server_no_enabled::</div><div class="section"><div class="meta">IP</div><input type="text" name="ip" value="<%= server.ip %>"></input></div><div id="tabs"><div id="state-tab" /><div id="logs-tab" /></div></div></div>';
AppTemplate.SelectServerDialog='<div class="meta">::server::</div><select name="server_id"></select>';
AppTemplate.AddFederationDialog='<div class="dialog add-federation-dialog"><div class="message"></div><div class="title">::add_federations::</div><form name="add-federation" method="post"><div class="meta">::name::</div><input type="text" name="name"></input><div class="meta">::url::</div><input type="text" name="url"></input><div class="toolbar"><input type="button" name="add-button" value="::add::"></input></div></form></div>';
AppTemplate.AddSpaceDialog='<div class="dialog add-space-dialog"><div class="message"></div><div class="title">::add_spaces::</div><form name="add-space" method="post"><div class="meta">::name::</div><input type="text" name="name"></input><div class="meta">::url::</div><input type="text" name="url"></input><div class="meta">::model::</div><select name="models"></select><div class="toolbar"><input type="button" name="add-button" value="::add::"></input></div></form></div>';
AppTemplate.DeploymentView='<div class="loading-spaces">.</div><div id="federations-viewer"><div class="meta italic">::federations::</div><div class="table"></div><div class="toolbar"><input type="button" name="remove_button" value="::remove::"/></div></div><div id="spaces-viewer"><div class="meta italic">::spaces::</div><div class="table"></div><div class="toolbar"><input type="button" name="remove_button" value="::remove::"/></div></div><div id="dialogs-bar"><div class="select-server-dialog"></div><div class="add-federation-dialog"></div><div class="new-space-dialog"></div></div>';
AppTemplate.SpacesSection='<div class="title">::spaces::</div><div class="section-list"><% if (spaces.length>0) { %><ul><% _.each(spaces, function(space) { %><li><a href="" code="<%= space.id %>"><%= space.name %></a></li><% }); %></ul><% }; %><% if (spaces.length == 0) { %><p>::no_spaces::</p><% } %></div>';
AppTemplate.FederationView='<div class="toolbar"><a href="" class="button" name="back">Deployment</a><a href="" class="button" name="save">Save</a><a href="" class="button" name="discard">Discard changes</a></div><div class="top"><div class="meta italic">::federation::</div><input type="text" name="name" value="<%= federation.name %>" disabled></input></div><div class="bottom"><div class="section-left"><div class="section" name="servers"><div class="bar"><div class="title">::server::</div></div><div class="section-list"><a href="" code="<%= federation.server.id %>" name="server"><%= federation.server.name %></a></div></div><div class="section" name="spaces"><div class="title">::spaces::</div><div class="section-list"><% if (spaces.length>0) { %><ul><% _.each(spaces, function(space) { %><li><a href="" code="<%= space.id %>"><%= space.name %></a></li><% }); %></ul><% }; %><% if (spaces.length == 0) { %><p>::no_spaces::</p><% } %></div></div><div class="state"><div>::state::</div><div class="running_time"></div><a href="" class="button" name="start_stop"></a></div></div><div class="section-right"><div class="section-header"><div class="column60"><div class="row"><div>::label::</div><input type="text" name="label" value="<%= federation.label %>"/></div><div class="row"><div>::url::</div><input type="text" name="url" value="<%= federation.url %>" disabled/></div></div><div class="upload-dialog"><div id="upload-federation-image-dialog"></div><img src="?op=downloadimage&filename=<%= federation.image %>&server_id=<%= federation.server.id %>&federation_id=<%= federation.id %>&model_type=0" name="image"></img><div class="toolbar"><a href="" name="change-image">::change::</a></div></div></div><div class="section"><div class="meta">::authentication::</div><div class="row"><input type="checkbox" id="check-user-password" value="::user_password::" /><label for="check-user-password">::user_password::</label></div><div class="row"><div class="meta">::source::</div><select name="source"><option value=\'database\'>Database</option><option value=\'ldap\'>LDAP</option><option value=\'mock\'>Mock</option></select></div><div id="connection-editor"><div id="database-editor"><div class="row"><div>::url::</div><input type="text" name="url" value="" /></div><div class="row"><div>::user::</div><input type="text" name="user" value="" /></div><div class="row"><div>::password::</div><input type="password" name="password" value="" /></div><div class="row"><div>::engine::</div><select name="engine"><option value=\'mysql\'>Mysql</option><option value=\'oracle\'>Oracle</option></select></div></div><div id="ldap-editor"><div class="row"><div>::url::</div><input type="text" name="url" value="" /></div><div class="row"><div>::schema::</div><input type="text" name="schema" value="" /></div><div class="row"><div>::user::</div><input type="text" name="user" value="" /></div><div class="row"><div>::password::</div><input type="password" name="password" value="" /></div><div class="meta level1">::alias::</div><div class="section"><div class="row"><div>CN</div><input type="text" name="cn_field" value="" /></div><div class="row"><div>UID</div><input type="text" name="uid_field" value="" /></div><div class="row"><div>Email</div><input type="text" name="email_field" value="" /></div><div class="row"><div>Lang</div><input type="text" name="lang_field" value="" /></div></div></div></div><div class="row"><input type="checkbox" id="check-certificate" value="::certificate::" /><label for="check-certificate">::certificate::</label></div><div class="row"><input type="checkbox" id="check-openid" value="::openid::" /><label for="check-openid">::openid::</label></div></div></div></div>';
AppTemplate.UploadDialog='<div class="dialog"><form method="post" enctype="multipart/form-data"><input type="file" name="source"></input></form><div name="filename"></div><div class="toolbar"><a href="" class="submit">::submit::</a><a href="" class="close">::close::</a></div></div>';
AppTemplate.ServicesView='<div class="message">::message::</div><select name="service_type"><% _.each(servicesTypes, function(servicesType) { %><option value="<%= servicesType.code %>"><%= servicesType.name %></option><% }); %></select><div><div class="elements-table"></div></div>';
AppTemplate.ServerSection='<div class="title">::server::</div><div><a href="" code="<%= server.id %>" name="server"><%= server.name %></a></div></div>';
AppTemplate.FederationSection='<div class="title">::federation::</div><div><a href="" code="<%= federation.id %>" name="federation"><%= federation.name %></a></div>';
AppTemplate.SpaceView='<div class="toolbar"><a href="" class="button" name="back">Deployment</a><a href="" class="button" name="save">Save</a><a href="" class="button" name="discard">Discard changes</a></div><div class="top"><div class="meta italic">::space::</div><input type="text" name="name" value="<%= space.name %>" disabled></input></div><div class="bottom"><div class="section-left"><div class="section" name="server"></div><div class="section" name="federation"></div><div class="section" name="model"></div><div class="state"><div>::state::</div><div class="running_time"></div><a href="" class="button" name="start_stop"></a></div></div><div class="section-right"><div class="section-header"><div class="column60"><div class="row"><div>::label::</div><input type="text" name="label" value="<%= space.label %>"></input></div><div class="row"><div>::url::</div><input type="text" name="url" value="<%= space.url %>" disabled/></div><div class="row"><% if (space.datawarehouse) { %><input type="checkbox" name="datawarehouse" checked>::use_datawarehouse::</input><% }; %><% if (! space.datawarehouse) { %><input type="checkbox" name="datawarehouse">::use_datawarehouse::</input><% }; %></div></div><div class="upload-dialog"><div id="upload-space-image-dialog"></div><img src="?op=downloadimage&filename=<%= space.logo %>&server_id=<%= space.federation.server.id %>&federation_id=<%= space.federation.id %>&space_id=<%= space.id %>&model_type=1" name="image"></img><div class="toolbar"><a href="" name="change-image">::change::</a></div></div></div><div id="tabs"><div id="publish-tab"/><div id="import-tab"/></div></div></div>';
AppTemplate.ImportSpaceDataView='<div id="import-wizard"><div class="import-dialog dialog"></div><div id="import-progress"></div><div id="import-result"></div></div>';
AppTemplate.ImportSpaceDataDialog='<form name="add-server" method="post" enctype="multipart/form-data"><div class="meta">::importer_type::</div><div><select name="importer_type"></select></div><div class="meta">::space_data_file::</div><div><input type="file" name="source"></input></div><div><div name="filename"></div></div><div class="toolbar"><input type="button" name="import-button" value="::import::"></input></div></form>';
AppTemplate.ImportProgressComposite='<div class="title">::importing::</div><div><div class="meta">::progress::<div class="progress"><%= progress.value %></div></div></div><div><div class="meta">::estimated_time::</div><div class="estimated_time"><%= progress.estimatedTime %></div></div><div class="toolbar"><input type="button" name="stop_button" value="::stop::"></div>';
AppTemplate.ImportResultComposite='<div class="message"></div><div class="duration"></div><div class="toolbar"><input type="button" name="close_button" value="::close::" /></div>';
AppTemplate.ModelsView='<div class="left-section"><div class="meta italic">::models::</div><div class="table"></div><div class="toolbar"><input type="button" name="remove_button" value="Delete"></input></div></div><div class="right-section"><div id="add-model-dialog"></div></div>';
AppTemplate.AddModelDialog='<div class="dialog add-model-dialog"><div class="title">::add_models::</div><form name="add-model" method="post" enctype="multipart/form-data"><div class="meta">::name::</div><input type="text" name="name"></input><div class="meta">::version::</div><input type="file" name="source"></input><div class="toolbar"><input type="button" name="add-button" value="::add::"></input></div></form></div>';
AppTemplate.ModelView='<div class="toolbar"><a href="" class="button" name="back">Models</a></div><div class="top"><div class="meta italic">::model::</div><input type="text" name="name" value="<%= model.name %>" disabled></input></div><div class="bottom"><div id="upload-model-version-dialog"></div><div class="section-header"></div><div class="versions"></div></div>';
AppTemplate.UploadModelVersionDialog='<div class="dialog-body"><div class="message">::message::</div><form method="post" enctype="multipart/form-data"><input type="file" name="source"></input></form><div name="filename"></div></div>';
AppTemplate.ModelVersionsView='<div class="section versions"><div class="toolbar"><div class="meta">::versions::</div><img src="images/grided/collapse-all.png" name="collapse-all" /><img src="images/grided/expand-all.png" name="expand-all" /><a href="" name="upload">::upload_model_version::</a></div><div class="section-body"><ul class="item"></ul></div></div>';
AppTemplate.ModelVersionSpaces='<% if (spaces.length>0) { %><ul><% _.each(spaces, function(space, index) { %><li><input type="checkbox" code="<%= space.id %>" /><a href="" code="<%= space.federation.id %>" type="federation"><%=space.federation.name %></a>-<a href="" type="space" code="<%= space.id %>"><%= space.name %></a></li><% }); %></ul><% if (! model.isLatestVersion(version)) { %><a href="" class="block-action">::upgrade::</a><% }; %><% }; %><% if (spaces.length == 0) { %><p>::no_spaces::</p><% }; %>';
AppTemplate.ModelVersionSpace='<li><input type="checkbox" code="<%= space.id %>" /><a href="" code="<%= space.federation.id %>" type="federation"><%=space.federation.name %></a>-<a href="" type="space" code="<%= space.id %>"><%= space.name %></a></li>';
AppTemplate.ModelVersionView='<li class="collapsed"><img src="images/grided/collapsed-arrow.png" /><input type="checkbox" code="<%= version.id %>" value="<%= version.label %>"><%= version.label %></input><div class="block" code="<%= version.id %>"><div class="block-body"></div></div></li>';
AppTemplate.Table='<div class="table-border"><table class="grided-table" callpadding="0" cellspacing="0"><% if (showHeader) { %><tr class="header"><% _.each(columns, function(header, index) { %><th><%= header %></th><% }); %></tr><% } %><% if (rows.length == 0) %><div class="message"><%= empty_message %></div><% _.each(rows, function(row, index) { %><tr id="<%= row.id %>"><% if (checkbox) { %><td><input id="<%= id %>_<%= index %>" type="checkbox" name="checked"/></td><% }; %><% _.each(columns, function(column, number) { %><% if (clickable)%><td><a name="<%= column %>" href=""><%= row[column] %></a></td><% if (! clickable) %><td><span name="<%= column %>"><label for="<%= id %>_<%= index %>"><%= row[column] %></label></span></td><% }); %><% if (action) { %><td class="action"><a href=""><%= action.name %></a></td><% } %><% if (conditional) { %><% var i = conditional.getIndex(row); %><td class="action"><a href=""><%= conditional.actions[i].name %></a></td><% } %></tr><% }); %></table></div>';
EMPTY = "";
BLANK = " ";
NBSP = "&nbsp;";
ELLIPSE = "...";
DOT = ".";
COMMA = ",";
EQUAL = "=";
QUOTE = "'";
LEFT_BRACKET = "(";
RIGHT_BRACKET = ")";
DOUBLE_QUOTE = '"';
AMP = "&";
DASH = "-";
SLASH = "/";
SHARP = "#";
BR = "<br>";
UL = "<ul>";
_UL = "</ul>";
LI = "<li>";
_LI = "</li>";
SPACE = "&nbsp;";
PX = 'px';
DUMMY = "dummy";
QUESTION = "?";
HTML_DIV = 'div';
HTML_INPUT = 'input';
TEMPLATE_SEPARATOR = "::";

OPTION_YES = "yes";
OPTION_NO = "no";
OPTION_OK = "ok";

BUTTON_RESULT_YES = OPTION_YES;
BUTTON_RESULT_OK = OPTION_OK;
BUTTON_RESULT_NO  = OPTION_NO;

var Ids = {
  Elements : {	
	HEADER_PANEL : "header-panel",
	NAVIGATION_PANEL : "navigation-panel",
	CONTENT_PANEL : "content-panel",
	NOTIFICATIONS_BAR : 'notification-bar',
				
	APP_TOOLBAR : 'toolbar',
	TABS : 'tabs',
	STATE_TAB :'state-tab',	
	PROCESSES_TAB : 'processes-tab',
	LOGS_TAB : 'logs-tab',
	PUBLISH_TAB : 'publish-tab',	
	IMPORT_TAB : 'import-tab',	
	
	SERVERS_VIEW : 'servers-view',
	SERVER_VIEW : 'server-view',
	DEPLOYMENT_VIEW : 'deployment-view',
	FEDERATION_VIEW : 'federation-view',
	SPACE_VIEW : 'space-view',	
	MODELS_VIEW : 'models-view',	
	MODEL_VIEW : 'model-view',
		
	ADD_SERVER_DIALOG : 'add-server-dialog',
	ADD_MODEL_DIALOG : 'add-model-dialog',
	LOAD_MODEL_VERSION_DIALOG : 'load-model-version-dialog',
	DIALOG : 'dialog'
  },
  
  Toolbar : {
    ACCOUNT_MENU: 'account-menu'
  }    
};

var Literals = {
  Data : "DataInit",
  SUCCESS : 'success',
  ERROR : 'error',
  SERVER_ERROR : 'server_error',
  
  Dialogs : {
	  Exception : "dialog-exception"
  },
   
  Exceptions: {
    SESSION_CLOSED : 'session_closed'
  }
  

};

var aMonths = {
  "es" : ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
  "en" : ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
};

var aDays = {
  "es" : ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
  "en" : ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
};





ModelTypes = {
  FEDERATION : 0,
  SPACE : 1
};

ServiceTypes = {
  ALL : '-1',		
  THESAURUS : 'thesaurus',
  SERVICE : 'service',
  MAP : 'map',
  CUBE : 'cube'  
};

DatabaseTypes = {
  MYSQL : 'mysql',
  ORACLE : 'oracle',  
};

ConnectionTypes = {
  DATABASE : 'database',
  LDAP : 'ldap',
  MOCK : 'mock'
};

LDAPAlias = {
  CN : 'cn',
  UID : 'uid',
  EMAIL : 'email',
  LANG : 'lang'		
};

function inherit(c, p) {
  var f = function() {};
  f.prototype = p.prototype;
  c.prototype = new f();
  c.uber = p.prototype;
  c.prototype.constructor = c;
};




(function(){
  var initializing = false, fnTest = /xyz/.test(function(){xyz;}) ? /\b_super\b/ : /.*/;
  
  this.Class = function(){};
  
  
  Class.extend = function(prop) {
    var _super = this.prototype;
    
    
    
    initializing = true;
    var prototype = new this();
    initializing = false;
    
    
    for (var name in prop) {
      
      prototype[name] = typeof prop[name] == "function" && 
        typeof _super[name] == "function" && fnTest.test(prop[name]) ?
        (function(name, fn){
          return function() {
            var tmp = this._super;
            
            
            
            this._super = _super[name];
            
            
            
            var ret = fn.apply(this, arguments);        
            this._super = tmp;
            
            return ret;
          };
        })(name, prop[name]) :
        prop[name];
    }
    
    
    function Class() {
      
      if ( !initializing && this.init )
        this.init.apply(this, arguments);
    }
    
    
    Class.prototype = prototype;
    
    
    Class.prototype.constructor = Class;

    
    Class.extend = arguments.callee;
    
    return Class;
  };
})();

function Menu(element) {
  this.$element = $(element);	
  this.handlers = {};
  this.items = {};
  this.bind();
}


Menu.prototype.setTitle = function(title) {
  var html = '<span>$1</span><span class="arrow"></span>';
  html = html.replace(/\$1/, title);
  this.extTitle.dom.innerHTML = html;	
};


Menu.prototype.show = function() {
  this.$layer.show();	
};


Menu.prototype.hide = function() {
  this.$layer.hide();	
};


Menu.prototype.addItem = function(id, itemName, handler, scope) {
  if (handler) this.handlers[id] = {fn: handler, scope: scope};
  this._addItem(id, itemName, handler);
};


Menu.prototype.bind = function() {
  this.extParent = Ext.get(this.$element);
  this.extTitle  = this.extParent.select('.menu-title').first();
  this.extBody   = this.extParent.select('.menu-body').first();
  this.extUl     = Ext.get(this.extBody.query('ul').first());
  
  this.extBody.hide();
  var body = Ext.get(document.getElementsByTagName('body')[0]);
  body.on('click', this._closeMenuHandler, this);
  this.extTitle.on('click', this._clickMenuHandler, this);
  this.extUl.on('click', this._clickMenuItemHandler, this);
};


Menu.prototype._addItem = function(id, itemName, handler) {
  var elt = document.createElement('li');
  if (handler) {
    var link = document.createElement('a');
    link.setAttribute('id', id);
    link.setAttribute('href', "");
    link.innerHTML = itemName;
    elt.appendChild(link);
  } else {	  
	elt.innerHTML = itemName;
  }
  this.extUl.appendChild(elt);
};


Menu.prototype._closeMenuHandler = function(event, target, options) {
  if (! this.extParent.contains(target)) this.extBody.hide();    	 
};


Menu.prototype._clickMenuHandler = function(event, target, options) {
  this.extBody.setVisible(!this.extBody.isVisible());      	
};

Menu.prototype._clickMenuItemHandler = function(event, target, options) {
  var nodeName = target.nodeName.toLowerCase();  
  if (nodeName !== 'a') return;
  
  event.stopEvent();
  var id = target.attributes['id'].value;  
  var handler = this.handlers[id];
  if (!handler) return;
  handler.fn.apply(handler.scope, [id]);
};

function Toolbar(id) {    
  this.$id = $(id);
  this.menuElement = Ext.get(id).query('.account-menu');
}


Toolbar.prototype.setPanel = function(panel) {
  this.panel = panel;	
};


Toolbar.prototype.setAccount = function(account) {
  this.account = account;
  this._createMenu();
};


Toolbar.prototype._createMenu = function(account) {
  var accountMenu = new Menu(this.menuElement);
  accountMenu.setTitle(this.account.owner_name);
  
  accountMenu.addItem('logout', Lang.LOGOUT, this._logoutHandler, this);  
};


Toolbar.prototype._logoutHandler = function() {
  if (this.panel) this.panel.onLogout();
};

Lang.Desktop = {
  home : 'Home',		
  servers: 'Servers',
  models : 'Models',
  deployment : 'Deployment'		  
};

function HeaderPanel(element) {    
  this.extElement = Ext.get(element);
  this.toolbar = null;
  this.extNavigationBar = null;
  
  this._init();  
};


HeaderPanel.prototype._init = function() {	
  this.toolbar = new Toolbar(Ext.get(Ids.Elements.APP_TOOLBAR));
  this.toolbar.setPanel(this);
  this.service = GridedService;
  this._loadAccount();
	
  this._bind();
};


HeaderPanel.prototype._bind = function() {  
  this.extNavigationBar = Ext.get('navigation-bar'); 
  this.extNavigationBar.on('click', this._clickItemHandler, this);   
};


HeaderPanel.prototype._clickItemHandler = function(event, target, options) {
  event.stopPropagation();
  event.preventDefault();
	  
  var name = target.getAttribute('name');
	  
  switch (name) {
    case Lang.Desktop.home:
      var event = {name: Events.OPEN_HOME, token: new HomePlace().toString(), data:{} };
      EventBus.fire(event);	      
    break;
    case Lang.Desktop.servers:      	
      var event = {name: Events.OPEN_SERVERS, token: new ServersPlace().toString(), data:{} };      
      EventBus.fire(event);
	  break;
    case Lang.Desktop.models:   	     
   	  var event = {name: Events.OPEN_MODELS, token: new ModelsPlace().toString(), data:{} };
      EventBus.fire(event);      
	  break;
	case Lang.Desktop.deployment:
	  var event = {name: Events.OPEN_DEPLOYMENT, token: new DeploymentPlace().toString(), data:{} };	  
	  EventBus.fire(event);
	  break;	
  }
};


HeaderPanel.prototype.selectItem = function(name) {
  var currentItem = this.extNavigationBar.query('a[name="' + name + '"]').first();	
  var items       = this.extNavigationBar.query('a');
  
  items.each(function(item) {
	$(item).removeClassName('selected');
  });
      
  $(currentItem).addClassName('selected');
};


HeaderPanel.prototype._loadAccount = function() {	
  this.service.loadAccount({
	context : this,
	success: function(account) {
	  this.toolbar.setAccount(account);
	},
	failure: function(ex) {
	  throw ex;
	}
  });
};


HeaderPanel.prototype.onLogout = function() {
  this.service.logout({
    context: this,
    success: function() {console.log("logout");},
    failure: function() {console.log("imposible hacer el logout");}
  });	
};

var Listener = Class.extend({
	init : function() {
		
	}
});

Listener.prototype.notify = function(event) {
  throw new Error('notify must be override');  
};

var EventsSource = Class.extend({
  init : function() {
    this.listeners = {};	  
  }	
});


EventsSource.prototype.on = function(eventName, handler, scope) {
  if (!this.listeners[eventName]) this.listeners[eventName] = [];        
  this.listeners[eventName].push({scope: scope, fn: handler});
}; 


EventsSource.prototype.un = function(eventName, handler) {
  if (!this.listeners[eventName]) return;
    
  if (handler) {     
    for (var i=0; i < this.listeners[eventName].length; i++) {
      var listener = this.listeners[eventName][i];
      if (listener.fn === handler) this.listeners[eventName].splice(i, 1);
    }
  }
  else { this.listeners[eventName] = []; delete this.listeners[eventName]; }
};


EventsSource.prototype.fire = function(event) {
  if (!this.listeners[event.name]) return;

  for (var i=0; i < this.listeners[event.name].length; i++) {
    var listener = this.listeners[event.name][i];
    listener.fn.notify.call(listener.scope, event);
  }
};

var Dialog = EventsSource.extend({
  init : function(element) {
	this._super();
	this.$el = $(element);	  
  }
});


Dialog.prototype.open = function() {
  this.$el.show();	
};


Dialog.prototype.close = function() {
 this.$el.hide();  
};


Dialog.prototype.getData = function() {
  throw new Error('this method has to be overrided');
};


Dialog.prototype.setFocus = function() {
  throw new Error('this method has to be overrided');
};

Lang.SaveChangesDialog = {
  no_saved_message : 'Changes no saved',
  title: 'Save changes'  
};

var SaveChangesDialog = Dialog.extend({
	
	init : function(element) {
	  this._super(element);
	  this.html = translate(AppTemplate.SaveChangesDialog, Lang.SaveChangesDialog);
	  
	  this._init();
	}
});


SaveChangesDialog.prototype.open = function() {
  this.extDialog.show();
  this.extDialog.focus();
};


SaveChangesDialog.prototype.close = function() {
  this.extDialog.hide();	
};


SaveChangesDialog.prototype.addButton = function(buttonName, handler, scope) {
  this.extDialog.addButton(buttonName, handler, scope);	
};
 

SaveChangesDialog.prototype._init = function() {
  this.extDialog = this._createDialog();
  this.extDialog.header.dom.innerHTML = Lang.SaveChangesDialog.title;
  this.extDialog.body.dom.innerHTML = this.html;
};


SaveChangesDialog.prototype._createDialog = function() {
  var dialog = new Ext.BasicDialog(this.$el, {
    modal: true,
    shadow: false,
    width: 300,
    height: 160,
    minWidth: 300,
    minHeight: 150,
    minimizable: false,
  });
  return dialog;
};

var Activity = Listener.extend({
	init : function() {
		
	}
});

Activity.prototype.start = function(data) {
  throw new Error("Activity start method has to be overrided");
};

Activity.prototype.stop = function() {
  throw new Error("Activity stop metohd has to be overrided");
};

Activity.prototype.canStop = function(callback) {
  throw new Error("Activity stop method has to be overrided");
};

ActivityManager = {
  currentActivity : null,
};


ActivityManager.start = function(activity, data) {    
  
  if (! ActivityManager.currentActivity) ActivityManager.doStart(activity, data);
  
  else {	  
    ActivityManager.currentActivity.canStop({
      context: ActivityManager,
    
      success: function() {      
        ActivityManager.currentActivity.stop();      
        ActivityManager.doStart(activity, data);
      },

      failure : function() {
    	alert("Activity was not stopped");
      }   
    });   
  }
};


ActivityManager.doStart = function(activity, data) {
  ActivityManager.currentActivity = activity;
  activity.start(data);  	
};

var View = Listener.extend({
  init : function(id) {
	this._super(id);
	this.id = id;
	this.$id = $(id);
  }
});


View.prototype.setPresenter = function(presenter) {
	throw new Error('View setPresenter method has to be overrided');
};


View.prototype.show = function() {
  this.$id.show(); 
};


View.prototype.hide = function() {
  this.$id.hide();	
};


View.prototype.destroy = function() {
};


View.prototype.merge = function(template, context) {
	return _.template(template, context);
};

var Model = EventsSource.extend({
	init : function() {
	  this._super(arguments);
	  this.id = '';
	  this.validations = {};
	  this.error = null;
	} 
});


Model.prototype.get = function(propertyName) {
  return this[propertyName];	
};


Model.prototype.set = function(propertyName, value) {
 var oldValue = this[propertyName];  
 if (! this.validate(propertyName, value)) return false;
 
 this[propertyName] = value;

 this.fire({name: Events.CHANGED, propertyName: propertyName, data: {oldValue: oldValue, newValue: value, model: this}});
 return true;
};


Model.prototype.add = function(collectionName, value) {
  if (! this[collectionName]) this[collectionName] = [];
  this[collectionName].push(value);
  
  this.fire({name: Events.ADDED, collectionName: collectionName, value:value});   
};


Model.prototype.remove = function(collectionName, value) {
  if (! this[collectionName]) return false;
  var index = this[collectionName].indexOf(value);
  if (index == -1) return false;
  var result = this[collectionName].splice(index, 1);
  if (result.length <= 0) return false;

  this.fire({name: Events.REMOVED, collectionName: collectionName, value:value});  
  return true;
};


Model.prototype.validate = function(propertyName, value) {  
  var validator = this.validations[propertyName];
  if (!validator) return true;
  
  if (! validator.validate(propertyName, value)) {
  	this.error = validator.getError();
  	return false;
  } 
  return true;
};

var Server = Model.extend({
  init: function() {
    this._super();
    this.federations = new Federations();
  }
});


Server.ID = 'id';
Server.NAME  = 'name';
Server.IP = 'ip';
Server.FEDERATIONS = 'federations',
Server.STATE = 'state';
Server.ENABLED = 'enabled';
Server.SERVER_STATE = "server_state";



var ServerState = Model.extend({
  init : function() {
    this._super();
  }
});

ServerState.PERFORMANCE = 'performance';
ServerState.TASK   = 'task';
ServerState.MEMORY = 'memory';
ServerState.SWAP   = 'swap',
ServerState.LOG    = 'log';

Lang.ServersView = {
  open : 'Open',
  no_servers: 'No servers',
  servers : 'Servers'  
};

Lang.AddServerDialog = {
  add_servers : 'Add servers',
  name : 'Name',
  ip : 'IP',
  add: 'Add'
};

var AddServerDialog = Dialog.extend({
  init : function(element) {
    this._super(element);
    this.html = translate(AppTemplate.AddServerDialog, Lang.AddServerDialog);
    element.innerHTML = this.html;
    
    this._bind();
  }
});
AddServerDialog.CLICK_ADD_EVENT = 'click-add';


AddServerDialog.prototype.getData = function() {      
  var data = {
    name : this.extName.dom.value,
    ip : this.extIP.dom.value
  };
  return data;    	
};


AddServerDialog.prototype.check = function() {
  if (this.extName.dom.value.length == 0 || this.extIP.dom.value.length == 0) {
    this.error = Lang.AddServerDialog.have_to_complete_info || "poner el mensaje a mostrar en AddSeverDialog";
    return false;
  }
  this.error = null;
  return true;
};


AddServerDialog.prototype.showError = function() {
  if (this.error) alert(this.error); 
  this.error = null;
};


AddServerDialog.prototype.clear = function() {
  this.extName.dom.value = "";
  this.extIP.dom.value = "";
  this.extAddButton.dom.disabled = true;
};


AddServerDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.$el); 	
  this.extName = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extIP   = Ext.get(this.extParent.query('input[name="ip"]').first());
  this.extAddButton = Ext.get(this.extParent.select('input[name="add-button"]').first()); 
  
  this.extName.on('keyup', this._changeHandler, this);
  this.extIP.on('keyup', this._changeHandler, this);
  this.extAddButton.on('click', this._addButtonHandler, this);
  this.extParent.on('keypress', this._keyPressHandler, this);
  
  this.extAddButton.dom.disabled = true;
};


AddServerDialog.prototype._addButtonHandler = function() {
  var event = {name: AddServerDialog.CLICK_ADD_EVENT, data: this};	
  this.fire(event);
  this.clear();
};


AddServerDialog.prototype._changeHandler = function(event, target, options) {
  this.extAddButton.dom.disabled = true;
  if (this._validate()) this.extAddButton.dom.disabled = false;    
};


AddServerDialog.prototype._keyPressHandler = function(event, target, options) {
  if (event.keyCode != Ext.EventObject.ENTER) return;
  this._addButtonHandler();
};


AddServerDialog.prototype._validate = function(event, target, options) {
  if (this.extName.dom.value.length > 0 && this.extIP.dom.value.length > 0) return true;
  else return false;
};

var ServersView = View.extend({
  init : function(id) {
    this._super(id);
    this.html = translate(AppTemplate.ServersView, Lang.Buttons);
    this.html = translate(this.html, Lang.ServersView);
    this.servers = [];
    this.presenter = null;
    
    this.initialized = false;
  }
});


ServersView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};


ServersView.prototype.setServers = function(servers) {
  this.servers = servers;  
  if (!this.initialized) this._init();
  
  this._refresh();
};


ServersView.prototype.showAddServerDialog = function() {
  if (! this.addServerDialog) {
    this.addServerDialog = new AddServerDialog(this.extAddServerDialog.dom);
    this.addServerDialog.on(AddServerDialog.CLICK_ADD_EVENT, {
    	notify : function(event)  {    	  	
    	  var dialog = event.data;
    	  if (! dialog.check()) { dialog.showError(); return; }
    	  var data = dialog.getData();
    	  this.presenter.addServer({name: data.name, ip: data.ip});
    	}
    }, this);
  }
  this.addServerDialog.open();  
};


ServersView.prototype.show = function() {
  this.showAddServerDialog();
  this.$id.show();
};


ServersView.prototype.hide = function() {
  this.hideAddServerDialog();
  this.$id.hide();
};


ServersView.prototype.hideAddServerDialog = function() {
  if (! this.addServerDialog) return;  
  this.addServerDialog.close();
};


ServersView.prototype.clearAddServerDialog = function() {
  if (this.addServerDialog) this.addServerDialog.clear();	
};


ServersView.prototype._refresh = function() {
  this.table.setData(this.servers); 
  this._refreshToolbar();
};


ServersView.prototype._init = function(event) {
  this.extParent = Ext.get(this.id);    
  this.extParent.dom.innerHTML = this.html;
  
  this.extRemoveButton = Ext.get(this.extParent.query('input[name="remove_button"]').first());
  this.extAddServerDialog = Ext.get(Ids.Elements.ADD_SERVER_DIALOG);
	  
  var columns = [Server.NAME];  
  var element = this.extParent.query('.table').first();
	   
  this.table = new Table(element, columns, {checkbox: true, clickable: true, empty_message: Lang.ServersView.no_servers});  
  this.table.setData(this.servers);
  
  $(this.extRemoveButton.dom).hide();
  
  this._bind();
  
  this.initialized = true;
};


ServersView.prototype._bind = function(extParent) {        
   this.extRemoveButton.on('click', this._removeHandler, this);
   
   this.table.on(Table.CLICK_ROW, {notify: this._openHandler}, this);
   this.table.on(Table.CHECK_ROW, {notify: this._checkHandler}, this);   
};


ServersView.prototype._removeHandler = function(event, target, options) {  
  var rows = this.table.getSelectedRows();
  var ids = [];
  $(rows).each(function(row) { ids.push(row.id); });
  this.presenter.removeServers(ids);
};


ServersView.prototype._openHandler = function(event) {  
  var row = event.data.row;		
  this.presenter.openServer(row.id);
};


ServersView.prototype._checkHandler = function(event) {
  this._refreshToolbar();
};


ServersView.prototype._refreshToolbar = function(event) {
  var rows = this.table.getSelectedRows();
  if (rows.length > 0) 
    $(this.extRemoveButton.dom).show();
  else
    $(this.extRemoveButton.dom).hide();	
};

var Collection = Model.extend({
	init : function() {
	  this._super();
	  this.models = {};
	}
});


Collection.prototype.add = function(model, options) {
  this.models[model.id] = model;
  if (! options || !options.silent) this.fire({name: Events.ADDED, data: {model: this, collection: this, item: model}});
};


Collection.prototype.remove = function(ids, options) {
  if (_.isArray(ids)) {
    for (var i=0; i < ids.length; i++) {
      var id = ids[i];	
	  delete this.models[id];	  
    }
  }	  	
  if (! options || !options.silent) this.fire({name: Events.REMOVED, data: {model : this, collection: this.models}}); 
};


Collection.prototype.getById = function(id) {
  return this.models[id];	
};


Collection.prototype.get = function(index) {
  var array = this.toArray();
  return array[index];
};


Collection.prototype.include = function(federation) {
  return _(this.toArray()).include(federation);
};


Collection.prototype.size = function() {
  if (Object.keys) return Object.keys(this.models).length;
  var count = 0;
  for (var i in this.models) {
    var model = this.models[i];
    if (this.models.hasOwnProperty(model)) count++;
  }
  return count;
};


Collection.prototype.each = function(callback, scope) {
  var size = this.size();	
  for (var i=0; i < size; i++) {
	var item = this.get(i);  
    callback.apply(scope, [i, item]);	  
  }	
};


Collection.prototype.clear = function() {
  this.models = {};
};


Collection.prototype.toArray = function() {
  var array = [];
  for (var i in this.models) {
    var model = this.models[i];
    array.push(model);
  }
  return array;	
};

var Servers = Collection.extend({
  init : function() {
	this._super();
  }
});

var ServersActivity = Activity.extend({
  init : function() {
	this.view = this._createView();	
	this.view.setPresenter(this);
	this.service = GridedService;
	this.serversCollection = null;	  
  }
});
	

ServersActivity.prototype.start = function(data) {
  if (! this.view) this.view = this._createView();
  
  if (this.serversCollection) {
    this._link(this.serversCollection);
    this._showView();    
  }
  
  if (! this.serversCollection) this.service.loadServers({
    context: this,
    success: function(serversCollection) {
      this.serversCollection = serversCollection;
      this.view.setServers(this.serversCollection.toArray());
      this._link(this.serversCollection);
      this._showView();
    },
    failure: function(ex) {
      throw ex; 
    }
  });
};


ServersActivity.prototype.stop = function() {
  this._unlink(this.serversCollection);	  
  this.view.hide();
};


ServersActivity.prototype.canStop = function(callback) {
  callback.success.call(callback.context, {});
};


ServersActivity.prototype.notify = function(event) {
  var serversCollection = event.data.model;  
  this.view.setServers(serversCollection.toArray());   
};


ServersActivity.prototype.openServer = function(id) {
  var event = {name: Events.OPEN_SERVER, token: new ServerPlace(id).toString(), data: {id:id}};
  EventBus.fire(event);
};


ServersActivity.prototype.addServer = function(data) {
  var server = new Server();
  server.set(Server.NAME, data.name);
  server.set(Server.IP, data.ip);
  
  NotificationManager.showNotification(Lang.Notifications.ADD_SERVER);
  
  this.service.addServer(server, {
	  context: this,
	  success : function(server) {
	    this.serversCollection.add(server);	
	    this.view.clearAddServerDialog();
	    NotificationManager.showNotification(Lang.Notifications.SERVER_ADDED, 1000);
	  },
	  failure : function(ex) {
	    NotificationManager.showNotification(Lang.Notifications.ADD_SPACE_ERROR, 1000);
	    throw ex;
	  }
  });  
};


ServersActivity.prototype.removeServers = function(ids) {    
  NotificationManager.showNotification(Lang.Notifications.REMOVING, 1000);
  
  var serverIds = ids; 	
  this.service.removeServers(serverIds, {
    context: this,
    success: function(ids) {    	
       this.serversCollection.remove(serverIds);
       NotificationManager.showNotification(Lang.Notifications.REMOVED, 1000);
    },
    failure : function(ex) {
      NotificationManager.showNotification(Lang.Notifications.REMOVE_ERROR, 1000);
      throw ex;
    }
  });	
};


ServersActivity.prototype.handle = function(event) {
  switch (event.name) {
    case Events.SERVER_SAVED: this.serversCollection = null; break;
  }
};


ServersActivity.prototype._link = function(model) {
  model.on(Events.ADDED, this, this);  
  model.on(Events.REMOVED, this, this);
};


ServersActivity.prototype._unlink = function(model) {
  if (!model) return;
  model.un(Events.REMOVED, this);
  model.un(Events.ADDED, this);
};


ServersActivity.prototype._createView = function(id, html) {
  var view = new ServersView(Ids.Elements.SERVERS_VIEW);
  return view;
};


ServersActivity.prototype._showView = function() {
  this.view.show();  
};



Lang.Buttons = {
  accept : "Aceptar",
  add   : "Añadir",
  back   : "Back",
  discard : 'Discard',
  cancel : "Cancelar",
  continue_editing : 'Continue Editing',
  close : "Cerrar",
  finish: "Finalizar",
  next: "Siguiente",
  load   : 'Load',
  open   : "Abrir",
  previous: "Anterior",
  remove : "Eliminar", 
  reset  : "Resetear",
  save   : 'Save',
  spaces : 'Spaces',	 
  start  : 'Start',
  stop   : 'Stop',
  upload : 'Upload'
};

Lang.Response = {
  yes : "Yes",
  no : "No"
};

Lang.Warning = {
  title: "Advertencia"
};

Lang.Error = {
  title: "Error"
};

Lang.Information = {
  title: "Informacion",
  wait: "Por favor, espere"
};

Lang.DateText = {
  at : "a las"
};

Lang.Exceptions = {
  Title : "Error",

  Default : "Unknow error",

	LoadOperation : "Load application was Imposible ",

  Request : {
    Title: "Error: Notify of error",
    Description: "<b>Se ha producido un error al intentar procesar la solicitud.</b><br/>Póngase en contanto con el Servicio Técnico e informe de este error:<br/><br/>"
  },

  Internal : {
    Title: "",
    Description: "<b>Se ha producido un error al intentar procesar la solicitud.</b><br/>Pongase en contanto con el Servicio Tecnico e informe de este error:<br/><br/>"
  },

  SessionExpired : "Por motivos de seguridad, la sesión tiene un límite de tiempo. Ese límite de tiempo se ha superado. Vuelva a iniciar sesión.",
  ServiceStopped : "El servicio ha sido parado. Contacte con el administrador para más información.",
  InitApplication : "Se ha producido un error al intentar iniciar su sesión. Por favor, contacte con el administrador del sistema.",
  
  DuplicateModelVersion : "This version already exists in the model"
    
    
    
};

Desktop = {};

Desktop.init = function() {  
  var html = translate(AppTemplate.Desktop, Lang.Desktop);    
  document.body.innerHTML = html;
  
  this.extAppLogo = Ext.get('app-logo');    
  this.extAppLogo.on('click', Desktop._clickAppLogoHandler, Desktop);
  
  Desktop.headerPanel     = new HeaderPanel(Ids.Elements.HEADER_PANEL);    
};


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


Desktop._clickAppLogoHandler = function(event) {    
  var ev = {name: Events.OPEN_HOME, token: new HomePlace().toString(), data: {}};
  EventBus.fire(ev);     
};

var History = {};

History.init = function() {
  dhtmlHistory.initialize();
  dhtmlHistory.fireOnNewListener = false;
};

History.addListener = function(listener) {
  this.listener = listener;
  dhtmlHistory.addListener(listener);
};

History.getLocation = function() {
  return dhtmlHistory.getCurrentLocation();
};

History.newLocation = function(location, data) {
  dhtmlHistory.add(location, data);
  if (this.listener) this.listener.apply(null, [location, data]);
};

History.goBack = function() {
  history.go(-1);
};

EventBus = {
  listeners : {},
  
  on : function(eventName, handler, scope) {
    if (!this.listeners[eventName]) this.listeners[eventName] = [];        
	   this.listeners[eventName].push({scope: scope, fn: handler});
  }, 

  un : function(eventName, handler) {
   if (! this.listeners[eventName]) return;
	    
   if (handler) {     
     for (var i=0; i < this.listeners[eventName].length; i++) {
	   var listener = this.listeners[eventName][i];
	   if (listener.fn === handler) this.listeners[eventName].splice(i, 1);
	 }
   }
   else { this.listeners[eventName] = []; delete this.listeners[eventName]; }
  },

  fire : function(event) {
    if (! this.listeners[event.name]) return;

	for (var i=0; i < this.listeners[event.name].length; i++) {
	  var listener = this.listeners[event.name][i];
	  listener.fn.call(listener.scope, event);
    }
  }
};

var Router = {
  routes  : {},
  patterns : [],

  getPattern : function(queryString) {
    var result = null; 
      for (var i=0; i < this.patterns.length; i++) {
        var pattern = this.patterns[i];
        if (queryString.match(pattern)) {result = pattern; break;}
      }
     return result;
  },
 
  addRoute : function(pattern, placename) {
    this.patterns.push(pattern);
    this.routes[pattern] = placename;
  },

  get : function(queryString) {
    var pattern = this.getPattern(queryString);
    if (!pattern) return null;
    return this.routes[pattern];
  }     
};

var LocationParser = {
  parse : function(location) {
    var dataPattern = /\d+$/;    
    var data  = location.match(dataPattern);     
    return (data)? data[0] : '';
  }
};

function HomePlace() {
  this.toString = function() {
    return HomePlace.token;     
  };
};
HomePlace.token = 'home/';

function ServersPlace() {
  this.toString = function() {
    return ServersPlace.token;     
  };
};
ServersPlace.token = 'servers/';


function ServerPlace(id) {
  this.toString = function() {
    return ServersPlace.token + id;     
  };
};

function ModelsPlace() {
  this.toString = function() {
    return ModelsPlace.token;     
  };
};
ModelsPlace.token = 'models/';

function ModelPlace(id) {
  this.toString = function() {
    return ModelsPlace.token + id;     
  };
};

function DeploymentPlace() {
  this.toString = function() {
    return DeploymentPlace.token;     
  };
};
DeploymentPlace.token = "deployment/";

function FederationPlace(id) {
  this.toString = function() {
	  return FederationPlace.token + id;
  };	
};
FederationPlace.token = 'deployment/federations/';


function SpacePlace(id) {
  this.toString = function() {
    return SpacePlace.token + id;
  };
};
SpacePlace.token = 'deployment/spaces/';

ChangePlaceEventHandlers = {};
ChangePlaceEventHandlers.handle = function(event) {
  History.newLocation(event.token, event.data);
  
};









ServerSavedHandler = {};
ServerSavedHandler.handle = function(event) {  
   var activity = ActivityMapper.get(ServersPlace);
   activity.handle(event);	
};








Events = {
  ADDED : 'added',
  CHANGED : 'changed',
  REMOVED: 'removed',
  CLICK_MENU_ITEM : 'click',
  RESTORED : 'restored',
  
  OPEN_HOME    : 'open_home', 
  OPEN_SERVERS : 'open_servers',
  OPEN_SERVER  : 'open_server',
  OPEN_MODELS  : 'open_models',
  OPEN_MODEL   : 'open_model',
  OPEN_DEPLOYMENT   : 'open_deployment',
  OPEN_FEDERATION : 'open_federation',
  OPEN_SPACE : 'open_space',
  
  FEDERATION_SAVED: 'federation_saved',
  SERVER_SAVED: 'server_saved',
  SPACE_SAVED: 'space_saved',
  MODEL_SAVED: 'model_saved'
};

Lang.Notifications = {
  ADD_SERVER : 'Adding server...',
  SERVER_ADDED : 'Server added',
  ADD_SERVER_ERROR : 'Error adding server',
  SERVER_SAVED : 'Server saved successfully',
  SAVE_SERVER_ERROR : 'Error saving server',
    
  ADD_SPACE : 'Adding space...',
  SPACE_ADDED : 'Space added',
  ADD_SPACE_ERROR : 'Error adding space',
  SPACE_SAVED : 'Space saved successfully',
  SAVE_SPACE_ERROR : 'Error saving space',

  ADD_FEDERATION : 'Adding federation...',
  FEDERATION_ADDED : 'Federation added',
  ADD_FEDERATION_ERROR : 'Error adding federation',
  FEDERATION_SAVED : 'Federation saved successfully',
  SAVE_FEDERATION_ERROR : 'Error saving federation',
   
  REMOVING : 'Removing ...',
  REMOVED : 'Removed',
  REMOVE_ERROR : 'Error removing items ...',
  
  STARTING : 'Starting...',
  STARTED : 'Started',  
  STOPPING : 'Stopping...',
  STOPPED : 'Stopped'    
};

var NotificationManager = {
  id : '',
   
  showNotification : function(message, time) {
    this.extNotificationBar.dom.innerHTML = message;
    $(this.extNotificationBar.dom).show();
    
    if (time) {           
      var context = this;
      setTimeout(function() { context.hideNotification();}, time);       
    }
  },
  
  hideNotification : function() {
    this.extNotificationBar.dom.innerHTML = "";
    $(this.extNotificationBar.dom).hide();
  },
 
  init : function(id) {
    this.extNotificationBar = Ext.get(id);  
  }  
};

var HomeActivity = Activity.extend({
  init : function() {
	  
  }	
});


HomeActivity.prototype.start = function(data) {
	alert("TODO not implemented home activity");
};


HomeActivity.prototype.stop = function() {
	
};


HomeActivity.prototype.canStop = function(callback) {
	callback.success.call(callback.context, {});
};

Lang.ServerView = {
  name : 'Name',
  ip : 'IP address',
  federations : 'Federations',
  no_federations: 'None federations in this server',
  state : 'State',    
  spaces : 'Spaces',
  no_spaces : 'None spaces in this server',
  log : 'Log',
  performance : 'performance',
  load : 'load',
  cpu : 'cpu',
  users : 'users',
  tasks : 'tasks',
  total : 'total',
  execution : 'execution',
  sleeped : 'sleeped',
  stopped : 'stopped',
  zoombies : 'zoombies',
  memory : 'memory',
  total : 'total',
  used : 'used',
  available : 'available',
  swap : 'swap',
  server : 'Server',
  server_no_enabled : "This server is disabled. You have to fill server's name and ip to enable it"
};

var Editor = EventsSource.extend({
  init : function() {    
	this._super();
    this.state = Editor.CLOSED;	
    this.editors = [];
  }	
});

Editor.CLOSED = 0;
Editor.OPENED = 1;
Editor.CHANGED = 2;


Editor.prototype.open = function() {	  
  if (this.options.type == 'single') { this.state = Editor.OPENED; return; }
	  
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	editor.open();
  }
	  
  this.state = Editor.OPENED; 
};


Editor.prototype.close = function() {
  if (this.options.type == 'single') { this.state = Editor.STOPPED; return; }
  
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	editor.close();
  }
  
  this.state = Editor.CLOSED;
};


Editor.prototype.isClosed = function() {
  return this.state === Editor.CLOSED;  	
};


Editor.prototype.isDirty = function() {
  if (this.options.type == 'single') { throw new Error('isDirty method has to be overrided in editor'); return; }
  
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
    if (editor.isDirty()) return true;
  }
  return false;
};


Editor.prototype.flush = function() {
  if (this.options.type == 'single') { throw new Error('flush method has to be overrided in editor'); return; }

  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
    if (editor.isDirty()) editor.flush();
  }
};


Editor.prototype.reset = function() {
  if (this.options.type == 'single')  { throw new Error('reset method has to be overrided in editor'); return; }
  
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	editor.reset();
  }	   
};


Editor.prototype.showError = function(error) {
  if (this.options.type == 'single')  { throw new Error('reset method has to be overrided in editor'); return; }    
  
  var editor = this.getEditor(error.propertyName);
  if (editor) editor.showError(error);    
};


Editor.prototype.getEditor = function(propertyName) {
  if (this.options.type == 'single')  return null;
  
  for (var i=0, l = this.editors.length; i < l; i++) {
	  var editor = this.editors[i];
	  if (editor.propertyName === propertyName) return editor;	
  }    
  return null;
};

var ServerEditor = Editor.extend({
  init : function( options) {
	this._super();  
    this.server = null;
    this.options = options || {};
    this.options.type = 'composed';
    
    this.nameEditor = null;
    this.ipEditor   = null;
  }	
});


ServerEditor.prototype.open = function(server) {
  this.server = server;
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	  editor.open();
  }  
  this.state = Editor.OPENED;
};


ServerEditor.prototype.close = function(server) {
  this.server = null;
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	editor.close();
  }  
  this.state = Editor.CLOSED;
};


ServerEditor.prototype.setServer = function(server) {
  this.server = server;
};


ServerEditor.prototype.addName = function(elt, propertyName) {
  this.nameEditor = new TextEditor(elt, this.server, propertyName);  
  this.nameEditor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
  this.nameEditor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);
  this.editors.push(this.nameEditor);
};


ServerEditor.prototype.addIp = function(elt, propertyName) {
  this.ipEditor = new TextEditor(elt, this.server, propertyName);
  this.ipEditor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
  this.ipEditor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);
  this.editors.push(this.ipEditor);
};


ServerEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor : this, data : event.data.value}};
  this.fire(ev);
};

var ServerView = View.extend({
  init : function(id) {
	this._super(id);
	this.html = translate(AppTemplate.ServerView, Lang.ServerView);     
    this.server = null;
    this.editor = null;
    this.saveDialog = null;
    
    this.serverInitialized = false;
    this.stateInitialized  = false;
  }
});


ServerView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};


ServerView.prototype.getEditor = function() {
  return this.editor;	
};


ServerView.prototype.destroy = function() {
  this.extTabs.destroy();
};


ServerView.prototype.hide = function() {
  this.pooler.stop();
  this.$id.hide();
};


ServerView.prototype.setServer = function(server) {
  this.server = server;
  this.state = this.server.get(Server.SERVER_STATE);
    
  this._initServer();
  this._initState();  
};


ServerView.prototype.setState = function(state) {
  this.state = state;
  
  if (! this.stateInitialized) this._initState();
  this._refreshState();
};


ServerView.prototype.setSpaces = function(spaces) {
  this.spaces = spaces;
  this._refreshSpaces();
};


ServerView.prototype.openSaveDialog = function(saveHandler, discardHandler, scope) {
  if (! this.saveDialog) {
    this.saveDialog = new SaveChangesDialog(this.extSaveDialog.dom);
    this.saveDialog.addButton(Lang.Buttons.save, saveHandler, scope);
    this.saveDialog.addButton(Lang.Buttons.discard, discardHandler, scope);
  }
  this.saveDialog.open();
};


ServerView.prototype.closeSaveDialog = function() {
  if (this.saveDialog) this.saveDialog.close();	
};


ServerView.prototype._refreshServer = function() {
  this.extName.dom.value = this.server.name;
  this.extIP.dom.value   = this.server.ip;	
  this._refreshTabs();
};


ServerView.prototype._refreshState = function() {  		
  this.logsTab.setContent(this.state.log);  
  if (this.server.enabled) this._showTabs();
};


ServerView.prototype._refreshSpaces = function() {  
  var section = this.merge(AppTemplate.SpacesSection, {spaces : this.spaces.toArray()});  
  section = translate(section, Lang.ServerView);
  this.extSpacesSection.dom.innerHTML = section;  	
};


ServerView.prototype._backButtonHandler = function(event, target, options) {  
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickBackButton();
};


ServerView.prototype._saveButtonHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickSaveButton();	
};


ServerView.prototype._discardButtonHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickDiscardButton();	
};


ServerView.prototype._initServer = function() {
  this.extParent = this.extParent || Ext.get(this.id);       
  var content = this.merge(this.html, {server: this.server, federations: this.server.get(Server.FEDERATIONS).toArray()}); 	
  this.extParent.dom.innerHTML = content;
      	
  this.backButton    = Ext.get(this.extParent.query('a[name="back"]').first());
  this.saveButton    = Ext.get(this.extParent.query('a[name="save"]').first());
  this.discardButton = Ext.get(this.extParent.query('a[name="discard"]').first());
  
  this.extName       = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extIP         = Ext.get(this.extParent.query('input[name="ip"]').first());
  
  this.extFederationsSection = Ext.get(this.extParent.query('div[name="federations"]').first());
  this.extSpacesSection = Ext.get(this.extParent.query('div[name="spaces"]').first());
  
  this.extTabs      = new Ext.TabPanel("tabs");
  this.stateTab     = this.extTabs.addTab(Ids.Elements.STATE_TAB, Lang.ServerView.state);  
  this.logsTab      = this.extTabs.addTab(Ids.Elements.LOGS_TAB, Lang.ServerView.log);
  this.extMessage   = this.extParent.select('.message').first();
      
  this.extSaveDialog = Ext.get(Ids.Elements.DIALOG); 
	  
  this.editor = this._createEditor(this.server); 
  
  this.extTabs.on('tabchange', function(tabPanel) {
    var tab = tabPanel.getActiveTab();
    if (!tab) return;
    tab.activate();
  }, this);
    
  this.extFederationsSection.on('click', this._clickFederationsHandler, this);
  this.extSpacesSection.on('click', this._clickSpacesHandler, this);
    	  
  this.backButton.on('click', this._backButtonHandler, this);
  this.saveButton.on('click', this._saveButtonHandler, this);
  this.discardButton.on('click', this._discardButtonHandler, this);  
  
  $(this.saveButton.dom).addClassName('disabled');  
  
  this._refreshTabs();
    
  this.pooler = new Pooler({poolTime: 3000, url: Context.Config.Api + "?op=loadserverstate", params : {id : this.server.id}});
  this.pooler.start({
    context : this,
    success : function() { 
      this.presenter.loadServerState(this.server.id);
    }
  });
  
  this.serverInitialized = true;
};


ServerView.prototype._initState = function() {
  var template = translate(AppTemplate.ServerState, Lang.ServerView);
  var html = this.merge(template, {state : this.server.get(Server.SERVER_STATE)});
  this.stateTab.setContent(html);
    
  this.logsTab.setContent(this.state.log);
  if (this.server.enabled) this._showTabs();
    
  this.stateInitialized = true;
};


ServerView.prototype._createEditor = function(server) {
  var editor = new ServerEditor({type: 'composed'});
  var nameElement = this.extParent.query('input[name="name"]').first();
  var ipElement   = this.extParent.query('input[name="ip"]').first();
  
  editor.setServer(server);
  editor.addName(nameElement, Server.NAME);
  editor.addIp(ipElement, Server.IP);  
  editor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  editor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  
  return editor;    
};


ServerView.prototype._refreshTabs = function() {
  if (this.server.enabled) {
	  this._showTabs();
    this.stateTab.activate();
    this.stateTab.show();
  }
  else { 
	this.extTabs.getActiveTab().hide(); 
	this._showMessage();
  }     	
};


ServerView.prototype._showTabs = function() {
  $(this.extTabs.el).show();
  this.extTabs.endUpdate();
  this.extMessage.dom.hide();
};


ServerView.prototype._showMessage = function() {
  $(this.extTabs.el).hide();
  this.extMessage.dom.show();	
};


ServerView.prototype._clickFederationsHandler = function(event, target, options) {
  if (target.nodeName.toLowerCase() != "a") return;
  
  event.preventDefault();
  event.stopPropagation();
  this.presenter.openFederation(target.getAttribute('code'));
};


ServerView.prototype._clickSpacesHandler = function(event, target, options) {
  if (target.nodeName.toLowerCase() != "a") return;
 
  event.preventDefault();
  event.stopPropagation();
  this.presenter.openSpace(target.getAttribute('code'));  	
};


ServerView.prototype._changeEditorHandler = function(event) {
  if (this.editor.isDirty()) $(this.saveButton.dom).removeClassName('disabled');
  else $(this.saveButton.dom).addClassName('disabled');
};

var ServerActivity = Activity.extend({
  init : function() {
	this.view = this._createView();
	this.view.setPresenter(this);
	this.service = GridedService;	
	this.server = null; 	  
  }
});


ServerActivity.prototype.start = function(data) {
  if (!this.server || this.server.get(Server.ID) != data.id) this._clearModel();	
	
  if (!this.view) this.view = this._createView();
    
  if (this.server) this._showView(this.server);
  if (!this.server) this.service.loadServer(data.id, {
	 context: this,
	 success: function(server) {
	   this.server = server;
	   this.view.setServer(this.server);	
	   this._showView(this.server);
	   
	   this._loadSpaces(server.id);	   
	 },
	 failure : function(ex) {
		alert("Error loading Server"); 
	 }
  });
};


ServerActivity.prototype.stop = function() {
  var editor = this.view.getEditor();
  if (editor) editor.close();
  this._unlink(this.server);	
         
  this.view.hide();  
};


ServerActivity.prototype.canStop = function(callback) {
 var editor = this.view.getEditor();
 if (! editor.isDirty()) { callback.success.call(callback.context, {}); return;}; 
	 
 var continueCallback = {
   context: this,
   success : function() { callback.success.call(callback.context, {}); },
   failure : function() { alert("Error saving server"); }     
 };	 
 
 var saveCallback = {
   context: this,
   save: function() { this._saveServer(continueCallback); },
   cancel : function() { editor.reset(); continueCallback.success.call(continueCallback.context, null); }   
 };
 
 this._askForSaveChanges(saveCallback); 
};


ServerActivity.prototype.loadServerState = function(id, callback) {
  this.service.loadServerState(id, {
    context : this,
    success : function(serverState) {
      this.server.set(Server.SERVER_STATE, serverState);
    },
    failure : function(ex) {
      throw ex;
    }
  });
};


ServerActivity.prototype.notify = function(event) {
  if (event.propertyName == Server.SERVER_STATE) {
    var server = event.data.model;    
    this.view.setState(server.get(Server.SERVER_STATE));
  }
};


ServerActivity.prototype._showView = function(server) {
  this.view.getEditor().open(server);
  this._link(server);
  this.view.show();	
};


ServerActivity.prototype.clickBackButton = function() {
  this._goBack();	
};


ServerActivity.prototype.clickSaveButton = function() {
  var continueCallback = {
	context: this,
	success : function() { console.log("Server saved");  },
	failure : function() { alert("Error saving server"); }
  };	
     
  this._saveServer(continueCallback);  	
};


ServerActivity.prototype.clickDiscardButton = function() {
  var editor = this.view.getEditor();
  editor.reset();
  this._goBack();  
};


ServerActivity.prototype.openFederation = function(id) {
  var event = {name: Events.OPEN_FEDERATION, token: new FederationPlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	
};


ServerActivity.prototype.openSpace = function(id) {
  var event = {name: Events.OPEN_SPACE, token: new SpacePlace(id).toString(), data: {id: id}};
  EventBus.fire(event);		
};


ServerActivity.prototype._loadSpaces = function(id) {
  this.service.loadServerSpaces(id, {
    context: this,
    success: function(spaces) {
      this.view.setSpaces(spaces);	
    },
    failure : function(ex) { throw ex; } 	
  });	
};


ServerActivity.prototype._saveServer = function(callback) { 	
  var editor = this.view.getEditor();
  if (! editor.isDirty()) return;

  editor.flush();
      
  var server = this.server;
  this.service.saveServer(this.server, {
    context : this,
    success : function() {      
      var event = {name: Events.SERVER_SAVED, data:{server : server} };
      EventBus.fire(event);	 
      callback.success.call(callback.context, null);
      
      NotificationManager.showNotification(Lang.Notifications.SERVER_SAVED, 1000);
    },
    failure : function() {
      callback.failure.call(callback.context, null);
      NotificationManager.showNotification(Lang.Notifications.SAVE_SERVER_ERROR, 1000);
    }
  });
};


ServerActivity.prototype._link = function(model) {
  model.on(Events.CHANGED, this, this);  
};


ServerActivity.prototype._unlink = function(model) {
  if (!model) return;  
  model.un(Events.CHANGED, this);
};


ServerActivity.prototype._goBack = function() {
  var event = {name : Events.OPEN_SERVERS, token : new ServersPlace().toString(), data: {}};
  EventBus.fire(event);
};


ServerActivity.prototype._createView = function() {
  var view = new ServerView(Ids.Elements.SERVER_VIEW, AppTemplate.ServerView);  
  return view;
};


ServerActivity.prototype._askForSaveChanges = function(callback) {    
  var saveHandler = function() { 
    this.view.closeSaveDialog();
    callback.save.call(callback.context, null);	  
  };   

  var discardHandler  = function() { 
    this.view.closeSaveDialog();    
    callback.cancel.call(callback.context, null);    
  };
		    	    
  this.view.openSaveDialog(saveHandler, discardHandler, this);	
};


ServerActivity.prototype._clearModel = function() {
  this.server = null;  
  this.environments = null;
};

var Federations = Collection.extend({
  init : function() {
	this._super();
  }
});

var Spaces = Collection.extend({
  init : function() {
	this._super();
  }
});













Lang.DeploymentView = {
  no_spaces: 'No spaces',
  no_federations : 'No federations',
  federations : 'Federations',
  spaces : 'Spaces'
};

Lang.SelectServerDialog = {
  server: 'Server',		  
  select_server_message : "Select a server"		
};

var SelectServerDialog = Dialog.extend({
  init : function(element, servers) {
    this._super(element);
    this.servers = servers;
    this.html = translate(AppTemplate.SelectServerDialog, Lang.SelectServerDialog);
    element.innerHTML = this.html;
    
    this._bind();
  }
});
SelectServerDialog.CHANGE_SELECTION_EVENT = "change_selection";


SelectServerDialog.prototype.getData = function() {
  var selectedOption = this.extSelect.dom.options[this.extSelect.dom.selectedIndex];
    
  var data = {
    serverId : selectedOption.getAttribute("value"),    
  };
  return data;
};


SelectServerDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.$el);  
  this.extSelect = this.extParent.select('select').first();
  this.extSelect.on('change', this._selectHandler, this);
        
  var options = this._createServerOptions();
  if (options.length > 0) this.extSelect.dom.innerHTML = options;    
};


SelectServerDialog.prototype._selectHandler = function(event, target, options) {
  if (target.nodeName.toLowerCase() != "select") return;	
  this.fire({name: SelectServerDialog.CHANGE_SELECTION_EVENT, data: this});    
};


SelectServerDialog.prototype._createServerOptions = function() {
  var options = "";
  var defaultOption = '<option value="">' + Lang.SelectServerDialog.select_server_message + '</option>';
  options += defaultOption;
  
  var tpl = '<option value="$1">$2</option>';	
  for (var i=0; i < this.servers.length; i++) {
	var server = this.servers[i]; 
	var option = tpl.replace(/\$1/g, server.id);
	option = option.replace(/\$2/g, server.name);
	options += option;
  }
  return options;
};

Lang.AddFederationDialog = {
  add_federations: "Add federations",
  name: 'Name',
  add : 'Add',
  file : 'File',  
  url : 'URL'	  
};

var AddFederationDialog = Dialog.extend({
  init : function(element) {
    this._super(element);	      
    this.html = translate(AppTemplate.AddFederationDialog, Lang.AddFederationDialog);
    element.innerHTML = this.html;

    this._bind();
  }	
});

AddFederationDialog.ADD_EVENT = "add";


AddFederationDialog.prototype.setFocus = function() {
  this.extName.dom.focus();
};


AddFederationDialog.prototype.getAddFormData = function() {      
  var data = {
    form : this.extAddForm.dom,		  
    name : this.extName.dom.value,
    url  : this.extURL.dom.value
  };
  return data;
};


AddFederationDialog.prototype.clear = function() {
  this.extName.dom.value = "";	 
  this.extURL.dom.value = "";
  this.extAddButton.dom.disabled = true;
};


AddFederationDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.$el);
  this.extAddForm  = Ext.get(this.extParent.query('form[name="add-federation"]').first());
  
  this.extName = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extURL  = Ext.get(this.extParent.query('input[name="url"]').first());
  this.extAddButton    = Ext.get(this.extParent.query('input[name="add-button"]').first());
            
  this.extAddButton.on('click', this._addHandler, this);
  
  this.extName.on('keyup', this._changeHandler, this);
  this.extURL.on('keyup', this._changeHandler, this);  
  this.extURL.on('keypress', this._keyPressHandler, this);
    
  this.extAddButton.dom.disabled = true;
};


AddFederationDialog.prototype._keyPressHandler = function(event, target, options) {  
  if (event.keyCode != Ext.EventObject.ENTER) return;  
  if (target === this.extURL.dom) {
	  event.preventDefault();
	  event.stopPropagation();	  
    this._addHandler(event, target, options); return; 
  }     
};


AddFederationDialog.prototype._addHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();	
  var evt = {name: AddFederationDialog.ADD_EVENT, data: this};
  this.fire(evt);    
};


AddFederationDialog.prototype._changeHandler = function(event, target, options) {    
  this.extAddButton.dom.disabled = true;
  if (this._validate()) this.extAddButton.dom.disabled = false;  
};


AddFederationDialog.prototype._validate = function(event, target, options) {
  if (this.extName.dom.value.length > 0 && this.extURL.dom.value.length > 0) return true;
  else return false;
};

Lang.AddSpaceDialog = {
  add_spaces: "Add spaces",
  name: 'Name',
  add : 'Add',   
  url: 'URL',
  select_model : 'Select a model...',
  model : 'Model'    
};

var AddSpaceDialog = Dialog.extend({
  init : function(element, models) {
    this._super(element);	    
    this.models = models;
    this.html = translate(AppTemplate.AddSpaceDialog, Lang.AddSpaceDialog);
    element.innerHTML = this.html;

    this.defaultOption = new Option(Lang.AddSpaceDialog.select_model, 0);
    this.defaultOption.selected = true;
    
    this._bind();
  }	
});

AddSpaceDialog.ADD_EVENT = "add";


AddSpaceDialog.prototype.setFocus = function() {
  this.extName.dom.focus();
};


AddSpaceDialog.prototype.getAddFormData = function() {      
  var data = {    
    form : this.extAddForm.dom,
    name : this.extName.dom.value,
    url  : this.extURL.dom.value,
    modelId : this.extModelsCombo.dom.options[this.extModelsCombo.dom.selectedIndex].value
  };
  return data;
};


AddSpaceDialog.prototype.clear = function() {
  this.extName.dom.value = "";
  this.extURL.dom.value = "";  
  this.extAddButton.dom.disabled = true;
  this.extModelsCombo.dom.selectedIndex = 0;
};


AddSpaceDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.$el);
  
  this.extAddForm  = Ext.get(this.extParent.query('form[name="add-space"]').first());	  
  this.extName  = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extURL   = Ext.get(this.extParent.query('input[name="url"]').first());
    
  this.extAddButton   = Ext.get(this.extParent.query('input[name="add-button"]').first());
  this.extModelsCombo = Ext.get(this.extParent.query('select').first());
            
  this.extAddButton.on('click', this._addHandler, this);
  this.extName.on('keyup', this._changeHandler, this);
  this.extURL.on('keyup', this._changeHandler, this);
  this.extModelsCombo.on('change', this._changeHandler, this);
  this.extURL.on('keypress', this._keyPressHandler, this);
  this.extAddButton.on('keypress', this._keyPressHandler, this);
    
  this._createModelsCombo();
  this.extAddButton.dom.disabled = true;
};


AddSpaceDialog.prototype._keyPressHandler = function(event, target, options) {  	
  if (event.keyCode != Ext.EventObject.ENTER) return;
	
  if (target === this.extURL.dom) {
   event.preventDefault();
   event.stopPropagation();
	 this._addHandler(event, target, options); 
	return;
  }  
};


AddSpaceDialog.prototype._addHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();	
  var evt = {name: AddSpaceDialog.ADD_EVENT, data: this};
  this.fire(evt);    
};


AddSpaceDialog.prototype._changeHandler = function(event, target, options) {
  this.extAddButton.dom.disabled = true;  
  if (this._validate()) this.extAddButton.dom.disabled = false;   
};


AddSpaceDialog.prototype._validate = function(event, target, options) {
  if ((this.extName.dom.value.length > 0) && (this.extURL.dom.value.length > 0) && (this.extModelsCombo.dom.selectedIndex != 0)) return true;
  else return false;
};


AddSpaceDialog.prototype._createModelsCombo = function() {  
  this.extModelsCombo.dom.options[0] = this.defaultOption;
     
  for (var i=0, l = this.models.length; i < l; i++) {
    var model = this.models[i];
    var option = new Option(model.name, model.id);
    this.extModelsCombo.dom.options[this.extModelsCombo.dom.options.length] = option;
  }  
};

var DeploymentView = View.extend({
  init : function(id) {
    this._super(id);	  
    var template = translate(AppTemplate.DeploymentView, Lang.DeploymentView); 
    this.html = translate(template, Lang.Buttons);

    this.servers = null;
    this.federations = null;
    this.spaces = null;
    this.addFederationDialog = null;
    this.addSpaceDialog = null;
    
    this.initialized = false;       
    this._init();
  }
});


DeploymentView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};


DeploymentView.prototype.setServers = function(servers) {
  this.servers = servers;  
};


DeploymentView.prototype.setModels = function(models) {
  this.models = models;
};


DeploymentView.prototype.setFederations = function(federations) {
  this.federations = federations;      
  this._refreshFederationsViewer();  
};


DeploymentView.prototype.setSpaces = function(spaces) {
  this.spaces = spaces;  
  this._refreshSpacesViewer();  
};


DeploymentView.prototype.refreshFederation = function(federation) {
	this._refreshFederationsViewer();  
};


DeploymentView.prototype.refreshFederationSpaces = function() {
  this._refreshSpacesViewer();
};


DeploymentView.prototype.selectFederation = function(federationId) { 
  this.federationsTable.openRow(federationId);
};


DeploymentView.prototype.showLoadingSpaces = function() {      	
  var extDocument = Ext.get(document);
  
  
  $(this.extLoadingSpaces.dom).show();
  
  extDocument.on('mousemove', function(mouseEvent) {    
    this.extLoadingSpaces.dom.style.left = mouseEvent.getPageX() + 15 + "px";
    this.extLoadingSpaces.dom.style.top = mouseEvent.getPageY() + "px";
  }, this);
};


DeploymentView.prototype.hideLoadingSpaces = function() {
  var scope = this;	  
  
  setTimeout(function(scope) {$(scope.extLoadingSpaces.dom).hide();}, 300, scope);	  
};


DeploymentView.prototype.showSpacesViewer = function() {
  this.extSpacesViewer.show();    
};


DeploymentView.prototype.hideSpacesViewer = function() {
  this.extSpacesViewer.hide();  
};


DeploymentView.prototype.show = function() {
  this.$id.show();
  this.extSpacesViewer.hide();
};


DeploymentView.prototype.hide = function() {
  this.$id.hide();	
};


DeploymentView.prototype.showSelectServerDialog = function() {
  if (! this.selectServerDialog) {
    this.selectServerDialog = new SelectServerDialog(this.extSelectServerDialog.dom, this.servers);
    this.selectServerDialog.on(SelectServerDialog.CHANGE_SELECTION_EVENT, {
  	  notify: function(event) {
  	    var dialog = event.data;
  	    var data = dialog.getData(); 
        this.presenter.selectServer(data);    
      }
  	},this);	  
  }
  this.selectServerDialog.open();       
};


DeploymentView.prototype.showAddFederationDialog = function() {
  if (! this.addFederationDialog) { 
    this.addFederationDialog = new AddFederationDialog(this.extFederationDialog.dom);
        
    this.addFederationDialog.on(AddFederationDialog.ADD_EVENT, {
  	  notify: function(event) {
  	    var dialog = event.data;
  	    var name = dialog.getAddFormData().name; 
  	    var url  = dialog.getAddFormData().url;
        this.presenter.addFederation(name, url);    
      }
  	},this);
  }
  this.addFederationDialog.open(); 
};


DeploymentView.prototype.hideAddFederationDialog = function() {
  if (! this.addFederationDialog) return;
  this.addFederationDialog.close();
};


DeploymentView.prototype.clearAddFederationDialog = function() {
  if (! this.addFederationDialog) return;
  this.addFederationDialog.clear();
};


DeploymentView.prototype.focusAddFederationDialog = function() {
  if (! this.addFederationDialog) return;
  this.addFederationDialog.setFocus();
};


DeploymentView.prototype.showAddSpaceDialog = function() {
  if (! this.addSpaceDialog) {	  
    this.addSpaceDialog = new AddSpaceDialog(this.extSpaceDialog.dom, this.models);
	
	  this.addSpaceDialog.on(AddSpaceDialog.ADD_EVENT, {
	    notify: function(event) {
	      var dialog = event.data;
	  	  var name    = dialog.getAddFormData().name;	  	
	  	  var url     = dialog.getAddFormData().url;
	  	  var modelId = dialog.getAddFormData().modelId;
	      this.presenter.addSpace(name, url, modelId);    
	    }
	  },this);
  }
  this.addSpaceDialog.open();
};


DeploymentView.prototype.clearAddSpaceDialog = function() {
  if (! this.addSpaceDialog) return;
  this.addSpaceDialog.clear();
};


DeploymentView.prototype.focusAddSpaceDialog = function() {
  if (! this.addSpaceDialog) return;
  this.addSpaceDialog.setFocus();
};


DeploymentView.prototype.hideAddSpaceDialog = function() {
  if (! this.addSpaceDialog) return;
  this.addSpaceDialog.close();
};


DeploymentView.prototype._init = function() {
  this.extParent = Ext.get(this.id);  
  this.extParent.dom.innerHTML = this.html;
  
  this.extSelectServerDialog = this.extParent.select('.select-server-dialog').first();
  this.extFederationDialog   = this.extParent.select('.add-federation-dialog').first();
  this.extSpaceDialog        = this.extParent.select('.new-space-dialog').first();
  
  this.extLoadingSpaces = this.extParent.select('.loading-spaces').first();
  
  this._createFederationsViewer();
  this._createSpacesViewer();  
  
  this.hideLoadingSpaces();
  $(this.extLoadingSpaces.dom).hide();
  
  this.hideAddFederationDialog();
  this.hideAddSpaceDialog();
  
  this.initialized = true;
};


DeploymentView.prototype._createFederationsViewer = function() {
  this.extFederationsViewer = Ext.get('federations-viewer');
   
  var extRemoveButton = Ext.get(this.extFederationsViewer.query('input[name="remove_button"]').first());
   		
  var tableElement = this.extFederationsViewer.select('.table').first();	
  var columns = [Federation.NAME];    
 
  var startAction = {name: Lang.Buttons.start};
  var stopAction  = {name: Lang.Buttons.stop};
  var conditional = {actions:[startAction, stopAction], getIndex: function(row) {
    return (row.state.running)? 1 : 0; 
  }}; 
  
  this.federationsTable = new Table(tableElement.dom, columns, {checkbox: true, clickable: true, conditional : conditional, empty_message: Lang.DeploymentView.no_federations});
    
  extRemoveButton.on('click', this._removeFederationHandler, this);
  
  this.federationsTable.on(Table.CLICK_ROW, {notify: this._openFederationHandler}, this);  
  this.federationsTable.on(Table.CHECK_ROW, {notify : this._refreshFederationsViewerToolbar}, this);
  this.federationsTable.on(Table.CLICK_ROW_ACTION, {notify: this._federationsActionHandler}, this);
  this.federationsTable.on(Table.ROW_MOUSE_OVER, {notify: this._selectFederationHandler}, this);
  
		  		    
  $(this.extFederationsViewer.dom).hide();
  
  this.federationsToolbar = {
    removeButton : extRemoveButton.dom
  };
  
  this._refreshFederationsViewerToolbar();
};


DeploymentView.prototype._refreshFederationsViewer = function() {
  this.federationsTable.setData(this.federations);  
  $(this.extFederationsViewer.dom).show();
  this._refreshFederationsViewerToolbar();
};


DeploymentView.prototype._refreshFederationsViewerToolbar = function() {  	
  var button = this.federationsToolbar.removeButton;
  var rows = this.federationsTable.getSelectedRows();
  if (rows.length > 0) $(button).show(); else $(button).hide();    
};


DeploymentView.prototype._addFederationHandler = function(event, target, options) {
  this.presenter.addFederation();
};


DeploymentView.prototype._removeFederationHandler = function(event, target, options) {
  var rows = this.federationsTable.getSelectedRows();
  var ids = [];
  $(rows).each(function(row) { ids.push(row.id); });
  this.presenter.removeFederations(ids);
};


DeploymentView.prototype._selectFederationHandler = function(event) {  
  var federationId = event.data.row.id;	
  this.presenter.selectFederation(federationId);
  this.federationsTable.openRow(federationId);
};


DeploymentView.prototype._unSelectFederationHandler = function(event) { 
  this.presenter.unSelectFederation();	
};


DeploymentView.prototype._openFederationHandler = function(event) {
  var federationId = event.data.row.id;
  this.presenter.openFederation(federationId);	
};


DeploymentView.prototype._federationsActionHandler = function(event) {  	
  switch (event.data.action) {
    case Lang.Buttons.start: this.presenter.startFederation(event.data.row.id); break;
    case Lang.Buttons.stop: this.presenter.stopFederation(event.data.row.id); break;
  }
};


DeploymentView.prototype._spacesActionHandler = function(event) {    
  switch (event.data.action) {
    case Lang.Buttons.start: this.presenter.startSpace(event.data.row.id); break;
    case Lang.Buttons.stop: this.presenter.stopSpace(event.data.row.id); break;
  }
};


DeploymentView.prototype._createSpacesViewer = function() {	
  this.extSpacesViewer = Ext.get('spaces-viewer');
    
  var extRemoveButton = Ext.get(this.extSpacesViewer.query('input[name="remove_button"]').first());		     	  

  var tableElement = this.extSpacesViewer.select('.table').first();	  
  var columns = [Space.NAME];  
  
  var startAction = {name : Lang.Buttons.start};
  var stopAction  = {name: Lang.Buttons.stop};
  var conditional = {actions:[startAction, stopAction], getIndex: function(row) {
    return (row.state.running)? 1 : 0; 
  }}; 
  
  this.spacesTable = new Table(tableElement.dom, columns, {checkbox: true, clickable: true, conditional : conditional, empty_message: Lang.DeploymentView.no_spaces});  
  
  extRemoveButton.on('click', this._removeSpaceHandler, this);
  
  this.spacesTable.on(Table.CLICK_ROW, {notify: this._openSpaceHandler}, this);    
  this.spacesTable.on(Table.CHECK_ROW, {notify: this._refreshSpacesViewerToolbar}, this);
  this.spacesTable.on(Table.MOUSE_ENTER, {notify: this._enterSpacesHandler}, this);
  this.spacesTable.on(Table.CLICK_ROW_ACTION, {notify: this._spacesActionHandler}, this);
 

  this.spacesToolbar = {
    removeButton : extRemoveButton.dom
  };
  
  this.extSpacesViewer.hide();
  this._refreshSpacesViewerToolbar();
};


DeploymentView.prototype._enterSpacesHandler = function(event, target, options) {
  this.presenter.onOverSpacesViewer();	  
};


DeploymentView.prototype._leaveSpacesHandler = function(event, target, options) {
  this.presenter.onOutSpacesViewer();	   
};


DeploymentView.prototype._refreshSpacesViewer = function() {	
  this.spacesTable.setData(this.spaces);
  this.extSpacesViewer.show();
  this._refreshSpacesViewerToolbar();
};


DeploymentView.prototype._refreshSpacesViewerToolbar = function() {  	
  var button = this.spacesToolbar.removeButton;
  var rows = this.spacesTable.getSelectedRows();
  if (rows.length > 0) $(button).show(); else $(button).hide();    
};


DeploymentView.prototype._removeSpaceHandler = function(event, target, options) {
  var rows = this.spacesTable.getSelectedRows();
  var ids = [];
  $(rows).each(function(row) { ids.push(row.id); });
  this.presenter.removeSpaces(ids);
};


DeploymentView.prototype._openSpaceHandler = function(event) {
  var spaceId = event.data.row.id;
  this.presenter.openSpace(spaceId);	
};

var DeploymentActivity = Activity.extend({
  init : function() {
    this.view = this._createView();
    this.view.setPresenter(this);
    this.service = GridedService;
    
    this.serversCollection = null;
    this.federationsCollection = null;    
    this.selectedFederation = null;
    
    this.selectedServer = null;
    this.browsingSpacesFlag = false;
  }	
});


DeploymentActivity.prototype.start = function(data) {
  if (! this.view) this.view = this._createView();
  
  if (this.federationsCollection) this._showView(this.federationsCollection);
  
  if (! this.federationsCollection) this.service.loadAllFederations({    
    context: this,
    success: function(federationsCollection) {
      this.federationsCollection = federationsCollection;
      this.view.setFederations(this.federationsCollection.toArray());     
      this._showView(this.federationsCollection);                       
    },
    failure: function(ex) {
      throw ex; 
    }
  });
};


DeploymentActivity.prototype.stop = function() {
  NotificationManager.hideNotification();
  
  this._unlink(this.federationsCollection);
  if (this.selectedFederation) this._unlink(this.selectedFederation);
  this.view.hide();
};


DeploymentActivity.prototype.canStop = function(callback) {
  callback.success.call(callback.context, {});	
};


DeploymentActivity.prototype.notify = function(event) {
  if (event.data.model instanceof Collection) {
    var federations = event.data.model.toArray();
    this.view.setFederations(federations);    
  }
  else if (event.data.model instanceof Federation) {
    var federation = event.data.model;
    
    switch (event.propertyName) {
      case Federation.SPACES: 
    	  this.view.setSpaces(federation.get(Federation.SPACES).toArray());
    	  break;
      case Federation.STATE:
    	  this.view.refreshFederation(federation);
    }    
  }
};


DeploymentActivity.prototype._showView = function(federations) {
  this._link(federations);
  
  if (federations.size() > 0) {
    if (! this.selectedFederation) this.selectedFederation = federations.get(0);
    this.selectFederation(this.selectedFederation.id);  
  }

  this._loadData();  
  this.view.show();
};


DeploymentActivity.prototype.selectServer = function(data) {
  var id = data.serverId;
  
  if (id != "") { 
	  this.selectedServer = this.serversCollection.getById(id);
	  this.view.showAddFederationDialog();
	  
	  if (this.selectedServer != null && this.selectedFederation != null) {
	    this.view.showAddSpaceDialog();	  
	  }
	  else {
		this.view.hideAddSpaceDialog();
	  }
	  
  } else {
	  this.selectedServer = null;
	  this.view.hideAddFederationDialog();
	  this.view.hideAddSpaceDialog();
  }
};


DeploymentActivity.prototype._loadFederation = function(id, callback) {

  this.view.showLoadingSpaces();
 
  this.service.loadFederation(id, {
     context : this,
     success : function(federation) {
       this.view.hideLoadingSpaces();
    	
    	  var oldFederation = this.federationsCollection.getById(federation.id);
    	  this._link(oldFederation);
    	  oldFederation.set(Federation.SPACES, federation.get(Federation.SPACES));
    	  this._unlink(oldFederation);
    	
    	if (callback) { callback.success.call(callback.context, oldFederation);}
     },
     failure : function() {
   	   this.view.hideLoadingSpaces();
       alert("Error loading federation"); 
     }
   }); 	
};


DeploymentActivity.prototype.selectFederation = function(id) {
  if (this.selectedFederation && ! this.selectedFederation.id == id) return;
	  
   this._loadFederation(id, {
    context: this,
    success: function(federation) {
      if (this.selectedFederation) this._unlink(this.selectedFederation);
      this.selectedFederation = federation;
      this.view.selectFederation(federation.id);
      this.view.showSpacesViewer();
      
  	  if (this.selectedServer) this.view.showAddSpaceDialog();    
    }, 
    failure: function(ex) {
      throw ex;
    }
   });
};


DeploymentActivity.prototype.unSelectFederation = function() {
  if (! this.browsingSpacesFlag) {
    this.view.hideSpacesViewer();
  }
};


DeploymentActivity.prototype.openFederation = function(id) {
  this.selectedFederation = this.federationsCollection.getById(id);
  var event = {name: Events.OPEN_FEDERATION, token: new FederationPlace(id).toString(), data: {id: id}};
  EventBus.fire(event);
};


DeploymentActivity.prototype.addFederation = function(name, url) {

  NotificationManager.showNotification(Lang.Notifications.ADD_FEDERATION);
  
  this.service.addFederation(this.selectedServer.id, name, url, {
    context: this,
    success: function(federation) {
	    this.federationsCollection.add(federation);
	    this.selectFederation(federation.id);
	    this.view.clearAddFederationDialog();
	    this.view.focusAddFederationDialog();
	    NotificationManager.showNotification(Lang.Notifications.FEDERATION_ADDED, 1000);	  
	  },
	  failure : function() {
  	  this.view.hideAddFederationDialog();
	    NotificationManager.showNotification(Lang.Notifications.ADD_FEDERATION_ERROR, 1000);	  
	  }
  });  	  
};



DeploymentActivity.prototype.removeFederations = function(ids) {    
  var federationIds = ids; 
  
  NotificationManager.showNotification(Lang.Notifications.REMOVING);
  
  this.service.removeFederations(ids, {
    context: this,
    success: function() {
      if (this.federationsCollection.size() > 0) {
        if (this.federationsCollection.include(this.selectedFederation)) this.selectFederation(this.federationsCollection.get(0).id); 
      }
	    this.federationsCollection.remove(federationIds);
	      
	    NotificationManager.showNotification(Lang.Notifications.REMOVED, 1000);
    },
    failure : function(ex) {	  
      NotificationManager.showNotification(Lang.Notifications.REMOVE_ERROR, 1000);
      throw ex;      
    }
  });  
};


DeploymentActivity.prototype.startFederation = function(id) {
  NotificationManager.showNotification(Lang.Notifications.STARTING);
  
  this.service.startFederation(id, {
    context: this,
    success : function(federation) {
      NotificationManager.showNotification(Lang.Notifications.STARTED, 1000);
    	this._updateFederationState(federation);
    }, 
    failure : function(ex) {
      throw ex;
    }
  });	
};


DeploymentActivity.prototype.stopFederation = function(id) {    
  NotificationManager.showNotification(Lang.Notifications.STOPPING);
  this.service.stopFederation(id, {
    context: this,
    success : function(federation) {
      NotificationManager.showNotification(Lang.Notifications.STOPPED, 1000);
   	  this._updateFederationState(federation);
   }, 
   failure : function(ex) {
     throw ex;
   }
 });	
};


DeploymentActivity.prototype.addSpace = function(name, url, modelId) {
  
  NotificationManager.showNotification(Lang.Notifications.ADD_SPACE);
  
  this.service.addSpace(this.selectedServer.id, this.selectedFederation.id, name, url, modelId, {
    context: this,
    success: function(space) {      
      this.selectedFederation.get(Federation.SPACES).add(space);
      this.view.setSpaces(this.selectedFederation.get(Federation.SPACES).toArray());            
	    this.view.clearAddSpaceDialog();      
      this.view.focusAddSpaceDialog();
	    
	    NotificationManager.showNotification(Lang.Notifications.SPACE_ADDED, 1000);	    
	  },
	  failure : function() {
	    this.view.clearAddSpaceDialog();
	    NotificationManager.showNotification(Lang.Notifications.ADD_SPACE_ERROR, 1000);	    
	  }
  });  	  
	
  this.view.showAddSpaceDialog();
};


DeploymentActivity.prototype.openSpace = function(id) {
  var event = {name: Events.OPEN_SPACE, token: new SpacePlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	
};


DeploymentActivity.prototype.removeSpaces = function(ids) {
  var spaceIds = ids;	
  
  NotificationManager.showNotification(Lang.Notifications.REMOVING);
  
  this.service.removeSpaces(ids, {
    context: this,
    success: function() {
      this.selectedFederation.get(Federation.SPACES).remove(spaceIds);
      this.view.setSpaces(this.selectedFederation.get(Federation.SPACES).toArray());
      NotificationManager.showNotification(Lang.Notifications.REMOVED, 1000);
	},
	failure : function(ex) {	  
	  NotificationManager.showNotification(Lang.Notifications.REMOVE_ERROR, 1000);
	  throw ex;
	}
  });  	  
};


DeploymentActivity.prototype.startSpace = function(id) {
  NotificationManager.showNotification(Lang.Notifications.STARTING);
  
  this.service.startSpace(id, {
    context: this,
    success : function(space) {
      NotificationManager.showNotification(Lang.Notifications.STARTED, 1000);
      this._updateSpaceState(space);
    }, 
    failure : function(ex) {
      throw ex;
    }
  }); 
};


DeploymentActivity.prototype.stopSpace = function(id) {    
  NotificationManager.showNotification(Lang.Notifications.STOPPING);
  
  this.service.stopSpace(id, {
    context: this,
    success : function(space) {
      NotificationManager.showNotification(Lang.Notifications.STOPPING, 1000);
      this._updateSpaceState(space);
   }, 
   failure : function(ex) {
     throw ex;
   }
 });  
};



DeploymentActivity.prototype.onOverSpacesViewer = function(federationId) {
  this.browsingSpacesFlag = true;	
};


DeploymentActivity.prototype.onOutSpacesViewer = function(federationId) {
  this.browsingSpacesFlag = false;	
  this.view.hideSpacesViewer();
};











DeploymentActivity.prototype._createView = function() {
  var view = new DeploymentView(Ids.Elements.DEPLOYMENT_VIEW);	
  return view;
};


DeploymentActivity.prototype._link = function(model) {
  model.on(Events.ADDED, this, this);  	
  model.on(Events.REMOVED, this, this); 
  model.on(Events.CHANGED, this, this);
};


DeploymentActivity.prototype._unlink = function(model) {
  if (!model) return;  
  model.un(Events.ADDED, this, this);
  model.un(Events.REMOVED, this, this);	  
  model.un(Events.CHANGED, this, this);
};


DeploymentActivity.prototype._updateFederationState = function(selectedFederation) {
  var federation = this.federationsCollection.getById(selectedFederation.id);
  this._link(federation);
  federation.set(Federation.STATE, selectedFederation.state);
  this._unlink(federation);
};


DeploymentActivity.prototype._updateSpaceState = function(newSpace) {
  var spaces = this.selectedFederation.get(Federation.SPACES);  
  var space = spaces.getById(newSpace.id);
  space.set(Space.STATE, newSpace.get(Space.STATE));
  this.view.refreshFederationSpaces();  
};


DeploymentActivity.prototype._loadData = function() {
  var queque = new TasksQueQue();
  queque.add(this._loadServers, this);
  queque.add(this._loadModels, this);
  
  queque.on(TasksQueQue.COMPLETE_EVENT, {notify : function(event) {
    this.serversCollection = event.data.results[0];
    this.modelsCollection  = event.data.results[1];
    
    this.view.setServers(this.serversCollection.toArray());
    this.view.setModels(this.modelsCollection.toArray());    
    this.view.showSelectServerDialog();    
  }}, this);
  
  queque.execute();
};


DeploymentActivity.prototype._loadModels = function(callback) {
  
  this.service.loadModels({
    context: this,
    success: function(modelsCollection) {      
      callback.end(modelsCollection);
    },
    failure : function(ex) {
      throw ex;
    }
  });  
};


DeploymentActivity.prototype._loadServers = function(callback) {
  
  
  this.service.loadServers({
    context: this,
    success: function(serversCollection) {    	
    	callback.end(serversCollection);
    },
    failure : function(ex) {
      throw ex;
    }
  });
};

var State = Model.extend({
  init : function() {
    this._super();
  }
});

State.RUNNING = 'running';
State.TIME = 'time';

var Federation = Model.extend({
  init : function() {
    this._super();
    this.spaces = new Spaces();
    
    this.validations = {
      'name' : Validators.Required,
      'url': Validators.Required
    };    
  }
});

Federation.NAME   = 'name';
Federation.LABEL  = 'label';
Federation.URL    = 'url';
Federation.SPACES = 'spaces';
Federation.SERVER = 'server';
Federation.LOGO  = 'logo';
Federation.STATE  = 'state';
Federation.RUNNING_TIME = 'running_time',

Federation.USER_AUTH = 'user_auth';
Federation.CERTIFICATE_AUTH = 'certificate_auth';
Federation.OPENID_AUTH = 'openid_auth';
Federation.CONNECTION = 'connection';
Federation.CONNECTION_TYPE = 'type';


var DatabaseConnection = Model.extend({
  init : function() {
    this._super();
    this.type = ConnectionTypes.DATABASE;
  }
});

DatabaseConnection.URL = 'url';
DatabaseConnection.USER = 'user';
DatabaseConnection.PASSWORD = 'password';
DatabaseConnection.CONFIG = 'config';

var DatabaseConnectionConfig = Model.extend({
  init : function() {
    this._super();    
  }
});

DatabaseConnectionConfig.DATABASE_TYPE  = 'databasetype';

var LDAPConnection = Model.extend({
  init : function() {
    this._super();
    this.type = ConnectionTypes.LDAP;
  }
});


LDAPConnection.URL = 'url';
LDAPConnection.USER = 'user';
LDAPConnection.PASSWORD = 'password';
LDAPConnection.CONFIG = 'config';

var LDAPConnectionConfig = Model.extend({
  init : function() {
    this._super();    
  }
});

LDAPConnectionConfig.SCHEMA = 'schema';
LDAPConnectionConfig.CN_FIELD = 'cn_field';
LDAPConnectionConfig.UID_FIELD = 'uid_field';
LDAPConnectionConfig.EMAIL_FIELD = 'email_field';
LDAPConnectionConfig.LANG_FIELD = 'lang_field';

var FederationEditor = Editor.extend({
  init : function(federation, userAuthenticationEditor, options) {
	this._super();  
    this.federation = federation;
    this.options = options || {};
    this.options.type = 'composed';
    this.userAuthenticationEditor = userAuthenticationEditor;    
        
    this._init();
  }	
});


FederationEditor.prototype.open = function(federation) {
  this.federation = federation;
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	editor.open();
  }  
  this.state = Editor.OPENED;
};


FederationEditor.prototype.close = function(server) {
  this.server = null;
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	editor.close();
  }  
  this.state = Editor.CLOSED;
};


FederationEditor.prototype.setImage = function(filename, source) {  
  this.imageEditor.setImage(filename, source);  	
};


FederationEditor.prototype._init = function() {
  var fe = Federation;	
  var map = this.options.map;  

  
  this.labelEditor                     = new TextEditor(map[fe.LABEL].element, this.federation, map[fe.LABEL].propertyName);
  this.imageEditor                     = new ImageEditor(map[fe.LOGO].element, this.federation, map[fe.LOGO].propertyName);
  this.certificateAuthenticationEditor = new CheckboxEditor(map[fe.CERTIFICATE_AUTH].element, this.federation, map[fe.CERTIFICATE_AUTH].propertyName);
  this.openIDAuthenticationEditor      = new CheckboxEditor(map[fe.OPENID_AUTH].element, this.federation, map[fe.OPENID_AUTH].propertyName);
  
  this.labelEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.labelEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.imageEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.imageEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);  
  this.certificateAuthenticationEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.certificateAuthenticationEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);    
  this.openIDAuthenticationEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.openIDAuthenticationEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.userAuthenticationEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.userAuthenticationEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);  
  
  
  this.editors.push(this.labelEditor);
  this.editors.push(this.imageEditor);
  this.editors.push(this.userAuthenticationEditor);
  this.editors.push(this.certificateAuthenticationEditor);
  this.editors.push(this.openIDAuthenticationEditor);    
};


FederationEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor: this}};
  this.fire(ev);
};






var UserAuthenticationEditor = Editor.extend({
  init : function(federation, sourceEditor, connectionEditor, options) {
	this._super();  
    this.federation = federation;
    this.options = options || {};
    this.options.type = 'composed';
  
    this.checkboxEditor = null;
    this.sourceEditor = sourceEditor;
    this.connectionEditor = connectionEditor;
	        
    this._init();
  }	
});


UserAuthenticationEditor.prototype.setConnectionEditor = function(connectionEditor) {
  this.editors = [];
  this.connectionEditor.un(Events.CHANGED);
  this.connectionEditor.un(Events.RESTORED);
  this.connectionEditor = connectionEditor;
  
  this.connectionEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.connectionEditor.on(Events.RESTORED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  
  this.editors.push(this.sourceEditor);
  this.editors.push(connectionEditor);
};


UserAuthenticationEditor.prototype._init = function() {
  var fe = Federation;	
  var map = this.options.map;
  
  this.checkboxEditor = new CheckboxEditor(map[fe.USER_AUTH].element, this.federation, map[fe.USER_AUTH].propertyName); 
  
  this.checkboxEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.checkboxEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.sourceEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.sourceEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.connectionEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.connectionEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);  
  
  this.editors.push(this.checkboxEditor);    
  this.editors.push(this.sourceEditor);
  this.editors.push(this.connectionEditor);
};


UserAuthenticationEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor: this}};
  this.fire(ev);
};






var DatabaseConnectionEditor = Editor.extend({
  init : function(connection, options) {
	this._super();  
    this.connection = connection;
    this.options = options || {};
    this.options.type = 'composed';
	        
    this._init();
  }	
});


DatabaseConnectionEditor.prototype._init = function() {  
  var map = this.options.map;
  var con = DatabaseConnection;
  var config = DatabaseConnectionConfig;
  
  this.urlEditor      = new TextEditor(map[con.URL].element, this.connection, map[con.URL].propertyName);
  this.userEditor     = new TextEditor(map[con.USER].element, this.connection, map[con.USER].propertyName);
  this.passwordEditor = new TextEditor(map[con.PASSWORD].element, this.connection, map[con.PASSWORD].propertyName);
  this.engineEditor   = new SelectEditor(map[config.DATABASE_TYPE].element, this.connection.config, map[config.DATABASE_TYPE].propertyName);
  
  this.urlEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.urlEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.userEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.userEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.passwordEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.passwordEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.engineEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.engineEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);  
  
  this.editors.push(this.urlEditor);
  this.editors.push(this.userEditor);  
  this.editors.push(this.passwordEditor);  
  this.editors.push(this.engineEditor);    
};


DatabaseConnectionEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor: this}};
  this.fire(ev);
};






var LDAPConnectionEditor = Editor.extend({
  init : function(connection, options) {
	this._super();  
    this.connection = connection;
    this.options = options || {};
    this.options.type = 'composed';
	        
    this._init();
  }	
});


LDAPConnectionEditor.prototype._init = function() {  
  var map = this.options.map;
  var con = LDAPConnection;
  var config = LDAPConnectionConfig;
    
  this.urlEditor      = new TextEditor(map[con.URL].element, this.connection, map[con.URL].propertyName);
  this.userEditor     = new TextEditor(map[con.USER].element, this.connection, map[con.USER].propertyName);
  this.passwordEditor = new TextEditor(map[con.PASSWORD].element, this.connection, map[con.PASSWORD].propertyName);

  this.schemaEditor     = new TextEditor(map[config.SCHEMA].element, this.connection.config, map[config.SCHEMA].propertyName);  
  this.cnFieldEditor    = new TextEditor(map[config.CN_FIELD].element, this.connection.config, map[config.CN_FIELD].propertyName);
  this.uiFieldEditor   = new TextEditor(map[config.UID_FIELD].element, this.connection.config, map[config.UID_FIELD].propertyName);
  this.emailFieldEditor = new TextEditor(map[config.EMAIL_FIELD].element, this.connection.config, map[config.EMAIL_FIELD].propertyName);
  this.langFieldEditor  = new TextEditor(map[config.LANG_FIELD].element, this.connection.config, map[config.LANG_FIELD].propertyName);
    
  this.urlEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.urlEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.userEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.userEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.passwordEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.passwordEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.schemaEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.schemaEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.cnFieldEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.cnFieldEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.uiFieldEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.uiFieldEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.emailFieldEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.emailFieldEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.langFieldEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.langFieldEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  
  this.editors.push(this.urlEditor);
  this.editors.push(this.userEditor);
  this.editors.push(this.passwordEditor);

  this.editors.push(this.schemaEditor);
  this.editors.push(this.cnFieldEditor);
  this.editors.push(this.uiFieldEditor);
  this.editors.push(this.emailFieldEditor);
  this.editors.push(this.langFieldEditor);
  
};


LDAPConnectionEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor: this}};
  this.fire(ev);
};

Lang.FederationView = {    
  federation : 'Federation',  
  server : 'Server',  
  change: 'change',
  zoom: 'zoom',
  spaces : 'Business spaces',
  authentication : 'Authentication',	
  user_password : 'Usuario/contraseña',
  name : 'Name',
  label : 'Label',
  source : 'Fuente',
  url : 'URL',
  user : 'User',
  password : 'Password',
  engine : 'Engine',
  schema : 'Schema',
  alias : 'Alias',
  certificate : 'Certificate',
  openid : 'openID',
  federation : 'Federation',
  no_spaces : 'No spaces',
  start : 'Start',
  stop : 'Stop',
  state : 'State'
};

Lang.UploadDialog = {
  submit : 'Submit',
  close : 'Close', 
  no_filename : 'Filename to upload is required'
};

var UploadDialog = Dialog.extend({
  init: function(element) {
	this._super();
    var html = translate(AppTemplate.UploadDialog, Lang.UploadDialog);
    this.element = element;
    this.$el = $(element);
    this.$el.innerHTML = html;
    this._bind();    	  
  }
});

UploadDialog.CLICK_SUBMIT = 'click-submit';
UploadDialog.CLICK_CLOSE = 'click-close';


UploadDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.element);	
  this.extForm = Ext.get(this.extParent.query('form').first());  
  this.extSource = Ext.get(this.extForm.query('input[type="file"]').first());
  this.extSubmitButton = this.extParent.select('.submit').first();
  this.extCloseButton  = this.extParent.select('.close').first();
  this.extFilename = Ext.get(this.extParent.query('div[name="filename"]').first());
 
  this.extSubmitButton.on('click', this._clickSubmitButtonHandler, this);
  this.extCloseButton.on('click', this._clickCloseButtonHandler, this);
  this.extSource.on('change', this._changeSourceHandler, this);
};


UploadDialog.prototype._clickSubmitButtonHandler = function(event, target, options) {
  event.stopPropagation();
  event.preventDefault();
  
  if (this.extSource.dom.value == "") {alert(Lang.UploadDialog.no_filename); return; }
  this.extFilename.dom.innerHTML = this.extSource.dom.value;
  
  var event = {name: UploadDialog.CLICK_SUBMIT, data : {form: this.extForm.dom}};
  this.fire(event);
};


UploadDialog.prototype._clickCloseButtonHandler = function(event, target, options) {
  event.stopPropagation();
  event.preventDefault();
  this.extFilename.dom.innerHTML = "";
  var event = {name: UploadDialog.CLICK_CLOSE, data : {form: this.extForm.dom}};
  this.fire(event);    
};


UploadDialog.prototype._changeSourceHandler = function(event, target, options) {
  this.extFilename.dom.innerHTML = this.extSource.dom.value;  
};

var FederationView = View.extend({
  init : function(id) {
    this._super(id);
    this.html = translate(AppTemplate.FederationView, Lang.Buttons);
    this.html = translate(AppTemplate.FederationView, Lang.FederationView);
    
    this.federation = null;
    this.presenter = null;
    this.editor = null;
    this.userAuthenticationEditor = null;
   
    this.saveDialog = null;
  }	
});


FederationView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};


FederationView.prototype.getEditor = function() {
  return this.editor;	
};


FederationView.prototype.setFederation = function(federation) {
  this.federation = federation;
  this._init();
};


FederationView.prototype.hide = function() {
  this.hideUploadImageDialog();
  this.$id.hide();
};


FederationView.prototype.refreshState = function() {
  if (this.federation.state.running) 
    this.extStartStopButton.dom.innerHTML = Lang.FederationView.stop;
  else 
    this.extStartStopButton.dom.innerHTML = Lang.FederationView.start;
  
  var $runningTime = $(this.extRunningTime.dom);  
  
  if (this.federation.state.running) {
    $runningTime.innerHTML = "Running from " + new Date(this.federation.state.time).format('d M Y - G:i');
    $runningTime.show();
  }
  else {
    $runningTime.innerHTML = "";
    $runningTime.hide();  
  }
};


FederationView.prototype.editConnection = function(connection) {
  var editor = (connection.type == ConnectionTypes.LDAP)? this._createLDAPConnectionEditor(connection) : this._createDatabaseConnectionEditor(connection);
  this.userAuthenticationEditor.setConnectionEditor(editor);   	
  this._switchToConnectionEditor(connection.type);
};


FederationView.prototype.showUploadImageDialog = function() {
  if (! this.uploadDialog) {
    this.uploadDialog = new UploadDialog(this.extUploadImageDialog.dom);
    this.uploadDialog.on(UploadDialog.CLICK_CLOSE, {
      notify : function(event) {
    	this._showLogo();  
    	this.hideUploadImageDialog();  
      }
    }, this);  
  	      	
	this.uploadDialog.on(UploadDialog.CLICK_SUBMIT, {
		notify : function(event) {
		  var form = event.data.form;
		  var callback = {scope: this, fn: function(filename, source) {
		    this.editor.setImage(filename, source);  
		  }};		  
		  this.presenter.uploadImage(form, callback);
		}
    }, this);
  }
  
  this._hideImage();  
  this.uploadDialog.open();
};


FederationView.prototype.hideUploadImageDialog = function() {
  if (this.uploadDialog) this.uploadDialog.close();
  this._showLogo();      
};


FederationView.prototype._showLogo = function() {
  $(this.extChangeLogo.dom).show();  
  $(this.extLogo.dom).show();	
};


FederationView.prototype._hideImage = function() {
  $(this.extChangeLogo.dom).hide();  
  $(this.extLogo.dom).hide();	
};


FederationView.prototype._switchToConnectionEditor = function(type) {
  switch (type) {
    case ConnectionTypes.DATABASE:
      $(this.extDatabaseEditor.dom).show();
      $(this.extLDAPEditor.dom).hide();      
    break;
    case ConnectionTypes.LDAP:
      $(this.extLDAPEditor.dom).show();
      $(this.extDatabaseEditor.dom).hide();      
    break;
  }
};


FederationView.prototype.openSaveDialog = function(saveHandler, discardHandler, scope) {
  if (! this.saveDialog) {
    this.saveDialog = new SaveChangesDialog(this.extSaveDialog.dom);
    this.saveDialog.addButton(Lang.Buttons.save, saveHandler, scope);
    this.saveDialog.addButton(Lang.Buttons.discard, discardHandler, scope);    
  }	  
  this.saveDialog.open();
};


FederationView.prototype.closeSaveDialog = function() {
  if (this.saveDialog) this.saveDialog.close();  	
};


FederationView.prototype._init = function() {
  this.extParent = this.extParent || Ext.get(this.id);
  var content = this.merge(this.html, {federation: this.federation, spaces : this.federation.get(Federation.SPACES).toArray()});
  this.extParent.dom.innerHTML = content;
  
  this.backButton     = Ext.get(this.extParent.query('a[name="back"]').first());
  this.saveButton     = Ext.get(this.extParent.query('a[name="save"]').first());
  this.discardButton  = Ext.get(this.extParent.query('a[name="discard"]').first());
  
  this.extUploadImageDialog   = Ext.get('upload-federation-image-dialog');
  this.extChangeLogo  = Ext.get(this.extParent.query('a[name="change-image"]').first());  
  this.extLogo        = Ext.get(this.extParent.query('img[name="image"]').first());
  
  this.extServerSection = Ext.get(this.extParent.query('div[name="servers"]').first());       
  this.extSpacesSection = Ext.get(this.extParent.query('div[name="spaces"]').first());  
  
  this.extLabel            = Ext.get(this.extParent.query('input[name="label"]').first());
  this.extName             = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extUserCheck        = Ext.get(this.extParent.query('input[id="check-user-password"]').first());
  this.extCertificateCheck = Ext.get(this.extParent.query('input[id="check-certificate"]').first());
  this.extOpenIDCheck      = Ext.get(this.extParent.query('input[id="check-openid"]').first());
  
  this.extSource           = Ext.get(this.extParent.query('select[name="source"]').first());    
    
  this.extDatabaseEditor = Ext.get('database-editor');
  this.extDatabaseUrl      = Ext.get(this.extDatabaseEditor.query('input[name="url"]').first());
  this.extDatabaseUser     = Ext.get(this.extDatabaseEditor.query('input[name="user"]').first());
  this.extDatabasePassword = Ext.get(this.extDatabaseEditor.query('input[name="password"]').first());
  this.extDatabaseEngine   = Ext.get(this.extDatabaseEditor.query('select[name="engine"]').first());
  
    
  this.extLDAPEditor = Ext.get('ldap-editor');
  this.extLDAPUrl        = Ext.get(this.extLDAPEditor.query('input[name="url"]').first());
  this.extLDAPSchema     = Ext.get(this.extLDAPEditor.query('input[name="schema"]').first());
  this.extLDAPUser       = Ext.get(this.extLDAPEditor.query('input[name="user"]').first());
  this.extLDAPPassword   = Ext.get(this.extLDAPEditor.query('input[name="password"]').first());
  
  this.extLDAPCNField    = Ext.get(this.extLDAPEditor.query('input[name="cn_field"]').first());
  this.extLDAPUIDField   = Ext.get(this.extLDAPEditor.query('input[name="uid_field"]').first());
  this.extLDAPEMailField = Ext.get(this.extLDAPEditor.query('input[name="email_field"]').first());
  this.extLDAPLangField  = Ext.get(this.extLDAPEditor.query('input[name="lang_field"]').first());  
  
  this.extSaveDialog = Ext.get(Ids.Elements.DIALOG); 
	  
  this.extServerSection.on('click', this._clickServerHandler, this);
  this.extSpacesSection.on('click', this._clickSpacesHandler, this);  
  
  switch (this.federation.connection.type) {
  
    case ConnectionTypes.DATABASE:
      this.extDatabaseUrl.dom.value = this.federation.connection.url;
      this.extDatabaseUser.dom.value = this.federation.connection.user;
      this.extDatabasePassword.dom.value = this.federation.connection.password;
      this.extDatabaseEngine.dom.selectedIndex = (this.federation.connection.config.databasetype == DatabaseTypes.MYSQL)? 0 : 1;      
    break;
    
    case ConnectionTypes.LDAP:
      this.extLDAPUrl.dom.value = this.federation.connection.url;
      this.extLDAPUser.dom.value = this.federation.connection.user;
      this.extLDAPSchema.dom.value = this.federation.connection.schema;
      this.extLDAPPassword.dom.value = this.federation.connection.password;    
      
      this.extLDAPCNField.dom = this.federation.connection.config.cn;
      this.extLDAPUIDField    = this.federation.connection.config.uid;
      this.extLDAPEMailField  = this.federation.connection.config.email;
      this.extLDAPLangField   = this.federation.connection.config.lang;
    break;        
  }      
  
  this.backButton.on('click', this._backButtonHandler, this);
  this.saveButton.on('click', this._saveButtonHandler, this);
  this.discardButton.on('click', this._discardButtonHandler, this);
  
  this.extChangeLogo.on('click' , this._changeLogoHandler, this);  
  this.extSource.on('change', this._changeConnectionTypeHandler, this);
  
  
  
  $(this.saveButton.dom).addClassName('disabled');
  $(this.extUploadImageDialog.dom).hide();
  
  this._initState();
  
  this.editor = this._createEditor(this.federation);  
};


FederationView.prototype._clickServerHandler = function(event, target, options) {  	
  if (target.nodeName.toLowerCase() != 'a') return;

  event.preventDefault();
  event.stopPropagation();
  
  switch (target.getAttribute('name')) {
    case 'server': this.presenter.openServer(target.getAttribute('code')); break;
    case 'change': this.presenter.changeServer(); break;
  }
};


FederationView.prototype._clickSpacesHandler = function(event, target, options) {  
  if (target.nodeName.toLowerCase() != 'a') return;
  
  event.preventDefault();
  event.stopPropagation();  
  this.presenter.openSpace(target.getAttribute('code'));    
};


FederationView.prototype._backButtonHandler = function(event, target, options) {  
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickBackButton();
};


FederationView.prototype._saveButtonHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickSaveButton();	
};


FederationView.prototype._discardButtonHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickDiscardButton();	
};


FederationView.prototype._changeLogoHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickChangeLogo();	  	
};


FederationView.prototype._changeConnectionTypeHandler = function(event, target, options) {
  var type = target.options[target.selectedIndex].getAttribute('value');
  if (type) this.presenter.changeConnectionType(type);
};


FederationView.prototype._createEditor = function(federation) {  	
  this.userAuthenticationEditor = this._createUserAuthenticationEditor(federation);	
  var federationEditor = this._createFederationEditor(federation, this.userAuthenticationEditor);	
  federationEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  federationEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  
  this._switchToConnectionEditor(federation.connection.type);
  
  return federationEditor;
};


FederationView.prototype._initState = function() {
  this.extRunningTime     = this.extParent.select('.running_time').first();
  this.extStartStopButton = Ext.get(this.extParent.query('a[name="start_stop"]').first()); 
    
  this.extStartStopButton.on('click', function(event, target, options) {
    event.stopPropagation();
    event.preventDefault();
    
    if (this.federation.state.running) this.presenter.stopFederation();
    else this.presenter.startFederation();
  }, this);
   
  this.refreshState();
};


FederationView.prototype._createFederationEditor = function(federation, userAuthenticationEditor) {
  var map = {};

  map[Federation.LABEL]            = {element: this.extLabel.dom,  propertyName: Federation.LABEL};
  map[Federation.LOGO]             = {element: this.extLogo.dom,  propertyName: Federation.LOGO};  
  map[Federation.USER_AUTH]        = {element: this.extUserCheck.dom,   propertyName: Federation.USER_AUTH};  
  map[Federation.CERTIFICATE_AUTH] = {element: this.extCertificateCheck.dom,   propertyName: Federation.CERTIFICATE_AUTH};
  map[Federation.OPENID_AUTH]      = {element: this.extOpenIDCheck.dom,   propertyName: Federation.OPENID_AUTH};
  	  
  var editor = new FederationEditor(federation, userAuthenticationEditor, {map: map});
  return editor;
};


FederationView.prototype._createUserAuthenticationEditor = function(federation) {  
  var sourceEditor     = null;
  var connectionEditor = null;
  var editor = null;
  
  var map = {};
  map[Federation.USER_AUTH] = {element: this.extUserCheck.dom,   propertyName: Federation.USER_AUTH};  
  
  switch (federation.connection.type) {
    case ConnectionTypes.DATABASE:
      connectionEditor = this._createDatabaseConnectionEditor(federation.connection);	
    break;
    
    case ConnectionTypes.LDAP:
      connectionEditor = this._createLDAPConnectionEditor(federation.connection);	 
    break;
    case ConnectionTypes.MOCK:
    break;	 
  }
  
  sourceEditor = new SelectEditor(this.extSource.dom, federation, Federation.CONNECTION_TYPE);
  editor = new UserAuthenticationEditor(federation, sourceEditor, connectionEditor, {map: map});
  
  return editor;	
};


FederationView.prototype._createDatabaseConnectionEditor = function(connection) {
  var map = {};
  map[DatabaseConnection.URL]      = {element: this.extDatabaseUrl.dom,   propertyName: DatabaseConnection.URL};
  map[DatabaseConnection.USER]     = {element: this.extDatabaseUser.dom,   propertyName: DatabaseConnection.USER};
  map[DatabaseConnection.PASSWORD] = {element: this.extDatabasePassword.dom, propertyName: DatabaseConnection.PASSWORD};
  map[DatabaseConnectionConfig.DATABASE_TYPE] = {element: this.extDatabaseEngine.dom, propertyName: DatabaseConnectionConfig.DATABASE_TYPE};
	    
  var editor = new DatabaseConnectionEditor(connection, {map: map});
  return editor;
};


FederationView.prototype._createLDAPConnectionEditor = function(connection) {  
  var map = {};
  map[LDAPConnection.URL]          = {element: this.extLDAPUrl.dom,   propertyName: LDAPConnection.URL};
  map[LDAPConnection.USER]         = {element: this.extLDAPUser.dom,   propertyName: LDAPConnection.USER};
  map[LDAPConnection.PASSWORD]     = {element: this.extLDAPPassword.dom, propertyName: LDAPConnection.PASSWORD};
  
  map[LDAPConnectionConfig.SCHEMA] = {element: this.extLDAPSchema.dom, propertyName: LDAPConnectionConfig.SCHEMA};
  map[LDAPConnectionConfig.CN_FIELD]    = {element: this.extLDAPCNField.dom, propertyName: LDAPConnectionConfig.CN_FIELD};
  map[LDAPConnectionConfig.UID_FIELD]   = {element: this.extLDAPUIDField.dom, propertyName: LDAPConnectionConfig.UID_FIELD};
  map[LDAPConnectionConfig.EMAIL_FIELD] = {element: this.extLDAPEMailField.dom, propertyName: LDAPConnectionConfig.EMAIL_FIELD};
  map[LDAPConnectionConfig.LANG_FIELD]  = {element: this.extLDAPLangField.dom, propertyName: LDAPConnectionConfig.LANG_FIELD};
	  
  var editor = new LDAPConnectionEditor(connection, {map: map});
  return editor;
};


FederationView.prototype._changeEditorHandler = function(event) {
  if (this.editor.isDirty()) $(this.saveButton.dom).removeClassName('disabled');
  else $(this.saveButton.dom).addClassName('disabled');  
};

var FederationActivity = Activity.extend({
  init : function() {
    this.view = this._createView();
    this.view.setPresenter(this);
    this.service = GridedService;
    this.federation = null;
  }	
});


FederationActivity.prototype.start = function(data) {
  if (!this.federation || this.federation.id != data.id) this._clearModel();	
	
  if (!this.view) this.view = this._createView();
  
  if (this.federation) this._showView(this.federation);
  if (!this.federation) this.service.loadFederation(data.id, {
	 context: this,
	 success: function(federation) {
	   this.federation = federation;
	   this.currentConnection = this.federation.connection;
	   this.view.setFederation(this.federation);
	   this._showView(this.federation);
	 },
	 failure : function(ex) {
		throw ex;		 
	 }
  });
};


FederationActivity.prototype.stop = function() {
  var editor = this.view.getEditor();
  if (editor) editor.close();
  this._unlink(this.federation);	
  this.view.hide();	
};


FederationActivity.prototype.canStop = function(callback) {
 var editor = this.view.getEditor();
 if (! editor.isDirty()) { callback.success.call(callback.context, {}); return; }; 
		 
 var continueCallback = {
   context: this,
   success : function() { callback.success.call(callback.context, {}); },
   failure : function() { alert("Error saving federation"); }     
 };	 
	 
 var saveCallback = {
   context: this,
   save: function() { this._saveFederation(continueCallback); },
   cancel : function() { editor.reset(); continueCallback.success.call(continueCallback.context, null); }   
 };
	 
 this._askForSaveChanges(saveCallback); 
  	  
};


FederationActivity.prototype.notify = function(event) {
  if (event.name == Events.CHANGED) {
    switch (event.propertyName) {
      case Federation.STATE :  
        this.view.refreshState();
      break;
    }       
  }    	  
};


FederationActivity.prototype.clickChangeLogo = function() {
  this.view.showUploadImageDialog();
};


FederationActivity.prototype.uploadImage = function(form, callback) {
  var params = {server_id: this.federation.server.id, federation_id: this.federation.id, model_type: ModelTypes.FEDERATION};
  
  this.service.uploadImage(form, params, {
    context: this,	  
    success: function(name, source) {           	
      this.view.hideUploadImageDialog();
      callback.fn.call(callback.scope, name, source);
    },
    failure : function() {
      this.view.hideUploadImageDialog();
    }
  });	
};


FederationActivity.prototype.openServer = function(id) {
  var event = {name: Events.OPEN_SERVER, token: new ServerPlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	 
};


FederationActivity.prototype.openSpace = function(id) {
  var event = {name: Events.OPEN_SPACE, token: new SpacePlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	
};


FederationActivity.prototype.clickBackButton = function() {
  this._goBack();
};


FederationActivity.prototype.clickDiscardButton = function() {
  var editor = this.view.getEditor();
  editor.reset();
  this._goBack();
};


FederationActivity.prototype.clickSaveButton = function() {
  var continueCallback = {
    context : this,
    success : function() { console.log("Federation saved"); },
    failure : function() { console.log("Error saving federation"); }	    
  };
  
  this._saveFederation(continueCallback);  
};


FederationActivity.prototype.changeConnectionType = function(type) {
  var connection = this.federation.connection;
  
  if (this.federation.connection.type != type) {
    connection = (type == ConnectionTypes.LDAP)? new LDAPConnection() : new DatabaseConnection();    
  }

  this.currentConnection = connection;
  this.view.editConnection(this.currentConnection);   
};


FederationActivity.prototype.startFederation = function() {
  NotificationManager.showNotification(Lang.Notifications.STARTING);
  
  this.service.startFederation(this.federation.id, {
    context: this,
    success : function(federation) {
      NotificationManager.showNotification(Lang.Notifications.STARTED, 1000);
      
      this.federation.set(Federation.STATE, federation.get(Federation.STATE));      
    },
    failure : function(ex) {
      throw ex;
    }
  });  
};


FederationActivity.prototype.stopFederation = function() {
  NotificationManager.showNotification(Lang.Notifications.STOPPING);
  
  this.service.stopFederation(this.federation.id, {    
    context: this,
    success : function(federation) {
      NotificationManager.showNotification(Lang.Notifications.STOPPED, 1000);
      
      this.federation.set(Federation.STATE, federation.get(Federation.STATE));
    },
    failure : function(ex) {
      throw ex;
    }
  });  
};


FederationActivity.prototype._saveFederation = function(callback) {
  var editor = this.view.getEditor();
  if (! editor.isDirty()) { callback.success.call(callback.context, null); return; };

  editor.flush();
  if (this.federation.error) {
	  editor.showError(this.federation.error);	  
	  return; 
  }
  
  this.federation.connection = this.currentConnection;
  
  this.service.saveFederation(this.federation, {
    context : this,
    success : function(federation) {    	
      var event = {name: Events.FEDERATION_SAVED, data:{federation : this.federation} };
      EventBus.fire(event);	 
      callback.success.call(callback.context, null);
      
      NotificationManager.showNotification(Lang.Notifications.FEDERATION_SAVED, 1000);
    },
    failure : function() {
      callback.failure.call(callback.context, null);
      NotificationManager.showNotification(Lang.Notifications.SAVE_FEDERATION_ERROR, 1000);
    }
  });
};


FederationActivity.prototype._goBack = function() {
  var event = {name : Events.OPEN_DEPLOYMENT, token : new DeploymentPlace().toString(), data : {}};
  EventBus.fire(event);
};


FederationActivity.prototype._showView = function(federation) {
  this.view.getEditor().open(federation);	
  this._link(federation);
  this.view.show();	
};


FederationActivity.prototype._createView = function() {
  var view = new FederationView(Ids.Elements.FEDERATION_VIEW, AppTemplate.FederationView);
  return view;
};


FederationActivity.prototype._askForSaveChanges = function(callback) {
  var saveHandler = function() { 
    this.view.closeSaveDialog();
    callback.save.call(callback.context, null);	  
  };   

  var discardHandler  = function() { 
    this.view.closeSaveDialog();    
    callback.cancel.call(callback.context, null);
  };
				    	    
  this.view.openSaveDialog(saveHandler, discardHandler, this);
};


FederationActivity.prototype._link = function(model) {
  model.on(Events.CHANGED, this, this);	
};


FederationActivity.prototype._unlink = function(model) {
  if (!model) return;
  model.un(Events.CHANGED, this);
};


FederationActivity.prototype._clearModel = function() {
  this.federation = null;
};

var Space = Model.extend({
  init: function() {
    this._super();
    this.publicationService = new Collection();
    
    this.validations = {
      'name' : Validators.Required,
      'url' : Validators.Required,
    };    
  }
});


Space.ID = 'id';
Space.NAME = 'name';
Space.LABEL = 'label';
Space.URL = 'url';
Space.LOGO = 'logo';
Space.FEDERATION = 'federation';
Space.SERVICES = 'services';
Space.DATAWAREHOUSE = 'datawarehouse';
Space.MODEL_VERSION = "model_version";
Space.STATE = "state";


Space.prototype.getService = function(id) {
  var collection = this.get(Space.SERVICES);
  return collection.getById(id);   
};


Space.prototype.addService = function(statement, options) {
  var collection = this.get(Space.SERVICES);	
  collection.add(statement, {silent : true});
  if (! options || !options.silent) this.fire({name: Events.ADDED, data: {model : this, collection: collection, item: statement}});
};


Space.prototype.removeService = function(ids, options) {
  var collection = this.get(Space.SERVICES);  	
  collection.remove(ids, {silent: true});
  if (! options || !options.silent) this.fire({name: Events.REMOVED, data: {model : this, collection: collection}});	
};


var PublicationService = Model.extend({
  init : function(name, type, published) {
    this._super();
    this.id = name;
    this.name = name;
    this.type = type;
    this.published = published;
  }
});

PublicationService.NAME = 'name';
PublicationService.TYPE = 'type';
PublicationService.PUBLISHED = 'published';

Lang.SpaceView = {		
  publish :  'Publish',		 
  import : 'Import',
  console : 'Console',
  server : 'Server',
  federation: 'Federation',
  name : 'Name',
  label : 'Label',
  url : 'URL',
  model: 'Model',
  change : 'Change',  
  space : 'Business Space',
  use_datawarehouse: 'Use a DataWarehouse',
  stop : 'Stop',
  start : 'Start',
  state : 'State'
};

Lang.ServicesView = {
  text : 'Check the items you want to be accessible from other business units'		
};

var ServicesEditor = Editor.extend({
  init : function(services, options) {
	this._super();  
    this.services = services;
    this.options = options || {};
    this.options.type = 'composed';
    
    this._init();
  }	
});


ServicesEditor.prototype.setServices = function(services) {
  this.editors = [];
  this._setServices(services);  
};


ServicesEditor.prototype.addEditor = function(editor) {
  editor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
  editor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);

  this.editors.push(editor);  	
};


ServicesEditor.prototype._init = function() {   
  this.editors = [];  
};


ServicesEditor.prototype._setServices = function(services) {
  this.services = services;
  
  var el = PublicationService;	
  var map = this.options.map;  
  
  this.services.each(function(service, index) {
    var editor = new CheckboxEditor(map[el.PUBLISHED].element, service, map[el.PUBLISHED].propertyName);
    editor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
    editor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);
    this.editors.push(editor);
  }, this);        	
};


ServicesEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor : this, data : event.data.value}};
  this.fire(ev);  
};

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


ServicesView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};


ServicesView.prototype.getEditor = function() {
  return this.editor;	
};


ServicesView.prototype.setServices = function(services) {  	
  this.services = services;
  if (! this.initialized) this._init();  
  this.refresh();
};


ServicesView.prototype.filterServiceByType = function(type) {
  if (type == ServiceTypes.ALL) this.table.showAll();
  else this.table.filterBy('type', type);	
};


ServicesView.prototype.refresh = function() {
  elements = this._adapt(this.services);
  this.table.setData(elements);	
  
  if (! this.editor) {
    this.editor = this._createEditor(this.services);
  } else {
    this.editor.setServices(this.services);
  }
};


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


ServicesView.prototype._changeServiceTypeHandler = function(event, target, options) {
  var option = target.options[target.selectedIndex];
  var type = option.value;
  this.presenter.changeServiceType(type);  
};


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


ServicesView.prototype._adapt = function(services) {
  var elements = [];
  this.services.each(function(service, index) {
    var element = {id: service.get(PublicationService.NAME), name: service.get(PublicationService.NAME), type : service.get(PublicationService.TYPE)};
    elements.push(element);
  }, this);
    
  return elements;
};

var SpaceEditor = Editor.extend({
  init : function(space, options) {
    this._super();
    this.space = space;
    this.options = options || {};
    this.options.type = 'composed';
    
    this.labelEditor = null;
    this.datawarehouseEditor = null;
    this.imageEditor = null;
    this.servicesEditor = null;
    
    this._init();
  }	
});


SpaceEditor.prototype.setServicesEditor = function(servicesEditor) {
  this.servicesEditor = servicesEditor;
  this.editors.push(this.servicesEditor);
};


SpaceEditor.prototype.setImage = function(filename, source) {
  this.imageEditor.setImage(filename, source);	
};


SpaceEditor.prototype._init = function(servicesEditor) {
  var sp = Space;
  var map = this.options.map;
  
  this.labelEditor = new TextEditor(map[sp.LABEL].element, this.space, map[sp.LABEL].propertyName);
  this.imageEditor = new ImageEditor(map[sp.LOGO].element, this.space, map[sp.LOGO].propertyName);
  this.datawarehouseEditor = new CheckboxEditor(map[sp.DATAWAREHOUSE].element, this.space, map[sp.DATAWAREHOUSE].propertyName);
  
  this.labelEditor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
  this.labelEditor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);
  this.imageEditor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
  this.imageEditor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);
  this.datawarehouseEditor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
  this.datawarehouseEditor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);
    
  this.editors.push(this.labelEditor);
  this.editors.push(this.datawarehouseEditor);
  this.editors.push(this.imageEditor);  
};


SpaceEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor : this, data : event.data.value}};
  this.fire(ev);
};

Lang.ImportSpaceDataDialog = {
  import : 'Import',
  importer_type: 'Importer type',
  space_data_file : 'Space data file'
};

var ImportSpaceDataDialog = Dialog.extend({
  init : function(element) {
    this._super(element);
    this.html = translate(AppTemplate.ImportSpaceDataDialog, Lang.ImportSpaceDataDialog);
    element.innerHTML = this.html;
    
    this._bind();
  }
});

ImportSpaceDataDialog.CLICK_IMPORT = 'click_import';


ImportSpaceDataDialog.prototype.getData = function() {      
  var data = {
    form : this.extForm.dom,
    importerTypeId : this.extSelect.dom.options[this.extSelect.dom.selectedIndex].value
  };
  return data;
};


ImportSpaceDataDialog.prototype.setImporterTypes = function(importerTypes) {      
  this.importerTypes = importerTypes;
  
  for (var i=0, l = importerTypes.length; i < l; i++) {
    var type = importerTypes[i];
    this.extSelect.dom.options[this.extSelect.dom.options.length] = new Option(type.label, type.id);
  }  
};


ImportSpaceDataDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.$el); 
  
  this.extForm   = Ext.get(this.extParent.query('form').first());
  this.extSelect = Ext.get(this.extParent.query('select[name="importer_type"]').first());
  this.extFile   = Ext.get(this.extParent.query('input[type="file"]').first());
  this.extImportButton = Ext.get(this.extParent.query('input[name="import-button"]').first());
  this.extFilename = Ext.get(this.extParent.query('div[name="filename"]').first());
  
  this.extImportButton.on('click', this._importButtonHandler, this);  
  this.extFile.on('change', this._changeFileHandler, this);
    
  this.extImportButton.dom.disabled = true;
};


ImportSpaceDataDialog.prototype._importButtonHandler = function(event, target, options) {
  var ev = {name : ImportSpaceDataDialog.CLICK_IMPORT, data: this.getData()};
  this.fire(ev);  
};


ImportSpaceDataDialog.prototype._changeFileHandler = function(event, target, options) {
  this.extFilename.dom.innerHTML = this.extFile.dom.value;
  if (this.extFilename != "") this.extImportButton.dom.disabled = false;
};

Lang.ImportProgressComposite = {
  progress : 'Progress',
  estimated_time : 'Estimated time',
  stop : 'Stop',
  importing : 'Importing data...'
};

var ImportProgressComposite = EventsSource.extend({  
  init : function(element) {
    this._super();
    this.$element = $(element);
    
    this._init();
  }
});
ImportProgressComposite.CLICK_STOP = 'click_stop';


ImportProgressComposite.prototype.show = function() {
  this.$element.show();
};


ImportProgressComposite.prototype.hide = function() {
  this.$element.hide();
};


ImportProgressComposite.prototype.setProgress = function(progress) {
  this.progress = progress;  
  this._refresh();
};


ImportProgressComposite.prototype._init = function() {
  this.extParent = Ext.get(this.$element);
  var template = translate(AppTemplate.ImportProgressComposite, Lang.ImportProgressComposite);
  if (! this.progress) this.progress = {value : 0, estimatedTime : 0};
  var html = _.template(template, {progress : this.progress});
  
  this.extParent.dom.innerHTML = html;
  this.extProgress      = this.extParent.select('.progress').first();
  this.extEstimatedTime = this.extParent.select('.estimated_time').first();        
  this.extStopButton    = Ext.get(this.extParent.query('input[name="stop_button"]').first());
  
  this.extStopButton.on('click', this._stopButtonHandler, this);
};


ImportProgressComposite.prototype._refresh = function() {
  if (! this.progress) return;
  this.extProgress.dom.innerHTML = this.progress.value;
  this.extEstimatedTime.dom.innerHTML = this.progress.estimatedTime;  
};


ImportProgressComposite.prototype._stopButtonHandler = function(event, target, options) {
  ev = {name : ImportProgressComposite.CLICK_STOP , data : {progress : this.progress}};
  this.fire(ev);
};

Lang.ImportResultComposite = {
  close : 'Close'  
};

var ImportResultComposite = EventsSource.extend({
  init : function(element) {
    this._super();
    this.$element = $(element);
    
    this._init();
  }
});
ImportResultComposite.CLICK_CLOSE = 'click_close';


ImportResultComposite.prototype.setResult = function(result) {
  this.result = result;
  if (! this._initialized) this._init();  
};


ImportResultComposite.prototype.show = function() {
  this.$element.show();
};


ImportResultComposite.prototype.hide = function() {
  this.$element.hide();
};


ImportResultComposite.prototype._init = function() {
  this.extParent = Ext.get(this.$element);
  var html = translate(AppTemplate.ImportResultComposite, Lang.ImportResultComposite);
  this.extParent.dom.innerHTML = html;
  
  this.extMessage  = this.extParent.select('.message').first();
  this.extDuration = this.extParent.select('.duration').first();
  this.extCloseButton = Ext.get(this.extParent.query('input[name="close_button"]').first());

  if (! this.result) this.result = {message: '', duration: 0};
  
  this.extMessage.dom.innerHTML = this.result.message;
  this.extDuration.dom.innerHTML = this.result.duration;
      
  this.extCloseButton.on('click', this._closeButtonHandler, this);
};


ImportResultComposite.prototype._closeButtonHandler = function(event, target, options) {
  ev = {name : ImportResultComposite.CLICK_CLOSE , data :{}};  
  this.fire(ev);
};

var ImportSpaceDataWizardDialog = Dialog.extend({
  init : function(element, elements) {
    this._super(element);
    this.elements = elements;
    this.wizard = new Wizard();
    this._bind();
  }
});
ImportSpaceDataWizardDialog.CLICK_IMPORT = 'click_import';
ImportSpaceDataWizardDialog.CLICK_STOP   = 'click_stop';
ImportSpaceDataWizardDialog.CLICK_CLOSE  = 'click_close';


ImportSpaceDataWizardDialog.prototype.setImporterTypes = function(importerTypes) {
  this.importDialog.setImporterTypes(importerTypes);
};


ImportSpaceDataWizardDialog.prototype.setProgress = function(progress) {
  this.progressComposite.setProgress(progress);
};


ImportSpaceDataWizardDialog.prototype.setResult = function(result) {
  this.resultComposite.setResult(result);  
};


ImportSpaceDataWizardDialog.prototype.open = function() {
  this.$element.show();
  this.wizard.start();
};


ImportSpaceDataWizardDialog.prototype._init = function() {
  this.progressComposite = null;
  this.importDialog = null;
  this.resultComposite = null;
};


ImportSpaceDataWizardDialog.prototype._bind = function() {
  this.progressComposite = this._getProgressComposite();
  this.importDialog      = this._getImportDialog();
  this.resultComposite   = this._getResultComposite();
  
  this.importDialog.on(ImportSpaceDataDialog.CLICK_IMPORT, {notify : function(event) {this._clickImportButtonHandler(event);}}, this);
  this.progressComposite.on(ImportProgressComposite.CLICK_STOP, {notify : function(event) {this._clickStopButtonHandler(event);}}, this);
  this.resultComposite.on(ImportResultComposite.CLICK_CLOSE, {notify : function(event) {this._clickCloseButtonHandler(event);}}, this);
    
  var firstPage  = new Page(this.importDialog); 
  var secondPage = new Page(this.progressComposite); 
  var thirdPage  = new Page(this.resultComposite); 
  
  var dialog = this.importDialog;
  firstPage.show = function() { dialog.open(); };
  firstPage.hide = function() { dialog.close(); };
    
  this.wizard.addPage(firstPage);
  this.wizard.addPage(secondPage);
  this.wizard.addPage(thirdPage);    
};


ImportSpaceDataWizardDialog.prototype._clickImportButtonHandler = function(event) {
  this.wizard.next();
  this.fire(event);  
};


ImportSpaceDataWizardDialog.prototype._clickStopButtonHandler = function(event) {
  this.wizard.next();
  this.fire(event);
};


ImportSpaceDataWizardDialog.prototype._clickCloseButtonHandler = function(event) {
  this._init();
  this.wizard.start();
  this.fire(event);
};



ImportSpaceDataWizardDialog.prototype._getImportDialog = function() {
  var importDialog = this.importDialog;
  
  if (importDialog == null) {
    var importDialog = new ImportSpaceDataDialog(this.elements.import_dialog);    
    importDialog.on(ImportSpaceDataDialog.CLICK_IMPORT_EVENT, {notify: function(event) {
      var form = event.data.form;
      var importerTypeId = event.data.importerTypeId;
      
      this.presenter.startImportation(form, importerTypeId);
    }}, this);    
  }
  return importDialog; 
};


ImportSpaceDataWizardDialog.prototype._getProgressComposite = function() {
  var resultComposite = this.resultComposite;
  if (resultComposite == null) {
    resultComposite = new ImportProgressComposite(this.elements.progress);
    resultComposite.hide();
  }
  return resultComposite;
};


ImportSpaceDataWizardDialog.prototype._getResultComposite = function() {
  var resultComposite = this.resultComposite;
  if (resultComposite == null) {
    resultComposite = new ImportResultComposite(this.elements.result);
    resultComposite.hide();
  }
  return resultComposite;
};

Lang.ImportSpaceDataView = {
  importation_canceled : 'Importation cancelled by user', 
  importation_finised  : 'Importation finished successfully'  
};

var ImportSpaceDataView = View.extend({
  init : function(id) {
    this._super(id);
    this.html = AppTemplate.ImportSpaceDataView;
    this.importerTypes = null; 
    this.progress = null;
    
    this.importWizard = null;
    this._init();
  }
}); 


ImportSpaceDataView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};


ImportSpaceDataView.prototype.setImporterTypes = function(importerTypes) {    
  this.importWizard.setImporterTypes(importerTypes);
};


ImportSpaceDataView.prototype.setProgress = function(progress) {  
  this.importWizard.setProgress(progress);
};


ImportSpaceDataView.prototype.setResult = function(result) {  
  this.importWizard.setResult(result);
};


ImportSpaceDataView.prototype.startImportation = function() {
  this.isImportationStarted = true;  
};


ImportSpaceDataView.prototype.stopImportation = function() {
  this.isImportationStarted = false;
};


ImportSpaceDataView.prototype._init = function() {
  this.extParent = this.extParent || Ext.get(this.id);
  var content = this.merge(this.html, {importerTypes : this.importerTypes});
  this.extParent.dom.innerHTML = content;      
  
  this.extImportWizard = Ext.get('import-wizard');
  this.extImportDialog = this.extParent.select('.import-dialog').first();
  this.extProgressComposite = Ext.get('import-progress');
  this.extResultComposite = Ext.get('import-result');
  
  var elements = {
    import_dialog : this.extImportDialog.dom,
    progress : this.extProgressComposite.dom,
    result : this.extResultComposite.dom
  };
  
  this.importWizard = new ImportSpaceDataWizardDialog(this.extImportWizard.dom, elements);     
  this.importWizard.on(ImportSpaceDataWizardDialog.CLICK_IMPORT, { notify: function(event) { this._clickImportButtonHandler(event);}}, this);
  this.importWizard.on(ImportSpaceDataWizardDialog.CLICK_STOP, { notify: function(event) { this._clickCancelButtonHandler(event);}}, this);
  this.importWizard.on(ImportSpaceDataWizardDialog.CLICK_CLOSE, { notify: function(event) { this._clickCloseButtonHandler(event);}}, this);   
};


ImportSpaceDataView.prototype._clickImportButtonHandler = function(event) {
  var form = event.data.form;
  var importerTypeId = event.data.importerTypeId;
  this.presenter.startImportation(form, importerTypeId);
};


ImportSpaceDataView.prototype._clickCancelButtonHandler = function(event) {
  var result = {message : Lang.importation_canceled, duration : event.data.progress.duration};
  this.importWizard.setResult(result);
  this.presenter.stopImportation();
};


ImportSpaceDataView.prototype._clickCloseButtonHandler = function(event) {
  this.presenter.stopImportation();
};

var SpaceView = View.extend({
  init : function(id) {
    this._super(id);    
    this.html = translate(AppTemplate.SpaceView, Lang.SpaceView);
    
    this.space = null;
    this.editor = null;
    this.saveDialog = null;
    this.uploadDialog = null;
    
    this._spaceInitialized = false;
    this._publishTabInitialized = false;
  }
}); 


SpaceView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};


SpaceView.prototype.getEditor = function() {
  return this.editor;   
};


SpaceView.prototype.setSpace = function(space) {
  this.space = space;  
  if (! this._spaceInitialized) this._init();
  this._refresh();
  
};


SpaceView.prototype.setServices = function(services) {
  this.services = services; 
};


SpaceView.prototype.setProgress = function(progress) {
  this.progress = progress;
  if (this.importSpaceDataView) this.importSpaceDataView.setProgress(this.progress);     
};


SpaceView.prototype.setImporterTypes = function(importerTypes) {
  this.importerTypes = importerTypes;
  if (this.importSpaceDataView) this.importSpaceDataView.setImporterTypes(importerTypes);  
};


SpaceView.prototype.finishImportation = function() {
  if (this.importSpaceDataView) this.importSpaceDataView.setResult(Lang.ImportSpaceDataView.importation_finised);
};


SpaceView.prototype.hide = function() {
  this.hideUploadImageDialog();
  this.$id.hide();
};


SpaceView.prototype.refreshState = function() {
  if (this.space.state.running) 
    this.extStartStopButton.dom.innerHTML = Lang.SpaceView.stop;
  else 
    this.extStartStopButton.dom.innerHTML = Lang.SpaceView.start;
  
  var $runningTime = $(this.extRunningTime.dom);  
  
  if (this.space.state.running) {
    $runningTime.innerHTML = "Running from " + new Date(this.space.state.time).format('d M Y - G:i');
    $runningTime.show();
  }
  else {
    $runningTime.innerHTML = "";
    $runningTime.hide();  
  }
};


SpaceView.prototype.filterServiceByType = function(type) {
  if (this.servicesView) this.servicesView.filterServiceByType(type);	
};


SpaceView.prototype.openSaveDialog = function(saveHandler, discardHandler, scope) {
  if (! this.saveDialog) {
    this.saveDialog = new SaveChangesDialog(this.extSaveDialog.dom);
    this.saveDialog.addButton(Lang.Buttons.save, saveHandler, scope);
    this.saveDialog.addButton(Lang.Buttons.discard, discardHandler, scope);
  }
  this.saveDialog.open();
};


SpaceView.prototype.closeSaveDialog = function() {
  if (this.saveDialog) this.saveDialog.close(); 
};


SpaceView.prototype.showUploadImageDialog = function() {
  if (! this.uploadDialog) {
    this.uploadDialog = new UploadDialog(this.extUploadImageDialog.dom);
    this.uploadDialog.on(UploadDialog.CLICK_CLOSE, {
      notify : function(event) {
        this._showLogo();
        this.hideUploadImageDialog();
      }    	
    }, this);
    
	this.uploadDialog.on(UploadDialog.CLICK_SUBMIT, {
		notify : function(event) {
		  var form = event.data.form;
		  var callback = {scope: this, fn: function(filename, source) {
		    this.editor.setImage(filename, source);  
		  }};		  
		  this.presenter.uploadImage(form, callback);
		}
    }, this);
  }
  
  this._hideImage();
  this.uploadDialog.open();
};


SpaceView.prototype.hideUploadImageDialog = function() {
  if (this.uploadDialog) this.uploadDialog.close();
  this._showLogo();
};


SpaceView.prototype._showLogo = function() {
  $(this.extChangeLogo.dom).show();  
  $(this.extLogo.dom).show();	  	
};


SpaceView.prototype._hideImage = function() {
  $(this.extChangeLogo.dom).hide();  
  $(this.extLogo.dom).hide();
};


SpaceView.prototype._refresh = function() {
  this.extName.dom.value = this.space.name;
  this.extLabel.dom.value = this.space.label;
  
  var serverSection = this.merge(AppTemplate.ServerSection, {server : this.space.federation.server});
  serverSection = translate(serverSection, Lang.SpaceView);
  this.extServerSection.dom.innerHTML = serverSection;
  
  var federationSection = this.merge(AppTemplate.FederationSection, {federation: this.space.federation});
  federationSection = translate(federationSection, Lang.SpaceView);
  this.extFederationSection.dom.innerHTML = federationSection;
  
  this._activatePublishTabHandler();
  this.refreshState();
  

};


SpaceView.prototype._init = function() {
  this.extParent = this.extParent || Ext.get(this.id);
  var content = this.merge(this.html, {space : this.space});
  this.extParent.dom.innerHTML = content;      
  
  this.backButton     = Ext.get(this.extParent.query('a[name="back"]').first());
  this.saveButton     = Ext.get(this.extParent.query('a[name="save"]').first());
  this.discardButton  = Ext.get(this.extParent.query('a[name="discard"]').first());

  this.extUploadImageDialog   = Ext.get('upload-space-image-dialog');
    
  this.extChangeLogo  = Ext.get(this.extParent.query('a[name="change-image"]').first());
  this.extLogo        = Ext.get(this.extParent.query('img[name="image"]').first()); 
  
  this.extServerSection     = Ext.get(this.extParent.query('div[name="server"]').first());       
  this.extFederationSection = Ext.get(this.extParent.query('div[name="federation"]').first());  
  this.extModelSection      = Ext.get(this.extParent.query('div[name="model"]').first());  
  
  this.extName  = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extLabel = Ext.get(this.extParent.query('input[name="label"]').first());    
  this.extDatawarehouse = Ext.get(this.extParent.query('input[name="datawarehouse"]').first()); 
  
  this.extTabs = new Ext.TabPanel("tabs");
  this.publishTab = this.extTabs.addTab(Ids.Elements.PUBLISH_TAB, Lang.SpaceView.publish);  
  this.importTab  = this.extTabs.addTab(Ids.Elements.IMPORT_TAB, Lang.SpaceView.import);    

  this.extSaveDialog = Ext.get(Ids.Elements.DIALOG); 
  
  this.extServerSection.on('click', this._clickServerHandler, this);
  this.extFederationSection.on('click', this._clickFederationHandler, this);
  this.extModelSection.on('click', this._clickModelHandler, this);
  
  this.backButton.on('click', this._backButtonHandler, this);
  this.saveButton.on('click', this._saveButtonHandler, this);
  this.discardButton.on('click', this._discardButtonHandler, this);   
  this.extChangeLogo.on('click', this._changeLogoHandler, this);
  
  this.publishTab.on('activate', this._activatePublishTabHandler, this);
  this.importTab.on('activate', this._activateImportTabHandler, this);
      
  this.extTabs.on('tabchange', function(tabPanel) {
    var tab = tabPanel.getActiveTab();
    if (!tab) return;
    tab.activate();
  }, this);
  
  $(this.extTabs.el).show();
  this.publishTab.activate();
  $(this.extUploadImageDialog.dom).hide();
  this.extTabs.endUpdate();
    
  this.editor = this._createEditor(this.space);
  $(this.saveButton.dom).addClassName('disable');
  
  this._initState();
  
  this._spaceInitialized = true;
};


SpaceView.prototype._clickServerHandler = function(event, target, options) {  	
  if (target.nodeName.toLowerCase() != 'a') return;

  event.preventDefault();
  event.stopPropagation();  
  this.presenter.openServer(target.getAttribute('code')); 
};


SpaceView.prototype._clickFederationHandler = function(event, target, options) {
  if (target.nodeName.toLowerCase() != 'a') return;
	  
  event.preventDefault();
  event.stopPropagation();  
  this.presenter.openFederation(target.getAttribute('code'));    	
};


SpaceView.prototype._clickModelHandler = function(event, target, options) {
  if (target.nodeName.toLowerCase() != 'a') return;
	  
  event.preventDefault();
  event.stopPropagation();  
  this.presenter.openModel(target.getAttribute('code'));    	
};


SpaceView.prototype._backButtonHandler = function(event, target, options) {  
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickBackButton();
};


SpaceView.prototype._saveButtonHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickSaveButton();	
};


SpaceView.prototype._discardButtonHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickDiscardButton();	
};


SpaceView.prototype._changeLogoHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickChangeLogo();	  	
};


SpaceView.prototype._activatePublishTabHandler = function(event, target, options) {
  this.presenter.clickPublishTab();	
};


SpaceView.prototype._activateImportTabHandler = function(event, target, options) {
  this.presenter.clickImportTab();	
};


SpaceView.prototype.showPublishTab = function() {
  if (this.servicesView == null) {
    this.servicesView = new ServicesView(this.publishTab.bodyEl.id);
    this.servicesView.setPresenter(this.presenter);
    this.servicesView.setServices(this.services);
    
    var servicesEditor = this.servicesView.getEditor();
    servicesEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
    servicesEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
    
    this.editor.setServicesEditor(this.servicesView.getEditor());  
  }
};


SpaceView.prototype.showImportTab = function() {
  if (this.importSpaceDataView == null) {
    this.importSpaceDataView = new ImportSpaceDataView(this.importTab.bodyEl.id);
    this.importSpaceDataView.setPresenter(this.presenter);
    this.importSpaceDataView.setImporterTypes(this.importerTypes);    
  }
  this.importSpaceDataView.show();
};


SpaceView.prototype._initState = function() {
  this.extRunningTime     = this.extParent.select('.running_time').first();
  this.extStartStopButton = Ext.get(this.extParent.query('a[name="start_stop"]').first()); 
    
  this.extStartStopButton.on('click', function(event, target, options) {
    event.stopPropagation();
    event.preventDefault();
    
    if (this.space.state.running) this.presenter.stopSpace();
    else this.presenter.startSpace();
  }, this);
   
  this.refreshState();
};


SpaceView.prototype._createEditor = function(space) {
  var map = {};
  
  map[Space.LABEL] = {element: this.extLabel.dom, propertyName: Space.LABEL};
  map[Space.LOGO]  = {element: this.extLogo.dom, propertyName: Space.LOGO};
  map[Space.DATAWAREHOUSE] = {element: this.extDatawarehouse.dom, propertyName: Space.DATAWAREHOUSE};
    
  this.editor = new SpaceEditor(space, {map: map});
  this.editor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.editor.on(Events.RESTORED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  
  return this.editor;
};


SpaceView.prototype._changeEditorHandler = function(event) {
  if (this.editor.isDirty()) $(this.saveButton.dom).removeClassName('disabled');
  else $(this.saveButton.dom).addClassName('disabled');  
};

var SpaceActivity = Activity.extend({
  init : function() {
    this.view = this._createView();
    this.view.setPresenter(this);
    this.service = GridedService;
    
    this.space = null;
    this.services = null;    
  }	
});


SpaceActivity.prototype.start = function(data) {
  if (!this.space || this.space.id != data.id) this._clearModel();	
	
  if (!this.view) this.view = this._createView();
  
  if (this.space) this._showView(this.space);
  if (! this.space) this.service.loadSpace(data.id, {
	 context: this,
	 success: function(space) {
	   this.space = space;	   
	   this.view.setSpace(this.space);
	   this._showView(this.space);
	 },
	 failure : function(ex) {
		 throw ex; 
	 }
  });
};


SpaceActivity.prototype.stop = function() {  
  var editor = this.view.getEditor();
  if (editor) editor.close();  
  if (this.pooler) this.pooler.stop();
  this._unlink(this.space);	
  this.space = null;
  this.view.hide();	
};


SpaceActivity.prototype.canStop = function(callback) {
 var editor = this.view.getEditor();
 if (! editor.isDirty()) { callback.success.call(callback.context, {}); return;}; 
		 
 var continueCallback = {
   context: this,
   success : function() { callback.success.call(callback.context, {}); },
   failure : function() { alert("Error saving space"); }     
 };	 
	 
 var saveCallback = {
   context: this,
   save: function() { this._saveSpace(continueCallback); },
   cancel : function() { editor.reset(); continueCallback.success.call(continueCallback.context, null); }   
 };
	 
 this._askForSaveChanges(saveCallback); 
  	  
};


SpaceActivity.prototype.notify = function(event) {   
  if (event.name = Events.CHANGED) {
    switch (event.propertyName) {
      case Space.SERVICES:
        this.view.setServices(this.space.get(Space.SERVICES).toArray());
        this.view.showPublishTab();
      break;
      case Space.STATE: 
        this.view.refreshState();
      break;
    }	  
  }
};


SpaceActivity.prototype.clickBackButton = function() {
  this._goBack();
};


SpaceActivity.prototype.clickDiscardButton = function() {
  var editor = this.view.getEditor();
  editor.reset();
  this._goBack();
};


SpaceActivity.prototype.clickSaveButton = function() {
  var continueCallback = {
    context : this,
    success : function() { console.log("Space saved"); },
    failure : function() { console.log("Error saving space"); }	    
  };
  
  this._saveSpace(continueCallback);  
};


SpaceActivity.prototype.clickChangeLogo = function() {
  this.view.showUploadImageDialog();  	  
};


SpaceActivity.prototype.publishService = function(id) {
  var service = this.space.getService(id);
  service.set(PublicationService.PUBLISHED, true);
};


SpaceActivity.prototype.unPublishService = function(id) {
  var service = this.space.getService(id);
  service.set(PublicationService.PUBLISHED, false);
};


SpaceActivity.prototype.changeServiceType = function(type) {
  this.view.filterServiceByType(type);	
};


SpaceActivity.prototype.uploadImage = function(form, callback) {
  var federation   = this.space.get(Space.FEDERATION);
  var federationId = federation.id;
  var serverId     = federation.get(Federation.SERVER).id;
	  
  var params = {server_id: serverId, federation_id: federationId, space_id: this.space.id, model_type: ModelTypes.SPACE};
  
  this.service.uploadImage(form, params, {
    context: this,	  
    success: function(name, source) {           	
      this.view.hideUploadImageDialog();
      callback.fn.call(callback.scope, name, source);
    },
    failure : function() {
      this.view.hideUploadImageDialog();
    }
  });	
};


SpaceActivity.prototype.openServer = function(id) {
  var event = {name: Events.OPEN_SERVER, token: new ServerPlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	   	
};


SpaceActivity.prototype.openFederation = function(id) {
  var event = {name: Events.OPEN_FEDERATION, token: new FederationPlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	   	
};


SpaceActivity.prototype.openModel = function(id) {
  var event = {name: Events.OPEN_MODEL, token: new ModelPlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	   	
};


SpaceActivity.prototype.clickPublishTab = function(id) {
  if (this.services) {
    this.view.showPublishTab();
    return;
  }
  
  this.service.loadServices(this.space.id, {
    context: this,
    success : function(services) {       	
      this.space.set(Space.SERVICES, services);
      this.view.showPublishTab();
    },
    failure : function(ex) {
      throw ex;	
    }
  });
};


SpaceActivity.prototype.clickImportTab = function(id) {
  if (this.importerTypes) {
    this.view.showImportTab();
    return;
  }
  
  this.service.loadImporterTypes(this.space.id, {
    context : this,
    success : function(importerTypes) {
      this.view.setImporterTypes(importerTypes);
      this.view.showImportTab(); 
    },
    failure : function(ex) {
      throw ex;
    }
  });    
};


SpaceActivity.prototype.startImportation = function(form, importerTypeId) {  
      
  this.pooler = new Pooler({poolTime: 3000, url: Context.Config.Api + "?op=loadimportprogress", params : {id : this.space.id}});
  var pooler = this.pooler;
  
  this.service.startImport(form, this.space.id, importerTypeId, {
    context: this,
    success : function() {      
      pooler.start({context : this, success : function(progress) {
        if (progress.value < 100) this.view.setProgress(progress);          
        else {
          pooler.stop();
          this.view.finishImportation();
        }
      }});      
    },
    failure : function(ex) {
      throw ex;
    }
  });  
};


SpaceActivity.prototype.stopImportation = function() {
  this.pooler.stop();
};


SpaceActivity.prototype.startSpace = function() {
  NotificationManager.showNotification(Lang.Notifications.STARTING);
  
  this.service.startSpace(this.space.id, {
    context: this,
    success : function(space) {
      NotificationManager.showNotification(Lang.Notifications.STARTED, 1000);
      
      this.space.set(Space.STATE, space.get(Space.STATE));      
    },
    failure : function(ex) {      
      throw ex;
    }
  });  
};


SpaceActivity.prototype.stopSpace = function() {
  NotificationManager.showNotification(Lang.Notifications.STOPPING);
  
  this.service.stopSpace(this.space.id, {
    context: this,
    success : function(space) {
      NotificationManager.showNotification(Lang.Notifications.STOPPED, 1000);
      
      this.space.set(Space.STATE, space.get(Space.STATE));      
    },
    failure : function(ex) {
      throw ex;
    }
  });  
};


SpaceActivity.prototype._askForSaveChanges = function(callback) {
  var saveHandler = function() { 
    this.view.closeSaveDialog();
	  callback.save.call(callback.context, null);	  
  };   

  var discardHandler  = function() { 
    this.view.closeSaveDialog();    
    callback.cancel.call(callback.context, null);
  };
		  
  this.view.openSaveDialog(saveHandler, discardHandler, this);  	
};


SpaceActivity.prototype._showView = function(space) {
  this.view.getEditor().open(space);
  this._link(space);
  this.view.show();
};


SpaceActivity.prototype._goBack = function() {  
  var event = {name : Events.OPEN_DEPLOYMENT, token : new DeploymentPlace().toString(), data : {}};
  EventBus.fire(event);
};


SpaceActivity.prototype._link = function(model) {
  model.on(Events.CHANGED, this, this);  
};


SpaceActivity.prototype._unlink = function(model) {
  if (!model) return;  
  model.un(Events.CHANGED, this);
};


SpaceActivity.prototype._saveSpace = function(callback) {
  var editor = this.view.getEditor();
  if (! editor.isDirty()) { callback.success.call(callback.context, null); return; };

  editor.flush();
  if (this.space.error) {
    editor.showError(this.space.error);
    return;
  }
      
  var space = this.space;
  this.service.saveSpace(this.space, {
    context : this,
    success : function() {    	
      var event = {name: Events.SPACE_SAVED, data:{space : space} };
      EventBus.fire(event);	 
      callback.success.call(callback.context, null);
      NotificationManager.showNotification(Lang.Notifications.SPACE_SAVED, 1000);
    },
    failure : function() {
      callback.failure.call(callback.context, null);
      NotificationManager.showNotification(Lang.Notifications.SAVE_SPACE_ERROR, 1000);
    }
  });
};


SpaceActivity.prototype._createView = function(space) {  
  var view = new SpaceView(Ids.Elements.SPACE_VIEW, AppTemplate.SpaceView);
  return view;
};


SpaceActivity.prototype._clearModel = function() {
  this.space = null;  
};

var BusinessModel = Model.extend({
  init : function() {
    this._super();    
  }	
});

BusinessModel.ID    = 'id';
BusinessModel.NAME  = 'name';
BusinessModel.LABEL = 'label';
BusinessModel.LOGO  = 'logo';
BusinessModel.VERSIONS = 'versions';
BusinessModel.LATEST_VERSION = 'latest_version';


BusinessModel.prototype.isLatestVersion = function(version) {
  return this.getLatestVersion(version) === version;  
};


BusinessModel.prototype.getLatestVersion = function(version) {
  var latestVersion = null;
  var versions = this.get(BusinessModel.VERSIONS);
  
  for (var i=0, l = versions.size(); i < l; i++) {
    var version = versions.get(i);
    if (latestVersion == null || version.get(ModelVersion.DATE) > latestVersion.get(ModelVersion.DATE)) {
       latestVersion = version;
    }
  }
  return latestVersion;
};


BusinessModel.prototype.getNextVersion = function(version) {
  var minDiff     = version.get(ModelVersion.DATE);
  var nextVersion = this.get(BusinessModel.LATEST_VERSION);
  var versions    = this.get(BusinessModel.VERSIONS);
  
  for (i=0, l = versions.size(); i < l; i++) {
    var v = versions.get(i);
    var diff = v.get(ModelVersion.DATE) - version.get(ModelVersion.DATE);
    if (diff > 0 && diff < minDiff) {
      minDiff = diff;
      nextVersion = v;
    }
  }
  return nextVersion;
};


var ModelVersion = Model.extend({
  init : function() {
    this._super();    
  }
});

ModelVersion.ID = 'id';
ModelVersion.LABEL = 'label';
ModelVersion.DATE = 'date';
ModelVersion.METAMODEL = 'metamodel';

Lang.ModelsView = {
  open : 'Open',
  no_models : 'None models',
  models : 'Models'  
};

Lang.AddModelDialog = {
  add_models: "Add models",
  name: 'Name',
  version: 'Model version',
  add : 'Add',    
  repository_url: 'Repository URL'  
};

var AddModelDialog = Dialog.extend({
  init : function(element) {
    this._super(element);
    this.html = translate(AppTemplate.AddModelDialog, Lang.AddModelDialog);
    element.innerHTML = this.html;
    
    this._bind();
  }	
});

AddModelDialog.ADD_EVENT = 'add';


AddModelDialog.prototype.setFocus = function() {
  this.extName.dom.focus();  
};


AddModelDialog.prototype.getData = function() {      
  var data = {    
    form : this.extAddForm.dom
  };
  return data;
};


AddModelDialog.prototype.clear = function() {
  this.extName.dom.value = '';   
  this.extAddButton.dom.disabled = true;
};


AddModelDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.$el);
  
  this.extAddForm  = Ext.get(this.extParent.query('form[name="add-model"]').first());	  
  this.extName  = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extVersion  = Ext.get(this.extParent.query('input[name="source"]').first());
    
  this.extAddButton    = Ext.get(this.extParent.query('input[name="add-button"]').first());
            
  this.extAddButton.on('click', this._addHandler, this);
  this.extName.on('keyup', this._changeHandler, this);    

  this.extVersion.on('change', this._changeHandler, this);    
  
  this.extAddButton.dom.disabled = true;
};


AddModelDialog.prototype._addHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();	
  var evt = {name: AddModelDialog.ADD_EVENT, data: this};
  this.fire(evt);    
};


AddModelDialog.prototype._changeHandler = function(event, target, options) {
  this.extAddButton.dom.disabled = true;
  if (this._validate()) this.extAddButton.dom.disabled = false;
};


AddModelDialog.prototype._validate = function(event, target, options) {
  if ((this.extName.dom.value.length > 0) && (this.extVersion.dom.value != "")) return true;
  else return false;
};

var ModelsView = View.extend({	
  init : function(id) {
    this._super(id);
    this.html = translate(AppTemplate.ModelsView, Lang.Buttons);
    this.html = translate(this.html, Lang.ModelsView);
    this.models = [];
    this.presenter = null;
    
    this.initialized = false;
  }
});


ModelsView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};


ModelsView.prototype.setModels = function(models) {
  this.models = models;  
  if (!this.initialized) this._init();  
  this._refresh();
};


ModelsView.prototype.show = function() {
  this.showAddModelDialog();
  this.$id.show();
};


ModelsView.prototype.hide = function() {
  this.hideAddModelDialog();
  this.$id.hide();
};


ModelsView.prototype.showAddModelDialog = function() {
  if (! this.addModelDialog) {
    this.addModelDialog = new AddModelDialog(this.extAddModelDialog.dom);
    
    this.addModelDialog.on(AddModelDialog.ADD_EVENT, {
    	notify : function(event) {
    	  var dialog = event.data;    	  
    	  var data = dialog.getData();
    	  this.presenter.addModel({form: data.form, name: data.name});    	  
    	}
    }, this);
  }
  this.addModelDialog.open();
};


ModelsView.prototype.hideAddModelDialog = function() {
  if (! this.addModelDialog) return;  
  this.addModelDialog.close();
};


ModelsView.prototype.clearAddModelDialog = function() {
  if (this.addModelDialog) this.addModelDialog.clear();
};


ModelsView.prototype.focusAddModelDialog = function() {
  if (this.addModelDialog) this.addModelDialog.setFocus();
};


ModelsView.prototype._refresh = function() {
  this.table.setData(this.models); 
  this._refreshToolbar();
};


ModelsView.prototype._init = function(event) {
  this.extParent = Ext.get(this.id);    
  this.extParent.dom.innerHTML = this.html;
  
  this.extRemoveButton = Ext.get(this.extParent.query('input[name="remove_button"]').first());
  this.extAddModelDialog = Ext.get(Ids.Elements.ADD_MODEL_DIALOG);
	  
  var columns = [BusinessModel.NAME];  
  var element = this.extParent.query('.table').first();
	   
  this.table = new Table(element, columns, {checkbox: true, clickable: true, empty_message: Lang.ModelsView.no_models});  
  this.table.setData(this.models);
  
  $(this.extRemoveButton.dom).hide();
  
  this._bind();
  
  this.initialized = true;
};


ModelsView.prototype._bind = function(extParent) {        
  this.extRemoveButton.on('click', this._removeHandler, this);
   
  this.table.on(Table.CLICK_ROW, {notify: this._openHandler}, this);
  this.table.on(Table.CHECK_ROW, {notify: this._checkHandler}, this);   
};


ModelsView.prototype._removeHandler = function(event, target, options) {  
  var rows = this.table.getSelectedRows();
  var ids = [];
  $(rows).each(function(row) { ids.push(row.id); });
  this.presenter.removeModels(ids);
};


ModelsView.prototype._openHandler = function(event) {  
  var row = event.data.row;		
  this.presenter.openModel(row.id);
};


ModelsView.prototype._checkHandler = function(event) {
  this._refreshToolbar();
};


ModelsView.prototype._refresh = function() {
  this.table.setData(this.models); 
  this._refreshToolbar();
};


ModelsView.prototype._refreshToolbar = function(event) {
  var rows = this.table.getSelectedRows();
  if (rows.length > 0) 
    $(this.extRemoveButton.dom).show();
  else
    $(this.extRemoveButton.dom).hide();	
};

Lang.ModelView = {    
  publish :  'Publish',    
  import : 'Import',
  model : 'Model',
  name : 'Name',
  label : 'Label',  
  change : 'Change',
  versions : 'Versions',
  upload_model_version: 'Upload model version',
  url : 'Url',
  upgrade : 'Upgrade',
  none_versions : 'None versions in this model'  
};

var ListEditor = Editor.extend({
  
  init : function(id, element, object, propertyName, adapter) {
    this._super();
    this.id = id;
    this.element = element;
    this.object = object;
    this.propertyName = propertyName;
    this.adapter = adapter;
    
    this.oldArray = this._getItems();
    this.newArray = this._getItems();
  }
});


ListEditor.prototype._getItems = function(index) {
  var items = [];
  var children = this.element.children;
  for (var i=0, l = children.length; i < l; i++) {
    var li = children[i];
    var item = this.adapter.adapt(li);
    items.push(item);
  }
  return items;
};


ListEditor.prototype.open = function() {
  this.state = Editor.OPENED;
};


ListEditor.prototype.close = function() {  
  this.state = Editor.CLOSED;
};


ListEditor.prototype.isDirty = function() {
  this.newArray = this._getItems();
  return _.isEqual(this.oldArray, this.newArray) == false;    
};


ListEditor.prototype.flush = function() {
  var collection = this.object.get(this.propertyName);
  collection.clear();
  this.newArray = this._getItems();
  
  var l = this.newArray.length;
  for (var i=0; i < l; i++) {
    var item = this.newArray[i];
    collection.add(item);
  }
};


ListEditor.prototype.reset = function() {
  var collection = this.object.get(this.propertyName);
  collection.clear();

  var l = this.oldArray.length;
  for (var i=0; i < l; i++) {
    var item = this.oldArray[i];
    collection.add(item);
  }
};

var ModelEditor = Editor.extend({
  init : function(model, options) {
    this._super();
    this.model = model;
    this.options = options || {};
    this.options.type = 'composed';
    
    this.labelEditor = null;
    this.imageEditor = null;
    
    this.versionEditors = {};    
    this._init();
  }
});


ModelEditor.prototype.setImage = function(filename, source) {
  this.imageEditor.setImage(filename, source);  
};


ModelEditor.prototype.hasVersionEditor = function(editorId) {
  var editor = this._findVersionEditor(editorId);
  return editor != null;
};


ModelEditor.prototype.getVersionEditor = function(editorId) {
  return this._findVersionEditor(editorId);
};


ModelEditor.prototype.addVersionEditor = function(editor) {    
  this.editors.push(editor);
};


ModelEditor.prototype.removeVersionEditor = function(editorId) {
  var editor = this._findVersionEditor(editorId);
  if (editor = null) return;
  
  editor.close();
  this.editors.splice(i, 1);     
};


ModelEditor.prototype._init = function() {
  var m = BusinessModel;
  var map = this.options.map;
  
  this.labelEditor = new TextEditor(map[m.LABEL].element, this.model, map[m.LABEL].propertyName);
  this.imageEditor = new ImageEditor(map[m.LOGO].element, this.model, map[m.LOGO].propertyName);
         
  this.editors.push(this.labelEditor);
  this.editors.push(this.imageEditor);  
};


ModelEditor.prototype._findVersionEditor = function(editorId) {
  for (var i=0, l = this.editors.length; i < l; i++) {
    var editor = this.editors[i];
    if (editor.id === editorId) {
      return editor;
    }
  }
  return null;
};

Lang.UploadModelVersionDialog = {
  title : 'Upload Model Version',
  message: 'Upload updated version of this model. (zip file)'  
};

var UploadModelVersionDialog = Dialog.extend({
  init : function(element) {
    this._super(element);
    this.id = this.$el.getAttribute('id');
    
    this._init();
  }    
});


UploadModelVersionDialog.prototype.getData = function() {
   return {
     form: this.extForm.dom,
   };
};


UploadModelVersionDialog.prototype.open = function() {
  this.extDialog.show();  
};


UploadModelVersionDialog.prototype.close = function() {
  this.extDialog.hide();  
};


UploadModelVersionDialog.prototype.addButton = function(buttonName, handler, scope) {
  this.extDialog.addButton(buttonName, handler, scope);     
};


UploadModelVersionDialog.prototype._init = function() {
  this.extDialog = this._createDialog();
  this.extDialog.setTitle(Lang.UploadModelVersionDialog.title);
  
  var html  = translate(AppTemplate.UploadModelVersionDialog, Lang.Buttons);
  this.extDialog.body.dom.innerHTML  = translate(html, Lang.UploadModelVersionDialog);
  
  this.extParent     = Ext.get(this.id);
  this.extMessage    = Ext.get(this.extParent.select(".message").first());
  this.extForm       = Ext.get(this.extParent.query("form").first());
  this.extFile       = Ext.get(this.extParent.query("input[type='file']").first());
  this.extFilename   = Ext.get(this.extParent.query('div[name="filename"]').first());
    
  this.extFile.on('change', this._changeFileHandler, this);
};


UploadModelVersionDialog.prototype._changeFileHandler = function(event, target, options) {
  this.extFilename.dom.innerHTML = this.extFile.dom.value;
};


UploadModelVersionDialog.prototype._createDialog = function(event, target, options) {  
  var dialog = new Ext.BasicDialog(this.id, {
    modal: true,
    shadow: false,
    width: 435,
    height: 236,
    minWidth: 430,
    minHeight: 230,
    minimizable: false
  });

  return dialog;  
};

Lang.ModelVersionsView = {
  versions : 'Versions',
  upload_model_version : 'Upload model versions'
};

Lang.ModelVersionView = {
    upgrade : 'upgrade',
    no_spaces : 'There is no spaces with this model version'  
};

var ModelVersionView = EventsSource.extend({
  init : function(element, model) {
    this._super();
    this.element = element;
    this.model   = model;
    this.spaces  = [];
    this.initialized = false;
  }
});
ModelVersionView.collapsed_image = "images/grided/collapsed-arrow.png";
ModelVersionView.expanded_image  = "images/grided/expanded-arrow.png";

ModelVersionView.TOGGLE_VERSION = "toogle_version";
ModelVersionView.CHECK_SPACE        = "check_space";
ModelVersionView.ON_OPEN_SPACE      = "open_space";
ModelVersionView.ON_OPEN_FEDERATION = "open_federation";
ModelVersionView.ON_UPGRADE         = "click_upgrade";



ModelVersionView.prototype.setVersion = function(version) {
  this.version = version;  
};


ModelVersionView.prototype.setSpaces = function(spaces) {
  this.spaces = spaces;  
};


ModelVersionView.prototype.getVersion = function() {
  return this.version;
};


ModelVersionView.prototype.refresh = function(version) {
  if (! this.initialized) this._init();
  
  this.$block.setAttribute('code', this.version.id);
  var ul = this.extBlockBody.query('ul').first();
  var p  = this.extBlockBody.query('p').first();
  
  if (! ul) {
    ul = document.createElement('ul');
    if (this.spaces.length > 0) this.extBlockBody.appendChild(ul);
    if (p) this.extBlockBody.dom.removeChild(p);
  }
  
  this.extVersionInput.disabled = false;
    
  while (ul.hasChildNodes()) {
    ul.removeChild(ul.lastChild);
  }

  for (var i=0, l = this.spaces.length; i < l; i++) {
    var space = this.spaces[i];
    var inputHTML = _.template(AppTemplate.ModelVersionSpace, {space : space});
    var li = document.createElement('li');
    li.innerHTML = inputHTML;       
    ul.appendChild(li);
  }
  
  if (! ul.hasChildNodes()) {    
    this.extBlockBody.dom.innerHTML = "";
    this.extVersionInput.dom.checked = false;
    this.extVersionInput.dom.disabled = true;
    
    var p = document.createElement('p');
    p.innerHTML = Lang.ModelVersionView.no_spaces;
    this.extBlockBody.dom.appendChild(p);
  }
         
  var extBlock = Ext.get(this.$block);
  var extUpgrade = this.extBlockBody.select('a[code]').first();

  extBlock.on('click', this._clickFederationOrSpaceHandler, this);
  
  if (extUpgrade) {
    extUpgrade.on('click', this._clickUpgradeHandler, this);
  }
  
  this.initialized = true;
};


ModelVersionView.prototype.expand = function() {  
  this.$li.removeClassName('collapsed');
  this.$li.addClassName('expanded');
  this.extImage.dom.src = ModelVersionView.expanded_image;      
  this.$block.show();    
};


ModelVersionView.prototype.collapse = function() {
  this.$li.removeClassName('expanded');
  this.$li.addClassName('collapsed');
  this.extImage.dom.src = ModelVersionView.collapsed_image;      
  this.$block.hide();  
};


ModelVersionView.prototype.getSelectedSpaces = function() {
  var spacesIds = [];
  var extBlock = Ext.get(this.$block);
  
  extBlock.select('input[type="checkbox"]').each(function(input, index) {
    var spaceId = input.dom.getAttribute('code');
    if (input.dom.checked) spacesIds.push(spaceId);        
  });
  return spacesIds;  
};


ModelVersionView.prototype.isExpanded = function() {
  return this.$li.className == 'expanded';
};


ModelVersionView.prototype._init = function() {
  this.extParent = Ext.get(this.element);
    
  var template = translate(AppTemplate.ModelVersionView, Lang.ModelVersionView);
  var html     = _.template(template, {model: this.model, version : this.version});
  
  var div = document.createElement('div');
  div.innerHTML = html;  
  this.extParent.dom.appendChild(div.firstChild);
  
  var extVersionLi     = Ext.get(this.extParent.dom.lastChild);  
  this.extImage        = Ext.get(extVersionLi.query('img').first());
  this.extVersionInput = Ext.get(extVersionLi.query('input[type="checkbox"]').first());  
  this.versionId       = this.extVersionInput.dom.getAttribute('code');

  this.$li        = $(extVersionLi.dom);
  this.$block     = this.$li.down('.block');  
  this.extBlockBody = Ext.get(this.$block.firstChild);
 
  this.extImage.on('click', this._clickImageHandler, this);
  this.extVersionInput.on('click', this._clickCheckbox, this);
  this.extBlockBody.on('click', this._clickVersionHandler, this);

  var spacesTemplate = translate(AppTemplate.ModelVersionSpaces, Lang.ModelVersionView);
  var spacesHTML = _.template(spacesTemplate, {model : this.model, version: this.version, spaces : this.spaces});
  this.extBlockBody.dom.innerHTML = spacesHTML;  

  this.$upgrade   = this.$block.down('.block-action');
  
  if (this.$upgrade) {
    Ext.get(this.$upgrade).on('click', this._clickUpgradeHandler, this);
  }  
};


ModelVersionView.prototype._clickImageHandler = function(event, target, options) {  
  var evt = {name: ModelVersionView.TOGGLE_VERSION, data : {view: this}};
  this.fire(evt);  
};


ModelVersionView.prototype._clickCheckbox = function(event, target, options) {
  this.extBlockBody.query('input[type="checkbox"]').each(function(item, index) {
    item.checked = target.checked;
  });
};


ModelVersionView.prototype._clickVersionHandler = function(event, target, options) {
  var nodeName = target.nodeName.toLowerCase();
  
  switch (nodeName) {
    case 'a'    : this._clickFederationOrSpaceHandler(event, target, options); break;
    case 'input': this._clickSpaceCheckbox(event, target, options); break;
    default: return;
  }
};


ModelVersionView.prototype._clickFederationOrSpaceHandler = function(event, target, options) {
  if (target.nodeName.toLowerCase() != 'a') return;
  
  event.preventDefault();
  event.stopPropagation();
  var type = target.getAttribute('type');
  var id   = target.getAttribute('code');
      
  switch (type) {
    case 'federation': 
      evt = {name : ModelVersionView.ON_OPEN_FEDERATION, data: {id : id}};
      this.fire(evt);
    break;
    case 'space' :
      evt = {name : ModelVersionView.ON_OPEN_SPACE, data: {id : id}};
      this.fire(evt);      
    break;     
  }  
};


ModelVersionView.prototype._clickSpaceCheckbox = function(event, target, options) {
  if (target.nodeName.toLowerCase() != 'input') return;
  
  var allInputsChecked = true;
  this.extBlockBody.query('input[type="checkbox"]').each(function(input, index) {
    if (! input.checked) allInputsChecked = false;
  });
  this.extVersionInput.dom.checked = allInputsChecked;
};


ModelVersionView.prototype._clickUpgradeHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  
  var spaceIds = this.getSelectedSpaces();
  var evt = {name: ModelVersionView.ON_UPGRADE, data : {spaceIds : spaceIds, versionId : this.versionId}};
  this.fire(evt);  
};

var ModelVersionsView = EventsSource.extend({
  init : function(element, model) {
    this._super();
    this.element = element;
    this.model = model;
    this.views = [];
  }
});

ModelVersionsView.ON_OPEN_FEDERATION = 'open_federation';
ModelVersionsView.ON_OPEN_SPACE      = 'open_space';
ModelVersionsView.ON_UPLOAD          = 'click_upload';
ModelVersionsView.ON_UPGRADE         = 'click_upgrade';


ModelVersionsView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};


ModelVersionsView.prototype.setVersions = function(versions) {
  this.versions = versions;  
};


ModelVersionsView.prototype.setSpaces = function(spaces) {
  this.spaces = spaces;  
  this._init();
};



ModelVersionsView.prototype.addVersion = function(version) {
  var view = this._createModelVersionView();
  view.setVersion(version);  
};


ModelVersionsView.prototype.refreshModelVersion = function(modelVersion, spaces) {
  var view = this._getView(modelVersion.id);
  view.setSpaces(spaces);
  if (view) view.refresh(modelVersion);
};


ModelVersionsView.prototype.expandAll = function() {
  for (var i=0, l= this.views.length; i < l; i++) {
    var view = this.views[i];
    view.expand();    
  }
};


ModelVersionsView.prototype._init = function() {
  this.extParent = Ext.get(this.element);
  
  var html = translate(AppTemplate.ModelVersionsView, Lang.ModelVersionsView);
  this.extParent.dom.innerHTML = html;
  
  this.root = this.extParent.query('ul').first();
  
  
  for (var i=0, l=this.versions.length; i < l; i++) {
    var version = this.versions[i];
    var versionSpaces = [];
    var view = this._createModelVersionView();  
    view.setVersion(version);
    
    for (var j=0, s=this.spaces.length; j < s; j++) {
      var space = this.spaces[j];
      if (space.get(Space.MODEL_VERSION).id == version.id) {
        versionSpaces.push(space);        
      }
    }
    view.setSpaces(versionSpaces);
    view.refresh();
  }
  
  this.extUploadModelVersion = Ext.get(this.extParent.query('a[name="upload"]').first());
  this.extCollapseAll        = Ext.get(this.extParent.query('img[name="collapse-all"]').first());
  this.extExpandAll          = Ext.get(this.extParent.query('img[name="expand-all"]').first());

  this.extUploadModelVersion.on('click', this._clickUploadHandler, this);
  this.extCollapseAll.on('click', this._clickCollapseAllHandler, this);  
  this.extExpandAll.on('click', this._clickExpandAllHandler, this);  
  
};


ModelVersionsView.prototype._getView = function(versionId) {
  var result = null;
  for (var i=0, l=this.views.length; i < l; i++) {
    var view = this.views[i];
    if (view.getVersion().id == versionId) result = view; 
  }
  return result;
};


ModelVersionsView.prototype._createModelVersionView = function() {
  var view = new ModelVersionView(this.root, this.model);
  
  view.on(ModelVersionView.TOGGLE_VERSION , {notify: this._toggleVersionHandler}, this);
  view.on(ModelVersionView.ON_OPEN_SPACE, {notify: this._openSpaceHandler}, this);
  view.on(ModelVersionView.ON_OPEN_FEDERATION, {notify: this._openFederationHandler}, this);
  view.on(ModelVersionView.ON_UPGRADE, {notify: this._upgradeHandler }, this);  
  
  this.views.push(view);
  return view;
};


ModelVersionsView.prototype._toggleVersionHandler = function(event) {
  var view = event.data.view;
  if (view.isExpanded()) view.collapse();
  else view.expand();
};


ModelVersionsView.prototype._clickCollapseAllHandler = function(event) {
  for (var i=0, l = this.views.length; i < l; i++) {
    var view = this.views[i];
    view.collapse();
  }
};


ModelVersionsView.prototype._clickExpandAllHandler = function(event) {
  for (var i=0, l = this.views.length; i < l; i++) {
    var view = this.views[i];
    view.expand();
  }
};


ModelVersionsView.prototype._openSpaceHandler = function(event) {
  this.fire(event);
};


ModelVersionsView.prototype._openFederationHandler = function(event) {
  this.fire(event);    
};


ModelVersionsView.prototype._upgradeHandler = function(event) {
  this.fire(event);
};


ModelVersionsView.prototype._clickUploadHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  evt = {name: ModelVersionsView.ON_UPLOAD, data: {}};
  this.fire(evt);
};

var ModelView = View.extend({
  init : function(id) {
    this._super(id);
    this.html = translate(AppTemplate.ModelView, Lang.ModelView);   
    this.model  = null;
    this.uploadModelVersionDialog = null;
    
    this.modelInitialized = false;
  }
});

ModelView.collapsed_image = "images/grided/collapsed-arrow.png";
ModelView.expanded_image  = "images/grided/expanded-arrow.png";


ModelView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};


ModelView.prototype.setModel = function(model) {
  this.model = model;  
};


ModelView.prototype.setSpaces = function(spaces) {
  this.spaces = spaces;
  this._init();  
};


ModelView.prototype.notify = function(event) {
  if (event.name == Events.ADDED) {
    if (event.data.collection == this.model.get(BusinessModel.VERSIONS)) {
      var version = event.data.item;
      this.modelVersionsView.addVersion(version);      
    }
  }
};


ModelView.prototype.refreshModelVersion = function(modelVersion, spaces) {  
  this.modelVersionsView.refreshModelVersion(modelVersion, spaces);
};


ModelView.prototype.hide = function() {
  this.$id.hide();
};


ModelView.prototype.openUploadModelVersionDialog = function() {
  if (! this.uploadModelVersionDialog) {
    this.uploadModelVersionDialog = new UploadModelVersionDialog(this.extUpLoadModelVersionDialog.dom);
    this.uploadModelVersionDialog.addButton(Lang.Buttons.upload, function(event, target, options) {      
      var data = this.uploadModelVersionDialog.getData();      
      this.presenter.uploadModelVersion(data.form);
    }, this);          

    this.uploadModelVersionDialog.addButton(Lang.Buttons.cancel, function(event, target, options) {
      this.closeUploadModelVersionDialog();
    }, this);    
  }
  
  this.uploadModelVersionDialog.open();
};


ModelView.prototype.closeUploadModelVersionDialog = function() {  
  if (this.uploadModelVersionDialog) this.uploadModelVersionDialog.close();
};


ModelView.prototype._init = function() {
  this.extParent = Ext.get(this.id);
  var content = this.merge(this.html, {model : this.model});
  this.extParent.dom.innerHTML = content;
  this.uploadModelVersionDialog = null;
  
  this.extVersions = Ext.get(this.extParent.select('.versions').first());
    
  var versions = this.model.get(BusinessModel.VERSIONS).toArray();
  var spaces   = this.spaces.toArray();
  
  this.modelVersionsView = new ModelVersionsView(this.extVersions.dom, this.model);
  this.modelVersionsView.on(ModelVersionsView.ON_UPLOAD, {notify : this._clickUploadHandler}, this);
  this.modelVersionsView.on(ModelVersionsView.ON_OPEN_SPACE, {notify : this._clickSpaceHandler}, this);
  this.modelVersionsView.on(ModelVersionsView.ON_OPEN_FEDERATION, {notify : this._clickFederationHandler}, this);
  this.modelVersionsView.on(ModelVersionsView.ON_UPGRADE, {notify: this._clickUpgradeHandler}, this);
  
  this.modelVersionsView.setVersions(versions);
  this.modelVersionsView.setSpaces(spaces);
  this.modelVersionsView.expandAll();
    
  this.backButton     = Ext.get(this.extParent.query('a[name="back"]').first());
  this.extUpLoadModelVersionDialog = Ext.get('upload-model-version-dialog');
        
  this.extName  = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extLabel = Ext.get(this.extParent.query('input[name="label"]').first());
    
  this.backButton.on('click', this._backButtonHandler, this);
  
  this._modelInitialized = true;
};



ModelView.prototype._backButtonHandler = function(event, target, options) {  
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickBackButton();
};


ModelView.prototype._clickUploadHandler = function(event) {
  this.presenter.clickUploadModelVersion();  
};


ModelView.prototype._clickSpaceHandler = function(event) {
  var id = event.data.id;
  this.presenter.openSpace(id);
};


ModelView.prototype._clickFederationHandler = function(event) {
  var id = event.data.id;
  this.presenter.openFederation(id);
};


ModelView.prototype._clickUpgradeHandler = function(event) {
  this.presenter.upgradeSpaces(event.data.spaceIds, event.data.versionId);
};

var ModelsActivity = Activity.extend({
  init : function() {
    this.view = this._createView();
    this.view.setPresenter(this);
    this.service = GridedService;
    this.modelsCollection = null;
  }
});


ModelsActivity.prototype.start = function(data) {
  if (! this.view) this.view = this._createView();
	  
  if (this.modelsCollection) {
    this._link(this.modelsCollection);
    this._showView();    
  }
  
  if (! this.modelsCollection) this.service.loadModels({
    context : this,
    success : function(modelsCollection) {
      this.modelsCollection = modelsCollection;
      this.view.setModels(this.modelsCollection.toArray());
      this._link(this.modelsCollection);
      this._showView();
    },
    failure : function(ex) {
      throw ex;
    }    
  });     
};


ModelsActivity.prototype.stop = function(data) {
  this._unlink(this.modelsCollection);  
  this.view.hide();
};


ModelsActivity.prototype.canStop = function(callback) {
  callback.success.call(callback.context, {});
};


ModelsActivity.prototype.notify = function(event) {
  var modelsCollection = event.data.model;  
  this.view.setModels(modelsCollection.toArray());   
};


ModelsActivity.prototype.openModel = function(id) {
  var event = {name: Events.OPEN_MODEL, token: new ModelPlace(id).toString(), data: {id:id}};
  EventBus.fire(event);
};


ModelsActivity.prototype.addModel = function(data) {
  
  this.service.addModel(data.form, data.name, {
    context : this,
    success : function(model) {
      this.modelsCollection.add(model);
      this.view.clearAddModelDialog();      
      this.view.focusAddModelDialog();
    },
    failure : function(ex) {
      throw ex;
    }
  });
};


ModelsActivity.prototype.removeModels = function(ids) {
  var modelIds = ids;
  this.service.removeModels(modelIds, {
    context : this,
    success : function(ids) {
      this.modelsCollection.remove(modelIds);    	
    },
    failure : function(ex) {
      throw ex;
    }
  });
};


ModelsActivity.prototype._createView = function() {
  var view = new ModelsView(Ids.Elements.MODELS_VIEW);
  return view;
};


ModelsActivity.prototype._showView = function() {
  this.view.show();  
};


ModelsActivity.prototype._link = function(model) {
  model.on(Events.ADDED, this, this);  
  model.on(Events.REMOVED, this, this);
};


ModelsActivity.prototype._unlink = function(model) {
  if (!model) return;  
  model.un(Events.REMOVED, this);
  model.un(Events.ADDED, this);
};

var ModelActivity = Activity.extend({
  init : function() {
    this.view = this._createView();
    this.view.setPresenter(this);
    this.service = GridedService;

    this.model = null;
  }
});


ModelActivity.prototype.start = function(data) {
  if (!this.model || this.model.id != data.id)
    this._clearModel();

  if (!this.view) this.view = this._createView();
  if (this.model) this._showView(this.model);
  
  if (!this.model)
    
    this.service.loadModel(data.id, {
      context : this,
      success : function(model) {                
        this.model = model;        
        this.view.setModel(this.model); 
        
        this._loadSpaces(this.model.id);        
        this._showView(this.model);
      },
      failure : function(ex) {
        throw ex;
      }
    });
};


ModelActivity.prototype.stop = function(data) {
  this._unlink(this.model);
  this.model = null;
  this.view.hide();
};


ModelActivity.prototype.canStop = function(callback) {
  callback.success.call(callback.context, {});
};


ModelActivity.prototype.notify = function(event) {
  this.view.notify(event);
};


ModelActivity.prototype.clickUploadModelVersion = function() {
  this.view.openUploadModelVersionDialog();
};


ModelActivity.prototype.uploadModelVersion = function(form) {
  this.service.uploadModelVersion(form, {model_id : this.model.id}, {
    context : this,
    success : function(version) {
      this.view.closeUploadModelVersionDialog();
      this.model.get(BusinessModel.VERSIONS).add(version);
    },
    failure : function(ex) {
      this.view.closeUploadModelVersionDialog();
      ExceptionHandler.handle(ex);
    }
  });
};


ModelActivity.prototype.clickExpandModelVersion = function(versionId) {
  var versions = this.model.get(BusinessModel.VERSIONS);
  var selectedVersion = versions.getById(versionId);

  this.service.loadSpacesWithModel(this.model.id, selectedVersion, {
    context : this,
    success : function(spaces, version) {
      version.set(ModelVersion.SPACES, spaces, {
        silent : true
      });
      this.view.expandModelVersion(version.id, version.get(ModelVersion.SPACES)
          .toArray());
    },
    failure : function(ex) {
      throw ex;
    }
  });
};


ModelActivity.prototype.clickCollapseModelVersion = function(versionId) {
  this.view.collapseModelVersion(versionId);
};


ModelActivity.prototype.collapseAllVersions = function() {
  this.view.collapseAllModelVersions();
};


ModelActivity.prototype.upgradeSpaces = function(spaceIds, versionId) {
  
  this.service.upgradeSpaces(spaceIds, this.model.id, versionId, {
    context: this,
    success : function(spaceIds, modelId, versionId) {
      var versions = this.model.get(BusinessModel.VERSIONS);
      var oldVersion = versions.getById(versionId);
      var nextVersion = this.model.getNextVersion(oldVersion);
      
      for (var i=0, l=spaceIds.length; i < l; i++) {
        var spaceId = spaceIds[i];
        var space = this.modelSpaces.getById(spaceId);
        space.set(Space.MODEL_VERSION, nextVersion, {silent: true});        
      }
      
      oldVersionSpaces = [];
      this.modelSpaces.each(function(index, space) {
        if (space.get(Space.MODEL_VERSION).id === oldVersion.id) oldVersionSpaces.push(space);
      }, this);
      
      nextVersionSpaces = [];
      this.modelSpaces.each(function(index, space) {
        if (space.get(Space.MODEL_VERSION).id === nextVersion.id) nextVersionSpaces.push(space);
      }, this);
      
      this.view.refreshModelVersion(nextVersion, nextVersionSpaces);
      this.view.refreshModelVersion(oldVersion, oldVersionSpaces);
      









                 
    },
    failure : function(ex) {
      throw ex;
    }
  });      
};


ModelActivity.prototype.openSpace = function(id) {
  var event = {
    name : Events.OPEN_SPACE,
    token : new SpacePlace(id).toString(),
    data : {
      id : id
    }
  };
  EventBus.fire(event);
};


ModelActivity.prototype.openFederation = function(id) {
  var event = {
    name : Events.OPEN_FEDERATION,
    token : new FederationPlace(id).toString(),
    data : {
      id : id
    }
  };
  EventBus.fire(event);
};


ModelActivity.prototype.clickBackButton = function() {
  this._goBack();
};


ModelActivity.prototype._goBack = function() {
  var event = {name : Events.OPEN_MODELS, token : new ModelsPlace().toString(), data: {}};
  EventBus.fire(event);
};


ModelActivity.prototype._link = function(model) {
  model.on(Events.CHANGED, this, this);
  model.on(Events.ADDED, this, this);  
};


ModelActivity.prototype._unlink = function(model) {
  if (!model)
    return;
  model.un(Events.CHANGED, this);
};


ModelActivity.prototype._loadSpaces = function(modelId) {
  this.service.loadSpacesByModel(modelId, {
    context : this,
    success : function(spaces) {
      this.modelSpaces = spaces;
      this.view.setSpaces(this.modelSpaces);
    },
    failure : function(ex) {
      throw ex;
    }
  });
};


ModelActivity.prototype._createView = function(space) {
  var view = new ModelView(Ids.Elements.MODEL_VIEW, AppTemplate.ModelView);
  return view;
};


ModelActivity.prototype._showView = function(model) {
  this._link(model);
  this._link(model.get(BusinessModel.VERSIONS));
  this.view.show();
};


ModelActivity.prototype._clearModel = function() {
  this.model = null;
};

ActivityMapper = {
  map: {},
  activities: {},
      
  get : function(placename) {
	var activity = this.activities[placename];
	if (! activity) {	
		var activityConstructor = this.map[placename];
		if (activityConstructor == null) throw new Error('Error mapping activity');
    
		activity = new activityConstructor();
		this.activities[placename] = activity;		
	}
	return activity;
  },
  
  register: function(placename, activity) {
    this.map[placename] = activity;	  
  }
};

var Controller = {};
Controller.defaultLocation = new ServersPlace().toString();

Controller.init = function() {
  Controller._initHistory();
  Controller._initRouter();
  Controller._initActivityMapper();
  Controller._initEventBus();
  Controller._initNotificationManager();
    
  var data = {};  
  var location = (History.getLocation() != "")? History.getLocation() : Controller.defaultLocation;
  data.id = LocationParser.parse(location);      

  var event = {token: location, data: data};
  ChangePlaceEventHandlers.handle(event);  
};


Controller.onChangeHistory = function(token ,data) {
  var place = Router.get(token);
  var activity = ActivityMapper.get(place);
  ActivityManager.start(activity, data);  
  EventBus.fire({name: Events.CHANGE_PLACE, data: place});
};


Controller._initHistory = function() {
  History.init();
  History.addListener(Controller.onChangeHistory);   
};


Controller._initRouter = function() {
  Router.addRoute(/home\/?$/, HomePlace);  
  Router.addRoute(/servers\/\d+$/, ServerPlace);
  Router.addRoute(/servers\/?$/, ServersPlace);
  Router.addRoute(/\\*models\/?$/, ModelsPlace);
  Router.addRoute(/\\*models\/\d+$/, ModelPlace);
  Router.addRoute(/deployment\/?$/, DeploymentPlace);
  Router.addRoute(/\\*federations\/\d+$/, FederationPlace);  
  Router.addRoute(/\\*spaces\/\d+$/, SpacePlace);    
};


Controller._initActivityMapper = function() {
  ActivityMapper.register(HomePlace, HomeActivity);
  ActivityMapper.register(ServersPlace, ServersActivity);
  ActivityMapper.register(ServerPlace, ServerActivity);
  ActivityMapper.register(DeploymentPlace, DeploymentActivity);
  ActivityMapper.register(FederationPlace, FederationActivity);
  ActivityMapper.register(SpacePlace, SpaceActivity);  
  ActivityMapper.register(ModelsPlace, ModelsActivity);
  ActivityMapper.register(ModelPlace, ModelActivity);
};


Controller._initEventBus = function() {  
  EventBus.on(Events.OPEN_HOME, ChangePlaceEventHandlers.handle);
  EventBus.on(Events.OPEN_SERVERS, ChangePlaceEventHandlers.handle);
  EventBus.on(Events.OPEN_SERVER, ChangePlaceEventHandlers.handle);
  EventBus.on(Events.OPEN_MODELS, ChangePlaceEventHandlers.handle);
  EventBus.on(Events.OPEN_MODEL, ChangePlaceEventHandlers.handle);
  EventBus.on(Events.OPEN_DEPLOYMENT, ChangePlaceEventHandlers.handle);
  EventBus.on(Events.OPEN_FEDERATION, ChangePlaceEventHandlers.handle);  
  EventBus.on(Events.OPEN_SPACE, ChangePlaceEventHandlers.handle);
  
  EventBus.on(Events.CHANGE_PLACE, Desktop.handle);
      
  EventBus.on(Events.SERVER_SAVED, ServerSavedHandler.handle);
};


Controller._initNotificationManager = function() {  
  NotificationManager.init(Ids.Elements.NOTIFICATIONS_BAR);
  NotificationManager.hideNotification();
};

var ExceptionHandler = new Object(); 


ExceptionHandler.handle = function(ex) {
  var message = Lang.Exceptions[ex.code];
  var elementId = Literals.Dialogs.Exception;
  this.showMessage(elementId, message);
}; 


ExceptionHandler.showMessage = function(elementId, message) {
  var extDialog = new Ext.BasicDialog(elementId, {width:300,height:200,resizable:false});
  
  extDialog.body.dom.innerHTML = message;      
  extDialog.setTitle(Lang.Exceptions.Internal.Title);
  extDialog.addButton(Lang.Buttons.close, function() { extDialog.hide();});
  extDialog.center();
  extDialog.show();         
};






DefaultExceptionHandler = function() {
	var containerId = '';
	var serverUrl = '';
	
	return {

	  init: function(containerId, serverUrl) {
			this.containerId = containerId;
			this.serverUrl = serverUrl;
			window.onerror = !window.onerror ? DefaultExceptionHandler.handleError : window.onerror.createSequence(DefaultExceptionHandler.handleError);
		},
		
		handleError:  function() {
		  var args = [];
	  	try {
  		    for (var x = 0; x < arguments.length; x++) {
			      args[x] = arguments[x];
			    }
  		    var message = this.getFormattedMessage(args);
  		    message = message.replace(/</g, "&lt;").replace(/>/g, "&gt;");
			    message = message.replace(/\n/g, "<br />").replace(/\t/g, " &nbsp; &nbsp;"),
			
		      this.displayError(message);
			    this.logToServer(message);
		  } catch(e) {
		    console.log("errorHandler is broken");
			  return false;
		  }
		  return true;
		},
					
		displayError: function(message) {
			var extDialog = new Ext.BasicDialog(this.containerId, {width:500,height:400,resizable:false});			
			
			extDialog.body.dom.innerHTML = message;
			    
			extDialog.setTitle(Lang.Exceptions.Internal.Title);
			extDialog.addButton(Lang.Buttons.close, CloseException.bind(this, extDialog));
			extDialog.center();
			extDialog.show();			  
		},
		
		logToServer: function(message) {
			var request = new Request({
			  url : this.serverUrl,
			  params: {message: message},
			  callback : {success: function() {return false;}, failure: function() {return false;}}
			});
			request.send();			
		},
					
		getFormattedMessage: function(args) {
			var lines = ["The following error has occured:"];
			if (args[0] instanceof Error) { 
				var err = args[0];
				lines[lines.length] = "Message: (" + err.name + ") " + err.message;
				lines[lines.length] = "Error number: " + (err.number & 0xFFFF); 
				lines[lines.length] = "Description: " + err.description;
			} else if ((args.length == 3) && (typeof(args[2]) == "number")) { 
				lines[lines.length] = "Message: " + args[0];
				lines[lines.length] = "URL: " + args[1];
				lines[lines.length] = "Line Number: " + args[2];
			} else {
				lines = ["An unknown error has occured."]; 
				lines[lines.length] = "The following information may be useful:";
				for (var x = 0; x < args.length; x++) {
					lines[lines.length] = Ext.encode(args[x]);
				}
			}
			return lines.join("\n");
		},
	};
}();


DefaultExceptionHandler.handleError = DefaultExceptionHandler.handleError.createDelegate(DefaultExceptionHandler);

function Request(config) {
  this.config = config;  
}

Request.prototype.send = function() {  	
  if (this.config.method == 'get') this._sendGetRequest();
  else this._sendPostRequest();    	 
};


Request.prototype._successHandler = function(res, req) {  
  var responseObject = (res.responseText != "")? Ext.util.JSON.decode(res.responseText) : null;
  if (! responseObject) this._failureHandler(res, req);
  if (responseObject.type == Literals.ERROR) this._failureHandler(res, req);
  else this.config.callback.success.call(this.config.callback.context, responseObject.data);  
};


Request.prototype._failureHandler = function(res, req) {   	
  var responseObject = (res.responseText != "")? Ext.util.JSON.decode(res.responseText) : null;
  if (responseObject == null) throw {type : Literals.SERVER_ERROR};
  
  var exception = responseObject;    
  this.config.callback.failure.call(this.config.callback.context, exception);
};


Request.prototype._sendPostRequest = function() {
  Ext.Ajax.request({
	url : this.config.url,  
    method: this.config.method,
    params : this.config.params,
	form  : this.config.form || null,
	isUpload : this.config.isUpload || false,
	scope : this,
	success : this._successHandler,
	failure : this._failureHandler	
  });	
};


Request.prototype._sendGetRequest = function() {
  Ext.Ajax.request({
	url: this.config.url,
	method: this.config.method,
	params : this.config.params,
	scope : this,	
	success : this._successHandler,
	failure : this._failureHandler	
  });	
};

ServerSerializer = {
		
  serialize: function(server) {
	  var json = {id: + server.id, name: server.get(Server.NAME), ip: server.get(Server.IP), enabled : server.get(Server.ENABLED)};
	  jsonText = Ext.util.JSON.encode(json);		 
	  return jsonText;
  },
  
  unserialize: function(jsonServer) {
    var server = new Server();
    server.set(Server.ID, jsonServer.id, {silent: true});
    server.set(Server.NAME, jsonServer.name, {silent: true});
    server.set(Server.IP, jsonServer.ip, {silent: true});
    server.set(Server.ENABLED, jsonServer.enabled, {silent: true});
        
    var jsonFederations = jsonServer.federations;
    var federations = new Collection();
    
    if (jsonFederations) {
      for (var i=0, l = jsonFederations.length; i < l; i++) {
    	var jsonFederation = jsonFederations[i];  
        var federation = new Federation();
        federation.id = jsonFederation.id;
        federation.set(Federation.NAME, jsonFederation.name, {silent: true});
        federations.add(federation, {silent: true});
      }        
      server.set(Server.FEDERATIONS, federations, {silent: true});
    }
    
    var jsonServerState = jsonServer.server_state;  
    
    var serverState = ServerStateSerializer.unserialize(jsonServerState);
    












    
    server.set(Server.SERVER_STATE, serverState, {silent: true});
    
    return server;
  }
};

ServerStateSerializer = {
    
  unserialize : function(jsonServerState) {    
    var serverState = new ServerState();
    
    var jsonPerformance = jsonServerState.performance;
    var jsonTask   = jsonServerState.task;
    var jsonMemory = jsonServerState.memory;
    var jsonSwap   = jsonServerState.swap;
    
    serverState.set(ServerState.LOG, jsonServerState.log);
    serverState.set(ServerState.PERFORMANCE, {load : jsonPerformance.load, cpu : jsonPerformance.cpu, users : jsonPerformance.users});
    serverState.set(ServerState.TASK, {total : jsonTask.total, execution : jsonTask.execution, sleeped : jsonTask.sleeped, stopped : jsonTask.stopped, zoombies : jsonTask.zoombies});
    serverState.set(ServerState.MEMORY, {total : jsonMemory.total, used : jsonMemory.used, available : jsonMemory.available});
    serverState.set(ServerState.SWAP, {total : jsonSwap.total, used: jsonSwap.used, available : jsonSwap.available});
        
    return serverState;
  }      
};

ServersSerializer = {
		
  serialize: function(server) {
	  
  },
  
  unserialize: function(jsonServers) {
    var servers = new Servers();
    
	for (var i=0; i < jsonServers.length; i++)  {
	  if (_.isFunction(jsonServers[i])) continue;
	  
	  var jsonServer = jsonServers[i];
	  var server = new Server();
	  server.set(Server.ID, jsonServer.id, {silent: true});
	  server.set(Server.NAME, jsonServer.name, {silent: true});    		  		
	  servers.add(server, {silent: true});
	}  
    return servers;
  }
};

FederationsSerializer = {
  serialize : function() {},
  
  unserialize : function(jsonFederations) {
	var federations = new Federations();
	
	  for (var i in jsonFederations) {
	    if (_.isFunction(jsonFederations[i])) continue;
	    federation = new Federation();
	    var jsonFederation = jsonFederations[i];
	  
	    federation.id = jsonFederation.id;	  
	    federation.set(Federation.NAME, jsonFederation.name, {silent: true});
	    federation.set(Federation.LABEL, jsonFederation.label, {silent: true});
	  
	    var jsonState = jsonFederation.state;
	    var state = new State();
	    state.set(State.RUNNING, jsonState.running, {silent : true});
	    state.set(State.TIME, jsonState.time, {silent : true});    
	    federation.set(Federation.STATE, state, {silent: true});	  
	    federations.add(federation, {silent: true});
	  }	
	return federations;
  }
};

FederationSerializer = {
		
  serialize : function(federation) {
    var json = {};
    json.id    = federation.id;
    json.name  = federation.get(Federation.NAME);
    json.label = federation.get(Federation.LABEL);
    json.logo  = federation.get(Federation.LOGO);
    json.url   = federation.get(Federation.URL);

    var state = federation.get(Federation.STATE);
    var jsonState = {};
    jsonState.running = state.get(State.RUNNING);
    jsonState.time    = state.get(State.TIME);
    
    json.state = jsonState;       
        
    var server = federation.get(Federation.SERVER);
    var jsonServer = {};
    jsonServer.id = server.id;
    jsonServer.name = server.get(Server.NAME);
    jsonServer.ip = server.get(Server.IP);
    
    json.server = jsonServer;
    
    json.user_auth        = federation.get(Federation.USER_AUTH);
    json.certificate_auth = federation.get(Federation.CERTIFICATE_AUTH);
    json.openid_auth      = federation.get(Federation.OPENID_AUTH);
    
    var connection = null;
    
    switch (federation.get(Federation.CONNECTION).get(Federation.CONNECTION_TYPE)) {
      case ConnectionTypes.DATABASE: connection = this.serializeDatabaseConnection(federation.get(Federation.CONNECTION));  break;
      case ConnectionTypes.LDAP: connection     = this.serializeLDAPConnection(federation.get(Federation.CONNECTION));  break;
      case ConnectionTypes.DATABASE: connection = this.serializeMockConnection(federation.get(Federation.CONNECTION));  break;    
    } 
    
    json.connection = connection;
    
    var jsonSpaces = SpacesSerializer.serialize(federation.get(Federation.SPACES));			
	json.spaces = jsonSpaces;
	
	var text = jsonText = Ext.util.JSON.encode(json);
	return text;
  },
  
  serializeDatabaseConnection : function(connection) {
    var jsonConnection = {};
    
    jsonConnection.type = connection.get(Federation.CONNECTION_TYPE);	
    jsonConnection.url = connection.get(DatabaseConnection.URL);	
    jsonConnection.user = connection.get(DatabaseConnection.USER);	
    jsonConnection.password = connection.get(DatabaseConnection.PASSWORD);
    
    var config = connection.get(DatabaseConnection.CONFIG);
    
    jsonConnection.config = {};
    jsonConnection.config.database_type = config.get(DatabaseConnectionConfig.DATABASE_TYPE);    
    
    return jsonConnection;
  },
  
  serializerLDAPConnection : function(connection) {
    var jsonConnection = {};
    jsonConnection.type = connection.get(Federation.CONNECTION_TYPE);	
    jsonConnection.url = connection.get(LDAPConnection.URL);	
    jsonConnection.user = connection.get(LDAPConnection.USER);	
    jsonConnection.password = connection.get(LDAPConnection.PASSWORD);
    
    var config = connection.get(LDAPConnection.CONFIG);
    
    jsonConnection.config = {};
    jsonConnection.config.schema = config.get(LDAPConnectionConfig.SCHEMA);
    
    var cnFieldParameter    = config.get(LDAPConnectionConfig.CN);
    var uidFieldParameter   = config.get(LDAPConnectionConfig.UID);
    var emailFieldParameter = config.get(LDAPConnectionConfig.EMAIL);
    var langFieldParameter  = config.get(LDAPConnectionConfig.LANG);
            
    jsonConnection.config.parameters = [cnFieldParameter, uidFieldParameter, emailFieldParameter, langFieldParameter];
     
    return jsonConnection;
  },
  
  serializerMockConnection : function(connection) {
	var jsonConnection = {};  
    jsonConnection.type = connection.get(Federation.CONNECTION_TYPE);	
    jsonConnection.url = connection.get(Federation.URL);	
    jsonConnection.user = connection.get(Federation.USER);	
    jsonConnection.password = connection.get(Federation.PASSWORD);
      
    return jsonConnection;    
  },
  
  
  unserialize : function(jsonFederation) {
	var federation = new Federation();
	
	federation.id = jsonFederation.id;
	federation.set(Federation.NAME, jsonFederation.name, {silent: true});
	federation.set(Federation.LABEL, jsonFederation.label, {silent: true});
	federation.set(Federation.LOGO, jsonFederation.logo, {silent: true});
	federation.set(Federation.URL, jsonFederation.url, {silent: true});
	
  var state = new State();
  state.set(State.RUNNING, jsonFederation.state.running, {silent : true});
  state.set(State.TIME, jsonFederation.state.time, {silent : true});	
  federation.set(Federation.STATE, jsonFederation.state, {silent: true});
  
	if (jsonFederation.server) {
	  var jsonServer = jsonFederation.server;
	  
	  var server = new Server();
	  server.id = jsonServer.id;
	  server.set(Server.NAME, jsonServer.name);
	  server.set(Server.IP, jsonServer.ip);    	  
	  federation.set(Federation.SERVER, server, {silent: true});
	}	  
  
	if (jsonFederation.authentication) {	  
	  federation.set(Federation.USER_AUTH, jsonFederation.authentication.user_auth, {silent: true});
	  federation.set(Federation.CERTIFICATE_AUTH, jsonFederation.authentication.certificate_auth, {silent: true});
	  federation.set(Federation.OPENID_AUTH, jsonFederation.authentication.openid_auth, {silent: true});	  
	}
	
	if (jsonFederation.connection) {
		var connection = null;
		
		switch (jsonFederation.connection.type) {
		  case 'database':
		    connection = this.unSerializeDatabaseConnection(jsonFederation.connection);    	  
		  break;
		  case 'ldap':
		    connection = this.unSerializeLDAPConnection(jsonFederation.connection);	   
		  break;
		  case 'mock':
            connection = this.unSerializeMockConnection(jsonFederation.connection);			  
		  break;
		}
		federation.set(Federation.CONNECTION, connection, {silent: true});
	}
	   	
	if (jsonFederation.spaces) {
		var spaces = SpacesSerializer.unserialize(jsonFederation.spaces);			
		federation.set(Federation.SPACES, spaces, {silent: true});
	}
	
	return federation;
  },
  
  unSerializeDatabaseConnection : function(jsonConnection) {
    var connection = new DatabaseConnection();
    connection.set(Federation.CONNECTION_TYPE, jsonConnection.type);
    connection.set(DatabaseConnection.URL, jsonConnection.url);
    connection.set(DatabaseConnection.USER, jsonConnection.user);
    connection.set(DatabaseConnection.PASSWORD, jsonConnection.password);
    
    if (jsonConnection.config) {
    	var jsonConfig = jsonConnection.config;
    	var config = new DatabaseConnectionConfig();
    	config.set(DatabaseConnectionConfig.DATABASE_TYPE, jsonConfig.database_type);    	
    	connection.set(DatabaseConnection.CONFIG, config, {silent: true});    	
    } 
    return connection;
  },
  
  unSerializeLDAPConnection : function(jsonConnection) {
    var connection = new LDAPConnection();
    connection.set(Federation.CONNECTION_TYPE, jsonConnection.type, {silent: true});
    connection.set(LDAPConnection.URL, jsonConnection.url, {silent: true});    
    connection.set(LDAPConnection.USER, jsonConnection.user, {silent: true});
    connection.set(LDAPConnection.PASSWORD, jsonConnection.password, {silent: true});
    
    if (jsConnection.config) {
    	var jsonConfig = jsonConnection.config;
    	var config = new LDAPConnectionConfig();
    	
    	config.set(LDAPConnectionConfig.SCHEMA, jsonConfig.schema, {silent: true});
    	        
        for (var i=0; i < jsonConfig.parameters.length; i++) {
    	  var parameter = jsonConfig.parameters[i];
    	  
    	  switch (parameter.name) {
    	    case LDAPAlias.CN:    config.set(LDAPConnectionConfig.CN, parameter, {silent: true}); break;
    	    case LDAPAlias.UID:   config.set(LDAPConnectionConfig.UID, parameter, {silent: true}); break;
    	    case LDAPAlias.EMAIL: config.set(LDAPConnectionConfig.EMAIL, parameter, {silent: true}); break;
    	    case LDAPAlias.LANG:  config.set(LDAPConnectionConfig.LANG, parameter, {silent: true}); break;
    	  };    	
        }
        
        connection.set(LDAPConnection.CONFIG, config, {silent: true});
    }
        
    return connection;    
  },
  
  unSerializeMockConnection : function() {
    var connection = new MockConnection();
    connection.set(Federation.CONNECTION_TYPE, jsonConnection.type);
    connection.set(LDAPConnection.URL, jsonConnection.url);    
    connection.set(LDAPConnection.USER, jsonConnection.user);
    connection.set(LDAPConnection.PASSWORD, jsonConnection.password);
    
    return connection;
  }
};

SpacesSerializer = {
    
    serialize: function(spaces) {
      var jsonSpaces = [];
      
      for (var i=0; i < spaces.size(); i++) {
        var jsonSpace = {};
        var space = spaces.get(i);
        jsonSpace.id = space.id;
        jsonSpace.name = space.get(Space.NAME);
                
        var state = space.get(Space.STATE);
        var jsonState = {};
        jsonState.running = state.get(State.RUNNING);
        jsonState.time = state.get(State.TIME);
        
        jsonSpace.state = jsonState;
        
        if (space.get(Space.MODEL_VERSION)) {
          var modelVersion = space.get(Space.MODEL_VERSION);        
          var jsonModelVersion = {};
          jsonModelVersion.id = modelVersion.get(MODEL_VERSION.ID);
          jsonModelVersion.label = modelVersion.get(MODEL_VERSION.LABEL);
         
          jsonSpace.model_version = jsonModelVersion;         
        }
        
        jsonSpaces.push(jsonSpace);
      }

      var text = Ext.util.JSON.encode(jsonSpaces);
      return text;
    },

    unserialize: function(jsonSpaces) {
      var spaces = new Spaces();

      for (var i=0; i < jsonSpaces.length; i++)  {
        if (_.isFunction(jsonSpaces[i])) continue;

        var jsonSpace = jsonSpaces[i];

        var space = new Space();
        space.set(Space.ID, jsonSpace.id, {silent: true});
        space.set(Space.NAME, jsonSpace.name, {silent: true});
        
        if (jsonSpace.model_version) {
          var jsonModelVersion = jsonSpace.model_version;
          var modelVersion = new ModelVersion();
          modelVersion.id = jsonModelVersion.id;
          modelVersion.set(ModelVersion.LABEL, jsonModelVersion.label, {silent: true});
          modelVersion.set(ModelVersion.METAMODEL, jsonModelVersion.metamodel, {silent: true});
        
          space.set(Space.MODEL_VERSION, modelVersion, {silent : true});
        }
        
        var jsonState = jsonSpace.state;
        var state = new State();
        state.set(State.RUNNING, jsonState.running, {silent : true});
        state.set(State.TIME, jsonState.time, {silent : true});    
        space.set(Space.STATE, state, {silent: true});    

        if (jsonSpace.federation) {
          var jsonServer = jsonSpace.federation.server;
          var server = new Server();
          server.id = jsonServer.id;
          server.set(Server.NAME, jsonServer.name, {silent: true});

          var jsonFederation = jsonSpace.federation;
          var federation = new Federation();
          federation.id = jsonFederation.id;
          federation.set(Federation.NAME, jsonFederation.name, {silent: true});
          federation.set(Federation.SERVER, server, {silent: true});

          space.set(Space.FEDERATION, federation, {silent: true});
        }

        spaces.add(space, {silent: true});
      }  
      return spaces;
    }
};

PublicationServicesSerializer = {

  serialize : function(services) {
  	  
  },
  
  unserialize : function(jsonServices) {
    var services = new Collection();
	  
	for (var i=0; i < jsonServices.length; i++) {
	  if (_.isFunction(jsonServices[i])) continue;
	  
	  var jsonService = jsonServices[i];
	  var service = new PublicationService();
	  service.id = jsonService.id;
	  service.set(PublicationService.NAME, jsonService.name, {silent: true});
	  service.set(PublicationService.TYPE, jsonService.type, {silent: true});
	  service.set(PublicationService.PUBLISHED, jsonService.published, {silent: true});
	  
	  services.add(service);
	}
	return services;
  }
};

ModelVersionSerializer = {
    
  serialize : function(version) {
    var json = {};
    json.id = version.id;
    json.label = version.get(ModelVersion.LABEL);
    json.date  = version.get(ModelVersion.DATE);
    json.metamodel = version.get(ModelVersion.METAMODEL);
        
    var text = Ext.util.JSON.encode(json);
    return text;
  },
    
  unserialize : function(json) {
    var modelVersion = new ModelVersion();
    modelVersion.id = json.id;
    modelVersion.set(ModelVersion.LABEL, json.label, {silent: true});
    modelVersion.set(ModelVersion.DATE, json.date, {silent: true});
    modelVersion.set(ModelVersion.METAMODEL, json.metamodel, {silent: true});
        
    return modelVersion;
  }
};

SpaceSerializer = {
  
  serialize: function(space) {
    var json = {};
    json.id = space.id;
    json.name = space.get(Space.NAME);
    json.label = space.get(Space.LABEL);
    json.logo = space.get(Space.LOGO);
    json.url = space.get(Space.URL);
    json.datawarehouse = space.get(Space.DATAWAREHOUSE);
    json.model_id = space.get(Space.MODEL_ID);
    
    var modelVersion = space.get(Space.MODEL_VERSION);
    if (modelVersion) {
      json.model_version = ModelVersionSerializer.serialize(modelVersion);
    }
    
    var state = space.get(Space.STATE);
    var jsonState = {};
    jsonState.running = state.get(State.RUNNING);
    jsonState.time    = state.get(State.TIME);
    
    json.state = jsonState;       
    
    var jsonFederation = {};
    var federation = space.get(Space.FEDERATION);    
    jsonFederation.id   = federation.id;
    jsonFederation.name = federation.get(Federation.NAME);
        
    var jsonServer = {};
    var server = federation.get(Federation.SERVER);
    jsonServer.id    = server.id;
    jsonServer.name = server.get(Server.NAME);
    jsonServer.ip    = server.get(Server.IP);
    
    jsonFederation.server = jsonServer;    
    json.federation = jsonFederation;
        
    var services = space.get(Space.SERVICES);
    var jsonServices = [];
    services.each(function(index, item) {
      var jsonService = {name: item.get(PublicationService.NAME), type: item.get(PublicationService.TYPE), published : item.get(PublicationService.PUBLISHED)};
      jsonServices.push(jsonService);
    });
    json.services = jsonServices;    
        
	var text = Ext.util.JSON.encode(json);
	return text;
  },
  
  unserialize: function(jsonSpace) {
    var space = new Space();
    space.set(Space.ID, jsonSpace.id, {silent: true});
    space.set(Space.NAME, jsonSpace.name, {silent: true});
    space.set(Space.LABEL, jsonSpace.label, {silent: true});    
    space.set(Space.LOGO, jsonSpace.logo, {silent: true});
    space.set(Space.URL, jsonSpace.url, {silent: true});
    space.set(Space.DATAWAREHOUSE, jsonSpace.datawarehouse, {silent: true});    
    
    if (jsonSpace.model_version) {
      var modelVersion = ModelVersionSerializer.unserialize(jsonSpace.model_version);    
      if (! modelVersion) {
        space.set(Space.MODEL_VERSION, modelVersion, {silent: true});
      }
    }
    
    var state = new State();
    state.set(State.RUNNING, jsonSpace.state.running, {silent : true});
    state.set(State.TIME, jsonSpace.state.time, {silent : true});

    space.set(Space.STATE, state, {silent: true});
    
    if (jsonSpace.federation) {
      var jsonFederation = jsonSpace.federation;
      var jsonServer = jsonFederation.server;
      
      var server = new Server();
      server.id = jsonServer.id;
      server.set(Server.NAME, jsonServer.name, {silent: true});
      server.set(Server.IP, jsonServer.ip, {silent: true});
      
      var federation = new Federation();
      federation.id = jsonFederation.id;
      federation.set(Federation.NAME, jsonFederation.name, {silent: true});
      federation.set(Federation.SERVER, server, {silent: true});
      space.set(Space.FEDERATION, federation, {silent: true});
    }
    
    if (jsonSpace.services) {
      var services = new Collection();
      
      for (var i=0; i < jsonSpace.services.length; i++) {
     	var jsonService = jsonSpace.services[i];
        var service = new PublicationService();        
        service.set(PublicationService.NAME, jsonService.name, {silent: true});
        service.set(PublicationService.TYPE, jsonService.type, {silent: true});
        service.set(PublicationService.PUBLISHED, jsonService.published, {silent: true});
        
        services.add(service, {silent: true});    	  
      }
      space.set(Space.SERVICES, services, {silent: true});
    }
    
    return space;
  }
};

ModelsSerializer = {
	serialize : function(models) {
		
	},
	
  unserialize : function(jsonModels) {
    var models = new Collection();

    for (var i=0; i < jsonModels.length; i++)  {
      if (_.isFunction(jsonModels[i])) continue;

      var jsonModel = jsonModels[i];
      var model = new BusinessModel();
      model.id = jsonModel.id;
      model.set(BusinessModel.NAME , jsonModel.name, {silent: true});		  
      models.add(model, {silent: true});
    }  
    return models;
  }
};

ModelSerializer = {

  serialize : function(model) {
	  var json = {};
	  json.id = model.id;
	  json.label = model.get(BusinessModel.LABEL);
	  json.logo  = model.get(BusinessModel.LOGO);
	  
	  var jsonVersions = [];
	  var versions = model.get(BusinessModel.VERSIONS);
	  
	  versions.each(function(index, version) {
	    var jsonVersion = ModelVersionSerializer.serialize(version);
	    jsonVersions.push(jsonVersion);	    
	  });
	  
	  json.versions = jsonVersions;
	  var text = Ext.util.JSON.encode(json);
	  return text;
  },
  
  unserialize : function(jsonModel) {
    var model = new BusinessModel();
    model.id = jsonModel.id;
    model.set(BusinessModel.NAME, jsonModel.name, {silent: true});
    model.set(BusinessModel.LABEL, jsonModel.label, {silent: true});
    model.set(BusinessModel.LOGO, jsonModel.logo, {silent: true});
    model.set(BusinessModel.LATEST_VERSION, jsonModel.latest_version, {silent: true});
    
    var versions = new Collection();
    
    for (var i=0, l = jsonModel.versions.length; i < l; i++) {      
      var jsonVersion = jsonModel.versions[i];
      var version = ModelVersionSerializer.unserialize(jsonVersion);
      versions.add(version, {silent: true});           
    }
    
    model.set(BusinessModel.VERSIONS, versions, {silent: true});    
    return model;
  }
};



GridedService = {
  		
};



GridedService.loadAccount = function(callback) {
  var request = new Request({
    url: Context.Config.Api + "?op=loadaccount",
	  method: 'get',
	  params: {},
	  callback : callback  
  });	
  
  request.send();	
};


GridedService.loadServers = function(callback) {
  var serviceCallback = {
	success : function(jsonServer) {
	  var servers = ServersSerializer.unserialize(jsonServer);
	  callback.success.call(callback.context, servers);
	},
	failure : function() { callback.failure.apply(callback.context, arguments); }
  };		
	
  var request = new Request({
    url: Context.Config.Api + "?op=loadservers",
	  method: 'get',
	  params: {},
	  callback : serviceCallback  
  });	
  
  request.send();
};


GridedService.loadServer = function(id, callback) {
  var serviceCallback = {
    success: function(jsonServer) {
      var server = ServerSerializer.unserialize(jsonServer);
      callback.success.call(callback.context, server);
    },
    failure: function() { callback.failure.apply(callback.context, arguments); }
  };
  
  var request = new Request({
    url: Context.Config.Api + "?op=loadserver",
    params: {id: id},
	  method: 'get',	
	  callback : serviceCallback
  });	
  
  request.send();
};


GridedService.removeServers = function(ids, callback) {
  var json = Ext.util.JSON.encode(ids);
  
  var request = new Request({
    url: Context.Config.Api + "?op=removeservers",
    params: {ids: json},
	  method: 'get',	
	  callback : callback
  });	
  
  request.send();
};


GridedService.addServer = function(server, callback) {
  var serviceCallback = {
    success: function(jsonServer) {
	  var server = ServerSerializer.unserialize(jsonServer);
      callback.success.call(callback.context, server);
    },
	failure: function() { callback.failure.apply(callback.context, arguments); }
  };
	
  var request = new Request({
	  url: Context.Config.Api + "?op=addserver",
	  params: {name: server.get(Server.NAME), ip: server.get(Server.IP)},
	  method: 'get',	
	  callback : serviceCallback
  });
  request.send();
};


GridedService.saveServer = function(server, callback) {
	
  jsonServer = ServerSerializer.serialize(server);
  
  var request = new Request({
	  url: Context.Config.Api + "?op=saveserver",
	  params: {server: jsonServer},
	  method: 'get',	
	  callback : callback
  });
  request.send();
};


GridedService.loadServerSpaces = function(id, callback) {
  var serviceCallback = {
    success: function(jsonSpaces) {
    var spacesCollection = SpacesSerializer.unserialize(jsonSpaces);
	  callback.success.call(callback.context, spacesCollection);
	},
	failure: function() { callback.failure.apply(callback.context, arguments); } 
  };
  
  var request = new Request({
    url: Context.Config.Api + "?op=loadserverspaces",
    params: {id: id},                
	  method: 'get',	  
	  callback : serviceCallback  
  });	
		  
  request.send(); 	
};


GridedService.loadServerState = function(id, callback) {
  var serviceCallback = {
    success: function(jsonServerState) {
      var serverState = ServerStateSerializer.unserialize(jsonServerState);
      callback.success.call(callback.context, serverState);
    },
    failure: function() { callback.failure.apply(callback.context, arguments); } 
  };   
  
  var request = new Request({
    url: Context.Config.Api + "?op=loadserverstate",
    params: {id: id},
	  method: 'get',	  
	  callback : serviceCallback  
  });	
  
  request.send();
};


GridedService.loadAllFederations = function(callback) {
  var serviceCallback = {
    success: function(jsonFederations) {
	  var federationsCollection = FederationsSerializer.unserialize(jsonFederations);
	  callback.success.call(callback.context, federationsCollection);
	},
	failure: function() { callback.failure.apply(callback.context, arguments); }
 };
	
  var request = new Request({
    url: Context.Config.Api + "?op=loadfederations",    
	  method: 'get',
	  params: {},
	  callback : serviceCallback  
  });	
	  
  request.send();	
};


GridedService.loadFederation = function(id, callback) {

  var serviceCallback = {
   success: function(jsonFederation) {
     var federation = FederationSerializer.unserialize(jsonFederation);
	   callback.success.call(callback.context, federation);
	},
    failure: function() { callback.failure.apply(callback.context, arguments); }
 };
	
  var request = new Request({
    url: Context.Config.Api + "?op=loadfederation",
    method: 'get',	
    params: {id: id},
	callback : serviceCallback  
  });	
  
  request.send();
};



GridedService.addFederation = function(serverId, name, url, callback) {
  var serviceCallback = {
    success: function(jsonFederation) {
    var federation = FederationSerializer.unserialize(jsonFederation);
	  callback.success.call(callback.context, federation);
	},
    failure: function() { callback.failure.apply(callback.context, arguments); }
  };
				
  var request = new Request({
    url: Context.Config.Api + "?op=addfederation",
	  method: 'get',	
	  params: {server_id: serverId, name: name, url: url},
	  callback : serviceCallback  
  });	
			  
  request.send();    
};


GridedService.saveFederation = function(federation, callback) {
	
  jsonFederation = FederationSerializer.serialize(federation);
  
  var request = new Request({
	  url: Context.Config.Api + "?op=savefederation",
	  params: {federation: jsonFederation},
	  method: 'get',	
	  callback : callback
  });
  request.send();
};


GridedService.removeFederations = function(ids, callback) {
  var json = Ext.util.JSON.encode(ids);
  
  var request = new Request({
    url: Context.Config.Api + "?op=removefederations",
    params: {ids: json},
	  method: 'get',	
	  callback : callback
  });	
			  
  request.send();	
};


GridedService.startFederation = function(id, callback) {    
  var serviceCallback = {
    success: function(jsonFederation) {
	    var federation = FederationSerializer.unserialize(jsonFederation);
      callback.success.call(callback.context, federation);
	},
    failure: function() { callback.failure.apply(callback.context, arguments); }
 };
	
  var request = new Request({
    url: Context.Config.Api + "?op=startfederation",
    params: {id: id},
	  method: 'get',	  
	  callback : serviceCallback
  });	
			  
  request.send();	
};


GridedService.stopFederation = function(id, callback) {    
  var serviceCallback = {
    success: function(jsonFederation) {
	    var federation = FederationSerializer.unserialize(jsonFederation);
      callback.success.call(callback.context, federation);
	},
    failure: function() { callback.failure.apply(callback.context, arguments); }
 };
	
  var request = new Request({
    url: Context.Config.Api + "?op=stopfederation",
    params: {id: id},
	  method: 'get',	
	  callback : serviceCallback
  });	
			  
  request.send();	
};




GridedService.loadSpace = function(id, callback) {
  var serviceCallback = {
    success: function(jsonSpace) {
      var space = SpaceSerializer.unserialize(jsonSpace);
      callback.success.call(callback.context, space);
    },
    failure: function(ex) { callback.failure.apply(callback.context, ex); }
 };
				
  var request = new Request({
    url: Context.Config.Api + "?op=loadspace",
    method: 'get',	
    params: {id: id},
	  callback : serviceCallback  
  });	
			  
  request.send();  	
};


GridedService.loadSpacesWithModel = function(id, version, callback) {
  var serviceCallback = {
    success : function(jsonSpaces) {
      var spaces = SpacesSerializer.unserialize(jsonSpaces);
      callback.success.call(callback.context, spaces, version);
    },
    failure : function(ex) { callback.failure.apply(callback.context, ex); }
  };
  
  var request = new Request({
    url: Context.Config.Api + "?op=loadspaceswithmodel",
    method: 'get',  
    params: {model_id: id, version_id: version.id},
    callback : serviceCallback  
  }); 
        
  request.send();
};


GridedService.loadSpacesByModel = function(modelId, callback) {
  var serviceCallback = {
    success : function(jsonSpaces) {
      var spaces = SpacesSerializer.unserialize(jsonSpaces);
      callback.success.call(callback.context, spaces);
    },
    failure : function(ex) { callback.failure.apply(callback.context, ex); }
  };
  
  var request = new Request({
    url: Context.Config.Api + "?op=loadspacesbymodel",
    method: 'get',  
    params: {model_id: modelId},
    callback : serviceCallback  
  }); 
        
  request.send();
};



GridedService.loadServices = function(spaceId, callback) {
  var serviceCallback = {
    success: function(jsonServices) {
      var services = PublicationServicesSerializer.unserialize(jsonServices);
      callback.success.call(callback.context, services);
    },
    failure: function(ex) { callback.failure.apply(callback.context, ex); }
  };
							
  var request = new Request({
    url: Context.Config.Api + "?op=loadservices",
    method: 'get',	
    params: {space_id: spaceId},
	  callback : serviceCallback
  });	
						  
  request.send();
};


GridedService.loadImporterTypes = function(spaceId, callback) {              
  var request = new Request({
    url: Context.Config.Api + "?op=loadimportertypes",
    method: 'get',  
    params: {space_id: spaceId},
    callback : callback
  }); 
              
  request.send();
};


GridedService.startImport = function(form, spaceId, importerTypeId, callback) {
  var params = {
    space_id : spaceId,
    importer_type_id : importerTypeId
  };
  
  this.upload(form, "startimport", params, callback);    
};


GridedService.saveSpace = function(space, callback) {
  
  jsonSpace = SpaceSerializer.serialize(space);
										
  var request = new Request({
    url: Context.Config.Api + "?op=savespace",
    method: 'get',	
    params: {space: jsonSpace},
	callback : callback
  });	
									  
  request.send();	
};


GridedService.addSpace = function(serverId, federationId, name, url, modelId, callback) {
  var serviceCallback = {
    success: function(jsonSpace) {
      var space = SpaceSerializer.unserialize(jsonSpace);
	    callback.success.call(callback.context, space);
	  },
    failure: function() { callback.failure.apply(callback.context, arguments); }
  };
				
  var request = new Request({
    url: Context.Config.Api + "?op=addspace",
	  method: 'get',	
	  params: {server_id: serverId, federation_id: federationId, name: name, url : url, model_id : modelId},
	  callback : serviceCallback  
  });	
			  
  request.send();    
};


GridedService.removeSpaces = function(ids, callback) {
  var json = Ext.util.JSON.encode(ids);
  
  var request = new Request({
    url: Context.Config.Api + "?op=removespaces",
    params: {ids: json},
	  method: 'get',	
	  callback : callback
  });	
			  
  request.send();	
};


GridedService.upgradeSpaces = function(spaceIds, modelId, versionId, callback) {
  var json = Ext.util.JSON.encode(spaceIds);

  var serviceCallback = {
    success : function() {      
      callback.success.call(callback.context, spaceIds, modelId, versionId);
    },
    failure : function() { callback.failure.apply(callback.context, arguments); }
  };
  
  var request = new Request({
    url: Context.Config.Api + "?op=upgradespaces",
    params: {ids: json, model_id: modelId, version_id : versionId},
    method: 'get',    
    callback : serviceCallback
  }); 
        
  request.send();
};


GridedService.startSpace = function(id, callback) {    
  var serviceCallback = {
    success: function(jsonSpace) {
      var space = SpaceSerializer.unserialize(jsonSpace);
      callback.success.call(callback.context, space);
  },
    failure: function() { callback.failure.apply(callback.context, arguments); }
 };
  
  var request = new Request({
    url: Context.Config.Api + "?op=startspace",
    params: {id: id},
    method: 'get',
    callback : serviceCallback
  }); 
        
  request.send(); 
};


GridedService.stopSpace = function(id, callback) {    
  var serviceCallback = {
    success: function(jsonSpace) {
      var space = SpaceSerializer.unserialize(jsonSpace);
      callback.success.call(callback.context, space);
   },
   failure: function() { callback.failure.apply(callback.context, arguments); }
 };
  
  var request = new Request({
    url: Context.Config.Api + "?op=stopspace",
    params: {id: id},
    method: 'get',    
    callback : serviceCallback
  }); 
        
  request.send(); 
};


GridedService.loadModels = function(callback) {
  var serviceCallback = {
	success : function(jsonModel) {
	  var models = ModelsSerializer.unserialize(jsonModel);
	  callback.success.call(callback.context, models);
	},
	failure : function() { callback.failure.apply(callback.context, arguments); }
  };		
	
  var request = new Request({
    url: Context.Config.Api + "?op=loadmodels",
	  method: 'get',
	  params: {},
	  callback : serviceCallback
  });	
  
  request.send();
};


GridedService.loadModel = function(id, callback) {
  var serviceCallback = {
    success: function(jsonModel) {
      var model = ModelSerializer.unserialize(jsonModel);
      callback.success.call(callback.context, model);
    },
    failure: function() { callback.failure.apply(callback.context, arguments); }
  };
  
  var request = new Request({
    url: Context.Config.Api + "?op=loadmodel",
    params: {id: id},
	  method: 'get',	  
	  callback : serviceCallback
  });	
  
  request.send();
};


GridedService.uploadModelVersion = function(form, params, callback) {
  var serviceCallback = {
    success: function(jsonModel) {         
      var model = ModelVersionSerializer.unserialize(jsonModel);
      callback.success.call(callback.context, model);
    },
    failure: function() { callback.failure.apply(callback.context, arguments); }
  };

  this.upload(form, "uploadmodelversion", params, serviceCallback);
};


GridedService.saveModel = function(model, callback) {
  var jsonModel = ModelSerializer.serialize(model);
  
  var request = new Request({
    url: Context.Config.Api + "?op=savemodel",
    method: 'get',  
    params: {model: jsonModel},
    callback : callback
  }); 
                    
  request.send();   
};


GridedService.addModel = function(form, name, callback) {
  var serviceCallback = {
    success: function(jsonModel) {
      var model = ModelSerializer.unserialize(jsonModel);
      callback.success.call(callback.context, model);
    },
    failure: function() { callback.failure.apply(callback.context, arguments); }      
  };
        

  
  this.upload(form, "addmodel", [], serviceCallback);

};



GridedService.removeModels = function(ids, callback) {
  var json = Ext.util.JSON.encode(ids);
  
  var request = new Request({
    url: Context.Config.Api + "?op=removemodels",
    params: {ids: json},
    method: 'get',    
    callback : callback
  }); 
        
  request.send(); 
};


GridedService.uploadImage = function(form, params, callback) {
  var serviceCallback = {
    success: function(jsonImage) {      	 
      var name   = URLEncoder.decode(jsonImage.name);
      var source = URLEncoder.decode(jsonImage.source);
      callback.success.call(callback.context, name, source);
    },
    failure: function() { callback.failure.apply(callback.context, arguments); }
  };
	
  this.upload(form, "uploadimage", params, serviceCallback);
};


GridedService.downloadImage = function(jsonIds, filename, callback) {
  	
  var request = new Request({
    url: Context.Config.Api + "?op=downloadImage",
    params: {'server-id': jsonIds.serverId, 'federation-id': jsonIds.federationId, 'space-id': jsonIds.spaceId, filename: filename},
	  method: 'get',	
	  callback : callback
  });						  
  request.send();	
};



GridedService.upload = function(form, operation, parameters, callback) {
  form.action = Context.Config.Api;
  addInput(form, "op", operation);
  
  if (parameters != null) {
    for (var name in parameters) {
      if (isFunction(parameters[name])) continue;
      addInput(form, name, parameters[name]);
    }
  }
  
  var request = new Request({
	  url :  Context.Config.Api + "?op=" + operation,  
	  method: 'post',	
	  form: form,
	  isUpload: true,
	  callback : callback  
  });
      
  request.send();
};

var TextEditor = Editor.extend({	
  init : function(element, object, propertyName) {    	  
	this._super();
	this.element = element;
	this.object = object;
	this.propertyName = propertyName;
	
	this.oldValue = element.value;
	this.newValue = this.oldValue;
  }	
});


TextEditor.prototype.open = function() {
  this._bind();
  this.state = Editor.OPENED;
};


TextEditor.prototype.close = function() {
  this._unBind();
  this.state = Editor.CLOSED;
};


TextEditor.prototype.isDirty = function() {
  return this.oldValue != this.newValue;	
};


TextEditor.prototype.flush = function() {
  if (this.object.set(this.propertyName, this.newValue)) {
	  this.oldValue = this.newValue; 	  
  }
};


TextEditor.prototype.reset = function() {
  this.newValue = this.oldValue;
  this.element.value = this.oldValue;
};


TextEditor.prototype.showError = function(error) {
	
};


TextEditor.prototype._bind = function() {
  this.extElement = Ext.get(this.element);
  this.extElement.on('keyup', this._changeHandler, this);
};


TextEditor.prototype._unBind = function() {
  if (! this.extElement) return;
  this.extElement.un('keyup', this._changeHandler);
  this.extElement = null;
};


TextEditor.prototype._changeHandler = function(event, target, options) {
  this.newValue = target.value;
  
  if (this.newValue == this.oldValue) ev = {name: Events.RESTORED, data: {editor: this, value: this.newValue}};  
  else ev = {name: Events.CHANGED, data: {editor: this, value: this.newValue}};  
  this.fire(ev);
};

var CheckboxEditor = Editor.extend({	
  init : function(element, object, propertyName) {    	  
	this._super();
	this.element = element;
	this.object = object;
	this.propertyName = propertyName;
	
	this.oldValue = object[propertyName];
	this.newValue = this.oldValue;
	this.element.checked = this.newValue;
  }	
});


CheckboxEditor.prototype.open = function() {
  this._bind();
  this.state = Editor.OPENED;
};


CheckboxEditor.prototype.close = function() {
  this._unBind();
  this.state = Editor.CLOSED;
};


CheckboxEditor.prototype.isDirty = function() {
  return this.oldValue != this.newValue;	
};


CheckboxEditor.prototype.flush = function() {
  this.object[this.propertyName] = this.newValue;	
  this.oldValue = this.newValue;
};


CheckboxEditor.prototype.reset = function() {
  this.newValue = this.oldValue;
  this.element.checked = this.oldValue;
};


CheckboxEditor.prototype._bind = function() {
  this.extElement = Ext.get(this.element);
  this.extElement.on('change', this._changeHandler, this);  
};


CheckboxEditor.prototype._unBind = function() {
  if (! this.extElement) return;
  this.extElement.un('change', this._changeHandler);  
  this.extElement = null;
};


CheckboxEditor.prototype._changeHandler = function(event, target, options) {
  var ev = null;
  
  this.newValue = target.checked;
  if (this.newValue == this.oldValue) ev = {name: Events.RESTORED, data: {editor: this, value: this.newValue}};
  else ev = {name: Events.CHANGED, data: {editor: this, value: this.newValue}}; 
  this.fire(ev);
};

var SelectEditor = Editor.extend({	
  init : function(element, object, propertyName) {    	  
	this._super();
	this.element = element;
	this.object = object;
	this.propertyName = propertyName;
	
	this.oldSelectedIndex = element.selectedIndex;
	this.newSelectedIndex = this.oldSelectedIndex;
  }	
});


SelectEditor.prototype.open = function() {
  this._bind();
  this.state = Editor.OPENED;
};


SelectEditor.prototype.close = function() {
  this._unBind();
  this.state = Editor.CLOSED;
};


SelectEditor.prototype.isDirty = function() {
  return this.oldSelectedIndex != this.newSelectedIndex;	
};


SelectEditor.prototype.flush = function() {
  this.object[this.propertyName] = this.element.options[this.newSelectedIndex].getAttribute('value');
  this.oldSelectedIndex = this.newSelectedIndex;
};


SelectEditor.prototype.reset = function() {
  this.newSelectedIndex = this.oldSelectedIndex;
  this.element.selectedIndex = this.oldSelectedIndex;
};


SelectEditor.prototype._bind = function() {
  this.extElement = Ext.get(this.element);
  this.extElement.on('change', this._changeHandler, this);  
};


SelectEditor.prototype._unBind = function() {
  if (! this.extElement) return;
  this.extElement.un('change', this._changeHandler);  
  this.extElement = null;
};


SelectEditor.prototype._changeHandler = function(event, target, options) {
  var ev = null;  
  this.newSelectedIndex = target.selectedIndex;    

  if (this.newSelectedIndex == this.oldSelectedIndex) ev = {name: Events.RESTORED, data: {editor: this, selectedIndex: this.newSelectedIndex}};
  else ev = {name: Events.CHANGED, data: {editor : this, selectedIndex: this.newSelectedIndex}};
  this.fire(ev);
};

var ImageEditor = Editor.extend({	
  init : function(element, object, propertyName) {    	  
	this._super();
	this.element = element;
	this.object = object;
	this.propertyName = propertyName;
	
	this.imageName = "";
	this.oldSource = element.getAttribute('src');
	this.newSource = this.oldSource;
  }	
});


ImageEditor.prototype.open = function() {
  this._bind();
  this.state = Editor.OPENED;
};


ImageEditor.prototype.close = function() {
  this._unBind();
  this.state = Editor.CLOSED;
};


ImageEditor.prototype.setImage = function(filename, source) {
  this.newSource = source;
  this.element.src = source;
  this.imageName = filename;
  this._changeHandler(source);
};


ImageEditor.prototype.isDirty = function() {
  return this.oldSource != this.newSource;	
};


ImageEditor.prototype.flush = function() {
  this.object[this.propertyName] = this.imageName;
  this.oldSource = this.newSource;
};


ImageEditor.prototype.reset = function() {
  this.newSource = this.oldSource;
  this.element.setAttribute('src', this.oldSource);
};


ImageEditor.prototype._bind = function() {
  this.extElement = Ext.get(this.element);   
};


ImageEditor.prototype._unBind = function() {
  if (! this.extElement) return;
  this.extElement = null;
};


ImageEditor.prototype._changeHandler = function(newSource) {
  var ev = null;
  this.newSource = newSource;
  
  if (this.newSource == this.oldSource) ev = {name: Events.RESTORED, data: {editor: this, value: this.newSource}};
  else ev = {name: Events.CHANGED, data: {editor: this, value: this.newSource}};
  this.fire(ev);
};




function ListComponent(layername) {
  var html = "<ul></ul>";
  this.layername = layername;
  this.$layer = $(layername);
  this.$layer.innerHTML = html;
  this.extElement = Ext.get(this.$layer.firstChild);

  this.items  = [];
  this.models = [];
  this.bind();
}


ListComponent.prototype.setItemViewClass = function(viewclass) {
  this.itemViewClass = viewclass;
};


ListComponent.prototype.selectFirstItem = function() {
  if (this.items.length == 0) return;	
  this._doSelect(0);
};


ListComponent.prototype.add = function(model) {  	
  var item = new ListItem(this.extElement);
  var view = new this.itemViewClass(model.data);
  view.el().index = model.index;  
  item.insert(view.el());
  
  this.items[model.index] = item;
  this.models[model.index] = model.data;
};


ListComponent.prototype.remove = function(model) {
  var item = this.items[model.index];
  if (item) this.extElement.dom.removeChild(item.el());    
};


ListComponent.prototype.onSelect = function(handler, scope) {
 this.handler = handler; 
 this.scope = scope;
};


ListComponent.prototype.bind = function() {
  this.extElement.on('click', this._clickHandler, this, {delegate: 'li'});  
};


ListComponent.prototype.el = function() {
  this.extElement.dom;
};


ListComponent.prototype._clickHandler = function(event, target, options) {
  event.preventDefault();  	
  var element = target.firstChild;    
  this._doSelect(element.index);  
};


ListComponent.prototype._doSelect = function(index) {    	
  var view = this.items[index];
  var model = this.models[index];
  view.select();
  this.handler.apply(this.scope, [model]);  	
};




function ListItem(extParent) {
  this.extParent  = extParent;	
  this.extElement = Ext.DomHelper.append(extParent, {tag: 'li'}, true);
  this.Styles = {
    item_selected : 'item_selected',
    item_unselected : 'item_unselected'
  };
}


ListItem.prototype.insert = function(child) {
  this.extElement.appendChild(child);
};


ListItem.prototype.select = function() {
  var extElements = this.extParent.select('li');
  Ext.each(extElements, function(elt) {
	 elt.removeClass(this.Styles.item_selected); 	 
  }, this);
  this.extElement.addClass(this.Styles.item_selected);
};


ListItem.prototype.el = function() {
  return this.extElement.dom;
};

function Wizard() {
  this.currentPage = 0;
  this.pagesCount = 0;
  this.pages = [];
}


Wizard.prototype.addPage = function(page) {
  this.pages[this.pagesCount++] = page;
};


Wizard.prototype.isInFirstPage = function() {
  return this.currentPage === 0;
};


Wizard.prototype.isInLastPage = function() {
  return this.currentPage + 1 === this.pagesCount;
};


Wizard.prototype.isCurrentPageCompleted = function() {
  return this.pages[this.currentPage].isCompleted();
};


Wizard.prototype.getCurrentPage = function() {
  return this.pages[this.currentPage];
};


Wizard.prototype.start = function() {
  this.currentPage = 0;
  this._hideAllPages();
  this.pages[this.currentPage].show();
};


Wizard.prototype.next = function() {
  if (this.currentPage >= this.pagesCount) return;
  this.pages[this.currentPage].hide();
  this.currentPage += 1;
  this.pages[this.currentPage].show();
};


Wizard.prototype.previous = function() {
  if (this.currentPage == 0) return;
  this.pages[this.currentPage].hide();
  this.currentPage -= 1;
  this.pages[this.currentPage].show();
};


Wizard.prototype._hideAllPages = function() {
  for (var i=0 , l = this.pages.length; i < l; i++) {
    var page = this.pages[i];
    page.hide();
  }
};





function Page(composite) {
  this.composite = composite;    
}


Page.prototype.show = function() {
  this.composite.show();
};


Page.prototype.hide = function() {
  this.composite.hide();
};






var Table = EventsSource.extend({
  init : function(element, columns, options) {
	this._super();
	this.html = AppTemplate.Table;
	this.element = element;
	this.columns = columns;
	this.options = options;
	this.rows = [];	
	this.id = _.uniqueId();
	
	this.mouseOverFlag = false;
	this.browsingFlag = true;
	
	this.enterFlag = false;
  }
});

Table.CLICK_ROW_ACTION = 'click_action';
Table.CLICK_ROW = 'click';
Table.CHECK_ROW = 'check';
Table.MOUSE_ENTER  = 'mouseenter';
Table.MOUSE_LEAVE  = 'mouseleave';
Table.ROW_MOUSE_OVER  = 'rowmouseover';
Table.ROW_MOUSE_OUT  = 'rowmouseout';

Table.MOUSE_EVENTS_TIME = 300;


Table.prototype.setData = function(rows) {  
  this.rows = rows;
  this._bind();
};


Table.prototype.setAction = function(action) {
  this.action = action;	
};


Table.prototype.getSelectedRows = function() {
  if (! this.options.checkbox) return 0;
  
  var rows = [];
  if (! this.extParent) return rows;
  var tbody = this.extTable.dom.firstChild;
  if (!tbody) return rows;
  
  for (var i=0; i < tbody.childNodes.length; i++) {
	var row = tbody.childNodes[i];  
    var input = Ext.get(row).query('input[type="checkbox"]').first();	 
	 if (input.checked) {
		 row = this._getRow(row);
	     rows.push(row);	     
	 }
  }  
  return rows;  
};


Table.prototype.each = function(elementSelector, callback, scope) {
  var elements = this.extParent.query(elementSelector);
  for (var i=0; i < elements.length; i++) {
	var element = elements[i];  
	callback.call(scope, element, i);
  }
};


Table.prototype.selectRow = function(rowId) {
  if (! this.options.checkbox) return;
  
  var tr = this.extTable.query('tr[id=' + rowId + ']').first();
  var input = tr.firstChild.firstChild;
  input.checked = true;
};


Table.prototype.unSelectRow = function(rowId) {
  if (! this.options.checkbox) return;
  var tr = this.extTable.query('tr[id=' + rowId + ']').first();
  var input = tr.firstChild.firstChild;
  input.checked = false;
};


Table.prototype.filterBy = function(columnName, value) {
  for (var i=0; i < this.rows.length; i++) {
	var row = this.rows[i];  
	var el = null;
    if (row[columnName] == value) {
      el = this.extTable.query('tr[id=' + row.id + ']').first();
      if (el) $(el).show();           
    } else {
      el = this.extTable.query('tr[id=' + row.id + ']').first();      
      if (el) $(el).hide();
    }	  
  }    	
};


Table.prototype.showAll = function() {
  for (var i=0; i < this.rows.length; i++) {
	var row = this.rows[i];  	
    el = this.extTable.query('tr[id=' + row.id + ']').first();
    if (el) $(el).show();           
  }    	  
};


Table.prototype.openRow = function(rowId) {
  for (var i=0; i < this.rows.length; i++) {
	var row = this.rows[i];  	
    el = this.extTable.query('tr[id=' + row.id + ']').first();
    if (el) $(el).removeClassName('selected');           
  }    	    	
  
  var el = this.extTable.query('tr[id=' + rowId + ']').first();
  if (el) $(el).addClassName('selected');
};


Table.prototype._bind = function() {
  this.extParent = Ext.get(this.element);
  var action      = this.options.action || null;
  var conditional = this.options.conditional || null;
  var checkbox    = this.options.checkbox || null;
  var showHeader  = this.options.showHeader || null;
  var clickable   = this.options.clickable || null;
  var empty_message = this.options.empty_message || "";
  
  var html	= _.template(this.html, {'rows': this.rows, 'columns': this.columns, 'id': this.id, 'checkbox': checkbox, 'clickable': clickable, 'action': action, 'conditional': conditional,'showHeader': showHeader,  empty_message: empty_message});
  this.extParent.dom.innerHTML = html;
  
  this.extTable = Ext.get(this.extParent.query('table').first());  
  this.extTable.on('click', this._clickHandler, this);
  
  this.extTable.on('mouseover', this._rowMouseOverHandler, this);
  this.extTable.on('mouseout', this._rowMouseOutHandler, this);
  
  if ((this.listeners[Table.MOUSE_ENTER]) || (this.listeners[Table.MOUSE_LEAVE])) {
    this.extTable.on('mouseover', this._mouseEnterHandler, this);  
    this.extParent.on('mouseout', this._mouseLeaveHandler, this);
  }
};
  

Table.prototype._rowMouseOverHandler = function(event, target, options) {	
  event.preventDefault();
  event.stopPropagation();
  if (event.target.nodeName.toLowerCase() != 'td' && 
	  event.target.nodeName.toLowerCase() != 'span' &&
	  event.target.nodeName.toLowerCase() != 'a') {
	  return;
  }   
	
  var scope = this;
  this.browsingFlag = true;
  if (! this.listeners[Table.ROW_MOUSE_OVER]) return;
	
  setTimeout(function(scope, arguments) { 
      if (scope.browsingFlag) scope._fireRowMouseOverEvent(arguments[0], arguments[1], arguments[2]); 
  }, Table.MOUSE_EVENTS_TIME, scope, arguments);   
};


Table.prototype._rowMouseOutHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  
  if (event.target.nodeName.toLowerCase() != 'td' && 
    event.target.nodeName.toLowerCase() != 'span' &&
    event.target.nodeName.toLowerCase() != 'a') {
    return;
  }  
  
  var scope = this;
  this.browsingFlag = false;
  if (! this.listeners[Table.ROW_MOUSE_OUT]) return;	
		
  setTimeout(function(scope, arguments) {
    if (! scope.browsingFlag) scope._fireRowMouseOutEvent(arguments[0], arguments[1], arguments[2]); 	
  }, Table.MOUSE_EVENTS_TIME, scope, arguments);  					
};



Table.prototype._isChildOf = function(parent, child) {
  if (parent === child)  return false;
  
   while (child && child !== parent) { 
     child = child.parentNode; 
   }
   return child === parent;
};


Table.prototype._mouseEnterHandler = function(event, target, options) {	  
  if (! this.listeners[Table.MOUSE_ENTER]) return;
  this.browsingFlag = true;
  
  if (this.extTable.dom === target || this._isChildOf(this.extTable, target)) {  
	this.enterFlag = true;
	this.browsingFlag = true;  
	this._fireMouseEnterEvent(event, target, options);
  }
};


Table.prototype._mouseLeaveHandler = function(event, target, options) {
		  
  if (! this.listeners[Table.MOUSE_LEAVE]) return;
  
  if (this.enterFlag && this.extTable.dom != target && ! this._isChildOf(this.extTable.dom, target)) { 
    this.enterFlag = false;

	var scope = this;  
	setTimeout(function(scope, arguments) {
		if (! scope.enterFlag) scope._fireMouseLeaveEvent(arguments[0], arguments[1], arguments[2]);       	
	}, Table.MOUSE_EVENTS_TIME, scope, arguments);    
  }
};


Table.prototype._fireRowMouseOverEvent = function(event, target, options) {	  
  var trElement = $(event.target).up('tr');
  if (!trElement) return;
  
  var row = this._getRow(trElement);
  var e = {name: Table.ROW_MOUSE_OVER, data: {row: row}};      	 
  this.fire(e);      	
};


Table.prototype._fireRowMouseOutEvent = function(event, target, options) {    
  var e = {name: Table.ROW_MOUSE_OUT, data: null};      	 
  this.fire(e);
};


Table.prototype._fireMouseEnterEvent = function(event, target, options) {
  var e = {name: Table.MOUSE_ENTER, data: null};
  this.fire(e);
};


Table.prototype._fireMouseLeaveEvent = function(event, target, options) {
  var e = {name: Table.MOUSE_LEAVE, data: null};
  this.fire(e);	
};


Table.prototype._clickHandler = function(event, target, options) {
  var parentElement = target.parentElement || target.parentNode;
  
  switch (target.nodeName.toLowerCase()) {
    case 'a':      	
      event.stopPropagation();	
      event.preventDefault();
      
      var trElement = $(target).up('tr');
      var row = this._getRow(trElement);
      var e = null;
      
      if (parentElement.getAttribute("class") == 'action')
        e = {name: Table.CLICK_ROW_ACTION, data: {row: row, action: target.innerHTML}};
      else
    	e = {name: Table.CLICK_ROW, data: {row: row}};  
      this.fire(e);
    break;  
    
    case 'span':
      event.stopPropagation();	
      event.preventDefault();
      
      var trElement = parentElement.parentElement || parentElement.parentNode;
      var row = this._getRow(trElement);
      var e = {name: Table.CLICK_ROW, data: {row: row}};
      this.fire(e);
    break;
        
    case 'input':
      event.stopPropagation();
            
      var type = target.getAttribute('type');
      if (type != 'checkbox') return;
      
      var trElement = parentElement.parentElement || parentElement.parentNode;
      var row = this._getRow(trElement);      
      
      var e = {name: Table.CHECK_ROW, data: {row: row}};
      this.fire(e);      
    break;        
  }     	
};


Table.prototype._getRow = function(trElement) {
 var row = {};
 var extElement = Ext.get(trElement);
 
 row.id = trElement.getAttribute('id');
 
 if (this.options.checkbox) {
   var input = extElement.dom.firstChild.firstChild;
   row.checked = (input.checked)? true : false;
 }
 
 for (var i=0; i < this.columns.length; i++) {
   var columnName = this.columns[i];	  
   var element = extElement.query('*[name="' + columnName + '"]').first();   
   row[columnName] = element.innerText;
 }
 return row;
};






String.prototype.trim = function() {return this.replace(/^\s+|\s+$/g,'');};




function isUrl(url) {
  var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
  return regexp.test(url);
};





function addInput(DOMForm, sName, sValue) {
  var DOMInput = document.createElement("input");
  DOMInput.setAttribute("type", "hidden");
  DOMInput.setAttribute("name", sName);
  DOMInput.setAttribute("value", sValue);
  DOMForm.appendChild(DOMInput);
};





var URLEncoder = {
		 
  encode : function (string) {
	return escape(this._utf8_encode(string));
  },
	 
  decode : function (string) {
	return this._utf8_decode(unescape(string));
  },
	 
  _utf8_encode : function (string) {
	string = string.replace(/\r\n/g,"\n");
	var utftext = "";
	 
	for (var n = 0; n < string.length; n++) {
  	  var c = string.charCodeAt(n);
  	  if (c < 128) {
	    utftext += String.fromCharCode(c);
	  }
	  else if((c > 127) && (c < 2048)) {
        utftext += String.fromCharCode((c >> 6) | 192);
	    utftext += String.fromCharCode((c & 63) | 128);
	  }
	  else {
	    utftext += String.fromCharCode((c >> 12) | 224);
	    utftext += String.fromCharCode(((c >> 6) & 63) | 128);
		utftext += String.fromCharCode((c & 63) | 128);
	  }	 
    }
	 
    return utftext;
  },
	 
  _utf8_decode : function (utftext) {
	var string = "";
	var i = 0;
	var c = c1 = c2 = 0;
	 
	while ( i < utftext.length ) {
  	  c = utftext.charCodeAt(i);
	 
	  if (c < 128) {
	    string += String.fromCharCode(c);
		i++;
	  }
	  else if((c > 191) && (c < 224)) {
	    c2 = utftext.charCodeAt(i+1);
		string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
		i += 2;
	  }
	  else {
	    c2 = utftext.charCodeAt(i+1);
		c3 = utftext.charCodeAt(i+2);
		string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
		i += 3;
	  }
	}
	
	return string;
  }	 
};





function isFunction(item) {
  return (typeof item == "function");
}


function isEmail(sEmail) {
  var arr;

  if(sEmail.length <= 0) return false;
  
  arr = sEmail.match("^(.+)@(.+)$");
  if(arr == null) return false;
  
  if(arr[1] != null ) {
    var regexp_user=/^\"?[\w-_\.]*\"?$/;
    if(arr[1].match(regexp_user) == null) return false;
  }

  if(arr[2] != null) {
    var regexp_domain=/^[\w-\.]*\.[A-Za-z]{2,4}$/;
    if(arr[2].match(regexp_domain) == null) {
      var regexp_ip =/^\[\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\]$/;
      if(arr[2].match(regexp_ip) == null) return false;
    }
    return true;
  }

  return false;
}


function isAlphanumeric(sContent) {
	var sDummy = sContent;

	for(var iPos=0; iPos<sDummy.length; iPos++) {
	  var iChar = sDummy.charAt(iPos).charCodeAt(0);
		if ((iChar > 47 && iChar < 58) || (iChar > 64 && iChar < 91) || (iChar > 96 && iChar < 123)) {}
		else {
		  return false;
		}
  }

  return true;
}




function translate(sText, Data, sPrefix) {
  if (!sText) return;
  if (!sPrefix) sPrefix = EMPTY;

  if (Context.Config) {
    Expression = new RegExp(TEMPLATE_SEPARATOR + Literals.ImagesPath + TEMPLATE_SEPARATOR, "g");
    sText = sText.replace(Expression, Context.Config.ImagesPath);
  }

  for (var index in Data) {
    if (Literals[index])
      var sName = sPrefix + Literals[index];
    else 
      var sName = sPrefix + index;

    var Aux = Data[index];
    if (typeof Aux == "object") {
      sText = translate(sText, Aux, sName + DOT);
    }
    else  {
      Expression = new RegExp(TEMPLATE_SEPARATOR + sName + TEMPLATE_SEPARATOR, "g");
      sText = sText.replace(Expression, Aux);
    }
  }
  return sText;
}



function parseServerDate(dtDate) {
  return Date.parseDate(dtDate, SERVER_DATE_TIME_FORMAT);
}



function getFormattedDate(dtDate, Language) {

  Date.dayNames = eval("aDays." + Language);
  Date.monthNames = eval("aMonths." + Language);

  switch (Language) {
    case "es" : sFormat = "G:i:s \\e\\l l, j \\d\\e F \\d\\e Y"; break;
    default : sFormat = "G:i:s. l, F d, Y";
  }

  return dtDate.format(sFormat);
}



function utf8Encode(sData) {
  return unescape(encodeURIComponent(sData));
}



function utf8Decode(sData) {
  return decodeURIComponent(escape(sData));
}




function readData(Data, extData) {
  var extDataList = extData.select(HTML_DIV);
  extDataList.each(function(extData) {
    eval("Data." + extData.dom.id + "= '" + extData.dom.innerHTML + "';");
  }, this);
}


function getFileExtension(sFilename) {
  var iPos = sFilename.lastIndexOf('.');
  if (iPos == -1) return "";
  return sFilename.substring(iPos+1);
}

function RequestException (conn,response,options){
  var extDialog, extTemplate;
  
  if (Ext.MessageBox.isVisible()) Ext.MessageBox.hide();
  
  extDialog = new Ext.BasicDialog(Literals.Dialogs.Exception,{width:500,height:400,resizable:false});
  extTemplate = new Ext.Template(Lang.Exceptions.Request.Description + 'url: {url}<br/>' + 'params: {params}<br/>' +'status: {status}<br/>' + 'response: {response}');
                            
  extDialog.body.dom.innerHTML = extTemplate.applyTemplate({url:options.url ? options.url : Ext.Ajax.url, params:options.params, status:response.status + " " + response.statusText, response:response.responseText});

  extDialog.setTitle(Lang.Exceptions.Request.Title);
  extDialog.addButton(Lang.Buttons.Close, CloseException.bind(this, extDialog));
  extDialog.center();
  extDialog.show();
}

function InternalException (e,dOp,dState){
  var extDialog, extTemplate;

  if (Ext.MessageBox.isVisible()) Ext.MessageBox.hide();
    
  extDialog = new Ext.BasicDialog(Literals.Dialogs.Exception,{width:500,height:400,resizable:false});
  extTemplate = new Ext.Template(Lang.Exceptions.Internal.Description + 'operation: {op}<br/>' +'state: {state}<br/>' + 'name: {name}<br/>' + 'message: {message}<br/>');
    
  extDialog.body.dom.innerHTML = extTemplate.applyTemplate({op: dOp, state: dState, name: e.name, message: e.message});
    
  extDialog.setTitle(Lang.Exceptions.Internal.Title);
  extDialog.addButton(Lang.Buttons.Close, CloseException.bind(this, extDialog));
  extDialog.center();
  extDialog.show();
}

function CloseException(extDialog) {
  if (! extDialog) return;
  extDialog.hide();
  extDialog.destroy();
}

function printStackTrace(e) {
	var callstack = [];
  var isCallstackPopulated = false;
  try {
    i.dont.exist+=0; 
  } catch(e) {
    if (e.stack) { 
      var lines = e.stack.split('\n');
      for (var i=0, len=lines.length; i<len; i++) {
        if (lines[i].match(/^\s*[A-Za-z0-9\-_\$]+\(/)) {
          callstack.push(lines[i]);
        }
      }
      
      callstack.shift();
      isCallstackPopulated = true;
    }
    else if (window.opera && e.message) { 
      var lines = e.message.split('\n');
      for (var i=0, len=lines.length; i < len; i++) {
        if (lines[i].match(/^\s*[A-Za-z0-9\-_\$]+\(/)) {
          var entry = lines[i];
          
          if (lines[i+1]) {
            entry += ' at ' + lines[i+1];
            i++;
          }
          callstack.push(entry);
        }
      }
      
      callstack.shift();
      isCallstackPopulated = true;
    }
  }
  if (!isCallstackPopulated) { 
    var currentFunction = arguments.callee.caller;
    while (currentFunction) {
      var fn = currentFunction.toString();
      var fname = fn.substring(fn.indexOf('function') + 8, fn.indexOf('')) || 'anonymous';
      callstack.push(fname);
      currentFunction = currentFunction.caller;
    }
  }
  output(callstack);
}
 
function output(arr) {
  console.log(arr.join('\n\n'));	
}




window.dhtmlHistory = {
	
	
	isIE: false,
	isOpera: false,
	isSafari: false,
	isKonquerer: false,
	isGecko: false,
	isSupported: false,
	
	
	create: function(options) {
		
		

		var that = this;

		
		var UA = navigator.userAgent.toLowerCase();
		var platform = navigator.platform.toLowerCase();
		var vendor = navigator.vendor || "";
		if (vendor === "KDE") {
			this.isKonqueror = true;
			this.isSupported = false;
		} else if (typeof window.opera !== "undefined") {
			this.isOpera = true;
			this.isSupported = true;
		} else if (typeof document.all !== "undefined") {
			this.isIE = true;
			this.isSupported = true;
		} else if (vendor.indexOf("Apple Computer, Inc.") > -1) {
			this.isSafari = true;
			this.isSupported = (platform.indexOf("mac") > -1);
		} else if (UA.indexOf("gecko") != -1) {
			this.isGecko = true;
			this.isSupported = true;
		}

		
		window.historyStorage.setup(options);

		
		if (this.isSafari) {
			this.createSafari();
		} else if (this.isOpera) {
			this.createOpera();
		}
		
		
		var initialHash = this.getCurrentLocation();

		
		this.currentLocation = initialHash;

		
		if (this.isIE) {
			this.createIE(initialHash);
		}

		

		var unloadHandler = function() {
			that.firstLoad = null;
		};
		
		this.addEventListener(window,'unload',unloadHandler);		

		
		if (this.isIE) {
			
			this.ignoreLocationChange = true;
		} else {
			if (!historyStorage.hasKey(this.PAGELOADEDSTRING)) {
				
				this.ignoreLocationChange = true;
				this.firstLoad = true;
				historyStorage.put(this.PAGELOADEDSTRING, true);
			} else {
				
				this.ignoreLocationChange = false;
				
				this.fireOnNewListener = true;
			}
		}

		
		var locationHandler = function() {
			that.checkLocation();
		};
		setInterval(locationHandler, 100);
	},	
	
	
	initialize: function() {
		
		if (this.isIE) {
			
			if (!historyStorage.hasKey(this.PAGELOADEDSTRING)) {
				
				this.fireOnNewListener = false;
				this.firstLoad = true;
				historyStorage.put(this.PAGELOADEDSTRING, true);
			}
			
			else {
				this.fireOnNewListener = true;
				this.firstLoad = false;   
			}
		}
	},

	
	addListener: function(listener) {
		this.listener = listener;
		
		if (this.fireOnNewListener) {
			this.fireHistoryEvent(this.currentLocation);
			this.fireOnNewListener = false;
		}
	},
	
	
	addEventListener: function(o,e,l) {
		if (o.addEventListener) {
			o.addEventListener(e,l,false);
		} else if (o.attachEvent) {
			o.attachEvent('on'+e,function() {
				l(window.event);
			});
		}
	},
	
	
	add: function(newLocation, historyData) {
		
		if (this.isSafari) {
			
			
			newLocation = this.removeHash(newLocation);

			
			historyStorage.put(newLocation, historyData);

			
			this.currentLocation = newLocation;
	
			
			window.location.hash = newLocation;
		
			
			this.putSafariState(newLocation);

		} else {
			
			
			var that = this;
			var addImpl = function() {

				
				if (that.currentWaitTime > 0) {
					that.currentWaitTime = that.currentWaitTime - that.waitTime;
				}
			
				
				newLocation = that.removeHash(newLocation);

				
				if (document.getElementById(newLocation) && that.debugMode) {
					var e = "Exception: History locations can not have the same value as _any_ IDs that might be in the document,"
					+ " due to a bug in IE; please ask the developer to choose a history location that does not match any HTML"
					+ " IDs in this document. The following ID is already taken and cannot be a location: " + newLocation;
					throw new Error(e); 
				}

				
				historyStorage.put(newLocation, historyData);

				
				that.ignoreLocationChange = true;

				
				that.ieAtomicLocationChange = true;

				
				that.currentLocation = newLocation;
		
				
				window.location.hash = newLocation;

				
				if (that.isIE) {
					that.iframe.src = "blank.html?" + newLocation;
				}

				
				that.ieAtomicLocationChange = false;
			};

			
			window.setTimeout(addImpl, this.currentWaitTime);

			
			this.currentWaitTime = this.currentWaitTime + this.waitTime;
		}
	},

	
	isFirstLoad: function() {
		return this.firstLoad;
	},

	
	getVersion: function() {
		return "0.6";
	},

	

	
	getCurrentLocation: function() {
		var r = (this.isSafari
			? this.getSafariState()
			: this.getCurrentHash()
		);
		return r;
	},
	
	
    getCurrentHash: function() {
		var r = window.location.href;
		var i = r.indexOf("#");
		return (i >= 0
			? r.substr(i+1)
			: ""
		);
    },
	
	
	
	
	PAGELOADEDSTRING: "DhtmlHistory_pageLoaded",
	
	
	listener: null,

	
	waitTime: 200,
	
	
	currentWaitTime: 0,

	
	currentLocation: null,

	
	iframe: null,

	
	safariHistoryStartPoint: null,
	safariStack: null,
	safariLength: null,

	
	ignoreLocationChange: null,

	
	fireOnNewListener: null,

	
	firstLoad: null,

	
	ieAtomicLocationChange: null,
	
	
	createIE: function(initialHash) {
		
		this.waitTime = 400;
		var styles = (historyStorage.debugMode
			? 'width: 800px;height:80px;border:1px solid black;'
			: historyStorage.hideStyles
		);
		var iframeID = "rshHistoryFrame";
		var iframeHTML = '<iframe frameborder="0" id="' + iframeID + '" style="' + styles + '" src="blank.html?' + initialHash + '"></iframe>';
		document.write(iframeHTML);
		this.iframe = document.getElementById(iframeID);
	},
	
	
	createOpera: function() {
		this.waitTime = 400;
		var imgHTML = '<img src="javascript:location.href=\'javascript:dhtmlHistory.checkLocation();\';" style="' + historyStorage.hideStyles + '" />';
		document.write(imgHTML);
	},
	
	
	createSafari: function() {
		var formID = "rshSafariForm";
		var stackID = "rshSafariStack";
		var lengthID = "rshSafariLength";
		var formStyles = historyStorage.debugMode ? historyStorage.showStyles : historyStorage.hideStyles;
		var inputStyles = (historyStorage.debugMode
			? 'width:800px;height:20px;border:1px solid black;margin:0;padding:0;'
			: historyStorage.hideStyles
		);
		var safariHTML = '<form id="' + formID + '" style="' + formStyles + '">'
			+ '<input type="text" style="' + inputStyles + '" id="' + stackID + '" value="[]"/>'
			+ '<input type="text" style="' + inputStyles + '" id="' + lengthID + '" value=""/>'
		+ '</form>';
		document.write(safariHTML);
		this.safariStack = document.getElementById(stackID);
		this.safariLength = document.getElementById(lengthID);
		if (!historyStorage.hasKey(this.PAGELOADEDSTRING)) {
			this.safariHistoryStartPoint = history.length;
			this.safariLength.value = this.safariHistoryStartPoint;
		} else {
			this.safariHistoryStartPoint = this.safariLength.value;
		}
	},
	
	
	getSafariStack: function() {
		var r = this.safariStack.value;
		return historyStorage.fromJSON(r);
	},

	
	getSafariState: function() {
		var stack = this.getSafariStack();
		var state = stack[history.length - this.safariHistoryStartPoint - 1];
		return state;
	},			
	
	putSafariState: function(newLocation) {
	    var stack = this.getSafariStack();
	    stack[history.length - this.safariHistoryStartPoint] = newLocation;
	    this.safariStack.value = historyStorage.toJSON(stack);
	},

	
	fireHistoryEvent: function(newHash) {
		
		var historyData = historyStorage.get(newHash);
		
		this.listener.call(null, newHash, historyData);
	},
	
	
	checkLocation: function() {
		
		
		if (!this.isIE && this.ignoreLocationChange) {
			this.ignoreLocationChange = false;
			return;
		}

		
		if (!this.isIE && this.ieAtomicLocationChange) {
			return;
		}
		
		
		var hash = this.getCurrentLocation();

		
		if (hash == this.currentLocation) {
			return;
		}

		
		this.ieAtomicLocationChange = true;

		if (this.isIE && this.getIframeHash() != hash) {
			this.iframe.src = "blank.html?" + hash;
		}
		else if (this.isIE) {
			
			return;
		}

		
		this.currentLocation = hash;

		this.ieAtomicLocationChange = false;

		
		this.fireHistoryEvent(hash);
	},

	
	getIframeHash: function() {
		var doc = this.iframe.contentWindow.document;
		var hash = String(doc.location.search);
		if (hash.length == 1 && hash.charAt(0) == "?") {
			hash = "";
		}
		else if (hash.length >= 2 && hash.charAt(0) == "?") {
			hash = hash.substring(1);
		}
		return hash;
	},

	
	removeHash: function(hashValue) {
		var r;
		if (hashValue === null || hashValue === undefined) {
			r = null;
		}
		else if (hashValue === "") {
			r = "";
		}
		else if (hashValue.length == 1 && hashValue.charAt(0) == "#") {
			r = "";
		}
		else if (hashValue.length > 1 && hashValue.charAt(0) == "#") {
			r = hashValue.substring(1);
		}
		else {
			r = hashValue;
		}
		return r;
	},

	
	iframeLoaded: function(newLocation) {
		
		if (this.ignoreLocationChange) {
			this.ignoreLocationChange = false;
			return;
		}

		
		var hash = String(newLocation.search);
		if (hash.length == 1 && hash.charAt(0) == "?") {
			hash = "";
		}
		else if (hash.length >= 2 && hash.charAt(0) == "?") {
			hash = hash.substring(1);
		}
		
		window.location.hash = hash;

		
		this.fireHistoryEvent(hash);
	}

};


window.historyStorage = {
	
	
	setup: function(options) {
		
		
		
		
		if (typeof options !== "undefined") {
			if (options.debugMode) {
				this.debugMode = options.debugMode;
			}
			if (options.toJSON) {
				this.toJSON = options.toJSON;
			}
			if (options.fromJSON) {
				this.fromJSON = options.fromJSON;
			}
		}		
		
		
		var formID = "rshStorageForm";
		var textareaID = "rshStorageField";
		var formStyles = this.debugMode ? historyStorage.showStyles : historyStorage.hideStyles;
		var textareaStyles = (historyStorage.debugMode
			? 'width: 800px;height:80px;border:1px solid black;'
			: historyStorage.hideStyles
		);
		var textareaHTML = '<form id="' + formID + '" style="' + formStyles + '">'
			+ '<textarea id="' + textareaID + '" style="' + textareaStyles + '"></textarea>'
		+ '</form>';
		document.write(textareaHTML);
		this.storageField = document.getElementById(textareaID);
		if (typeof window.opera !== "undefined") {
			this.storageField.focus();
		}
	},
	
	
	put: function(key, value) {
		this.assertValidKey(key);
		
		if (this.hasKey(key)) {
			this.remove(key);
		}
		
		this.storageHash[key] = value;
		
		this.saveHashTable();
	},

	
	get: function(key) {
		this.assertValidKey(key);
		
		this.loadHashTable();
		var value = this.storageHash[key];
		if (value === undefined) {
			value = null;
		}
		return value;
	},

	
	remove: function(key) {
		this.assertValidKey(key);
		
		this.loadHashTable();
		
		delete this.storageHash[key];
		
		this.saveHashTable();
	},

	
	reset: function() {
		this.storageField.value = "";
		this.storageHash = {};
	},

	
	hasKey: function(key) {
		this.assertValidKey(key);
		
		this.loadHashTable();
		return (typeof this.storageHash[key] !== "undefined");
	},

	
	isValidKey: function(key) {
		return (typeof key === "string");
	},
	
	
	showStyles: 'border:0;margin:0;padding:0;',
	hideStyles: 'left:-1000px;top:-1000px;width:1px;height:1px;border:0;position:absolute;',
	
	
	debugMode: false,
	
	

	
	storageHash: {},

	
	hashLoaded: false, 

	
	storageField: null,

	
	assertValidKey: function(key) {
		var isValid = this.isValidKey(key);
		if (!isValid && this.debugMode) {
			throw new Error("Please provide a valid key for window.historyStorage. Invalid key = " + key + ".");
		}
	},

	
	loadHashTable: function() {
		if (!this.hashLoaded) {	
			var serializedHashTable = this.storageField.value;
			if (serializedHashTable !== "" && serializedHashTable !== null) {
				this.storageHash = this.fromJSON(serializedHashTable);
				this.hashLoaded = true;
			}
		}
	},
	
	saveHashTable: function() {
		this.loadHashTable();
		var serializedHashTable = this.toJSON(this.storageHash);
		this.storageField.value = serializedHashTable;
	},
	
	toJSON: function(o) {
		return o.toJSONString();
	},
	fromJSON: function(s) {
		return s.parseJSON();
	}
};

var ValidationErrors = {
  Required : 'required'		
};

Validators = {};


Validators.Required = {	
  propertyName : '',
  
  validate : function(propertyName, value) {
	this.propertyName = propertyName;
		
	if (value == undefined || value == "" || value == null) return false;
    return true;
  },
  getError : function() {
	  return {propertyName: this.propertyName,  message: ValidationErrors.Required};
  }
};

var Pooler = EventsSource.extend({
  init : function(config) {
    this._super();
    this.config   = config;
    this.poolTime = this.config.poolTime || 1000;
    this.refreshTime = 1000;
    this.poolInterval = null;
    this.state = 'stopped';
    
    this.startTime = 0;
    this.completeTime = 0;
  }
});  
  
Pooler.ON_REQUEST  = 'on_request';
Pooler.ON_COMPLETE = 'on_complete';


Pooler.prototype.start = function(callback) {
  this.state = 'running';
  var context = this;
  
  var request = new Request({
    url    : this.config.url,
    params : this.config.params,
    method : 'get',

    callback : {
      context : this,
      success : function() { 
        context.startTime = new Date().getTime();
        context.fire({name : Pooler.ON_COMPLETE});
        
        var response = arguments[0];
        if (this.config.serializer) reponse = this.config.serializer.unserialize(arguments[0]);                    
        callback.success.call(callback.context, response); 
      },
        
      failure: function()  {
        context.stop();
        context.fire({name : Pooler.ON_COMPLETE});      
        callback.failure.apply(callback.context, arguments);       
      }
    }
  });
    
  this.poolInterval = window.setInterval(function() {
    context.completeTime = new Date().getTime();
    if (context.completeTime - context.startTime > context.refreshTime) context.fire({name : Pooler.ON_REQUEST});
    setTimeout(function() {context.fire({name : Pooler.ON_REQUEST}); }, context.refreshTime);
    request.send();
  }, this.poolTime);  
};


Pooler.prototype.stop = function(callback) {
  window.clearInterval(this.poolInterval);  
  this.state = 'stopped';
};


Pooler.prototype.isRunning = function() {
  return this.state = 'running';
};






var TasksQueQue = EventsSource.extend({
  init : function() {
    this._super();
    this._tasks = [];
    this._results = [];
    this.iter = 0;
  }
});

TasksQueQue.COMPLETE_EVENT = 'complete_event';


TasksQueQue.prototype.add = function(fn, scope) {
  this._tasks.push(new Task(fn, scope));
  return this;
};


TasksQueQue.prototype.clear = function() {
  this._tasks = [];
  this._results = [];
  return this;
};


TasksQueQue.prototype.execute = function() {  
  var task = this._tasks[this.iter++];    
  if (!task) return ;
  
  task.on(Task.FINISH_EVENT, {notify : function(event) { 
    var result = event.data.result;
    this._results.push(result);
    
    if (this._tasks.length == this._results.length) this._fireCompleteEvent();
    else this.execute();
  }}, this);
    
  task.execute(); 
};


TasksQueQue.prototype._fireCompleteEvent = function() {
  var event =  {name : TasksQueQue.COMPLETE_EVENT, data : {results : this._results}};
  this.fire(event);
};







var Task = EventsSource.extend({
  init : function(fn, scope) {
    this._super();
    this._fn = fn;    
    this._scope = scope;
  }
});

Task.FINISH_EVENT = 'finish_event';


Task.prototype.execute = function() {
  var self = this;
  var callback = {};
  
  callback.end = function(result) {
    var event =  {name : Task.FINISH_EVENT, data : {result : result}};
   self.fire(event);    
  };

  this._fn.call(this._scope, callback);          
};

var Context = {};
Context.Config = {};
Context.Config.Layer = {};
Context.Pages = {};


dhtmlHistory.create({
  toJSON: function(o) {
    var result = Ext.util.JSON.encode(o);
    return result;
  }, 
  fromJSON: function(s) {
    var result = Ext.util.JSON.decode(s);
    return result;
  }
});



var Application = new Object;
Application.listeners = [];

Application.init = function() {
  if (!console)  console = { log : function(){} };
 
  readData(Context, Ext.get(Literals.Data));
  
  var loggingUrl = Context.Config.Api + "?op=savelog";
  var responder = function(caller, ex) { 
    DefaultExceptionHandler.handleError(ex);
    if (ex.type == Literals.SERVER_ERROR) Ajax.Responders.unregister(this); 
  };
  
  Ajax.Responders.register({ onException : responder });
  
  DefaultExceptionHandler.init(Literals.Dialogs.Exception, loggingUrl);
  
  Desktop.init();
  Controller.init();
};

Ext.onReady(
  function() {
    Ext.QuickTips.init();
    Ext.enableListenerCollection = true;
    
    try {    	           
      Application.init();
       
    } catch (ex) {
      console.log(ex);
      ErrorHandler.handleError(ex);            
    }
  }
);

