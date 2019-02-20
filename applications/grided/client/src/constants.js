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

//----------------------------------------------------------------------------------
// Domain constants
//----------------------------------------------------------------------------------

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

