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

SERVER_DATE_TIME_FORMAT = "Y-m-d";
SERVER_ERROR_PREFIX = "err_";
SERVER_SESSION_EXPIRES = "err_session_expires";
SERVER_USER_NOT_LOGGED = "err_user_not_logged";
SERVER_SERVICE_STOPPED = "err_service_stopped";

OPTION_YES = "yes";
OPTION_NO = "no";
OPTION_OK = "ok";

BUTTON_RESULT_YES = OPTION_YES;
BUTTON_RESULT_OK = OPTION_OK;
BUTTON_RESULT_NO  = OPTION_NO;

ACCOUNT_TYPE_BACK = "back";
ACCOUNT_TYPE_FRONT = "front";
ACCOUNT_TYPE_MODELER = "modeler";

THESAURUS_TYPE_INTERNAL = "internal";
THESAURUS_TYPE_EXTERNAL = "external";

THESAURUS_RECURRENCY_NEVER = "never";
THESAURUS_RECURRENCY_DAILY = "daily";
THESAURUS_RECURRENCY_WEEKLY = "weekly";
THESAURUS_RECURRENCY_MONTHLY = "monthly";
THESAURUS_RECURRENCY_YEARLY = "yearly";

DEFAULT_LANGUAGE = "es";

var Literals = new Object;
Literals = {
  Data: "DataInit",
  Desktop: "Desktop",
  ViewsContainer: "ViewsContainer",
  Loading: "Loading",
  LoadingMask: "LoadingMask",
  LoadingIndicator: "LoadingIndicator",
  ReportContainer: "ReportContainer",
  ThesaurusGrid: "ThesaurusGrid",
  ThesaurusToolbar : "ThesaurusToolbar",
          
  AppLogo: "AppLogo",

  TabPanel : {
	  Name : "Tabs",
    Tabs : {
      BusinessUnit : "BusinessUnitTab",
      EventLogViewer : "EventLogViewerTab"
    }
  },

  Frames : {
	  History : "frameHistory"
  },

  StyleRules : {
	  Command : "command",
	  ChangeCommand : "changecommand"
  },

  ActionMessage : {
    Loading : "ActionLoading",
    Done : "ActionDone"
  },

  Toolbars : {
    EventLogGrid : "ToolbarEventLogGrid",
    ServiceLinkList : "ToolbarServiceLinkList",    
    ThesaurusList : "ToolbarThesaurusList",
    AccountList : "ToolbarAccountList",
    Account : "ToolbarAccount"
  },

  Views : {	  	  
	  BusinessModel : "ViewBusinessModel",	      
      BusinessSpace : "ViewBusinessSpace",
      Thesaurus : "ViewThesaurus",
      Logs : "ViewLogs"	  
  },
  
  Tabs : {
     Model : "ModelTab",
     Space : "SpaceTab",     
     Thesaurus : "ThesaurusTab",
     Providers : "ProvidersTab",
     Console : "ConsoleTab",
  },

  Dialogs : {	  
    UpdateBusinessModel : "dialog-update-model",
    UpdateBusinessSpace: "dialog-update-space",
    UpdateSpaceWizard : "wizard-dialog-update-space",
    Confirm : "dialog-modal",
    Message : "dialog-message",
    Modal : "dialog-modal",
    Exception: "dialog-exception"	
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