<?php
   error_reporting(E_ALL ^ E_NOTICE);

  define("PATH", "path");
  define("FILES", "files");
  define("_PROTECTED", "protected");
  define("SINGLE", "");
  define("TAG_LANGUAGE", "<language>");

  $ListLanguages["es"] = "spanish";
  $ListLanguages["en"] = "english";

  $Main[PATH] = "../src/";
  $Main[FILES] = array(
    "constants.js" => SINGLE,
    "application.js" => array("constants.js", "base.js", "desktop.js", "controller.js", "errorshandler.js", "gridedservice.js", "editors.js", "components.js", "common.lib.js", "exception.lib.js", "rsh.js", "validation.lib.js", "pooler.js", "tasksqueque.js")
  );

//---------------------------------------------------------------
  $Control[PATH] = "../src/control/";
  $Control[FILES] = array(
     "history.js" => SINGLE,
     "request.js" => SINGLE,
     "router.js" => SINGLE,
     "eventbus.js" => SINGLE,
     "controller.js" => array("history.js", "eventbus.js", "router.js", "locationparser.js", "places.js", "handlers.js", "events.js", "notificationmanager.js", "activitymanager.js", "activitymapper.js", "activity.js", "servers.activity.js", "server.activity.js")    
  );  

  //---------------------------------------------------------------
  $ControlCommands[PATH] = "../src/control/commands/";
  $ControlCommands[FILES] = array(
    "commandfactory.js" => SINGLE,
    "commanddispatcher.js" => SINGLE,
    "commandlistener.js" => array("commanddispatcher.js"),
    "process.js" => SINGLE,
    "action.js" => array("commandfactory.js", "process.js"),        
    "request.js" => SINGLE    	
  );

  //---------------------------------------------------------------
  $ControlNotifications[PATH] = "../src/control/notifications/";
  $ControlNotifications[FILES] = array(
    "notificationmanager.js" => array("notifications.lang.js")            
  );
  
  //---------------------------------------------------------------
  $ControlActivities[PATH] = "../src/control/activities/";
  $ControlActivities[FILES] = array(
    "activitymanager.js" => array("activity.js"),
    "activity.js" => array("listener.js", "savechanges.dialog.js"),
    "home.activity.js" => array("activity.js"),
    "servers.activity.js" => array("activity.js", "servers.view.js", "servers.js"),
    "server.activity.js" => array("activity.js", "server.view.js"),
    "deployment.activity.js" => array("activity.js", "deployment.view.js"),
    "federation.activity.js" => array("activity.js", "federation.view.js"),
    "space.activity.js" => array("activity.js", "space.view.js"),
    "models.activity.js" => array("activity.js", "models.view.js", "model.view.js"),
    "model.activity.js" => array("activity.js", "model.view.js"),    
    "activitymapper.js" => array("home.activity.js", "servers.activity.js", "server.activity.js", "deployment.activity.js", "federation.activity.js", "space.activity.js", "models.activity.js", "model.activity.js")
  );
  
  //---------------------------------------------------------------
  $ControlPlaces[PATH] = "../src/control/places/";
  $ControlPlaces[FILES] = array(    
    "places.js" => SINGLE,       
    "handlers.js" => array("history.js"),
    "locationparser.js" => SINGLE,
  );      
 
  //---------------------------------------------------------------
  $Core[PATH] = "../src/core/";
  $Core[FILES] = array(
    "gridedservice.js" => array("request.js", "serializers.js") 
  );
  
  //---------------------------------------------------------------
  $CoreModel[PATH] = "../src/core/model/";
  $CoreModel[FILES] = array(
    "model.js" => array("eventssource.js"),    
    "collection.js" => array("model.js"),
    "server.js" => array("model.js"),
    "servers.js" => array("collection.js"),
    "federations.js" => array("collection.js"),
    "federation.js" => array("model.js", "state.js"),
    "spaces.js" => array("collection.js"),
    "space.js" => array("model.js", "state.js"),
    "businessmodel.js" => array("model.js"),
    "state.js" => array("model.js")
  );

  //---------------------------------------------------------------
  $CoreEvents[PATH] = "../src/core/events/";
  $CoreEvents[FILES] = array(
    "events.js" => SINGLE,    
    "eventssource.js" => SINGLE,
    "listener.js" => SINGLE
  );
  
  //---------------------------------------------------------------
  $CoreSerializers[PATH] = "../src/core/serializer/";
  $CoreSerializers[FILES] = array(
    "server.serializer.js" => array("server.js"),
    "serverstate.serializer.js" => array("server.js"),
    "servers.serializer.js" => array("servers.js"),
    "federation.serializer.js" => array("federation.js"),
    "federations.serializer.js" => array("federations.js"),
    "spaces.serializer.js" => array("spaces.js"),
    "space.serializer.js" => array("space.js", "services.serializer.js", "modelversion.serializer.js"),
    "services.serializer.js" => array("space.js"),
    "models.serializer.js" => array("businessmodel.js"),
    "model.serializer.js" => array("businessmodel.js", "modelversion.serializer.js"),    
    "modelversion.serializer.js" => array("businessmodel.js"),    
    "serializers.js" => array("server.serializer.js", "serverstate.serializer.js", "servers.serializer.js", "federations.serializer.js", "federation.serializer.js", "spaces.serializer.js", "space.serializer.js", "models.serializer.js", "model.serializer.js", "modelversion.serializer.js")    
  );  

  //---------------------------------------------------------------
  $CoreExceptions[PATH] = "../src/core/exceptions/";
  $CoreExceptions[FILES] = array(
    "errorshandler.js" => SINGLE
  );  

  //---------------------------------------------------------------
  $CoreKernel[PATH] = "../src/core/kernel/";
  $CoreKernel[FILES] = array();  

  //---------------------------------------------------------------
  $UI[PATH] = "../src/ui/";
  $UI[FILES] = array();

  //---------------------------------------------------------------
  $UIComponent[PATH] = "../src/ui/component/";
  $UIComponent[FILES] = array(       
    "listcomponent.js" => SINGLE,    
    "wizard.js" => SINGLE,
    "menu.js" => SINGLE,    
    "toolbar.js" => array("menu.js", "Toolbar.html"),        
    "table.js" => array("Table.html"),    
    "components.js" => array("listcomponent.js", "wizard.js", "menu.js", "toolbar.js", "table.js")      
  );

  //---------------------------------------------------------------
  $UIDialog[PATH] = "../src/ui/dialog/";
  $UIDialog[FILES] = array(
    "dialog.js" => array("eventssource.js"),
    "upload.dialog.js" => array("dialog.js", "UploadDialog.html", "dialogupload.lang.js"),
    "addserver.dialog.js" => array("dialog.js", "dialogaddserver.lang.js", "AddServerDialog.html"),
    "selectserver.dialog.js" => array("dialog.js", "dialogselectserver.lang.js", "SelectServerDialog.html"),        
    "addfederation.dialog.js" => array("dialog.js", "AddFederationDialog.html", "dialogaddfederation.lang.js"),
    "addspace.dialog.js" => array("dialog.js", "AddSpaceDialog.html", "dialogaddspace.lang.js"),    
    "importspacedata.dialog.js" => array("dialog.js", "ImportSpaceDataDialog.html", "dialogimportspacedata.lang.js"),
    "importspacedata.wizard.dialog.js" => array("dialog.js", "importspacedata.dialog.js", "importprogress.composite.js", "importresult.composite.js"),
    "addmodel.dialog.js" => array("dialog.js", "AddModelDialog.html", "dialogaddmodel.lang.js"),
    "savechanges.dialog.js" => array("dialog.js", "SaveChangesDialog.html", "dialogsavechanges.lang.js"),
    "uploadmodelversion.dialog.js" => array("dialog.js", "dialoguploadmodelversion.lang.js", "UploadModelVersionDialog.html")     
  );

  //---------------------------------------------------------------
  $UIEditor[PATH] = "../src/ui/editor/";
  $UIEditor[FILES] = array(
    "editors.js" => array("editor.js", "text.editor.js", "checkbox.editor.js", "select.editor.js", "image.editor.js", "list.editor.js"),
    "text.editor.js" => array("editor.js"),
    "checkbox.editor.js" => array("editor.js"),
    "select.editor.js" => array("editor.js"),
    "image.editor.js" => array("editor.js"),
    "list.editor.js" => array("editor.js"),
    "editor.js" => array("eventssource.js"),
    "server.editor.js" => array("editor.js", "server.js"),    
    "federation.editor.js" => array("editor.js", "federation.js"),
    "space.editor.js" => array("editor.js", "space.js"),
    "services.editor.js" => array("editor.js", "space.js"),
    "model.editor.js" => array("editor.js", "list.editor.js", "businessmodel.js")       
  );

  //---------------------------------------------------------------
  //$UILang[PATH] = "../src/ui/lang/<language>/";
  $UILang[PATH] = "../src/ui/lang/english/";  
  $UILang[FILES] = array(
    "error.lang.js" => array("common.lang.js"),
    "common.lang.js" => SINGLE,
    "desktop.lang.js" => SINGLE,
    "viewservers.lang.js" => SINGLE,
    "viewserver.lang.js" => SINGLE,    
    "viewspace.lang.js" => SINGLE,    
    "viewservices.lang.js" => SINGLE,
    "viewfederation.lang.js" => SINGLE,
    "viewdeployment.lang.js" => SINGLE,
    "viewmodels.lang.js" => SINGLE,
    "viewmodel.lang.js" => SINGLE,
    "viewmodelversion.lang.js" => SINGLE,
    "viewmodelversions.lang.js" => SINGLE,
    "viewimportspacedata.lang.js" => SINGLE,
        
    "compositeimportprogress.lang.js" => SINGLE,
    "compositeimportresult.lang.js" => SINGLE,
    "notifications.lang.js" => SINGLE,
           
    "dialogupload.lang.js" => SINGLE,
    "dialogsavechanges.lang.js" => SINGLE,
    "dialogaddserver.lang.js" => SINGLE,
    "dialogselectserver.lang.js" => SINGLE,
    "dialogaddfederation.lang.js" => SINGLE,
    "dialogaddspace.lang.js" => SINGLE,
    "dialogimportspacedata.lang.js" => SINGLE,
    "dialogaddmodel.lang.js" => SINGLE,
    "dialoguploadmodelversion.lang.js" => SINGLE        
  );

  //---------------------------------------------------------------
  $UILayout[PATH] = "../src/ui/layout/";
  $UILayout[FILES] = array(
    "desktop.js" => array("headerpanel.js", "contentpanel.js", "Desktop.html", "error.lang.js"),
    "headerpanel.js" => array("toolbar.js", "desktop.lang.js"),
    "contentpanel.js" => array("activity.js", "activitymanager.js", "servers.activity.js"),            
  );

  //---------------------------------------------------------------
  $UIView[PATH] = "../src/ui/view/";
  $UIView[FILES] = array(
    "view.js" => array("listener.js"),
    "servers.view.js" => array("view.js", "server.js", "viewservers.lang.js", "ServersView.html", "viewservers.lang.js", "addserver.dialog.js"),
    "server.view.js"  => array("view.js", "server.js", "ServerView.html", "viewserver.lang.js", "server.editor.js"),
    "deployment.view.js" => array("view.js", "federations.js", "spaces.js", "viewdeployment.lang.js", "selectserver.dialog.js", "addfederation.dialog.js", "addspace.dialog.js", "DeploymentView.html"),
    "federation.view.js" => array("view.js", "federation.js", "federation.editor.js", "FederationView.html", "viewfederation.lang.js", "upload.dialog.js"),
    "space.view.js" => array("view.js", "space.js", "viewspace.lang.js", "services.view.js", "SpaceView.html", "upload.dialog.js", "space.editor.js", "importspacedata.view.js"),
    "importspacedata.view.js" => array("view.js", "ImportSpaceDataView.html", "importspacedata.wizard.dialog.js", "viewimportspacedata.lang.js"),            
    "importprogress.composite.js" => array("view.js", "ImportProgressComposite.html", "compositeimportprogress.lang.js"),
    "importresult.composite.js" => array("view.js", "ImportResultComposite.html", "compositeimportresult.lang.js"),        
    "services.view.js" => array("view.js", "space.js", "viewservices.lang.js", "services.editor.js", "ServicesView.html"),
    "models.view.js" => array("view.js", "businessmodel.js", "viewmodels.lang.js", "ModelsView.html", "addmodel.dialog.js"),
    "model.view.js" => array("view.js", "businessmodel.js", "viewmodel.lang.js", "model.editor.js", "ModelView.html", "uploadmodelversion.dialog.js", "modelversions.view.js"),
    "modelversions.view.js" => array("view.js", "businessmodel.js", "ModelVersionsView.html", "viewmodelversions.lang.js", "modelversion.view.js"), 
    "modelversion.view.js" => array("view.js", "businessmodel.js", "viewmodelversion.lang.js", "ModelVersionView.html")    
  );

  //---------------------------------------------------------------
  $Library[PATH] = "../src/lib/";
  $Library[FILES] = array(
    "common.lib.js" => SINGLE,
    "exception.lib.js" => SINGLE,
    "base.js" => SINGLE,
    "rsh.js" => SINGLE,
    "validation.lib.js" => SINGLE,
    "pooler.js" => SINGLE,
    "tasksqueque.js" => SINGLE
  );
    
  //---------------------------------------------------------------
  $ResourcesStyles[PATH] = "../src/resources/styles/";     
  $ResourcesStyles[FILES] = array(
    "common.css" => SINGLE,
    "components.css" => array("common.css"),
    "Desktop.css" => array("common.css", "components.css"),
    "layout.css" => array("Desktop.css"),
    "dialogs.css" => array("layout.css"),    
    "ServersView.css" => array("layout.css", "dialogs.css"),
    "ServerView.css" => array("layout.css", "dialogs.css"),
    "DeploymentView.css" => array("layout.css", "dialogs.css"),
    "FederationView.css" => array("layout.css", "dialogs.css"),
    "SpaceView.css" => array("layout.css", "dialogs.css"),
    "ImportProgressComposite.css" => array("layout.css", "dialogs.css"),
    "ImportResultComposite.css" => array("layout.css", "dialogs.css"),
    "ModelsView.css" => array("layout.css", "dialogs.css"),
    "ModelView.css" => array("layout.css", "dialogs.css"),
    "ImportSpaceDataDialog.css" => array("layout.css", "dialogs.css"),
    "UploadDialog.css" => array("dialogs.css"), 
    "AddServerDialog.css" => array("dialogs.css"),
    "SelectServerDialog.css" => array("dialogs.css"),
    "AddFederationDialog.css" => array("dialogs.css"),
    "AddSpaceDialog.css" => array("dialogs.css"),
    "AddModelDialog.css" => array("dialogs.css"),
    "UploadModelVersionDialog.css" => array("dialogs.css")
  );  

  //---------------------------------------------------------------
  $ResourcesStylesLang[PATH] = "../src/resources/styles/<language>";     
  $ResourcesStylesLang[FILES] = array(); 

  //---------------------------------------------------------------
  $ResourcesStylesExt[PATH] = "../src/resources/styles/ext/";
  $ResourcesStylesExt[FILES] = array();
  
  //---------------------------------------------------------------
  $ResourcesTemplates[PATH] = "../src/resources/templates/";
  $ResourcesTemplates[FILES] = array(
    "Desktop.html" => array("Desktop.css"),      
    "Toolbar.html" => SINGLE,    
    "Table.html" => SINGLE,
    
    "AddServerDialog.html" => array("AddServerDialog.css"),
    "SelectServerDialog.html" => array("SelectServerDialog.css"),
    "AddFederationDialog.html" => array("AddFederationDialog.css"),
    "AddSpaceDialog.html" => array("AddSpaceDialog.css"),
    "ImportSpaceDataDialog.html" => array("ImportSpaceDataDialog.css"),
    "AddModelDialog.html" => array("AddModelDialog.css"),
    "UploadDialog.html" => array("UploadDialog.css"),
    "SaveChangesDialog.html" => SINGLE,
    "UploadModelVersionDialog.html" => array("UploadModelVersionDialog.css"),
    
    "ImportSpaceDataView.html" => SINGLE,    
    "ServersView.html" => array("ServersView.css"),
    "ServerView.html" => array("ServerState.html", "ServerView.css"),
    "ServerState.html" => SINGLE,
    "DeploymentView.html" => array("AddFederationDialog.html", "AddSpaceDialog.html", "DeploymentView.css"),
    "FederationView.html" => array("FederationView.css", "SpacesSection.html"),
    "SpacesSection.html" => SINGLE,    
    "SpaceView.html" => array("SpaceView.css", "ServerSection.html", "FederationSection.html"),
    "ImportProgressComposite.html" => array("ImportProgressComposite.css"),
    "ImportResultComposite.html" => array("ImportResultComposite.css"),
    "ServerSection.html" => SINGLE,
    "FederationSection.html" => SINGLE,
    "ServicesView.html" => SINGLE,
    "ModelsView.html" => array("ModelsView.css"),
    "ModelView.html" => array("ModelView.css"),
    "ModelVersionsView.html" => SINGLE,
    "ModelVersionSpaces.html" => SINGLE,
    "ModelVersionSpace.html" => SINGLE,
    "ModelVersionView.html" => array("ModelVersionSpaces.html", "ModelVersionSpace.html")
  );  

?>
